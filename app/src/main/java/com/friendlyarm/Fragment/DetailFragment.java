package com.friendlyarm.Fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.friendlyarm.LEDDemo.R;
import com.friendlyarm.Utils.HttpUtil;
import com.friendlyarm.Utils.ImageThread;
import com.friendlyarm.Utils.LoadImage;
import com.friendlyarm.Utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    /** 存储步骤的list */
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private static String id1;
    private static String title1;
    private static String tags1;
    private static String imtro1;
    private static String ingredients1;
    private static String burden1;
    private static String albums1;
    private static String steps1;

    private TextView idText;
    private TextView titleText;
    private TextView tagsText;
    private TextView imtroText;
    private TextView ingredientsText;
    private TextView burdenText;
    private ImageView albumsImage;
    private ListView listView;

    private ViewHolder viewHolder = null;
    private CookAdapter cookAdapter;
    private Activity activity;


    public DetailFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    /**
     * 实例化的同时传递参数
     * @param title
     * @return
     */
    public static DetailFragment getInstance(String id,String title,String tags,String imtro,String ingredients,String burden,String albums,String steps){
        DetailFragment detailFragment = new DetailFragment();
        id1 = id;
        title1 = title;
        tags1 = tags;
        imtro1 = imtro;
        ingredients1 = ingredients;
        burden1 = burden;
        albums1 = albums;
        steps1 = steps;
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        idText = (TextView) view.findViewById(R.id.id);
        titleText = (TextView) view.findViewById(R.id.title);
        tagsText = (TextView) view.findViewById(R.id.tags);
        imtroText = (TextView) view.findViewById(R.id.imtro);
        ingredientsText = (TextView) view.findViewById(R.id.ingredients);
        burdenText = (TextView) view.findViewById(R.id.burden);
        albumsImage = (ImageView) view.findViewById(R.id.albums);
        list = HttpUtil.getLsit(steps1);
        new LoadImage(list);
    	/* 通过适配器向控件中添加内容 */
        listView = (ListView) view.findViewById(R.id.listView);
        setText();// 为控件添加内容
        return view;
    }

    class CookAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = activity.getLayoutInflater().inflate(R.layout.cook_step_detail, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.setImageView((ImageView) view.findViewById(R.id.cookImage));
                viewHolder.setTextView((TextView) view.findViewById(R.id.message));
                view.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) view.getTag();
            RequestQueue requestQueue= Volley.newRequestQueue(activity);
            ImageRequest imageRequest= HttpUtil.getImage(list.get(position).get("img").toString(), viewHolder.getImageView());
            requestQueue.add(imageRequest);
            viewHolder.getTextView().setText(list.get(position).get("step"));
            return view;
        }
    }

    public void setText() {/* 向控件中添加内容 */
        idText.setText("编号是：" + id1);
        titleText.setText("菜名是：" + title1);
        tagsText.setText("标签：" + tags1);
        imtroText.setText("菜的来源：" + imtro1);
        ingredientsText.setText("主料：" + ingredients1);
        burdenText.setText("佐料：" + burden1); // 为菜谱设置图片‘ // 添加图片
        new ImageThread(HttpUtil.getURL(albums1), albumsImage).start();
		/*
		 * 通过适配器添加图片到 界面中当中实现 界面的额布局
		 */
        cookAdapter = new CookAdapter();
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = 180 * listView.getDividerHeight() * list.size();
        listView.setLayoutParams(params);
        listView.setAdapter(cookAdapter);
    }

}
