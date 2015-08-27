package com.ak.app.cb.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ak.app.cb.activity.R;
import com.ak.app.roti.info.FbLogin;
import com.ak.app.roti.info.ForgetPassword;
import com.ak.app.roti.info.LoginOption;
import com.ak.app.roti.info.LoginPage;
import com.ak.app.roti.info.SignUp;
import com.ak.app.roti.info.TermsOfUsage;
import com.ak.app.roti.pay.ManagePaymentFragment;
import com.ak.app.roti.pay.PayEarnFragment;
import com.ak.app.roti.snap.CameraHelpView;
//import com.ak.app.roti.snap.Earn;
import com.ak.app.roti.snap.LoyaltyPage;
import com.ak.app.roti.snap.ReceiptComplete;
import com.ak.app.roti.snap.ReceiptProgress;
import com.ak.app.roti.snap.ReceiptSurvey;
import com.ak.app.roti.snap.ScanBarcode;
import com.ak.app.roti.snap.ScanTutorial;
import com.ak.app.roti.snap.Servey;
import com.ak.app.roti.snap.SnapLocation;
import com.akl.app.roti.bean.LocationBean;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.RefreshListner;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

//import com.ak.app.roti.snap.SnapStart;

public class Snap extends Activity {
	private static Snap mSnapPage;
	private SharedPreferences mPreference;
	private Editor prefsEditor;
	private ProgressDialog progressDialog;
	private LayoutInflater mInflater;
	private LinearLayout mParentLayout;
	public boolean isReceiptSurvey = false;
	private List<View> snapViewList = new ArrayList<View>();

	// private LinearLayout mCameraParentLayout;

	public static Snap getInstance() {
		return mSnapPage;
	}

	public Editor getPreferenceEditor() {
		return prefsEditor;
	}

	public SharedPreferences getPreference() {
		return mPreference;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.snappage);
		mSnapPage = this;
		mPreference = PreferenceManager.getDefaultSharedPreferences(this);
		prefsEditor = mPreference.edit();
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading...");

