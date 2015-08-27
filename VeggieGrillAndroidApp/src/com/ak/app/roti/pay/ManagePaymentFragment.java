package com.ak.app.roti.pay;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.R;
import com.ak.app.cb.activity.RotiHomeActivity;
import com.ak.app.cb.activity.Snap;
import com.ak.app.cb.activity.Tabbars;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.CardNumberFilter;
import com.akl.zoes.kitchen.util.CreditCardNumberTextWatcher;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;

public class ManagePaymentFragment {
	private static ManagePaymentFragment screen;
	private static Tabbars mActivity=Tabbars.getInstance();
	private EditText editDate;
	private Spinner spinnerPaymentType;
	private EditText editCardNumber;
	private LayoutInflater mInflater;
	private RelativeLayout rootView;
	
	private EditText editZipCode;
	private EditText tampPaymentType;
	private ImageView btnSave;
	private static LinearLayout layoutManagePayment;

	private int keyDel;
	private String a;

	private DatePicker datePicker;
	// private int year, month, day;
	private int selectedDay, selectedMonth, selectedYear;
	private Integer fYear, fMonth, fDate;

	private String cardNumber, date, zipCode, cVvNumber, spinnerSelectedItem;
	private static AlertDialog.Builder alertDialogBuilder;
	private static Boolean isGetClientToken=true;

	private String paymentTypeId = "";

	static String authToken;

	public ManagePaymentFragment() {
	}

	public ManagePaymentFragment(boolean isGetClientToken) {
		this.isGetClientToken = isGetClientToken;
	}

	public static ManagePaymentFragment getInstance() {
		if (screen == null)
			screen = new ManagePaymentFragment();
		return screen;
	}

	

public RelativeLayout setInflater(LayoutInflater inflater) {
	mInflater = inflater;
	rootView = (RelativeLayout) mInflater.inflate(
			R.layout.fragment_manage_payment, null);
	return rootView;
}

public RelativeLayout getScreenParentLayout() {
	return rootView;
}

	public void oncreate()
	{
	
	//	mActivity = Tabbars.getInstance();
		final Calendar currentDate = Calendar.getInstance();

		authToken = mActivity.getPreference().getString(
				AppConstants.PREFAUTH_TOKEN, "");

	//	mActivity.setPageTitle("MANAGE PAYMENT", "Manage Payment");
		editDate = (EditText) rootView.findViewById(R.id.text_date_picker);
		spinnerPaymentType = (Spinner) rootView
				.findViewById(R.id.spinner_payment_type);
		editCardNumber = (EditText) rootView
				.findViewById(R.id.edit_card_number);
		editZipCode = (EditText) rootView.findViewById(R.id.edit_zip_code);
		// editCvvNumber = (EditText)
		// rootView.findViewById(R.id.edit_cvv_number);
		btnSave = (ImageView) rootView.findViewById(R.id.btn_save);
		layoutManagePayment = (LinearLayout) rootView
				.findViewById(R.id.layout_manage_payment);

		tampPaymentType = (EditText) rootView
				.findViewById(R.id.tamp_payment_type);
		AppConstants.fontDinLightTextView(editCardNumber, 14,
				AppConstants.COLORBLACKRGB, mActivity.getAssets());
		AppConstants.fontDinLightTextView(editDate, 14,
				AppConstants.COLORBLACKRGB, mActivity.getAssets());
	
		AppConstants.fontDinLightTextView(editZipCode, 14,
				AppConstants.COLORBLACKRGB, mActivity.getAssets());
		
		
		TextView topTitle = (TextView) rootView
				.findViewById(R.id.topTitle);
		AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mActivity.getAssets());
		

//		AppConstants.fontDinLightTextView(editZipCode, 26,
//				AppConstants.COLORBLACKRGB, mActivity.getAssets());

		
		//		AppConstants.fontDinLightTextView(editCvvNumber, 26,
//				AppConstants.COLORBLACKRGB, mActivity.getAssets());
//		
		
