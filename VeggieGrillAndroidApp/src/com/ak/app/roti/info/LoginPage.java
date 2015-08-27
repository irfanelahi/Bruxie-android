package com.ak.app.roti.info;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings.Secure;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.R;
import com.ak.app.cb.activity.Rewards;
import com.ak.app.cb.activity.RotiHomeActivity;
import com.ak.app.cb.activity.Snap;
import com.ak.app.cb.activity.Tabbars;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class LoginPage {
	private static LoginPage screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	// private Tabbars mHomePage;
	private Activity mHomePage;
	private String androidOS = Build.VERSION.RELEASE;
	private String model = Build.MODEL;
	private String manufacturer = Build.MANUFACTURER;
	public String pageDestination = "";

	// private ProgressBar progressBar;

	public static LoginPage getInstance() {
		if (screen == null)
			screen = new LoginPage();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(R.layout.info_login,
				null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	boolean isInfo = false;
	boolean isLovePrevPage = false;
	String mTabName;
	EditText emailEdit;
	EditText pwdEdit;
	ImageView loginBtn;

	public void onCreate(boolean b, final String tabName) {
		isLovePrevPage = false;
		isInfo = b;
		mTabName = tabName;
		mHomePage = Tabbars.getInstance();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Email Login").build());

		if (mHomePage == null)
			mHomePage = RotiHomeActivity.getInstance();
		emailEdit = (EditText) mParentLayout
				.findViewById(R.id.login_edit_email);
		pwdEdit = (EditText) mParentLayout
				.findViewById(R.id.login_edit_password);
		TextView loginText = (TextView) mParentLayout
				.findViewById(R.id.loginText);
		TextView forgotPassword = (TextView) mParentLayout
				.findViewById(R.id.forgotPassword);
//		ImageView signupFB = (ImageView) mParentLayout
//				.findViewById(R.id.login_image_facebook);
		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());
		
		AppConstants.fontDinLightTextView(loginText, 26,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
		
		AppConstants.gothamNarrowBookTextView(forgotPassword, 14,
				AppConstants.COLOR_GRAY_TEXT, mHomePage.getAssets());
		
		AppConstants.gothamNarrowBookTextView(emailEdit, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());
		AppConstants.gothamNarrowBookTextView(pwdEdit, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());
		
		// progressBar = (ProgressBar) mParentLayout
		// .findViewById(R.id.greenProgressBar);

//		emailEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus)
//					emailEdit.setBackgroundResource(R.drawable.formfield);
//				else if (emailEdit.getText().toString().equals(""))
//					emailEdit.setBackgroundResource(R.drawable.promo_btn_gray);
//			}
//		});
//
//		pwdEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus)
//					pwdEdit.setBackgroundResource(R.drawable.formfield);
//				else if (pwdEdit.getText().toString().equals(""))
//					pwdEdit.setBackgroundResource(R.drawable.promo_btn_gray);
//			}
//		});

		loginBtn = (ImageView) mParentLayout
				.findViewById(R.id.login_image_login);
		SetTextWatcherForAmountEditView(emailEdit);
		SetTextWatcherForAmountEditView(pwdEdit);
		// loginBtn.setBackgroundResource(R.drawable.log_in_btn_idle);
		loginBtn.setEnabled(false);

		// Login Feature implementation
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					InputMethodManager inputManager = (InputMethodManager) mHomePage
							.getSystemService(Activity.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(
							emailEdit.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				} catch (Exception e) {
				}
				String email = emailEdit.getText().toString();
				String mPassword = pwdEdit.getText().toString();
				String android_id = Secure.getString(
						mHomePage.getContentResolver(), Secure.ANDROID_ID);
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					String[] param = new String[] { email, mPassword,
							android_id };
					// Login Async Task
					new submitLoginDetailsToServer().execute(param);
				} else {
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
				}
			}
		});

//		signupFB.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String mrefer = "0";
//				String android_id = Secure.getString(
//						mHomePage.getContentResolver(), Secure.ANDROID_ID);
//				SharedPreferences preferences = PreferenceManager
//						.getDefaultSharedPreferences(mHomePage);
//				SharedPreferences.Editor editor = preferences.edit();
//
//				editor.putString("android_id", android_id);
//				editor.commit();
//
//				if (AppConstants.isNetworkAvailable(mHomePage)) {
//					// mHomePage.postMessage(false, mrefer);
//					if (Tabbars.getInstance() != null) {
//						Tabbars.getInstance().doNotFinishAllActivities = true;
//						Tabbars tabbar = Tabbars.getInstance();
//						tabbar.pageDestinationAfterFbLogin = pageDestination;
//						tabbar.postMessage(false, mrefer);
//					}
//				} else {
//					AppConstants.showMsgDialog("Alert",
//							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
//				}
//
//			}
//		});

		// Forget password implementation
		forgotPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					InputMethodManager inputManager = (InputMethodManager) mHomePage
							.getSystemService(Activity.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(
							emailEdit.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				} catch (Exception e) {
				}
				// Info.getInstance().showForgetPasswordPage(isInfo);
				if (tabName.equals("INFO") && Info.getInstance() != null) {
					Info.getInstance().showForgetPasswordPage(isInfo);
				} else if (tabName.equals("SNAP") && Snap.getInstance() != null) {
					Snap.getInstance().showForgetPasswordPage(isInfo);
				} else if (tabName.equals("ROTIHOMEACTIVITY")
						&& RotiHomeActivity.getInstance() != null) {
					RotiHomeActivity.getInstance().showForgetPasswordPage(
							isInfo);
				} else if (tabName.equals("REWARDS")
						&& Rewards.getInstance() != null) {
					Rewards.getInstance().showForgetPasswordPage(isInfo);
				}
			}
		});

	}

	public void onCreate(boolean b, final String tabName, boolean isFromLovePage) {
		isLovePrevPage = true;
		isInfo = b;
		mTabName = tabName;
		mHomePage = Tabbars.getInstance();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Email Login").build());

		if (mHomePage == null)
			mHomePage = RotiHomeActivity.getInstance();
		emailEdit = (EditText) mParentLayout
				.findViewById(R.id.login_edit_email);
		pwdEdit = (EditText) mParentLayout
				.findViewById(R.id.login_edit_password);
		TextView loginText = (TextView) mParentLayout
				.findViewById(R.id.loginText);
		TextView forgotPassword = (TextView) mParentLayout
				.findViewById(R.id.forgotPassword);
		// progressBar = (ProgressBar) mParentLayout
		// .findViewById(R.id.greenProgressBar);
//		ImageView signupFB = (ImageView) mParentLayout
//				.findViewById(R.id.login_image_facebook);
		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());

