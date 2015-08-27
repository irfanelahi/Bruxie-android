package com.ak.app.roti.reward;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.app.cb.activity.Rewards;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.app.roti.bean.MyGoodieRewardsBean;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.RefreshListner;
import com.akl.zoes.kitchen.util.RewardsBean;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class MyGoodies implements RefreshListner {
	private static MyGoodies screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;
	private static AlertDialog.Builder alertDialogBuilder;
	private RelativeLayout fruitCup;
	// private ProgressBar greenProgressBar;

	private ProgressBar pageProgressBar;

	public static MyGoodies getInstance() {
		if (screen == null)
			screen = new MyGoodies();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.enjoy_mygoodies, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	LinearLayout mParntReward;
	public ArrayList<MyGoodieRewardsBean> listMyGoodieRewardsBean;
	private boolean listMyGoodieRewardsAvailable;
	public double mTotalPoint = 0;
	private TextView rewardpointsvalueTextView;
	private ImageView circleImage;
	private TextView myRewardsText;

	public void onCreate() {
//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Reward Main Page").build());
		mHomePage = Tabbars.getInstance();
	//	AppConstants.changeScreenBrightnessToDefault(mHomePage);
		mParntReward = (LinearLayout) mParentLayout
				.findViewById(R.id.enjoy_mygoodies_linear_reward_parent);
		rewardpointsvalueTextView = (TextView) mParentLayout
				.findViewById(R.id.secondFruitPointsText);
		fruitCup = (RelativeLayout) mParentLayout.findViewById(R.id.fruitCup);
		circleImage = (ImageView) mParentLayout.findViewById(R.id.circle_image);

		myRewardsText = (TextView) mParentLayout
				.findViewById(R.id.myRewardsText);
		myRewardsText.setVisibility(View.INVISIBLE);
		// greenProgressBar = (ProgressBar) mParentLayout
		// .findViewById(R.id.greenProgressBar);
		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.odinRoundedBoldTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());

		AppConstants.americanTypewriterTextView(rewardpointsvalueTextView, 18,
				AppConstants.COLORWHITECORNERBKERY, mHomePage.getAssets());

		AppConstants.americanTypewriterTextView(myRewardsText, 15,
				AppConstants.COLORBLACKCORNERBAKERYINFO, mHomePage.getAssets());

		rewardpointsvalueTextView.setText(" ");
		refreshView();
	}

	@Override
	public void notifyRefresh(String className) {
		if (className.equalsIgnoreCase("showRewardPage"))
			refreshView();
	}

	public void refreshView() {
		if (AppConstants.isNetworkAvailable(mHomePage)) {
			if (mHomePage.checkIfLogin()) {
				new fetchReferralRequestServer().execute("");
			} else {
				Rewards.getInstance().showLoginOptionPage(false, "REWARDS");
				// mHomePage.showLoginOptionPage(false);
			}
		} else
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, mHomePage);
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			// Log exception
			return null;
		}
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().show();
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
			fruitCup.setVisibility(View.VISIBLE);
			rewardpointsvalueTextView.setVisibility(View.VISIBLE);

			try {
				Bitmap resized;
				resized = Bitmap.createScaledBitmap(result,
						fruitCup.getHeight(), fruitCup.getHeight() - 10, true);
				bmImage.setImageBitmap(resized);
			} catch (Exception e) {
				// TODO: handle exception
				Log.i("elang", "elang" + e);
			}

			// if (fruitCup.getHeight()<311){
			// resized = Bitmap.createScaledBitmap(result,
			// fruitCup.getHeight()/2 + 50,
			// fruitCup.getHeight()/2 + 50, true);
			// Toast.makeText(mHomePage, "1", Toast.LENGTH_LONG).show();
			// } else if(fruitCup.getHeight()<400){
			// resized = Bitmap.createScaledBitmap(result,
			// fruitCup.getHeight()/2 + 75,
			// fruitCup.getHeight()/2 + 75, true);
			// }
			// else{
			// resized = Bitmap.createScaledBitmap(result,
			// fruitCup.getHeight()/2 + 158,
			// fruitCup.getHeight()/2 + 158, true);
			// }
			//
			// Bitmap circleBitmap = Bitmap.createBitmap(
			// resized.getWidth(), resized.getHeight(),
			// Bitmap.Config.ARGB_8888);
			// BitmapShader shader = new BitmapShader(resized,
			// TileMode.CLAMP, TileMode.CLAMP);
			// Paint paint = new Paint();
			// paint.setShader(shader);
			// Canvas c = new Canvas(circleBitmap);
			// c.drawCircle(resized.getWidth() / 2,
			// resized.getHeight() / 2, resized.getWidth() / 2,
			// paint);

			// bmImage.setPadding(left, top, right, bottom)
			// if(fruitCup.getHeight()<400){
			// bmImage.setPadding(7, 21, 0, 0);
			// } else{
			// bmImage.setPadding(7, 43, 0, 0);
			// }

			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().dismiss();
		}
	}

	private class fetchRewardsFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (mHomePage != null && mHomePage.getProgressDialog() != null
					&& !mHomePage.getProgressDialog().isShowing())
				mHomePage.getProgressDialog().show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");

			// MyGoodies get reward @@@@@@@@@@@@@@
			result = WebHTTPMethodClass.httpGetService("/rewards", //
					"auth_token=" + authToken + "&appkey="
							+ AppConstants.APPKEY + "&locale="
							+ AppConstants.LOCALE_HEADER_VALUE);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().dismiss();

			// fruitCup.setVisibility(View.INVISIBLE);
			// rewardpointsvalueTextView.setVisibility(View.INVISIBLE);
			mParntReward.removeAllViews();
			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					String balance = resObject.getString("balance");
					JSONObject jsonBal = new JSONObject(balance);
					String mailstonePoints = jsonBal
							.getString("milestone_points");
					String points = jsonBal.getString("points");
					String rewardsImage = resObject.getString("rewards_image");
					// String points = jsonBal.getString("milestone_points");
					try {
						String rewardNotif = resObject
								.getString("new_reward_notification");

						showShareMsgDialog("", rewardNotif, mHomePage);

					} catch (Exception e) {

					}

					// points = "800.0";
					if (points.contains("."))
						points = points.substring(0, points.indexOf("."));

					if (mailstonePoints.contains("."))
						mailstonePoints = mailstonePoints.substring(0,
								mailstonePoints.indexOf("."));

					Double ceilPoints = Math.ceil(Double
							.valueOf(mailstonePoints));
					// ceilPoints = 8.0;

					if (ceilPoints <= 0)
						fruitCup.setBackgroundResource(R.drawable.circle_0);
					else if (ceilPoints >= 1 && ceilPoints <= 10)
						fruitCup.setBackgroundResource(R.drawable.circle_1);
					else if (ceilPoints >= 11 && ceilPoints <= 20)
						fruitCup.setBackgroundResource(R.drawable.circle_2);
					else if (ceilPoints >= 21 && ceilPoints <= 30)
						fruitCup.setBackgroundResource(R.drawable.circle_3);
					else if (ceilPoints >= 31 && ceilPoints <= 40)
						fruitCup.setBackgroundResource(R.drawable.circle_4);
					else if (ceilPoints >= 41 && ceilPoints <= 50)
						fruitCup.setBackgroundResource(R.drawable.circle_5);
					else if (ceilPoints >= 51 && ceilPoints <= 60)
						fruitCup.setBackgroundResource(R.drawable.circle_6);
					else if (ceilPoints >= 61 && ceilPoints <= 70)
						fruitCup.setBackgroundResource(R.drawable.circle_7);
					else if (ceilPoints >= 71 && ceilPoints <= 80)
						fruitCup.setBackgroundResource(R.drawable.circle_8);
					else if (ceilPoints >= 81 && ceilPoints <= 90)
						fruitCup.setBackgroundResource(R.drawable.circle_9);
					else if (ceilPoints >= 91 && ceilPoints <= 99)
						fruitCup.setBackgroundResource(R.drawable.circle_9);

					rewardpointsvalueTextView.setText(mailstonePoints + "%");

					// make the image have circle stroke
					// Bitmap bitmap =
					// BitmapFactory.decodeResource(mHomePage.getResources(),
					// R.drawable.home_image_1);

					if (!rewardsImage.equals(""))
						new DownloadImageTask(circleImage)
								.execute(rewardsImage);
					// Bitmap bitmap = getBitmapFromURL(rewardsImage);
					// Bitmap resized = Bitmap.createScaledBitmap(bitmap, 228,
					// 228, true);
					//
					// Bitmap circleBitmap = Bitmap.createBitmap(
					// resized.getWidth(), resized.getHeight(),
					// Bitmap.Config.ARGB_8888);
					// BitmapShader shader = new BitmapShader(resized,
					// TileMode.CLAMP, TileMode.CLAMP);
					// Paint paint = new Paint();
					// paint.setShader(shader);
					// Canvas c = new Canvas(circleBitmap);
					// c.drawCircle(resized.getWidth() / 2,
					// resized.getHeight() / 2, resized.getWidth() / 2,
					// paint);
					// circleImage.setImageBitmap(circleBitmap);
					// circleImage.setPadding(7, 21, 0, 0);

					// end

					try {
						mTotalPoint = Double.parseDouble(points);
					} catch (Exception e) {
					}
					if (sucess.equalsIgnoreCase("true")) {
						JSONArray jsonArray = resObject.getJSONArray("rewards");
						listMyGoodieRewardsBean = new ArrayList<MyGoodieRewardsBean>();
						for (int i = 0; i < jsonArray.length(); i++) {
							MyGoodieRewardsBean myGoodieRewardsBean = new MyGoodieRewardsBean();
							myGoodieRewardsBean.setPOSCode(jsonArray
									.getJSONObject(i).getString("POSCode"));
							myGoodieRewardsBean.setChain_id(jsonArray
									.getJSONObject(i).getString("chain_id"));
							myGoodieRewardsBean.setEffectiveDate(jsonArray
									.getJSONObject(i)
									.getString("effectiveDate"));
							myGoodieRewardsBean.setExpiryDate(jsonArray
									.getJSONObject(i).getString("expiryDate"));
							myGoodieRewardsBean.setFineprint(jsonArray
									.getJSONObject(i).getString("fineprint"));
							myGoodieRewardsBean.setId(jsonArray
									.getJSONObject(i).getString("id"));
							myGoodieRewardsBean.setName(jsonArray
									.getJSONObject(i).getString("name"));
							myGoodieRewardsBean.setPoints(jsonArray
									.getJSONObject(i).getString("points"));
							myGoodieRewardsBean.setReward_type(jsonArray
									.getJSONObject(i).getString("reward_type"));
							myGoodieRewardsBean.setSurvey_id(jsonArray
									.getJSONObject(i).getString("survey_id"));
							myGoodieRewardsBean.setExpired(jsonArray
									.getJSONObject(i).getString("expired"));
							myGoodieRewardsBean.setSort_by_id(jsonArray
									.getJSONObject(i).getString("sort_by_id"));
							myGoodieRewardsBean.setGifter(jsonArray
									.getJSONObject(i).getString("gifter"));
							myGoodieRewardsBean.setImageURL(jsonArray
									.getJSONObject(i).getString("image_url"));
							JSONArray additionalFieldsArray = jsonArray
									.getJSONObject(i).getJSONArray(
											"additional_fields");
							if (additionalFieldsArray.length() == 0)
								myGoodieRewardsBean
										.setIsAdditionalInfoExist(false);
							else {
								myGoodieRewardsBean
										.setIsAdditionalInfoExist(true);

								myGoodieRewardsBean
										.setArrayLengthOfAdditionalInfo(additionalFieldsArray
												.length());
								for (int j = 0; j < additionalFieldsArray
										.length(); j++) {
									myGoodieRewardsBean.setAddInfoAppText(
											additionalFieldsArray
													.getJSONObject(j)
													.getString("app_text"), j);
									myGoodieRewardsBean.setAddInfoDescription(
											additionalFieldsArray
													.getJSONObject(j)
													.getString("description"),
											j);
									myGoodieRewardsBean.setAddInfoId(
											additionalFieldsArray
													.getJSONObject(j)
													.getString("id"), j);
									myGoodieRewardsBean.setAddInfoTitle(
											additionalFieldsArray
													.getJSONObject(j)
													.getString("title"), j);
								}

							}
							listMyGoodieRewardsBean.add(myGoodieRewardsBean);
						}

						// if (listMyGoodieRewardsAvailable == true) {
						// createRewardView(listMyGoodieRewardsBean);
						// myRewardsText.setVisibility(View.VISIBLE);
						// }

						if (listMyGoodieRewardsBean != null
								&& listMyGoodieRewardsBean.size() > 0) {
							createRewardView(listMyGoodieRewardsBean);
							myRewardsText.setVisibility(View.VISIBLE);
							myRewardsText
									.setText("Earned a reward? Tap to redeem.");
							// listMyGoodieRewardsAvailable = true;

						} else {
							myRewardsText.setVisibility(View.VISIBLE);
							myRewardsText
									.setText("Scan your loyalty Barcode to make progress towards your next surprise!");
						}
					}
				} catch (Exception e) {
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORFAILEDAPI, mHomePage);
					e.printStackTrace();
				}
			} else
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mHomePage);
			AppConstants.parseInput(result, mHomePage);
		}
	}

	public void createRewardView(
			ArrayList<MyGoodieRewardsBean> listMyGoodieRewardsBean) {

		for (int i = 0; i < listMyGoodieRewardsBean.size(); i++) {
			MyGoodieRewardsBean myGoodieRewardsBean = listMyGoodieRewardsBean
					.get(i);
			LinearLayout cellViewMainLayout = (LinearLayout) mInflater.inflate(
					R.layout.enjoy_mygodie_reward_list, null);
			TextView titleTextView = (TextView) cellViewMainLayout
					.findViewById(R.id.enjoy_mygodie_reward_list_text_title);
			final TextView dateTextView = (TextView) cellViewMainLayout
					.findViewById(R.id.enjoy_mygodie_reward_list_text_date);
			final TextView dateText = (TextView) cellViewMainLayout
					.findViewById(R.id.enjoy_mygodie_reward_list_date);
			dateText.setVisibility(View.GONE);

			AppConstants.americanTypewriterTextView(titleTextView, 13,
					AppConstants.COLORBLACKCORNERBAKERYINFO,
					mHomePage.getAssets());
			AppConstants.americanTypewriterTextView(dateTextView, 13,
					AppConstants.COLORBLACKCORNERBAKERYINFO,
					mHomePage.getAssets());
			AppConstants.americanTypewriterTextView(dateText, 9,
					AppConstants.COLORBLACKCORNERBAKERYINFO,
					mHomePage.getAssets());// fontdinmediumTextView
			RelativeLayout cellParent = (RelativeLayout) cellViewMainLayout
					.findViewById(R.id.enjoy_mygodie_reward_list_parent_reward);
			Drawable inactiveBackground = mHomePage.getResources().getDrawable(
					R.drawable.rewards_list_inactive);
			inactiveBackground.setAlpha(50);
			cellParent.setBackgroundResource(R.drawable.rewards_list_inactive); // set
			// for
			// expired
			// rewards

			String points = myGoodieRewardsBean.getPoints();
			// points = "800.0";
			if (points.contains("."))
				points = points.substring(0, points.indexOf("."));
			titleTextView.setText("" + myGoodieRewardsBean.getName());
			{
				double point = 0;
				try {
					point = Double.parseDouble(points);
				} catch (Exception e) {
				}
				if (!myGoodieRewardsBean.getExpired().equals("true")
						&& point <= mTotalPoint) {
					cellParent.setBackgroundResource(R.drawable.rewards_list);
					; // active rewards
					dateTextView.setVisibility(View.VISIBLE);
					myGoodieRewardsBean.setExpirestate("reward");
					try {

						if (points.equals("0")) {
							dateTextView.setText("FREE");

							try {
								String deadline = myGoodieRewardsBean
										.getExpiryDate();
								java.util.Date d1 = AppConstants
										.makeDate(deadline);
								SimpleDateFormat curFormater = new SimpleDateFormat(
										"MM/dd/yyyy");// "MM-dd-yy"
								String currentTime = curFormater.format(d1);
								dateText.setText("EXPIRES " + currentTime);
								dateText.setVisibility(View.VISIBLE);
							} catch (Exception e) {

							}
						} else
							dateTextView.setText(points + " FRUITS");

						dateTextView.setVisibility(View.INVISIBLE);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (myGoodieRewardsBean.getExpired().equals("false")) {
					myGoodieRewardsBean.setExpirestate("expired");
					dateTextView.setText(points + " FRUITS");
					dateTextView.setVisibility(View.INVISIBLE);
				} else if (myGoodieRewardsBean.getGifter().equals("true")) {

				} else if (myGoodieRewardsBean.getGifter().equals("false")) {
				} else {
					myGoodieRewardsBean.setExpirestate("expire");
					dateTextView.setText("EXPIRED");

					AppConstants.americanTypewriterTextView(titleTextView, 13,
							AppConstants.COLORGREYCORNERBAKERY3,
							mHomePage.getAssets());
					AppConstants.americanTypewriterTextView(dateTextView, 13,
							AppConstants.COLORGREYCORNERBAKERY3,
							mHomePage.getAssets());
					AppConstants.americanTypewriterTextView(dateText, 9,
							AppConstants.COLORGREYCORNERBAKERY3,
							mHomePage.getAssets());
					dateTextView.setVisibility(View.VISIBLE);

				}

				dateTextView.setTag(myGoodieRewardsBean);
				cellParent.setTag(dateTextView);
				dateTextView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						RewardsBean myGoodieRewardsBean = (RewardsBean) v
								.getTag();
						setDeleteButtonState(myGoodieRewardsBean, (TextView) v);
					}
				});
				cellParent.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						TextView dateTextView = (TextView) v.getTag();
						RewardsBean myGoodieRewardsBean = (RewardsBean) dateTextView
								.getTag();
						setDeleteButtonState(myGoodieRewardsBean, dateTextView);
					}
				});
			}
			mParntReward.addView(cellViewMainLayout);
		}
	}

	private void setDeleteButtonState(RewardsBean myGoodieRewardsBean,
			TextView dateTextView) {
		if (myGoodieRewardsBean.getExpired().equals("true")
				&& myGoodieRewardsBean.getExpirestate().equals("expire")) {
			dateTextView.setText("DELETE");
			AppConstants.americanTypewriterTextView(dateTextView, 14,
					AppConstants.COLORGREYCORNERBAKERY2, mHomePage.getAssets());
			myGoodieRewardsBean.setExpirestate("delete");
		} else if (myGoodieRewardsBean.getExpired().equals("true")
				&& myGoodieRewardsBean.getExpirestate().equals("delete")) {
			if (mHomePage.checkIfLogin()) {
				new deleteRewardFromServer().execute(myGoodieRewardsBean
						.getId()/* "" */);
			} else {
				Rewards.getInstance().showLoginOptionPage(false, "REWARDS");
				// mHomePage.showLoginOptionPage(false);
			}
		} else if (!myGoodieRewardsBean.getExpired().equals("true")
				&& myGoodieRewardsBean.getExpirestate().equals("reward")) {

			Tabbars.getInstance().startGPS();
			if (AppConstants.isNetworkAvailable(mHomePage)) {
				if (mHomePage.checkIfLogin()) {
					// Rewards.getInstance().showClaimRewardPage(
					// myGoodieRewardsBean);
					if (myGoodieRewardsBean.getGifter().equals("true")) {
						Rewards.getInstance().showClaimRewardPageGifter(
								myGoodieRewardsBean, "true");
						// has been changed by me
						// mHomePage.showFriendGoodiesPage(myGoodieRewardsBean);
					} else
						Rewards.getInstance().showClaimRewardPage(
								myGoodieRewardsBean);
				} else {
					Rewards.getInstance().showLoginOptionPage(false, "REWARDS");
				}
			} else
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORNETWORKCONNECTION, mHomePage);

		} else if (!myGoodieRewardsBean.getExpired().equals("true")
				&& myGoodieRewardsBean.getExpirestate().equals("reward")) {

			mHomePage.startGPS();
			if (myGoodieRewardsBean.getGifter().equals("true")) {
				Rewards.getInstance().showClaimRewardPageGifter(
						myGoodieRewardsBean, "true");
				// has been changed by me
				// mHomePage.showFriendGoodiesPage(myGoodieRewardsBean);
			} else
				Rewards.getInstance().showClaimRewardPage(myGoodieRewardsBean);
			// mHomePage.showClaimRewardPage(myGoodieRewardsBean);

		}
	}

	private class deleteRewardFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			result = WebHTTPMethodClass.httpDeleteService("/rewards/"
					+ params[0], "auth_token=" + authToken + "&appkey="
					+ AppConstants.APPKEY + "&locale="
					+ AppConstants.LOCALE_HEADER_VALUE);
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
					if (sucess.equalsIgnoreCase("true")) {
						if (AppConstants.isNetworkAvailable(mHomePage)) {
							if (mHomePage.checkIfLogin()) {
								new fetchRewardsFromServer().execute("");
							} else {
								Rewards.getInstance().showLoginOptionPage(
										false, "REWARDS");
								// mHomePage.showLoginOptionPage(false);
							}
						} else
							AppConstants.showMsgDialog("Alert",
									AppConstants.ERRORNETWORKCONNECTION,
									mHomePage);
					} else
						AppConstants.parseInput(result, mHomePage);
				} catch (Exception e) {
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORFAILEDAPI, mHomePage);
					e.printStackTrace();
				}
			} else {
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mHomePage);
				AppConstants.parseInput(result, mHomePage);
			}
		}
	}

	private class fetchReferralRequestServer extends
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
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().dismiss();
			// if (result != null && !result.equals("")) {
			// try {
			// JSONObject resObject = new JSONObject(result);
			// String sucess = resObject.getString("status");
			// if (sucess != null && !sucess.equals("")
			// && sucess.equals("true")) {
			// try {
			// if (resObject.has("email_title")) {
			// name_title = resObject
			// .getString("referral_program_title");
			// email_title = resObject
			// .getString("email_title");
			// email_body = resObject.getString("email_body");
			// incentive_title = resObject
			// .getString("incentive_title");
			// isFriends = true;
			// }
			// } catch (Exception e) {
			// }
			// } else {
			// email_title = "";
			// email_body = "";
			// incentive_title = "";
			// isFriends = false;
			// }
			// } catch (Exception e) {
			// // AppConstants.showMsgDialog("Alert",
			// // AppConstants.ERRORFAILEDAPI, mHomePage);
			// AppConstants.showMsgDialog("Alert",
			// "Ini Lima : " + e.getMessage(), mHomePage);
			// e.printStackTrace();
			// }
			// } else {
			// email_title = "";
			// email_body = "";
			// incentive_title = "";
			// isFriends = false;
			// // AppConstants.showMsgDialog("Alert",
			// // AppConstants.ERRORFAILEDAPI, mHomePage);
			// AppConstants.showMsgDialog("Alert",
			// "Ini Enam", mHomePage);
			// }
			new fetchRewardsFromServer().execute("");
		}
	}

	String email_title = "";
	String name_title = "";
	String email_body = "";
	String incentive_title = "";
	boolean isFriends = false;

	private void openMailView() {
		final Intent emailIntent = new Intent(
				android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { "" });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, email_title);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, email_body);
		mHomePage.startActivity(Intent.createChooser(emailIntent, "Email"));
	}

	private void showShareMsgDialog(String title, final String message,
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
			} else {
				alert.setTitle(title);
			}
			alert.show();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
