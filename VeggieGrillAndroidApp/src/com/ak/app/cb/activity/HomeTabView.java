package com.ak.app.cb.activity;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class HomeTabView {
	private static HomeTabView screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;

	public static HomeTabView getInstance() {
		screen = new HomeTabView();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(R.layout.splash,
				null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	public void onCreate(boolean isfromTab) {
		mHomePage = Tabbars.getInstance();
		//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
		//				AppConstants.GA_TRACK_ID);
		//		tracker.send(MapBuilder.createAppView()
		//				.set(Fields.SCREEN_NAME, "Home Page").build());

		if (AppConstants.isBarcodePage == false) {
			AppConstants.setScreenBrightnessToDefault(mHomePage);
		}
		AppConstants.changeScreenBrightnessToDefault(mHomePage);

		Button referFriendBtn = (Button) mParentLayout
				.findViewById(R.id.tellAFriendButton);
		Button homeEarnBtn = (Button) mParentLayout
				.findViewById(R.id.scanPayButton);
		// ImageView homeMenuBtn = (ImageView) mParentLayout
		// .findViewById(R.id.homeMenuBtn);
		Button homeRewardsBtn = (Button) mParentLayout
				.findViewById(R.id.myRewardButton);
		//		ImageView homeLocationsBtn = (ImageView) mParentLayout
		//				.findViewById(R.id.homeLocationsBtn);

		if (AppConstants.isNetworkAvailable(mHomePage)) {
			if (RotiHomeActivity.getInstance().checkIfLogin()) {
				new pullSurveyAsyncTask().execute("");
			}
		} else
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, mHomePage);

		homeEarnBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isTutorialShown = mHomePage.getPreference().getBoolean(
						AppConstants.PREF_SNAP_ISNOTOPENSTARTPAGE, false);

				if (AppConstants.isNetworkAvailable(mHomePage)) {
					
					if (RotiHomeActivity.getInstance().checkIfLogin()) {
				
						
						if (!isTutorialShown) {
							
							
							RotiHomeActivity.getInstance().oPenTabView(2);
							Snap.getInstance().showScanTutorialPage();
							Editor editSession = mHomePage.getPreferenceEditor();
							editSession.putBoolean(
									AppConstants.PREF_SNAP_ISNOTOPENSTARTPAGE, true);
							editSession.commit();
						} else
						{

							
							RotiHomeActivity.getInstance().oPenTabView(2);
							Snap.getInstance().showScanBarcodePage();
						
						}
	
					
					
					}	
				
					
					
					else {
						try {
							//	Toast.makeText(mHomePage, "back", Toast.LENGTH_LONG).show();
							RotiHomeActivity.getInstance().showLoginOptionPage(
									false, "ROTIHOMEACTIVITY", "REWARDS");
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								//Toast.makeText(mHomePage, "back2", Toast.LENGTH_LONG).show();
							}
		
					
					
					}
			}
			
			
					else
					{
						AppConstants.showMsgDialog("Alert",
								AppConstants.ERRORNETWORKCONNECTION,
								RotiHomeActivity.getInstance());
				}
			
			
		}
		});
		referFriendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (RotiHomeActivity.getInstance().checkIfLogin()) {
						RotiHomeActivity.getInstance().showReferFriendPage("Roti");
					} else {
						RotiHomeActivity.getInstance().showLoginOptionPage(
								false, "ROTIHOMEACTIVITY", "referFriend");
					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
			}
		});
		homeRewardsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Tabbars.getInstance().showTabs();
