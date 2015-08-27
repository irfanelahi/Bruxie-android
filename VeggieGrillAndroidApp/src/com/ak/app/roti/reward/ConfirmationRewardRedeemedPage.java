package com.ak.app.roti.reward;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.ak.app.cb.activity.Rewards;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class ConfirmationRewardRedeemedPage {
	private static ConfirmationRewardRedeemedPage screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;
	private EditText enterBarcodeEdit;
	private ImageView enterBtn;
	public static Boolean firstTimeTwitterLoggedin = false;

	public static ConfirmationRewardRedeemedPage getInstance() {
		if (screen == null)
			screen = new ConfirmationRewardRedeemedPage();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.confirmation_page, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	public void onCreate() {
		mHomePage = Tabbars.getInstance();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Rewards Redeemed Confirmation Page")
//				.build());

		TextView firstText = (TextView) mParentLayout
				.findViewById(R.id.textConfirmation1);
		TextView secondText = (TextView) mParentLayout
				.findViewById(R.id.textConfirmation2);
		TextView thirdText = (TextView) mParentLayout
				.findViewById(R.id.textConfirmation3);
		TextView textBack = (TextView) mParentLayout
				.findViewById(R.id.textBack);
		ImageView btnRedeemed = (ImageView) mParentLayout
				.findViewById(R.id.btnRedeemed);
		TextView textLabel = (TextView) mParentLayout
				.findViewById(R.id.textLabel);

		AppConstants.fontDINEngschriftRegularTextView(firstText, 18,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
		AppConstants.fontDINEngschriftRegularTextView(secondText, 14,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
		AppConstants.fontDINEngschriftRegularTextView(thirdText, 10,
				AppConstants.COLORGRAYTEXTFIELD, mHomePage.getAssets());
		AppConstants.fontDINEngschriftRegularTextView(textBack, 12,
				AppConstants.COLORGREEN, mHomePage.getAssets());
		AppConstants.fontDINEngschriftRegularTextView(textLabel, 12,
				AppConstants.COLORGRAYTEXTFIELD, mHomePage.getAssets());

		btnRedeemed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TabHost tabHost = (TabHost) Rewards.getInstance().getParent()
						.findViewById(android.R.id.tabhost);
				if (tabHost != null) {
					tabHost.setCurrentTab(2);
					tabHost.setCurrentTab(3);// showMyGoodiesPage();
				}

			}
		});
	}

	static AlertDialog.Builder alertDialogBuilder;

	public void showShareMsgDialog(String title, final String message,
			Context context) {
		try {
			if (alertDialogBuilder == null) // {
				alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder
					.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									alertDialogBuilder = null;
									dialog.cancel();
								}
							});
			AlertDialog alert = alertDialogBuilder.create();
			if (title.equals("")) {
				alert.setTitle(AppConstants.CONSTANTTITLEMESSAGE);
				alert.setIcon(AlertDialog.BUTTON_NEGATIVE);
			} else {
				alert.setTitle(title);
				alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
			}
			alert.show();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
