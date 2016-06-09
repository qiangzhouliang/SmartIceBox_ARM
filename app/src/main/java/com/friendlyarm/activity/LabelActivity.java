package com.friendlyarm.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.friendlyarm.AndroidSDK.HardwareControler;
import com.friendlyarm.LEDDemo.R;
import com.friendlyarm.Utils.Load;
import com.friendlyarm.user.Food;
import com.friendlyarm.user.Note;
import com.friendlyarm.Utils.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class LabelActivity extends AppCompatActivity {

    private static final String TAG = "SerialPort";
    private static final int add = 0;//添加食物
    private static final int delete = 1;//删除食物
    private static final int FLAG_ADD = 2;
    private static final int FLAG_DELETE = 1;

    private static int x = 0;

    private static int FLAG = 0;

    private static String deletename;//删除食品时会用到
    private static String addname;//添加食品时用到

    private final int MAXLINES = 200;
    private StringBuilder remoteData = new StringBuilder(256 * MAXLINES);

    private boolean flag1 = true;//串口读取的标志
    private boolean flag2 = true;//串口读取的标志

    private String devName = "/dev/ttyUSB0";
    private int speed = 115200;
    private int dataBits = 8;
    private int stopBits = 1;
    private int devfd = -1;

    private TextView textView_scan;

    //放food
    private List<Note> notes = new ArrayList<>();
    private ListView listView;
    private NoteAdapter na;

    private String shelf_life;
    private int num;

    //单次查找
    private byte[] cmd1 = {(byte) 0xAA, (byte) 0x00, (byte) 0x22, (byte) 0x00,
            (byte) 0x00, (byte) 0x22, (byte) 0x8E};
    //多次查找
    private byte[] cmd2 = {(byte) 0xAA, (byte) 0x00, (byte) 0x27, (byte) 0x00,
            (byte) 0x03, (byte) 0x22, (byte) 0x27, (byte) 0x10,
            (byte) 0x83, (byte) 0x8E};
    //结束多次查询
    private byte[] cmd3 = {(byte) 0xAA, (byte) 0x00, (byte) 0x28, (byte) 0x00,
            (byte) 0x00, (byte) 0x28, (byte) 0x8E};

    //定义数组，存放食品保质期
    private String[] shelf = new String[]{"15", "30", "45", "60", "75", "90"};
    private int[] food_id = new int[100];
    private String[] label_ID = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
            "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
            "50", "51", "52", "53", "54", "55", "56", "57", "58", "59",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_label);
        Bmob.initialize(this, "7111b1b0ca4263017a2765f3cdeacfd2");

        listView = (ListView) findViewById(R.id.listView2);

        textView_scan = (TextView) findViewById(R.id.textView_scan);

        FLAG = getIntent().getIntExtra("key", 0);
        if (FLAG == FLAG_ADD) {
            addFood();
        } else if (FLAG == FLAG_DELETE) {
            deleteFood();
        }
    }

    //想串口中写入命令
    private void writeSerial(byte[] bytes, int in) {
        int ret = HardwareControler.write(devfd, bytes);
        //测试 看这个命令帧是否完全写入进去
        System.out.println(ret);
        if (ret != in) {
            HardwareControler.write(devfd, bytes);
        }
    }

    @Override
    public void onDestroy() {
        System.out.println("------ onDestroy");
        if (devfd != -1) {
            HardwareControler.close(devfd);
            devfd = -1;
        }
        FLAG = 0;
        flag1 = false;
        flag2 = false;
        x = 0;
        super.onDestroy();
    }

    private final int BUFSIZE = 512;
    private byte[] buf = new byte[BUFSIZE];
    private void readSerialFood(final int flagfood) {
        if (HardwareControler.select(devfd, 0, 0) == 1) {
            int retSize = HardwareControler.read(devfd, buf, BUFSIZE);
            if (retSize > 8) {
                System.out.println("retSize"+retSize);
                //调用转换方法，得到字节数组
                String[] str1 = util.Bytes2HexString(buf, BUFSIZE);
                for (int i = 0; i < str1.length; i++) {
                    if ((str1[i] != null) && str1[i].equals("6E") && str1[i + 8].equals("5E")) {
                        for (int j = 1; j < 3; j++) {
                            if (j == 1) {
                                //第一个代表标签ID，第二个数据代表保质期编号
                                for (int k = 0; k < label_ID.length; k++) {
                                    if (label_ID[k].equals(str1[j])) {
                                        final int finalK = k;
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                loadData(Integer.parseInt(label_ID[finalK]), flagfood);
                                            }
                                        }).start();
                                    }
                                }
                            } else if (j == 2) {
                                shelf_life = shelf[Integer.parseInt(str1[j])];
                            }
                        }
                    }
                }
            }
        }
    }


    private void loadData(int k, final int kk) {
        if (kk == add) {
            String bql = "select * from Food where id = ?";//根据id号查询
            new BmobQuery<Food>().doSQLQuery(LabelActivity.this, bql, new SQLQueryListener<Food>() {
                @Override
                public void done(BmobQueryResult<Food> result, BmobException e) {
                    if (e == null) {
                        List<Food> list = (List<Food>) result.getResults();
                        if (list != null && list.size() > 0) {
                            //将查到的食品存入里面
                            textView_scan.setText("扫描到如下食品信息...");
                            for (int i = 0; i < list.size(); i++) {
                                Food food = list.get(i);
                                addname = food.getName();
                                //根据名称查询数据
                                queryName(addname,food.getRemark(),food.getIcon(),food.getPhoto());
                                x = 1;
                                System.out.println("------" + addname);
                            }

                        } else {
                            Log.i("smile", "查询成功，无数据返回");
                            textView_scan.setTextColor(Color.RED);
                            textView_scan.setText("没有找到存根，请点击右上角更新食物存根...");
                        }
                    } else {
                        Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                    }
                }
            }, k);
        } else if (kk == delete) {
            String bql = "select * from Food where id = ?";//根据id号查询
            new BmobQuery<Food>().doSQLQuery(LabelActivity.this, bql, new SQLQueryListener<Food>() {

                @Override
                public void done(BmobQueryResult<Food> result, BmobException e) {
                    if (e == null) {
                        List<Food> list = (List<Food>) result.getResults();
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                Food food = list.get(i);
                                deletename = food.getName();
                                x = 2;
                                //根据名称查询数据
                                queryName(deletename,food.getRemark(),food.getIcon(),food.getPhoto());
                                System.out.println("------" + deletename);
                            }

                        } else {
                            Log.i("smile", "查询成功，无数据返回");
                            textView_scan.setText("没有找到存根，请点击右上角更新食物存根...");
                            textView_scan.setTextColor(Color.RED);
                        }
                    } else {
                        Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                    }
                }
            }, k);
        }

    }

    private List<String> list2=new ArrayList<>();
    private List<Note> list3 = new ArrayList<>();
    private void queryName(final String name, final String s,final BmobFile bmobFile, final String photo) {
        String bql1 = "select * from Note where foodname = ?";
        new BmobQuery<Note>().doSQLQuery(LabelActivity.this, bql1, new SQLQueryListener<Note>() {
            @Override
            public void done(BmobQueryResult<Note> result, BmobException e) {
                if (e == null) {
                    List<Note> list = (List<Note>) result.getResults();
                    boolean flag=true;
                    if(list3.size()==0 ){
                        list3.add(list.get(0));
                        flag=false;
                    }else{
                        for (int i = 0; i < list3.size();i++){
                            if((list3.get(i).getFoodname()).equals(list.get(0).getFoodname())){
                                list3.remove(i);
                                list3.add(list.get(0));
                                flag=false;
                                break;
                            }
                        }
                    }
                    if(flag){
                        list3.add(list.get(0));
                    }
                    if (list != null && list.size() > 0) {
                        Note note = new Note();
                        note = list.get(0);
                        String id = note.getObjectId();
                        notes = list3;
                        na = new NoteAdapter(LabelActivity.this, notes);
                        listView.setAdapter(na);
                        //将查到的食品存入里面
                        textView_scan.setText("被更新的食品有...");
                        if(x == 1){
                            num = note.getNum();
                            num = num +1;
                            note.setNum(num);
                            //更新数据
                            note.update(LabelActivity.this, id, new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    //保存成功
                                    Toast.makeText(LabelActivity.this,"更新数据成功",Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(LabelActivity.this,"更新数据失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else if(x == 2) {
                            num = note.getNum();
                            if(num != 1){
                                num = num -1;
                                note.setNum(num);
                                //更新数据
                                note.update(LabelActivity.this, id, new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        //保存成功
                                        Toast.makeText(LabelActivity.this, "更新数据成功", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Toast.makeText(LabelActivity.this, "更新数据失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                //删除食品信息
                                deletefood(id);
                            }
                        }

                    }
                } else {
                    textView_scan.setText("新加入的食物有...");
                    List<String> list1 = new ArrayList<>();
                    list1.add("食物名称: "+name+"   保质期:    "+shelf_life+"  数量 "+ 1+"  备注:"+s);
                    list2.add(list1.get(0));
                    ArrayAdapter adapter = new ArrayAdapter(LabelActivity.this,android.R.layout.simple_list_item_1,list2);
                    listView.setAdapter(adapter);
                    final String ur = "http://file.bmob.cn/" + bmobFile.getUrl();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+photo;
                            File file = new File(path);
                            if(!file.exists()) {
                                //加载图片
                                Load.getPicture(ur, photo);
                            }
                            //添加食品信息
                            addFoodInfo(name, shelf_life, 1, s, photo);
                        }
                    }).start();
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        }, name);
    }

    //自定义Note适配器（静态内部类）
    class NoteAdapter extends BaseAdapter {

        private Context context;
        private List<Note> list;

        //传递参数
        public NoteAdapter(Context context, List<Note> list) {
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
            if (view == null) {
                //获取列表布局
                view = LayoutInflater.from(context).inflate(R.layout.layout_note_item_addfood, null);
            }
            final Note note = list.get(i);
            String id = note.getObjectId();
            TextView name = (TextView) view.findViewById(R.id.textView_name);
            TextView num1 = (TextView) view.findViewById(R.id.textView_numnum);
            TextView shelf_life1 = (TextView) view.findViewById(R.id.textView_shelf_life);
            TextView remark = (TextView) view.findViewById(R.id.textView_remark);

            name.setText(note.getFoodname());//设置显示内容
            num1.setText(note.getNum() + "");
            shelf_life1.setText(shelf_life);
            remark.setText(note.getRemark());
            return view;
        }
    }

    private void deletefood(String objectId) {
        Note note = new Note();
        note.delete(this, objectId, new DeleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(LabelActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                textView_scan.setText("以下食品被删除...");
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LabelActivity.this, "删除失败，你可以试试长按选项手动删除...", Toast.LENGTH_SHORT).show();
                //删除成功之后让跳到列表界面
                Intent intent = new Intent(LabelActivity.this, NoteListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addFoodInfo(final String name, final String shelf_life, final int num, final String remark, final String photo) {
        //得到图片文件路径
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + photo;
        //图片的上传
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.upload(LabelActivity.this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                Note note = new Note();
                if (!TextUtils.isEmpty(name)) {
                    note.setFoodname(name);
                    note.setShelf_life(shelf_life);
                    note.setNum(num);
                    note.setRemark(remark);
                    note.setIcon(bmobFile);
                    note.save(LabelActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            //保存成功
                            Toast.makeText(LabelActivity.this, "添加数据成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(LabelActivity.this, "添加数据失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(int i, String s) {
                System.out.println("-----" + s);
            }
        });
    }

    //创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_food, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //菜单被选中时
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_note_addFood:
                //添加食品存根
                addFoodStub();
                break;
            //添加食品
            case R.id.menu_note_add:
                addFood();
                break;
            //更新食品
            case R.id.menu_note_delete:
                deleteFood();
                break;
        }
        return true;
    }

    //扫标签更新食物
    private void deleteFood() {
        //连接串口
        devfd = HardwareControler.openSerialPort(devName, speed, dataBits, stopBits);
        if (devfd >= 0) {
            flag2 = true;
            flag1 = false;
            //向串口发送指令帧，扫描食品
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (flag2) {
                        writeSerial(cmd1, cmd1.length);
                        readSerialFood(delete);
                        flag2 = true;
                        try {
                            Thread.sleep(2 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } else {
            devfd = -1;
            Toast.makeText(this, "扫描食品出错，请重新扫描", Toast.LENGTH_SHORT).show();
        }

    }

    private void addFood() {
        textView_scan.setText("正在扫描食品信息...");
        //连接串口
        devfd = HardwareControler.openSerialPort(devName, speed, dataBits, stopBits);
        if (devfd >= 0) {
            flag1 = true;
            flag2 = false;
            //向串口发送指令帧，扫描食品
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (flag1) {
                        writeSerial(cmd1, cmd1.length);
                        readSerialFood(add);
                        flag1 = true;
                        try {
                            Thread.sleep(2 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } else {
            devfd = -1;
            Toast.makeText(this, "扫描食品出错，请重新扫描", Toast.LENGTH_SHORT).show();
        }
    }
    //添加食物
    private void addFoodStub() {
        Intent intent = new Intent(this, LableAddActivity.class);
        startActivity(intent);
    }
}
