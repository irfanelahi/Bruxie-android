
package com.ak.app.roti.info;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.RotiHomeActivity;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class Skipsurvey {
	private static Skipsurvey screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;

	public static Skipsurvey getInstance() {
		if (screen == null)
			screen = new Skipsurvey();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.submit, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	ImageView logoutBtn;

	public void onCreate() {
		mHomePage = Tabbars.getInstance();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "About Us").build());


		RelativeLayout backarrow;
		backarrow=(RelativeLayout)mParentLayout.findViewById(R.id.backarrow);
		backarrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RotiHomeActivity.getInstance().onBackPressed();
			
			}
		});
		

		TextView thanku = (TextView) mParentLayout
				.findViewById(R.id.thanku);
		
		TextView thanku2 = (TextView) mParentLayout
				.findViewById(R.id.thanku2);
		
		
		
		
		
		// TextView backText = (TextView) mParentLayout
		// .findViewById(R.id.aboutroti_text_back);
		// backText.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Info.getInstance().handleBackButton();
		// }
		// });
		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());

		//AppConstants.gothamNarrowBoldTextView(a1Text, 18, AppConstants.COLORBLACKCORNERBAKERYINFO,  mHomePage.getAssets());
		
		AppConstants.kingthingsTextView(thanku, 48, AppConstants.COLOR_BLACK_PAGETITLE, mHomePage.getAssets());
		AppConstants.kingthingsTextView(thanku2, 48, AppConstants.COLOR_BLACK_PAGETITLE, mHomePage.getAssets());
		
	
	
	}
}
