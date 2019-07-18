package com.xxm.minidouyin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.xxm.minidouyin.api.IMiniDouyinService;
import com.xxm.minidouyin.model.Video;
import com.xxm.minidouyin.util.ImageHelper;
import com.xxm.minidouyin.util.Permission;
import com.xxm.minidouyin.util.ResourceUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadVideoActivity extends AppCompatActivity {

    private static final String TAG = "UploadVideoActivity";
    private static final int PICK_IMAGE = 1;
    private static final int PICK_VIDEO = 2;

    private Toolbar mToolbar;
    private ImageButton mBackButton;
    private Button mAddCoverButton, mAddVideoButton, mUploadButton;
    private ImageView mCoverImageView, mVideoImageView;
    ProgressBar mProgressBar;
    private Uri mSelectedImage;
    private Uri mSelectedVideo;
    private Retrofit retrofit;
    private IMiniDouyinService miniDouyinService;

    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mCoverImageView = findViewById(R.id.img_cover);
        mVideoImageView = findViewById(R.id.img_video);

        initBtns();

        clearState();
    }
    private void initBtns() {
        mToolbar = findViewById(R.id.tb_post);
        mAddCoverButton = findViewById(R.id.bt_addCover);
        mAddVideoButton = findViewById(R.id.bt_addVideo);
        mUploadButton = findViewById(R.id.bt_upload);

        mToolbar = findViewById(R.id.tb_post);

        setSupportActionBar(mToolbar);

        // goback
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        // add Cover
        mAddCoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
                mCoverImageView.setWillNotDraw(false);
                ImageHelper.displayLocalImage(mSelectedImage, mCoverImageView);
            }
        });

        // add video
        mAddVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseVideo();
                mVideoImageView.setWillNotDraw(false);
                ImageHelper.displayVideoImage(mSelectedVideo, mVideoImageView);
            }
        });

        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postVideo();
            }
        });
    }


    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE);
    }

    public void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult() called with: requestCode = ["
                + requestCode
                + "], resultCode = ["
                + resultCode
                + "], data = ["
                + data
                + "]");

        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == PICK_IMAGE) {
                mSelectedImage = data.getData();
                Log.d(TAG, "selectedImage = " + mSelectedImage);
                ImageHelper.displayLocalImage(mSelectedImage, mCoverImageView);
                if (mSelectedVideo != null) {
                    mUploadButton.setVisibility(View.VISIBLE);
                    mUploadButton.setEnabled(true);
                }

            } else if (requestCode == PICK_VIDEO) {
                mSelectedVideo = data.getData();
                Log.d(TAG, "mSelectedVideo = " + mSelectedVideo);
                // mBtn.setText(R.string.post_it);
                ImageHelper.displayVideoImage(mSelectedVideo, mVideoImageView);

                if (mSelectedImage != null) {
                    mUploadButton.setVisibility(View.VISIBLE);
                    mUploadButton.setEnabled(true);
                }
            }
        }
    }

    private void postVideo() {
        mUploadButton.setText("POSTING...");
        mUploadButton.setEnabled(false);
        final MultipartBody.Part coverImagePart = getMultipartFromUri("cover_image", mSelectedImage);
        final MultipartBody.Part videoPart = getMultipartFromUri("video", mSelectedVideo);

        new AsyncTask<Object, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Object... objects) {
                try {
                    Response<Video.PostVideoResponse> response = getVideoService()
                            .postVideo(getStrudentId(), getNickname(), coverImagePart, videoPart)
                            .execute();
                    Log.d(TAG, response.body().toString());

                    return response.body().getSuccess();
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean s) {
                super.onPostExecute(s);
                Log.d(TAG, String.valueOf(s));

                // 上传成功
                if (s) {
                    clearState();
                    Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                }
                else {
                    mUploadButton.setText("Upload");
                    mUploadButton.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
                }
            }

        }.execute();


    }
    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        File f = new File(ResourceUtils.getRealPath(UploadVideoActivity.this, uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

    private IMiniDouyinService getVideoService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(IMiniDouyinService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())         // 可以将对象转为json
                    .build();
        }
        if (miniDouyinService == null) {
            miniDouyinService = retrofit.create(IMiniDouyinService.class);
        }
        return miniDouyinService;
    }

    private void clearState() {
        mUploadButton.setText("Upload");
        mUploadButton.setEnabled(false);
        mUploadButton.setVisibility(View.INVISIBLE);
        mCoverImageView.setWillNotDraw(true);
        mVideoImageView.setWillNotDraw(true);
        mSelectedImage = null;
        mSelectedVideo = null;
    }


    private String getNickname() {
        return getSharedPreferences().getString("nickname", null);
    }

    private String getStrudentId() {
        return getSharedPreferences().getString("user_id", null);
    }

    private SharedPreferences getSharedPreferences() {
        if (this.sharedPreferences == null) {
            this.sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
            return this.sharedPreferences;
        } else {
            return this.sharedPreferences;
        }
    }
}
