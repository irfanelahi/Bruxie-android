package com.ak.app.roti.info;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.GeolocationPermissions;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.RefreshListner;
import com.akl.zoes.kitchen.util.globalVar;

public class webView implements RefreshListner {
	private static webView screen;
	// private static webView mWebView;
	private List<String> urlList;
	private int urlCounter = 0;
	private WebView webView;
	// private ProgressDialog progressDialog;
	private LayoutInflater mInflater;
	private RelativeLayout mParentLayout;
	public boolean isWebView = false;
	public boolean isCheckWebView = false;

	private ProgressBar pageProgressBar;

	public static webView getInstance() {
		if (screen == null)
			screen = new webView();
		return screen;
	}

	public class GeoWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// When user clicks a hyperlink, load in the existing WebView
			view.loadUrl(url);
			return true;
		}
	}

	/**
	 * WebChromeClient subclass handles UI-related calls Note: think chrome as
	 * in decoration, not the Chrome browser
	 */
	public class GeoWebChromeClient extends WebChromeClient {
		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
				GeolocationPermissions.Callback callback) {
			// Always grant permission since the app itself requires location
			// permission and the user has therefore already granted it
			callback.invoke(origin, true, false);
		}
	}

	// added for example
	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(R.layout.webview,
				null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	private Tabbars mHomePage;

	// public void onDestroy() {
	//
	// }

	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(String uRL) {
		int showToinfo = globalVar.getInstance().getMyVar();
		isWebView = true;
		isCheckWebView = true;
		if (showToinfo == 0) {
			Info.getInstance().handleBackButton();
		} else {
			globalVar.getInstance().setMyVar(0);
			mHomePage = Tabbars.getInstance();
			// progressDialog = new ProgressDialog(mHomePage);
			// progressDialog.setMessage("Loading...");
			pageProgressBar = (ProgressBar) mParentLayout
					.findViewById(R.id.pageLoadingIndicator);
			webView = (WebView) mParentLayout
					.findViewById(R.id.socialize_webview);
			urlList = new ArrayList<String>();

			final ImageView browserBackBtn = (ImageView) mParentLayout
					.findViewById(R.id.socialize_imview_browser_back);
			final ImageView browserForwardBtn = (ImageView) mParentLayout
					.findViewById(R.id.socialize_imview_browser_forward);
			ImageView browserReloadBtn = (ImageView) mParentLayout
					.findViewById(R.id.socialize_imview_browser_reload);

			WebSettings settings = webView.getSettings();
			settings.setJavaScriptEnabled(true);
			settings.setJavaScriptCanOpenWindowsAutomatically(true);
			browserBackBtn.setImageResource(R.drawable.social_nav_back_idle);
			webView.setWebViewClient(new GeoWebViewClient());
			webView.getSettings().setGeolocationEnabled(true);
			webView.requestFocus(View.FOCUS_DOWN);
			browserBackBtn.setImageResource(R.drawable.social_nav_back_idle);
			webView.setOnTouchListener(new View.OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					case MotionEvent.ACTION_UP:
						if (!v.hasFocus()) {
							v.requestFocus();
						}
						break;
					}
					return false;
				}
			});

			// webView.getSettings().setAppCacheEnabled(true);
			// webView.getSettings().setDatabaseEnabled(true);
			// webView.getSettings().setDomStorageEnabled(true);
			try {
				Method m = WebSettings.class.getMethod("setDomStorageEnabled",
						new Class[] { boolean.class });
				m.invoke(webView.getSettings(), true);
			} catch (Exception e) {
			}

			settings.setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");

			try {
				Method m = WebSettings.class.getMethod("setDomStorageEnabled",
						new Class[] { boolean.class });
				m.invoke(webView.getSettings(), true);
			} catch (Exception e) {
			}

			browserBackBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// if (progressDialog != null &&
					// !progressDialog.isShowing()) {
					// progressDialog.show();
					// }
					pageProgressBar.setVisibility(View.VISIBLE);
					if (urlList != null && urlCounter - 2 >= 0
							&& urlList.size() > urlCounter - 2) {
						Log.i("url List", urlList.toString());
						Log.i("url Counter", "" + urlCounter);
						browserForwardBtn
								.setImageResource(R.drawable.social_nav_forward_active);
						webView.loadUrl(urlList.get(urlCounter - 2));
						--urlCounter;
						if (urlCounter < 2)
							browserBackBtn
									.setImageResource(R.drawable.social_nav_back_idle);
						else
							browserBackBtn
									.setImageResource(R.drawable.social_nav_back_active);
					}
				}
			});
			browserForwardBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// if (progressDialog != null &&
					// !progressDialog.isShowing()) {
					// progressDialog.show();
					// }
					pageProgressBar.setVisibility(View.VISIBLE);
					if (urlList != null && urlList.size() > urlCounter) {
						Log.i("url List", urlList.toString());
						Log.i("url Counter", "" + urlCounter);
						browserBackBtn
								.setImageResource(R.drawable.social_nav_back_active);
						webView.loadUrl(urlList.get(urlCounter));
						++urlCounter;
						if (urlList.size() > urlCounter)
							browserForwardBtn
									.setImageResource(R.drawable.social_nav_forward_active);
						else
							browserForwardBtn
									.setImageResource(R.drawable.social_nav_forward_idle);
					}
				}
			});
			browserReloadBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// if (progressDialog != null &&
					// !progressDialog.isShowing()) {
					// progressDialog.show();
					// }
					pageProgressBar.setVisibility(View.VISIBLE);
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

			webView.setWebChromeClient(new MyWebChromeClient());
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

								// if (urlList.size() > 1)
								// browserBackBtn
								// .setBackgroundResource(R.drawable.social_nav_back_active);
								// else
								// browserBackBtn
								// .setBackgroundResource(R.drawable.social_nav_back_idle);
								// if (urlList.size() > urlCounter)
								// browserForwardBtn
								// .setBackgroundResource(R.drawable.social_nav_forward_active);
								// else
								// browserForwardBtn
								// .setBackgroundResource(R.drawable.social_nav_forward_idle);
							}
						}
					}
					view.loadUrl(url);
					Log.i("", "Processing webview url click...");
					return true;
				}

				public void onPageFinished(WebView view, String url) {
					Log.i("", "Finished loading URL: " + url);
					// if (progressDialog != null && progressDialog.isShowing())
					// {
					// progressDialog.dismiss();
					// }
					pageProgressBar.setVisibility(View.GONE);
				}

				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
					Log.i("", "onReceivedError loading URL: " + "");
					// if (progressDialog != null && progressDialog.isShowing())
					// {
					// progressDialog.dismiss();
					// }
					pageProgressBar.setVisibility(View.GONE);
				}
			});
			refreshView(uRL);
		}
	}

	public void backBrowser() {
		// if (progressDialog != null && !progressDialog.isShowing()) {
		// progressDialog.show();
		pageProgressBar.setVisibility(View.VISIBLE);
		webView.goBack();
		// }
		// if (urlList != null && urlCounter - 2 >= 0
		// && urlList.size() > urlCounter - 2) {
		// // browserForwardBtn
		// // .setBackgroundResource(R.drawable.social_nav_forward_active);
		// webView.loadUrl(urlList.get(urlCounter - 2));
		// --urlCounter;
		// // if (urlCounter < 2)
		// //// browserBackBtn
		// //// .setBackgroundResource(R.drawable.social_nav_back_idle);
		// // else
		// //// browserBackBtn
		// //// .setBackgroundResource(R.drawable.social_nav_back_active);
		// }
	}

	// @Override
	// protected void onResume() {
	// Tabbars.getInstance().stopGPS();
	// refreshView();
	// super.onResume();
	// }

	public void refreshView(String uRL) {
		if (AppConstants.isNetworkAvailable(mHomePage)) {
			loadWebPages(webView, uRL);
		} else {
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, mHomePage);
		}
	}

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

	final class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				final JsResult result) {
			new AlertDialog.Builder(mHomePage)
					.setTitle(AppConstants.CONSTANTTITLEMESSAGE)
					.setMessage(message)
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									result.confirm();
								}
							})
					.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									result.cancel();
								}
							}).create().show();

			return true;
		}

		@Override
		public void onGeolocationPermissionsShowPrompt(final String origin,
				final Callback callback) {
			// TODO Auto-generated method stub
			super.onGeolocationPermissionsShowPrompt(origin, callback);
			Log.i("TAG", "onGeolocationPermissionsShowPrompt()");

			final boolean remember = false;
			AlertDialog.Builder builder = new AlertDialog.Builder(mHomePage);
			builder.setTitle("Locations");
			builder.setMessage("Would like to use your Current Location ")
					.setCancelable(true)
					.setPositiveButton("Allow",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// origin, allow, remember
									callback.invoke(origin, true, remember);
								}
							})
					.setNegativeButton("Don't Allow",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// origin, allow, remember
									callback.invoke(origin, false, remember);
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}

	}

	@Override
	public void notifyRefresh(String className) {
		// TODO Auto-generated method stub

	}
}
