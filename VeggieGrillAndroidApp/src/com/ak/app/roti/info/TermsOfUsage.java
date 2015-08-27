package com.ak.app.roti.info;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.Rewards;
import com.ak.app.cb.activity.RotiHomeActivity;
import com.ak.app.cb.activity.Snap;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.RefreshListner;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class TermsOfUsage implements RefreshListner {
	private static TermsOfUsage screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private WebView webView;
	private String mURL;
	ImageView logoutBtn;
	// private Tabbars mHomePage;
	private Activity mHomePage;

	public static TermsOfUsage getInstance() {
		if (screen == null)
			screen = new TermsOfUsage();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.info_termsofusage, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	boolean isInfo = false;
	String mTabName;

	public void onCreate(boolean b, String uRL, final String tabName,
			final String title) {
//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView().set(Fields.SCREEN_NAME, title)
//				.build());

		isInfo = b;
		mTabName = tabName;
		mHomePage = Tabbars.getInstance();
		if (mHomePage == null)
			mHomePage = RotiHomeActivity.getInstance();
		mURL = uRL;
		webView = (WebView) mParentLayout
				.findViewById(R.id.termsofusage_browser_webview);
		TextView backText = (TextView) mParentLayout
				.findViewById(R.id.ltermsofusage_text_back);

		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		topTitle.setText(title.toUpperCase());
		AppConstants.odinRoundedBoldTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());
		backText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tabName.equals("INFO") && Info.getInstance() != null) {
					Info.getInstance().handleBackButton();// showLoginOptionPage(isInfo,
															// mTabName);
				} else if (tabName.equals("SNAP") && Snap.getInstance() != null) {
					// Snap.getInstance().popPreviousView();
					Snap.getInstance().handleBackButton();
					// Snap.getInstance().showLoginOptionPage(isInfo, mTabName);
				} else if (tabName.equals("ROTIHOMEACTIVITY")
						&& RotiHomeActivity.getInstance() != null) {
					// RotiHomeActivity.getInstance().popPreviousView();
					RotiHomeActivity.getInstance().handleBackButton();
					// RotiHomeActivity.getInstance().showLoginOptionPage(isInfo);
				} else if (tabName.equals("REWARDS")
						&& Rewards.getInstance() != null) {
					// Rewards.getInstance().popPreviousView();
					Rewards.getInstance().handleBackButton();
					// Rewards.getInstance().showLoginOptionPage(isInfo);
				}
			}
		});
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);

		// if (Tabbars.getInstance() != null
		// && Tabbars.getInstance().getProgressDialog() != null
		// && !Tabbars.getInstance().getProgressDialog().isShowing())
		// Tabbars.getInstance().getProgressDialog().show();
		// else if (RotiHomeActivity.getInstance() != null
		// && RotiHomeActivity.getInstance().getProgressDialog() != null
		// && !RotiHomeActivity.getInstance().getProgressDialog()
		// .isShowing())
		// RotiHomeActivity.getInstance().getProgressDialog().show();

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.i("", "Processing webview url click...");
				view.loadUrl(url);
				return true;
			}

			public void onPageFinished(WebView view, String url) {
				Log.i("", "Finished loading URL: " + url);
				// if (Tabbars.getInstance() != null
				// && Tabbars.getInstance().getProgressDialog() != null
				// && Tabbars.getInstance().getProgressDialog()
				// .isShowing())
				// Tabbars.getInstance().getProgressDialog().dismiss();
				// else if (RotiHomeActivity.getInstance() != null
				// && RotiHomeActivity.getInstance().getProgressDialog() != null
				// && RotiHomeActivity.getInstance().getProgressDialog()
				// .isShowing())
				// RotiHomeActivity.getInstance().getProgressDialog()
				// .dismiss();
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Log.i("", "onReceivedError loading URL: " + "");
				// if (Tabbars.getInstance() != null
				// && Tabbars.getInstance().getProgressDialog() != null
				// && Tabbars.getInstance().getProgressDialog()
				// .isShowing())
				// Tabbars.getInstance().getProgressDialog().dismiss();
				// else if (RotiHomeActivity.getInstance() != null
				// && RotiHomeActivity.getInstance().getProgressDialog() != null
				// && RotiHomeActivity.getInstance().getProgressDialog()
				// .isShowing())
				// RotiHomeActivity.getInstance().getProgressDialog()
				// .dismiss();
			}
		});
		refreshView();
	}

	@Override
	public void notifyRefresh(String className) {
		if (className.equalsIgnoreCase("showTermsOfUsagePage"))
			refreshView();
	}

	public void refreshView() {
		if (AppConstants.isNetworkAvailable(mHomePage)) {
			webView.loadUrl(mURL);
		} else {
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, mHomePage);
		}
	}
}
