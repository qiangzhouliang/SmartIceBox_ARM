package com.friendlyarm.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.friendlyarm.LEDDemo.R;
import com.friendlyarm.user.Note;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

public class NoteListActivity extends AppCompatActivity {

    private static final int DEL_ITEM = 0x1;

    //放note
    private List<Note> notes = new ArrayList<>();
    private ListView listView;
    private NoteAdapter na;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_note_list);//activity_note_list

        listView = (ListView) findViewById(R.id.listView_note);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView_name = (TextView) view.findViewById(R.id.textView_name);
                TextView textView_shelf_life = (TextView) view.findViewById(R.id.textView_shelf_life);
                TextView textView_num = (TextView) view.findViewById(R.id.textView_num);
                TextView textView_remark = (TextView) view.findViewById(R.id.textView_remark);


                String objectId = (String) view.getTag();//取出ID
                String name = textView_name.getText().toString();//取出内容
                String shelf_life = textView_shelf_life.getText().toString();
                String num = textView_num.getText().toString() + "";
                String remark = textView_remark.getText().toString();

                Intent intent = new Intent(NoteListActivity.this, NoteDetailActivity.class);
                intent.putExtra("objectId", objectId);
                intent.putExtra("name", name);
                intent.putExtra("shelf_life", shelf_life);
                intent.putExtra("num", num);
                intent.putExtra("remark", remark);
                startActivity(intent);
            }
        });
        //上下文菜单(实现长按删除的功能)
        registerForContextMenu(listView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");
        //从后台加载数据
        loadData();

    }

    //从后台取出数据
    private void loadData() {
        //直接查询
        BmobQuery<Note> query = new BmobQuery<>();
        query.setLimit(50);//限定条数（默认是10条）
        //查询多个
        query.findObjects(this, new FindListener<Note>() {
            @Override
            public void onSuccess(List<Note> list) {
                notes = list;
                na = new NoteAdapter(NoteListActivity.this,notes);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(na);
                    }
                });

                System.out.println(notes.size());
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    //自定义Note适配器（静态内部类）
    class NoteAdapter extends BaseAdapter {

        private Context context;
        private List<Note> list;
        //传递参数
        public NoteAdapter(Context context,List<Note> list){
            this.context = context;
            this.list = list;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(context).inflate(R.layout.layout, null);
            final Note note = list.get(i);
            String id = note.getObjectId();//拿到id
            //获取列表布局
            view = LayoutInflater.from(context).inflate(R.layout.layout_note_item, null);

            TextView name = (TextView) view.findViewById(R.id.textView_name);
            TextView num = (TextView) view.findViewById(R.id.textView_num);
            TextView shelf_life = (TextView) view.findViewById(R.id.textView_shelf_life);
            final TextView remark = (TextView) view.findViewById(R.id.textView_remark);
            //是这一个点击事件
            remark.setOnClickListener(new View.OnClickListener() {
                Boolean flag = true;
                @Override
                public void onClick(View v) {
                    if(flag){

                        flag = false;
                        remark.setSingleLine(flag);
                    }else{
                        flag = true;
                        remark.setSingleLine(flag);
                    }
                }
            });
            final ImageView imageView_icon = (ImageView) view.findViewById(R.id.imageView_iconn);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BmobFile bf = note.getIcon();
                    if (bf != null) {
                        //显示原图
                        //bf.loadImage(this,iv);
                        //缩列图
                        bf.loadImageThumbnail(NoteListActivity.this, imageView_icon, 36, 36, 100);
                    }
                }
            }).start();
            name.setText(note.getFoodname());//设置显示内容
            String str = note.getNum() + "";
            num.setText(str);
            shelf_life.setText(note.getShelf_life());
            remark.setText(note.getRemark());

            view.setTag(note.getObjectId());//保存ID
            return view;
        }
    }

    //菜单被选中时
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_note:
                Intent intent = new Intent(NoteListActivity.this,NoteNewActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    //创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_list,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //创建上下文菜单(删除菜单)长按删除
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //参数（groupID,自己的id,排序，文本内容）
        menu.add(1,DEL_ITEM,100,"删除");//添加一个删除按钮
    }
    //点击监听事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case DEL_ITEM:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                View view = info.targetView;//就是当前目标（点击的目标）
                //测试
                System.out.println(info);
                String objectId = (String) view.getTag();
                Note note = new Note();
                note.delete(this, objectId, new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        loadData();//重新更新一下
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
                break;
        }
        return super.onContextItemSelected(item);
    }
}
