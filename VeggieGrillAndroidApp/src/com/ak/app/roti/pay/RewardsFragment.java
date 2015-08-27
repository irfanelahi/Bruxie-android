package com.ak.app.roti.pay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ak.app.cb.activity.R;
import com.ak.app.cb.activity.Rewards;
import com.ak.app.cb.activity.RotiHomeActivity;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.roti.reward.MyGoodies;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.RewardsBean;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RewardsFragment  {

	private Tabbars mActivity=Tabbars.getInstance();;
	private static AlertDialog.Builder alertDialogBuilder;
	private ImageView rewardBlur1, rewardBlur2, rewardBlur3;
	//private TextView numberOfEarned;
	//private TextView numberOfUntilNextEarned;
	private LinearLayout rewardList;
	public ArrayList<RewardsBean> listRewardsBean;
	private LayoutInflater mInflater;
	private TextView textNumberOfEarnedDesc;
	private TextView textNumberOfUntilNextEarnedDesc;
	private ImageView btnPromoCode;
	private static RewardsFragment screen;
	private RelativeLayout rootView;
	

	public double mTotalPoint = 0;

	public RewardsFragment() {
	}

	
	public static RewardsFragment getInstance() {
		if (screen == null)
			screen = new RewardsFragment();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		rootView = (RelativeLayout) mInflater.inflate(
				R.layout.myreward, null);
		return rootView;
	}

	public RelativeLayout getScreenParentLayout() {
		return rootView;
	}

	
	public void oncreat()
	{
	
		
//		mActivity.cPosition = 2;
//		mActivity.setPageTitle("MY REWARDS", "Reward Main Page");

		rewardBlur1 = (ImageView) rootView.findViewById(R.id.reward1);
		rewardBlur2 = (ImageView) rootView.findViewById(R.id.reward2);
		rewardBlur3 = (ImageView) rootView.findViewById(R.id.reward3);
//				numberOfEarned = (TextView) rootView
//				.findViewById(R.id.number_of_earned);
//		numberOfUntilNextEarned = (TextView) rootView
//				.findViewById(R.id.number_of_until_next_earned);

		rewardList = (LinearLayout) rootView
				.findViewById(R.id.reward_list_parent);
//		textNumberOfEarnedDesc = (TextView) rootView
//				.findViewById(R.id.number_of_earned_desc);
//		textNumberOfUntilNextEarnedDesc = (TextView) rootView
//				.findViewById(R.id.number_of_until_next_earned_desc);

		btnPromoCode = (ImageView) rootView.findViewById(R.id.btn_promo_code);
		

		TextView textSubTitle = (TextView) rootView.findViewById(R.id.text_sub_title);

		AppConstants.kingthingsTextView(textSubTitle, 25, AppConstants.COLOR_BLACK_PAGETITLE, mActivity.getAssets());
		TextView textSubTitle2 = (TextView) rootView.findViewById(R.id.text_sub_title2);
		
	
		RelativeLayout backarrow;
		backarrow=(RelativeLayout)rootView.findViewById(R.id.backarrow);
		backarrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Rewards.getInstance().onBackPressed();
			
			
			}
		});
		
		
		AppConstants.gothamNarrowBookTextView(textSubTitle2, 14, AppConstants.COLORRGRAY,mActivity.getAssets());
		TextView topTitle = (TextView) rootView
				.findViewById(R.id.topTitle);
				AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mActivity.getAssets());


//		AppConstants.fontAmaticBold(numberOfEarned, 35,
//				AppConstants.colorWhite, mActivity.getAssets());
//		AppConstants.fontAmaticBold(numberOfUntilNextEarned, 35,
//				AppConstants.colorWhite, mActivity.getAssets());
//		AppConstants.fontAmaticBold(textNumberOfEarnedDesc, 25,
//				AppConstants.colorWhite, mActivity.getAssets());
//		AppConstants.fontAmaticBold(textNumberOfUntilNextEarnedDesc, 25,
//				AppConstants.colorWhite, mActivity.getAssets());


		btnPromoCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//mActivity.setDisplayView(23);
