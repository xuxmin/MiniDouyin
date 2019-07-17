package com.xxm.minidouyin.personalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.xxm.minidouyin.R;

public class UserInfoActivity extends AppCompatActivity {

    public static final String KEY_EXTRA_USER = "user";
    private String user;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView tvGood, tvAtten, tvFan;
    private Button mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        // 获取传入的用户名参数
        this.user = null;
        Intent i = getIntent();
        this.user = i.getStringExtra(KEY_EXTRA_USER);



        initToolBar();
        initTv();
        initBtn();
        initViewPager();
    }

    private void initToolBar() {
        mToolbar = findViewById(R.id.tb_name);

//        collapsingToolbarLayout = findViewById(R.id.ctb_title);
//        collapsingToolbarLayout.setTitle(user);

        mToolbar.setTitle(user);

        setSupportActionBar(mToolbar);

        //关键下面两句话，设置了回退按钮，及点击事件的效果
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initTv() {
        tvGood = findViewById(R.id.tv_good);
        tvAtten = findViewById(R.id.tv_atten);
        tvFan = findViewById(R.id.tv_fan);
        // TODO 通过API获得用户的信息
        tvGood.setText("0");
        tvAtten.setText("0");
        tvFan.setText("0");
    }

    private void initBtn() {
        // TODO 关注功能

        mButton = findViewById(R.id.bt_add_atten);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initViewPager() {
        ViewPager mPager = findViewById(R.id.view_pager);
        TabLayout mTabLayout = findViewById(R.id.tab_layout);

        SectionPagerAdapter mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), this.user);
        // 设置适配器
        mPager.setAdapter(mSectionPagerAdapter);
        mTabLayout.setupWithViewPager(mPager);
    }
}

