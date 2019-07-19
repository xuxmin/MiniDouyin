package com.xxm.minidouyin;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.xxm.minidouyin.IJKPlayer.VideoPlayerIJK;
import com.xxm.minidouyin.IJKPlayer.VideoPlayerListener;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class IJKPlayerActivity extends Activity {
    VideoPlayerIJK ijkPlayer = null;
    ImageButton btnSetting;
    ImageButton btnPlay;
    SeekBar seekBar;
    TextView tvTime;
    TextView tvLoadMsg;
    ProgressBar pbLoading;
    RelativeLayout rlLoading;
    TextView tvPlayEnd;
    RelativeLayout rlPlayer;

    int mVideoWidth = 0;
    int mVideoHeight = 0;

    private boolean isPortrait = true;
    private boolean isPlay = false;

    private Handler handler;
    public static final int MSG_REFRESH = 1001;

    private boolean menu_visible = false;
    RelativeLayout rl_bottom;
    boolean isPlayFinish = false;

    private static String TAG = "IJKPlayerActivity";

    private String url;

    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Log.d(TAG, "onCreate");

        url = getIntent().getStringExtra("url");
        Log.d(TAG, url);

        lottieAnimationView = findViewById(R.id.animation_view);
        lottieAnimationView.setVisibility(View.GONE);

        init();
        initIJKPlayer();
    }

    public static void launch(Context context, String url) {
        Intent intent = new Intent(context, IJKPlayerActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // 全屏
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private boolean waitDouble = true;
    private static final int DOUBLE_CLICK_TIME = 350; //两次单击的时间间隔

    private void init() {
        btnPlay = findViewById(R.id.btn_play);
        seekBar = findViewById(R.id.seekBar);
//        btnSetting = findViewById(R.id.btn_setting);

        rl_bottom = findViewById(R.id.include_play_bottom);
        rl_bottom.setVisibility(View.GONE);
        VideoPlayerIJK ijkPlayerView = findViewById(R.id.ijkPlayer);

        tvTime = findViewById(R.id.tv_time);
        tvLoadMsg = findViewById(R.id.tv_load_msg);
        pbLoading = findViewById(R.id.pb_loading);
        rlLoading = findViewById(R.id.rl_loading);
        tvPlayEnd = findViewById(R.id.tv_play_end);
        rlPlayer = findViewById(R.id.rl_player);

        ijkPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(waitDouble){
                    waitDouble = false;
                    Thread thread = new Thread(){
                        @Override
                        public void run(){
                            try {
                                sleep(DOUBLE_CLICK_TIME);
                                if(!waitDouble){
                                    waitDouble = true;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            singleClick();
                                        }
                                    });
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                }else{
                    waitDouble = true;
                    doubleClick();
                }
            }
        });


        // btnSetting.setOnClickListener(this);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPlay) {
                    ijkPlayer.pause();
                    btnPlay.setImageResource(R.drawable.pause);
                    isPlay = true;
                } else {
                    ijkPlayer.start();
                    btnPlay.setImageResource(R.drawable.play);
                    isPlay = false;
                    handler.sendEmptyMessageDelayed(MSG_REFRESH, 100);
                }
            }
        });

        btnPlay.setImageResource(R.drawable.play);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //进度改变
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //开始拖动
                handler.removeCallbacksAndMessages(null);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //停止拖动
                // 根据 ijkPlayer 设置播放进度
                ijkPlayer.seekTo(ijkPlayer.getDuration() * seekBar.getProgress() / 100);

                //
                handler.sendEmptyMessageDelayed(MSG_REFRESH, 100);
            }
        });

        // 实现 handler 捕获消息时的响应
        handler = new

                Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case MSG_REFRESH:
                                if (ijkPlayer.isPlaying()) {
                                    refresh();
                                    handler.sendEmptyMessageDelayed(MSG_REFRESH, 50);
                                }

                                break;
                        }

                    }
                }

        ;
    }

    private void refresh() {
        // 根据 ijkPlayer，设置 SeekBar 与 TextView 显示的进度
        long current = ijkPlayer.getCurrentPosition() / 1000;
        long duration = ijkPlayer.getDuration() / 1000;
        long current_second = current % 60;
        long current_minute = current / 60;
        long total_second = duration % 60;
        long total_minute = duration / 60;
        String time = current_minute + ":" + current_second + "/" + total_minute + ":" + total_second;
        tvTime.setText(time);
        if (duration != 0) {
            seekBar.setProgress((int) (current * 100 / duration));
        }
    }

    private void initIJKPlayer() {
        //加载native库
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            this.finish();
        }

        ijkPlayer = findViewById(R.id.ijkPlayer);
        ijkPlayer.setListener(new VideoPlayerListener());

        // -----
        //ijkPlayer.setVideoResource(R.raw.yuminhong);
        // ijkPlayer.setVideoResource(R.raw.big_buck_bunny);
        Log.d(TAG, url);
        ijkPlayer.setVideoPath(url);
            /*ijkPlayer.setVideoResource(R.raw.big_buck_bunny);
            ijkPlayer.setVideoPath("https://media.w3.org/2010/05/sintel/trailer.mp4");
            ijkPlayer.setVideoPath("http://vjs.zencdn.net/v/oceans.mp4");*/
        // ----


        ijkPlayer.setListener(new VideoPlayerListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            }

            @Override
            public void onCompletion(IMediaPlayer mp) {
                seekBar.setProgress(100);

                btnPlay.setImageResource(R.drawable.pause);
                isPlay = false;
            }

            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                return false;
            }

            @Override
            public void onPrepared(IMediaPlayer mp) {
                refresh();
                handler.sendEmptyMessageDelayed(MSG_REFRESH, 50);
                isPlayFinish = false;
                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();

                Log.d(TAG, "VideoHeight:" + mVideoHeight + " VideoWidth: " + mVideoWidth);
                videoScreenInit();
                mp.start();
                rlLoading.setVisibility(View.GONE);
            }

            @Override
            public void onSeekComplete(IMediaPlayer mp) {

            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        ijkPlayer.setVideoPath(url);
        Log.d(TAG, "onResume");
        handler.sendEmptyMessageDelayed(MSG_REFRESH, 10);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        // 停止播放器
        if (ijkPlayer != null && ijkPlayer.isPlaying()) {
            ijkPlayer.stop();
        }
        IjkMediaPlayer.native_profileEnd();
        // 移除消息，防止内存泄漏
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestory");
        if (ijkPlayer != null) {
            ijkPlayer.stop();
            ijkPlayer.release();
            ijkPlayer = null;
        }

        super.onDestroy();
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.ijkPlayer:
//                singleClick();
//                break;
//            case R.id.btn_setting:
//                toggle();       // 横竖屏设置
//                break;
//            case R.id.btn_play:
//
//                if (!isPlay) {
//                    ijkPlayer.pause();
//                    btnPlay.setImageResource(R.drawable.pause);
//                    isPlay = true;
//                } else {
//                    ijkPlayer.start();
//                    btnPlay.setImageResource(R.drawable.play);
//                    isPlay = false;
//                    handler.sendEmptyMessageDelayed(MSG_REFRESH, 100);
//                }
//                break;
//        }
//    }


    private void singleClick() {

        if (!menu_visible) {
            rl_bottom.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_bottom);
            rl_bottom.startAnimation(animation);

            menu_visible = true;
        } else {
            rl_bottom.setVisibility(View.INVISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_bottom);
            rl_bottom.startAnimation(animation);

            menu_visible = false;
        }

        if (!isPlay) {
            ijkPlayer.pause();
            btnPlay.setImageResource(R.drawable.pause);
            isPlay = true;
        } else {
            ijkPlayer.start();
            btnPlay.setImageResource(R.drawable.play);
            isPlay = false;
            handler.sendEmptyMessageDelayed(MSG_REFRESH, 100);
        }

        Log.d(TAG, String.valueOf(menu_visible));
//        Toast.makeText(this, "single",Toast.LENGTH_SHORT).show();
    }

    private void doubleClick() {
        //        Toast.makeText(this, "double",Toast.LENGTH_SHORT).show();
        lottieAnimationView.setVisibility(View.VISIBLE);

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(lottieAnimationView, "alpha", 1f, 0f);
        animator1.setDuration(1000);

        animatorSet.playTogether(animator1);
        animatorSet.start();
    }


    private void videoScreenInit() {
        if (isPortrait) {
            portrait();
        } else {
            lanscape();
        }
    }

    private void toggle() {
        if (!isPortrait) {
            portrait();
        } else {
            lanscape();
        }
    }

    private void portrait() {
        ijkPlayer.pause();
        isPortrait = true;
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);

        float width = wm.getDefaultDisplay().getWidth();
        float height = wm.getDefaultDisplay().getHeight();

        Log.d(TAG, "width:" + width + " height:" + height);

        float ratio = width / height;
        if (width < height) {
            ratio = height / width;
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlPlayer.getLayoutParams();
        layoutParams.height = (int) (mVideoWidth * ratio);
        layoutParams.width = (int) width;
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        rlPlayer.setLayoutParams(layoutParams);

//        btnSetting.setImageResource(R.drawable.portrait);

        ijkPlayer.start();
    }

    private void lanscape() {
        ijkPlayer.pause();
        isPortrait = false;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        float width = wm.getDefaultDisplay().getWidth();
        float height = wm.getDefaultDisplay().getHeight();
        float ratio = width / height;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlPlayer.getLayoutParams();

        layoutParams.height = (int) RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.width = (int) RelativeLayout.LayoutParams.MATCH_PARENT;
        rlPlayer.setLayoutParams(layoutParams);

        btnSetting.setImageResource(R.drawable.lanscape);

        ijkPlayer.start();
    }
}