Rewards.getInstance().showPrmocode("reward");
			}
		});

		if (RotiHomeActivity.getInstance().checkIfLogin()) {
			
		
		new fetchRewardsFromServer().execute("");
		}
	}

	private class fetchRewardsFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (mActivity != null && !mActivity.isFinishing()&&mActivity.getProgressDialog() != null
					&& !mActivity.getProgressDialog().isShowing())
				mActivity.getProgressDialog().show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String authToken = mActivity.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");

			// MyGoodies get reward @@@@@@@@@@@@@@
			result = WebHTTPMethodClass.httpGetServiceNCR("/rewards", //
					"auth_token=" + authToken + "&appkey="
							+ AppConstants.APPKEY + "&locale="
							+ AppConstants.LOCALE_HEADER_VALUE);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mActivity != null &&!mActivity.isFinishing()&&mActivity.getProgressDialog() != null
					&& mActivity.getProgressDialog().isShowing())
				mActivity.getProgressDialog().dismiss();
			rewardList.removeAllViews();

			int totalBox = 10;
			int totalMailstonePoints = 0;
			if (result != null && !result.equals("")) {

				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					String balance = resObject.getString("balance");
					JSONObject jsonBal = new JSONObject(balance);
					String mailstonePoints = jsonBal
							.getString("milestone_points");
					String points = jsonBal.getString("points");

					try {
						String rewardNotif = resObject
								.getString("new_reward_notification");

						showShareMsgDialog("", rewardNotif, mActivity);

					} catch (Exception e) {

					}

					if (points.contains("."))
						points = points.substring(0, points.indexOf("."));

					if (mailstonePoints.contains("."))
						mailstonePoints = mailstonePoints.substring(0,
								mailstonePoints.indexOf("."));

					totalMailstonePoints = Integer.parseInt(mailstonePoints);

					Log.i("elang", "elang totalMailstonePoints"
							+ mailstonePoints);

					Double ceilPoints = Math.ceil(Double
							.valueOf(mailstonePoints));

					if (totalMailstonePoints == 0) {
//						numberOfEarned.setText("0");
//						numberOfUntilNextEarned.setText("10");
//				
					} else if (totalMailstonePoints == 1) {
						//numberOfEarned.setText("1");
					//	numberOfUntilNextEarned.setText("9");
						rewardBlur1.setImageResource(R.drawable.rewards_dot_black);
					} else if (totalMailstonePoints == 2) {
						//numberOfEarned.setText("2");
						//numberOfUntilNextEarned.setText("8");
						rewardBlur1.setImageResource(R.drawable.rewards_dot_black);
						rewardBlur2.setImageResource(R.drawable.rewards_dot_yellow);
					} else if (totalMailstonePoints == 3) {
						//numberOfEarned.setText("3");
						//numberOfUntilNextEarned.setText("7");
						rewardBlur1.setImageResource(R.drawable.rewards_dot_black);
						rewardBlur2.setImageResource(R.drawable.rewards_dot_yellow);
						rewardBlur3.setImageResource(R.drawable.rewards_dot_red);
					} 
					try {
						mTotalPoint = Double.parseDouble(points);
					} catch (Exception e) {
					}

					if (sucess.equalsIgnoreCase("true")) {

						JSONArray jsonArray = resObject.getJSONArray("rewards");
						listRewardsBean = new ArrayList<RewardsBean>();

						for (int i = 0; i < jsonArray.length(); i++) {

							RewardsBean myRewardsBean = new RewardsBean();
							myRewardsBean.setPOSCode(jsonArray.getJSONObject(i)
									.getString("POSCode"));
							myRewardsBean.setChain_id(jsonArray
									.getJSONObject(i).getString("chain_id"));
							myRewardsBean.setEffectiveDate(jsonArray
									.getJSONObject(i)
									.getString("effectiveDate"));
							myRewardsBean.setExpiryDate(jsonArray
									.getJSONObject(i).getString("expiryDate"));
							myRewardsBean.setFineprint(jsonArray.getJSONObject(
									i).getString("fineprint"));
							myRewardsBean.setId(jsonArray.getJSONObject(i)
									.getString("id"));
							myRewardsBean.setName(jsonArray.getJSONObject(i)
									.getString("name"));
							myRewardsBean.setPoints(jsonArray.getJSONObject(i)
									.getString("points"));
							myRewardsBean.setReward_type(jsonArray
									.getJSONObject(i).getString("reward_type"));
							myRewardsBean.setSurvey_id(jsonArray.getJSONObject(
									i).getString("survey_id"));
							myRewardsBean.setExpired(jsonArray.getJSONObject(i)
									.getString("expired"));
							myRewardsBean.setSort_by_id(jsonArray
									.getJSONObject(i).getString("sort_by_id"));
							myRewardsBean.setGifter(jsonArray.getJSONObject(i)
									.getString("gifter"));
							myRewardsBean.setImageURL(jsonArray
									.getJSONObject(i).getString("image_url"));
							JSONArray additionalFieldsArray = jsonArray
									.getJSONObject(i).getJSONArray(
											"additional_fields");

							if (additionalFieldsArray.length() == 0)
								myRewardsBean.setIsAdditionalInfoExist(false);
							else {

								myRewardsBean.setIsAdditionalInfoExist(true);

								myRewardsBean
										.setArrayLengthOfAdditionalInfo(additionalFieldsArray
												.length());
								for (int j = 0; j < additionalFieldsArray
										.length(); j++) {
									myRewardsBean.setAddInfoAppText(
											additionalFieldsArray
													.getJSONObject(j)
													.getString("app_text"), j);
									myRewardsBean.setAddInfoDescription(
											additionalFieldsArray
													.getJSONObject(j)
													.getString("description"),
											j);
									myRewardsBean.setAddInfoId(
											additionalFieldsArray
													.getJSONObject(j)
													.getString("id"), j);
									myRewardsBean.setAddInfoTitle(
											additionalFieldsArray
													.getJSONObject(j)
													.getString("title"), j);
								}
							}
							listRewardsBean.add(myRewardsBean);
						}

						if (listRewardsBean != null
								&& listRewardsBean.size() > 0) {
							createRewardView(listRewardsBean);
						}
					}

				} catch (Exception e) {
					AppConstants.showMsgDialog(
							"Alert",
							AppConstants.ERRORFAILEDAPI + " -> "
									+ e.getMessage(), mActivity);
					e.printStackTrace();
				}
			} else
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mActivity);
			AppConstants.parseInput(result, mActivity);
		}
	}

	public void createRewardView(ArrayList<RewardsBean> listRewardsBean) {
		//Toast.makeText(mActivity, String.valueOf(listRewardsBean.size()),Toast.LENGTH_LONG).show();
		
		for (int i = 0; i < listRewardsBean.size(); i++) {

			
			RewardsBean myRewardsBean = listRewardsBean.get(i);

			LinearLayout cellViewMainLayout = (LinearLayout) mInflater.inflate(
					R.layout.reward_list, null);

			final TextView titleTextView = (TextView) cellViewMainLayout
					.findViewById(R.id.enjoy_mygodie_reward_list_text_title);

			final TextView dateTextView = (TextView) cellViewMainLayout
					.findViewById(R.id.enjoy_mygodie_reward_list_text_date);
			final TextView dateText = (TextView) cellViewMainLayout
					.findViewById(R.id.enjoy_mygodie_reward_list_date);
			dateText.setVisibility(View.GONE);
AppConstants.gothamNarrowMediumTextView(titleTextView, 16, AppConstants.COLOR_LIGHT_GRAYBLACK,  mActivity.getAssets());

AppConstants.gothamNarrowBookTextView(dateText, 14, AppConstants.COLOR_LIGHT_GRAYBLACK,  mActivity.getAssets());

AppConstants.gothamNarrowBookTextView(dateTextView, 14, AppConstants.COLOR_LIGHT_GRAYBLACK,  mActivity.getAssets());


//			AppConstants.fontAmaticBold(titleTextView, 20,
//					AppConstants.colorWhite, mActivity.getAssets());
//			AppConstants.fontAmaticBold(dateTextView, 20,
//					AppConstants.colorWhite, mActivity.getAssets());
//
//			AppConstants.fontAmaticBold(dateText, 16, AppConstants.colorWhite,
//					mActivity.getAssets());

			RelativeLayout cellParent = (RelativeLayout) cellViewMainLayout
					.findViewById(R.id.enjoy_mygodie_reward_list_parent_reward);

			//cellParent.setBackgroundResource(R.drawable.btn_reward_disable); // set
			// for
			// expired
			// rewards

			String points = myRewardsBean.getPoints();
			// points = "800.0";
			if (points.contains("."))
				points = points.substring(0, points.indexOf("."));
			titleTextView.setText("" + myRewardsBean.getName());

			double point = 0;
			try {
				point = Double.parseDouble(points);
			} catch (Exception e) {
			}
			if (!myRewardsBean.getExpired().equals("true")
					&& point <= mTotalPoint) {
				//cellParent.setBackgroundResource(R.drawable.btn_reward);
				; // active rewards
				dateTextView.setVisibility(View.VISIBLE);
				myRewardsBean.setExpirestate("reward");
				try {

					if (points.equals("0")) {
						dateTextView.setText("FREE");

						try {
							String deadline = myRewardsBean.getExpiryDate();
							java.util.Date d1 = AppConstants.makeDate(deadline);
							SimpleDateFormat curFormater = new SimpleDateFormat(
									"MM/dd/yyyy");// "MM-dd-yy"
							String currentTime = curFormater.format(d1);
							dateText.setText("EXP: " + currentTime);
							dateText.setVisibility(View.VISIBLE);
						} catch (Exception e) {

						}
					} else
						dateTextView.setText(points + " FRUITS");

					dateTextView.setVisibility(View.INVISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (myRewardsBean.getExpired().equals("false")) {
				myRewardsBean.setExpirestate("expired");
				dateTextView.setText(points + " FRUITS");
				dateTextView.setVisibility(View.VISIBLE);
			} else if (myRewardsBean.getGifter().equals("true")) {

			} else if (myRewardsBean.getGifter().equals("false")) {
			} else {
				// myRewardsBean.setExpirestate("expired");
				// dateTextView.setText("EXPIRED");
				//
				// AppConstants.fontAmaticBold(titleTextView, 20,
				// AppConstants.colorWhite, mActivity.getAssets());
				// AppConstants.fontAmaticBold(dateTextView, 13,
				// AppConstants.colorWhite, mActivity.getAssets());
				// AppConstants.fontAmaticBold(dateText, 9,
				// AppConstants.colorWhite, mActivity.getAssets());
				// dateTextView.setVisibility(View.VISIBLE);

				myRewardsBean.setExpirestate("expire");

				try {
					dateTextView.setVisibility(View.VISIBLE);
					String deadline = myRewardsBean.getExpiryDate();
					java.util.Date d1 = AppConstants.makeDate(deadline);
					SimpleDateFormat curFormater = new SimpleDateFormat("M/dd");// "MM-dd-yy"
					String currentTime = curFormater.format(d1);
					dateTextView.setText("" + currentTime);
					myRewardsBean.setExpirestate("expire");
					dateTextView.setText("EXPIRED");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			dateTextView.setTag(myRewardsBean);
			cellParent.setTag(dateTextView);
			dateTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					RewardsBean myRewardsBean = (RewardsBean) v.getTag();
					setDeleteButtonState(myRewardsBean, (TextView) v);
				}
			});
			cellParent.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					TextView dateTextView = (TextView) v.getTag();
					RewardsBean myRewardsBean = (RewardsBean) dateTextView
							.getTag();
					setDeleteButtonState(myRewardsBean, dateTextView);
				}
			});

			rewardList.addView(cellViewMainLayout);

		}
	}

	private void setDeleteButtonState(RewardsBean myGoodieRewardsBean,
			TextView dateTextView) {
		if (myGoodieRewardsBean.getExpired().equals("true")
				&& myGoodieRewardsBean.getExpirestate().equals("expire")) {
			Log.i("elang", "elang delete 1");
			dateTextView.setText("DELETE");

			// AppConstants.americanTypewriterTextView(dateTextView, 14,
			// AppConstants.COLORGREYCORNERBAKERY2, mHomePage.getAssets());
			myGoodieRewardsBean.setExpirestate("delete");
		} else if (myGoodieRewardsBean.getExpired().equals("true")
				&& myGoodieRewardsBean.getExpirestate().equals("delete")) {
			if (mActivity.checkIfLogin()) {
				new deleteRewardFromServer().execute(myGoodieRewardsBean
						.getId()/* "" */);
			} else {
				//mActivity.setDisplayView(99);
			}
		} else if (!myGoodieRewardsBean.getExpired().equals("true")
				&& myGoodieRewardsBean.getExpirestate().equals("reward")) {

			mActivity.startGPS();
			if (AppConstants.isNetworkAvailable(mActivity)) {
				if (mActivity.checkIfLogin()) {
					
					
					// Rewards.getInstance().showClaimRewardPage(
					// myGoodieRewardsBean);
					if (myGoodieRewardsBean.getGifter().equals("true")) {
						// Rewards.getInstance().showClaimRewardPageGifter(
						// myGoodieRewardsBean, "true");
						// has been changed by me
						// mHomePage.showFriendGoodiesPage(myGoodieRewardsBean);
					} else {
						Rewards.getInstance().showClaimRewardPage(myGoodieRewardsBean);
//						mActivity.setMyRewardsBean(myGoodieRewardsBean);
//						mActivity.setDisplayView(21);
//					
					}

				} else {
					//mActivity.setDisplayView(99);
				}
			} else
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORNETWORKCONNECTION, mActivity);

		} else if (!myGoodieRewardsBean.getExpired().equals("true")
				&& myGoodieRewardsBean.getExpirestate().equals("reward")) {

			mActivity.startGPS();
			if (myGoodieRewardsBean.getGifter().equals("true")) {
				// Rewards.getInstance().showClaimRewardPageGifter(
				// myGoodieRewardsBean, "true");
				// has been changed by me
				// mHomePage.showFriendGoodiesPage(myGoodieRewardsBean);
			} else {
				Rewards.getInstance().showClaimRewardPage(myGoodieRewardsBean);

//				mActivity.setMyRewardsBean(myGoodieRewardsBean);
//				mActivity.setDisplayView(21);
		}
			// mHomePage.showClaimRewardPage(myGoodieRewardsBean);

		}
	}

	private class deleteRewardFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (mActivity != null &&!mActivity.isFinishing()&& mActivity.getProgressDialog() != null
					&& !mActivity.getProgressDialog().isShowing())
				mActivity.getProgressDialog().show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String authToken = mActivity.getPreference().getString(
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
			if (mActivity != null &&!mActivity.isFinishing()&& mActivity.getProgressDialog() != null
					&& mActivity.getProgressDialog().isShowing())
				mActivity.getProgressDialog().dismiss();

			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					if (sucess.equalsIgnoreCase("true")) {
						if (AppConstants.isNetworkAvailable(mActivity)) {
							if (mActivity.checkIfLogin()) {
								new fetchRewardsFromServer().execute("");
							} else {
								//mActivity.setDisplayView(99);
								// mHomePage.showLoginOptionPage(false);
							}
						} else
							AppConstants.showMsgDialog("Alert",
									AppConstants.ERRORNETWORKCONNECTION,
									mActivity);
					} else
						AppConstants.parseInput(result, mActivity);
				} catch (Exception e) {
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORFAILEDAPI, mActivity);
					e.printStackTrace();
				}
			} else {
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mActivity);
				AppConstants.parseInput(result, mActivity);
			}
		}
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
