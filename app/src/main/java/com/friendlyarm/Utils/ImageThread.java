package com.friendlyarm.Utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class ImageThread  extends Thread {
	
	public Handler handler=new Handler();

	public ImageThread(String URL, ImageView imageView) {
		// TODO Auto-generated constructor stub
		this.imageView = imageView;
		this.URL = URL;
	}

	private ImageView imageView;
	private String URL;

	@Override
	public void run() {
		final Bitmap bitmap = HttpUtil.loadImage(URL);
		if (bitmap != null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					imageView.setImageBitmap(bitmap);
				}
			});
		} else {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Log.e("error ", "&&&&&&&&&&&&&&& ???");
				}
			});
		}
	}


}
