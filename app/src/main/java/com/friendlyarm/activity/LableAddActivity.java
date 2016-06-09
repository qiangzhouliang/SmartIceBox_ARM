package com.friendlyarm.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.friendlyarm.LEDDemo.R;
import com.friendlyarm.Utils.Constant;
import com.friendlyarm.user.Food;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class LableAddActivity extends AppCompatActivity implements View.OnClickListener{
    private String photo = "1.png";

    private EditText editText_id,editText_name,editText_remark;

    private Button button_select,button_save,button_cancel;//选怎图片
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lable_add2);
        editText_id = (EditText) findViewById(R.id.editText_id);
        editText_name = (EditText) findViewById(R.id.editText_foodName);
        editText_remark = (EditText) findViewById(R.id.editText_remark);

        button_select = (Button) findViewById(R.id.button_select);
        button_save = (Button) findViewById(R.id.button_save);
        button_cancel = (Button) findViewById(R.id.button_cancel);

        imageView = (ImageView) findViewById(R.id.imageView);

        button_cancel.setOnClickListener(this);
        button_save.setOnClickListener(this);
        button_select.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_select:
                //选怎照片
                selectPhoto();
                break;
            case R.id.button_save:
                save_food_stub();
                break;
            case R.id.button_cancel:
                finish();
                break;
        }
    }

    private void save_food_stub() {
        //得到图片文件路径
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+photo;
        //图片的上传
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.upload(this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                Food food = new Food();
                String name = editText_name.getText().toString();
                String numm = editText_id.getText().toString();
                int id = 0;
                if(!numm.isEmpty()) {
                    id = Integer.parseInt(numm);
                }
                String remark = editText_remark.getText().toString();

                if(!TextUtils.isEmpty(name)){
                    food.setName(name);
                    food.setId(id);
                    food.setRemark(remark);
                    food.setPhoto(photo);
                    food.setIcon(bmobFile);
                    food.save(LableAddActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            //保存成功
                            Toast.makeText(LableAddActivity.this, "添加数据成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(LableAddActivity.this,"添加数据失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    private void selectPhoto() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LableAddActivity.this);
        builder.setIcon(R.drawable.apple);
        builder.setTitle("请选择相对应的照片：");
        builder.setItems(Constant.photo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < Constant.namePhotos.length; i++) {
                    photo = Constant.namePhotos[which];
                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+photo;
                    Bitmap bm = BitmapFactory.decodeFile(path);
                    imageView.setImageBitmap(bm);//不会变形
                }
            }
        });
        builder.create().show();
    }
}
