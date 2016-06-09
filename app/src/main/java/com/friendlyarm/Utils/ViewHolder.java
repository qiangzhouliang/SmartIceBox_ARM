package com.friendlyarm.Utils;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/4/28.
 */
public class ViewHolder {
	private ImageView imageView;
	private TextView textView;
	private TextView burden;

	public TextView getBurden() {
		return burden;
	}

	public void setBurden(TextView burden) {
		this.burden = burden;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public TextView getTextView() {
		return textView;
	}

	public void setTextView(TextView textView) {
		this.textView = textView;
	}
}
