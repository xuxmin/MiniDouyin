package com.xxm.minidouyin.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")   private String id;
    @SerializedName("username") private String userName;
    @SerializedName("password") private String password;
    @SerializedName("good_num") private int goodNum;
    @SerializedName("atten_num") private int attenNum;
    @SerializedName("fan_num") private int fanNum;


    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getStudentId() {
//        return this.studentId;
//    }
//
//    public void setStudentId(String studentId) {
//        this.studentId = studentId;
//    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGoodNum(int goodNum) {
        this.goodNum = goodNum;
    }

    public int getGoodNum() {
        return this.goodNum;
    }

    public void setAttenNum(int attenNum) {
        this.attenNum = attenNum;
    }

    public int getAttenNum() {
        return this.attenNum;
    }

    public void setFanNum(int fanNum) {
        this.fanNum = fanNum;
    }

    public int getFanNum() {
        return this.fanNum;
    }


    @Override
    public String toString() {
        return "User: " + userName;
    }

    public class LoginResponse {
        @SerializedName("isSuccess") private Boolean isSuccess;
        @SerializedName("msg") private  String msg;
        @SerializedName("user") private User user;

        public Boolean getIsSuccess() {
            return this.isSuccess;
        }

        public void setIsSuccess(Boolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public String getMsg() {
            return this.msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public User getUser() {
            return this.user;
        }

        public void serUser(User user) {
            this.user = user;
        }

        @Override
        public String toString() {
            return " isSuccess: " + String.valueOf(isSuccess)
                    + " msg: " + msg
                    + " User: " + user.toString();
        }
    }
}
