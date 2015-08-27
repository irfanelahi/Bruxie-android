package com.ak.app.roti.info;

import java.lang.reflect.Method;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class FAQPage {
	private static FAQPage screen;
	private RelativeLayout mParentLayout;
	// private ProgressDialog progressDialog;
	private LayoutInflater mInflater;
	private WebView webView;
	private Tabbars mHomePage;

	private ProgressBar pageProgressBar;

	public static FAQPage getInstance() {
		if (screen == null)
			screen = new FAQPage();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(R.layout.info_faq,
				null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	ImageView logoutBtn;

	private void loadWebPages(WebView webView, String url) {
		// if (progressDialog != null && !progressDialog.isShowing()) {
		// progressDialog.show();
		// }
		pageProgressBar.setVisibility(View.VISIBLE);
		if (AppConstants.isNetworkAvailable(mHomePage)) {
			webView.loadUrl(url);
		} else {
			// if (progressDialog != null && progressDialog.isShowing()) {
			// progressDialog.dismiss();
			// }
			pageProgressBar.setVisibility(View.GONE);
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, mHomePage);
		}
	}

	public void onCreate() {
		mHomePage = Tabbars.getInstance();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView().set(Fields.SCREEN_NAME, "FAQ")
//				.build());

		TextView faqContact = (TextView) mParentLayout
				.findViewById(R.id.faqContact);
		
		ImageView faqContact2 = (ImageView) mParentLayout
				.findViewById(R.id.contactus);
		
		
		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());

		
		AppConstants.fontDinMediumTextView(faqContact, 18,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());



		RelativeLayout backarrow;
		backarrow=(RelativeLayout)mParentLayout.findViewById(R.id.backarrow);
		backarrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Info.getInstance().onBackPressed();
			
			}
		});
		

		RelativeLayout emailBar = (RelativeLayout) mParentLayout
				.findViewById(R.id.faqEmailLayout);

		
		faqContact2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			Info.getInstance().onContactUsCreate();
			}
		});
		
		
		emailBar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					mHomePage.doNotFinishAllActivities = true;
					Intent intent = new Intent(Intent.ACTION_VIEW);
					Uri data = Uri.parse("mailto:"
							+ AppConstants.EMAILFAQ_CONTACT_US + "?subject="
							+ AppConstants.EMAILSUBJECTFAQ + "&body="
							+ getDeviceName());
					intent.setData(data);
					mHomePage.startActivity(intent);
				} catch (Exception e) {
				}
			}
		});

		// progressDialog = new ProgressDialog(mHomePage);
		// progressDialog.setMessage("Loading...");

		pageProgressBar = (ProgressBar) mParentLayout
				.findViewById(R.id.pageLoadingIndicator);

		webView = (WebView) mParentLayout.findViewById(R.id.socialize_webview);
		// loadWebPages(webView, AppConstants.URL_FAQ);
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		try {
			Method m = WebSettings.class.getMethod("setDomStorageEnabled",
					new Class[] { boolean.class });
			m.invoke(webView.getSettings(), true);
		} catch (Exception e) {
		}
		settings.setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");

		webView.setWebViewClient(new WebViewClient() {

			public void onPageFinished(WebView view, String url) {
				Log.i("", "Finished loading URL: " + url);
				// if (progressDialog != null && progressDialog.isShowing()) {
				// progressDialog.dismiss();
				// }
				pageProgressBar.setVisibility(View.GONE);
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Log.i("", "onReceivedError loading URL: " + "");
				// if (progressDialog != null && progressDialog.isShowing()) {
				// progressDialog.dismiss();
				// }
				pageProgressBar.setVisibility(View.GONE);
			}
		});
		refreshView();

	}

	public void refreshView() {
		if (AppConstants.isNetworkAvailable(mHomePage)) {
			loadWebPages(webView, AppConstants.URL_FAQ);
			// tweetBtn.setBackgroundResource(R.drawable.twitter_btn);
			// fbBtn.setBackgroundResource(R.drawable.fb_btn_active);
		} else {
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, mHomePage);
		}
	}

	public String getDeviceName() {
		String texts = "\n\n";

		try {
			PackageInfo pInfo;
			pInfo = mHomePage.getPackageManager().getPackageInfo(
					mHomePage.getPackageName(), 0);
			String version = pInfo.versionName;
			String androidOS = Build.VERSION.RELEASE;
			String manufacturer = Build.MANUFACTURER;
			String model = Build.MODEL;
			if (model.startsWith(manufacturer)) {
				texts = texts + capitalize(model);
			} else {
				texts = texts + capitalize(manufacturer) + " " + model;
			}
			texts = texts + " " + androidOS + " \nApp Version " + version;
			String email = mHomePage.getPreference().getString(
					AppConstants.PREFLOGINID, "");
			if (!email.equals(""))
				texts = texts + "  \nEmail used " + email;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return texts;
	}

	private String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}
}
