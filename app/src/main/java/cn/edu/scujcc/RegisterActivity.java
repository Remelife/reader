package cn.edu.scujcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;



public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "YueYue";
    TextInputLayout birthdayInput;
    private Button registerButton;
    private Date birthday = new Date();
    private UserLab lab = UserLab.getInstance();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg != null) {
                switch (msg.what) {
                    case UserLab.USER_REGISTER_SUCCESS:
                        Toast.makeText(RegisterActivity.this, "注册成功！欢迎你的加入！", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case UserLab.USER_REGISTER_FAIL:
                        Toast.makeText(RegisterActivity.this, "注册失败！请稍候再试。", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //新建一个Builder
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        //告诉builder我们要的效果
        builder.setTitleText(R.string.birthday_title);
        //告诉builder 开工
        MaterialDatePicker<Long> picker = builder.build();
        picker.addOnPositiveButtonClickListener(s -> {
            String date = picker.getHeaderText();
            birthdayInput.getEditText().setText(date);
        });

        //点击图标弹出日历
        birthdayInput = findViewById(R.id.register_birthday);
        birthdayInput.setEndIconOnClickListener(v -> {
            Log.d(TAG,"生日图标被点击了");
            picker.show(getSupportFragmentManager(), picker.toString());
        });

        //zheli
        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> {
            register();
        });
        //zgeli

    }





    private void register() {
        User u = new User();
        boolean error = false;
        String errorMessage;
        //获得用户名
        TextInputLayout usernameInput = findViewById(R.id.login_username);
        Editable username = usernameInput.getEditText().getText();
        u.setUsername(username != null ? username.toString() : "");

        //检查密码是否一致
        TextInputLayout passwordInput1 = findViewById(R.id.register_password);
        TextInputLayout passwordInput2 = findViewById(R.id.register_confirm_password);
        Editable password1 = passwordInput1.getEditText().getText();
        Editable password2 = passwordInput2.getEditText().getText();
        if (password1 != null && password2 != null) {
            if (!password2.toString().equals(password1.toString())) { //两次密码不相同
                error = true;
                errorMessage = "两次密码不相同";
            } else {
                u.setPassword(password1.toString());
            }
        }

        //获得手机号
        TextInputLayout phoneInput = findViewById(R.id.register_phone);
        Editable phone = usernameInput.getEditText().getText();
        u.setPhone(phone != null ? phone.toString() : "");

        //获得性别
        RadioGroup genderGroup = findViewById(R.id.register_gender);
        int gender = genderGroup.getCheckedRadioButtonId();
        switch (gender) {
            case R.id.register_male:
                u.setGender("男");
                break;
            case R.id.register_female:
                u.setGender("女");
                break;
            default:
                u.setGender("保密");
        }

        //获得生日
        u.setBirthday(birthday);

        //把u发送到服务器
        lab.register(u, handler);
    }



    }
