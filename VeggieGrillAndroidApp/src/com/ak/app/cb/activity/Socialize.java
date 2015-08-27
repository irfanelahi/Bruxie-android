package com.ak.app.cb.activity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.CustomProgressDialog;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class Socialize extends Activity {

	private static Socialize mSocializePage;
	private List<String> urlList;
	private int urlCounter = 0;
	private WebView webView;
	private ProgressDialog progressDialog;
	private LayoutInflater mInflater;
	private RelativeLayout mParentLayout;

	public static Socialize getInstance() {
		return mSocializePage;
	}

	ImageView tweetBtn;
	ImageView fbBtn;
	ImageView instaBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.socialpage);
		mSocializePage = this;
		progressDialog = CustomProgressDialog.ctor(this);
		// progressDialog = new ProgressDialog(this);
		// progressDialog.setMessage("Loading...");

		tweetBtn = (ImageView) findViewById(R.id.socialize_imview_browser_twitter);
		fbBtn = (ImageView) findViewById(R.id.socialize_imview_browser_facebook);
		instaBtn = (ImageView) findViewById(R.id.socialize_imview_browser_instagram);
		final ImageView browserBackBtn = (ImageView) findViewById(R.id.socialize_imview_browser_back);
		final ImageView browserForwardBtn = (ImageView) findViewById(R.id.socialize_imview_browser_forward);
		ImageView browserReloadBtn = (ImageView) findViewById(R.id.socialize_imview_browser_reload);
		webView = (WebView) findViewById(R.id.socialize_webview);
		urlList = new ArrayList<String>();

		TextView topTitle = (TextView) findViewById(R.id.topTitle);
		AppConstants.odinRoundedBoldTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, this.getAssets());

		tweetBtn.setImageResource(R.drawable.twitter_social_inactive);
		instaBtn.setImageResource(R.drawable.instagram_social_inactive);
		fbBtn.setImageResource(R.drawable.fb_social_active);
		browserBackBtn.setImageResource(R.drawable.social_nav_back_idle);

		instaBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Tracker tracker = GoogleAnalytics.getInstance(mSocializePage)
//						.getTracker(AppConstants.GA_TRACK_ID);
//				tracker.send(MapBuilder.createAppView()
//						.set(Fields.SCREEN_NAME, "Social, Instagram").build());
				fbBtn.setImageResource(R.drawable.fb_social_inactive);
				tweetBtn.setImageResource(R.drawable.twitter_social_inactive);
				instaBtn.setImageResource(R.drawable.instagram_social_active);
				loadWebPages(webView, AppConstants.URL_INSTAGRAM_PAGE);
			}
		});

		tweetBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Tracker tracker = GoogleAnalytics.getInstance(mSocializePage)
//						.getTracker(AppConstants.GA_TRACK_ID);
//				tracker.send(MapBuilder.createAppView()
//						.set(Fields.SCREEN_NAME, "Social, Twitter").build());
				fbBtn.setImageResource(R.drawable.fb_social_inactive);
				tweetBtn.setImageResource(R.drawable.twitter_social_active);
				instaBtn.setImageResource(R.drawable.instagram_social_inactive);
				loadWebPages(webView, AppConstants.URL_TWITTER_PAGE);
			}
		});

		fbBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Tracker tracker = GoogleAnalytics.getInstance(mSocializePage)
//						.getTracker(AppConstants.GA_TRACK_ID);
//				tracker.send(MapBuilder.createAppView()
//						.set(Fields.SCREEN_NAME, "Social, Facebook").build());
				fbBtn.setImageResource(R.drawable.fb_social_active);
				tweetBtn.setImageResource(R.drawable.twitter_social_inactive);
				instaBtn.setImageResource(R.drawable.instagram_social_inactive);
				loadWebPages(webView, AppConstants.URL_FB_PAGE);
			}
		});

		browserBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (progressDialog != null && !progressDialog.isShowing()) {
					progressDialog.show();
				}
				// progressBar.setVisibility(View.VISIBLE);
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
				// progressBar.setVisibility(View.VISIBLE);
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
				// progressBar.setVisibility(View.VISIBLE);
				if (progressDialog != null && !progressDialog.isShowing()) {
					progressDialog.show();
				}
				if (AppConstants.isNetworkAvailable(mSocializePage)) {
					if (urlList != null && urlList.size() > urlCounter - 1) {
						webView.loadUrl(urlList.get(urlCounter - 1));
					}
				} else {
					AppConstants
							.showMsgDialog("Alert",
									AppConstants.ERRORNETWORKCONNECTION,
									mSocializePage);
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
										.setImageResource(R.drawable.social_nav_back_active);
							else
								browserBackBtn
										.setImageResource(R.drawable.social_nav_back_idle);
							if (urlList.size() > urlCounter)
								browserForwardBtn
										.setImageResource(R.drawable.social_nav_forward_active);
							else
								browserForwardBtn
										.setImageResource(R.drawable.social_nav_forward_idle);
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
				// progressBar.setVisibility(View.GONE);
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Log.i("", "onReceivedError loading URL: " + "");
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				// progressBar.setVisibility(View.GONE);
			}
		});
		refreshView();
	}

	@Override
	protected void onResume() {
		com.facebook.AppEventsLogger.activateApp(getApplicationContext(),
				AppConstants.FACEBOOK_APPID);
		Tabbars.getInstance().stopGPS();
		refreshView();
		super.onResume();
	}

	// @Override
	// protected void onRestart() {
	// super.onRestart();
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (progressDialog != null && !progressDialog.isShowing())
				progressDialog.show();
			// progressBar.setVisibility(View.VISIBLE);
			webView.goBack();
			// }
			// if (urlList != null && urlCounter - 2 >= 0
			// && urlList.size() > urlCounter - 2) {
			// // browserForwardBtn
			// // .setBackgroundResource(R.drawable.social_nav_forward_active);
			// webView.loadUrl(urlList.get(urlCounter - 2));
			// --urlCounter;
			// // if (urlCounter < 2)
			// // browserBackBtn
			// // .setBackgroundResource(R.drawable.social_nav_back_idle);
			// // else
			// // browserBackBtn
			// // .setBackgroundResource(R.drawable.social_nav_back_active);
			// }
		}
		return super.onKeyDown(keyCode, event);
	}

	public void refreshView() {
		if (AppConstants.isNetworkAvailable(mSocializePage)) {
//			Tracker tracker = GoogleAnalytics.getInstance(mSocializePage)
//					.getTracker(AppConstants.GA_TRACK_ID);
//			tracker.send(MapBuilder.createAppView()
//					.set(Fields.SCREEN_NAME, "Social, Facebook").build());
			loadWebPages(webView, AppConstants.URL_FB_PAGE);
			// tweetBtn.setBackgroundResource(R.drawable.twitter_btn);
			// fbBtn.setBackgroundResource(R.drawable.fb_btn_active);
		} else {
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, mSocializePage);
		}
	}

	private void loadWebPages(WebView webView, String url) {
		if (progressDialog != null && !progressDialog.isShowing()) {
			progressDialog.show();
		}
		// progressBar.setVisibility(View.VISIBLE);
		if (AppConstants.isNetworkAvailable(mSocializePage)) {
			webView.loadUrl(url);
		} else {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			// progressBar.setVisibility(View.GONE);
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, mSocializePage);
		}
	}

}