		mInflater = LayoutInflater.from(this);
		mParentLayout = (LinearLayout) findViewById(R.id.snappage_linear_container);
		// mCameraParentLayout = (LinearLayout)
		// findViewById(R.id.snappage_linear_container_camera);
		// mCameraParentLayout.setVisibility(View.GONE);
		// showSnapLocationPage();
	}

	public void showSnapLocationPage() {
		SnapLocation socializePageView = SnapLocation.getInstance();
		RelativeLayout socializePageParentLayout = socializePageView
				.setInflater(mInflater);
		socializePageView.onCreate();
		setViewParams(socializePageParentLayout, "showSelectLocationPage",
				snapViewList);
	}

	public void showpaymentmangament() {
		ManagePaymentFragment socializePageView = ManagePaymentFragment.getInstance();
		RelativeLayout socializePageParentLayout = socializePageView
				.setInflater(mInflater);
		socializePageView.oncreate();
		setViewParams(socializePageParentLayout, "paymentmangment",
				snapViewList);
	}
	
	public void showTutorial() {
		PaymentTutorialFragment socializePageView = 	PaymentTutorialFragment.getInstance();
		RelativeLayout socializePageParentLayout = socializePageView
				.setInflater(mInflater);
		socializePageView.oncreat();
		setViewParams(socializePageParentLayout, "showTutorial",
				snapViewList);
	}

	public void showHomePage(boolean isfromTab) {
		HomeTabView socializePageView = HomeTabView.getInstance();
		RelativeLayout socializePageParentLayout = socializePageView
				.setInflater(mInflater);
		socializePageView.onCreate(isfromTab);
		setViewParams(socializePageParentLayout, "showhome",
				snapViewList);
	}

	
	
	
	public void showcardsummery() {
		CardSumaryFragment socializePageView = 	CardSumaryFragment.getInstance();
		RelativeLayout socializePageParentLayout = socializePageView
				.setInflater(mInflater);
		socializePageView.oncreat();
		setViewParams(socializePageParentLayout, "showCardsummery",
				snapViewList);
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		com.facebook.AppEventsLogger.activateApp(getApplicationContext(),
				AppConstants.FACEBOOK_APPID);
		// if (!isCamera)
		// showScanBarcodePage();
		// showScanBarcodePage();

		if (AppConstants.isNetworkAvailable(this)) {
			if (AppConstants.isNetworkAvailable(this)) {
				if (RotiHomeActivity.getInstance().checkIfLogin()) {
					showScanBarcodePage();
				} else {
					try {
						Snap.getInstance().handleBackButton();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						RotiHomeActivity.getInstance().oPenTabView(2);
						Snap.getInstance().setNextViewName(
								"showScanBarcodePage");
						Snap.getInstance().showLoginOptionPage(false, "SNAP");
					}
				}
			} else
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORNETWORKCONNECTION, this);
		}
	}

	@Override
	protected void onPause() {
		handleBackButton();
		super.onPause();
	}

	boolean isCamera = false;

	public void showCameraViewPage(LocationBean thisWeekBeans) {

		ScanActivity socializePageView = ScanActivity.getInstance();
		RelativeLayout socializePageParentLayout = socializePageView
				.setInflater(mInflater);
		socializePageView.onCreate();
		setViewParams(socializePageParentLayout, "showCameraViewPage",
				snapViewList);
	}

	public void showScanBarcodePage() {
	//	showHomePage(true);
		PayEarnFragment scanBarcodePageView = PayEarnFragment.getInstance();
		RelativeLayout scanBarcodePageParentLayout = scanBarcodePageView
				.setInflater(mInflater);
		
		scanBarcodePageView.oncreat();
		try {
			setViewParams(scanBarcodePageParentLayout, "showScanBarcodePage",
					snapViewList);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(mSnapPage, e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}

	public void showScanTutorialPage() {
		showHomePage(true);
		
		
		ScanTutorial scanTutorialPageView = ScanTutorial.getInstance();
		RelativeLayout scanTutorialPageParentLayout = scanTutorialPageView
				.setInflater(mInflater);
		scanTutorialPageView.onCreate();
		setViewParams(scanTutorialPageParentLayout, "showScanTutorialPage",
				snapViewList);
	}

	public void showCameraHelpViewPage() {
		CameraHelpView socializePageView = CameraHelpView.getInstance();
		RelativeLayout socializePageParentLayout = socializePageView
				.setInflater(mInflater);
		socializePageView.onCreate();
		setViewParams(socializePageParentLayout, "showCameraHelpViewPage",
				snapViewList);
	}

	public void showReceiptProgressPage(/* String receiptIds, String surveyIds */) {
		// receiptId = receiptIds;
		// surveyId = surveyIds;
		ReceiptProgress socializePageView = ReceiptProgress.getInstance();
		RelativeLayout socializePageParentLayout = socializePageView
				.setInflater(mInflater);
		socializePageView.onCreate(/* receiptId, surveyId */);
		setViewParams(socializePageParentLayout, "showReceiptProgressPage",
				snapViewList);
		// Tabbars.getInstance().showTabs();
		// mHomePage.hideTabs();
	}

	// public void checkReciptSubmitionStatus(boolean isStats, String
	// receiptIds,
	// String surveyIds) {
	// isStatus = isStats;
	// receiptId = receiptIds;
	// surveyId = surveyIds;
	// if (!isStatus) {
	// } else {
	// submitstaus = 1;
	// }
	// }
	//
	// public int getSubmitstaus() {
	// return submitstaus;
	// }
	//
	// int submitstaus = 0;
	//
	// public boolean showServeyPage(/* String receiptIds, String surveyIds */)
	// {
	// if (isStatus) {
	// if (submitstaus == 1) {
	// submitstaus = 0;
	// showSuccessMsgDialog("Roti", noticemessage, mSnapPage);
	// }
	// }
	// else{
	// submitstaus = 0;
	// isStatus = true;
	// showerrorMsgDialog("Roti", noticemessage, mSnapPage);
	// }
	// return isStatus;
	// }
	private boolean isStatus = false;

	public void checkReciptSubmitionStatus(boolean isStats, String receiptIds,
			String surveyIds) {
		isStatus = isStats;
		receiptId = receiptIds;
		surveyId = surveyIds;
		if (!isStatus) {
		} else {
			submitstaus = 1;
		}
		isReceipt = true;
		if (isProgress)
			showSurveymessage();
	}

	public int getSubmitstaus() {
		return submitstaus;
	}

	int submitstaus = 0;
	private boolean isProgress = false;
	private boolean isReceipt = false;

	public boolean showServeyPage() {
		isProgress = true;
		if (isReceipt)
			showSurveymessage();
		return isStatus;
	}

	private void showSurveymessage() {
		isProgress = false;
		isReceipt = false;
		if (isStatus) {
			if (submitstaus == 1) {
				submitstaus = 0;
				showSuccessMsgDialog(AppConstants.CONSTANTTITLEMESSAGE,
						noticemessage, this);
			}
		} else {
			submitstaus = 0;
			isStatus = true;
			if (noticemessage != null && !noticemessage.equals(""))
				showerrorMsgDialog(AppConstants.CONSTANTTITLEMESSAGE,
						noticemessage, this);
		}
	}

	public void showSuccessMsgDialog(String title, final String message,
			Context context) {
		try {
			if (alertDialogBuilder == null) // {
				alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder
					.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									alertDialogBuilder = null;
									Snap.getInstance().showReceiptServeyPage(
											receiptId, surveyId,
											restaurantName, receiptDate);
									dialog.cancel();
								}
							});
			AlertDialog alert = alertDialogBuilder.create();
			if (title.equals("")) {
				alert.setTitle(AppConstants.CONSTANTTITLEMESSAGE);
				alert.setIcon(AlertDialog.BUTTON_NEGATIVE);
			} else {
				alert.setTitle(title);
				alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
			}
			alert.show();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showerrorMsgDialog(String title, final String message,
			Context context) {
		try {
			if (alertDialogBuilder == null) // {
				alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder
					.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									alertDialogBuilder = null;
									Snap.getInstance().handleBackButton();
									showCameraViewPage(Tabbars.getInstance()
											.getLocationBean());
									dialog.cancel();
								}
							});
			AlertDialog alert = alertDialogBuilder.create();
			if (title.equals("")) {
				alert.setTitle(AppConstants.CONSTANTTITLEMESSAGE);
				alert.setIcon(AlertDialog.BUTTON_NEGATIVE);
			} else {
				alert.setTitle(title);
				alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
			}
			alert.show();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		Servey socializePageView = Servey.getInstance();
		RelativeLayout socializePageParentLayout = socializePageView
				.setInflater(mInflater);
		socializePageView.onCreate(receiptId, surveyId, restaurantName,
				receiptDate);
		setViewParams(socializePageParentLayout, "showReceiptServeyPage",
				snapViewList);
	}

	public void showReceiptCompletePage() {
		ReceiptComplete socializePageView = ReceiptComplete.getInstance();
		RelativeLayout socializePageParentLayout = socializePageView
				.setInflater(mInflater);
		socializePageView.onCreate();
		setViewParams(socializePageParentLayout, "showReceiptCompletePage",
				snapViewList);
		isCamera = false;
	}

	public void showLoyaltyPage() {
		LoyaltyPage loyalty = LoyaltyPage.getInstance();
		RelativeLayout socializePageParentLayout = loyalty
				.setInflater(mInflater);
		loyalty.onCreate();
		setViewParams(socializePageParentLayout, "showLoyaltyPage",
				snapViewList);
		isCamera = false;
	}

	public void showReceiptServeyPage() {
		ReceiptSurvey socializePageView = ReceiptSurvey.getInstance();
		RelativeLayout socializePageParentLayout = socializePageView
				.setInflater(mInflater);
		socializePageView.onCreate();
		setViewParams(socializePageParentLayout, "showReceiptServeyPage",
				snapViewList);
		isCamera = false;
	}

	private void setViewParams(View view, String tagName, List<View> viewList) {
		mParentLayout.removeAllViews();
		view.setTag(tagName);
		checkIfViewExist(view, snapViewList);
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
				snapViewList);
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
				snapViewList);
	}

	public void showFbLoginPage(boolean b) {
		FbLogin InfoView = FbLogin.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "SNAP");
		setViewParams(infoParentLayout, "showInfoPage", snapViewList);
	}

	public void showFbLoginPage(boolean b, String pageDestination) {
		FbLogin InfoView = FbLogin.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.pageDestination = pageDestination;
		InfoView.onCreate(b, "SNAP");
		setViewParams(infoParentLayout, "showInfoPage", snapViewList);
	}

	public void showSignUpPage(boolean b) {
		SignUp InfoView = SignUp.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "SNAP");
		setViewParams(infoParentLayout, "showFbLoginPage", snapViewList);
	}

	public void showSignUpPage(boolean b, String pageDestination) {
		SignUp InfoView = SignUp.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.pageDestinationAfterSignup = pageDestination;
		InfoView.onCreate(b, "SNAP");
		setViewParams(infoParentLayout, "showFbLoginPage", snapViewList);
	}

	public void showLoginPage(boolean b) {
		LoginPage InfoView = LoginPage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "SNAP");
		setViewParams(infoParentLayout, "showLoginPage", snapViewList);
	}

	public void showLoginPage(boolean b, String pageDestination) {
		LoginPage InfoView = LoginPage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.pageDestination = pageDestination;
		InfoView.onCreate(b, "SNAP");
		setViewParams(infoParentLayout, "showLoginPage", snapViewList);
	}

	public void showForgetPasswordPage(boolean b) {
		ForgetPassword InfoView = ForgetPassword.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "SNAP");
		setViewParams(infoParentLayout, "showForgetPasswordPage", snapViewList);
	}

	public void showTermsOfUsagePage(boolean b, String URL, String tabName,
			String title) {
		TermsOfUsage InfoView = TermsOfUsage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, URL, tabName, title);
		setViewParams(infoParentLayout, "showInfoPage", snapViewList);
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
			if (nextViewName.equals("showCameraViewPage")) {
				showCameraViewPage(Tabbars.getInstance().getLocationBean());
			}
			if (nextViewName.equals("Servey")) {
				showReceiptServeyPage(receiptId, surveyId, restaurantName,
						receiptDate);
			}
			if (nextViewName.equals("showScanBarcodePage")) {
				showScanBarcodePage();
			}
		} catch (Exception e) {
		}
		nextViewName = "";
	}

	public void exitAppFunc() {
		
	
	//Tabbars.getInstance().returnhome();
	//finish();
	RotiHomeActivity.getInstance().oPenTabView(
			0);

	
	}
