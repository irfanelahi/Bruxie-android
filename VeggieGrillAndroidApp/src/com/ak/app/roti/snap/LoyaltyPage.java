package com.ak.app.roti.snap;

import org.kobjects.util.Strings;

import android.R.string;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.ak.app.cb.activity.Snap;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;

public class LoyaltyPage {
	private static LoyaltyPage screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;

	public static LoyaltyPage getInstance() {
		if (screen == null)
			screen = new LoyaltyPage();
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

		ImageView viewMyGoodiesImageView = (ImageView) mParentLayout
				.findViewById(R.id.completegoalreceipt_text_view_my_goodies);
		TextView thankYouText = (TextView) mParentLayout
				.findViewById(R.id.thankYouText);
		thankYouText.setText("YOUR LOYALTY POINTS\nWILL BE\nPROCESSED SOON!");

		AppConstants.fontDinLightTextView(thankYouText, 26,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
		viewMyGoodiesImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (Tabbars.getInstance() != null
							&& Tabbars.getInstance().checkIfLogin()) {
						TabHost tabHost = (TabHost) Snap.getInstance()
								.getParent().findViewById(android.R.id.tabhost);
						if (tabHost != null)
							tabHost.setCurrentTab(1);// showMyGoodiesPage();
						Snap.getInstance().showScanBarcodePage();
					} else {
						Snap.getInstance().setNextViewName(
								"viewMyGoodiesImageView");
						Snap.getInstance().showLoginOptionPage(false, "SNAP");
					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
			}
		});
	}
}
