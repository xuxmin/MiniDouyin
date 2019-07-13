package com.xxm.minidouyin.personalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.intrusoft.library.FrissonView;
import com.xxm.minidouyin.R;
import com.xxm.minidouyin.UploadVideoActivity;
import com.xxm.minidouyin.videoRecyclerView.VideoGridFragment;
import com.xxm.minidouyin.videoRecyclerView.VideoListFragment;

public class PersonalCenterFragment extends Fragment {

    private Button mUploadButton;
    private FrissonView mFrissonView;
    private static final String KEY_EXTRA_USER = "extra_user";
    private static final String TAG = "PersonalCenterFragment";
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 解析参数
        String user = null;
        Bundle args = getArguments();
        if (args != null) {
            user = args.getString(KEY_EXTRA_USER);
        }

        view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        initBtn();
//        initRecyclerView(user);

        ViewPager mPager = view.findViewById(R.id.view_pager);
        TabLayout mTabLayout = view.findViewById(R.id.tab_layout);

//        NestedScrollView mNestScrollView = view.findViewById(R.id.nest_scrollview);
//        mNestScrollView.setFillViewport(true);

        SectionPagerAdapter mSectionPagerAdapter = new SectionPagerAdapter(getChildFragmentManager(), user);
        // 设置适配器
        mPager.setAdapter(mSectionPagerAdapter);
        mTabLayout.setupWithViewPager(mPager);

        return view;
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
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UploadVideoActivity.class);
                startActivity(intent);
            }
        });
    }

//    private void initRecyclerView(String user) {
//        getChildFragmentManager()
//                .beginTransaction()
//                .replace(R.id.layout_videogrid, VideoGridFragment.newInstance(user))
//                .commit();
//    }
}
