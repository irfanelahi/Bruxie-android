package com.akl.zoes.kitchen.util;
//package com.akl.zoes.kitchen.activity;
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
//import com.akl.zoes.kitchen.util.AppConstants;
//import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//
//public class LoginOptionPage 
//{
//	private static LoginOptionPage screen;
//	private LinearLayout mParentLayout;
//	private LayoutInflater mInflater;
//	private HomePage mHomePage;
//
//	public static LoginOptionPage getInstance() {
//		if (screen == null)
//			screen = new LoginOptionPage();
//		return screen;
//	}
//
//	public LinearLayout setInflater(LayoutInflater inflater) {
//		mInflater = inflater;
//		LinearLayout lifePageMainLayout = (LinearLayout) mInflater.inflate(
//				R.layout.whitebackgroundparentview, null);
//		mParentLayout = (LinearLayout) lifePageMainLayout
//		.findViewById(R.id.lifemainview_linear_parent);
//		return lifePageMainLayout;
////		mParentLayout = (LinearLayout) mInflater.inflate(
////				R.layout.whitebackgroundparentview, null);
////		return mParentLayout;
//	}
//
//	public LinearLayout getScreenParentLayout() {
//		return mParentLayout;
//	}
//
//	public void onCreate() {
//		mParentLayout.removeAllViews();
//		mHomePage = HomePage.getInstance();
//		LinearLayout loginoptionPageMainLayout = (LinearLayout) mInflater
//				.inflate(R.layout.loginoption, null);
//		ImageView signupFB = (ImageView) loginoptionPageMainLayout
//				.findViewById(R.id.loginoption_image_facebook);
//		ImageView signupEmail = (ImageView) loginoptionPageMainLayout
//				.findViewById(R.id.loginoption_image_signup);
//		ImageView loginBtn = (ImageView) loginoptionPageMainLayout
//				.findViewById(R.id.loginoption_image_login);
//		TextView privacyText = (TextView) loginoptionPageMainLayout
//				.findViewById(R.id.loginoption_text_privacy);
//		TextView termsText = (TextView) loginoptionPageMainLayout
//				.findViewById(R.id.loginoption_text_terms);
//		TextView returningusersText = (TextView) loginoptionPageMainLayout
//				.findViewById(R.id.loginoption_text_returningusers);
//		AppConstants.fontVerdanaItalicTextView(returningusersText, 16,
//				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
//		AppConstants.fontVerdanaTextView(privacyText, 12,
//				AppConstants.colorDarkGrayrgb, mHomePage.getAssets());
//		AppConstants.fontVerdanaTextView(termsText, 12,
//				AppConstants.colorDarkGrayrgb, mHomePage.getAssets());
//		signupFB.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//			}
//		});
//		signupEmail.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				onSignUpCreate();
//			}
//		});
//		loginBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				onLoginCreate();
//			}
//		});
//		privacyText.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String URL = "http://frelevant.herokuapp.com/privacy_policy";
//				if (AppConstants.isNetworkAvailable(mHomePage)) {
//					onPrivacyCreate(URL);
//				} else {
//					showMsgDialog("Alert", "Please find a network connection");
//				}
//			}
//		});
//		termsText.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String URL = "http://frelevant.herokuapp.com/terms_of_use";
//				if (AppConstants.isNetworkAvailable(mHomePage)) {
//					onPrivacyCreate(URL);
//				} else {
//					showMsgDialog("Alert", "Please find a network connection");
//				}
//			}
//		});
//		mParentLayout.addView(loginoptionPageMainLayout);
//	}
//
//	public void onSignUpCreate() {
//		mParentLayout.removeAllViews();
//		LinearLayout loginoptionPageMainLayout = (LinearLayout) mInflater
//				.inflate(R.layout.signupemail, null);
//		final EditText emailEdit = (EditText) loginoptionPageMainLayout
//				.findViewById(R.id.signup_edit_email);
//		final EditText pwdEdit = (EditText) loginoptionPageMainLayout
//				.findViewById(R.id.signup_edit_password);
//		ImageView signupBtn = (ImageView) loginoptionPageMainLayout
//				.findViewById(R.id.signup_image_signup);
//		TextView signupemailText = (TextView) loginoptionPageMainLayout
//				.findViewById(R.id.signup_edit_signupemail);
//
//		AppConstants.fontVerdanaItalicTextView(signupemailText, 16,
//				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
//		signupBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// boolean check = true;
//				String email = emailEdit.getText().toString();
//				String mPassword = pwdEdit.getText().toString();
//				// if (!ValidateInputFields.isFieldEmpty(email)
//				// && !ValidateInputFields.isEmailValid(email)) {
//				// showMsgDialog("Message", "Invalid Email");
//				// check = false;
//				// }
//				// if (check && !ValidateInputFields.isFieldEmpty(mPassword)) {
//				// showMsgDialog("Message", "Invalid password");
//				// check = false;
//				// }
//				// if (check)
//				{
//					if (AppConstants.isNetworkAvailable(mHomePage)) {
//						String[] param = new String[] { email, mPassword };
//						new submitSignUpDetailsToServer().execute(param);
//					} else {
//						showMsgDialog("Alert",
//								"Please find a network connection");
//					}
//				}
//			}
//		});
//		mParentLayout.addView(loginoptionPageMainLayout);
//	}
//
//	public void onLoginCreate() {
//		mParentLayout.removeAllViews();
//		LinearLayout loginoptionPageMainLayout = (LinearLayout) mInflater
//				.inflate(R.layout.login, null);
//		final EditText emailEdit = (EditText) loginoptionPageMainLayout
//				.findViewById(R.id.login_edit_email);
//		final EditText pwdEdit = (EditText) loginoptionPageMainLayout
//				.findViewById(R.id.login_edit_password);
//		ImageView loginBtn = (ImageView) loginoptionPageMainLayout
//				.findViewById(R.id.login_image_login);
//		TextView pleaseLoginText = (TextView) loginoptionPageMainLayout
//				.findViewById(R.id.login_edit_pleaselogin);
//		TextView forgetPasswordText = (TextView) loginoptionPageMainLayout
//				.findViewById(R.id.login_text_forgetpassword);
//
//		AppConstants.fontVerdanaItalicTextView(pleaseLoginText, 16,
//				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
//		AppConstants.fontVerdanaTextView(forgetPasswordText, 16,
//				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
//
//		loginBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String email = emailEdit.getText().toString();
//				String mPassword = pwdEdit.getText().toString();
//				if (AppConstants.isNetworkAvailable(mHomePage)) {
//					String[] param = new String[] { email, mPassword };
//					new submitLoginDetailsToServer().execute(param);
//				} else {
//					showMsgDialog("Alert", "Please find a network connection");
//				}
//			}
//		});
//
//		forgetPasswordText.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				onForgetPasswordCreate();
//			}
//		});
//		mParentLayout.addView(loginoptionPageMainLayout);
//	}
//
//	public void onForgetPasswordCreate() {
//		mParentLayout.removeAllViews();
//		LinearLayout loginoptionPageMainLayout = (LinearLayout) mInflater
//				.inflate(R.layout.forgetpassword, null);
//		final EditText emailEdit = (EditText) loginoptionPageMainLayout
//				.findViewById(R.id.forgetpassword_edit_email);
//		ImageView resetpasswordBtn = (ImageView) loginoptionPageMainLayout
//				.findViewById(R.id.forgetpassword_image_resetpassword);
//		TextView pleaseenteremailText = (TextView) loginoptionPageMainLayout
//				.findViewById(R.id.forgetpassword_edit_pleaseenteremail);
//
//		AppConstants.fontVerdanaItalicTextView(pleaseenteremailText, 16,
//				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
//
//		resetpasswordBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String email = emailEdit.getText().toString();
//				if (AppConstants.isNetworkAvailable(mHomePage)) {
//					String[] param = new String[] { email };
//					new submitForgetPasswordToServer().execute(param);
//				} else {
//					showMsgDialog("Alert", "Please find a network connection");
//				}
//			}
//		});
//		mParentLayout.addView(loginoptionPageMainLayout);
//	}
//
//	@SuppressLint("SetJavaScriptEnabled")
//	public void onPrivacyCreate(String uRL) {
//		mParentLayout.removeAllViews();
//		RelativeLayout loginoptionPageMainLayout = (RelativeLayout) mInflater
//				.inflate(R.layout.food_browser, null);
//
//		WebView webView = (WebView) loginoptionPageMainLayout
//				.findViewById(R.id.food_browser_webview);
//		WebSettings settings = webView.getSettings();
//		settings.setJavaScriptEnabled(true);
//		
//        if (HomePage.getInstance() != null && HomePage.getInstance().getProgressDialog() != null &&
//        		!HomePage.getInstance().getProgressDialog().isShowing()) {
//            HomePage.getInstance().getProgressDialog().show();
//        }
//		
//		webView.setWebViewClient(new WebViewClient() {
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.i("", "Processing webview url click...");
//                view.loadUrl(url);
//                return true;
//            }
// 
//            public void onPageFinished(WebView view, String url) {
//                Log.i("", "Finished loading URL: " +url);
//                if (HomePage.getInstance() != null && HomePage.getInstance().getProgressDialog() != null &&
//                		HomePage.getInstance().getProgressDialog().isShowing()) {
//                    HomePage.getInstance().getProgressDialog().dismiss();
//                }
//            }
// 
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Log.i("", "onReceivedError loading URL: " + "");
//                if (HomePage.getInstance() != null && HomePage.getInstance().getProgressDialog() != null &&
//                		HomePage.getInstance().getProgressDialog().isShowing()) {
//                    HomePage.getInstance().getProgressDialog().dismiss();
//                }
//            }
//        });
//		webView.loadUrl(uRL);
//		ImageView backBtn = (ImageView) loginoptionPageMainLayout
//				.findViewById(R.id.food_browser_back);
//		ImageView forwardBtn = (ImageView) loginoptionPageMainLayout
//				.findViewById(R.id.food_browser_forward);
//		ImageView reloadBtn = (ImageView) loginoptionPageMainLayout
//				.findViewById(R.id.food_browser_reload);
//		backBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//			}
//		});
//		forwardBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//			}
//		});
//		reloadBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//			}
//		});
//		mParentLayout.addView(loginoptionPageMainLayout);
//	}
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
//
//}
//
//// [email=ajay6@gmail.com, password=123456, latitude=0.0, longitude=0.0,
//// register_device_type=android, register_type=1,
//// appkey=IJUw7NIUxwHXZFch,
//// device_id=ffffffff-faab-3444-1fca-42de0033c587, device_token=]
//
//// {"notice":"Check My Rewards page if you qualify for any rewards","status":true,"auth_token":"YvfbEdzqdT4HiCr7M6B1"}
//// {"status":true,"notice":"Check My Rewards page if you qualify for any rewards","auth_token":"YvfbEdzqdT4HiCr7M6B1"}
//// {"status":false,"errors":{"email":["is invalid"]},"notice":"Please provide required fields"}
//// {"status":false,"notice":"Email Id already used. Please login"}
//
//// @Override
//// protected void onPostExecute(String result) {
//// if(mHomePage != null && mHomePage.getProgressDialog() != null)
//// mHomePage.getProgressDialog().dismiss();
//// if (result != null && !result.equals("")) {
//// try {
//// JSONObject resObject = new JSONObject(result);
//// String sucess = resObject.getString("Success");
//// if (sucess != null && (!sucess.equals("false")))
//// {
//// // prefsEditor.putString("USERID", resObject.getString("Result"));
//// // prefsEditor.putString("SUCCESS", sucess);
//// // prefsEditor.putString("USERNAME", mUserName.trim());
//// // prefsEditor.putString("PASSWORD", mPassword.trim());
//// // prefsEditor.commit();
//// // Intent loginIntent = new Intent(ClientMainActivity.this,
//// FormScreen.class);
//// // startActivity(loginIntent);
//// // finish();
//// }
//// else
//// {
//// // prefsEditor.putString("USERID", "");
//// // prefsEditor.putString("SUCCESS", sucess+"");
//// // prefsEditor.putString("USERNAME", "");
//// // prefsEditor.putString("PASSWORD", "");
//// // prefsEditor.commit();
//// }
//// // showMsgDialog("Message", message);
//// } catch (Exception e) {
//// e.printStackTrace();
//// }
//// }
//// }