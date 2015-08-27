package com.ak.app.cb.activity;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.rtp.RtpStream;
import android.os.AsyncTask;
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

public class ReferFriend {

	private static ReferFriend screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;
	private String mediaShare;
	public static Boolean firstTimeTwitterLoggedin = false;

	// private ProgressBar pageProgressBar;

	public static ReferFriend getInstance() {
		if (screen == null)
			screen = new ReferFriend();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.refer_friend, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	public void onCreate(final String screen) {
		mHomePage = Tabbars.getInstance();
//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Refer a Friend Page").build());

		ImageView shareFbLayout = (ImageView) mParentLayout
				.findViewById(R.id.referFacebook);
		ImageView shareTwitterLayout = (ImageView) mParentLayout
				.findViewById(R.id.referTweet);
		ImageView shareSocialLayout = (ImageView) mParentLayout
				.findViewById(R.id.referText);
		ImageView shareMailLayout = (ImageView) mParentLayout
				.findViewById(R.id.referEmail);

		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		TextView referFriendText = (TextView) mParentLayout
				.findViewById(R.id.referFriendText);

		TextView referFriendDesc = (TextView) mParentLayout
				.findViewById(R.id.referFriendDesc);
		TextView dotted_line = (TextView) mParentLayout
				.findViewById(R.id.dotted_line);
		

		RelativeLayout backarrow;
		backarrow=(RelativeLayout)mParentLayout.findViewById(R.id.backarrow);
		backarrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(screen.equalsIgnoreCase("roti"))
				{
					
				RotiHomeActivity.getInstance().onBackPressed();
				}
				else
				{
					Info.getInstance().onBackPressed();
							
				}
				
			
			}
		});
		
		

		// pageProgressBar = (ProgressBar) mParentLayout
		// .findViewById(R.id.greenProgressBar);

		AppConstants.gothamNarrowMediumTextView(topTitle, 18, AppConstants.COLORWHITERGB,mHomePage.getAssets());
		
		AppConstants.kingthingsTextView(referFriendText, 45, AppConstants.COLOR_BLACK_PAGETITLE, mHomePage.getAssets());
		AppConstants.gothamNarrowBookTextView(referFriendDesc, 14, AppConstants.COLORRGRAY,mHomePage.getAssets());
	
		AppConstants.gothamNarrowBoldTextView(dotted_line, 14, AppConstants.COLORRGRAY,mHomePage.getAssets());
			
		
		
		shareFbLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (mHomePage.checkIfLogin()) {
						mHomePage.doNotFinishAllActivities = true;
						mediaShare = "facebook";
						// mHomePage.mSimpleFacebook.login(onLoginListener);
					} else {
						RotiHomeActivity.getInstance().oPenTabView(4);
						Info.getInstance().showLoginOptionPage(false, "INFO");

					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);

			}
		});

		shareSocialLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (mHomePage.checkIfLogin()) {
						mHomePage.doNotFinishAllActivities = true;
						mediaShare = "message";
						new fetchReferralRequestServer().execute("");

					} else {
						RotiHomeActivity.getInstance().oPenTabView(4);
						Info.getInstance().showLoginOptionPage(false, "INFO");

					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);

			}
		});

		shareMailLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (mHomePage.checkIfLogin()) {
						mHomePage.doNotFinishAllActivities = true;
						mediaShare = "email";
						new fetchReferralRequestServer().execute("");

					} else {
						RotiHomeActivity.getInstance().oPenTabView(4);
						Info.getInstance().showLoginOptionPage(false, "INFO");

					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);

			}
		});

		shareTwitterLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (mHomePage.checkIfLogin()) {
						mHomePage.doNotFinishAllActivities = true;
						mHomePage.mediaShare = "twitter";
						mHomePage.loginToTwitter(false);

					} else {
						RotiHomeActivity.getInstance().oPenTabView(4);
						Info.getInstance().showLoginOptionPage(false, "INFO");

					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
			}
		});

		if (firstTimeTwitterLoggedin && mHomePage.isTwitterLoggedInAlready()) {
			firstTimeTwitterLoggedin = false;
			mHomePage.mediaShare = "twitter";
			mHomePage.new fetchReferralRequestServer().execute("");
		}

	}

	public class fetchReferralRequestServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().show();
			// pageProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			String result = WebHTTPMethodClass.httpGetService(
					"/referral/email", "auth_token=" + authToken + "&appkey="
							+ AppConstants.APPKEY);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().dismiss();
			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					if (sucess != null && !sucess.equals("")
							&& sucess.equals("true")) {
						try {
							if (resObject.has("email_title")) {
								if (mediaShare == "facebook")
									postToWall(resObject
											.getString("facebook_text"));
								else if (mediaShare == "email") {
									try {
										mHomePage.doNotFinishAllActivities = true;
										Intent intent = new Intent(
												Intent.ACTION_SEND);
										// Uri data = Uri
										// .parse("mailto:?subject="
										// + resObject
										// .getString("email_title")
										// + "&body="
										// + resObject
										// .getString("email_body"));
										// intent.setData(data);
										intent.setType("message/rfc822");

										intent.putExtra(
												android.content.Intent.EXTRA_SUBJECT,
												resObject
														.getString("email_title"));

										intent.putExtra(
												android.content.Intent.EXTRA_TEXT,
												resObject
														.getString("email_body"));
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
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORFAILEDAPI, mHomePage);
					e.printStackTrace();
				}
			} else
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mHomePage);
			// mHomePage.getProgressDialog().hide();
			// pageProgressBar.setVisibility(View.GONE);
		}
	}

	static AlertDialog.Builder alertDialogBuilder;

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

	private void postToWall(String text) {

		/*
		 * You can override other methods here: onThinking(), onFail(String
		 * reason), onException(Throwable throwable)
		 */

		// custom dialog
		final Dialog dialog = new Dialog(mHomePage);
		dialog.setTitle("Post to Your Wall");
		// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.facebook_dialog);

		Button dialogButton = (Button) dialog
				.findViewById(R.id.facebook_but_publish);
		Button facebook_but_skip = (Button) dialog
				.findViewById(R.id.facebook_but_skip);
		final EditText facebook_edit_message = (EditText) dialog
				.findViewById(R.id.facebook_edit_message);
		facebook_edit_message.setText(text);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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

}
