package com.xxm.minidouyin.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Video {

    @SerializedName("student_id") private String studentId;
    @SerializedName("user_name") private String userName;
    @SerializedName("image_url") private String imageUrl;
    @SerializedName("video_url") private String videoUrl;
    @SerializedName("createdAt") private String createdAt;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public String getFormatCreateAt() {
        String tmp = this.createdAt;
        String date = tmp.split("T")[0].substring(5);
        if (date.substring(0,1).equals("0")) {
            date = date.substring(1);
        }
        String time = tmp.split("T")[1].substring(0,5);
        String hour = time.substring(3,5);
        String minute = time.substring(0,2);
        return date + " " + hour + ":" + minute;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public static class GetVideoResponse {
        @SerializedName("feeds")
        private List<Video> feeds;

        @SerializedName("success")
        private Boolean success;

        public List<Video> getVideos(){
            return feeds;
        }

        public void setVideos(List<Video> videos) {
            this.feeds = videos;
        }

        public Boolean getSuccess(){
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }
    }

    public static class PostVideoResponse {
        @SerializedName("result") private Map<String, String> result;
        @SerializedName("url") private String url;
        @SerializedName("success") private Boolean success;

        public void setResult(Map<String, String> result) {
            this.result = result;
        }

        public Map<String, String> getResult(){
            return this.result;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return this.url;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public Boolean getSuccess() {
            return this.success;
        }

        @Override public String toString() {
            return "response{" +"}" +
                    "result='"  + '\'' +
                    ", url='" + url + '\'' +
                    ", success=" + success.toString() +
                    '}';
        }
    }
}