//				Tabbars.getInstance().stopGPS();
				if (AppConstants.isNetworkAvailable(RotiHomeActivity
						.getInstance())) {
					if (RotiHomeActivity.getInstance().checkIfLogin()) {
						//Toast.makeText(mHomePage, "login", Toast.LENGTH_LONG).show();
						RotiHomeActivity.getInstance().oPenTabView(1);
					} else {
						try {
						//	Toast.makeText(mHomePage, "back", Toast.LENGTH_LONG).show();
									RotiHomeActivity.getInstance().showLoginOptionPage(
									false, "ROTIHOMEACTIVITY", "REWARDS");
					
							//Info.getInstance().handleBackButton();
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							//Toast.makeText(mHomePage, "back2", Toast.LENGTH_LONG).show();
							
							//RotiHomeActivity.getInstance().oPenTabView(1);
						//	RotiHomeActivity.getInstance().handleBackButton();
						}
					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION,
							RotiHomeActivity.getInstance());
			}
		});
		//		homeLocationsBtn.setOnClickListener(new OnClickListener() {
		//			@Override
		//			public void onClick(View v) {
		//				if (AppConstants.CheckEnableGPS(mHomePage)) {
		//					Tabbars.getInstance().startGPS();
		//					if (AppConstants.isNetworkAvailable(mHomePage)) {
		//						RotiHomeActivity.getInstance().oPenTabView(3);
		//						Location.getInstance().showLocationPage();
		//					} else
		//						AppConstants.showMsgDialog("",
		//								AppConstants.ERRORNETWORKCONNECTION, mHomePage);
		//				} else
		//					AppConstants.showMsgDialog("",
		//							AppConstants.ERRORLOCATIONSERVICES, mHomePage);
		//			}
		//		});
	}

	private void showconfirmDialog() {
		try {
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(mHomePage);
			alt_bld.setMessage(
					"If this is your first time ordering with us, after selecting your order items you must choose 'Create Account' or 'Checkout as Guest'.")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									RotiHomeActivity.getInstance().oPenTabView(
											4);
									Info.getInstance().showWebView(
											AppConstants.URL_ORDER_ONLINE);
									Editor edit = mHomePage
											.getPreferenceEditor();
									edit.putBoolean(
											AppConstants.IS_ORDER_ONLINE_POPUPOPEN,
											true);
									edit.commit();
								}
							});
			AlertDialog alert = alt_bld.create();
			alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
			alert.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JSONObject responseJson;
	Boolean isGetApiSurvey = true;

	private class pullSurveyAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().show();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			String errorMessage = "";
			try {
				responseJson = new JSONObject(result.toString());
				Log.i("elang", "elang->" + responseJson);
				if (responseJson.getBoolean("status") == true) {
					Log.i("elang", "elang->1");
					// RotiHomeActivity.getInstance().oPenTabView(2);
					RotiHomeActivity.getInstance().showReceiptServeyPage(
							responseJson.getString("receipt_id"),
							responseJson.getString("survey_id"),
							responseJson.getString("restaurant_name"),
							responseJson.getString("receipt_date"));
				}
			} catch (Exception e) {
				Log.i("elang", "elang->2" + e);
				isGetApiSurvey = false;
			}

			if (isGetApiSurvey == false) {
				isGetApiSurvey = true;
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mHomePage);
			}

			if (errorMessage != null && errorMessage.length() > 0
					&& errorMessage.equals("401")) {
				AppConstants.showMessageDialogWithNewIntent(
						AppConstants.ERROR401SERVICES, mHomePage);
			} else if (errorMessage != null && errorMessage.length() > 0) {
				AppConstants.showMsgDialog("", errorMessage, mHomePage);
			}

			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().dismiss();
		}

		@Override
		protected String doInBackground(String... arg0) {
			String result = pullSurvey();
			return result;
		}
	}

	private String pullSurvey() {
		String line = "";
		try {
			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			String param = "appkey=" + AppConstants.APPKEY + "&auth_token="
					+ authToken;
			// param += "&locale=" + AppConstants.LOCALE_HEADER_VALUE;
			String params = null;

			Log.i("request", "url " + param + "  param " + params);
			line = WebHTTPMethodClass.httpGetService("/survey/pull", param);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return line;
	}
}
