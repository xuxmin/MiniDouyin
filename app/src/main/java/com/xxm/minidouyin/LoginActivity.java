package com.xxm.minidouyin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.xxm.minidouyin.api.IMyUserService;
import com.xxm.minidouyin.model.User;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mUsernmae, mPassword;
    private Button mLoginButton;
    private Retrofit retrofit;
    private IMyUserService myUserService;

    private static String TAG = "LoginActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolBar();
        initBtn();
    }

    private void initToolBar() {
        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        //关键下面两句话，设置了回退按钮，及点击事件的效果
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initBtn() {
        mLoginButton = findViewById(R.id.login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d(TAG, "button click");
                if (isValidPassword() && isValidUsername()) {
                    final String username = mUsernmae.getText().toString();
                    final String password = mPassword.getText().toString();
                    Log.d(TAG, "DEBUG*"+username+password);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Response<User.LoginResponse> response =
                                        getMyUserService()
                                                .authenticate(new User(username, password))
                                                .execute();
                                Log.d(TAG, response.body().toString());
                                Boolean isSuccess = response.body().getIsSuccess();


                                if (isSuccess) {
                                    User user = response.body().getUser();
                                    Log.d(TAG, user.toString());
                                    // 存储登录的信息
                                    SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username", user.getUserName());
                                    editor.putString("password", user.getPassword());
                                    // 默认的 user_id 与 nickname
                                    editor.putString("user_id", "**********");
                                    editor.putString("nickname", user.getUserName());
                                    editor.apply();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "登录失败, 用户名和密码均为 test11", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }


                            } catch (Exception e) {
                                Log.d(TAG, e.getMessage());
                            }

                        }
                    }.start();
                } else {
                    Log.d(TAG, "无效的用户名密码");
                    Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }


            }
        });

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

    private Boolean isValidUsername() {
        mUsernmae = findViewById(R.id.username);

        if (mUsernmae.getText().toString().trim().isEmpty()) {
            return false;
        }
        else {
            return true;
        }
    }

    private Boolean isValidPassword() {
        mPassword = findViewById(R.id.password);

        if (mPassword.getText().toString().trim().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}