//		emailEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus)
//					emailEdit.setBackgroundResource(R.drawable.formfield);
//				else if (emailEdit.getText().toString().equals(""))
//					emailEdit.setBackgroundResource(R.drawable.promo_btn_gray);
//			}
//		});
//
//		pwdEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus)
//					pwdEdit.setBackgroundResource(R.drawable.formfield);
//				else if (pwdEdit.getText().toString().equals(""))
//					pwdEdit.setBackgroundResource(R.drawable.promo_btn_gray);
//			}
//		});
		AppConstants.americanTypewriterTextView(forgotPassword, 14,
				AppConstants.COLORDARKGRAYRGB, mHomePage.getAssets());
		
		AppConstants.fontDinLightTextView(loginText, 26,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
		
		AppConstants.gothamNarrowBookTextView(emailEdit, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());
		AppConstants.gothamNarrowBookTextView(pwdEdit, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());
		
		loginBtn = (ImageView) mParentLayout
				.findViewById(R.id.login_image_login);
		SetTextWatcherForAmountEditView(emailEdit);
		SetTextWatcherForAmountEditView(pwdEdit);
		// loginBtn.setBackgroundResource(R.drawable.log_in_btn_idle);
		
		AppConstants.setViewEndable(loginBtn, false);
		// Login Feature implementation
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					InputMethodManager inputManager = (InputMethodManager) mHomePage
							.getSystemService(Activity.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(
							emailEdit.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				} catch (Exception e) {
				}
				String email = emailEdit.getText().toString();
				String mPassword = pwdEdit.getText().toString();
				String android_id = Secure.getString(
						mHomePage.getContentResolver(), Secure.ANDROID_ID);
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					String[] param = new String[] { email, mPassword,
							android_id };
					// Login Async Task
					new submitLoginDetailsToServer().execute(param);
				} else {
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
				}
			}
		});

