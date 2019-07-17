package com.xxm.minidouyin.personalCenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xxm.minidouyin.R;

public class PersonalInfoActivity extends AppCompatActivity {

    private EditText mUsernameEditText, mIdEditText, mNickEditText;
    private Toolbar mToolbar;
    private Button mButton, mLogoutBtn;

    private static String TAG_MODIFY = "修改";
    private static String TAG_OK = "确定";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        initToolBar();
        initTv();
        initData();
        initBtn();
    }

    private void initToolBar() {
        mToolbar = findViewById(R.id.tb_info);

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

    private void initTv() {
        mIdEditText = findViewById(R.id.user_id);
        mNickEditText = findViewById(R.id.nickname);
        mUsernameEditText = findViewById(R.id.username);
        mIdEditText.setEnabled(false);
        mNickEditText.setEnabled(false);
        mUsernameEditText.setEnabled(false);
    }

    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);//(key,若无数据需要赋的值)
        String userId = sharedPreferences.getString("user_id", null);
        String nickname = sharedPreferences.getString("nickname", null);
        mIdEditText.setText(userId);
        mUsernameEditText.setText(username);
        mNickEditText.setText(nickname);
    }

    private void initBtn() {
        mButton = findViewById(R.id.bt_modify);
        mLogoutBtn = findViewById(R.id.bt_logout);

        mButton.setText(TAG_MODIFY);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mButton.getText().toString().equals(TAG_MODIFY)) {
                    mButton.setText(TAG_OK);
                    mIdEditText.setEnabled(true);
                    mNickEditText.setEnabled(true);
                } else if (mButton.getText().toString().equals(TAG_OK)) {
                    mButton.setText(TAG_MODIFY);
                    mIdEditText.setEnabled(false);
                    mNickEditText.setEnabled(false);
                    SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("user_id", mIdEditText.getText().toString());
                    editor.putString("nickname", mNickEditText.getText().toString());
                    editor.apply();
                }
            }
        });

        // 退出登录
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.remove("username");
                editor.remove("nickname");
                editor.remove("password");
                editor.remove("user_id");
                editor.apply();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

}
