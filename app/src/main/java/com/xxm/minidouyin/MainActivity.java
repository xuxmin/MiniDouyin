package com.xxm.minidouyin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.xxm.minidouyin.personalCenter.PersonalCenterFragment;
import com.xxm.minidouyin.util.Permission;
import com.xxm.minidouyin.videoRecyclerView.VideoListFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private String user_name;
    private static int REQUEST_CODE_PERMISSION = 1001;

    String[] permissions = new String[] {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contextFrameLayout, new VideoListFragment())
                            .commit();
                    return true;
                case R.id.navigation_take:
                    startActivity(new Intent(MainActivity.this, RecordVideoActivity.class));
                    return true;
                case R.id.navigation_me:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contextFrameLayout, PersonalCenterFragment.newInstance(user_name))
                            .commit();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (! Permission.isPermissionsReady(this, permissions)) {
            //权限检查
            Permission.reuqestPermissions(this, permissions, REQUEST_CODE_PERMISSION);
        }

        // 获取当前登录的用户名
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        this.user_name = sharedPreferences.getString("username", null);//(key,若无数据需要赋的值)


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contextFrameLayout, new VideoListFragment())
                .commit();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
