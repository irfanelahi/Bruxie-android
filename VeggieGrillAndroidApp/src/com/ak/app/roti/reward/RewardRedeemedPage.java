package com.ak.app.roti.reward;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.app.cb.activity.Rewards;
import com.ak.app.cb.activity.RotiHomeActivity;
import com.ak.app.cb.activity.SnapInstagramImageActivity;
import com.ak.app.cb.activity.SplashScreen;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.ReferFriend.fetchReferralRequestServer;
import com.ak.app.cb.activity.R;
import com.akl.app.roti.bean.MyGoodieRewardsBean;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.RewardsBean;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class RewardRedeemedPage {
	private static RewardRedeemedPage screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;
	private EditText enterBarcodeEdit;
	private ImageView enterBtn;
	public static Boolean firstTimeTwitterLoggedin = false;
	private String mediaShare;

	private TextView userCode;
	private String rewardCode;
	int b=0;
	int brightness=0;
	
	// instagram property start
	String type = "image/*";
	String filename = "/share67.jpg";
	String mediaPath = Environment.getExternalStorageDirectory() + filename;
	String captionText = "<< media caption >>";

	private void createInstagramIntent(String type, String mediaPath,
			String caption) {
		Log.i("elang", "elang " + mediaPath);
		// Create the new Intent using the 'Send' action.
		Intent share = new Intent(Intent.ACTION_SEND);

		// Set the MIME type
		share.setType(type);

		// Create the URI from the media
		File media = new File(mediaPath);
		Uri uri = Uri.fromFile(media);

		// Add the URI and the caption to the Intent.
		share.putExtra(Intent.EXTRA_STREAM, uri);
		share.putExtra(Intent.EXTRA_TEXT, caption);

		// Broadcast the Intent.
		mHomePage.startActivity(Intent.createChooser(share, "Share to"));
	}

	// instagram property end

	public static RewardRedeemedPage getInstance() {
		if (screen == null)
			screen = new RewardRedeemedPage();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.reward_redeemed, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
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
						// enterBtn.setBackgroundResource(R.drawable.purple_enter_btn);
						enterBtn.setEnabled(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// enterBtn.setBackgroundResource(R.drawable.purple_enter_btn_idle);
					enterBtn.setEnabled(false);
				}
			}

			private boolean filterLongEnough(EditText amountEditText) {
				return amountEditText.getText().toString().trim().length() > 0;
			}

		};
		amountEditText.addTextChangedListener(fieldValidatorTextWatcher);
	}

	static String intToString(int num, int digits) {
		if (digits == 0)
			return String.valueOf(num);
		else {
			// create variable length array of zeros
			char[] zeros = new char[digits];
			Arrays.fill(zeros, '0');
			// format number as String
			DecimalFormat df = new DecimalFormat(String.valueOf(zeros));

			return df.format(num);
		}
	}

	/**************************************************************
	 * getting from com.google.zxing.client.android.encode.QRCodeEncoder
	 * 
	 * See the sites below http://code.google.com/p/zxing/
	 * http://code.google.com
	 * /p/zxing/source/browse/trunk/android/src/com/google/
	 * zxing/client/android/encode/EncodeActivity.java
	 * http://code.google.com/p/zxing
	 * /source/browse/trunk/android/src/com/google/
	 * zxing/client/android/encode/QRCodeEncoder.java
	 */

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width,
			int img_height) throws WriterException {
		String contentsToEncode = contents;
		if (contentsToEncode == null) {
			return null;
		}
		Map<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(contentsToEncode);
		if (encoding != null) {
			hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result;
		try {
			result = writer.encode(contentsToEncode, format, img_width,
					img_height, hints);
		} catch (IllegalArgumentException iae) {
			// Unsupported format
			return null;
		}
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}

	public void onCreate(final RewardsBean myGoodieRewardsBean,
			final String rewardCode) {

		mHomePage.tampRewardCode = rewardCode;
		mHomePage.tampMyGoodieRewardsBean = myGoodieRewardsBean;

		mHomePage = Tabbars.getInstance();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Rewards Redeemed Page").build());

	
mParentLayout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
			
			@Override
			public void onViewDetachedFromWindow(View v) {
				// TODO Auto-generated method stub
			
			AppConstants.setbrightness(brightness, mHomePage);
				
			b++;
			}
			
			@Override
			public void onViewAttachedToWindow(View v) {
				// TODO Auto-generated method stub
				
					brightness=AppConstants.getbrightness(mHomePage);
							
					
					AppConstants.changeScreenBrightness(mHomePage);
				
					
							
			
			
			}
		});
	
	

		
		
		
		
		this.rewardCode = rewardCode;
		ImageView fbButton = (ImageView) mParentLayout
				.findViewById(R.id.rewards_fb_button);
		ImageView twitterButton = (ImageView) mParentLayout
				.findViewById(R.id.rewards_twitter_button);
		ImageView instagramButton = (ImageView) mParentLayout
				.findViewById(R.id.rewards_ig_button);

		
		RelativeLayout backarrow;
		backarrow=(RelativeLayout)mParentLayout.findViewById(R.id.backarrow);
		backarrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Rewards.getInstance().onBackPressed();
			
			
			}
		});
		
		
		TextView titleTV = (TextView) mParentLayout
				.findViewById(R.id.rewardTitle);

