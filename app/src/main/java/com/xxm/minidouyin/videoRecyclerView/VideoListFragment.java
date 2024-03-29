package com.xxm.minidouyin.videoRecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xxm.minidouyin.R;
import com.xxm.minidouyin.model.Video;
import com.xxm.minidouyin.util.Setting;

import java.util.List;

public class VideoListFragment extends Fragment {

    private RecyclerView mRecycleView;
    private VideoListAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    // private List<Video> mVideos = new ArrayList<>();
    private final String TAG = "VideoListFragment";

    public VideoListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 设置状态栏颜色
        Setting.setStatusBarColor(Color.BLACK, getActivity());


        view = inflater.inflate(R.layout.fragment_video_list, container, false);

        // 设置 RecyclerView
        initRecyclerView();

        // 初始化 数据
        initData();

        initSwipeRefresh();


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private void initRecyclerView() {
        mRecycleView = (RecyclerView)view.findViewById(R.id.rc_videolist);

        mRecycleView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        // 创建 Adapter, 传入数据和上下文
        mAdapter = new VideoListAdapter(getActivity(), getActivity().getSupportFragmentManager());
        // 给 RecyclerView 设置 Adapter
        mRecycleView.setAdapter(mAdapter);
        // 设置 layoutManager
        // mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                            // Toast.makeText(getActivity(),"刷新成功",Toast.LENGTH_SHORT).show();
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }

            }
        }.start();

    }

    private void initSwipeRefresh() {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                VideoFactory.refresh();
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
