package com.friendlyarm.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.friendlyarm.LEDDemo.R;
import com.friendlyarm.Utils.WorkConnected;
import com.friendlyarm.user._User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class LoginMainActivity extends AppCompatActivity {

    private EditText editText_user,editText_password;
    private CheckBox rememberPass;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private WorkConnected workConnected;
    private boolean bool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        workConnected = new WorkConnected();
        editText_user = (EditText) findViewById(R.id.editText_user);
        editText_password = (EditText) findViewById(R.id.editText2_password);
        rememberPass();
        bool = workConnected.isNetworkConnected(this);

    }

    //实现记住密码功能
    private void rememberPass() {
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember){
            //将账号和密码都设置到文本框中
            String account = pref.getString("username","");
            String password = pref.getString("pass","");
            editText_user.setText(account);
            editText_password.setText(password);
            rememberPass.setChecked(true);
        }
    }

    //用户登录
    public void loginClick(View view){
        if(bool){
            String username = editText_user.getText().toString();
            String pass = editText_password.getText().toString();
            if(username.equals(username) && pass.equals(pass)){
                editor = pref.edit();
                //检查复选框是否被选中
                if(rememberPass.isChecked()){
                    editor.putBoolean("remember_password",true);
                    editor.putString("username",username);
                    editor.putString("pass",pass);
                }else {
                    editor.clear();
                }
                editor.commit();
            }
            final BmobUser bmobuser = new BmobUser();
            bmobuser.setUsername(username);
            bmobuser.setPassword(pass);
            bmobuser.login(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    _User user = BmobUser.getCurrentUser(LoginMainActivity.this, _User.class);
                    //邮箱验证是否成功
                    if (user.getEmailVerified()) {
                        Toast.makeText(LoginMainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        //登录成功后
                        Intent intent = new Intent(LoginMainActivity.this, FragmentActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginMainActivity.this, "用户未激活", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int i, String s) {
                    System.out.println("++++"+s);
                    Toast.makeText(LoginMainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this,"网络不可用,请检查网络设置",Toast.LENGTH_SHORT).show();
        }
    }
    //注册
    public void regClick(View view){
        if(bool){
            Intent intent = new Intent(this,RegActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(this,"网络不可用，请检查网络设置",Toast.LENGTH_SHORT).show();
        }

    }
}