//		TextView rewardText = (TextView) mParentLayout
//				.findViewById(R.id.rewardText);
//		
		TextView rewardsDesc = (TextView) mParentLayout
				.findViewById(R.id.redeemDesc);
		ImageView doneBtn = (ImageView) mParentLayout
				.findViewById(R.id.doneBtn);
		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());

		userCode = (TextView) mParentLayout.findViewById(R.id.userCode);
		// pageProgressBar = (ProgressBar) mParentLayout
		// .findViewById(R.id.greenProgressBar);

		// EAN13CodeBuilder bb = new EAN13CodeBuilder(intToString(
		// Integer.valueOf(rewardCode), 12));
		// barcodeResult.setText(bb.getCode());

		titleTV.setText(myGoodieRewardsBean.getName());

		// AppConstants.fontEANBarcodeTextView(barcodeResult, 150,
		// AppConstants.COLORBLACKRGB, mHomePage.getAssets());
		AppConstants.kingthingsTextView(titleTV, 25, AppConstants.COLOR_BLACK_PAGETITLE, mHomePage.getAssets());
		
		
//		AppConstants.fontDinLightTextView(rewardText, 26,
//				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
//		
		
		AppConstants.gothamNarrowBookTextView(rewardsDesc, 14, AppConstants.COLORRGRAY,mHomePage.getAssets());
		
		
		
		
		AppConstants.gothamNarrowBookTextView(userCode, 14, AppConstants.COLORRGRAY,mHomePage.getAssets());
		
		
		
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//AppConstants.reduceScreenBrightness(mHomePage);
				boolean isFirstPage = false;
				while (!isFirstPage) {
					try {
						AppConstants.isBarcodePage = false;
						isFirstPage = !Rewards.getInstance().handleBackButton();
					} catch (Exception e) {
						isFirstPage = true;
					}

				}
				if (isFirstPage)
					RotiHomeActivity.getInstance().oPenTabView(1);
			}
		});

		instagramButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mediaShare = "instagram";
				new fetchStatusShareRequestServer().execute("");
				// Rewards.getInstance().showSnapInstagramImage();
				// createInstagramIntent(type, mediaPath, captionText);
			}
		});

		twitterButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (mHomePage.checkIfLogin()) {
						mHomePage.doNotFinishAllActivities = true;
						mHomePage.loginToTwitterToRedeem();
					} else {

						Rewards.getInstance().showLoginOptionPage(false,
								"REWARDS");

					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);

			}
		});

		fbButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (mHomePage.checkIfLogin()) {
						mHomePage.doNotFinishAllActivities = true;
						mediaShare = "facebook";
						mHomePage.postToWall();
						// mHomePage.mSimpleFacebook.login(onLoginListener);
					} else {

						Rewards.getInstance().showLoginOptionPage(false,
								"REWARDS");

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

		new getUserDetail().execute("");
	}

	private class getUserDetail extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// pageProgressBar.setVisibility(View.VISIBLE);
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().show();
		}

		@Override
		protected String doInBackground(String... params) {

			String result = "";
			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			result = WebHTTPMethodClass.httpGetService("/user/code",
					"auth_token=" + authToken + "&appkey="
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
			if (result != null && !result.equals(""))
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					if (sucess.equalsIgnoreCase("true")) {

						// barcode data
						String barcode_data = RewardRedeemedPage.this.rewardCode;

						// barcode image
						Bitmap bitmap = null;
						ImageView QRCode = (ImageView) mParentLayout
								.findViewById(R.id.QRCode);
						RelativeLayout barcodeLayout = (RelativeLayout) mParentLayout
								.findViewById(R.id.barcodeLayout);

					//	AppConstants.changeScreenBrightness(mHomePage);
						AppConstants.isBarcodePage = true;

						try {
							int width = barcodeLayout.getWidth() + 100;
							int height = Integer.valueOf(width / 3);
							bitmap = encodeAsBitmap(barcode_data,
									BarcodeFormat.QR_CODE, width, height);
							QRCode.setImageBitmap(bitmap);

						} catch (WriterException e) {
							e.printStackTrace();
						}

						userCode.setText("Code: "
								+ RewardRedeemedPage.this.rewardCode);

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			else
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mHomePage);

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

	private class fetchStatusShareRequestServer extends
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
								} else if (mediaShare == "instagram") {
									Log.i("elang", "elang ig keteken");
									try {
										mHomePage.doNotFinishAllActivities = true;
										Rewards.getInstance().isCameraShowing = true;
										Rewards.getInstance().igShareText = resObject
												.getString("instagram_share_text");
										// SnapInstagramImageActivity
										// .getInstance().captionText =
										// resObject
										// .getString("instagram_share_text");
										Intent i = new Intent(
												mHomePage,
												SnapInstagramImageActivity.class);
										mHomePage.startActivity(i);
									} catch (Exception e) {
										Log.i("elang", "elang error :" + e);
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
			mHomePage.getProgressDialog().hide();
			// new fetchRewardsFromServer().execute("");
		}
	}

	String mediumShare;

	private void postToWall(String text) {

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
				// post on user's wall.

				// Feed feed = new Feed.Builder().setMessage(
				// facebook_edit_message.getText().toString()).build();

				// mHomePage.mSimpleFacebook
				// .publish(feed, true, onPublishListener);
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

}
