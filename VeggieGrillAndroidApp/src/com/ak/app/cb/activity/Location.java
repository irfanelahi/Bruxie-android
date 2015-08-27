package com.ak.app.cb.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ak.app.cb.activity.R;
import com.ak.app.roti.info.LocationPage;
import com.ak.app.roti.info.OrderPage;
import com.akl.zoes.kitchen.util.AppConstants;

public class Location extends Activity {
	private static Location mLocationPage;
	private LayoutInflater mInflater;
	private LinearLayout mParentLayout;
	private List<View> locationViewList = new ArrayList<View>();

	public static Location getInstance() {
		return mLocationPage;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		mLocationPage = this;
		AppConstants.changeScreenBrightnessToDefault(mLocationPage);
		mInflater = LayoutInflater.from(this);
		mParentLayout = (LinearLayout) findViewById(R.id.info_linear_container);
		Log.d("", "Logged Here1");
		showLocationPage();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showLocationPage();
	}

	public void showOrderWeb(String URL) { 
		OrderPage OrderWeb = OrderPage.getInstance();
		RelativeLayout infoParentLayout = OrderWeb.setInflater(mInflater);
		OrderWeb.onCreate(URL);

		setViewParams(infoParentLayout, "showOrderWeb", locationViewList);
	}

	public void showLocationPage() {
		LocationPage InfoView = LocationPage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.isRequestFromOrderTab = true;
		InfoView.onCreate();
		setViewParams(infoParentLayout, "scanPageView", locationViewList);
	}

	private void setViewParams(View view, String tagName, List<View> viewList) {
		mParentLayout.removeAllViews();
		view.setTag(tagName);
		checkIfViewExist(view, locationViewList);
		mParentLayout.addView(view);
	}

	@SuppressWarnings("unused")
	private void checkIfViewExist(View view, List<View> listView) {
		String currentViewName = (String) view.getTag();
		boolean isPresent = false;
		if (listView != null && listView.size() > 0) {
			for (int i = 0; i < listView.size(); i++) {
				String compareViewName = (String) listView.get(i).getTag();
				if (compareViewName.equalsIgnoreCase(currentViewName))
					isPresent = true;
				break;
			}
		}
		if (!isPresent)
			listView.add(view);
	}

	public boolean handleBackButton() {
		boolean isLast = false;
		isLast = setBackButtonHandledView(locationViewList, 0);
		return isLast;
	}

	private boolean setBackButtonHandledView(List<View> listView, int status) {
		boolean isLast = false;
		if (listView != null && listView.size() > 1) {
			mParentLayout.removeAllViews();
			mParentLayout.addView(listView.get(listView.size() - 2));
			listView.remove(listView.size() - 1);
			isLast = true;
		} else if (listView != null && listView.size() == 1) {
			mParentLayout.removeAllViews();
			mParentLayout.addView(listView.get(0));
		} else
			isLast = false;
		return isLast;
	}

	public void exitAppFunc() {
		RotiHomeActivity.getInstance().oPenTabView(
				0);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (locationViewList != null && locationViewList.size() > 1) {
				handleBackButton();
				doRefresh();
			} else
				exitAppFunc();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// i modified doRefresh function, so this is different with doRefresh
	// function on other pages
	private void doRefresh() {
		boolean isFirstPage = false;
		while (!isFirstPage)
			try {
				isFirstPage = !handleBackButton();
			} catch (Exception e) {
				isFirstPage = true;
			}

		showLocationPage();
	}

}
