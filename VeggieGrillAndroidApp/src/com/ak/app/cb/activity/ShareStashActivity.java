package com.ak.app.cb.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class ShareStashActivity extends Activity {

	// Your Facebook APP ID
	private static String APP_ID = "382775391756573"; // Replace your App ID
														// here

	// Instance of Facebook Class
	private Facebook facebook;
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;

	static AlertDialog.Builder alertDialogBuilder;
	SharedPreferences mPreference;

	public SharedPreferences getPreference() {
		return mPreference;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_stash);
		mPreference = PreferenceManager.getDefaultSharedPreferences(this);
		Button shareViaFacebook = (Button) findViewById(R.id.shareViaFacebook);
		Button shareViaTwitter = (Button) findViewById(R.id.shareViaTwitter);
		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		shareViaFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (!isAppInstalled("com.facebook.katana"))
				// ShareStashActivity
				// .showMessageDialog("Please install facebook!",
				// ShareStashActivity.this);
				// else {
				loginToFacebook();
				// new fetchReferralRequestServer().execute("");
				// }
			}
		});

		shareViaTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isAppInstalled("com.twitter.android"))
					ShareStashActivity.showMessageDialog(
							"Please install twitter!", ShareStashActivity.this);
				else {

				}
			}
		});
	}

	private void loginToFacebook() {
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(this, new String[] { "read_stream", "email",
					"user_photos", "publish_checkins", "publish_stream",
					"offline_access", "photo_upload" }, new DialogListener() {

				@Override
				public void onCancel() {
					// Function to handle cancel event
				}

				@Override
				public void onComplete(Bundle values) {
					// Function to handle complete event
					// Edit Preferences and update facebook acess_token
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putString("access_token", facebook.getAccessToken());
					editor.putLong("access_expires",
							facebook.getAccessExpires());
					editor.commit();
					postToWall();
				}

				@Override
				public void onError(DialogError error) {
					// Function to handle error

				}

				@Override
				public void onFacebookError(FacebookError fberror) {
					// Function to handle Facebook errors

				}

			});
		} else
			postToWall();
	}

	private void postToWall() {
		Bundle parameters = new Bundle();

		parameters.putString("app_id", APP_ID);
		// parameters.putString("name", "Facebook Dialog");
		// parameters.putString("caption", "Reference Documentation");

		parameters.putString("message", "Facebook Dialogs are easy!");

		facebook.dialog(this, "feed", parameters, new DialogListener() {

			@Override
			public void onComplete(Bundle values) {
			}

			@Override
			public void onFacebookError(FacebookError error) {
			}

			@Override
			public void onError(DialogError e) {
			}

			@Override
			public void onCancel() {
			}
		});

	}

	private boolean isAppInstalled(String uri) {
		PackageManager pm = getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}

	private static void showMessageDialog(String message, Context context) {
		if (alertDialogBuilder == null) {
			alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder
					.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {
									alertDialogBuilder = null;
									dialog.cancel();

								}
							});
			AlertDialog alert = alertDialogBuilder.create();
			alert.show();
		}
	}

	private class fetchReferralRequestServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String authToken = getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			String result = WebHTTPMethodClass.httpGetService(
					"/referral/email", "auth_token=" + authToken + "&appkey="
							+ AppConstants.APPKEY);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		// /referral/email result =
		// {"status":true,"email_title":"join us","email_body":"sign up with this promocode  CGAPYXXL ","referral_code":"CGAPYXXL","start_date":"2012-11-22","end_date":"2013-01-31","referral_program_title":"vfxv","incentive_title":"+30"}

		@Override
		protected void onPostExecute(String result) {

			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					if (sucess != null && !sucess.equals("")
							&& sucess.equals("true")) {
						String email_title = "";
						String email_body = "";
						// String incentive_title = "";
						try {
							if (resObject.has("email_title")) {
								email_title = resObject
										.getString("email_title");
								email_body = resObject.getString("email_body");
								// incentive_title =
								// resObject.getString("incentive_title");
							}
							// final Intent emailIntent = new Intent(
							// android.content.Intent.ACTION_SEND);
							// emailIntent.setType("plain/text");
							// emailIntent.putExtra(
							// android.content.Intent.EXTRA_EMAIL,
							// new String[] { "" });
							// emailIntent.putExtra(
							// android.content.Intent.EXTRA_SUBJECT,
							// email_title);
							// emailIntent.putExtra(
							// android.content.Intent.EXTRA_TEXT,
							// email_body);
							// startActivity(Intent.createChooser(emailIntent,
							// "Email"));

							Intent intent = ShareStashActivity.this
									.getPackageManager()
									.getLaunchIntentForPackage(
											"com.facebook.katana");
							if (intent != null) {
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.putExtra("facebookMessage",
										"is integrating stuff again.");

								startActivity(intent);
							}
						} catch (Exception e) {
						}
						// Editor prefsEditor = mHomePage.getPreferenceEditor();
						// prefsEditor.putBoolean(
						// AppConstants.PREFREFERRAL_CODE, false);
						// prefsEditor.commit();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			AppConstants.parseInput(result, ShareStashActivity.this);
		}
	}

}
