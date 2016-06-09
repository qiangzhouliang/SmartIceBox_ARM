package com.friendlyarm.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.friendlyarm.Fragment.CookBookFragment;
import com.friendlyarm.Fragment.DetailFragment;
import com.friendlyarm.Fragment.MenuFragment;
import com.friendlyarm.LEDDemo.R;

import java.util.ArrayList;

public class RecipeActivity extends Activity implements ViewPager.OnPageChangeListener,CookBookFragment.CookBookChange,MenuFragment.CookBookStep{

    private ViewPager viewPager;
    private PagerTabStrip pagerTabStrip;
    private WebView webView;
    private CookBookFragment cbf;

    //定义一组标题
    private String[] titles = {"冰箱菜谱","今日推荐","食材大全"};
    //用来装布局用的
    private ArrayList<View> views = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        initView();
    }

    private void initView(){
        pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagertab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //将两个布局加入到views里面去
        views.add(getLayoutInflater().inflate(R.layout.layout2,null));
        views.add(getLayoutInflater().inflate(R.layout.layout1, null));
        views.add(getLayoutInflater().inflate(R.layout.layout3, null));

        //对PagerTabStrip进行设置
        pagerTabStrip.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(android.R.color.holo_blue_bright));//设置指示器的颜色
        pagerTabStrip.setTextColor(Color.WHITE);
        pagerTabStrip.setTextSize(1, 16.0f);
        viewPager.setOnPageChangeListener(this);//注册事件
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setCurrentItem(1);//设置默认选中的页
    }

    //正在滚动
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    //页面被选中了
    @Override
    public void onPageSelected(int position) {
        //加载今日推荐
        if(position == 0){
            cbf = CookBookFragment.getInstance("");
            loadFragment(cbf);

        }else if(position == 1){
            webView = (WebView)views.get(position).findViewById(R.id.web_view);
            String url = "http://m.yz.sm.cn/s?q=%E7%BE%8E%E9%A3%9F&from=wm936310";
            webViewLoad(webView,url);
        }else if(position == 2){
            webView = (WebView)views.get(position).findViewById(R.id.web_view2);
            String url = "http://www.douguo.com/shicai/";
            webViewLoad(webView,url);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        //把当前Fragment添加到Activity栈中
        ft.addToBackStack(null);
        ft.commit();
    }

    private void webViewLoad(WebView webView,String url) {
        WebSettings settings = webView.getSettings();
        //适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);//根据传入的数据再去加载新的网页
                return true;
            }
        });
        webView.loadUrl(url);
    }

    //状态正在发生变化
    @Override
    public void onPageScrollStateChanged(int state) {}

    /**
     * 点击某一种食品时，自动跳转到详细做法
     * @param id
     * @param title
     * @param tags
     * @param imtro
     * @param ingredients
     * @param burden
     * @param albums
     * @param steps
     */
    @Override
    public void cookStep(String id, String title, String tags, String imtro, String ingredients, String burden, String albums, String steps) {
        DetailFragment df = DetailFragment.getInstance(id,title,tags,imtro,ingredients,burden,albums,steps);
        loadFragment(df);
    }

    class MyPagerAdapter extends PagerAdapter {
        //获取总数
        @Override
        public int getCount() {
            return views.size();
        }
        //判断view和视图的对象v是否相等
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //实例化每一个选项卡
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = views.get(position);
            container.addView(v);
            return v;
        }

        //删除选项卡
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        //获取标题
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    //发生点击时的接口监听事件
    @Override
    public void cookbookchange(String name) {
        System.out.println(name);
        MenuFragment mf = MenuFragment.getInstance(name);
        loadFragment(mf);
    }

    @Override
    public void searchCookBook(String str) {
        MenuFragment mf = MenuFragment.getInstance(str);
        loadFragment(mf);
    }
    //键盘按下
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(getFragmentManager().getBackStackEntryCount() == 0){
                finish();
            }else{
                getFragmentManager().popBackStack();//出栈操作
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
