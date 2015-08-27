package com.ak.app.roti.reward;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.R;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.roti.info.PromoPage;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;

public class PaymentAcivityFragment  {

	private Tabbars mActivity;
	private TextView textActivity;
	private TextView textRedeem;
	private LinearLayout btnTextRedeem;
	private LinearLayout btnTextActivity;
	private ImageView imageActivityLine;
	private ImageView imageRedeemLine;
	private static PaymentAcivityFragment screen;
	private RelativeLayout rootView;
	

	private LinearLayout mParntActivityList;
	private LayoutInflater mInflater;

	public List<RewardActivity> listRewardActivityBean;
	public List<RewardClaim> listRewardClaimBean;

	
	public static PaymentAcivityFragment getInstance() {
		if (screen == null)
			screen = new PaymentAcivityFragment();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		rootView = (RelativeLayout) mInflater.inflate(
				R.layout.fragment_payment_activity, null);
		return rootView;
	}

	public RelativeLayout getScreenParentLayout() {
		return rootView;
	}

	
	
	
	
	public void oncreat()
	{
	
	
	
	
		mActivity = Tabbars.getInstance();

		
		textActivity = (TextView) rootView.findViewById(R.id.text_activity);
		textRedeem = (TextView) rootView.findViewById(R.id.text_redeem);
		btnTextActivity = (LinearLayout) rootView
				.findViewById(R.id.btn_text_activity);
		btnTextRedeem = (LinearLayout) rootView
				.findViewById(R.id.btn_text_redeem);
		imageActivityLine = (ImageView) rootView
				.findViewById(R.id.image_activity_line);
		imageRedeemLine = (ImageView) rootView
				.findViewById(R.id.image_redeem_line);
		mParntActivityList = (LinearLayout) rootView
				.findViewById(R.id.viewactivity_linear_list_parent);
		
		TextView topTitle = (TextView) rootView
				.findViewById(R.id.topTitle);
		AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mActivity.getAssets());


		RelativeLayout backarrow;
		backarrow=(RelativeLayout)rootView.findViewById(R.id.backarrow);
		backarrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Info.getInstance().onBackPressed();
			
			}
		});
		


