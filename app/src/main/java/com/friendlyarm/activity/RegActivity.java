package com.friendlyarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.friendlyarm.LEDDemo.R;
import com.friendlyarm.Utils.Load;
import com.friendlyarm.user._User;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class RegActivity extends AppCompatActivity implements Load.LoadCompare{

    private EditText editText_name,editText_password,editText_email,phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_password = (EditText) findViewById(R.id.editText2_password);
        editText_email = (EditText) findViewById(R.id.editText_email);
        phoneNumber = (EditText) findViewById(R.id.phone_num);
    }

    public void registerClick(View view){
        final String photo = "1.png";
        final String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + photo;
        final File file = new File(path);
        if(!file.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Load.load_Photo(RegActivity.this, photo);
                }
            }).start();
        }else {
            reg(path);
        }
    }

    @Override
    public void strPath(String path) {
        //注册方法
        reg(path);
    }

    private void reg(String path) {
        final String name = editText_name.getText().toString();
        final String pass = editText_password.getText().toString();
        final String email = editText_email.getText().toString();
        final String phone = phoneNumber.getText().toString();
        //图片的上传
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.upload(this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                //上传成功
                _User user = new _User();
                user.setUsername(name);
                user.setPassword(pass);
                user.setEmail(email);
                user.setIcon(bmobFile);
                user.setMobilePhoneNumber(phone);
                //注册
                user.signUp(RegActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {

                        Toast.makeText(RegActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegActivity.this, LoginMainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        System.out.println("==="+s);
                        Toast.makeText(RegActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                System.out.println("==="+s);
            }
        });
    }
}

