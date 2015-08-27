package com.ak.app.roti.info;

import org.json.JSONObject;

import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.RefreshListner;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class SettingInfo implements RefreshListner {
	private static SettingInfo screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;

	// private ProgressBar progressBar;

	public static SettingInfo getInstance() {
		if (screen == null)
			screen = new SettingInfo();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.info_setting, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	TextView logoutBtn;

	public void onCreate() {
		mHomePage = Tabbars.getInstance();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Settings Page").build());

		// TextView backBtn = (TextView) mParentLayout
		// .findViewById(R.id.settings_text_back);
		TextView ChangePasswordText = (TextView) mParentLayout
				.findViewById(R.id.settings_text_ChangePassword);
		logoutBtn = (TextView) mParentLayout
				.findViewById(R.id.settings_text_LOGOUT);
		RelativeLayout resetPasswordButton = (RelativeLayout) mParentLayout
				.findViewById(R.id.resetPasswordButton);
		RelativeLayout authButton = (RelativeLayout) mParentLayout
				.findViewById(R.id.authButton);
		// progressBar = (ProgressBar) mParentLayout
		// .findViewById(R.id.greenProgressBar);

		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.gothamNarrowMediumTextView(topTitle, 18, AppConstants.COLORWHITERGB,mHomePage.getAssets());

		AppConstants.gothamNarrowBookTextView(ChangePasswordText, 16, AppConstants.COLORRGRAY,mHomePage.getAssets());
		AppConstants.gothamNarrowBookTextView(logoutBtn, 16, AppConstants.COLORRGRAY,mHomePage.getAssets());

		RelativeLayout backarrow;
		backarrow=(RelativeLayout)mParentLayout.findViewById(R.id.backarrow);
		backarrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Info.getInstance().handleBackButton();
//				Info.getInstance().showInfoMainPage();
				Info.getInstance().onBackPressed();
			}
		});
		resetPasswordButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mHomePage != null && mHomePage.checkIfLogin()) {
					Info.getInstance().showChangePasswordPage();
				} else {
					Info.getInstance().showLoginOptionPage(true, "INFO");
				}
			}
		});

		// logout listener

		authButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (mHomePage != null && mHomePage.checkIfLogin()) {
						boolean logoutStatus = mHomePage.getPreference()
								.getBoolean(AppConstants.PREFLOGOUTBUTTONTAG,
										false);
						if (logoutStatus) {
							Info.getInstance()
									.showLoginOptionPage(true, "INFO");
						} else {
							// if (mHomePage.checkIfLogin()) {
							// mHomePage.mSimpleFacebook.logout(onLogoutListener);
							mHomePage.logoutFromTwitter();
							new submitLogoutToServer().execute("");
							// } else {
							// Info.getInstance().showLoginOptionPage(false);
							// }
						}
					} else {
						Info.getInstance().showLoginOptionPage(true, "INFO");
					}
				} else {
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
				}
			}
		});
		refreshView();
	}

	@Override
	public void notifyRefresh(String className) {
		if (className.equalsIgnoreCase("showSettingsPage"))
			refreshView();
	}

	private void refreshView() {
		boolean logoutStatus = mHomePage.getPreference().getBoolean(
				AppConstants.PREFLOGOUTBUTTONTAG, true);
		if (logoutStatus /* && !mHomePage.checkIfLogin() */) {
			logoutBtn.setText(mHomePage.getString(R.string.LOGIN));
		} else {
			logoutBtn.setText(mHomePage.getString(R.string.LOGOUT));
		}
	}

	private class submitLogoutToServer extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().show();
			// progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			String result = WebHTTPMethodClass.httpGetService("/user/logout",
					"auth_token=" + authToken + "&appkey="
							+ AppConstants.APPKEY);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().dismiss();
			// progressBar.setVisibility(View.GONE);

			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					if (sucess != null && !sucess.equals("")
							&& sucess.equals("true")) {
						// mHomePage.logoutWithFB();
						Editor prefsEditor = mHomePage.getPreferenceEditor();
						prefsEditor.putString(AppConstants.PREFLOGINID, "");
						prefsEditor.putBoolean(
								AppConstants.PREFLOGOUTBUTTONTAG, true);
						prefsEditor.putString(AppConstants.PREFAUTH_TOKEN, "");
						prefsEditor.commit();
						boolean logoutStatus = mHomePage.getPreference()
								.getBoolean(AppConstants.PREFLOGOUTBUTTONTAG,
										false);
						if (logoutStatus) {
							logoutBtn.setText(mHomePage
									.getString(R.string.LOGIN));
						} else {
							logoutBtn.setText(mHomePage
									.getString(R.string.LOGOUT));
						}
						// mHomePage.logoutWithFB();
						try {
							new submitFBLogoutToServer().execute("");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			AppConstants.parseInput(result, mHomePage);
		}
	}

	private class submitFBLogoutToServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			mHomePage.logoutWithFB();
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
		}
	}
}
