package com.ak.app.roti.reward;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.Rewards;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.app.roti.bean.MyGoodieRewardsBean;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.ImageLoader;
import com.akl.zoes.kitchen.util.RefreshListner;
import com.akl.zoes.kitchen.util.RewardsBean;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class ClaimRewardPage implements RefreshListner {
	private static ClaimRewardPage screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;

	private LinearLayout mClaimLinearParnt;
	private LinearLayout mtimerLinear;
	private LinearLayout mRedeemLinearParnt;
int b=0;
	private TextView storeaddressTV;
	private RewardsBean mMyGoodieRewardsBean;
	private TextView mRedeemtitleTV;
	private TextView redeemcodeTV;
	private boolean statusRedeem = false;
ImageLoader imageload;
	private TextView timeremainingvalueTV;
	private TextView timeredeemTV;
	private RelativeLayout mTimeremainingRelativeParnt;
	TextView pageTitle;
	int brightness=0;
	RelativeLayout stashGuyLayout;
	LinearLayout reward_claim_linear_parent;

//	private ProgressBar pageProgressBar;

	public static ClaimRewardPage getInstance() {
		if (screen == null)
			screen = new ClaimRewardPage();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.reward_claim, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	public void onCreate(final RewardsBean myGoodieRewardsBean) {
		
		
		
		
		
		
		
		
		
		
		mMyGoodieRewardsBean = myGoodieRewardsBean;
		mHomePage = Tabbars.getInstance();
		imageload=new ImageLoader(mHomePage);

		
		
		//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Redeem Reward").build());

		TextView titleTV = (TextView) mParentLayout
				.findViewById(R.id.rewardTitle);

//		TextView rewardText = (TextView) mParentLayout
//				.findViewById(R.id.rewardText);
//		
		TextView rewardsDesc = (TextView) mParentLayout
				.findViewById(R.id.redeemDesc);
		ImageView btnRewardClaim = (ImageView) mParentLayout
				.findViewById(R.id.redeemBtn);
		
		ImageView rewardimag=(ImageView)mParentLayout.findViewById(R.id.rewardimag);
		
		if(!myGoodieRewardsBean.getImageURL().equalsIgnoreCase(""))
			
		{
			
			imageload.DisplayImage(mMyGoodieRewardsBean.getImageURL(), mHomePage, rewardimag);
		}
			
		
		
		RelativeLayout backarrow;
		backarrow=(RelativeLayout)mParentLayout.findViewById(R.id.backarrow);
		backarrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Rewards.getInstance().onBackPressed();
			
			
			}
		});
		titleTV.setText(mMyGoodieRewardsBean.getName());
		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());

//		AppConstants.americanTypewriterTextView(titleTV, 30,
//				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
//	
		AppConstants.kingthingsTextView(titleTV, 25, AppConstants.COLOR_BLACK_PAGETITLE, mHomePage.getAssets());
		
		
//		
//		AppConstants.fontDinLightTextView(rewardText, 26,
//				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
//		
		
//		AppConstants.americanTypewriterTextView(rewardsDesc, 13,
//				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
//	
		AppConstants.gothamNarrowBookTextView(rewardsDesc, 14, AppConstants.COLORRGRAY,mHomePage.getAssets());
		
		
		btnRewardClaim.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (AppConstants.isNetworkAvailable(mHomePage)) {
					// if (mHomePage.checkIfLogin()) {
					// showRedeemConfirmMsgDialog(AppConstants.CONSTANTTITLEMESSAGE,
					// "Are you sure you're ready to redeem?");
					// } else {
					// Rewards.getInstance().showLoginOptionPage(false,
					// "REWARDS");
					// }
					if (AppConstants.isNetworkAvailable(mHomePage)) {
							 Tabbars.getInstance().startGPS();
							if (AppConstants.isNetworkAvailable(mHomePage)) {
								new fetchWarningValuesFromServer().execute("");
							} else
								AppConstants.showMsgDialog("",
										AppConstants.ERRORNETWORKCONNECTION,
										mHomePage);
						

					} else {
						Rewards.getInstance().showLoginOptionPage(false,
								"REWARDS");
					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);

			}
		});

		refreshView();
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

	// public void handleBack(){
	// if (statusRedeem == true) {
	// Rewards.getInstance().showRewardPage();
	// // countDownTimer = null;
	// } else {
	// showBackconfirmDialog();
	// // countDownTimer = null;
	// }
	// }

	@Override
	public void notifyRefresh(String className) {
		if (className.equalsIgnoreCase("showClaimRewardPage"))
			refreshView();
	}

	public void refreshView() {
		if (AppConstants.isNetworkAvailable(mHomePage)) {
			if (mHomePage.checkIfLogin()) {
				new fetchRewardAddressFromServer().execute("");
			} else {
				Rewards.getInstance().showLoginOptionPage(false, "REWARDS");
				// mHomePage.showLoginOptionPage(false);
			}
		} else
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, mHomePage);
	}

	public void createRedeemView() {
		// countDownTimer = null ;
		mClaimLinearParnt.setVisibility(View.GONE);
		mtimerLinear.setVisibility(View.VISIBLE);
		mRedeemLinearParnt.setVisibility(View.VISIBLE);

		mTimeremainingRelativeParnt.setVisibility(View.GONE);
		// mTimeremainingRelativeParnt.setBackgroundResource(R.drawable.redeemed);
		redeemcodeTV.setVisibility(View.VISIBLE);
		try {
		} catch (Exception e) {
		}
	}

	private class fetchWarningValuesFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			 if (mHomePage != null && mHomePage.getProgressDialog() != null)
			 mHomePage.getProgressDialog().show();
