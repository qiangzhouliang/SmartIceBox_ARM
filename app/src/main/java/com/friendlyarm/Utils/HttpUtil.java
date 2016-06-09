package com.friendlyarm.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.friendlyarm.LEDDemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by Administrator on 2016/4/28.
 */
public class HttpUtil {
	static int[]  child_image = new int[] { R.drawable.a_1, R.drawable.a_2, R.drawable.a_3, R.drawable.a_4, R.drawable.a_5,
			R.drawable.a_6, R.drawable.a_7, R.drawable.a_8, R.drawable.a_9, R.drawable.a_10, R.drawable.a_11,
			R.drawable.a_12, R.drawable.a_13, R.drawable.a_14, R.drawable.a_15, R.drawable.a_16, R.drawable.a_17,
			R.drawable.a_18, R.drawable.a_19, R.drawable.a_20, R.drawable.a_21, R.drawable.a_22, R.drawable.a_23,
			R.drawable.a_24};

	static int[] aged = new int[] { R.drawable.aged_1, R.drawable.aged_2, R.drawable.aged_3, R.drawable.aged_4, R.drawable.aged_5,
			R.drawable.aged_6, R.drawable.aged_7, R.drawable.aged_8, R.drawable.aged_9, R.drawable.aged_10, R.drawable.aged_11,
			R.drawable.aged_12, R.drawable.aged_13, R.drawable.aged_14, R.drawable.aged_15, R.drawable.aged_16, R.drawable.aged_17,
			R.drawable.aged_18};

	static int[] Gravida = new int[] {
			R.drawable.gravida_1, R.drawable.gravida_2, R.drawable.gravida_3, R.drawable.gravida_4, R.drawable.gravida_5,
			R.drawable.gravida_6, R.drawable.gravida_7, R.drawable.gravida_8, R.drawable.gravida_9, R.drawable.gravida_10, R.drawable.gravida_11,
			R.drawable.gravida_12, R.drawable.gravida_13, R.drawable.gravida_14, R.drawable.gravida_15, R.drawable.gravida_16, R.drawable.gravida_17,
			R.drawable.gravida_18, R.drawable.gravida_19, R.drawable.gravida_20, R.drawable.gravida_21, R.drawable.gravida_22, R.drawable.gravida_23,
			R.drawable.gravida_24, R.drawable.gravida_25, R.drawable.gravida_26, R.drawable.gravida_27, R.drawable.gravida_28, R.drawable.gravida_29,
			R.drawable.gravida_30, R.drawable.gravida_31, R.drawable.gravida_32, R.drawable.gravida_33, R.drawable.gravida_34, R.drawable.gravida_35,
			R.drawable.gravida_36, R.drawable.gravida_37, R.drawable.gravida_38, R.drawable.gravida_39, R.drawable.gravida_40, R.drawable.gravida_41,
			R.drawable.gravida_42, R.drawable.gravida_43
	};

	public static int[] getChild_image() {
		return child_image;
	}

	public static int[] getAged() {
		return aged;
	}

	public static int[] getGravida() {
		return Gravida;
	}


