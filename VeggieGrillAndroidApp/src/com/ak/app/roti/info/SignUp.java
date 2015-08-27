package com.ak.app.roti.info;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings.Secure;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.Rewards;
import com.ak.app.cb.activity.RotiHomeActivity;
import com.ak.app.cb.activity.Snap;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class SignUp {
	private static SignUp screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	// private Tabbars mHomePage;
	private Activity mHomePage;
	private String androidOS = Build.VERSION.RELEASE;
	private String model = Build.MODEL;
	private String manufacturer = Build.MANUFACTURER;
	public String pageDestinationAfterSignup = "";

	// private ProgressBar progressBar;

	public static SignUp getInstance() {
		if (screen == null)
			screen = new SignUp();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.info_signupemail, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	ImageView logoutBtn;

	boolean isInfo = false;
	String mTabName;
	EditText emailEdit;
	EditText pwdEdit;
	EditText zipCodeEdit;
	// EditText phoneEdit;
	EditText firstNameEdit;
	EditText lastNameEdit;
	ImageView signupBtn;
	EditText editTampDob;

	DatePicker date_picker;
	RelativeLayout datepickerclick;
	TextView textDob;
	Integer dobYear;
	Integer dobMonth;
	Integer dobDate;
	private int year, month, day;
	private int selectedDay, selectedMonth, selectedYear;

	public void onCreate(boolean b, final String tabName) {
		isInfo = b;
		mTabName = tabName;
		mHomePage = Tabbars.getInstance();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Email Sign up").build());

		if (mHomePage == null)
			mHomePage = RotiHomeActivity.getInstance();
		emailEdit = (EditText) mParentLayout
				.findViewById(R.id.signup_edit_email);
		firstNameEdit = (EditText) mParentLayout
				.findViewById(R.id.signup_edit_first_name);
		lastNameEdit = (EditText) mParentLayout
				.findViewById(R.id.signup_edit_last_name);
		pwdEdit = (EditText) mParentLayout
				.findViewById(R.id.signup_edit_password);
		zipCodeEdit = (EditText) mParentLayout
				.findViewById(R.id.signup_edit_ZipCode);
		TextView signUpText = (TextView) mParentLayout
				.findViewById(R.id.signUpText);
		TextView optinText = (TextView) mParentLayout
				.findViewById(R.id.optinText);
		editTampDob = (EditText) mParentLayout.findViewById(R.id.edit_tamp_dob);
		final CheckBox marketingOptionCB = (CheckBox) mParentLayout
				.findViewById(R.id.marketingOptionCB);
		// progressBar = (ProgressBar) mParentLayout
		// .findViewById(R.id.greenProgressBar);
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
//
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
//
//			}
//		});

		AppConstants.fontDinLightTextView(signUpText, 26,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
		AppConstants.gothamNarrowBookTextView(zipCodeEdit, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());
		
		AppConstants.gothamNarrowBookTextView(editTampDob, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());
		
		AppConstants.gothamNarrowBookTextView(emailEdit, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());
		AppConstants.gothamNarrowBookTextView(pwdEdit, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());

		AppConstants.gothamNarrowBookTextView(firstNameEdit, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());
		AppConstants.gothamNarrowBookTextView(lastNameEdit, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());
		
//		AppConstants.gothamNarrowBookTextView(optinText, 11,
//				AppConstants.COLORBLACKCORNERBAKERYINFO, mHomePage.getAssets());

		datepickerclick = (RelativeLayout) mParentLayout
				.findViewById(R.id.layoutdob);
		textDob = (TextView) mParentLayout.findViewById(R.id.textviewdob);
		
		AppConstants.gothamNarrowBookTextView(textDob, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());
		
		final EditText referEdit = (EditText) mParentLayout
				.findViewById(R.id.signup_edit_Refercodeoptional);

		referEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					referEdit.setBackgroundResource(R.drawable.formfield);
				else if (referEdit.getText().toString().equals(""))
					referEdit.setBackgroundResource(R.drawable.promo_btn_gray);

			}
		});
		AppConstants.americanTypewriterTextView(referEdit, 13,
				AppConstants.COLORGREYCORNERBAKERY, mHomePage.getAssets());
//		AppConstants.americanTypewriterTextView(textDob, 13,
//				AppConstants.COLORGREYCORNERBAKERY, mHomePage.getAssets());
		signupBtn = (ImageView) mParentLayout
				.findViewById(R.id.signup_image_signup);

		dobYear = 0;
		dobMonth = 0;
		dobDate = 0;

		final Calendar calendar = Calendar.getInstance();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		selectedDay = day;
		selectedMonth = month;
		selectedYear = year;

		datepickerclick.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				final Dialog dialog = new Dialog(mHomePage);
				dialog.setContentView(R.layout.customdialog);

				date_picker = (DatePicker) dialog
						.findViewById(R.id.date_picker);

				if (selectedDay != day || selectedMonth != month
						|| selectedYear != year)
					date_picker.init(selectedYear, selectedMonth, selectedDay,
							null);
				else
					date_picker.init(year, month, day, null);

				Button dialogButton = (Button) dialog
						.findViewById(R.id.dialogButtonOK);
				// if button is clicked, close the custom dialog
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						date_picker.clearFocus();
						dobYear = date_picker.getYear();
						dobMonth = date_picker.getMonth() + 1;
						dobDate = date_picker.getDayOfMonth();

						Calendar currentDate = Calendar.getInstance();

						Integer d = currentDate.get(Calendar.DATE);
						Integer m = currentDate.get(Calendar.MONTH) + 1;
						Integer y = currentDate.get(Calendar.YEAR);

						Calendar validDate = Calendar.getInstance();
						validDate.set(dobYear, dobMonth - 1, dobDate);

						StringBuilder sb = new StringBuilder();
						if (validDate.before(currentDate)) {
							selectedYear = dobYear;
							selectedMonth = dobMonth - 1;
							selectedDay = dobDate;

							sb.append(getMonth(dobMonth)).append(" ")
									.append(dobDate.toString()).append(", ")
									.append(dobYear.toString());
						} else {
							selectedYear = y;
							selectedMonth = m - 1;
							selectedDay = d;
							sb.append(getMonth(m)).append(" ")
									.append(d.toString()).append(", ")
									.append(y.toString());
						}
						String dobStr = sb.toString();

						textDob.setText(dobStr);
						editTampDob.setText(dobStr);
						datepickerclick
								.setBackgroundResource(R.drawable.promo_btn_gray);

						dialog.dismiss();
					}
				});

				dialog.show();
			}

		});

		SetTextWatcherForAmountEditView(emailEdit);
		SetTextWatcherForAmountEditView(pwdEdit);
		SetTextWatcherForAmountEditView(firstNameEdit);
		SetTextWatcherForAmountEditView(lastNameEdit);
		SetTextWatcherForAmountEditView(zipCodeEdit);
		SetTextWatcherForAmountEditView(editTampDob);

		signupBtn.setAlpha(0.5f);
		signupBtn.setEnabled(false);

		signupBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					InputMethodManager inputManager = (InputMethodManager) mHomePage
							.getSystemService(Activity.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(
							emailEdit.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String email = emailEdit.getText().toString();
				String mPassword = pwdEdit.getText().toString();
				String mrefer = referEdit.getText().toString();
				String marketingOptin = "";
				String zipcode = zipCodeEdit.getText().toString();
				String firstName = firstNameEdit.getText().toString();
				String lastName = lastNameEdit.getText().toString();

				if (marketingOptionCB.isChecked())
					marketingOptin = "true";
				else
					marketingOptin = "false";

				if (AppConstants.isNetworkAvailable(mHomePage)) {
					showBackConfirmMsgDialog(
							"",
							mHomePage.getString(R.string.signupheader)
									+ "\""
									+ email
									+ "\""

									+ mHomePage
											.getString(R.string.signupmessages),
							email, mPassword, mrefer, marketingOptin, zipcode,
							firstName, lastName);
				} else {
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
				}

			}
		});
		mHomePage.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	private String getMonth(int month) {
		return new DateFormatSymbols().getMonths()[month - 1];
	}

	public void showconfirmDialog() {
		try {
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(mHomePage);
			alt_bld.setMessage("Please enter a valid 10-digit number")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();

								}
							});
			AlertDialog alert = alt_bld.create();
			alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
			alert.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
				if (filterLongEnough(emailEdit) && filterLongEnough(pwdEdit)
						&& filterLongEnough(firstNameEdit)
						&& filterLongEnough(lastNameEdit)
						&& filterLongEnough(zipCodeEdit)
						&& filterLongEnough(editTampDob)) {
					try {
						signupBtn.setAlpha(1f);
						signupBtn.setBackgroundResource(R.drawable.signupemail_btn_signup);
						signupBtn.setEnabled(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					signupBtn.setAlpha(0.5f);
					signupBtn.setEnabled(false);
				}
			}

			private boolean filterLongEnough(EditText amountEditText) {
				return amountEditText.getText().toString().trim().length() > 0;
			}

			private boolean filterLongEnoughPhone(EditText amountEditText) {
				return amountEditText.getText().toString().trim().length() >= 12;
			}
		};
		amountEditText.addTextChangedListener(fieldValidatorTextWatcher);
	}

	// public void showBackConfirmMsgDialog(String title, final String message,
	// final String email, final String mPassword, final String phone,
	// final String mrefer, final String fname, final String lname) {
	private void showBackConfirmMsgDialog(String title, final String message,
			final String email, final String mPassword, final String mrefer,
			final String marketingOptin, final String zipcode,
			final String firstName, final String lastName) {
		try {
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(mHomePage);
			alt_bld.setMessage(message)
					.setCancelable(false)
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							})
					.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();

									if (AppConstants
											.isNetworkAvailable(mHomePage)) {
										String android_id = Secure.getString(
												mHomePage.getContentResolver(),
												Secure.ANDROID_ID);

										String[] param = new String[] { email,
												mPassword, mrefer, android_id,
												marketingOptin, zipcode,
												firstName, lastName };

										new submitSignUpDetailsToServer()
												.execute(param);
									} else {
										AppConstants
												.showMsgDialog(
														"Alert",
														AppConstants.ERRORNETWORKCONNECTION,
														mHomePage);
									}
								}
							});
			AlertDialog alert = alt_bld.create();
			alert.setTitle(AppConstants.CONSTANTTITLEMESSAGE);
			alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
			alert.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	String mEmailID = "";

	private class submitSignUpDetailsToServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (Tabbars.getInstance() != null
					&& Tabbars.getInstance().getProgressDialog() != null)
				Tabbars.getInstance().getProgressDialog().show();
			else if (RotiHomeActivity.getInstance() != null
					&& RotiHomeActivity.getInstance().getProgressDialog() != null)
				RotiHomeActivity.getInstance().getProgressDialog().show();
			// progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			String regId = "";// mHomePage.getPreference().getString(
			// AppConstants.PREFPUSHREGISTRATIONID, "");

			String strday;
			String strmonth;
			String stryear;
			strday = dobDate.toString();
			strmonth = dobMonth.toString();
			stryear = dobYear.toString();
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
			nameparams.add(new BasicNameValuePair("zipcode", params[5]));
			nameparams.add(new BasicNameValuePair("first_name", params[6]));
			nameparams.add(new BasicNameValuePair("last_name", params[7]));
			nameparams.add(new BasicNameValuePair("android_id", params[3]));
			nameparams.add(new BasicNameValuePair("latitude", Tabbars
					.getInstance() != null ? Tabbars.getInstance()
					.getGetLatLongObj().getLatitude()
					+ "" : RotiHomeActivity.getInstance().getGetLatLongObj()
					.getLatitude()
					+ ""));
			nameparams.add(new BasicNameValuePair("longitude", Tabbars
					.getInstance() != null ? Tabbars.getInstance()
					.getGetLatLongObj().getLongitude()
					+ "" : RotiHomeActivity.getInstance().getGetLatLongObj()
					.getLongitude()
					+ ""));
			nameparams.add(new BasicNameValuePair("register_device_type",
					AppConstants.DEVICE_TYPE));
			nameparams.add(new BasicNameValuePair("register_type",
					AppConstants.REGISTERTYPE));
			nameparams
					.add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
			nameparams.add(new BasicNameValuePair("referral_code", params[2]));
			nameparams
					.add(new BasicNameValuePair("marketing_optin", params[4]));
			nameparams.add(new BasicNameValuePair("sign_in_device_type",
					AppConstants.DEVICE_TYPE));
			nameparams.add(new BasicNameValuePair("device_token", regId));
			nameparams.add(new BasicNameValuePair("device_id", AppConstants
					.getDeviceID(mHomePage)));
			nameparams.add(new BasicNameValuePair("phone_model", manufacturer
					+ " " + model));
			nameparams.add(new BasicNameValuePair("os", androidOS));
			nameparams.add(new BasicNameValuePair("dob_day", strday));
			nameparams.add(new BasicNameValuePair("dob_month", strmonth));
			nameparams.add(new BasicNameValuePair("dob_year", stryear));
			String result = WebHTTPMethodClass.executeHttPost("/user/signup",
					nameparams);// loginService();
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		// {"status":true,"notice":"Check My Rewards page if you qualify for any rewards","auth_token":"aQdfpNqfTd51Qrs9ZGc4"}

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
					String notice = resObject.getString("notice");
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
							e.printStackTrace();
						}
						prefsEditor.commit();
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
							AppConstants.parseInput(result, mHomePage);
						} else if (mTabName.equals("SNAP")
								&& Snap.getInstance() != null) {
							Snap.getInstance().popPreviousView();
							Snap.getInstance().handleBackButton();
							if (pageDestinationAfterSignup
									.equals("scanReceiptBarcode"))
								Snap.getInstance().showScanBarcodePage();
							else
								Snap.getInstance().setNextView();
						} else if (mTabName.equals("ROTIHOMEACTIVITY")
								&& RotiHomeActivity.getInstance() != null) {
							RotiHomeActivity.getInstance().popPreviousView();
							RotiHomeActivity.getInstance().handleBackButton();
							if (pageDestinationAfterSignup
									.equals("referFriend"))
								RotiHomeActivity.getInstance()
										.showReferFriendPage("info");
							else
								RotiHomeActivity.getInstance().setNextView();
						} else if (mTabName.equals("REWARDS")
								&& Rewards.getInstance() != null) {
							Rewards.getInstance().popPreviousView();
							Rewards.getInstance().handleBackButton();
							Rewards.getInstance().setNextView();
						}
					} else {
						AppConstants.parseInput(result, mHomePage);
						AppConstants.showMsgDialog("Alert", notice, mHomePage);
					}
				} catch (Exception e) {
					e.printStackTrace();
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORFAILEDAPI, mHomePage);
				}
			} else {
				AppConstants.parseInput(result, mHomePage);
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mHomePage);
			}
			// AppConstants.parseInput(result, mHomePage);
		}
	}
}
