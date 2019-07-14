package com.xxm.minidouyin.personalCenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.intrusoft.library.FrissonView;
import com.xxm.minidouyin.LoginActivity;
import com.xxm.minidouyin.R;
import com.xxm.minidouyin.UploadVideoActivity;

public class PersonalCenterFragment extends Fragment {

    private Button mUploadButton;
    private static final String KEY_EXTRA_USER = "extra_user";
    private static final String TAG = "PersonalCenterFragment";
    private TextView mLoginTextView;
    private View view;
    private String LoginUser;
    private String user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 获取当前登录的用户名
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        this.LoginUser = sharedPreferences.getString("username", null);//(key,若无数据需要赋的值)

        // 解析参数, 获得传入的用户名
        user = null;
        Bundle args = getArguments();
        if (args != null) {
            user = args.getString(KEY_EXTRA_USER);
        }

        view = inflater.inflate(R.layout.fragment_personal_center, container, false);

        // 当传入的用户名与登录的用户名不同时，表示浏览他人的个人主页
        if (user != null && !user.equals(LoginUser)) {

        } else if (user != null && user.equals(LoginUser)) { // 当传入的用户名等于登录的用户名，显示上传按钮
            initBtn();
        } else {    // 当传入的用户名为 null 时，表示未登录
            // 不显示上传按钮
        }

        Log.d(TAG, "DEBUG***");
        if (user != null) {
            Log.d(TAG, user);
        }

        // viewPager 显示对应用户的数据
        initViewPager(this.user);

        //
        refreshData(this.user);

        return view;
    }

    private void initViewPager(String user) {
        ViewPager mPager = view.findViewById(R.id.view_pager);
        TabLayout mTabLayout = view.findViewById(R.id.tab_layout);


        SectionPagerAdapter mSectionPagerAdapter = new SectionPagerAdapter(getChildFragmentManager(), user);
        // 设置适配器
        mPager.setAdapter(mSectionPagerAdapter);
        mTabLayout.setupWithViewPager(mPager);
    }

    private void refreshData(String user) {
        mLoginTextView = view.findViewById(R.id.tv_login);

        // 获取当前登录的用户名
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String LoginUser = sharedPreferences.getString("username", null);//(key,若无数据需要赋的值)

        // 当前已登录，且显示的就是登录的用户
        if (LoginUser != null && LoginUser.equals(this.user)) {
            Log.d(TAG, "LoginUser != null && LoginUser.equals(this.user)");
            String nickname = sharedPreferences.getString("nickname", null);//(key,若无数据需要赋的值)

            mLoginTextView.setText(nickname);
            // mLoginTextView.setClickable(false);
            mLoginTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                    startActivity(intent);
                }
            });

        } else if (LoginUser != null && !LoginUser.equals(this.user)) {
            // 当前已登录，显示的不是登录的用户
            mLoginTextView.setText(user);
        } else  if (LoginUser == null && user == null){

            Log.d(TAG, "LoginUser == null && user == null");
            // 当前未登录，而且传入的也是null, 显示个人中心
            mLoginTextView.setText("点我登录");
            mLoginTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            // 当前未登录，显示其他人的主页
            mLoginTextView.setText(user);
        }
    }

    // Fragment中创建静态newInstance方法，使用setArguments()方法构造参数
    public static PersonalCenterFragment newInstance(String user) {
        PersonalCenterFragment cf = new PersonalCenterFragment();
        Bundle args = new Bundle();				// bundle ???
        args.putString(KEY_EXTRA_USER, user);	// 设置 bundle 参数
        cf.setArguments(args);
        return cf;
    }

    private void initBtn() {
        mUploadButton = view.findViewById(R.id.bt_uploadVideo);
        mUploadButton.setVisibility(View.INVISIBLE);
        mUploadButton.setEnabled(true);
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UploadVideoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.d(TAG, "onResume");
//        // 保证登录成功后，返回到个人主页，能够立刻看到修改
//        if (this.LoginUser != null  && this.LoginUser.equals(this.user)) {     // 在个人中心
//            // 获取当前登录的用户名
//            SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
//            this.LoginUser = sharedPreferences.getString("username", null);//(key,若无数据需要赋的值)
//            // 退出登录
//            if (this.LoginUser == null) {
//                this.user = null;
//                refreshData(null);
//            } else {
//                refreshData(this.user);
//            }
//        } else if (this.LoginUser == null && this.user == null) {
//            // 在个人中心，未登录
//            // 获取当前登录的用户名
//            SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
//            this.LoginUser = sharedPreferences.getString("username", null);//(key,若无数据需要赋的值)
//            if (this.LoginUser != null) {
//                // 登录
//                this.user = this.LoginUser;
//                refreshData(this.LoginUser);
//            }
//        }

    }
}
