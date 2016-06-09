package com.friendlyarm.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetImage{
	/*@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 下载一个baidu的logo
		new MyTask().execute("http://www.baidu.com/img/bdlogo.png");
	}
*/
	// 第一个参数的需要处理的数据，第二个参数是进度值，第三个参数是处理结果的类型
	public class MyTask extends AsyncTask<String, Integer, Bitmap> {

		@Override
		protected void onPostExecute(Bitmap result) {
			Log.d("", "onPostExecute");
			updateImageView(result);
		}

		@Override
		protected void onPreExecute() {
			Log.d("", "onPreExecute");
		}

		@Override
		protected Bitmap doInBackground(String... args) {
			String website = args[0];
			HttpURLConnection conn = null;
			InputStream ins = null;
			try {
				URL url = new URL(website);
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				conn.setDoInput(true);
				if (conn.getResponseCode() == 200) {
					Log.d("", "get image is ok");
					ins = conn.getInputStream();
					return BitmapFactory.decodeStream(ins);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (ins != null) {
					try {
						ins.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

	}

	public void updateImageView(Bitmap result) {
		if (result != null) {
			//ImageView img = (ImageView) findViewById(R.id.iamge);
			//img.setImageBitmap(result);
		}
	}
}
