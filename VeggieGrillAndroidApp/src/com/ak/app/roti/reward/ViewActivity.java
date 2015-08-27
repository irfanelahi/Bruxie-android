package com.ak.app.roti.reward;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.app.roti.bean.RewardActivity;
import com.akl.app.roti.bean.RewardClaim;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class ViewActivity {
	private static ViewActivity screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;

	// private ProgressBar greenProgressBar;

	public static ViewActivity getInstance() {
		if (screen == null)
			screen = new ViewActivity();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.viewactivity, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	boolean isInfo = false;
	// TextView activityText;
	// TextView claimedText;
	TextView dateText;
	TextView pointsText;
	LinearLayout mParntReward;

	public void onCreate() {
		mHomePage = Tabbars.getInstance();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "View activity").build());

		// greenProgressBar = (ProgressBar) mParentLayout
		// .findViewById(R.id.greenProgressBar);

		// activityText = (TextView) mParentLayout
		// .findViewById(R.id.viewactivity_text_activity);
		// claimedText = (TextView) mParentLayout
		// .findViewById(R.id.viewactivity_text_claimed);
		dateText = (TextView) mParentLayout
				.findViewById(R.id.viewactivity_text_date);
		pointsText = (TextView) mParentLayout
				.findViewById(R.id.viewactivity_text_points);
		mParntReward = (LinearLayout) mParentLayout
				.findViewById(R.id.viewactivity_linear_list_parent);
		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.odinRoundedBoldTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());

		// TextView backText = (TextView) mParentLayout
		// .findViewById(R.id.viewactivity_text_back);
		// backText.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Rewards.getInstance().showRewardPage();
		// }
		// });
		// activityText.setBackgroundResource(R.drawable.activity_btn_active);
		// claimedText.setBackgroundResource(R.drawable.claimed_btn);
		dateText.setText("Date");
		pointsText.setText("Points");
		// activityText.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// dateText.setText("Date");
		// mParntReward.removeAllViews();
		// activityText
		// .setBackgroundResource(R.drawable.activity_btn_active);
		// claimedText.setBackgroundResource(R.drawable.claimed_btn);
		// if (listRewardActivityBean != null
		// && listRewardActivityBean.size() > 0) {
		// createActivityDetailView(listRewardActivityBean);
		// }
		// }
		// });
		// claimedText.setOnClickListener(new OnClickListener() {s

		// Create Font for every textview and buttons;
		AppConstants.fontGothamBoldTextView(dateText, 16,
				AppConstants.COLORORANGETEXTRGB, mHomePage.getAssets());// fontdinmediumTextViewBold
		AppConstants.fontGothamBoldTextView(pointsText, 16,
				AppConstants.COLORORANGETEXTRGB, mHomePage.getAssets());
		if (AppConstants.isNetworkAvailable(mHomePage)) {
			if (mHomePage.checkIfLogin()) {
				new fetchClaimedViewFromServer().execute("");
			} else {
				// mHomePage.showLoginOptionPage(false);
			}
		} else
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, mHomePage);
	}

	public List<RewardActivity> listRewardActivityBean;
	public List<RewardClaim> listRewardClaimBean;

	private class fetchClaimedViewFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// greenProgressBar.setVisibility(View.VISIBLE);
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
			// greenProgressBar.setVisibility(View.GONE);
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().dismiss();
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
							if (jsonobjreward.getString("points").equals("0"))
								rewardClaimBean.setPoints("Free");
							else
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
						if (listRewardClaimBean != null
								&& listRewardClaimBean.size() > 0) {
							// createActivityDetailView(listRewardClaimBean);
							createClaimDetailView(listRewardClaimBean);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORFAILEDAPI, mHomePage);
				}
			} else
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mHomePage);
			AppConstants.parseInput(result, mHomePage);
		}
	}

	private void createClaimDetailView(List<RewardClaim> listRewardClaimBean) {
		mParntReward.removeAllViews();
		if (listRewardClaimBean != null && listRewardClaimBean.size() > 0) {
			for (int i = 0; i < listRewardClaimBean.size(); i++) {
				RewardClaim rewardClaimBean = listRewardClaimBean.get(i);
				RelativeLayout cellViewMainLayout = (RelativeLayout) mInflater
						.inflate(R.layout.viewactivity_list, null);
				TextView titleTextView = (TextView) cellViewMainLayout
						.findViewById(R.id.viewactivity_list_text_date);
//				TextView ListDateTextView = (TextView) cellViewMainLayout
//						.findViewById(R.id.viewactivity_list_date);
//			
				final TextView dateTextView = (TextView) cellViewMainLayout
						.findViewById(R.id.viewactivity_list_text_points);
				titleTextView.setText("");
				dateTextView.setText("");

				AppConstants.americanTypewriterTextView(titleTextView, 16,
						AppConstants.COLORGREY, mHomePage.getAssets());
				AppConstants.americanTypewriterTextView(dateTextView, 16,
						AppConstants.COLORGREY, mHomePage.getAssets());
//				AppConstants.americanTypewriterTextView(ListDateTextView, 13,
//						AppConstants.COLORGREY, mHomePage.getAssets());
//				
				titleTextView.setText(rewardClaimBean.getName());
			//	ListDateTextView.setText(rewardClaimBean.getClaim_date());
				dateTextView.setText(rewardClaimBean.getPoints());
				mParntReward.addView(cellViewMainLayout);
				if (i < listRewardClaimBean.size() - 1)
					createSeqUnderLine(mParntReward);
			}
		}
	}

	static AlertDialog.Builder alertDialogBuilder;

	public static void showMessageDialog(String title, final String message) {
		try {
			if (alertDialogBuilder == null) {
				alertDialogBuilder = new AlertDialog.Builder(
						Tabbars.getInstance());
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createSeqUnderLine(LinearLayout mainLinearLayout) {
		RelativeLayout underlineView = (RelativeLayout) mInflater.inflate(
				R.layout.dottedlineview, null);
		mainLinearLayout.addView(underlineView);
	}
}
