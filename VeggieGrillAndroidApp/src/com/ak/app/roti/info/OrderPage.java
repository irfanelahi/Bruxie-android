package com.ak.app.roti.info;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
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

import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.CustomProgressDialog;
import com.akl.zoes.kitchen.util.RefreshListner;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class OrderPage implements RefreshListner {

	private static OrderPage mOrderPage;
	private List<String> urlList;
	private int urlCounter = 0;
	private WebView webView;
	private LayoutInflater mInflater;
	private RelativeLayout mParentLayout;
	private ProgressDialog progressDialog;
	private Tabbars mHomePage;
	private String mURL;

	public static OrderPage getInstance() {
		if (mOrderPage == null)
			mOrderPage = new OrderPage();
		return mOrderPage;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(R.layout.orderpage,
				null);
		return mParentLayout;
	}

	public void onCreate(String Url) {
		mHomePage = Tabbars.getInstance();
//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "online order").build());
		progressDialog = CustomProgressDialog.ctor(mHomePage);

		mURL = Url;
		// loadWebPages(webView, mURL);

		final ImageView browserBackBtn = (ImageView) mParentLayout
				.findViewById(R.id.socialize_imview_browser_back);
		final ImageView browserForwardBtn = (ImageView) mParentLayout
				.findViewById(R.id.socialize_imview_browser_forward);
		ImageView browserReloadBtn = (ImageView) mParentLayout
				.findViewById(R.id.socialize_imview_browser_reload);
		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.odinRoundedBoldTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());
		webView = (WebView) mParentLayout.findViewById(R.id.socialize_webview);
		urlList = new ArrayList<String>();

		browserBackBtn.setBackgroundResource(R.drawable.social_nav_back_idle);
		browserBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (progressDialog != null && !progressDialog.isShowing()) {
					progressDialog.show();
				}
				if (urlList != null && urlCounter - 2 >= 0
						&& urlList.size() > urlCounter - 2) {
					browserForwardBtn
							.setBackgroundResource(R.drawable.social_nav_forward_active);
					webView.loadUrl(urlList.get(urlCounter - 2));
					--urlCounter;
					if (urlCounter < 2)
						browserBackBtn
								.setBackgroundResource(R.drawable.social_nav_back_idle);
					else
						browserBackBtn
								.setBackgroundResource(R.drawable.social_nav_back_active);
				}
			}
		});

		browserForwardBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (progressDialog != null && !progressDialog.isShowing()) {
					progressDialog.show();
				}
				if (urlList != null && urlList.size() > urlCounter) {
					browserBackBtn
							.setBackgroundResource(R.drawable.social_nav_back_active);
					webView.loadUrl(urlList.get(urlCounter));
					++urlCounter;
					if (urlList.size() > urlCounter)
						browserForwardBtn
								.setBackgroundResource(R.drawable.social_nav_forward_active);
					else
						browserForwardBtn
								.setBackgroundResource(R.drawable.social_nav_forward_idle);
					// urlList.remove(urlList.size() - 1);
				}
			}
		});
		browserReloadBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (progressDialog != null && !progressDialog.isShowing()) {
					progressDialog.show();
				}
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (urlList != null && urlList.size() > urlCounter - 1) {
						webView.loadUrl(urlList.get(urlCounter - 1));
					}
				} else {
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
				}
			}
		});
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
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (urlList != null/* && urlList.size() > 0 */) {
					if (!urlList.contains(url)) {
						urlCounter++;
						urlList.add(url);
						if (urlList != null && urlList.size() > 0) {
							if (urlList.size() > 1)
								browserBackBtn
										.setBackgroundResource(R.drawable.social_nav_back_active);
							else
								browserBackBtn
										.setBackgroundResource(R.drawable.social_nav_back_idle);
							if (urlList.size() > urlCounter)
								browserForwardBtn
										.setBackgroundResource(R.drawable.social_nav_forward_active);
							else
								browserForwardBtn
										.setBackgroundResource(R.drawable.social_nav_forward_idle);
						}
					}
				}
				view.loadUrl(url);
				Log.i("", "Processing webview url click...");
				return true;
			}

			public void onPageFinished(WebView view, String url) {
				Log.i("", "Finished loading URL: " + url);
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Log.i("", "onReceivedError loading URL: " + "");
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}
		});
		refreshView();
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	private void loadWebPages(WebView webView, String url) {
		if (progressDialog != null && !progressDialog.isShowing()) {
			progressDialog.show();
		}
		if (AppConstants.isNetworkAvailable(mHomePage)) {
			webView.loadUrl(url);
		} else {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, mHomePage);
		}
	}

	public void refreshView() {
		if (AppConstants.isNetworkAvailable(mHomePage)) {
			webView.loadUrl(mURL);
		} else {
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, mHomePage);
		}
	}

	@Override
	public void notifyRefresh(String className) {
		if (progressDialog != null && !progressDialog.isShowing()) {
			progressDialog.show();
		}
		refreshView();
	}

	// private void loadWebPages(WebView webView, String url) {
	// if (progressDialog != null && !progressDialog.isShowing()) {
	// progressDialog.show();
	// }
	// if (AppConstants.isNetworkAvailable(mHomePage)) {
	// webView.loadUrl(url);
	// } else {
	// if (progressDialog != null && progressDialog.isShowing()) {
	// progressDialog.dismiss();
	// }
	// AppConstants.showMsgDialog("Alert",
	// AppConstants.ERRORNETWORKCONNECTION, mHomePage);
	// }
	// }
}
