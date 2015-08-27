package com.ak.app.cb.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akl.zoes.kitchen.util.AppConstants;

public class EarnMoreKarmma {

	private static EarnMoreKarmma screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;

	public static EarnMoreKarmma getInstance() {
		if (screen == null)
			screen = new EarnMoreKarmma();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.earn_more_karmma, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	protected void onCreate() {
		mHomePage = Tabbars.getInstance();

		TextView shareYourStashText = (TextView) mParentLayout
				.findViewById(R.id.shareYourStashText);
		TextView getSocialText = (TextView) mParentLayout
				.findViewById(R.id.getSocialText);
		TextView whackMelText = (TextView) mParentLayout
				.findViewById(R.id.whackMelText);
		RelativeLayout shareYourStashLayout = (RelativeLayout) mParentLayout
				.findViewById(R.id.shareYourStashLayout);
		RelativeLayout getSocialLayout = (RelativeLayout) mParentLayout
				.findViewById(R.id.getSocialLayout);

		AppConstants.fontMyriadMediumTextView(shareYourStashText, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());
		AppConstants.fontMyriadMediumTextView(getSocialText, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());
		AppConstants.fontMyriadMediumTextView(whackMelText, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());

		shareYourStashLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (RotiHomeActivity.getInstance().checkIfLogin()) {
						RotiHomeActivity.getInstance().showReferFriendPage("roti");
					} else {
						RotiHomeActivity.getInstance().showLoginOptionPage(
								false, "ROTIHOMEACTIVITY", "referFriend");
					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);

			}
		});

		getSocialLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (RotiHomeActivity.getInstance().checkIfLogin()) {
						RotiHomeActivity.getInstance().showGetSocialPage();
					} else {
						RotiHomeActivity.getInstance().showLoginOptionPage(
								false, "ROTIHOMEACTIVITY", "getSocial");
					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
			}
		});
	}

}
