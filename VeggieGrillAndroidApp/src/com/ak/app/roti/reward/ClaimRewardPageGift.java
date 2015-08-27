package com.ak.app.roti.reward;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.Rewards;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.app.roti.bean.MyGoodieRewardsBean;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.RefreshListner;
import com.akl.zoes.kitchen.util.RewardsBean;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;

public class ClaimRewardPageGift implements RefreshListner {
	private static ClaimRewardPageGift screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;

	private LinearLayout mClaimLinearParnt;
	private LinearLayout mRedeemLinearParnt;

	private TextView storeaddressTV;
	private RewardsBean mMyGoodieRewardsBean;
	private TextView mRedeemtitleTV;

	public static ClaimRewardPageGift getInstance() {
		if (screen == null)
			screen = new ClaimRewardPageGift();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.reward_claim_gift_true, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	public void onCreate(RewardsBean myGoodieRewardsBean, String gifter) {
		mMyGoodieRewardsBean = myGoodieRewardsBean;
		mHomePage = Tabbars.getInstance();
		TextView titleTV = (TextView) mParentLayout
				.findViewById(R.id.reward_claim_text_title);
		// mRedeemtitleTV = (TextView) mParentLayout
		// .findViewById(R.id.reward_claim_text_redeemtitle);
		storeaddressTV = (TextView) mParentLayout
				.findViewById(R.id.reward_claim_text_address);

		mClaimLinearParnt = (LinearLayout) mParentLayout
				.findViewById(R.id.reward_claim_linear_claim_btn);
		TextView claim_btn = (TextView) mParentLayout
				.findViewById(R.id.reward_claim_image_claim_btn);

		mRedeemLinearParnt = (LinearLayout) mParentLayout
				.findViewById(R.id.reward_claim_linear_redeembutton);
		// TextView titlereedeemdTV = (TextView) mParentLayout
		// .findViewById(R.id.reward_claim_text_redeembutton);

		// titleTV.setText(mMyGoodieRewardsBean.getName());
		AppConstants.fontGothamBoldTextView(titleTV, 17,
				AppConstants.COLORGREEN, mHomePage.getAssets());
		// AppConstants.fontGothamBoldTextView(mRedeemtitleTV, 14,
		// AppConstants.COLORGREY, mHomePage.getAssets());
		AppConstants.fontGothamMediumTextView(storeaddressTV, 16,
				AppConstants.COLORGREY, mHomePage.getAssets());// 3f2513
		TextView backText = (TextView) mParentLayout
				.findViewById(R.id.reward_claim_text_delete);
		backText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Rewards.getInstance().showRewardPage();
			}
		});

		mClaimLinearParnt.setVisibility(View.VISIBLE);
		mRedeemLinearParnt.setVisibility(View.GONE);
		claim_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AppConstants.CheckEnableGPS(mHomePage)) {
					if (AppConstants.isNetworkAvailable(mHomePage)) {
						if (mHomePage.checkIfLogin()) {
							new ClaimRewardAsyncTask().execute("");
						} else {
							Rewards.getInstance().showLoginOptionPage(false,
									"REWARDS");
						}
					} else
						AppConstants.showMsgDialog("Alert",
								AppConstants.ERRORNETWORKCONNECTION, mHomePage);
				} else
					AppConstants.showMsgDialog("",
							AppConstants.ERRORLOCATIONSERVICES, mHomePage);
			}
		});
		refreshView();
	}

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
		mClaimLinearParnt.setVisibility(View.GONE);
		mRedeemLinearParnt.setVisibility(View.VISIBLE);
		try {
		} catch (Exception e) {
		}
	}

	private class fetchRewardAddressFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// if (mHomePage != null && mHomePage.getProgressDialog() != null)
			// mHomePage.getProgressDialog().show();
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
			// if (mHomePage != null && mHomePage.getProgressDialog() != null)
			// mHomePage.getProgressDialog().dismiss();
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
			}
			AppConstants.parseInput(result, mHomePage);
		}
	}

	String rewardLocateid = "";

	private class ClaimRewardAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// if (mHomePage != null && mHomePage.getProgressDialog() != null)
			// mHomePage.getProgressDialog().show();
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
			nameparams.add(new BasicNameValuePair("warn", "true"));
			result = WebHTTPMethodClass.executeHttPost("/rewards/gift",
					nameparams);
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
					String status = resObject.getString("status");
					String warning = "";
					// {"status":true,"warning":"tap on continue to generate
					if (status.equals("true"))
						if (resObject.has("warning")) {
							warning = resObject.getString("warning");
							// warn_body = resObject.getString("warn_body");
							showRedeemConfirmMsgDialog("", warning);
						}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	private class fetchTimerCodeValuesFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// if (mHomePage != null && mHomePage.getProgressDialog() != null)
			// mHomePage.getProgressDialog().show();
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
			nameparams.add(new BasicNameValuePair("warn", "false"));
			result = WebHTTPMethodClass.executeHttPost("/rewards/gift",
					nameparams);
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
					String email_subject = "";
					String email_body = "";
					if (sucess.equalsIgnoreCase("true")) {
						if (resObject.has("email_subject")
								&& resObject.has("email_body")) {
							email_subject = resObject
									.getString("email_subject");
							// storeaddressTV.setText(" " + email_subject);
							email_body = resObject.getString("email_body");
							try {
								// Rewards.getInstance().showRewardPage();
								// {"status":true,"email_subject":"you have a  gift  - COOKIE",
								// "email_body":"a friend has sent you COOKIE .\r\n\r\nto use it enter  "
								// +
								// " WC5E1FD7 under info -> promocode\r\n\r\nBEst regards\r\nZoes"}

								final Intent emailIntent = new Intent(
										android.content.Intent.ACTION_SEND);
								emailIntent.setType("plain/text");
								emailIntent.putExtra(
										android.content.Intent.EXTRA_EMAIL,
										new String[] { "" });
								emailIntent.putExtra(
										android.content.Intent.EXTRA_SUBJECT,
										email_subject);
								emailIntent.putExtra(
										android.content.Intent.EXTRA_TEXT,
										email_body);
								mHomePage.startActivity(Intent.createChooser(
										emailIntent, "Email"));
								// mHomePage.checkIfEmail(true);
							} catch (Exception e) {
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void showRedeemConfirmMsgDialog(String title, final String message) {
		try {
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(mHomePage);
			alt_bld.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									if (AppConstants
											.isNetworkAvailable(mHomePage)) {
										if (mHomePage.checkIfLogin()) {
											new fetchTimerCodeValuesFromServer()
													.execute("");
										} else {
											Rewards.getInstance()
													.showLoginOptionPage(false,
															"REWARDS");
											// mHomePage
											// .showLoginOptionPage(false);
										}
									} else
										AppConstants
												.showMsgDialog(
														"Alert",
														AppConstants.ERRORNETWORKCONNECTION,
														mHomePage);
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									Rewards.getInstance().showRewardPage();
									Tabbars.getInstance().stopGPS();
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
}

// private class fetchWarningValuesFromServer extends
// AsyncTask<String, Void, String> {
//
// @Override
// protected void onPreExecute() {
// if (mHomePage != null && mHomePage.getProgressDialog() != null)
// mHomePage.getProgressDialog().show();
// }
//
// @Override
// protected String doInBackground(String... params) {
// String result = "";
// String authToken = mHomePage.getPreference().getString(
// AppConstants.PREFAUTH_TOKEN, "");
// List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
// nameparams
// .add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
// nameparams.add(new BasicNameValuePair("auth_token", authToken));
// nameparams.add(new BasicNameValuePair("reward_id",
// mMyGoodieRewardsBean.getId()));// id
// // of
// // reward
// nameparams.add(new BasicNameValuePair("lat", mHomePage
// .getGetLatLongObj().getLatitude() + ""));
// nameparams.add(new BasicNameValuePair("lng", mHomePage
// .getGetLatLongObj().getLongitude() + ""));
// nameparams.add(new BasicNameValuePair("location", rewardLocateid));// id
// // get
// // from
// // get
// // address
// // reward
// nameparams.add(new BasicNameValuePair("warn", "true"));
// result = WebHTTPMethodClass.executeHttPost("/rewards/claim",
// nameparams);
// return result;
// }
//
// @Override
// protected void onProgressUpdate(Void... values) {
//
// }
//
// @Override
// protected void onPostExecute(String result) {
// if (mHomePage != null && mHomePage.getProgressDialog() != null)
// mHomePage.getProgressDialog().dismiss();
// if (result != null && !result.equals("")) {
// try {
// JSONObject resObject = new JSONObject(result);
// String warn_tile = "";
// String warn_body = "";
// if (resObject.has("warn_tile")
// && resObject.has("warn_body")) {
// warn_tile = resObject.getString("warn_tile");
// warn_body = resObject.getString("warn_body");
// }
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
// AppConstants.parseInput(result, mHomePage);
// }
// }