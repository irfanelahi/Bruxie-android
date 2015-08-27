package com.ak.app.cb.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class GetSocial {

	private static GetSocial screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;
	private String mediaShare = "";
	private String mediumShare;
	private static AlertDialog.Builder alertDialogBuilder;
	public static Boolean firstTimeTwitterLoggedin = false;

	private ProgressBar pageProgressBar;

	public static GetSocial getInstance() {
		if (screen == null)
			screen = new GetSocial();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(R.layout.get_social,
				null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	public void onCreate() {
		mHomePage = Tabbars.getInstance();
//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Get Social Page").build());

		TextView pageTitle = (TextView) mParentLayout
				.findViewById(R.id.pageTitle);

		TextView pageDesc = (TextView) mParentLayout
				.findViewById(R.id.pageDesc);

		AppConstants.fontDinLightTextView(pageTitle, 26,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
		// AppConstants.fontDinLightTextView(pageDesc, 18,
		// AppConstants.COLORGRAYNEKTER, mHomePage.getAssets()); by herlangga

		ImageView tweetItBtn = (ImageView) mParentLayout
				.findViewById(R.id.shareTweet);
		ImageView shareFbBtn = (ImageView) mParentLayout
				.findViewById(R.id.shareFacebook);
		pageProgressBar = (ProgressBar) mParentLayout
				.findViewById(R.id.greenProgressBar);

		shareFbBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (mHomePage.checkIfLogin()) {
						mHomePage.doNotFinishAllActivities = true;
						mediaShare = "facebook";
						// mHomePage.mSimpleFacebook.login(onLoginListener);
					} else {

						Info.getInstance().showLoginOptionPage(false, "INFO");

					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);

			}
		});

		tweetItBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (mHomePage.checkIfLogin()) {
						mHomePage.doNotFinishAllActivities = true;
						mHomePage.loginToTwitter(true);
					} else {

						Info.getInstance().showLoginOptionPage(false, "INFO");

					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);

			}
		});

		if (firstTimeTwitterLoggedin && mHomePage.isTwitterLoggedInAlready()) {
			firstTimeTwitterLoggedin = false;
			mHomePage.new fetchStatusShareRequestServer().execute("");
		}

	}

	private class fetchStatusShareRequestServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// if (mHomePage != null && mHomePage.getProgressDialog() != null)
			// mHomePage.getProgressDialog().show();
			pageProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			String result = WebHTTPMethodClass.httpGetService("/social_shares",
					"appkey=" + AppConstants.APPKEY);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			// if (mHomePage != null && mHomePage.getProgressDialog() != null)
			// mHomePage.getProgressDialog().dismiss();
			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					if (sucess != null && !sucess.equals("")
							&& sucess.equals("true")) {
						try {
							if (resObject.has("social_share_program_id")) {
								if (mediaShare == "facebook")
									postToWall(resObject
											.getString("facebook_share_text"));
								else if (mediaShare == "email") {
									try {
										Intent intent = new Intent(
												Intent.ACTION_VIEW);
										Uri data = Uri
												.parse("mailto:?subject="
														+ resObject
																.getString("email_title")
														+ "&body="
														+ resObject
																.getString("email_body"));
										intent.setData(data);
										mHomePage.startActivity(intent);
									} catch (Exception e) {
									}
								} else if (mediaShare == "message") {
									Intent sendIntent = new Intent(
											Intent.ACTION_VIEW);
									sendIntent.putExtra("sms_body", resObject
											.getString("other_media_text"));
									sendIntent
											.setType("vnd.android-dir/mms-sms");
									mHomePage.startActivity(sendIntent);
								}
							}
						} catch (Exception e) {
						}
					} else {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
			}
			pageProgressBar.setVisibility(View.GONE);
			// mHomePage.getProgressDialog().dismiss();
		}
	}

	public void shareToTwitter(String text) {

		// custom dialog
		final Dialog dialog = new Dialog(mHomePage);
		dialog.setTitle("Post to Twitter");
		// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.twitterdialog);

		Button dialogButton = (Button) dialog
				.findViewById(R.id.twitter_but_tweet);
		TextView twitter_cancel = (TextView) dialog
				.findViewById(R.id.twitter_but_back);
		final EditText twitter_edit_message = (EditText) dialog
				.findViewById(R.id.twitter_edit_message);
		twitter_edit_message.setText(text);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// post on user's wall.

				mHomePage.new updateTwitterStatus().execute(
						twitter_edit_message.getText().toString(),
						"social share");
				dialog.dismiss();
			}
		});

		twitter_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});

		dialog.show();

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
				if (filterLongEnough(facebook_edit_message)) {
					try {
						// signupBtn.setBackgroundResource(R.drawable.sign_up_btn);
						dialogShareFBButton.setEnabled(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// signupBtn
					// .setBackgroundResource(R.drawable.sign_up_btn_idle);
					dialogShareFBButton.setEnabled(false);
				}
			}

			private boolean filterLongEnough(EditText amountEditText) {
				return amountEditText.getText().toString().trim().length() > 0;
			}

		};
		amountEditText.addTextChangedListener(fieldValidatorTextWatcher);
	}

	private Button dialogShareFBButton;
	private EditText facebook_edit_message;

	private void postToWall(String text) {

		// custom dialog
		final Dialog dialog = new Dialog(mHomePage);
		dialog.setTitle("Post to Your Wall");
		// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.facebook_dialog);

		dialogShareFBButton = (Button) dialog
				.findViewById(R.id.facebook_but_publish);
		Button facebook_but_skip = (Button) dialog
				.findViewById(R.id.facebook_but_skip);
		facebook_edit_message = (EditText) dialog
				.findViewById(R.id.facebook_edit_message);

		// facebook_edit_message.setText(text);

		SetTextWatcherForAmountEditView(facebook_edit_message);
		dialogShareFBButton.setEnabled(false);

		// if button is clicked, close the custom dialog
		dialogShareFBButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// post on user's wall.

				// Feed feed = new Feed.Builder().setMessage(
				// facebook_edit_message.getText().toString()).build();
				// mHomePage.mSimpleFacebook.publish(feed, onPublishListener);
				dialog.dismiss();

			}
		});

		facebook_but_skip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});

		dialog.show();

	}

	private class requestUserInteractionToServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {

			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			String appKey = AppConstants.APPKEY;

			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
			nameparams.add(new BasicNameValuePair("auth_token", authToken));
			nameparams.add(new BasicNameValuePair("appkey", appKey));
			nameparams.add(new BasicNameValuePair("medium_id", mediumShare));
			String result = WebHTTPMethodClass.executeHttPost(
					"/social_shares/user_interaction", nameparams);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
		}
	}

	public void showShareMsgDialog(String title, final String message,
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

}
