package com.akl.zoes.kitchen.util;
//package com.akl.zoes.kitchen.infopage;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONObject;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.os.AsyncTask;
//import android.preference.PreferenceManager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.akl.zoes.kitchen.activity.HomePage;
//import com.akl.zoes.kitchen.activity.R;
//import com.akl.zoes.kitchen.activity.Tutorial;
//import com.akl.zoes.kitchen.util.AppConstants;
//import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//
//public class Info {
//	private static Info screen;
//	private LinearLayout mParentLayout;
//	private LayoutInflater mInflater;
//	private HomePage mHomePage;
//
//	public static Info getInstance() {
//		if (screen == null)
//			screen = new Info();
//		return screen;
//	}
//
//	public LinearLayout setInflater(LayoutInflater inflater) {
//		mInflater = inflater;
//		mParentLayout = (LinearLayout) mInflater.inflate(
//				R.layout.infopage, null);
//		return mParentLayout;
//	}
//
//	public LinearLayout getScreenParentLayout() {
//		return mParentLayout;
//	}
//
//	ImageView logoutBtn;
//
//	public void onCreate() {
////		mParentLayout.removeAllViews();
//		mHomePage = HomePage.getInstance();
////		LinearLayout loginoptionPageMainLayout = (LinearLayout) mInflater
////				.inflate(R.layout.infopage, null);
//		ImageView crossBtn = (ImageView) mParentLayout
//				.findViewById(R.id.infopage_image_cross);
//		ImageView lifeChallangeBtn = (ImageView) mParentLayout
//				.findViewById(R.id.infopage_image_lifechallange);
//		ImageView faqBtn = (ImageView) mParentLayout
//				.findViewById(R.id.infopage_image_faq);
//		ImageView promocodeBtn = (ImageView) mParentLayout
//				.findViewById(R.id.infopage_image_promocode);
//		ImageView changePwdBtn = (ImageView) mParentLayout
//				.findViewById(R.id.infopage_image_changepwd);
//		ImageView coantactusBtn = (ImageView) mParentLayout
//				.findViewById(R.id.infopage_image_contactus);
//		logoutBtn = (ImageView) mParentLayout
//				.findViewById(R.id.infopage_image_logout);
//		TextView copyrightText = (TextView) mParentLayout
//				.findViewById(R.id.infopage_text_copyright);
//		copyrightText.setText("Copyright Zoes Kitchen 2012");
//		AppConstants.fontHelveticaNeueItalicTextView(copyrightText, 13,
//				AppConstants.colorDarkGrayrgb, mHomePage.getAssets());
//		crossBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mHomePage.handleBackButton();
//			}
//		});
//		lifeChallangeBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent i = new Intent(mHomePage, Tutorial.class);
//				i.putExtra("packageName.nearby.push.message.classname",
//						"C2DMRECEIVER");
//				mHomePage.startActivity(i);
//			}
//		});
//		faqBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mHomePage.showFAQPage();
//			}
//		});
//		promocodeBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (AppConstants.isNetworkAvailable(mHomePage)) {
//					if (HomePage.getInstance() != null
//							&& HomePage.getInstance().checkIfLogin()) {
//						mHomePage.showPromoPage();
//					} else {
//						mHomePage.showLoginPage();
//					}
//				} else {
//					showMsgDialog("Alert", "Please find a network connection");
//				}
//			}
//		});
//		changePwdBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mHomePage.showChangePasswordPage();
//			}
//		});
//		coantactusBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				onContactUsCreate();
//			}
//		});
//
//		SharedPreferences mPreference = PreferenceManager
//				.getDefaultSharedPreferences(mHomePage);
//		boolean logoutStatus = mPreference.getBoolean(
//				AppConstants.PREFLOGOUTBUTTONTAG, false);
//		if (logoutStatus) {
//			logoutBtn.setBackgroundResource(R.drawable.info_login_btn);
//		} else {
//			logoutBtn.setBackgroundResource(R.drawable.info_logout_btn);
//		}
//		logoutBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (AppConstants.isNetworkAvailable(mHomePage)) {
//					if (HomePage.getInstance() != null
//							&& HomePage.getInstance().checkIfLogin()) {
//						SharedPreferences mPreference = PreferenceManager
//								.getDefaultSharedPreferences(mHomePage);
//						boolean logoutStatus = mPreference.getBoolean(
//								AppConstants.PREFLOGOUTBUTTONTAG, false);
//						if (logoutStatus) {
//							mHomePage.showLoginOptionPage();
//						} else {
//							new submitLogoutToServer().execute("");
//						}
//					} else {
//						mHomePage.showLoginOptionPage();
//					}
//				} else {
//					showMsgDialog("Alert", "Please find a network connection");
//				}
//			}
//		});
////		mParentLayout.addView(loginoptionPageMainLayout);
//	}
//
//	protected void onContactUsCreate() {
//		final Intent emailIntent = new Intent(
//				android.content.Intent.ACTION_SEND);
//		emailIntent.setType("plain/text");
//		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
//				new String[] { AppConstants.EMAILCONTACT_US });
//		// String subject = AppConstants.EMAILSUBJECT;
//		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
//				AppConstants.EMAILSUBJECT);
//		mHomePage.startActivity(Intent.createChooser(emailIntent, "Email"));
//	}
//
////	public void onFAQCreate() {
////		mParentLayout.removeAllViews();
////		LinearLayout loginoptionPageMainLayout = (LinearLayout) mInflater
////				.inflate(R.layout.faq, null);
////		TextView q1Text = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.faq_text_q1);
////		TextView q2Text = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.faq_text_q2);
////		TextView q3Text = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.faq_text_q3);
////		TextView q4Text = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.faq_text_q4);
////		TextView q5Text = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.faq_text_q5);
////		TextView q6Text = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.faq_text_q6);
////		TextView a1Text = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.faq_text_a1);
////		TextView a2Text = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.faq_text_a2);
////		TextView a3Text = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.faq_text_a3);
////		TextView a4Text = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.faq_text_a4);
////		TextView a5Text = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.faq_text_a5);
////		TextView a6Text = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.faq_text_a6);
////
////		AppConstants.fontVerdanaTextView(q1Text, 15,
////				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
////		AppConstants.fontVerdanaTextView(a1Text, 15,
////				AppConstants.colorDarkGrayrgb, mHomePage.getAssets());
////
////		AppConstants.fontVerdanaTextView(q2Text, 15,
////				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
////		AppConstants.fontVerdanaTextView(a2Text, 15,
////				AppConstants.colorDarkGrayrgb, mHomePage.getAssets());
////
////		AppConstants.fontVerdanaTextView(q3Text, 15,
////				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
////		AppConstants.fontVerdanaTextView(a3Text, 15,
////				AppConstants.colorDarkGrayrgb, mHomePage.getAssets());
////
////		AppConstants.fontVerdanaTextView(q4Text, 15,
////				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
////		AppConstants.fontVerdanaTextView(a4Text, 15,
////				AppConstants.colorDarkGrayrgb, mHomePage.getAssets());
////
////		AppConstants.fontVerdanaTextView(q5Text, 15,
////				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
////		AppConstants.fontVerdanaTextView(a5Text, 15,
////				AppConstants.colorDarkGrayrgb, mHomePage.getAssets());
////
////		AppConstants.fontVerdanaTextView(q6Text, 15,
////				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
////		AppConstants.fontVerdanaTextView(a6Text, 15,
////				AppConstants.colorDarkGrayrgb, mHomePage.getAssets());
////
////		mParentLayout.addView(loginoptionPageMainLayout);
////	}
////
////	public void onPromoCodeCreate() {
////		mParentLayout.removeAllViews();
////		LinearLayout loginoptionPageMainLayout = (LinearLayout) mInflater
////				.inflate(R.layout.info_promocode, null);
////		final EditText promocodeEdit = (EditText) loginoptionPageMainLayout
////				.findViewById(R.id.promocode_edit_promo);
////		ImageView submitBtn = (ImageView) loginoptionPageMainLayout
////				.findViewById(R.id.promocode_image_submit);
////		TextView titleText = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.promocode_edit_title);
////
////		AppConstants.fontVerdanaItalicTextView(titleText, 17,
////				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
////
////		submitBtn.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				String email = promocodeEdit.getText().toString();
////				if (AppConstants.isNetworkAvailable(mHomePage)) {
////					String[] param = new String[] { email };
////					new submitPromoCodeToServer().execute(param);
////				} else {
////					showMsgDialog("Alert", "Please find a network connection");
////				}
////			}
////		});
////		mParentLayout.addView(loginoptionPageMainLayout);
////	}
////
////	public void onChangePasswordCreate() {
////		mParentLayout.removeAllViews();
////		LinearLayout loginoptionPageMainLayout = (LinearLayout) mInflater
////				.inflate(R.layout.info_changepassword, null);
////		final EditText oldpwdEdit = (EditText) loginoptionPageMainLayout
////				.findViewById(R.id.changepassword_edit_oldpassword);
////		final EditText newpwdEdit = (EditText) loginoptionPageMainLayout
////				.findViewById(R.id.changepassword_edit_newpassword);
////		final EditText repwdEdit = (EditText) loginoptionPageMainLayout
////				.findViewById(R.id.changepassword_edit_repeatpassword);
////		ImageView submitBtn = (ImageView) loginoptionPageMainLayout
////				.findViewById(R.id.changepassword_image_submit);
////		TextView titleText = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.changepassword_text_newpassword);
////		TextView titleText2 = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.changepassword_text_oldpassword);
////
////		AppConstants.fontHelveticaNeueItalicTextView(titleText, 16,
////				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
////
////		AppConstants.fontHelveticaNeueItalicTextView(titleText2, 16,
////				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
////
////		submitBtn.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				String oldpwd = oldpwdEdit.getText().toString();
////				String newpwd = newpwdEdit.getText().toString();
////				String repwd = repwdEdit.getText().toString();
////				if (AppConstants.isNetworkAvailable(mHomePage)) {
////					String[] param = new String[] { oldpwd, newpwd, repwd };
////					new submitChangePaswordToServer().execute(param);
////				} else {
////					showMsgDialog("Alert", "Please find a network connection");
////				}
////			}
////		});
////		mParentLayout.addView(loginoptionPageMainLayout);
////	}
//
//	private class submitPromoCodeToServer extends
//			AsyncTask<String, Void, String> {
//
//		@Override
//		protected void onPreExecute() {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//			String authToken = PreferenceManager.getDefaultSharedPreferences(
//					mHomePage).getString(AppConstants.PREFAUTH_TOKEN, "");
//			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
//			nameparams
//					.add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
//			nameparams.add(new BasicNameValuePair("auth_token", authToken));
//			nameparams.add(new BasicNameValuePair("code", params[0]));
//			nameparams.add(new BasicNameValuePair("force", "true"));
//			String result = WebHTTPMethodClass.executeHttPost("/promocode",
//					nameparams);
//			return result;
//		}
//
//		@Override
//		protected void onProgressUpdate(Void... values) {
//
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().dismiss();
//			parseInput(result);
//		}
//	}
//
//	private class submitChangePaswordToServer extends
//			AsyncTask<String, Void, String> {
//
//		@Override
//		protected void onPreExecute() {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//			String authToken = PreferenceManager.getDefaultSharedPreferences(
//					mHomePage).getString(AppConstants.PREFAUTH_TOKEN, "");
//			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
//			nameparams
//					.add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
//			nameparams.add(new BasicNameValuePair("auth_token", authToken));
//			nameparams
//					.add(new BasicNameValuePair("current_password", params[0]));
//			nameparams.add(new BasicNameValuePair("password", params[1]));
//			nameparams.add(new BasicNameValuePair("password_confirmation",
//					params[2]));
//			String result = WebHTTPMethodClass.executeHttPost(
//					"/user/update_password", nameparams);
//			return result;
//		}
//
//		@Override
//		protected void onProgressUpdate(Void... values) {
//
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().dismiss();
//			parseInput(result);
//		}
//	}
//
//	private class submitLogoutToServer extends AsyncTask<String, Void, String> {
//
//		@Override
//		protected void onPreExecute() {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//			String authToken = PreferenceManager.getDefaultSharedPreferences(
//					mHomePage).getString(AppConstants.PREFAUTH_TOKEN, "");
//			String result = WebHTTPMethodClass.httpGetService("/user/logout",
//					"auth_token=" + authToken + "&appkey="
//							+ AppConstants.APPKEY);
//			return result;
//		}
//
//		@Override
//		protected void onProgressUpdate(Void... values) {
//
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().dismiss();
//
//			if (result != null && !result.equals("")) {
//				try {
//					JSONObject resObject = new JSONObject(result);
//					String sucess = resObject.getString("status");
//					if (sucess != null && !sucess.equals("") && sucess.equals("true")) {
//						SharedPreferences mPreference = PreferenceManager
//								.getDefaultSharedPreferences(mHomePage);
//						Editor prefsEditor = mPreference.edit();
//						prefsEditor.putBoolean(AppConstants.PREFLOGOUTBUTTONTAG,
//								true);
//						prefsEditor.commit();
//						boolean logoutStatus = mPreference.getBoolean(
//								AppConstants.PREFLOGOUTBUTTONTAG, false);
//						if (logoutStatus) {
//							logoutBtn.setBackgroundResource(R.drawable.info_login_btn);
//						} else {
//							logoutBtn.setBackgroundResource(R.drawable.info_logout_btn);
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			parseInput(result);
//		}
//	}
//
////	public void onLogInCreate() {
////		mParentLayout.removeAllViews();
////		mHomePage = HomePage.getInstance();
////		LinearLayout loginoptionPageMainLayout = (LinearLayout) mInflater
////				.inflate(R.layout.loginoption, null);
////		ImageView signupFB = (ImageView) loginoptionPageMainLayout
////				.findViewById(R.id.loginoption_image_facebook);
////		ImageView signupEmail = (ImageView) loginoptionPageMainLayout
////				.findViewById(R.id.loginoption_image_signup);
////		ImageView loginBtn = (ImageView) loginoptionPageMainLayout
////				.findViewById(R.id.loginoption_image_login);
////		TextView privacyText = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.loginoption_text_privacy);
////		TextView termsText = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.loginoption_text_terms);
////		TextView returningusersText = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.loginoption_text_returningusers);
////		AppConstants.fontVerdanaItalicTextView(returningusersText, 16,
////				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
////		AppConstants.fontVerdanaTextView(privacyText, 12,
////				AppConstants.colorDarkGrayrgb, mHomePage.getAssets());
////		AppConstants.fontVerdanaTextView(termsText, 12,
////				AppConstants.colorDarkGrayrgb, mHomePage.getAssets());
////		signupFB.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////			}
////		});
////		signupEmail.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				onSignUpCreate();
////			}
////		});
////		loginBtn.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				onLoginViewCreate();
////			}
////		});
////		privacyText.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				String URL = "http://frelevant.herokuapp.com/privacy_policy";
////				if (AppConstants.isNetworkAvailable(mHomePage)) {
////					onPrivacyCreate(URL);
////				} else {
////					showMsgDialog("Alert", "Please find a network connection");
////				}
////			}
////		});
////		termsText.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				String URL = "http://frelevant.herokuapp.com/terms_of_use";
////				if (AppConstants.isNetworkAvailable(mHomePage)) {
////					onPrivacyCreate(URL);
////				} else {
////					showMsgDialog("Alert", "Please find a network connection");
////				}
////			}
////		});
////		mParentLayout.addView(loginoptionPageMainLayout);
////	}
//
////	public void onSignUpCreate() {
////		mParentLayout.removeAllViews();
////		LinearLayout loginoptionPageMainLayout = (LinearLayout) mInflater
////				.inflate(R.layout.signupemail, null);
////		final EditText emailEdit = (EditText) loginoptionPageMainLayout
////				.findViewById(R.id.signup_edit_email);
////		final EditText pwdEdit = (EditText) loginoptionPageMainLayout
////				.findViewById(R.id.signup_edit_password);
////		ImageView signupBtn = (ImageView) loginoptionPageMainLayout
////				.findViewById(R.id.signup_image_signup);
////		TextView signupemailText = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.signup_edit_signupemail);
////
////		AppConstants.fontVerdanaItalicTextView(signupemailText, 16,
////				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
////		signupBtn.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				// boolean check = true;
////				String email = emailEdit.getText().toString();
////				String mPassword = pwdEdit.getText().toString();
////				// if (!ValidateInputFields.isFieldEmpty(email)
////				// && !ValidateInputFields.isEmailValid(email)) {
////				// showMsgDialog("Message", "Invalid Email");
////				// check = false;
////				// }
////				// if (check && !ValidateInputFields.isFieldEmpty(mPassword)) {
////				// showMsgDialog("Message", "Invalid password");
////				// check = false;
////				// }
////				// if (check)
////				{
////					if (AppConstants.isNetworkAvailable(mHomePage)) {
////						String[] param = new String[] { email, mPassword };
////						new submitSignUpDetailsToServer().execute(param);
////					} else {
////						showMsgDialog("Alert",
////								"Please find a network connection");
////					}
////				}
////			}
////		});
////		mParentLayout.addView(loginoptionPageMainLayout);
////	}
////
////	public void onLoginViewCreate() {
////		mParentLayout.removeAllViews();
////		LinearLayout loginoptionPageMainLayout = (LinearLayout) mInflater
////				.inflate(R.layout.login, null);
////		final EditText emailEdit = (EditText) loginoptionPageMainLayout
////				.findViewById(R.id.login_edit_email);
////		final EditText pwdEdit = (EditText) loginoptionPageMainLayout
////				.findViewById(R.id.login_edit_password);
////		ImageView loginBtn = (ImageView) loginoptionPageMainLayout
////				.findViewById(R.id.login_image_login);
////		TextView pleaseLoginText = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.login_edit_pleaselogin);
////		TextView forgetPasswordText = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.login_text_forgetpassword);
////
////		AppConstants.fontVerdanaItalicTextView(pleaseLoginText, 16,
////				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
////		AppConstants.fontVerdanaTextView(forgetPasswordText, 16,
////				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
////
////		loginBtn.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				String email = emailEdit.getText().toString();
////				String mPassword = pwdEdit.getText().toString();
////				if (AppConstants.isNetworkAvailable(mHomePage)) {
////					String[] param = new String[] { email, mPassword };
////					new submitLoginDetailsToServer().execute(param);
////				} else {
////					showMsgDialog("Alert", "Please find a network connection");
////				}
////			}
////		});
////
////		forgetPasswordText.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				onForgetPasswordCreate();
////			}
////		});
////		mParentLayout.addView(loginoptionPageMainLayout);
////	}
////
////	public void onForgetPasswordCreate() {
////		mParentLayout.removeAllViews();
////		LinearLayout loginoptionPageMainLayout = (LinearLayout) mInflater
////				.inflate(R.layout.forgetpassword, null);
////		final EditText emailEdit = (EditText) loginoptionPageMainLayout
////				.findViewById(R.id.forgetpassword_edit_email);
////		ImageView resetpasswordBtn = (ImageView) loginoptionPageMainLayout
////				.findViewById(R.id.forgetpassword_image_resetpassword);
////		TextView pleaseenteremailText = (TextView) loginoptionPageMainLayout
////				.findViewById(R.id.forgetpassword_edit_pleaseenteremail);
////
////		AppConstants.fontVerdanaItalicTextView(pleaseenteremailText, 16,
////				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
////
////		resetpasswordBtn.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				String email = emailEdit.getText().toString();
////				if (AppConstants.isNetworkAvailable(mHomePage)) {
////					String[] param = new String[] { email };
////					new submitForgetPasswordToServer().execute(param);
////				} else {
////					showMsgDialog("Alert", "Please find a network connection");
////				}
////			}
////		});
////		mParentLayout.addView(loginoptionPageMainLayout);
////	}
////
////	@SuppressLint("SetJavaScriptEnabled")
////	public void onPrivacyCreate(String uRL) {
////		mParentLayout.removeAllViews();
////		RelativeLayout loginoptionPageMainLayout = (RelativeLayout) mInflater
////				.inflate(R.layout.food_browser, null);
////
////		WebView webView = (WebView) loginoptionPageMainLayout
////				.findViewById(R.id.food_browser_webview);
////		WebSettings settings = webView.getSettings();
////		settings.setJavaScriptEnabled(true);
////
////		if (HomePage.getInstance() != null
////				&& HomePage.getInstance().getProgressDialog() != null
////				&& !HomePage.getInstance().getProgressDialog().isShowing()) {
////			HomePage.getInstance().getProgressDialog().show();
////		}
////
////		webView.setWebViewClient(new WebViewClient() {
////			public boolean shouldOverrideUrlLoading(WebView view, String url) {
////				Log.i("", "Processing webview url click...");
////				view.loadUrl(url);
////				return true;
////			}
////
////			public void onPageFinished(WebView view, String url) {
////				Log.i("", "Finished loading URL: " + url);
////				if (HomePage.getInstance() != null
////						&& HomePage.getInstance().getProgressDialog() != null
////						&& HomePage.getInstance().getProgressDialog()
////								.isShowing()) {
////					HomePage.getInstance().getProgressDialog().dismiss();
////				}
////			}
////
////			public void onReceivedError(WebView view, int errorCode,
////					String description, String failingUrl) {
////				Log.i("", "onReceivedError loading URL: " + "");
////				if (HomePage.getInstance() != null
////						&& HomePage.getInstance().getProgressDialog() != null
////						&& HomePage.getInstance().getProgressDialog()
////								.isShowing()) {
////					HomePage.getInstance().getProgressDialog().dismiss();
////				}
////			}
////		});
////		webView.loadUrl(uRL);
////		ImageView backBtn = (ImageView) loginoptionPageMainLayout
////				.findViewById(R.id.food_browser_back);
////		ImageView forwardBtn = (ImageView) loginoptionPageMainLayout
////				.findViewById(R.id.food_browser_forward);
////		ImageView reloadBtn = (ImageView) loginoptionPageMainLayout
////				.findViewById(R.id.food_browser_reload);
////		backBtn.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////			}
////		});
////		forwardBtn.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////			}
////		});
////		reloadBtn.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////			}
////		});
////		mParentLayout.addView(loginoptionPageMainLayout);
////	}
//
//	private class submitSignUpDetailsToServer extends
//			AsyncTask<String, Void, String> {
//
//		@Override
//		protected void onPreExecute() {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//			String regId = PreferenceManager.getDefaultSharedPreferences(
//					mHomePage).getString(AppConstants.PREFPUSHREGISTRATIONID,
//					""); /* "dafda343gfgs6yr65tgf5656hfghdhggd"; */
//			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
//			nameparams.add(new BasicNameValuePair("email", params[0]));
//			nameparams.add(new BasicNameValuePair("password", params[1]));
//			nameparams.add(new BasicNameValuePair("latitude", mHomePage
//					.getGetLatLongObj().getLatitude() + ""));
//			nameparams.add(new BasicNameValuePair("longitude", mHomePage
//					.getGetLatLongObj().getLongitude() + ""));
//			nameparams.add(new BasicNameValuePair("register_device_type",
//					AppConstants.DEVICE_TYPE));
//			nameparams.add(new BasicNameValuePair("register_type",
//					AppConstants.REGISTERTYPE));
//			nameparams
//					.add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
//			nameparams.add(new BasicNameValuePair("device_id", AppConstants
//					.getDeviceID(mHomePage)));
//			nameparams.add(new BasicNameValuePair("device_token", regId));
//			String result = WebHTTPMethodClass.executeHttPost("/user/signup",
//					nameparams);// loginService();
//			return result;
//		}
//
//		@Override
//		protected void onProgressUpdate(Void... values) {
//
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().dismiss();
//			parseInput(result);
//		}
//	}
//
//	private class submitLoginDetailsToServer extends
//			AsyncTask<String, Void, String> {
//
//		@Override
//		protected void onPreExecute() {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//			String regId = PreferenceManager.getDefaultSharedPreferences(
//					mHomePage).getString(AppConstants.PREFPUSHREGISTRATIONID,
//					""); /* "dafda343gfgs6yr65tgf5656hfghdhggd"; */
//			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
//			nameparams.add(new BasicNameValuePair("email", params[0]));
//			nameparams.add(new BasicNameValuePair("password", params[1]));
//			nameparams.add(new BasicNameValuePair("sign_in_device_type",
//					AppConstants.DEVICE_TYPE));
//			nameparams.add(new BasicNameValuePair("register_type",
//					AppConstants.REGISTERTYPE));
//			nameparams
//					.add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
//			nameparams.add(new BasicNameValuePair("device_id", AppConstants
//					.getDeviceID(mHomePage)));
//			nameparams.add(new BasicNameValuePair("device_token", regId));
//			String result = WebHTTPMethodClass.executeHttPost("/user/login",
//					nameparams);// loginService();
//			return result;
//		}
//
//		@Override
//		protected void onProgressUpdate(Void... values) {
//
//		}
//
//		// {"notice":"Check My Rewards page if you qualify for any rewards","status":true,"auth_token":"YvfbEdzqdT4HiCr7M6B1"}
//		// {"status":true,"auth_token":"65fzbE8Tp5WGmpioX9cM","notice":"Welcome back"}
//		@Override
//		protected void onPostExecute(String result) {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().dismiss();
//
//			if (result != null && !result.equals("")) {
//				try {
//					JSONObject resObject = new JSONObject(result);
//					String sucess = resObject.getString("status");
//					if (sucess != null && !sucess.equals("") && sucess.equals("true")) {
//						SharedPreferences mPreference = PreferenceManager
//								.getDefaultSharedPreferences(mHomePage);
//						Editor prefsEditor = mPreference.edit();
//						prefsEditor.putBoolean(AppConstants.PREFLOGOUTBUTTONTAG,
//								false);
//						prefsEditor.commit();
//						boolean logoutStatus = mPreference.getBoolean(
//								AppConstants.PREFLOGOUTBUTTONTAG, false);
//						if (logoutStatus) {
//							logoutBtn.setImageResource(R.drawable.info_logout_btn);
//						} else {
//							logoutBtn.setImageResource(R.drawable.info_login_btn);
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			parseInput(result);
//		}
//	}
//
//	private class submitForgetPasswordToServer extends
//			AsyncTask<String, Void, String> {
//
//		@Override
//		protected void onPreExecute() {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
//			nameparams.add(new BasicNameValuePair("email", params[0]));
//			nameparams.add(new BasicNameValuePair("register_type",
//					AppConstants.REGISTERTYPE));
//			nameparams
//					.add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
//			String result = WebHTTPMethodClass.executeHttPost(
//					"/user/forgot_password", nameparams);// loginService();
//			return result;
//		}
//
//		// {"status":true,"notice":"Your new password has been emailed to you.
//		// You should receive it in few minutes.","new_pass":"zb47nc"}
//		@Override
//		protected void onProgressUpdate(Void... values) {
//
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().dismiss();
//			parseInput(result);
//		}
//	}
//
//	private void parseInput(String result) {
//		if (result != null && !result.equals("")) {
//			try {
//				JSONObject resObject = new JSONObject(result);
//				String sucess = resObject.getString("status");
//				String notice = resObject.getString("notice");
//
//				String errors = "";
//				String auth_token = "";
//				try {
//					auth_token = resObject.getString("auth_token");
//					SharedPreferences mPreference = PreferenceManager
//							.getDefaultSharedPreferences(mHomePage);
//					Editor prefsEditor = mPreference.edit();
//					prefsEditor.putString(AppConstants.PREFAUTH_TOKEN,
//							auth_token);
//					Log.d("auth_token", auth_token);
//					prefsEditor.commit();
//				} catch (Exception e) {
//				}
//				try {
//					errors = resObject.getString("errors");
//				} catch (Exception e) {
//				}
//				if (sucess != null && (!sucess.equals(""))) {// false
//					AppConstants
//							.showMsgDialog("ZoesKitchen", notice, mHomePage);
//				} else {
//					if (errors != null && (!errors.equals(""))) {
//						AppConstants.showMsgDialog("ZoesKitchen", errors
//								+ notice, mHomePage);
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public void showMsgDialog(String title, final String message) {
//		AlertDialog.Builder alt_bld = new AlertDialog.Builder(mHomePage);
//		alt_bld.setMessage(message).setCancelable(false)
//				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						dialog.cancel();
//					}
//				});
//		AlertDialog alert = alt_bld.create();
//		alert.setTitle(title);
//		alert.setIcon(AlertDialog.BUTTON_NEGATIVE);
//		alert.show();
//	}
//}
