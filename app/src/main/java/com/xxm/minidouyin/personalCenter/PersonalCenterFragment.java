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

import com.google.android.material.tabs.TabLayout;
import com.xxm.minidouyin.LoginActivity;
import com.xxm.minidouyin.R;
import com.xxm.minidouyin.UploadVideoActivity;

public class PersonalCenterFragment extends Fragment {

    private Button mUploadButton;
    private static final String KEY_EXTRA_USER = "extra_user";
    private static final String TAG = "PersonalCenterFragment";
    private TextView mLoginTextView;
    private View view;
    private SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 获取当前登录的用户名
        String LoginUser = getLoginUser();

        // 解析参数, 获得传入的用户名(这里实际上用不到)
        String user = null;
        Bundle args = getArguments();
        if (args != null) {
            user = args.getString(KEY_EXTRA_USER);
        }

        view = inflater.inflate(R.layout.fragment_personal_center, container, false);

        initBtn();
        initViewPager();
        refreshData();

        return view;
    }

    private void initViewPager() {
        ViewPager mPager = view.findViewById(R.id.view_pager);
        TabLayout mTabLayout = view.findViewById(R.id.tab_layout);

        SectionPagerAdapter mSectionPagerAdapter = new SectionPagerAdapter(getChildFragmentManager(), getNickname());
        // 设置适配器
        mPager.setAdapter(mSectionPagerAdapter);
        mTabLayout.setupWithViewPager(mPager);

    }

    private void refreshData() {
        mLoginTextView = view.findViewById(R.id.tv_login);

        if (getLoginUser() != null) {
            // 当前已登录
            String nickname = getNickname();

            mLoginTextView.setText(nickname);
            mLoginTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            // 当前未登录
            mLoginTextView.setText("点我登录");
            mLoginTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
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
        // 当有登录用户时，显示上传按钮
        Log.d(TAG, "initBtn");
        Log.d(TAG, String.valueOf(getLoginUser()));
        mUploadButton = view.findViewById(R.id.bt_uploadVideo);
        if (getLoginUser() != null) {
            mUploadButton.setVisibility(View.VISIBLE);
            mUploadButton.setEnabled(true);
            mUploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), UploadVideoActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            mUploadButton.setVisibility(View.INVISIBLE);
            mUploadButton.setEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initBtn();
        initViewPager();
        refreshData();
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


    private String getLoginUser() {
        // 获取当前登录的用户名
        return getSharedPreferences().getString("username", null);//(key,若无数据需要赋的值)
    }

    private String getNickname() {
        return getSharedPreferences().getString("nickname", null);
    }

    private SharedPreferences getSharedPreferences() {
        if (this.sharedPreferences == null) {
            this.sharedPreferences= getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            return this.sharedPreferences;
        } else {
            return this.sharedPreferences;
        }
    }
}
