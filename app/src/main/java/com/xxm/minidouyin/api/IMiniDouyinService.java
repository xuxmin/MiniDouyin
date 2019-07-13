package com.xxm.minidouyin.api;

import com.xxm.minidouyin.model.Video;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface IMiniDouyinService {
    String BASE_URL = "http://test.androidcamp.bytedance.com/mini_douyin/invoke/";

    @Multipart
    @POST("video")
    Call<Video.PostVideoResponse> postVideo(
            @Query("student_id") String studentId,
            @Query("user_name") String userName,
            @Part MultipartBody.Part image, @Part  MultipartBody.Part video
    );

    @GET("video")
    Call<Video.GetVideoResponse> getVideos();
}
