package com.xxm.minidouyin.videoRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xxm.minidouyin.IJKPlayerActivity;
import com.xxm.minidouyin.IjkPlayerTextureViewActivity;
import com.xxm.minidouyin.R;
import com.xxm.minidouyin.model.Video;
import com.xxm.minidouyin.util.ImageHelper;

public class VideoGridViewHolder extends RecyclerView.ViewHolder {

    public ImageView img;

    public VideoGridViewHolder(@NonNull View itemView) {
        super(itemView);
        // layout 中的 view 实例化
        img = itemView.findViewById(R.id.img_grid);
    }

    public static VideoGridViewHolder create(Context context, ViewGroup root) {
        // 新建一个 layout
        View v = LayoutInflater.from(context).inflate(R.layout.video_grid_item_view, root, false);
        return new VideoGridViewHolder(v);
    }

    public void bind(final Context context, final Video video) {
        // 将数据 video 绑定到 layout 中
        ImageHelper.displayWebImage(video.getImageUrl(), img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IJKPlayerActivity.launch(context, video.getVideoUrl());
//                IjkPlayerTextureViewActivity.launch(context, video.getVideoUrl());

            }
        });
    }
}
