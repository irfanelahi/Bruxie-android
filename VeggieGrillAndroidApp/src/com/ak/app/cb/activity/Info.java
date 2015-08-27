package com.ak.app.cb.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ak.app.cb.activity.R;
import com.ak.app.roti.info.AboutRoti;
import com.ak.app.roti.info.ChangePasswordPage;
import com.ak.app.roti.info.FAQPage;
import com.ak.app.roti.info.FbLogin;
import com.ak.app.roti.info.ForgetPassword;
import com.ak.app.roti.info.InfoMainPage;
import com.ak.app.roti.info.LocationPage;
import com.ak.app.roti.info.LoginOption;
import com.ak.app.roti.info.LoginPage;
import com.ak.app.roti.info.OrderPage;
import com.ak.app.roti.info.PromoPage;
import com.ak.app.roti.info.SettingInfo;
import com.ak.app.roti.info.SignUp;
import com.ak.app.roti.info.SocialFeed;
import com.ak.app.roti.info.TermsOfUsage;
import com.ak.app.roti.info.howToGet;
import com.ak.app.roti.info.socialFeedWebView;
import com.ak.app.roti.info.webView;
import com.ak.app.roti.reward.PaymentAcivityFragment;
import com.ak.app.roti.reward.ViewActivity;
import com.akl.zoes.kitchen.util.RefreshListner;
import com.akl.zoes.kitchen.util.globalVar;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;
import com.akl.zoes.kitchen.util.AppConstants;

//import com.ak.app.roti.snap.SnapStart;

public class Info extends Activity {
	private static Info mInfoPage;
	private LayoutInflater mInflater;
	private LinearLayout mParentLayout;
	private List<View> infoViewList = new ArrayList<View>();

