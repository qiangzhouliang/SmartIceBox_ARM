package com.friendlyarm.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.friendlyarm.LEDDemo.R;
import com.friendlyarm.Utils.CookBook;
import com.friendlyarm.Utils.HttpUtil;
import com.friendlyarm.Utils.ImageThread;
import com.friendlyarm.Utils.ViewHolder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private GridView gridView;
    private CookAdapter cookAdapter;
    private ViewHolder viewHolder = null;
    private RelativeLayout relativeLayout;
    public static String checkName = "";
    private Activity activity;
    private CookBookStep cookBookStep;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        cookBookStep = (CookBookStep) activity;
    }

    public MenuFragment() {
    }

    /**
     * 实例化的同时传递参数
     *
     * @param title
     * @return
     */
    public static MenuFragment getInstance(String title) {
        MenuFragment m = new MenuFragment();

        Bundle b = new Bundle();
        b.putString("title", title);
        m.setArguments(b);
        checkName = title;
        return m;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        new HttpResource().execute(checkName);
        gridView = (GridView) view.findViewById(R.id.gView);
        return view;
    }


    class HttpResource extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {// 此处传入关于菜谱的问题
            String result = null;
            result = CookBook.getRequest1(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // 解析网络端传来的数据
            if (result != null) {
                // 将JSON类型的字符串转化为JSON的数据
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("error_code") == 0) {
                        // System.out.println(object.get("result"));
                        /** 将得到的数据转化为一个JSON 对象 */
                        JSONObject object1 = new JSONObject(object.get("result").toString());
                        // System.out.println(object1.get("data"));
                        /** 从JSON 对象中取出需要的数据 */
                        String result1 = object1.get("data").toString();
                        /** 从JSON 对象中取出需要的数据转化为一个JSON数组 */
                        JSONArray jsonArray = new JSONArray(result1);
                        /** 获取到JSON数组中第一个元素的信息，转化为一个JSON数组 */
                        // JSONObject object12=jsonArr.getJSONObject(0);
                        /** 打印出相应的元素信息 */
                        JSONObject obj = null;
                        Map<String, Object> map = null;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            map = new HashMap<String, Object>();
                            map.put("id", obj.getString("id"));
                            map.put("title", obj.getString("title"));
                            map.put("tags", obj.getString("tags"));
                            map.put("imtro", obj.getString("imtro"));
                            map.put("ingredients", obj.getString("ingredients"));
                            map.put("burden", obj.getString("burden"));
                            map.put("albums", obj.getString("albums"));// 含图片
                            map.put("steps", obj.getString("steps"));// JSONArray
                            data.add(map);
                        }
                        // System.out.println("得到的链表的数据是data:" + data);
                    } else {
                        /** 网络异常 */
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // 网络异常
                Log.e("error", "网络异常");
            }
            if (data.size() > 0) {
                cookAdapter = new CookAdapter(data);
                gridView.setAdapter(cookAdapter);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    class CookAdapter extends BaseAdapter {
        private List<Map<String, Object>> data;

        public CookAdapter(List<Map<String, Object>> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
        /* 此处加载View中的内容 */
            View view = convertView;
            if (view == null) {
                view = activity.getLayoutInflater().inflate(R.layout.cook_layout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.setImageView((ImageView) view.findViewById(R.id.iamge));
                viewHolder.setTextView((TextView) view.findViewById(R.id.dishName));
                view.setTag(viewHolder);
            }
            relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
            viewHolder = (ViewHolder) view.getTag();
			/* 此处添加菜谱的信息 */
            new ImageThread(HttpUtil.getURL(data.get(position).get("albums").toString()), viewHolder.getImageView())
                    .start();
            viewHolder.getTextView().setText(data.get(position).get("title").toString());
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                String id = data.get(position).get("id").toString();
                String title = data.get(position).get("title").toString();
                String tags = data.get(position).get("tags").toString();
                String imtro = data.get(position).get("imtro").toString();
                String ingredients = data.get(position).get("ingredients").toString();
                String burden = data.get(position).get("burden").toString();
                String albums = data.get(position).get("albums").toString();
                String steps = data.get(position).get("steps").toString();
                cookBookStep.cookStep(id, title, tags, imtro, ingredients, burden, albums, steps);

                }
            });
            return view;
        }
    }

    public static interface CookBookStep {
        public void cookStep(String id, String title, String tags, String imtro, String ingredients, String burden, String albums, String steps);
    }
}
