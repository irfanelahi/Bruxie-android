package com.ak.app.roti.info;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
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

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class ChangePasswordPage {
	private static ChangePasswordPage screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;

	// private ProgressBar progressBar;

	public static ChangePasswordPage getInstance() {
		if (screen == null)
			screen = new ChangePasswordPage();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.info_changepassword, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	ImageView logoutBtn;
	private EditText repwdEdit;
	private EditText oldpwdEdit;
	private EditText newpwdEdit;
	private ImageView submitBtn;

	public void onCreate() {
		mHomePage = Tabbars.getInstance();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Reset Password").build());

		// TextView backBtn = (TextView) mParentLayout
		// .findViewById(R.id.changepassword_text_back);
		oldpwdEdit = (EditText) mParentLayout
				.findViewById(R.id.changepassword_edit_oldpassword);
		newpwdEdit = (EditText) mParentLayout
				.findViewById(R.id.changepassword_edit_newpassword);
		repwdEdit = (EditText) mParentLayout
				.findViewById(R.id.changepassword_edit_repeatpassword);
		submitBtn = (ImageView) mParentLayout
				.findViewById(R.id.changepassword_image_submit);
		TextView pageTitle = (TextView) mParentLayout
				.findViewById(R.id.pageTitle);
		// progressBar = (ProgressBar) mParentLayout
		// .findViewById(R.id.greenProgressBar);

		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);

		AppConstants.gothamNarrowBookTextView(oldpwdEdit, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());

		AppConstants.gothamNarrowBookTextView(newpwdEdit, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());

		AppConstants.gothamNarrowBookTextView(repwdEdit, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());

		
		AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());
		
		SetTextWatcherForAmountEditView(oldpwdEdit);
		SetTextWatcherForAmountEditView(newpwdEdit);
		SetTextWatcherForAmountEditView(repwdEdit);
		submitBtn.setEnabled(false);

		submitBtn.setBackgroundResource(R.drawable.forgot_btn_reset);

		oldpwdEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus)
					oldpwdEdit.setBackgroundResource(R.drawable.formfield);
				else if (oldpwdEdit.getText().toString().equals(""))
					oldpwdEdit
							.setBackgroundResource(R.drawable.promo_btn_gray);
			}
		});

		newpwdEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus)
					newpwdEdit.setBackgroundResource(R.drawable.formfield);
				else if (newpwdEdit.getText().toString().equals(""))
					newpwdEdit
							.setBackgroundResource(R.drawable.promo_btn_gray);
			}
		});

		repwdEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus)
					repwdEdit.setBackgroundResource(R.drawable.formfield);
				else if (repwdEdit.getText().toString().equals(""))
					repwdEdit.setBackgroundResource(R.drawable.promo_btn_gray);
			}
		});
		AppConstants.fontDinLightTextView(pageTitle, 26,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());

		AppConstants.gothamNarrowBookTextView(oldpwdEdit, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());
		
		AppConstants.gothamNarrowBookTextView(newpwdEdit, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());
		
		AppConstants.gothamNarrowBookTextView(repwdEdit, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());

		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					InputMethodManager inputManager = (InputMethodManager) mHomePage
							.getSystemService(Activity.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(
							oldpwdEdit.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String oldpwd = oldpwdEdit.getText().toString();
				String newpwd = newpwdEdit.getText().toString();
				String repwd = repwdEdit.getText().toString();
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					String[] param = new String[] { oldpwd, newpwd, repwd };
					if (mHomePage.checkIfLogin()) {
						new submitChangePaswordToServer().execute(param);
					} else {
						Info.getInstance().setNextViewName("ChangePasword");
						Info.getInstance().showLoginOptionPage(false, "INFO");
						// Info.getInstance().showSettingsPage();
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
				if (filterLongEnough(oldpwdEdit)
						&& filterLongEnough(newpwdEdit)
						&& filterLongEnough(repwdEdit)) {
					try {
						submitBtn.setBackgroundResource(R.drawable.submit_btn);
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

	private class submitChangePaswordToServer extends
			AsyncTask<String, Void, String> {

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
			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
			nameparams
					.add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
			nameparams.add(new BasicNameValuePair("auth_token", authToken));
			nameparams
					.add(new BasicNameValuePair("current_password", params[0]));
			nameparams.add(new BasicNameValuePair("password", params[1]));
			nameparams.add(new BasicNameValuePair("password_confirmation",
					params[2]));
			String result = WebHTTPMethodClass.executeHttPost(
					"/user/update_password", nameparams);
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
							&& sucess.equals("true"))
						Info.getInstance().handleBackButton();
				} catch (Exception e) {
					e.printStackTrace();
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORFAILEDAPI, mHomePage);
				}
			} else {
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mHomePage);
			}
			// Info.getInstance().showSettingsPage();
			AppConstants.parseInput(result, mHomePage);
		}
	}
}
