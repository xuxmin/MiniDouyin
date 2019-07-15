package com.xxm.minidouyin.videoRecyclerView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xxm.minidouyin.R;
import com.xxm.minidouyin.VideoActivity;
import com.xxm.minidouyin.model.Video;
import com.xxm.minidouyin.personalCenter.PersonalCenterFragment;
import com.xxm.minidouyin.util.ImageHelper;

public class VideoListViewHolder extends RecyclerView.ViewHolder {

    private ImageView img;
    private TextView mAuthorTextView, mDateTextView;

    public VideoListViewHolder(@NonNull View itemView) {
        super(itemView);
        // layout 中的 view 实例化
        img = itemView.findViewById(R.id.img_list);
        mAuthorTextView = itemView.findViewById(R.id.tv_author);
        mDateTextView = itemView.findViewById(R.id.tv_date);
    }

    public static VideoListViewHolder create(Context context, ViewGroup root) {
        // 新建一个 layout
        View v = LayoutInflater.from(context).inflate(R.layout.video_list_item_view, root, false);
        return new VideoListViewHolder(v);
    }

    public void bind(final Context context, final FragmentManager fragmentManager, final Video video) {
        // 将数据 video 绑定到 layout 中
        ImageHelper.displayWebImage(video.getImageUrl(), img);

        final String user_name = video.getUserName();
        mAuthorTextView.setText(user_name);
        mDateTextView.setText(video.getCreatedAt());


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(context, VideoActivity.class);
                // context.startActivity(intent);
                VideoActivity.launch(context, video.getVideoUrl());
            }
        });

        mAuthorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.contextFrameLayout, PersonalCenterFragment.newInstance(user_name))
                        .commit();
            }
        });
    }
}