//		signupFB.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// Info.getInstance().showFbLoginPage(isInfo);
//				if (tabName.equals("INFO") && Info.getInstance() != null) {
//					Info.getInstance().showFbLoginPage(isInfo);
//				} else if (tabName.equals("SNAP") && Snap.getInstance() != null) {
//					Snap.getInstance().showFbLoginPage(isInfo, pageDestination);
//				} else if (tabName.equals("ROTIHOMEACTIVITY")
//						&& RotiHomeActivity.getInstance() != null) {
//					RotiHomeActivity.getInstance().showFbLoginPage(isInfo,
//							pageDestination);
//				} else if (tabName.equals("REWARDS")
//						&& Rewards.getInstance() != null) {
//					Rewards.getInstance().showFbLoginPage(isInfo);
//				}
//			}
//		});

		// Forget password implementation
		forgotPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					InputMethodManager inputManager = (InputMethodManager) mHomePage
							.getSystemService(Activity.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(
							emailEdit.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				} catch (Exception e) {
				}
				// Info.getInstance().showForgetPasswordPage(isInfo);
				if (tabName.equals("INFO") && Info.getInstance() != null) {
					Info.getInstance().showForgetPasswordPage(isInfo);
				} else if (tabName.equals("SNAP") && Snap.getInstance() != null) {
					Snap.getInstance().showForgetPasswordPage(isInfo);
				} else if (tabName.equals("ROTIHOMEACTIVITY")
						&& RotiHomeActivity.getInstance() != null) {
					RotiHomeActivity.getInstance().showForgetPasswordPage(
							isInfo);
				} else if (tabName.equals("REWARDS")
						&& Rewards.getInstance() != null) {
					Rewards.getInstance().showForgetPasswordPage(isInfo);
				}
			}
		});

	}

	private void SetTextWatcherForAmountEditView(final EditText amountEditText) {
		TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (filterLongEnough(emailEdit) && filterLongEnough(pwdEdit)) {
					try {
						AppConstants.setViewEndable(loginBtn, true);
									
					
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					AppConstants.setViewEndable(loginBtn, false);
				}
			}

			private boolean filterLongEnough(EditText amountEditText) {
				return amountEditText.getText().toString().trim().length() > 0;
			}
		};
		amountEditText.addTextChangedListener(fieldValidatorTextWatcher);
	}

	String mEmailID = "";

	private class submitLoginDetailsToServer extends
			AsyncTask<String, Void, String> {

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
			String regId = "";// mHomePage.getPreference().getString(
			// AppConstants.PREFPUSHREGISTRATIONID, "");
			if (Tabbars.getInstance() != null)
				regId = Tabbars.getInstance().getPreference()
						.getString(AppConstants.PREFPUSHREGISTRATIONID, "");
			else if (RotiHomeActivity.getInstance() != null)
				regId = RotiHomeActivity.getInstance().getPreference()
						.getString(AppConstants.PREFPUSHREGISTRATIONID, "");
			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
			nameparams.add(new BasicNameValuePair("email", params[0]));
			mEmailID = params[0];
			nameparams.add(new BasicNameValuePair("password", params[1]));
			nameparams.add(new BasicNameValuePair("android_id", params[2]));
			nameparams.add(new BasicNameValuePair("sign_in_device_type",
					AppConstants.DEVICE_TYPE));
			nameparams.add(new BasicNameValuePair("register_type",
					AppConstants.REGISTERTYPE));
			nameparams
					.add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
			nameparams.add(new BasicNameValuePair("device_token", regId));
			nameparams.add(new BasicNameValuePair("device_id", AppConstants
					.getDeviceID(mHomePage)));
			nameparams.add(new BasicNameValuePair("phone_model", manufacturer
					+ " " + model));
			nameparams.add(new BasicNameValuePair("os", androidOS));
			String result = WebHTTPMethodClass.executeHttPost("/user/login",
					nameparams);// loginService();
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

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
			// progressBar.setVisibility(View.GONE);

			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					if (sucess != null && !sucess.equals("")
							&& sucess.equals("true")) {
						Editor prefsEditor = null;
						if (Tabbars.getInstance() != null)
							prefsEditor = Tabbars.getInstance()
									.getPreferenceEditor();
						else if (RotiHomeActivity.getInstance() != null)
							prefsEditor = RotiHomeActivity.getInstance()
									.getPreferenceEditor();
						// Editor prefsEditor = mHomePage.getPreferenceEditor();
						prefsEditor.putString(AppConstants.PREFLOGINID,
								mEmailID);
						prefsEditor.putBoolean(
								AppConstants.PREFLOGOUTBUTTONTAG, false);

						String auth_token = "";
						try {
							if (resObject.has("auth_token")) {
								auth_token = resObject.getString("auth_token");
								prefsEditor
										.putString(AppConstants.PREFAUTH_TOKEN,
												auth_token);
								Log.d("auth_token", auth_token);
							}
						} catch (Exception e) {
						}
						prefsEditor.commit();
						// mHomePage.popPreviousView();
						// mHomePage.handleBackButton();
						// mHomePage.setNextView();
						if (mTabName.equals("INFO")
								&& Info.getInstance() != null) {
							// Info.getInstance().showLoginOptionPage(isInfo,
							// mTabName);
							Info.getInstance().popPreviousView();
							Info.getInstance().handleBackButton();
							if (!Info.getInstance().getNextViewName()
									.equals("fetchReferralRequest"))
								Info.getInstance().handleBackButton();
							Info.getInstance().setNextView();
						} else if (mTabName.equals("SNAP")
								&& Snap.getInstance() != null) {
							Snap.getInstance().popPreviousView();
							Snap.getInstance().handleBackButton();
							if (pageDestination.equals("scanReceiptBarcode"))
								Snap.getInstance().showScanBarcodePage();
							else
								Snap.getInstance().setNextView();
						} else if (mTabName.equals("ROTIHOMEACTIVITY")
								&& RotiHomeActivity.getInstance() != null) {
							if (pageDestination.equals("referFriend")) {
								RotiHomeActivity.getInstance()
										.popPreviousView();
								RotiHomeActivity.getInstance()
										.handleBackButton();
								RotiHomeActivity.getInstance()
										.showReferFriendPage("info");
							} else if (pageDestination.equals("getSocial")) {
								RotiHomeActivity.getInstance()
										.popPreviousView();
								RotiHomeActivity.getInstance()
										.handleBackButton();
								RotiHomeActivity.getInstance()
										.showGetSocialPage();
							} else {
								RotiHomeActivity.getInstance()
										.popPreviousView();
								RotiHomeActivity.getInstance()
										.handleBackButton();
								RotiHomeActivity.getInstance().setNextView();
							}
						} else if (mTabName.equals("REWARDS")
								&& Rewards.getInstance() != null
								&& !isLovePrevPage) {
							Rewards.getInstance().popPreviousView();
							Rewards.getInstance().handleBackButton();
							Rewards.getInstance().setNextView();
						} else if (mTabName.equals("REWARDS") && isLovePrevPage
								&& Info.getInstance() != null
								&& RotiHomeActivity.getInstance() != null) {
							Info.getInstance().popPreviousView();
							Info.getInstance().handleBackButton();
							RotiHomeActivity.getInstance().oPenTabView(1);

						}
						// if (isInfo && Info.getInstance() != null)
						// Info.getInstance().showInfoMainPage();
					} else
						AppConstants.parseInput(result, mHomePage);
				} catch (Exception e) {
					AppConstants.parseInput(result, mHomePage);
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORFAILEDAPI, mHomePage);
					e.printStackTrace();
				}
				// progressBar.setVisibility(ProgressBar.GONE);
			} else {
				AppConstants.parseInput(result, mHomePage);
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mHomePage);
			}
		}
	}
}
