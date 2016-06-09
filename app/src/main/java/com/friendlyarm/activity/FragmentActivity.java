package com.friendlyarm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.friendlyarm.AndroidSDK.HardwareControler;
import com.friendlyarm.LEDDemo.R;
import com.friendlyarm.Utils.JsonParser;
import com.friendlyarm.Utils.Vioce;
import com.friendlyarm.application.QzlApplication;
import com.friendlyarm.service.UpdateService;
import com.friendlyarm.user.Note;
import com.friendlyarm.Utils.util;
import com.friendlyarm.user.Temp;
import com.friendlyarm.user._User;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

public class FragmentActivity extends Activity implements View.OnClickListener{

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    //放note
    private List<Note> notes = new ArrayList<>();
    private ListView listView;
    private NoteAdapter na;

    private final String str = "温馨提示，您的冰箱内有将要过期的食品,请及时使用！";
    private final String str1 = "温馨提示，您的冰箱内有已经过期的食品，请及时处理，误食过期食品对身体不好！";
    private boolean flag = true;//语音提示标志

    private boolean flag1 = true;//过期食品标志
    private boolean flag2 = true;//语音跳转判断
    private boolean Flag3 = true;//是否在冰箱上调节温度
    private int compare_temp=0;
    private Button button_query,button_food,button_analyze,button_temp,button_flag_add,button_set;
    private ImageButton imageButton_voice;
    private TextView textView_temp,textView_date,textView_time,textView_hint,textView_open_close;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    String open_flag;//门开关标志

    private static final String TAG = "SerialPort";
    private final int MAXLINES = 200;
    private String devName = "/dev/s3c2410_serial3";
    private int speed = 9600;
    private int dataBits = 8;
    private int stopBits = 1;
    private int devfd = -1;
    private StringBuilder remoteData = new StringBuilder(256 * MAXLINES);

    private final byte [] str11 = new  byte[]{0x31};
    private final byte [] str22 = new  byte[]{0x32};
    private final byte [] str33 = new  byte[]{0x33};

