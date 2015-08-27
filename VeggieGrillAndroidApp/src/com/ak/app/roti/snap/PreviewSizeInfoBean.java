package com.ak.app.roti.snap;

import android.hardware.Camera;

public class PreviewSizeInfoBean implements Comparable<PreviewSizeInfoBean> {

	int width = 0;
	int height = 0;
	int area = 0;
	Camera.Size size = null;

	public Camera.Size getSize() {
		return size;
	}

	public void setSize(Camera.Size size) {
		this.size = size;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	@Override
	public int compareTo(PreviewSizeInfoBean arg0) {
		if (this.getArea() > arg0.getArea()) {
			return 1;
		} else if (getArea() < arg0.getArea()) {
			return -1;
		} else {
			return 0;
		}
	}

}
