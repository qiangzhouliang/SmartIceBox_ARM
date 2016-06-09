package com.friendlyarm.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.friendlyarm.LEDDemo.R;
import com.friendlyarm.user.Note;

import cn.bmob.v3.listener.UpdateListener;

public class NoteDetailActivity extends AppCompatActivity {
    private EditText editText_name,editText_shelf_life,editText_num,editText_remark;
    private String objectId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_shelf_life = (EditText) findViewById(R.id.editText_shelf_life);
        editText_num = (EditText) findViewById(R.id.editText_num);
        editText_remark = (EditText) findViewById(R.id.editText_remark);

        objectId = getIntent().getStringExtra("objectId");
        editText_name.setText(getIntent().getStringExtra("name"));
        editText_shelf_life.setText(getIntent().getStringExtra("shelf_life"));
        editText_num.setText(getIntent().getStringExtra("num"));
        editText_remark.setText(getIntent().getStringExtra("remark"));

    }

    //返回键自动保存
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Note note = new Note();
            String name = editText_name.getText().toString();
            String shelf_life = editText_shelf_life.getText().toString();
            String numm = editText_num.getText().toString();
            int num = 0;
            if(!numm.isEmpty()) {
                num = Integer.parseInt(numm);
            }
            String remark = editText_remark.getText().toString();
            if(!TextUtils.isEmpty(name)){
                note.setFoodname(name);
                note.setShelf_life(shelf_life);
                note.setNum(num);
                note.setRemark(remark);
                //更新数据
                note.update(this, objectId, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        //保存成功
                        Toast.makeText(NoteDetailActivity.this,"修改数据成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(NoteDetailActivity.this,"修改数据失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
