package com.xxm.minidouyin.videoRecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xxm.minidouyin.R;
import com.xxm.minidouyin.VideoActivity;
import com.xxm.minidouyin.model.Video;
import com.xxm.minidouyin.util.ImageHelper;

public class VideoViewHolder extends RecyclerView.ViewHolder {

    public ImageView img;

    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        // layout 中的 view 实例化
        img = itemView.findViewById(R.id.img);
    }

    public static VideoViewHolder create(Context context, ViewGroup root) {
        // 新建一个 layout
        View v = LayoutInflater.from(context).inflate(R.layout.video_item_view, root, false);
        return new VideoViewHolder(v);
    }

    public void bind(final Context context, final Video video) {
        // 将数据 video 绑定到 layout 中
        ImageHelper.displayWebImage(video.getImageUrl(), img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(context, VideoActivity.class);
                // context.startActivity(intent);
                VideoActivity.launch(context, video.getVideoUrl());
            }
        });
    }
}
