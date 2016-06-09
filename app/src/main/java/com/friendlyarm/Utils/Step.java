package com.friendlyarm.Utils;

import android.graphics.Bitmap;

public class Step {

	private int step; //步数

	private String descript; //描述
	
	private Bitmap image;//图片

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

}