	public static String[] getChild_cookName() {
		String cookNa = "菠萝船古老肉,茄汁豆腐 ,桂花糯米藕 ,茄汁彩蔬鱼卷,芝香椒盐虾,菠菜翡翠球,彩椒圈太阳花煎蛋,金玉猪肉卷 ,五彩蔬菜牛肉串 ,桂花山药,香炸蚕豆,鸭梨银耳羹,小猪豆沙包,酸奶紫薯饼,香煎虾饼,蛋皮烧麦,小刺猬豆沙包,彩虹面条,草莓酱曲奇饼干,琥珀桃仁,原味蛋奶松饼,奶酪土豆泥,酸奶水果沙拉,奶油奶酪布丁";
		String[] str = cookNa.split(",");
		return str;
	}
	public static String[] getAged_cookName() {
		String cookNa = "鱼米之乡,醋椒黑木耳,胡萝卜羊肉汤,白萝卜炖蜂蜜,腐竹玉米马蹄汤,家常豆腐,双菇羊肉煲,藕条炒木耳,羊肉炖白萝卜,香菇油菜,双豆银耳大枣粥,洋葱炒蛋,金钩腐竹,虾仁蒸豆腐,山药羊肉煲,家常木耳炒鸡蛋,香菇培根卷,白萝卜炖虾";
		String[] str = cookNa.split(",");
		return str;
	}
	public static String[] getGravidastr_cookName() {
		String cookNa = "蛋肉糕,玉米汁,土豆浓汤,鲜虾豆腐翡翠汤,甜豆炒鲜贝,鲜香担担面,姜汁菠菜,鸡蛋三明治,鸡肉起司卷,莴笋炒虾仁,老北京疙瘩汤,猕猴桃西米露,海带冬瓜排骨汤,杂骨菌菇汤,白萝卜丝炒黄豆,三色炒虾仁,小芋头烧鸡翅,凉拌西红柿,桂圆红枣小米糊,半杯干斑豆,三丝银鱼羹,核桃芝麻豆浆,南瓜银耳羹,冬瓜粥,豆腐海藻鲜虾汤,四喜豆腐,土豆炖鸡块,猪肝西红柿浓汤,莲藕焖猪手,芦笋鸡蛋沙拉,冬瓜粥,鸡蓉玉米粥,杏仁炒荷兰豆,鸡丝芹菜沙拉,开胃蛋包饭,香蕉猕猴桃奶昔,生鱼红枣汤,冬瓜枸杞金针菇姜丝汤,洋葱酱香南瓜,西红柿烩菜花,酸奶布丁,鸭血豆腐汤 ";
		String[] str = cookNa.split(",");
		return str;
	}


	public static  List<Map<String,String>> getLsit(String steps){
		List<Map<String ,String>> list=new ArrayList<Map<String,String>>();
		
		try {
			JSONArray jsonArray=new JSONArray(steps);
			/** 获取到JSON数组中第一个元素的信息，转化为一个JSON数组 */
			// JSONObject object12=jsonArr.getJSONObject(0);
			/** 打印出相应的元素信息 */
			JSONObject obj = null;
			Map<String, String> map = null;
			for (int i = 0; i < jsonArray.length(); i++) {
				obj = jsonArray.getJSONObject(i);
				map = new HashMap<String, String>();
				map.put("step", obj.getString("step"));
				map.put("img", obj.getString("img").toString().replace("\\",""));
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//private Bitmap bitMap;

	/* 传入路径地址下载网上的图片 */
	public static void loadImage1(final String path) {
		/* 位图Android中的图片均用此表示 */
		/* 位图的构造方法是私有的没有公有的构造方法 */

		Bitmap bitmap = null;
		InputStream in = null;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			in = conn.getInputStream();
			/* 流对象转化为位图对象 */
			/* 使用BitmapFactory 将InputStream转化为Bitmap对象 */
			bitmap = BitmapFactory.decodeStream(in);
			// bitMap.bitMap(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getURL(String url) {
		String URL = "";
		URL = url.substring(2, url.length() - 2);
		URL = URL.replace("\\", "");
		return URL;

	}

	public static List<CookBook> getList(List<Map<String, Object>> list) {
		//List<CookBook> cookList = new ArrayList<CookBook>();
		Cookor cookor = null;
		for (Map<String, Object> map : list) {
			cookor = new Cookor();
			cookor.setId(map.get("id").toString());
			// cookor.setAlbums();// 设置图片getURL(map.get("albums").toString())；
		}
		return null;
	}

	public static Bitmap loadImage(String path){
		/* 位图Android中的图片均用此表示 */
		Bitmap bitmap = null;
		/* 位图的构造方法是私有的没有公有的构造方法 */
		InputStream in = null;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			in = conn.getInputStream();
			/* 流对象转化为位图对象 */
			/* 使用BitmapFactory 将InputStream转化为Bitmap对象 */
			bitmap = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}
	//下载图片的方法
	public static ImageRequest getImage(String  URL,final ImageView imageView){
		ImageRequest imageRequest=new ImageRequest( URL , new Response.Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap bitmap) {
				if(bitmap != null){
					imageView.setImageBitmap(bitmap);
				}
			}
		},0,0,null,new  Response.ErrorListener(){
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				System.out.println("网络故障！！！！！！");
			}
		});
	return  imageRequest;
	}


}
