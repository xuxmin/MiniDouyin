package com.xxm.minidouyin.data;

import android.util.Log;

import com.xxm.minidouyin.api.IMyUserService;
import com.xxm.minidouyin.data.model.LoggedInUser;
import com.xxm.minidouyin.model.User;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private Retrofit retrofit;
    private IMyUserService myUserService;

    private static String TAG = "LoginDataSource";

    public Result<LoggedInUser> login(String username, String password) {
        final String tmpUserName = username;
        final String tmpPassword = password;
        try {
            // TODO: handle loggedInUser authentication

            Response<User.LoginResponse> response =
                            getMyUserService()
                                    .authenticate(new User(tmpUserName, tmpPassword))
                                    .execute();
            Log.d(TAG, response.toString());

            Boolean isSuccess = response.body().getIsSuccess();
            Log.d(TAG, response.body().toString());

            User user = response.body().getUser();

            Log.d(TAG, String.valueOf(isSuccess));

            if (isSuccess) {
                LoggedInUser LoginUser =
                        new LoggedInUser(
                                user.getId(),
                                username);
                return new Result.Success<>(LoginUser);
            } else {
                return new Result.Error(new Exception("Error login"));
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    private IMyUserService getMyUserService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(IMyUserService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())         // 可以将对象转为json
                    .build();
        }
        if (myUserService == null) {
            myUserService = retrofit.create(IMyUserService.class);
        }
        return myUserService;
    }
}
