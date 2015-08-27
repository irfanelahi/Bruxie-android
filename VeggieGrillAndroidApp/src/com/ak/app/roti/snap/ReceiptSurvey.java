package com.ak.app.roti.snap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.ak.app.cb.activity.RotiHomeActivity;
import com.ak.app.cb.activity.Snap;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class ReceiptSurvey {
	private static ReceiptSurvey screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;

	public static ReceiptSurvey getInstance() {
		if (screen == null)
			screen = new ReceiptSurvey();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.life_completegoalreceipt, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	public void onCreate() {
		mHomePage = Tabbars.getInstance();
		// Snap.getInstance().handleBackButton();
		// mHomePage.popPreviousView();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Survey Confirmation").build());

		// RotiHomeActivity.getInstance().isReceiptSurvey = true;
		ImageView viewMyGoodiesImageView = (ImageView) mParentLayout
				.findViewById(R.id.completegoalreceipt_text_view_my_goodies);
		TextView thankYouText = (TextView) mParentLayout
				.findViewById(R.id.thankYouText);
		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.odinRoundedBoldTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());
		AppConstants.americanTypewriterTextView(thankYouText, 33,
				AppConstants.COLORBLACKCORNERBAKERYINFO, mHomePage.getAssets());
		viewMyGoodiesImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (Tabbars.getInstance() != null
						&& Tabbars.getInstance().checkIfLogin()) {
					TabHost tabHost = (TabHost) RotiHomeActivity.getInstance()
							.getParent().findViewById(android.R.id.tabhost);
					if (tabHost != null)
						tabHost.setCurrentTab(1);// showMyGoodiesPage();
						// RotiHomeActivity.getInstance().showRew();
				} else {

					RotiHomeActivity.getInstance().showLoginOptionPage(false,
							"ROTIHOMEACTIVITY", "referFriend");
					// Snap.getInstance()
					// .setNextViewName("viewMyGoodiesImageView");
					// Snap.getInstance().showLoginOptionPage(false, "SNAP");
				}

			}
		});
	}
}
