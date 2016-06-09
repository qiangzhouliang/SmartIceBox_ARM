package com.friendlyarm.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import com.friendlyarm.LEDDemo.R;
import com.friendlyarm.Utils.HttpUtil;
import com.friendlyarm.Utils.ViewHolder;
import com.friendlyarm.application.QzlApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class CookBookFragment extends Fragment {
    private GridView gridView;
    private GridView gridView2;
    private GridView gridView3;
    private LayoutInflater layoutInflater;
    private QzlApplication app;
    private Activity activity;

    private String[] Childstr;
    private String[] Gravidastr;
    private String[] Agedstr;
    int[]  child_image =HttpUtil.getChild_image();
    int[] aged = HttpUtil.getAged();
    int[] Gravida = HttpUtil.getGravida();
    private RelativeLayout relativeLayout;
    private SearchView searchView;
    private ListView listView;

    private CookBookChange cookBookChange;
    public CookBookFragment() {
    }

    /**
     * 实例化的同时传递参数
     * @param title
     * @return
     */
    public static CookBookFragment getInstance(String title){
        CookBookFragment c = new CookBookFragment();

        Bundle b = new Bundle();
        b.putString("title",title);
        c.setArguments(b);
        return c;
    }

    //获得宿主activity
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        cookBookChange = (CookBookChange) activity;
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cook_book,container,false);
        layoutInflater = inflater;
        app = new QzlApplication();
        init(view);
        return view;
    }

    /**
     * 初始化控件
     */
    private void init(View view) {
        TabHost th = (TabHost) view.findViewById(R.id.tabhost);
        th.setup(); // 初始化TabHost容器
        // 在TabHost创建标签，然后设置：标题／图标／标签页布局
        th.addTab(th.newTabSpec("tab1").setIndicator("小孩", getResources().getDrawable(R.drawable.ic_launcher))
                .setContent(R.id.tab1));
        th.addTab(th.newTabSpec("tab2").setIndicator("孕妇", getResources().getDrawable(R.drawable.ic_launcher))
                .setContent(R.id.tab2));
        th.addTab(th.newTabSpec("tab3").setIndicator("老人", getResources().getDrawable(R.drawable.ic_launcher))
                .setContent(R.id.tab3));
        // 上面的null可以为getResources().getDrawable(R.drawable.图片名)设置图标

        searchView = (SearchView) view.findViewById(R.id.serchView);
        listView = (ListView) view.findViewById(R.id.listView);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("查询");

        Cursor cursor = getTestCursor();
        @SuppressWarnings("deprecation")
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(activity, R.layout.mytextview, cursor,
                new String[] { "tb_name" }, new int[] { R.id.textview });
        searchView.setSuggestionsAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String str) {

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String str) {
                /*此处添加查询项目*/
                cookBookChange.searchCookBook(str);
                return false;
            }
        });

        Childstr = HttpUtil.getChild_cookName();
        gridView = (GridView) view.findViewById(R.id.gridView1);
        OSAdapter osAdapter = new OSAdapter(Childstr,child_image);
        gridView.setAdapter(osAdapter);


        Gravidastr=HttpUtil.getGravidastr_cookName();
        gridView2 = (GridView) view.findViewById(R.id.gridView2);
        OSAdapter osAdapter1 = new OSAdapter(Gravidastr,Gravida);
        gridView2.setAdapter(osAdapter1);

        Agedstr=HttpUtil.getAged_cookName();
        gridView3 = (GridView) view.findViewById(R.id.gridView3);
        OSAdapter osAdapter2 = new OSAdapter(Agedstr ,aged);
        gridView3.setAdapter(osAdapter2);
    }

    class OSAdapter extends BaseAdapter {
        private String[] name;
        private int[] image;

        public OSAdapter(String[] name,int[] image) {
            this.name = name;
            this.image=image;
        }

        @Override
        public int getCount() {
            return name.length;
        }

        @Override
        public Object getItem(int position) {
            return name[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder viewHolder = null;
            if (view == null) {
                view = layoutInflater.inflate(R.layout.cook_layout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.setTextView((TextView) view.findViewById(R.id.dishName));
                viewHolder.setImageView((ImageView) view.findViewById(R.id.iamge));
                view.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.getTextView().setText(name[position]);
            viewHolder.getImageView().setImageResource(image[position]);

            relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cookBookChange.cookbookchange(name[position]);
                    /*Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    intent.putExtra("name", name[position]);
                    startActivity(intent);*/
                }
            });
            return view;
        }
    }
    // 添加suggestion需要的数据
    public Cursor getTestCursor() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(activity.getFilesDir() + "/my.db3", null);
        Cursor cursor = null;
        try {
            String insertSql = "insert into tb_test values (null,?,?)";
            db.execSQL(insertSql, new Object[] { "鱼香鸡蛋", 1 });
            db.execSQL(insertSql, new Object[] { "土豆丝", 2 });
            db.execSQL(insertSql, new Object[] { "西红柿炒蛋", 3 });
            db.execSQL(insertSql, new Object[] { "青椒肉丝", 4 });
            db.execSQL(insertSql, new Object[] { "糖拌西红柿", 5 });
            String querySql = "select * from tb_test";
            cursor = db.rawQuery(querySql, null);

        } catch (Exception e) {
            String sql = "create table tb_test (_id integer primary key autoincrement,tb_name varchar(20),tb_age integer)";
            db.execSQL(sql);
            String insertSql = "insert into tb_test values (null,?,?)";
            db.execSQL(insertSql, new Object[] { "茄汁豆腐", 1 });
            db.execSQL(insertSql, new Object[] { "芝香椒盐虾", 2 });
            db.execSQL(insertSql, new Object[] { "菠菜翡翠球", 3 });
            db.execSQL(insertSql, new Object[] { "彩椒圈太阳花煎蛋", 4 });
            db.execSQL(insertSql, new Object[] { "香炸蚕豆", 5 });
            String querySql = "select * from tb_test";
            cursor = db.rawQuery(querySql, null);
        }
        return cursor;
    }

    //创建一个回调接口
    public static interface CookBookChange{
        public void cookbookchange(String name);
        public void searchCookBook(String str);
    }
}
