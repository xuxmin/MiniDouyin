package com.xxm.minidouyin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.xxm.minidouyin.util.Permission;
import com.xxm.minidouyin.util.Utils;

public class RecordVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private ImageView mVideoImageView;
    private static final int REQUEST_VIDEO_CAPTURE = 1;

    private SharedPreferences sharedPreferences;

    private static final int REQUEST_CODE_LOGIN= 1001;
    private static int REQUEST_CODE_STORAGE_PERMISSION = 1001;

    private static final int REQUEST_EXTERNAL_CAMERA = 101;
    String[] permissions = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        videoView = findViewById(R.id.img);
        findViewById(R.id.btn_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isPermissionsReady(RecordVideoActivity.this, permissions)) {
                    //todo 打开摄像机
                    openVideoRecordApp();
                } else {
                    //todo 权限检查
                    Utils.reuqestPermissions(RecordVideoActivity.this, permissions, REQUEST_EXTERNAL_CAMERA);
                }
            }
        });


        findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getLoginUser() != null) {
                    // 拥有权限才能上传图片
                    if (Permission.isPermissionsReady(RecordVideoActivity.this , permissions)) {
                        Intent intent = new Intent(RecordVideoActivity.this, UploadVideoActivity.class);
                        startActivity(intent);
                    } else {
                        Permission.reuqestPermissions(RecordVideoActivity.this, permissions, REQUEST_CODE_STORAGE_PERMISSION);
                    }
                } else {
                    Intent intent = new Intent(RecordVideoActivity.this, LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_LOGIN);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            //todo 播放刚才录制的视频
            Uri videoUri=intent.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_CAMERA: {
                //todo 判断权限是否已经授予
                if (Utils.isPermissionsReady(this, permissions)) {
                    //todo 打开摄像机
                    openVideoRecordApp();
                }
                break;
            }
        }
    }

    private void openVideoRecordApp() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(takeVideoIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takeVideoIntent,REQUEST_VIDEO_CAPTURE);
        }
    }

    private String getLoginUser() {
        // 获取当前登录的用户名
        return getSharedPreferences().getString("username", null);//(key,若无数据需要赋的值)
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