//			pageProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
			nameparams
					.add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
			nameparams.add(new BasicNameValuePair("auth_token", authToken));
			nameparams.add(new BasicNameValuePair("reward_id",
					mMyGoodieRewardsBean.getId()));// id
			nameparams.add(new BasicNameValuePair("lat", mHomePage
					.getGetLatLongObj().getLatitude() + ""));
			nameparams.add(new BasicNameValuePair("lng", mHomePage
					.getGetLatLongObj().getLongitude() + ""));
			nameparams.add(new BasicNameValuePair("location", rewardLocateid));// id
																				// get
																				// from
																				// get
																				// address
																				// reward
			nameparams.add(new BasicNameValuePair("warn", "true"));
			result = WebHTTPMethodClass.executeHttPost("/rewards/claim",
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
//			pageProgressBar.setVisibility(View.GONE);
			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String warn_tile = "";
					String warn_body = "";
					if (resObject.has("warn_tile")
							&& resObject.has("warn_body")) {
						warn_tile = resObject.getString("warn_tile");
						warn_body = resObject.getString("warn_body");
						showRedeemConfirmMsgDialog(warn_tile, warn_body);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	private void formatTime(long time) {
		long min = time / 60000;
		long sec = time - min * 60000;
		long showTime = sec / 1000;
		String delim = ":";
		if (showTime < 10)
			delim = ":0";
		if (min < 1 && showTime < 1)
			showTime = 0;
		timeremainingvalueTV.setText(min + delim + showTime);
	}

	private class fetchRewardAddressFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			 if (mHomePage != null && mHomePage.getProgressDialog() != null)
			 mHomePage.getProgressDialog().show();
//			pageProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			result = WebHTTPMethodClass.httpGetService("/rewards/locate",
					"auth_token=" + authToken + "&appkey="
							+ AppConstants.APPKEY + "&lat="
							+ mHomePage.getGetLatLongObj().getLatitude()
							+ "&lng="
							+ mHomePage.getGetLatLongObj().getLongitude());
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().dismiss();
//			pageProgressBar.setVisibility(View.GONE);
			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					if (sucess.equalsIgnoreCase("true")) {
						JSONArray jsonArray = resObject
								.getJSONArray("restaurants");
						for (int i = 0; i < jsonArray.length(); i++) {
							String address = jsonArray.getJSONObject(i)
									.getString("address");
							rewardLocateid = jsonArray.getJSONObject(i)
									.getString("id");
							storeaddressTV.setText(address);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mHomePage);
			}
			AppConstants.parseInput(result, mHomePage);
		}
	}

	String rewardLocateid = "";

	private void createTimeRemainingView() {
		mClaimLinearParnt.setVisibility(View.GONE);
		mRedeemLinearParnt.setVisibility(View.VISIBLE);
		mTimeremainingRelativeParnt.setVisibility(View.VISIBLE);
		mRedeemLinearParnt.setVisibility(View.GONE);
		// mRedeemedButton.setVisibility(View.GONE);
		countDownTimer = new RedeemCountDownTimer(startTime, interval);
		// storeaddressTV.setText("PLEASE SHOW THIS CODE TO CASHIER");
		storeaddressTV
				.setText("Please show this to your cashier. Once the cashier has your code, return to prior page to exit");
	}

	private class ClaimRewardAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			 if (mHomePage != null && mHomePage.getProgressDialog() != null)
			 mHomePage.getProgressDialog().show();
