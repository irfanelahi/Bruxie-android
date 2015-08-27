package com.ak.app.cb.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ak.app.cb.activity.R;
import com.ak.app.roti.info.FbLogin;
import com.ak.app.roti.info.ForgetPassword;
import com.ak.app.roti.info.LoginOption;
import com.ak.app.roti.info.LoginPage;
import com.ak.app.roti.info.SignUp;
import com.ak.app.roti.info.Skipsurvey;
import com.ak.app.roti.info.Submitsurvey;
import com.ak.app.roti.info.TermsOfUsage;
import com.ak.app.roti.snap.ReceiptComplete;
import com.ak.app.roti.snap.ReceiptSurvey;
import com.ak.app.roti.snap.Servey;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.GetLatLongFromGPS;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
import com.google.android.gcm.GCMRegistrar;

public class RotiHomeActivity extends Activity {

	private static RotiHomeActivity mHomePage;
	SharedPreferences mPreference;
	Editor prefsEditor;
	private LayoutInflater mInflater;
	private LinearLayout mParentLayout;
	private List<View> homeViewList = new ArrayList<View>();
	private ProgressDialog progressDialog;
	private GetLatLongFromGPS getLatLongObj;
	// private Facebook mFacebook;
	// private AsyncFacebookRunner mAsyncRunner;
	// private SessionListener mSessionListener = new SessionListener();
	private String messageContent;
	private String mFromClassName;
	public boolean isInBackGround;

	public static RotiHomeActivity getInstance() {
		return mHomePage;
	}

	public Editor getPreferenceEditor() {
		return prefsEditor;
	}

	public SharedPreferences getPreference() {
		return mPreference;
	}

	public GetLatLongFromGPS getGetLatLongObj() {
		return getLatLongObj;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		mHomePage = this;
		mPreference = PreferenceManager.getDefaultSharedPreferences(mHomePage);
		prefsEditor = mPreference.edit();
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading...");
		// getLatLongObj = GetLatLongFromGPS.getinstance(this);
		// if (getLatLongObj != null)
		// getLatLongObj.startGPS();

		// mFacebook = new Facebook(AppConstants.FACEBOOK_APPID);
		// mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		//
		// SessionStore.restore(mFacebook, this);
		// SessionEvents.addAuthListener(mSessionListener);
		// SessionEvents.addLogoutListener(mSessionListener);

		// boolean isHomeOpen = mPreference.getBoolean(
		// AppConstants.PREF_HOME_ISOPENHOMEPAGE, true);
		// boolean isfromTab = false;
		Bundle bundle = getIntent().getExtras();
		// if (bundle != null) {
		// isfromTab = bundle.getBoolean(
		// AppConstants.INTENT_EXTRA_HOME_ISFROMTAB, false);
		// }
		// if (!isHomeOpen && !isfromTab) {
		// Intent newIntent = new Intent(RotiHomeActivity.this, Tabbars.class);
		// newIntent.putExtra(AppConstants.INTENT_EXTRA_HOME_TABNUMBER, 2);
		// startActivity(newIntent);
		// finish();
		// return;
		// }
		mInflater = LayoutInflater.from(this);
		mParentLayout = (LinearLayout) findViewById(R.id.home_linear_container);
		// showHomePage(isfromTab);
	showHomePage(true);

		//oPenTabView(2);
		
		//Snap.getInstance().showHomePage(true);
		
		String regId = mPreference.getString(
				AppConstants.PREFPUSHREGISTRATIONID, "");
		if (regId.equals("")) {
			registerPushAccount();
		}
		if (bundle != null) {
			messageContent = bundle
					.getString(AppConstants.PUSH_NOTIFICATION_MESSAGE);
			mFromClassName = bundle
					.getString(AppConstants.PUSH_NOTIFICATION_CLASS);
			if (mFromClassName != null
					&& mFromClassName.equalsIgnoreCase("C2DMRECEIVER")
					&& messageContent != null
					&& !messageContent.equalsIgnoreCase("")) {
				Log.e("messageContent:6", messageContent);
				setMessage(messageContent/* , bedgeNumber */);
				prefsEditor.putString(AppConstants.PUSH_NOTIFICATION_MESSAGE,
						messageContent);
				prefsEditor.putString(AppConstants.PUSH_NOTIFICATION_CLASS,
						mFromClassName);
				prefsEditor.commit();
			}
		}
	}