		//
//		AppConstants.fontrobotoCondensedRegular(editCardNumber, 16,
//				AppConstants.colorBlack, mActivity.getAssets());
//		AppConstants.fontrobotoCondensedRegular(editDate, 16,
//				AppConstants.colorBlack, mActivity.getAssets());
//		AppConstants.fontrobotoCondensedRegular(editZipCode, 16,
//				AppConstants.colorBlack, mActivity.getAssets());
		// AppConstants.fontAmaticBold(editCvvNumber, 20,
		// AppConstants.colorBlack,
		// mActivity.getAssets());

		spinnerPaymentType.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					InputMethodManager inputManager = (InputMethodManager) mActivity.getSystemService(
									Activity.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(
							editCardNumber.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				} catch (Exception e) {
				}
				return false;
			}
		});

		final String[] items = new String[5];
		items[0] = "Visa";
		items[1] = "Master Card";
		items[2] = "Amex";
		items[3] = "Discover";
		items[4] = "PAYMENT TYPE";

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mActivity,
				R.layout.custom_simple_spinner_item, items) {
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);

				if (position == getCount()) {
					((TextView) v.findViewById(android.R.id.text1)).setText("");
					((TextView) v.findViewById(android.R.id.text1))
							.setHint(getItem(getCount())); // "Hint to be displayed"
				}
				
				AppConstants.fontDinLightTextView(((TextView) v), 14,
						AppConstants.COLORBLACKRGB, mActivity.getAssets());
				
				