@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	if (snapViewList != null && snapViewList.size() > 1) {
	//	Toast.makeText(mSnapPage, "56", Toast.LENGTH_SHORT).show();
		handleBackButton();
	}
	else
	{
		exitAppFunc();
			
	}


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
		if (snapViewList != null && snapViewList.size() > 1) {
			snapViewList.remove(snapViewList.size() - 2);
		}
	}

	public boolean submitbtn() {
		boolean isLast = false;

		if (snapViewList != null
				&& snapViewList.size() > 1
				&& ((String) snapViewList.get(snapViewList.size() - 1).getTag())
						.equalsIgnoreCase("showReceiptServeyPage")) {
			if (snapViewList != null && snapViewList.size() > 1) {
				mParentLayout.removeAllViews();
				mParentLayout
						.addView(snapViewList.get(snapViewList.size() - 1));
				snapViewList.remove(snapViewList.size() - 1);
				isLast = true;
			}
			showReceiptServeyPage();
		} else {
			isLast = setBackButtonHandledView(snapViewList, 0);
		}
		return isLast;
	}

	public boolean handleBackButton() {
		boolean isLast = false;

		if (snapViewList != null
				&& snapViewList.size() > 1
				&& ((String) snapViewList.get(snapViewList.size() - 1).getTag())
						.equalsIgnoreCase("showReceiptServeyPage")
				&& isReceiptSurvey == false) {
			if (snapViewList != null && snapViewList.size() > 1) {
				mParentLayout.removeAllViews();
				mParentLayout
						.addView(snapViewList.get(snapViewList.size() - 1));
				snapViewList.remove(snapViewList.size() - 1);
				isLast = true;
			}
			// new skipSurvey().execute(""); elang edit

		} else if (snapViewList != null
				&& snapViewList.size() > 1
				&& (((String) snapViewList.get(0).getTag())
						.equals("showLoginPage")
						|| ((String) snapViewList.get(0).getTag())
								.equals("showFbLoginPage") || ((String) snapViewList
						.get(0).getTag()).equals("showInfoPage"))) {

			if (snapViewList != null && snapViewList.size() > 1) {
				mParentLayout.removeAllViews();
				mParentLayout
						.addView(snapViewList.get(snapViewList.size() - 1));
				snapViewList.remove(snapViewList.size() - 1);
				
				isLast = true;
			}
			showScanBarcodePage();

		} else {
			isLast = setBackButtonHandledView(snapViewList, 0);
		}
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
		if (viewName.equalsIgnoreCase("showSelectLocationPage")) {
			RefreshListner listner = ScanBarcode.getInstance();
			if (listner != null)
				listner.notifyRefresh("showSelectLocationPage");
		} else if (viewName.equalsIgnoreCase("showReceiptServeyPage")) {
			RefreshListner listner = Servey.getInstance();
			if (listner != null)
				listner.notifyRefresh("showReceiptServeyPage");
		} else if (viewName.equalsIgnoreCase("showScanBarcodePage")) {
			RefreshListner listner = ScanBarcode.getInstance();
			if (listner != null)
				listner.notifyRefresh("showScanBarcodePage");
		}
	}

	public void parseInput(String result, Activity mHomePage) {
		if (result != null && !result.equals("")) {
			try {
				if (result.equals(""))
					return;
				JSONObject resObject = new JSONObject(result);
				String sucess = "";// resObject.getString("status");
				String notice = "";
				if (resObject.has("status"))
					sucess = resObject.getString("status");
				if (resObject.has("notice")) {
					notice = resObject.getString("notice");
					if (!notice.equals("")
							&& notice.equals("Unauthorized API request.")) {
						// mHomePage.showLoginOptionPage(false);//TODO
						return;
					}
				}
				if (resObject.has("message"))
					notice = resObject.getString("message");

				String errors = "";
				String auth_token = "";
				try {
					if (resObject.has("auth_token")) {
						auth_token = resObject.getString("auth_token");
						SharedPreferences mPreference = PreferenceManager
								.getDefaultSharedPreferences(mHomePage);
						Editor prefsEditor = mPreference.edit();
						prefsEditor.putString(AppConstants.PREFAUTH_TOKEN,
								auth_token);
						Log.d("auth_token", auth_token);
						prefsEditor.commit();
					}
				} catch (Exception e) {
				}
				try {
					if (resObject.has("errors")) {
						errors = resObject.getString("errors");
					}
				} catch (Exception e) {
				}
				noticemessage = notice;
				if (sucess != null && !sucess.equals("") && !notice.equals("")) {// false
				} else {
					if (errors != null && (!errors.equals(""))) {
						showMsgDialog(AppConstants.CONSTANTTITLEMESSAGE, errors
								+ notice, mHomePage);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static String noticemessage = "";
	static AlertDialog.Builder alertDialogBuilder;

	public void showMsgDialog(String title, final String message,
			Context context) {
		try {
			if (alertDialogBuilder == null) // {
				alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder
					.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									alertDialogBuilder = null;
									dialog.cancel();
								}
							});
			AlertDialog alert = alertDialogBuilder.create();
			if (title.equals("")) {
				alert.setTitle(AppConstants.CONSTANTTITLEMESSAGE);
				alert.setIcon(AlertDialog.BUTTON_NEGATIVE);
			} else {
				alert.setTitle(title);
				alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
			}
			alert.show();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "List Offer").build());
	}

}