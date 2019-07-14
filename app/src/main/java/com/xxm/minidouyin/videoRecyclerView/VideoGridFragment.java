package com.xxm.minidouyin.videoRecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xxm.minidouyin.R;
import com.xxm.minidouyin.model.Video;

import java.util.List;

public class VideoGridFragment extends Fragment {
    private RecyclerView mRecycleView;
    private VideoGridAdapter mAdapter;
    private View view;
    private static final String KEY_EXTRA_USER = "extra_user";
    private final String TAG = "VideoGridFragment";

    public VideoGridFragment () {

    }

    // Fragment中创建静态newInstance方法，使用setArguments()方法构造参数
    public static VideoGridFragment newInstance(String user) {
        VideoGridFragment cf = new VideoGridFragment();
        Bundle args = new Bundle();				// bundle ???
        args.putString(KEY_EXTRA_USER, user);	// 设置 bundle 参数
        cf.setArguments(args);
        return cf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 解析参数
        String user = null;
        Bundle args = getArguments();
        if (args != null) {
            user = args.getString(KEY_EXTRA_USER);
        }

        view = inflater.inflate(R.layout.fragment_video_gird, container, false);

        initRecyclerView();

        initData(user);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initRecyclerView() {
        mRecycleView = (RecyclerView)view.findViewById(R.id.rc_videogrid);
        // 创建 Adapter, 传入数据和上下文
        mAdapter = new VideoGridAdapter(getActivity());
        // 给 RecyclerView 设置 Adapter
        mRecycleView.setAdapter(mAdapter);
        // 设置 GridLayoutManager
        mRecycleView.setLayoutManager(new GridLayoutManager(getContext(),3));

        mAdapter.notifyDataSetChanged();
    }

    private void initData(final String user) {

        if (user == null) {
            Log.d(TAG, "user == null");
        } else {
            Log.d(TAG, user);
        }

        new Thread() {
            @Override
            public void run() {
                VideoFactory.refresh();

                List<Video> mVideos = VideoFactory.getAllVideos(user);

                Log.d(TAG, String.valueOf(mVideos));

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
