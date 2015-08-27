package com.ak.app.roti.info;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.ak.app.cb.activity.RotiHomeActivity;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class ForgetPassword {
	private static ForgetPassword screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	// private Tabbars mHomePage;
	private Activity mHomePage;

	// private ProgressBar progressBar;

	public static ForgetPassword getInstance() {
		if (screen == null)
			screen = new ForgetPassword();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.info_forgetpassword, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	String mTabName;
	private ImageView resetpasswordBtn;

	public void onCreate(final boolean b, final String tabName) {
		mTabName = tabName;
		mHomePage = Tabbars.getInstance();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Forgot Password").build());

		TextView pageTitle = (TextView) mParentLayout
				.findViewById(R.id.pageTitle);
		// progressBar = (ProgressBar) mParentLayout
		// .findViewById(R.id.pageLoadingIndicator);

		if (mHomePage == null)
			mHomePage = RotiHomeActivity.getInstance();
		final EditText emailEdit = (EditText) mParentLayout
				.findViewById(R.id.forgetpassword_edit_email);
		resetpasswordBtn = (ImageView) mParentLayout
				.findViewById(R.id.forgetpassword_image_resetpassword);
		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());

		AppConstants.fontDinLightTextView(pageTitle, 26,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
		AppConstants.gothamNarrowBookTextView(emailEdit, 14,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());

		SetTextWatcherForAmountEditView(emailEdit);

		AppConstants.setViewEndable(resetpasswordBtn, false);

		resetpasswordBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) mHomePage
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(emailEdit.getWindowToken(), 0);

				String email = emailEdit.getText().toString();
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					String[] param = new String[] { email };
					new submitForgetPasswordToServer().execute(param);
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
				if (filterLongEnough(amountEditText)
						&& AppConstants.isEmailValid(amountEditText.getText().toString().trim()) ) {
					try {
						AppConstants.setViewEndable(resetpasswordBtn, true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					AppConstants.setViewEndable(resetpasswordBtn, false);
				}
			}

			private boolean filterLongEnough(EditText amountEditText) {
				return amountEditText.getText().toString().length() > 0;
			}

		};
		amountEditText.addTextChangedListener(fieldValidatorTextWatcher);
	}

	private class submitForgetPasswordToServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (Tabbars.getInstance() != null
					&& Tabbars.getInstance().getProgressDialog() != null)
				Tabbars.getInstance().getProgressDialog().show();
			// progressBar.setVisibility(View.VISIBLE);
			else if (RotiHomeActivity.getInstance() != null
					&& RotiHomeActivity.getInstance().getProgressDialog() != null)
				RotiHomeActivity.getInstance().getProgressDialog().show();
			// progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
			nameparams.add(new BasicNameValuePair("email", params[0]));
			nameparams.add(new BasicNameValuePair("register_type",
					AppConstants.REGISTERTYPE));
			nameparams
					.add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
			String result = WebHTTPMethodClass.executeHttPost(
					"/user/forgot_password", nameparams);// loginService();
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			if (Tabbars.getInstance() != null
					&& Tabbars.getInstance().getProgressDialog() != null)
				Tabbars.getInstance().getProgressDialog().dismiss();
			// progressBar.setVisibility(ProgressBar.GONE);
			else if (RotiHomeActivity.getInstance() != null
					&& RotiHomeActivity.getInstance().getProgressDialog() != null)
				RotiHomeActivity.getInstance().getProgressDialog().dismiss();
			// progressBar.setVisibility(View.GONE);
			AppConstants.parseInput(result, mHomePage);
		}
	}
}