//				Typeface externalFont = Typeface.createFromAsset(
//						mActivity.getAssets(), "RobotoCondensed-Regular.ttf");
//				((TextView) v).setTypeface(externalFont);
//				((TextView) v).setTextColor(Color.parseColor("#231f20"));
//				((TextView) v).setTextSize(16);

				return v;
			}

			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				View v = super.getDropDownView(position, convertView, parent);

				return v;
			}

			@Override
			public int getCount() {
				return super.getCount() - 1; // you dont display
												// last item. It
												// is used as
												// hint.
			}
		};
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPaymentType.setAdapter(dataAdapter);
		spinnerPaymentType.setSelection(dataAdapter.getCount());

		spinnerPaymentType
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView adapter, View v,
							int i, long lng) {
						if (!items[i].equals("Payment type")) {
							paymentTypeId = getPostValString(items[i]);
						} else {
							paymentTypeId = "";
						}
						tampPaymentType.setText(paymentTypeId);

					}

					@Override
					public void onNothingSelected(AdapterView<?> parentView) {

					}
				});

		TextWatcher tw = new TextWatcher() {

			private String current = "";
			private String mmyyyy = "MMYYYY";
			private Calendar cal = Calendar.getInstance();

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!s.toString().equals(current)) {
					String clean = s.toString().replaceAll("[^\\d.]", "");
					String cleanC = current.replaceAll("[^\\d.]", "");

					int cl = clean.length();
					int sel = cl;
					for (int i = 2; i <= cl && i < 6; i += 2) {
						sel++;
					}
					// Fix for pressing delete next to a forward slash
					if (clean.equals(cleanC))
						sel--;

					if (clean.length() < 6) {
						clean = clean + mmyyyy.substring(clean.length());
					} else {
						int mon = Integer.parseInt(clean.substring(0, 2));
						int year = Integer.parseInt(clean.substring(2, 6));

						if (mon > 12)
							mon = 12;
						cal.set(Calendar.MONTH, mon - 1);
						// day = (day > cal.getActualMaximum(Calendar.DATE))?
						// cal.getActualMaximum(Calendar.DATE):day;
						year = (year < currentDate.get(Calendar.YEAR)) ? currentDate
								.get(Calendar.YEAR) : (year > 2100) ? 2100
								: year;
						clean = String.format("%02d%02d", mon, year);

					}

					clean = String.format("%s/%s", clean.substring(0, 2),
							clean.substring(2, 6));

					sel = sel < 0 ? 0 : sel;
					current = clean;
					editDate.setText(current);
					editDate.setSelection(sel < current.length() ? sel
							: current.length());
				}

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		};

		editCardNumber
				.addTextChangedListener(new CreditCardNumberTextWatcher());
		editCardNumber.setFilters(new InputFilter[] { mActivity.spaceFilter,
				new CardNumberFilter(), new InputFilter.LengthFilter(19) });
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					InputMethodManager inputManager = (InputMethodManager) mActivity.getSystemService(
									Activity.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(
							editCardNumber.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				} catch (Exception e) {
				}

				cardNumber = editCardNumber.getText().toString();
				date = editDate.getText().toString();
				zipCode = editZipCode.getText().toString();
				// cVvNumber = editCvvNumber.getText().toString();
				spinnerSelectedItem = getPostValString(spinnerPaymentType
						.getSelectedItem().toString());
				cardNumber = cardNumber.replace("-", "");

				if (AppConstants.isNetworkAvailable(mActivity)) {
					String[] param = new String[] { cardNumber, date, zipCode,
							spinnerSelectedItem };
					new getSettingCreditCard().execute(param);
				} else {
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mActivity);
				}

			}
		});

		editDate.addTextChangedListener(tw);
		SetTextWatcherForAmountEditView(editCardNumber);
		SetTextWatcherForAmountEditView(editZipCode);
		SetTextWatcherForAmountEditView(editDate);
		SetTextWatcherForAmountEditView(tampPaymentType);
		btnSave.setImageResource(R.drawable.managepayment_btn_save);
		btnSave.setEnabled(false);

		if (isGetClientToken) {
			layoutManagePayment.setVisibility(View.GONE);
			new getClientToken().execute("");
		} else {
			layoutManagePayment.setVisibility(View.VISIBLE);
		}
			
	
	
	}

	private String getPostValString(String paymentType) {
		String result = null;
		if ("Visa".equals(paymentType))
			result = "7";
		else if ("Master Card".equals(paymentType))
			result = "6";
		else if ("Amex".equals(paymentType))
			result = "0";
		else if ("Discover".equals(paymentType))
			result = "3";
		return result;
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

				if (editCardNumber.length() == 19
						&& filterLongEnough(editZipCode)
						&& filterLongEnough(editDate)
						&& filterLongEnough(tampPaymentType)) {

					try {
						btnSave.setImageResource(R.drawable.managepayment_btn_save);
						btnSave.setEnabled(true);
					} catch (Exception e) {
						e.printStackTrace();

					}
				} else {

					btnSave.setImageResource(R.drawable.managepayment_btn_save);
					btnSave.setEnabled(false);
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
		return new DateFormatSymbols().getShortMonths()[month - 1];
	}

	private class getSettingCreditCard extends AsyncTask<String, Void, String> {

		private String[] paymentDetails;

		@Override
		protected void onPreExecute() {
			if (mActivity != null && mActivity.getProgressDialog() != null
					&& !mActivity.getProgressDialog().isShowing())
				mActivity.getProgressDialog().show();
		}

		@Override
		protected String doInBackground(String... params) {
			paymentDetails = params;
			String result = WebHTTPMethodClass.httpGetServiceNCR(
					"/cloudconnect/setting", "appkey=" + AppConstants.APPKEY
							+ "&auth_token=" + authToken);
			return result;

		}

		@Override
		protected void onPostExecute(String result) {

			if (AppConstants.ERROR401.equalsIgnoreCase("401")) {
				if (mActivity != null && mActivity.getProgressDialog() != null
						&& mActivity.getProgressDialog().isShowing())
					mActivity.getProgressDialog().dismiss();
				new AppConstants.logoutAccount().execute("");
		RotiHomeActivity.getInstance().showHomePage(false);
				//	MainActivity.getInstance().setDisplayView(0);
			} else {
				if (result != null && !result.equals("")) {
					try {
						JSONObject resObject = new JSONObject(result);
						String sucess = resObject.getString("status");

						if (sucess.equalsIgnoreCase("true")) {
							String username = resObject.getString("username");
							String password = resObject.getString("password");
							String useBypassCredentials = resObject
									.getString("use_bypass_credentials");

							String api_root = null;
							if (useBypassCredentials.equals("true"))
								api_root = resObject
										.getString("bypass_aoo_customer_root");
							else
								api_root = resObject
										.getString("aoo_customer_root");

							String company_code = resObject
									.getString("company_code");
							String[] params = new String[] { paymentDetails[0],
									paymentDetails[1], paymentDetails[2],
									paymentDetails[3], username, password,
									api_root, company_code };
							new postPaymentDetail().execute(params);

						} else {
							if (mActivity != null
									&& mActivity.getProgressDialog() != null
									&& mActivity.getProgressDialog()
											.isShowing())
								mActivity.getProgressDialog().dismiss();
							AppConstants.parseInput(result, mActivity);
						}

					} catch (Exception e) {
						e.printStackTrace();
						AppConstants.parseInput(result, mActivity);
					}
				}
			}
		}
	}

	private class postPaymentDetail extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (mActivity != null && mActivity.getProgressDialog() != null
					&& !mActivity.getProgressDialog().isShowing())
				mActivity.getProgressDialog().show();

		}

		@Override
		protected String doInBackground(String... params) {
			String customerId = mActivity.getPreference().getString(
					AppConstants.CUSTOMER_ID, "");
			String result = WebHTTPMethodClass.callAuthServiceHttpPost(
					customerId, params);

			return result;

		}

		@Override
		protected void onPostExecute(String result) {
			if (mActivity != null && mActivity.getProgressDialog() != null
					&& mActivity.getProgressDialog().isShowing())
				mActivity.getProgressDialog().dismiss();
			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					if (resObject.has("Errors")) {
						JSONArray jsonArray = resObject.getJSONArray("Errors");
						String errorMessage = jsonArray.getJSONObject(0)
								.getString("Message");
						AppConstants.showMsgDialog("",
								"Credit card not created.", mActivity);
						// AppConstants.showMsgDialog("", errorMessage,
						// mActivity);

					} else
						showMsgDialog("",
								"Credit card is successfully created.",
								mActivity);

				} catch (Exception e) {
					e.printStackTrace();
					showMsgDialog("", "Credit card is successfully created.",
							mActivity);
				}
			}

		}
	}

	private static void showMsgDialog(String title, final String message,
			Context context) {
		try {
			if (alertDialogBuilder == null) {
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

										new getClientToken().execute("");
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class getClientToken extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

			if (mActivity != null && mActivity.getProgressDialog() != null
					&& !mActivity.getProgressDialog().isShowing())
				mActivity.getProgressDialog().show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = WebHTTPMethodClass.httpGetServiceNCR(
					"/user/onlineorder/payment_methods", "appkey="
							+ AppConstants.APPKEY + "&auth_token=" + authToken);
			return result;

		}

		@Override
		protected void onPostExecute(String result) {

			if (mActivity != null && mActivity.getProgressDialog() != null
					&& mActivity.getProgressDialog().isShowing())
				mActivity.getProgressDialog().dismiss();

			if (AppConstants.ERROR401.equalsIgnoreCase("401")) {
				new AppConstants.logoutAccount().execute("");
				// Info.getInstance().clearViewStack();
				 //Info.getInstance().showInfoMainPage();
				RotiHomeActivity.getInstance().showHomePage(false);
						} else {
				if (result != null && !result.equals("")) {
					try {
						JSONObject resObject = new JSONObject(result);
						String sucess = resObject.getString("status");
						if (sucess.equalsIgnoreCase("true")) {
							JSONObject responseObject = resObject
									.getJSONObject("response");
							JSONArray jsonArray = responseObject
									.getJSONArray("Payments");
							if (jsonArray.length() > 0) {
								String cardNumber = jsonArray.getJSONObject(0)
										.getString("MaskedAccountNumber");

								String expirationDate = jsonArray
										.getJSONObject(0).getString(
												"ExpirationDate");
								mActivity.maskedAccountNumber = cardNumber;
								mActivity.expiringDateCardNumber = expirationDate;
								
								Snap.getInstance().showcardsummery();
								
//								mActivity.setNextView();
//								mActivity.setDisplayView(15);
//							
							
							} else {
								layoutManagePayment.setVisibility(View.VISIBLE);
							}

						} else
							layoutManagePayment.setVisibility(View.VISIBLE);

					} catch (Exception e) {
						e.printStackTrace();
						AppConstants.showMsgDialog("", e.getMessage(),
								mActivity);
					}
				}
			}
		}
	}

}
