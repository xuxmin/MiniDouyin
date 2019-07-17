package com.xxm.minidouyin;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.xxm.minidouyin.personalCenter.PersonalCenterFragment;
import com.xxm.minidouyin.videoRecyclerView.VideoListFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private String user_name;
    private static int REQUEST_CODE_STORAGE_PERMISSION = 1001;


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

        ActivityCompat.requestPermissions( this, new
                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);

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