//			pageProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
			nameparams
					.add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
			nameparams.add(new BasicNameValuePair("auth_token", authToken));
			nameparams.add(new BasicNameValuePair("reward_id",
					mMyGoodieRewardsBean.getId()));
			nameparams.add(new BasicNameValuePair("lat", mHomePage
					.getGetLatLongObj().getLatitude() + ""));
			nameparams.add(new BasicNameValuePair("lng", mHomePage
					.getGetLatLongObj().getLongitude() + ""));
			nameparams.add(new BasicNameValuePair("location", rewardLocateid));
			result = WebHTTPMethodClass.executeHttPost("/rewards/claim",
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
//			pageProgressBar.setVisibility(View.GONE);
			Tabbars.getInstance().stopGPS();
			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					String reward_timer = "";
					if (sucess.equalsIgnoreCase("true")) {
						if (resObject.has("reward_code")) {
							Rewards.getInstance().showRedeemedRewardPage(
									mMyGoodieRewardsBean,
									resObject.getString("reward_code"));
						}
					} else
						AppConstants.parseInput(result, mHomePage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mHomePage);
		}
	}

	// public void showBackconfirmDialog() {
	// try {
	// AlertDialog.Builder alt_bld = new AlertDialog.Builder(mHomePage);
	// alt_bld.setMessage(
	// "Do NOT exit this page until the cashier has seen your 3-digit code. \n\nTapping \"Continue\" will return you to a prior screen. You will not be able to access this code again.")
	// .setCancelable(false)
	// .setPositiveButton("Continue",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int id) {
	// dialog.cancel();
	// countDownTimer.cancel();
	// // countDownTimer.cancel();
	// timerHasStarted = false;
	// // countDownTimer = null;
	// Rewards.getInstance().showRewardPage();
	// }
	// })
	// .setNegativeButton("Cancel",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int id) {
	// dialog.cancel();
	// // countDownTimer.cancel();
	// }
	// });
	// AlertDialog alert = alt_bld.create();
	// alert.setTitle("are you done?");
	// alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
	// alert.show();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public void showRedeemConfirmMsgDialog(String title, final String message) {
		try {
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(mHomePage);
			alt_bld.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							})
					.setNegativeButton("Redeem",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									new ClaimRewardAsyncTask().execute("");

								}
							});
			AlertDialog alert = alt_bld.create();
			if (title.equals("")) {
				alert.setTitle(AppConstants.CONSTANTTITLEMESSAGE);
				alert.setIcon(AlertDialog.BUTTON_NEGATIVE);
			} else {
				alert.setTitle(title);
				alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
			}
			alert.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean timerHasStarted = false;
	private RedeemCountDownTimer countDownTimer;
	private long startTime = 60000;
	private final long interval = 1000;
	private long timeElapsed = 0;

	public class RedeemCountDownTimer extends CountDownTimer {
		public RedeemCountDownTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		@Override
		public void onFinish() {
			// Rewards.getInstance().isClaimReward = false;
			Rewards.getInstance().isClaimReward = false;
			countDownTimer.cancel();
			timerHasStarted = false;
			countDownTimer = null;
			mtimerLinear.setVisibility(View.GONE);
			// mTimeremainingRelativeParnt
			// .setBackgroundResource(R.drawable.redeemed);
			timeremainingvalueTV.setVisibility(View.GONE);
			mClaimLinearParnt.setVisibility(View.GONE);
			mtimerLinear.setVisibility(View.VISIBLE);
			redeemcodeTV.setVisibility(View.VISIBLE);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			startTime = startTime - interval;// millisUntilFinished;
			timeElapsed = startTime;// millisUntilFinished;
			formatTime(timeElapsed);
		}
	}

	public void handlePause(boolean istab) {
		if (countDownTimer == null) {
			Rewards.getInstance().showRewardPage();
			Tabbars.getInstance().stopGPS();
			Rewards.getInstance().isClaimReward = false;
			return;
		}
		showBackConfirmMsgDialog(
				mHomePage.getString(R.string.claimmessageheader),
				mHomePage.getString(R.string.claimmessages), istab);
	}

	public void showBackConfirmMsgDialog(String title, final String message,
			final boolean istab) {
		try {
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(mHomePage);
			alt_bld.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							})
					.setNegativeButton("Continue",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									// Rewards.getInstance().isClaimReward =
									// false;
									countDownTimer.cancel();
									timerHasStarted = false;
									countDownTimer = null;
									if (!istab) {
										Rewards.getInstance()
												.handleBackButton();
										Rewards.getInstance().showRewardPage();
										Tabbars.getInstance().stopGPS();
										// mHomePage.showSpreadtheworldPage(mMyGoodieRewardsBean.getId());
										// } else {
										// mHomePage.setTabview();
									}
								}
							});
			AlertDialog alert = alt_bld.create();
			alert.setTitle(title);
			alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
			alert.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// handleBack();
	// // handleBackButton();
	// return true;
	// }
	// return true;
	// }
}