	public static Info getInstance() {
		return mInfoPage;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// try {
		// PackageInfo info = getPackageManager().getPackageInfo(
		// "com.ak.app.neckter.activity",
		// PackageManager.GET_SIGNATURES);
		// for (Signature signature : info.signatures) {
		// MessageDigest md = MessageDigest.getInstance("SHA");
		// md.update(signature.toByteArray());
		// Log.d("KeyHash:",
		// Base64.encodeToString(md.digest(), Base64.DEFAULT));
		// Toast.makeText(getBaseContext(), Base64.encodeToString(md.digest(),
		// Base64.DEFAULT),
		// Toast.LENGTH_LONG).show();
		//
		// new AlertDialog.Builder(this)
		// .setTitle("Delete entry")
		// .setMessage(Base64.encodeToString(md.digest(), Base64.DEFAULT))
		// .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int which) {
		// // continue with delete
		// }
		// })
		// .setNegativeButton("No", new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int which) {
		// // do nothing
		// }
		// })
		// .show();
		//
		//
		// }
		// } catch (NameNotFoundException e) {
		//
		// } catch (NoSuchAlgorithmException e) {
		//
		// }
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infopage);
		mInfoPage = this;
		mInflater = LayoutInflater.from(this);
		mParentLayout = (LinearLayout) findViewById(R.id.info_linear_container);
		showInfoMainPage();
	}

	@Override
	protected void onResume() {
		super.onResume();
		com.facebook.AppEventsLogger.activateApp(getApplicationContext(),
				AppConstants.FACEBOOK_APPID);
//		Tabbars.getInstance().stopGPS();
		// showInfoMainPage();
		if (infoViewList != null && infoViewList.size() > 0) {
			doRefresh(infoViewList.get(infoViewList.size() - 1).getTag()
					.toString());
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public void showInfoMainPage() {
		InfoMainPage InfoView = InfoMainPage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate();
		setViewParams("INFO", infoParentLayout, "showInfoMainPage",
				infoViewList);
	}

	public void showOrderWeb(String URL) {
		OrderPage OrderWeb = OrderPage.getInstance();
		RelativeLayout infoParentLayout = OrderWeb.setInflater(mInflater);
		OrderWeb.onCreate(URL);
		setViewParams("INFO", infoParentLayout, "showOrderWeb", infoViewList);
	}

	public void showViewActivityPage() {
		PaymentAcivityFragment InfoView = PaymentAcivityFragment.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.oncreat();
		setViewParams("INFO", infoParentLayout, "showViewActivityPage",
				infoViewList);
	}

	public void showSocialWebView() {
		socialFeedWebView web = socialFeedWebView.getInstance();
		RelativeLayout infoParentLayout = web.setInflater(mInflater);
		web.onCreate();
		setViewParams("INFO", infoParentLayout, "showSocialWebView",
				infoViewList);
	}

	public void showReferFriendPage(String screen) {
		ReferFriend referFriendPageView = ReferFriend.getInstance();
		RelativeLayout referFriendPageParentLayout = referFriendPageView
				.setInflater(mInflater);
		referFriendPageView.onCreate(screen);
		setViewParams("INFO", referFriendPageParentLayout,
				"showReferFriendPage", infoViewList);
	}

	public void showSocialFeedPage() {
		SocialFeed socialFeedView = SocialFeed.getInstance();
		RelativeLayout socialFeedParentLayout = socialFeedView
				.setInflater(mInflater);
		socialFeedView.onCreate();
		setViewParams("INFO", socialFeedParentLayout, "showSocialFeedPage",
				infoViewList);
	}

	public void showGetSocialPage() {
		GetSocial getSocialPageView = GetSocial.getInstance();
		RelativeLayout getSocialPageParentLayout = getSocialPageView
				.setInflater(mInflater);
		getSocialPageView.onCreate();
		setViewParams("INFO", getSocialPageParentLayout, "showGetSocialPage",
				infoViewList);
	}

public  void onContactUsCreate() {
	Tabbars.getInstance().doNotFinishAllActivities = true;
	
		
		final Intent emailIntent = new Intent(
				android.content.Intent.ACTION_SEND);
		emailIntent.setType("message/rfc822");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { AppConstants.EMAILCONTACT_US });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				AppConstants.EMAILSUBJECTFAQ);
		emailIntent
				.putExtra(android.content.Intent.EXTRA_TEXT, getDeviceName());
		startActivity(Intent.createChooser(emailIntent, "Email"));
	}

	public void showSettingsPage() {
		SettingInfo InfoView = SettingInfo.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate();
		setViewParams("INFO", infoParentLayout, "showSettingsPage",
				infoViewList);
	}

	public void showChangePasswordPage() {
		ChangePasswordPage InfoView = ChangePasswordPage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate();
		setViewParams("INFO", infoParentLayout, "showChangePasswordPage",
				infoViewList);
	}

	public void showLoginOptionPage(boolean b, String tabName) {
		LoginOption InfoView = LoginOption.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(false, tabName);
		setViewParams("INFO", infoParentLayout, "showLoginOptionPage",
				infoViewList);
	}

	public void showFbLoginPage(boolean b) {
		FbLogin InfoView = FbLogin.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "INFO");
		setViewParams("INFO", infoParentLayout, "showFbLoginPage", infoViewList);
	}

	public void showSignUpPage(boolean b) {
		SignUp InfoView = SignUp.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "INFO");
		setViewParams("INFO", infoParentLayout, "showSignUpPage", infoViewList);
	}

	public void showLoginPage(boolean b) {
		LoginPage InfoView = LoginPage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "INFO");
		setViewParams("INFO", infoParentLayout, "showLoginPage", infoViewList);
	}

	public void showForgetPasswordPage(boolean b) {
		ForgetPassword InfoView = ForgetPassword.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "INFO");
		setViewParams("INFO", infoParentLayout, "showForgetPasswordPage",
				infoViewList);
	}

	public void showTermsOfUsagePage(boolean b, String URL, String tabName,
			String title) {
		TermsOfUsage InfoView = TermsOfUsage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, URL, tabName, title);
		setViewParams("INFO", infoParentLayout, "showTermsOfUsagePage",
				infoViewList);
	}

	public void showAboutRoti() {
		AboutRoti InfoView = AboutRoti.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate();
		setViewParams("INFO", infoParentLayout, "showAboutRoti", infoViewList);
	}

	public void showFaq() {
		FAQPage InfoView = FAQPage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate();
		setViewParams("INFO", infoParentLayout, "showFaq", infoViewList);
	}

	public void showFAQPage() {
		FAQPage InfoView = FAQPage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate();
		setViewParams("INFO", infoParentLayout, "showFAQPage", infoViewList);
	}

	public void showPromoPage(String screen) {
		PromoPage InfoView = PromoPage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate("info");
		setViewParams("INFO", infoParentLayout, "showPromoPage", infoViewList);
	}

	// public void showSnapStart() {
	// SnapStart InfoView = SnapStart.getInstance();
	// RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
	// InfoView.onCreate();
	// setViewParams("INFO", infoParentLayout, "showSnapStart", infoViewList);
	// }

	public void showMyAccount() {
		SettingInfo InfoView = SettingInfo.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate();
		setViewParams("INFO", infoParentLayout, "showSettingsPage",
				infoViewList);
	}
	
	public void showHowTo() {
		howToGet InfoView = howToGet.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate();
		setViewParams("INFO", infoParentLayout, "showHowTo", infoViewList);
	}

	public void showLocationPage() {
		LocationPage InfoView = LocationPage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate();
		setViewParams("INFO", infoParentLayout, "showLocationPage",
				infoViewList);
	}

	// added for show webview
	public void showWebView(String url) {
		globalVar.getInstance().setMyVar(1);
		webView web = webView.getInstance();
		RelativeLayout infoParentLayout = web.setInflater(mInflater);
		web.onCreate(url);
		setViewParams("INFO", infoParentLayout, "showWebPage", infoViewList);
	}

	private void setViewParams(String tabName, View view, String tagName,
			List<View> viewList) {
		mParentLayout.removeAllViews();
		view.setTag(tagName);
		checkIfViewExist(view, infoViewList);
		mParentLayout.addView(view);
	}

	@SuppressWarnings("unused")
	private void checkIfViewExist(View view, List<View> listView) {
		String currentViewName = (String) view.getTag();
		boolean isPresent = false;
		if (listView != null && listView.size() > 0) {
			for (int i = 0; i < listView.size(); i++) {
				String compareViewName = (String) listView.get(i).getTag();
				if (compareViewName.equalsIgnoreCase(currentViewName))
					isPresent = true;
				break;
			}
		}
		if (!isPresent)
			listView.add(view);
	}

	private String nextViewName = "";

	public String getNextViewName() {
		return nextViewName;
	}

	public void setNextViewName(String nextViewName) {
		this.nextViewName = nextViewName;
	}

	public void setNextView() {
		try {
			if (nextViewName.equals("PromoPage")) {
				showPromoPage("info");
			}
			if (nextViewName.equals("showPromoPage")) {
				showPromoPage("info");
			}
			if (nextViewName.equals("fetchReferralRequest")) {
				Tabbars.getInstance().fetchReferralRequest();
			}
			if (nextViewName.equals("ChangePasword")) {
				showChangePasswordPage();
			}

			if (nextViewName.equals("showReferFriendPage")) {
				showReferFriendPage("info");
			}
		} catch (Exception e) {
		}
		nextViewName = "";
	}

	public void exitAppFunc() {
		RotiHomeActivity.getInstance().oPenTabView(
				0);
	}

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (webView.getInstance().isWebView == true) {
			webView.getInstance().backBrowser();
		} else {
			if (infoViewList != null && infoViewList.size() > 1) {
				handleBackButton();
			} else {
				exitAppFunc();
			}
		}
	
	}
	

	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (webView.getInstance().isWebView == true) {
//				webView.getInstance().backBrowser();
//			} else {
//				if (infoViewList != null && infoViewList.size() > 1) {
//					handleBackButton();
//				} else {
//					exitAppFunc();
//				}
//				// handleBackButton();
//				return true;
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	public void showLoginPage(boolean b, boolean isFromLovePage) {
		LoginPage InfoView = LoginPage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "REWARDS", true);
		setViewParams("INFO", infoParentLayout, "showLoginPage", infoViewList);
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// handleBackButton();
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	public void popPreviousView() {
		if (infoViewList != null && infoViewList.size() > 1) {
			infoViewList.remove(infoViewList.size() - 2);
		}
	}

	public boolean handleBackButton() {
		try {
			InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(
					mParentLayout.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
		}
		boolean isLast = false;
		isLast = setBackButtonHandledView(infoViewList, 0);
		return isLast;
	}

	public void showLoginOptionPage(boolean b, String tabName,
			boolean isFromLovePage) {
		LoginOption InfoView = LoginOption.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(false, tabName, true);
		setViewParams("INFO", infoParentLayout, "showLoginOptionPage",
				infoViewList);
	}
	public String getDeviceName() {
		String texts = "\n\n";

		try {
			PackageInfo pInfo;
			pInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
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
			String email = Tabbars.getInstance().getPreference().getString(
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
	private boolean setBackButtonHandledView(List<View> listView, int status) {
		boolean isLast = false;
		if (listView != null && listView.size() > 1) {
			mParentLayout.removeAllViews();
			mParentLayout.addView(listView.get(listView.size() - 2));
			doRefresh((String) listView.get(listView.size() - 2).getTag());
			listView.remove(listView.size() - 1);
			isLast = true;
		} else if (listView != null && listView.size() == 1) {
			mParentLayout.removeAllViews();
			mParentLayout.addView(listView.get(0));
			doRefresh((String) listView.get(0).getTag());
		} else
			isLast = false;
		return isLast;
	}

	private void doRefresh(String viewName) {
		if (viewName.equalsIgnoreCase("showSettingsPage")) {
			RefreshListner listner = SettingInfo.getInstance();
			if (listner != null)
				listner.notifyRefresh("showSettingsPage");
		}
		// else if (viewName.equalsIgnoreCase("showLocationPage")) {
		// RefreshListner listner = LocationPage.getInstance();
		// if (listner != null)
		// listner.notifyRefresh("showLocationPage");
		// }
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Info Page").build());
	}

}