package com.ak.app.roti.info;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.app.cb.activity.RotiHomeActivity;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class FbLogin {
	private static FbLogin screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	// private Tabbars mHomePage;
	private Activity mHomePage;
	private SharedPreferences mPreference;
	private Editor prefsEditor;
	public String pageDestination = "";
	private int year, month, day;
	private int selectedDay, selectedMonth, selectedYear;
	DatePicker date_picker;
	RelativeLayout datepickerclick;
	TextView textDob;
	Integer dobYear;
	Integer dobMonth;
	Integer dobDate;

	public static FbLogin getInstance() {
		if (screen == null)
			screen = new FbLogin();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.info_fb_signin, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	ImageView logoutBtn;
	boolean isInfo = false;
	// EditText phoneEdit;
	ImageView signupBtn;
	String mTabName;
	EditText zipCodeEdit;
	EditText firstNameEdit;
	EditText lastNameEdit;
	EditText editTampDob;

	public void onCreate(boolean b, final String tabName) {

		isInfo = b;
		mTabName = tabName;
		mHomePage = Tabbars.getInstance();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Facebook Connect").build());

		if (mHomePage == null)
			mHomePage = RotiHomeActivity.getInstance();
		final EditText referEdit = (EditText) mParentLayout
				.findViewById(R.id.loginoption_fb_edittext_refercode);
		final CheckBox marketingOptionCB = (CheckBox) mParentLayout
				.findViewById(R.id.marketingOptionCB);
		TextView optinText = (TextView) mParentLayout
				.findViewById(R.id.optinText);

		zipCodeEdit = (EditText) mParentLayout
				.findViewById(R.id.loginoption_fb_edittext_ZipCode);
		firstNameEdit = (EditText) mParentLayout
				.findViewById(R.id.loginoption_fb_edittext_first_name);
		lastNameEdit = (EditText) mParentLayout
				.findViewById(R.id.loginoption_fb_edittext_last_name);
		TextView signinText = (TextView) mParentLayout
				.findViewById(R.id.signinText);
		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		editTampDob = (EditText) mParentLayout.findViewById(R.id.edit_tamp_dob);
		AppConstants.odinRoundedBoldTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());

		referEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					referEdit.setBackgroundResource(R.drawable.formfield);
				else if (referEdit.getText().toString().equals(""))
					referEdit.setBackgroundResource(R.drawable.promo_btn_gray);
			}
		});
		// phoneEdit = (EditText) mParentLayout
		// .findViewById(R.id.loginoptionfb_text_phone);
		signupBtn = (ImageView) mParentLayout
				.findViewById(R.id.loginoption_fb_image_facebook);
		// TextView signuptext = (TextView) mParentLayout
		// .findViewById(R.id.signuptext);
		// TextView backText = (TextView) mParentLayout
		// .findViewById(R.id.loginoption_fb_text_back);
		// TextView tapFBText = (TextView) mParentLayout
		// .findViewById(R.id.loginoption_fb_text_tapbutton);
		// tapFBText.setText("");

		datepickerclick = (RelativeLayout) mParentLayout
				.findViewById(R.id.layoutdob);
		textDob = (TextView) mParentLayout.findViewById(R.id.textviewdob);

		AppConstants.fontDinLightTextView(signinText, 26,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
		AppConstants.americanTypewriterTextView(zipCodeEdit, 13,
				AppConstants.COLORGREYCORNERBAKERY, mHomePage.getAssets());
		AppConstants.americanTypewriterTextView(referEdit, 13,
				AppConstants.COLORGREYCORNERBAKERY, mHomePage.getAssets());
		AppConstants.americanTypewriterTextView(firstNameEdit, 13,
				AppConstants.COLORGREYCORNERBAKERY, mHomePage.getAssets());
		AppConstants.americanTypewriterTextView(lastNameEdit, 13,
				AppConstants.COLORGREYCORNERBAKERY, mHomePage.getAssets());
		AppConstants.americanTypewriterTextView(textDob, 13,
				AppConstants.COLORGREYCORNERBAKERY, mHomePage.getAssets());
		AppConstants.americanTypewriterTextView(optinText, 11,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());

		final Calendar calendar = Calendar.getInstance();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		selectedDay = day;
		selectedMonth = month;
		selectedYear = year;

		dobYear = 0;
		dobMonth = 0;
		dobDate = 0;

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

		SetTextWatcherForAmountEditView(firstNameEdit);
		SetTextWatcherForAmountEditView(lastNameEdit);
		SetTextWatcherForAmountEditView(zipCodeEdit);
		SetTextWatcherForAmountEditView(editTampDob);
		signupBtn.setBackgroundResource(R.drawable.signup_btn_facebook);
		signupBtn.setEnabled(false);

		// AppConstants.fontGothamMediumTextView(tapFBText, 14,
		// AppConstants.COLORGREY, mHomePage.getAssets());
		signupBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String mrefer = referEdit.getText().toString();
				// String phone = phoneEdit.getText().toString();

				// String newPhone = phone.replace("-", "");
				String android_id = Secure.getString(
						mHomePage.getContentResolver(), Secure.ANDROID_ID);
				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(mHomePage);
				SharedPreferences.Editor editor = preferences.edit();
				String strday = dobDate.toString();
				String strmonth = dobMonth.toString();
				String stryear = dobYear.toString();
				String marketingOptin = "";
				String zipcode = zipCodeEdit.getText().toString();
				String firstName = firstNameEdit.getText().toString();
				String lastName = lastNameEdit.getText().toString();

				Date bornTime;
				Date timeNow;
				long differenceYears = 0;

				if (!strday.equals("0") && !strmonth.equals("0")
						&& !stryear.equals("0")) {
					bornTime = new Date(String.valueOf(month + 1) + "/"
							+ String.valueOf(day) + "/" + String.valueOf(year));
					timeNow = new Date(strmonth + "/" + strday + "/" + stryear);

					differenceYears = (bornTime.getTime() - timeNow.getTime())
							/ (24 * 60 * 60 * 1000);
				}
				if (marketingOptionCB.isChecked())
					marketingOptin = "true";
				else
					marketingOptin = "false";
				// editor.putString("phoneNumber", newPhone);
				editor.putString("android_id", android_id);
				editor.putString("marketing_optin", marketingOptin);
				editor.putString("day_dob", strday);
				editor.putString("month_dob", strmonth);
				editor.putString("year_dob", stryear);
				editor.putString("zipcode", zipcode);
				editor.putString("first_name", firstName);
				editor.putString("last_name", lastName);
				editor.commit();

				// mPreference = null;
				// prefsEditor = mPreference.edit();

				// prefsEditor.putString("phoneNumber",
				// newPhone);
				// prefsEditor.commit();

				// char checkPhone = phone.charAt(0);
				// String firstNumber = Character.toString(checkPhone);
				// if (firstNumber.equals("0")) {
				// showconfirmDialog();
				// } else {

				if (AppConstants.isNetworkAvailable(mHomePage)) {
					// mHomePage.postMessage(false, mrefer);
					if (Tabbars.getInstance() != null) {
						Tabbars tabbar = Tabbars.getInstance();
						Tabbars.getInstance().doNotFinishAllActivities = true;
						tabbar.pageDestinationAfterFbLogin = pageDestination;
						tabbar.postMessage(false, mrefer);
					}
				} else {
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
				}

			}
		});

		mHomePage.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
				// && filterLongEnoughPhone(phoneEdit)
				// && filterLongEnough(firstName)
				// && filterLongEnough(lastName)
				if (filterLongEnough(firstNameEdit)
						&& filterLongEnough(lastNameEdit)
						&& filterLongEnough(zipCodeEdit)
						&& filterLongEnough(editTampDob)) {
					try {
						signupBtn
								.setBackgroundResource(R.drawable.signup_btn_facebook);
						signupBtn.setEnabled(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					signupBtn
							.setBackgroundResource(R.drawable.signup_btn_facebook);
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

	// private void SetTextWatcherForAmountEditView(final EditText
	// amountEditText) {
	// TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
	// @Override
	// public void afterTextChanged(Editable s) {
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// }
	//
	// @Override
	// public void onTextChanged(CharSequence s, int start, int before,
	// int count) {
	// if (filterLongEnoughPhone(phoneEdit)) {
	// try {
	// signupBtn
	// .setBackgroundResource(R.drawable.fb_signin_btn);
	// signupBtn.setEnabled(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// } else {
	// // signupBtn
	// // .setBackgroundResource(R.drawable.fb_signup_btn_idle);
	// signupBtn.setEnabled(false);
	// }
	// }
	//
	// private boolean filterLongEnoughPhone(EditText amountEditText) {
	// return amountEditText.getText().toString().trim().length() >= 12;
	// }
	// };
	// amountEditText.addTextChangedListener(fieldValidatorTextWatcher);
	// }
}
