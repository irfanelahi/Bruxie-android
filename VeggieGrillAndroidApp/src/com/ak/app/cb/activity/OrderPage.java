package com.ak.app.cb.activity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;

public class OrderPage extends Activity {

	private static OrderPage mOrderPage;
	private List<String> urlList;
	private int urlCounter = 0;
	private WebView webView;
	private ProgressDialog progressDialog;

	public static OrderPage getInstance() {
		return mOrderPage;
	}

//	ImageView tweetBtn;
//	ImageView fbBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderpage);
		mOrderPage = this;
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading...");
		loadWebPages(webView, AppConstants.URL_TWITTER_PAGE);
//		tweetBtn = (ImageView) findViewById(R.id.socialize_imview_browser_twitter);
//		fbBtn = (ImageView) findViewById(R.id.socialize_imview_browser_facebook);
		final ImageView browserBackBtn = (ImageView) findViewById(R.id.socialize_imview_browser_back);
		final ImageView browserForwardBtn = (ImageView) findViewById(R.id.socialize_imview_browser_forward);
		ImageView browserReloadBtn = (ImageView) findViewById(R.id.socialize_imview_browser_reload);
		webView = (WebView) findViewById(R.id.socialize_webview);
		urlList = new ArrayList<String>();

//		tweetBtn.setBackgroundResource(R.drawable.twitter_btn);
//		fbBtn.setBackgroundResource(R.drawable.fb_btn_active);
		browserBackBtn.setBackgroundResource(R.drawable.social_nav_back_idle);

//		tweetBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				loadWebPages(webView, AppConstants.URL_TWITTER_PAGE);
//				fbBtn.setBackgroundResource(R.drawable.fb_btn);
//				tweetBtn.setBackgroundResource(R.drawable.twitter_btn_active);
//			}
//		});
//
//		fbBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				loadWebPages(webView, AppConstants.URL_FB_PAGE);
//				fbBtn.setBackgroundResource(R.drawable.fb_btn_active);
//				tweetBtn.setBackgroundResource(R.drawable.twitter_btn);
//			}
//		});
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
				if (AppConstants.isNetworkAvailable(mOrderPage)) {
					if (urlList != null && urlList.size() > urlCounter - 1) {
						webView.loadUrl(urlList.get(urlCounter - 1));
					}
				} else {
					AppConstants
							.showMsgDialog("Alert",
									AppConstants.ERRORNETWORKCONNECTION,
									mOrderPage);
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
	
	@Override
	protected void onResume() {
		Tabbars.getInstance().stopGPS();
		refreshView();
		super.onResume();
	}

//	@Override
//	protected void onRestart() {
//		super.onRestart();
//	}

	public void refreshView() {
		if (AppConstants.isNetworkAvailable(mOrderPage)) {
			loadWebPages(webView, AppConstants.URL_FB_PAGE);
//			tweetBtn.setBackgroundResource(R.drawable.twitter_btn);
//			fbBtn.setBackgroundResource(R.drawable.fb_btn_active);
		} else {
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, mOrderPage);
		}
	}

	private void loadWebPages(WebView webView, String url) {
		if (progressDialog != null && !progressDialog.isShowing()) {
			progressDialog.show();
		}
		if (AppConstants.isNetworkAvailable(mOrderPage)) {
			webView.loadUrl(url);
		} else {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, mOrderPage);
		}
	}
}