	// @Override
	// public boolean dispatchKeyEvent(KeyEvent event) {
	// if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	// if (event.getAction() == KeyEvent.ACTION_DOWN
	// && event.getRepeatCount() == 0) {
	// // Tell the framework to start tracking this event.
	// return true;
	// }
	// }
	// return super.dispatchKeyEvent(event);
	// }

	public void registerPushAccount() {
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		if (GCMRegistrar.isRegistered(this)) {
			Log.d("info", GCMRegistrar.getRegistrationId(this));
		}
		String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			GCMRegistrar.register(this, AppConstants.PUSH_NOTIFICATION_KEY);
			Log.d("info", GCMRegistrar.getRegistrationId(this));
			regId = GCMRegistrar.getRegistrationId(this);
		} else {
			Log.d("info", "already registered as" + regId);
		}
		prefsEditor.putString(AppConstants.PREFPUSHREGISTRATIONID, regId);
		Log.d("info", regId);
		prefsEditor.commit();
	}

	public void setMessage(String message/* , String bedgeNumber */) {
		try {
			showPushDialog(AppConstants.PUSH_NOTIFICATION_TAG, message);
			clearNotificationStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showPushDialog(String title, final String message) {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage(message).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						messageContent = "";
					}
				});
		AlertDialog alert = alt_bld.create();
		alert.setTitle(title);
		alert.setIcon(AlertDialog.BUTTON_POSITIVE);
		alert.show();
	}

	public void clearNotificationStatus() {
		try {
			NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			nm.cancel(AppConstants.PUSH_NOTIFICATION_TAG,
					AppConstants.PUSH_NOTIFICATION_ID);
			nm.cancelAll();
			prefsEditor.putString(AppConstants.PUSH_NOTIFICATION_MESSAGE, "");
			prefsEditor.putString(AppConstants.PUSH_NOTIFICATION_CLASS, "");
			prefsEditor.commit();
		} catch (Exception e) {
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		com.facebook.AppEventsLogger.activateApp(getApplicationContext(),
				AppConstants.FACEBOOK_APPID);
		Log.d("=============================", "test");
		// Tabbars.getInstance().hideTabs();
		try {
			InputMethodManager inputManager = (InputMethodManager) mHomePage
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(
					mParentLayout.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
		}
		// isInBackGround = false;
		// getLatLongObj = GetLatLongFromGPS.getinstance(this); // PP
		// if (getLatLongObj != null) { // PP
		// if (getLatLongObj.mbUpdatesStopped == true) {
		// getLatLongObj.startGPS();
		// }
		// }
		// try {
		// Thread.sleep(2000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	protected void onPause() {
		super.onPause();
		// isInBackGround = true;
		if (progressDialog != null)
			progressDialog.dismiss();
		// if (mCamera != null) {
		// mCamera.release();
		// }
		// if (getLatLongObj != null) { // PP
		// if (getLatLongObj.mbUpdatesStopped == true) {
		// getLatLongObj.stopLocationListening();
		// }
		// }
	}

	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}

	public boolean checkIfLogin() {
		String authToken = mPreference.getString(AppConstants.PREFAUTH_TOKEN,
				"");
		if (authToken.equals(""))
			return false;
		else
			return true;
	}

	public void oPenTabView(int tabnum) {
		if (Tabbars.getInstance() == null) {
			//Toast.makeText(mHomePage, "main2", Toast.LENGTH_SHORT).show();
			
			Intent newIntent = new Intent(RotiHomeActivity.this, Tabbars.class);
			newIntent
					.putExtra(AppConstants.INTENT_EXTRA_HOME_TABNUMBER, tabnum);
			startActivity(newIntent);
		finish();
		} else {
			//Toast.makeText(mHomePage, "main", Toast.LENGTH_SHORT).show();
			Tabbars.getInstance().SetcurrentTabs(tabnum);
		}
		// Intent newIntent = new Intent(RotiHomeActivity.this, Tabbars.class);
		// newIntent.putExtra(AppConstants.INTENT_EXTRA_HOME_TABNUMBER, tabnum);
		// startActivity(newIntent);
		// finish();
		prefsEditor.putBoolean(AppConstants.PREF_HOME_ISOPENHOMEPAGE, false);
		prefsEditor.commit();
	}

	public void openOrderPage() {
		Intent newIntent = new Intent(RotiHomeActivity.this, OrderPage.class);
		startActivity(newIntent);
	}

	public void showHomePage(boolean isfromTab) {
		HomeTabView socializePageView = HomeTabView.getInstance();
		RelativeLayout socializePageParentLayout = socializePageView
				.setInflater(mInflater);
		socializePageView.onCreate(isfromTab);
		setViewParams(socializePageParentLayout, "showSnapStartPagew",
				homeViewList);
	}

	public void showEarnMoreKarmmaPage() {
		EarnMoreKarmma earnMoreKarmmaPageView = EarnMoreKarmma.getInstance();
		RelativeLayout earnMoreKarmmaPageParentLayout = earnMoreKarmmaPageView
				.setInflater(mInflater);
		earnMoreKarmmaPageView.onCreate();
		setViewParams(earnMoreKarmmaPageParentLayout, "showEarnMoreKarmmaPage",
				homeViewList);
	}

	public void showReferFriendPage(String screen) {
		ReferFriend referFriendPageView = ReferFriend.getInstance();
		RelativeLayout referFriendPageParentLayout = referFriendPageView
				.setInflater(mInflater);
		referFriendPageView.onCreate(screen);
		setViewParams(referFriendPageParentLayout, "showReferFriendPage",
				homeViewList);
	}

	public void showGetSocialPage() {
		GetSocial getSocialPageView = GetSocial.getInstance();
		RelativeLayout getSocialPageParentLayout = getSocialPageView
				.setInflater(mInflater);
		getSocialPageView.onCreate();
		setViewParams(getSocialPageParentLayout, "showGetSocialPage",
				homeViewList);
	}

	private void setViewParams(View view, String tagName, List<View> viewList) {
		mParentLayout.removeAllViews();
		view.setTag(tagName);
		checkIfViewExist(view, homeViewList);
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

	public void showLoginOptionPage(boolean b, String tabName) {
		// mTabName = "INFO";
		LoginOption loginOptionView = LoginOption.getInstance();
		RelativeLayout loginOptionParentLayout = loginOptionView
				.setInflater(mInflater);
		loginOptionView.onCreate(b, tabName);
		setViewParams(loginOptionParentLayout, "showLoginOptionPage",
				homeViewList);
	}

	public void showLoginOptionPage(boolean b, String tabName,
			String pageDestination) {
		// mTabName = "INFO";
		LoginOption loginOptionView = LoginOption.getInstance();
		RelativeLayout loginOptionParentLayout = loginOptionView
				.setInflater(mInflater);
		loginOptionView.pageDestination = pageDestination;
		loginOptionView.onCreate(b, tabName);
		setViewParams(loginOptionParentLayout, "showLoginOptionPage",
				homeViewList);
	}

	public void showFbLoginPage(boolean b) {
		FbLogin InfoView = FbLogin.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "ROTIHOMEACTIVITY");
		setViewParams(infoParentLayout, "showInfoPage", homeViewList);
	}

	public void showFbLoginPage(boolean b, String pageDestination) {
		FbLogin InfoView = FbLogin.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.pageDestination = pageDestination;
		InfoView.onCreate(b, "ROTIHOMEACTIVITY");
		setViewParams(infoParentLayout, "showInfoPage", homeViewList);
	}

	public void showSignUpPage(boolean b) {
		SignUp InfoView = SignUp.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "ROTIHOMEACTIVITY");
		setViewParams(infoParentLayout, "showFbLoginPage", homeViewList);
	}

	public void showSignUpPage(boolean b, String pageDestination) {
		SignUp InfoView = SignUp.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.pageDestinationAfterSignup = pageDestination;
		InfoView.onCreate(b, "ROTIHOMEACTIVITY");
		setViewParams(infoParentLayout, "showFbLoginPage", homeViewList);
	}

	public void showLoginPage(boolean b) {
		LoginPage InfoView = LoginPage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "ROTIHOMEACTIVITY");
		setViewParams(infoParentLayout, "showLoginPage", homeViewList);
	}

	public void showLoginPage(boolean b, String pageDestination) {
		LoginPage InfoView = LoginPage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.pageDestination = pageDestination;
		InfoView.onCreate(b, "ROTIHOMEACTIVITY");
		setViewParams(infoParentLayout, "showLoginPage", homeViewList);
	}

	public void showForgetPasswordPage(boolean b) {
		ForgetPassword InfoView = ForgetPassword.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "ROTIHOMEACTIVITY");
		setViewParams(infoParentLayout, "showForgetPasswordPage", homeViewList);
	}

	public void showTermsOfUsagePage(boolean b, String URL, String tabName,
			String title) {
		TermsOfUsage InfoView = TermsOfUsage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, URL, tabName, title);
		setViewParams(infoParentLayout, "showInfoPage", homeViewList);
	}

	private String receiptId = "";
	private String surveyId = "";
	private String restaurantName = "";
	private String receiptDate = "";

	public void showReceiptServeyPage(String receiptIds, String surveyIds,
			String restaurantNames, String receiptDates) {
		receiptId = receiptIds;
		surveyId = surveyIds;
		receiptDate = receiptDates;
		restaurantName = restaurantNames;
		Log.i("elang", "elang roti home 1");
		Servey socializePageView = Servey.getInstance();
		Log.i("elang", "elang roti home 2");
		RelativeLayout socializePageParentLayout = socializePageView
				.setInflater(mInflater);
		Log.i("elang", "elang roti home 3");
		socializePageView.onCreate(receiptId, surveyId, restaurantName,
				receiptDate);
		Log.i("elang", "elang roti home 4");
		setViewParams(socializePageParentLayout, "showReceiptServeyPage",
				homeViewList);
	}
	
	public void showReceiptCompletePage() {
		ReceiptComplete socializePageView = ReceiptComplete.getInstance();
		RelativeLayout socializePageParentLayout = socializePageView
				.setInflater(mInflater);
		socializePageView.onCreate();
		setViewParams(socializePageParentLayout, "showReceiptCompletePage",
				homeViewList);
//		isCamera = false;
	}


	public void showReceiptServeyPage() {
		Submitsurvey socializePageView = Submitsurvey.getInstance();
		RelativeLayout socializePageParentLayout = socializePageView
				.setInflater(mInflater);
		socializePageView.onCreate();
		setViewParams(socializePageParentLayout, "showReceiptServeyPage",
				homeViewList);
		// isCamera = false;
	}
	public void showSKIPServeyPage() {
		Skipsurvey socializePageView = Skipsurvey.getInstance();
		RelativeLayout socializePageParentLayout = socializePageView
				.setInflater(mInflater);
		socializePageView.onCreate();
		setViewParams(socializePageParentLayout, "showskipServeyPage",
				homeViewList);
		// isCamera = false;
	}

	public boolean submitbtn() {
		boolean isLast = false;

		if (homeViewList != null
				&& homeViewList.size() > 1
				&& ((String) homeViewList.get(homeViewList.size() - 1).getTag())
						.equalsIgnoreCase("showReceiptServeyPage")) {
			if (homeViewList != null && homeViewList.size() > 1) {
				mParentLayout.removeAllViews();
				mParentLayout
						.addView(homeViewList.get(homeViewList.size() - 1));
				homeViewList.remove(homeViewList.size() - 1);
				isLast = true;
			}
			showReceiptServeyPage();
		} else {
			isLast = setBackButtonHandledView(homeViewList, 0);
		}
		return isLast;
	}


	public void skipbtn() {
		boolean isLast = false;

		if (homeViewList != null
				&& homeViewList.size() > 1
				&& ((String) homeViewList.get(homeViewList.size() - 1).getTag())
						.equalsIgnoreCase("showReceiptServeyPage")) {
			if (homeViewList != null && homeViewList.size() > 1) {
				mParentLayout.removeAllViews();
				mParentLayout
						.addView(homeViewList.get(homeViewList.size() - 1));
				homeViewList.remove(homeViewList.size() - 1);
				isLast = true;
			}
			showSKIPServeyPage();
		} else {
			isLast = setBackButtonHandledView(homeViewList, 0);
		}
		
	}

	
	
	public class skipSurvey extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (Tabbars.getInstance() != null
					&& Tabbars.getInstance().getProgressDialog() != null
					&& !Tabbars.getInstance().getProgressDialog().isShowing())
				Tabbars.getInstance().getProgressDialog().show();
			else if (RotiHomeActivity.getInstance() != null
					&& RotiHomeActivity.getInstance().getProgressDialog() != null
					&& !RotiHomeActivity.getInstance().getProgressDialog()
							.isShowing())
				RotiHomeActivity.getInstance().getProgressDialog().show();
			// progressBar.setVisibility(View.VISIBLE);

		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String authToken = Tabbars.getInstance().getPreference()
					.getString(AppConstants.PREFAUTH_TOKEN, "");
			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
			nameparams
					.add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
			nameparams.add(new BasicNameValuePair("auth_token", authToken));
			nameparams.add(new BasicNameValuePair("survey_id", surveyId));// id
			nameparams.add(new BasicNameValuePair("receipt_id", receiptId));// id

			result = WebHTTPMethodClass.executeHttPost("/survey/skip",
					nameparams);// loginService();
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (Tabbars.getInstance() != null
					&& Tabbars.getInstance().getProgressDialog() != null
					&& Tabbars.getInstance().getProgressDialog().isShowing())
				Tabbars.getInstance().getProgressDialog().dismiss();
			else if (RotiHomeActivity.getInstance() != null
					&& RotiHomeActivity.getInstance().getProgressDialog() != null
					&& RotiHomeActivity.getInstance().getProgressDialog()
							.isShowing())
				RotiHomeActivity.getInstance().getProgressDialog().dismiss();

			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);

					String sucess = resObject.getString("status");
					if (sucess != null && !sucess.equals("")
							&& sucess.equals("true")) {
					//	showSKIPServeyPage();
						skipbtn();
						//showReceiptCompletePage();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
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
			if (nextViewName.equals("viewrewardText")) {
				oPenTabView(1);
			} else if (nextViewName.equals("snapreceiptText")) {
				oPenTabView(2);
			} else if (nextViewName.equals("referafriendText")) {
				fetchReferralRequest();
			} else if (nextViewName.equals("socializeText")) {
				oPenTabView(2);
			}
		} catch (Exception e) {
		}
		nextViewName = "";
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// handleBackButton();
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	public void exitAppFunc() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (homeViewList != null && homeViewList.size() > 1) {
			handleBackButton();
		} else {
			exitAppFunc();
		}

	}
	
	public void popPreviousView() {
		if (homeViewList != null && homeViewList.size() > 1) {
			homeViewList.remove(homeViewList.size() - 2);
		}
	}

	public boolean handleBackButton() {
		boolean isLast = false;
		isLast = setBackButtonHandledView(homeViewList, 0);
		return isLast;
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
		// if (viewName.equalsIgnoreCase("showSelectLocationPage")) {
		// RefreshListner listner = SnapLocation.getInstance();
		// if (listner != null)
		// listner.notifyRefresh("showSelectLocationPage");
		// } else if (viewName.equalsIgnoreCase("showReceiptServeyPage")) {
		// RefreshListner listner = Servey.getInstance();
		// if (listner != null)
		// listner.notifyRefresh("showReceiptServeyPage");
		// }
	}

	public void fetchReferralRequest() {
		new fetchReferralRequestServer().execute("");
	}

	private class fetchReferralRequestServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (progressDialog != null)
				progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String authToken = getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			String result = WebHTTPMethodClass.httpGetService(
					"/referral/email", "auth_token=" + authToken + "&appkey="
							+ AppConstants.APPKEY);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		// /referral/email result =
		// {"status":true,"email_title":"join us","email_body":"sign up with this promocode  CGAPYXXL ","referral_code":"CGAPYXXL","start_date":"2012-11-22","end_date":"2013-01-31","referral_program_title":"vfxv","incentive_title":"+30"}

		@Override
		protected void onPostExecute(String result) {
			if (progressDialog != null)
				progressDialog.dismiss();
			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					if (sucess != null && !sucess.equals("")
							&& sucess.equals("true")) {
						String email_title = "";
						String email_body = "";
						// String incentive_title = "";
						try {
							if (resObject.has("email_title")) {
								email_title = resObject
										.getString("email_title");
								email_body = resObject.getString("email_body");
								// incentive_title =
								// resObject.getString("incentive_title");
							}
							final Intent emailIntent = new Intent(
									android.content.Intent.ACTION_SEND);
							emailIntent.setType("plain/text");
							emailIntent.putExtra(
									android.content.Intent.EXTRA_EMAIL,
									new String[] { "" });
							emailIntent.putExtra(
									android.content.Intent.EXTRA_SUBJECT,
									email_title);
							emailIntent.putExtra(
									android.content.Intent.EXTRA_TEXT,
									email_body);
							startActivity(Intent.createChooser(emailIntent,
									"Email"));
						} catch (Exception e) {
						}
						// Editor prefsEditor = mHomePage.getPreferenceEditor();
						// prefsEditor.putBoolean(
						// AppConstants.PREFREFERRAL_CODE, false);
						// prefsEditor.commit();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			AppConstants.parseInput(result, RotiHomeActivity.this);
		}
	}

}
