package com.xxm.minidouyin.videoRecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xxm.minidouyin.R;
import com.xxm.minidouyin.VideoActivity;
import com.xxm.minidouyin.model.Video;
import com.xxm.minidouyin.personalCenter.PersonalCenterFragment;

import java.util.List;

public class VideoListFragment extends Fragment {

    private RecyclerView mRecycleView;
    private VideoListAdapter mAdapter;
    private View view;
    // private List<Video> mVideos = new ArrayList<>();
    private final String TAG = "VideoListFragment";

    public VideoListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_video_list, container, false);

        Log.d(TAG, "onCreateView");
        // 设置 RecyclerView
        initRecyclerView();

        // 初始化 数据
        initData();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private void initRecyclerView() {
        mRecycleView = (RecyclerView)view.findViewById(R.id.rc_videolist);
        // 创建 Adapter, 传入数据和上下文
        mAdapter = new VideoListAdapter(getActivity(), getActivity().getSupportFragmentManager());
        // 给 RecyclerView 设置 Adapter
        mRecycleView.setAdapter(mAdapter);
        // 设置 layoutManager
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter.notifyDataSetChanged();
    }

    private void initData() {

        new Thread() {
            @Override
            public void run() {
                VideoFactory.refresh();

                List<Video> mVideos = VideoFactory.getAllVideos();

                mAdapter.setVideoList(mVideos);

                Log.d(TAG, String.valueOf(mVideos.size()));

                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"刷新成功",Toast.LENGTH_SHORT).show();
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }

            }
        }.start();

    }
}
