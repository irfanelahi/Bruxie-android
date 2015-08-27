package com.ak.app.roti.info;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.Rewards;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class PromoPage {
	private static PromoPage screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;
	public boolean isPromoPage = false;

	// private ProgressBar pageProgressBar;

	private class URLSpanNoUnderline extends URLSpan {
		public URLSpanNoUnderline(String url) {
			super(url);
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
			ds.setUnderlineText(false);
		}
	}

	public static PromoPage getInstance() {
		if (screen == null)
			screen = new PromoPage();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.info_promocode, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	ImageView logoutBtn;
	private ImageView submitBtn;

	public void onCreate(final String screen) {
		mHomePage = Tabbars.getInstance();
//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Promo Page").build());
		final EditText promocodeEdit = (EditText) mParentLayout
				.findViewById(R.id.promocode_edit_promo);
		submitBtn = (ImageView) mParentLayout
				.findViewById(R.id.promocode_image_submit);

		TextView pageTitle = (TextView) mParentLayout
				.findViewById(R.id.pageTitle);
		TextView pageTitle2 = (TextView) mParentLayout
				.findViewById(R.id.pageTitle2);
		
		
		TextView firstDescText = (TextView) mParentLayout
				.findViewById(R.id.promocode_edit_first_text);
		TextView secondDescText = (TextView) mParentLayout
				.findViewById(R.id.promocode_edit_second_text);
		TextView contactText = (TextView) mParentLayout
				.findViewById(R.id.promocode_edit_contactus);
		// pageProgressBar = (ProgressBar) mParentLayout
		// .findViewById(R.id.greenProgressBar);

		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());
		AppConstants.kingthingsTextView(pageTitle, 23, AppConstants.COLOR_BLACK_PAGETITLE, mHomePage.getAssets());
		AppConstants.kingthingsTextView(pageTitle2, 23, AppConstants.COLOR_BLACK_PAGETITLE, mHomePage.getAssets());
			
//		AppConstants.americanTypewriterTextView(pageTitle, 16,
//				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
//		
		AppConstants.gothamNarrowBookTextView(firstDescText, 14, AppConstants.COLOR_LIGHT_GRAY_TEXT,mHomePage.getAssets());
		
		AppConstants.gothamNarrowBookTextView(secondDescText, 14, AppConstants.COLOR_LIGHT_GRAY_TEXT,mHomePage.getAssets());
		AppConstants.gothamNarrowBookTextView(contactText, 14, AppConstants.COLOR_LIGHT_GRAY_TEXT,mHomePage.getAssets());
			
	
		RelativeLayout backarrow;
		backarrow=(RelativeLayout)mParentLayout.findViewById(R.id.backarrow);
		backarrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			if(screen.equalsIgnoreCase("reward"))
			{
				Rewards.getInstance().onBackPressed();
				
			}
			else
			{
				
				Info.getInstance().onBackPressed();
			}
			
			}
		});
		
		
		
		TextView contactLinkText = (TextView) mParentLayout
				.findViewById(R.id.promocode_edit_contactus_link);
	
		AppConstants.gothamNarrowBookTextView(contactLinkText, 14, AppConstants.COLORGREENCORNERBAKERY,mHomePage.getAssets());
		
		
		promocodeEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus)
					promocodeEdit.setBackgroundResource(R.drawable.formfield);
				else if (promocodeEdit.getText().toString().equals(""))
					promocodeEdit
							.setBackgroundResource(R.drawable.promo_btn_gray);
			}
		});

		SetTextWatcherForAmountEditView(promocodeEdit);
		submitBtn.setEnabled(false);

		submitBtn.setBackgroundResource(R.drawable.submit_btn_inactive);
		AppConstants.gothamNarrowBookTextView(promocodeEdit, 14, AppConstants.COLOR_LIGHT_GRAY_TEXT,mHomePage.getAssets());
		
		
		contactLinkText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mHomePage.doNotFinishAllActivities = true;
				String to = AppConstants.EMAILFAQ_CONTACT_US;
				String subject = "Corner Bakery Cafe query - Promo code assistance";
				String message = getDeviceName();

				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
				// email.putExtra(Intent.EXTRA_CC, new String[]{ to});
				// email.putExtra(Intent.EXTRA_BCC, new String[]{to});
				email.putExtra(Intent.EXTRA_SUBJECT, subject);
				email.putExtra(Intent.EXTRA_TEXT, message);

				// need this to prompts email client only
				email.setType("message/rfc822");

				// mHomePage.startActivity(Intent.createChooser(email,
				// "Choose an Email client :"));
				mHomePage.startActivity(email);

			}
		});

		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					InputMethodManager inputManager = (InputMethodManager) mHomePage
							.getSystemService(Activity.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(
							promocodeEdit.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				} catch (Exception e) {
				}
				String promocode = promocodeEdit.getText().toString();
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (mHomePage.checkIfLogin()) {
						new submitPromoCodeToServer().execute(promocode);
					} else {
						Info.getInstance().setNextViewName("showPromoPage");
						Info.getInstance().showLoginOptionPage(false, "INFO");
					}
				} else {
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
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
				if (filterLongEnough(amountEditText)) {
					try {
						submitBtn.setBackgroundResource(R.drawable.promo_btn_submit);
						submitBtn.setEnabled(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					submitBtn
							.setBackgroundResource(R.drawable.submit_btn_inactive);
					submitBtn.setEnabled(false);
				}
			}

			private boolean filterLongEnough(EditText amountEditText) {
				return amountEditText.getText().toString().length() > 0;
			}

		};
		amountEditText.addTextChangedListener(fieldValidatorTextWatcher);
	}

	public void showBackConfirmMsgDialog(String title, final String message,
			final String promocode) {
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
										if (mHomePage.checkIfLogin()) {
											new submitPromoCodeToServer()
													.execute(promocode);
										} else {
											Info.getInstance()
													.showLoginOptionPage(false,
															"INFO");
										}
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

	private class submitPromoCodeToServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().show();

		}

		@Override
		protected String doInBackground(String... params) {
			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
			nameparams
					.add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
			nameparams.add(new BasicNameValuePair("auth_token", authToken));
			nameparams.add(new BasicNameValuePair("code", params[0]));
			nameparams.add(new BasicNameValuePair("force", "true"));
			String result = WebHTTPMethodClass.executeHttPost("/promocode",
					nameparams);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().dismiss();
			// pageProgressBar.setVisibility(View.GONE);
			AppConstants.parseInput(result, mHomePage);
			isPromoPage = true;
			// Info.getInstance().handleBackButton();
		}
	}

	public String getDeviceName() {
		String texts = "\n\n";

		try {
			PackageInfo pInfo;
			pInfo = mHomePage.getPackageManager().getPackageInfo(
					mHomePage.getPackageName(), 0);
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
			String email = mHomePage.getPreference().getString(
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
}
