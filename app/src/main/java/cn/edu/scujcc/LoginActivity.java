package cn.edu.scujcc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;


public class LoginActivity<MyPreference> extends AppCompatActivity {
    private Button loginButton;
    private String user;
    private cn.edu.scujcc.MyPreference prefs = cn.edu.scujcc.MyPreference.getInstance();
    private final static String TAG = "DianDian";
    private UserLab lab = UserLab.getInstance();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case UserLab.USER_LOGIN_SUCCESS:
                        loginSuccess(msg.obj);
                        break;
                    case UserLab.USER_LOGIN_PASSWORD_ERROR:
                        loginPasswordError();
                        break;
                    case UserLab.USER_LOGIN_NET_ERROR:
                        loginFailed();
                        break;
                }
            }
        }
    };

    private void loginPasswordError() {
        Toast.makeText(LoginActivity.this,
                "密码错误，请重试！",
                Toast.LENGTH_LONG)
                .show();
    }

    private void loginSuccess(Object token) {
        Toast.makeText(LoginActivity.this,
                "登录成功！",
                Toast.LENGTH_LONG)
                .show();
        Log.d(TAG ,"服务器返回的token是：" + token);
       prefs.saveUser(user,(String)token);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void loginFailed() {
        Toast.makeText(LoginActivity.this,
                "服务器错误，请稍候再试！",
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            TextInputLayout username = findViewById(R.id.login_username);
            TextInputLayout password = findViewById(R.id.login_password);
            user = username.getEditText().getText().toString();
            String p = password.getEditText().getText().toString();
            // 调用Retrofit
            lab.login(user, p, handler);
        });



        //跳转注册页面
        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        });


       prefs.setup(getApplicationContext());

    }



}