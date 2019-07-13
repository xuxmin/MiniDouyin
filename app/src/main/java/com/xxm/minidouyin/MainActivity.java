package com.xxm.minidouyin;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.xxm.minidouyin.personalCenter.PersonalCenterFragment;
import com.xxm.minidouyin.videoRecyclerView.VideoListFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private String user_name = "Rain";

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

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contextFrameLayout, new VideoListFragment())
                .commit();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
