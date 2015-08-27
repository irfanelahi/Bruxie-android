package com.ak.app.roti.info;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.RotiHomeActivity;
import com.ak.app.cb.activity.Snap;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.app.roti.bean.LocationBean;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.RefreshListner;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class howToGet implements RefreshListner {
	private static howToGet screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	LinearLayout lifePageCellContainerLayout;
	List<LocationBean> listThisWeekBean;
	private List<View> snapViewList = new ArrayList<View>();

	public static howToGet getInstance() {
		if (screen == null)
			screen = new howToGet();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.howtogetsomelove, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	private Tabbars mHomePage;

	public void onCreate() {
		mHomePage = Tabbars.getInstance();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Info, Tutorial").build());

		//

		// ImageView getStartedBtn = (ImageView) mParentLayout
		// .findViewById(R.id.getStartedBtn);

		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);

		TextView firstStep = (TextView) mParentLayout
				.findViewById(R.id.step_first);
		TextView secondStep = (TextView) mParentLayout
				.findViewById(R.id.step_second);
		TextView thirdStep = (TextView) mParentLayout
				.findViewById(R.id.step_third);
		TextView fourStep = (TextView) mParentLayout
				.findViewById(R.id.step_four);
		TextView fiveStep = (TextView) mParentLayout
				.findViewById(R.id.step_five);
		TextView sixStep = (TextView) mParentLayout.findViewById(R.id.step_six);

		AppConstants.odinRoundedBoldTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());
		AppConstants.americanTypewriterTextView(firstStep, 13,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());
		AppConstants.americanTypewriterTextView(secondStep, 13,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());
		AppConstants.americanTypewriterTextView(thirdStep, 13,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());
		AppConstants.americanTypewriterTextView(fourStep, 13,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());
		AppConstants.americanTypewriterTextView(fiveStep, 13,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());
		AppConstants.americanTypewriterTextView(sixStep, 13,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());

		// howToGetLove.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Info.getInstance().handleBackButton();
		// SnapLocation socializePageView = SnapLocation.getInstance();
		// RelativeLayout socializePageParentLayout = socializePageView
		// .setInflater(mInflater);
		// socializePageView.onCreate();
		// setViewParams(socializePageParentLayout,
		// "showSelectLocationPage", snapViewList);
		// RotiHomeActivity.getInstance().oPenTabView(2);
		//
		// }
		// });

		// getStartedBtn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// boolean isTutorialShown = mHomePage.getPreference().getBoolean(
		// AppConstants.PREF_SNAP_ISNOTOPENSTARTPAGE, false);
		//
		// if (isTutorialShown) {
		// Tabbars.getInstance().showTabs();
		// Tabbars.getInstance().stopGPS();
		// RotiHomeActivity.getInstance().oPenTabView(2);
		// } else {
		//
		// RotiHomeActivity.getInstance().oPenTabView(2);
		// Snap.getInstance().showScanTutorialPage();
		// Editor edit = mHomePage.getPreferenceEditor();
		// edit.putBoolean(AppConstants.PREF_SNAP_ISNOTOPENSTARTPAGE,
		// true);
		// edit.commit();
		// Tabbars.getInstance().showTabs();
		//
		// }
		// }
		// });

		// backText.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Info.getInstance().handleBackButton();
		// }
		// });
		// refreshView();
	}

	private void setViewParams(View view, String tagName, List<View> viewList) {
		mParentLayout.removeAllViews();
		view.setTag(tagName);
		checkIfViewExist(view, snapViewList);
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

	@Override
	public void notifyRefresh(String className) {
		// TODO Auto-generated method stub

	}

	// public void refreshView() {
	// if (AppConstants.CheckEnableGPS(mHomePage)) {
	// // Tabbars.getInstance().startGPS();
	// if (AppConstants.isNetworkAvailable(mHomePage)) {
	// new fetchLocationFromServer().execute("");
	// } else
	// AppConstants.showMsgDialog("",
	// AppConstants.ERRORNETWORKCONNECTION, mHomePage);
	// } else
	// AppConstants.showMsgDialog("", AppConstants.ERRORLOCATIONSERVICES,
	// mHomePage);
	// }

}
