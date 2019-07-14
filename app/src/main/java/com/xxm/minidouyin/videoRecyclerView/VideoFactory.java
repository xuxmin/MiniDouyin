package com.xxm.minidouyin.videoRecyclerView;

import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.constraintlayout.solver.LinearSystem;

import com.xxm.minidouyin.api.IMiniDouyinService;
import com.xxm.minidouyin.model.Video;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoFactory {

    private static List<Video> mVideos = new ArrayList<>();
    private static Retrofit retrofit;
    private static IMiniDouyinService miniDouyinService;


    private static String TAG = "VideoFactory";

    public static List<Video> getAllVideos() {
        return mVideos;
    }

    public static List<Video> getAllVideos(String user) {

        List<Video> videos = new ArrayList<>();

        try {
            for (int i = 0; i<mVideos.size(); i++) {
                if (mVideos.get(i).getUserName().equals(user)) {
                    videos.add(mVideos.get(i));
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "IndexOutOfBoundsException");
        }

        return videos;
    }

    // public static Boolean isSuccess = false;

    public static void refresh() {
        try {
            fetchFeed();
        } catch (Exception e) {
            Log.d(TAG, "refresh failed");
        }
    }

    private static void fetchFeed() {


        Call<Video.GetVideoResponse> call = getVideoService().getVideos();

        try {
            List<Video> videos = call.execute().body().getVideos();
            mVideos.clear();
            for (int i = 0; i < videos.size(); i++) {
                mVideos.add(videos.get(i));
            }
            Log.d(TAG, String.valueOf(videos.size()));

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private static IMiniDouyinService getVideoService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(IMiniDouyinService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())         // 可以将对象转为json
                    .build();
        }
        if (miniDouyinService == null) {
            miniDouyinService = retrofit.create(IMiniDouyinService.class);
        }
        return miniDouyinService;
    }
}