    private String PhoneNumber;//存储电话号码
    private QzlApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fragment);
        app = new QzlApplication();
        listView = (ListView) findViewById(R.id.listView);
        button_query = (Button) findViewById(R.id.button_query);
        button_food = (Button) findViewById(R.id.button_food);
        button_analyze = (Button) findViewById(R.id.button_analyze);
        button_temp = (Button) findViewById(R.id.button_temp);
        button_flag_add = (Button) findViewById(R.id.button_flag_add);
        button_set = (Button) findViewById(R.id.button_set);
        imageButton_voice = (ImageButton) findViewById(R.id.imageButton_voice);

        textView_temp = (TextView) findViewById(R.id.textView_temp);
        textView_date = (TextView) findViewById(R.id.textView_date);
        textView_time = (TextView) findViewById(R.id.textView_time);
        textView_hint = (TextView) findViewById(R.id.textView_hint);
        textView_open_close = (TextView) findViewById(R.id.textView_open_close);

        button_query.setOnClickListener(this);
        button_food.setOnClickListener(this);
        button_analyze.setOnClickListener(this);
        button_temp.setOnClickListener(this);
        button_flag_add.setOnClickListener(this);
        button_set.setOnClickListener(this);
        imageButton_voice.setOnClickListener(this);

        devfd = HardwareControler.openSerialPort( devName, speed, dataBits, stopBits );
        if (devfd >= 0) {
            timer.schedule(task, 0, 10);
        } else {
            devfd = -1;
            Toast.makeText(this,"温度传输出错",Toast.LENGTH_SHORT).show();
        }
       new Thread(new Runnable() {
           @Override
           public void run() {
               while (true) {
                   //获取日期
                   //创建一个日历对象
                   final Calendar calendar = Calendar.getInstance();
                   year = calendar.get(Calendar.YEAR);
                   month = calendar.get(Calendar.MONTH);
                   day = calendar.get(Calendar.DAY_OF_MONTH);
                   hour = calendar.get(Calendar.HOUR_OF_DAY);
                   minute = calendar.get(Calendar.MINUTE);
                   Message message = new Message();
                   message.what = 2;
                   handler.sendMessage(message);
                   try {
                       Thread.sleep(500);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }
       }).start();

        Bundle bundle = getIntent().getExtras();
        final _User user = (_User) bundle.get("user");
        PhoneNumber = user.getMobilePhoneNumber().toString();
        //启动一个服务
        startServer();
        //测试
        //String st = 28+" ℃";
        //temperatureTo(st);

        //上下文菜单
        registerForContextMenu(listView);
    }

    //启动一个服务
    private void startServer() {
        Intent intent = new Intent(this,UpdateService.class);
        intent.putExtra("PhoneNumber",PhoneNumber);
        startService(intent);
    }

    //停止一个服务
    private void stopServerClick(){
        Intent intent = new Intent(this,UpdateService.class);
        intent.putExtra("flag_date", app.flag_date);
        stopService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        loadData();
                        Thread.sleep(1000 * 60*10);//86400000
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if(Flag3) {
                            queryTemp();
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        flag1 = true;
    }

    //温度查询
    private void queryTemp() {
        BmobQuery<Temp> bmobQuery = new BmobQuery<Temp>();
        //查询一个
        bmobQuery.getObject(this, "10166ef327", new GetListener<Temp>() {
            @Override
            public void onSuccess(Temp temp) {
                String flag_temp = temp.getFlagtemp();
                if (!flag_temp.isEmpty() && Integer.parseInt(flag_temp) != 0) {
                    contrial(Integer.parseInt(flag_temp), str11, str33);
                } else {
                    int reg = HardwareControler.write(devfd, str22);
                    if (reg > 0) {
                        //Toast.makeText(FragmentActivity.this, "调节完成...", Toast.LENGTH_SHORT).show();
                    }
                }
                open_flag = temp.getOpenflag();
                Message msg = new Message();
                msg.what = 3;
                msg.obj = open_flag;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(FragmentActivity.this, "查询数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //从后台取出数据
    private void loadData() {
        flag1 = true;
        //直接查询
        BmobQuery<Note> query = new BmobQuery<>();
        query.setLimit(50);//限定条数（默认是10条）
        //查询多个
        query.findObjects(this, new FindListener<Note>() {
            @Override
            public void onSuccess(List<Note> list) {
                notes = list;
                na = new NoteAdapter(FragmentActivity.this, notes);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(na);
                    }
                });
                System.out.println(notes.size());
                if (notes.size() == 0) {
                    textView_hint.setText("温馨提示：您的冰箱内没有将要过期的食物...");
                    textView_hint.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onError(int i, String s) {
                System.out.println("onError");
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
            view = LayoutInflater.from(context).inflate(R.layout.layout,null);
            final Note note = list.get(i);
            String id = note.getObjectId();//拿到id
            String num_f = note.getShelf_life();
            int num_flag = 0;
            if(!num_f.isEmpty()) {
                num_flag = Integer.parseInt(num_f);
            }
            if(num_flag <= app.flag_date){
                flag1 = false;
                //获取列表布局
                view = LayoutInflater.from(context).inflate(R.layout.layout_note_item_deadline,null);

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
                if(num_flag > 0 ){
                    textView_hint.setText("温馨提示：您的如下食物即将过期...");
                    textView_hint.setTextColor(Color.RED);
                    if(flag) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //语音提示
                                Vioce.xunfeiVicos(str, FragmentActivity.this);
                                flag = false;
                            }
                        }).start();
                    }
                }else if(num_flag <= 0){
                    textView_hint.setText("温馨提示：您的如下食物即将已经过期...");
                    textView_hint.setTextColor(Color.RED);
                    if(flag) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //语音提示
                                Vioce.xunfeiVicos(str1, FragmentActivity.this);
                                flag = false;
                            }
                        }).start();
                    }
                }

                name.setText(note.getFoodname());//设置显示内容
                String str = note.getNum()+"";
                num.setText(str);
                shelf_life.setText(note.getShelf_life());
                remark.setText(note.getRemark());
                view.setTag(note.getObjectId());//保存ID
                final ImageView imageView_icon = (ImageView) view.findViewById(R.id.imageView_iconn);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BmobFile bf = note.getIcon();
                        if (bf != null) {
                            //缩列图
                            bf.loadImageThumbnail(FragmentActivity.this, imageView_icon, 36, 36, 100);
                        }
                    }
                }).start();
            }else if(flag1){
                textView_hint.setText("温馨提示：您的冰箱内没有将要过期的食物...");
                textView_hint.setTextColor(Color.BLACK);
            }
            return view;
        }
    }


    private final int BUFSIZE = 4;
    private byte[] buf = new byte[BUFSIZE];
    private Timer timer = new Timer();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (HardwareControler.select(devfd, 0, 0) == 1) {
                        int retSize = HardwareControler.read(devfd, buf, BUFSIZE);
                        if (retSize > 0) {
                            //String[] str1 = util.hex(buf,1);
                            String[] str1 = util.Bytes2HexString_2(buf,3);
                            String str2 = "";
                            int in_temp;
                            for (int i = 0;i< 3;i++){
                                in_temp = Integer.parseInt(str1[i].substring(1,str1[i].length()));
                                if(i==2){
                                    compare_temp = Integer.parseInt(str2.trim());
                                    str2=str2+".";
                                }
                                str2 = str2+in_temp;
                            }
                            str2=str2+" ℃";
                            String Str = str2;
                            temperatureTo(Str);
                            remoteData.append(str2);
                            if (textView_temp.getLineCount() > MAXLINES) {
                                int nLineCount = textView_temp.getLineCount();
                                int i = 0;
                                for (i = 0; i < remoteData.length(); i++) {
                                    if (remoteData.charAt(i) == '\n') {
                                        nLineCount--;

                                        if (nLineCount <= MAXLINES) {
                                            break;
                                        }
                                    }
                                }
                                remoteData.delete(0, i);
                                //Log.d(TAG, "#### remoteData.delete(0, " + i + ")");
                                textView_temp.setText(remoteData.toString());
                            } else {
                                textView_temp.setText(str2);
                            }
                        }
                    }
                    break;
                case 2:
                    textView_date.setText(year + "年" + (month + 1) + "月" + day + "日 ");
                    if(minute >= 0 && minute < 10) {
                        textView_time.setText(hour + " : " +"0"+ minute);
                    }else {
                        textView_time.setText(hour + " : " + minute);
                    }
                    break;
                case 3:
                    if(msg.obj.equals("01")){
                        textView_open_close.setText("关");
                    }else if(msg.obj.equals("02")){
                        textView_open_close.setText("开");
                    }
                    break;

            }
            super.handleMessage(msg);
        }
    };

    private void temperatureTo(String st) {
        Temp t = new Temp();
        t.setTemp(st);
        //更新数据
        t.update(this, "10166ef327", new UpdateListener() {
            @Override
            public void onSuccess() {
                //保存成功
                //Toast.makeText(FragmentActivity.this, "修改数据成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(FragmentActivity.this, "修改数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //食品查询
            case R.id.button_query:
                Intent intent = new Intent(this,NoteListActivity.class);
                startActivity(intent);
                break;
            //冰箱食谱
            case R.id.button_food:
                Intent intent2 = new Intent(this,RecipeActivity.class);
                startActivity(intent2);
                break;
            //营养分析
            case R.id.button_analyze:
                trophic_analysis();
                break;
            //温度调节
            case R.id.button_temp:
                update_temp();
                break;
            //扫标签添加
            case R.id.button_flag_add:
                Intent intent1 = new Intent(this,LabelActivity.class);
                startActivity(intent1);
                break;
            case R.id.button_set:
                set_date();
                break;
            case R.id.imageButton_voice:
                //语音提示
                Vioce.xunfeiVicos("请说话", FragmentActivity.this);
                flag2 = true;
                //语音合成功能（实现食品添加的RFID读写器的唤醒）
                startSpeechClick();
                break;
        }
    }
    //营养分析
    private void trophic_analysis() {
        startActivity(new Intent(this, Trophic_analysis_Activity.class));
    }

    private void set_date() {
        final String[] item = new String[]{"1","2","3","4","5"};
        AlertDialog.Builder builder = new AlertDialog.Builder(FragmentActivity.this);
        builder.setTitle("请选择合适自己的提醒时间");
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        app.flag_date = 1;
                        Toast.makeText(FragmentActivity.this, "您选择了一天后提示", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        app.flag_date = 2;
                        Toast.makeText(FragmentActivity.this, "您选择了两天后提示", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        app.flag_date = 3;
                        Toast.makeText(FragmentActivity.this, "您选择了三天后提示", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        app.flag_date = 4;
                        Toast.makeText(FragmentActivity.this, "您选择了四天后提示", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        app.flag_date = 5;
                        Toast.makeText(FragmentActivity.this, "您选择了五天后提示", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        builder.create().show();
    }


    private void update_temp() {

        final String[] items = new String[]{"冷冻（-18）","冷藏（7）","春秋季（3~4）","夏季（2~3）","冬季（4~5）","调试","完成"};
        AlertDialog.Builder builder = new AlertDialog.Builder(FragmentActivity.this);
        builder.setIcon(R.drawable.temp);
        builder.setTitle("请选择合适的温度：");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        Flag3 = false;
                        contrial(-18, str11, str33);
                        break;
                    case 1:
                        Flag3 = false;
                        contrial(7,str11, str33);
                        break;
                    case 2:
                        Flag3 = false;
                        contrial(4, str11, str33);
                        break;
                    case 3:
                        Flag3 = false;
                        contrial(3, str11, str33);
                        break;
                    case 4:
                        Flag3 = false;
                        contrial(5, str11, str33);
                        break;
                    case 5:
                        Flag3 = false;
                        contrial(30, str11, str33);
                        break;
                    case 6:
                        int reg = HardwareControler.write(devfd, str22);
                        if(reg > 0){
                            Flag3 = true;
                            Toast.makeText(FragmentActivity.this, "调节完成...", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
        builder.create().show();
    }

    public void contrial(int com_temp, byte[] str1, byte[] str3) {
        if(com_temp < compare_temp){
            int reg = HardwareControler.write(devfd, str1);
            System.out.println("----------"+reg);
            if(reg > 0){
                Toast.makeText(FragmentActivity.this, "正在调节...", Toast.LENGTH_SHORT).show();
            }
        }else if(com_temp > compare_temp){
            int reg = HardwareControler.write(devfd, str3);
            if(reg > 0){
                Toast.makeText(FragmentActivity.this,"正在调节...",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        if (devfd != -1) {
            HardwareControler.close(devfd);
            devfd = -1;
        }
        //停止一个服务（正真运行时不能停止）
        stopServerClick();
        super.onDestroy();
    }

    //创建上下文菜单(删除菜单)长按删除
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //参数（groupID,自己的id,排序，文本内容）
        menu.add(1,1,100,"删除");//添加一个删除按钮
    }
    //点击监听事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
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
                        System.out.println("====="+s);
                    }
                });
                break;
        }
        return super.onContextItemSelected(item);
    }

    //语音合成
    public void startSpeechClick(){
        //1.创建记录语音的SpeechRecognizer对象，第二个参数：本地识别时传InitListener
        SpeechRecognizer mIat= SpeechRecognizer.createRecognizer(this, null);
        //2.设置听写参数，详见《MSC Reference Manual》SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");//应用领域
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//语言
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");//普通话
        mIat.setParameter(SpeechConstant.ENGINE_TYPE,SpeechConstant.TYPE_CLOUD);//输入引擎
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理（这儿设为4秒）
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音（设置为2秒）
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        //mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        //mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");

        //设置听写结果是否动态修正，为“1”则在听写过程中动态递增的返回结果，否怎只在听写结束之后返回最终结果
        //注 ：该参数暂时只对在线听写有效
        mIat.setParameter(SpeechConstant.ASR_DWA,"0");//0:整句话说完之后才显示出来

        //3.开始听写
        mIat.startListening(mRecoListener);
    }

    //听写监听器
    private RecognizerListener mRecoListener = new RecognizerListener() {
        //音量值
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }
        //开始录音
        @Override
        public void onBeginOfSpeech() {

        }
        //结束录音
        @Override
        public void onEndOfSpeech() {

        }
        //返回结果
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            //输出是一个json数据
            System.out.println(recognizerResult.getResultString());
            //调运了返回结果处理
            printResult(recognizerResult);
        }

        @Override
        public void onError(SpeechError speechError) {

        }
        //事件回调
        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }

    };

    //输出结果处理
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        //将结果存入Stringbuffer里面
        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        String stt = resultBuffer.toString();
        if(flag2 && (stt.equals("我要取东西") || stt.equals("取东西") || stt.equals("拿东西") || stt.equals("我要拿东西") || stt.equals("拿")) ){
            Intent intent = new Intent(FragmentActivity.this,LabelActivity.class);
            intent.putExtra("key",1);
            flag2 = false;
            startActivity(intent);
        }else if(flag2 && (stt.equals("我要放东西") || stt.equals("放东西") || stt.equals("我要放东西啊")||stt.equals("搁东西") || stt.equals("放"))){
            Intent intent = new Intent(FragmentActivity.this,LabelActivity.class);
            intent.putExtra("key",2);
            flag2 = false;
            startActivity(intent);
        }else if (flag2 &&(stt.equals("查询食品") || stt.equals("内部食品") || stt.equals("查看食品"))){
            startActivity(new Intent(FragmentActivity.this,NoteListActivity.class));
            flag2 = false;
        }
    }

}