//		AppConstants.fontrobotoCondensedRegular(textActivity, 15,
//				AppConstants.colorOrange, mActivity.getAssets());
//		AppConstants.fontrobotoCondensedRegular(textRedeem, 15,
//				AppConstants.colorGrey, mActivity.getAssets());
//		
		
		imageActivityLine.setImageResource(R.drawable.ac_activity_on);
		imageRedeemLine.setImageResource(R.drawable.ac_activity_line);

		btnTextActivity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				AppConstants.fontrobotoCondensedRegular(textActivity, 15,
//						AppConstants.colorOrange, mActivity.getAssets());
//				AppConstants.fontrobotoCondensedRegular(textRedeem, 15,
//						AppConstants.colorGrey, mActivity.getAssets());
//			
				imageActivityLine.setImageResource(R.drawable.ac_activity_on);
				imageRedeemLine.setImageResource(R.drawable.ac_activity_line);

				mParntActivityList.removeAllViews();

				if (listRewardActivityBean != null
						&& listRewardActivityBean.size() > 0) {
					createActivityDetailView(listRewardActivityBean);
				}
			}
		});

		btnTextRedeem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				AppConstants.fontrobotoCondensedRegular(textActivity, 15,
//						AppConstants.colorGrey, mActivity.getAssets());
//				AppConstants.fontrobotoCondensedRegular(textRedeem, 15,
//						AppConstants.colorOrange, mActivity.getAssets());
				imageRedeemLine.setImageResource(R.drawable.ac_activity_on);
				imageActivityLine.setImageResource(R.drawable.ac_activity_line);

				mParntActivityList.removeAllViews();
				if (listRewardClaimBean != null
						&& listRewardClaimBean.size() > 0) {
					createClaimDetailView(listRewardClaimBean);
				}
			}
		});

		if (AppConstants.isNetworkAvailable(mActivity)) {

			new fetchViewActivityServer().execute("");

		} else
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, mActivity);

		
	}

	public String getMonth(int month) {
		return new DateFormatSymbols().getShortMonths()[month - 1];
	}

	private class fetchViewActivityServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (mActivity != null && mActivity.getProgressDialog() != null)
				mActivity.getProgressDialog().show();
		}

		@Override
		protected String doInBackground(String... params) {
			String authToken = mActivity.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
		
			String result = WebHTTPMethodClass.httpGetService("/user/activity",
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
			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					if (sucess.equalsIgnoreCase("true")) {
						JSONArray jsonArray = resObject
								.getJSONArray("receipts");
						listRewardActivityBean = new ArrayList<RewardActivity>();
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonobj = jsonArray.getJSONObject(i);
							String jsonStr = jsonobj
									.getString("last_transaction");
							JSONObject jsonobjlast_transaction = new JSONObject(
									jsonStr);

							RewardActivity rewardActivityBean = new RewardActivity();

							rewardActivityBean.setId(jsonobj.getString("id"));
							SimpleDateFormat curFormater = new SimpleDateFormat(
									"yyyy-MM-dd");
							try {
								Date dateObj = curFormater
										.parse(jsonobjlast_transaction
												.getString("created_at"));
								SimpleDateFormat postFormater = new SimpleDateFormat(
										"MM-dd-yy");
								String newDateStr = postFormater
										.format(dateObj);
								rewardActivityBean.setCreated_at(newDateStr);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							rewardActivityBean
									.setStatus(jsonobjlast_transaction
											.getString("status"));
							rewardActivityBean
									.setTotal_points_earned(jsonobjlast_transaction
											.getString("total_points_earned"));
							Log.e("getTotal_points_earned ",
									rewardActivityBean.getTotal_points_earned()
											+ " : "
											+ jsonobjlast_transaction
													.getString("base_points_earned"));
							rewardActivityBean
									.setAdmin_id(jsonobjlast_transaction
											.getString("admin_id"));
							// rewardActivityBean.setId(jsonArray.getJSONObject(i)
							// .getString("id"));
							listRewardActivityBean.add(rewardActivityBean);
						}
						if (listRewardActivityBean != null
								&& listRewardActivityBean.size() > 0) {
							createActivityDetailView(listRewardActivityBean);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				new fetchClaimedViewFromServer().execute("");
			}
		}
	}

	private class fetchClaimedViewFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (mActivity != null && mActivity.getProgressDialog() != null
					&& !mActivity.getProgressDialog().isShowing())
				mActivity.getProgressDialog().show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String authToken = mActivity.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");

			// MyGoodies get reward @@@@@@@@@@@@@@
			result = WebHTTPMethodClass.httpGetService("/rewards/activity", //
					"auth_token=" + authToken + "&appkey="
							+ AppConstants.APPKEY);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			if (mActivity != null && mActivity.getProgressDialog() != null
					&& mActivity.getProgressDialog().isShowing())
				mActivity.getProgressDialog().dismiss();
			listRewardClaimBean = new ArrayList<RewardClaim>();
			//
			// mParntReward.removeAllViews();
			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					if (sucess.equalsIgnoreCase("true")) {
						JSONArray jsonArray = resObject
								.getJSONArray("activities");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonobj = jsonArray.getJSONObject(i);
							String jsonStr = jsonobj.getString("reward");
							JSONObject jsonobjreward = new JSONObject(jsonStr);

							RewardClaim rewardClaimBean = new RewardClaim();
							rewardClaimBean
									.setId(jsonobjreward.getString("id"));
							SimpleDateFormat curFormater = new SimpleDateFormat(
									"yyyy-MM-dd");
							try {
								Date dateObj = curFormater.parse(jsonobjreward
										.getString("created_at"));
								SimpleDateFormat postFormater = new SimpleDateFormat(
										"MM-dd-yy");
								String newDateStr = postFormater
										.format(dateObj);
								rewardClaimBean.setCreated_at(newDateStr);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							rewardClaimBean.setName(jsonobjreward
									.getString("name"));
							rewardClaimBean.setPoints(jsonobjreward
									.getString("points"));
							rewardClaimBean.setChain_id(jsonobjreward
									.getString("chain_id"));
							try {
								Date dateObj = curFormater.parse(jsonobj
										.getString("claim_date"));
								SimpleDateFormat postFormater = new SimpleDateFormat(
										"MM-dd-yy");
								String newDateStr = postFormater
										.format(dateObj);
								rewardClaimBean.setClaim_date(newDateStr);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							listRewardClaimBean.add(rewardClaimBean);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			AppConstants.parseInput(result, mActivity);
		}
	}

	public void createActivityDetailView(
			List<RewardActivity> listRewardActivityBean) {
		mParntActivityList.removeAllViews();
		if (listRewardActivityBean != null && listRewardActivityBean.size() > 0) {
			for (int i = 0; i < listRewardActivityBean.size(); i++) {
				RewardActivity rewardClaimBean = listRewardActivityBean.get(i);
				RelativeLayout cellViewMainLayout = (RelativeLayout) mInflater
						.inflate(R.layout.viewactivity_list, null);
				TextView titleTextView = (TextView) cellViewMainLayout
						.findViewById(R.id.viewactivity_list_text_date);
				final TextView pointsTextView = (TextView) cellViewMainLayout
						.findViewById(R.id.viewactivity_list_text_points);

				TextView addressTextView = (TextView) cellViewMainLayout
						.findViewById(R.id.viewactivity_list_text_date_description);

				TextView statusTextView = (TextView) cellViewMainLayout
						.findViewById(R.id.viewactivity_list_text_description);

//				AppConstants.fontAmaticBold(titleTextView, 25,
//						AppConstants.colorOrange, mActivity.getAssets());
//				AppConstants.fontAmaticBold(pointsTextView, 25,
//						AppConstants.colorBlack, mActivity.getAssets());
//				AppConstants.fontAmaticBold(addressTextView, 25,
//						AppConstants.colorBlack, mActivity.getAssets());
//				AppConstants.fontAmaticBold(statusTextView, 25,
//						AppConstants.colorBlack, mActivity.getAssets());

				titleTextView.setText("");
				pointsTextView.setText("");
				addressTextView.setText("");
				statusTextView.setText("");

				DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
				final Calendar calendar = Calendar.getInstance();
				try {
					calendar.setTime(df.parse(rewardClaimBean.getCreated_at()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				int m = calendar.get(Calendar.MONTH) + 1;
				int d = calendar.get(Calendar.DATE);

				if (rewardClaimBean.gettype().equalsIgnoreCase("payments")) {

					titleTextView
							.setText(getMonth(m) + " " + String.valueOf(d));
					pointsTextView.setText("- $" + rewardClaimBean.getAmount());
					addressTextView.setText("" + rewardClaimBean.getAddress());
					statusTextView.setText("Purchase");

				} else {
					// titleTextView
					// .setText(getMonth(m) + " " + String.valueOf(d));
					//
					// // addressTextView.setVisibility(View.GONE);
					// // statusTextView.setVisibility(View.GONE);
					//
					// if (rewardClaimBean.getStatus().equalsIgnoreCase("1")) {
					// pointsTextView.setText("- Received");
					// } else if (rewardClaimBean.getStatus()
					// .equalsIgnoreCase("2")) {
					// pointsTextView.setText("- Pending");
					// } else if (rewardClaimBean.getStatus()
					// .equalsIgnoreCase("3")) {
					// pointsTextView.setText("- "
					// + rewardClaimBean.getTotal_points_earned());
					// } else {
					// pointsTextView.setText("- Rejected");
					// }
				}

				mParntActivityList.addView(cellViewMainLayout);
				// if (i < listRewardActivityBean.size() - 1)
				// createSeqUnderLine(mParntActivityList);
			}
		}
	}

	private void createClaimDetailView(List<RewardClaim> listRewardClaimBean) {
		mParntActivityList.removeAllViews();
		if (listRewardClaimBean != null && listRewardClaimBean.size() > 0) {
			for (int i = 0; i < listRewardClaimBean.size(); i++) {
				RewardClaim rewardClaimBean = listRewardClaimBean.get(i);
				RelativeLayout cellViewMainLayout = (RelativeLayout) mInflater
						.inflate(R.layout.viewactivity_list, null);
				TextView titleTextView = (TextView) cellViewMainLayout
						.findViewById(R.id.viewactivity_list_text_date);
				final TextView dateTextView = (TextView) cellViewMainLayout
						.findViewById(R.id.viewactivity_list_text_points);
				titleTextView.setText("");
				dateTextView.setText("");

//				AppConstants.fontAmaticBold(titleTextView, 25,
//						AppConstants.colorOrange, mActivity.getAssets());
//				AppConstants.fontAmaticBold(dateTextView, 25,
//						AppConstants.colorBlack, mActivity.getAssets());

				DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
				final Calendar calendar = Calendar.getInstance();
				try {
					calendar.setTime(df.parse(rewardClaimBean.getClaim_date()));
				} catch (ParseException e) {
					e.printStackTrace();
				}

				int m = calendar.get(Calendar.MONTH) + 1;
				int d = calendar.get(Calendar.DATE);

				titleTextView.setText(getMonth(m) + " " + String.valueOf(d));
				dateTextView.setText("- " + rewardClaimBean.getName());
				dateTextView.setGravity(Gravity.CENTER);
				mParntActivityList.addView(cellViewMainLayout);
				// if (i < listRewardClaimBean.size() - 1)
				// createSeqUnderLine(mParntReward);
			}
		}
	}
}
