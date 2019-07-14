package com.xxm.minidouyin.videoRecyclerView;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.xxm.minidouyin.model.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter {

    private List<Video> mVideoList =new ArrayList<>();
    private int selectIndex;
    private Context context;
    private FragmentManager fragmentManager;

    public VideoListAdapter(Context context, FragmentManager fragmentManager) {
        // 构造函数传递 上下文和数据
        this.context = context;
        // this.mVideoList = videoList;
        this.fragmentManager = fragmentManager;

    }

    // create ViewHolder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 创建每一项的自定义布局
        return VideoListViewHolder.create(parent.getContext(), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // bind data
        final Video video = mVideoList.get(position);
        ((VideoListViewHolder) holder).bind(this.context,this.fragmentManager, video);
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public void setVideoList(List<Video> list) {
        if (!checkListNonNull(list)) {
            return;
        }
        mVideoList = list;
    }



    private boolean checkListNonNull(List<Video> list) {
        return list != null && list.size() != 0;
    }

}
