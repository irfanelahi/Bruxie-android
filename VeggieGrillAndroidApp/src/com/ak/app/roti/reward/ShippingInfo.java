package com.ak.app.roti.reward;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.ak.app.cb.activity.Rewards;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.app.roti.bean.MyGoodieRewardsBean;
import com.akl.app.roti.bean.Surveybean;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.RefreshListner;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;
import com.google.gson.Gson;

public class ShippingInfo implements RefreshListner {
	private static ShippingInfo screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;
	private ImageView submit;
	private LinkedList<String> choiceId = new LinkedList<String>();
	private LinkedList<String> choiceString = new LinkedList<String>();
	private LinkedList<String> choiceQuestion = new LinkedList<String>();
	private JSONObject responseJson;
	private JSONArray responseArray;
	private Surveybean surveyArray[];
	private Gson gson = new Gson();
	private int textViewType;
	private String questionsId[];
	private String answersId[];
	private EditText ans[];
	private Spinner dropdown;
	private String surveyId = "";
	private String receiptId = "";
	private String questId = "", ansId = "";
	private String selecteditem;
	private ProgressBar pageProgressBar;
	private MyGoodieRewardsBean mMyGoodieRewardsBean;

	public static ShippingInfo getInstance() {
		if (screen == null)
			screen = new ShippingInfo();
		return screen;
	}

	public static void setInstance() {
		screen = null;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.info_shipping, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	LinearLayout mShippingInfoLinearLayout;

	public void onCreate(final MyGoodieRewardsBean myGoodieRewardsBean,
			final String rewardLocateid) {
		mHomePage = Tabbars.getInstance();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Shipping Info Page").build());

		mMyGoodieRewardsBean = myGoodieRewardsBean;

		mShippingInfoLinearLayout = (LinearLayout) mParentLayout
				.findViewById(R.id.login_linear_parent);

		TextView pageTitle = (TextView) mParentLayout
				.findViewById(R.id.pageTitle);

		pageProgressBar = (ProgressBar) mParentLayout
				.findViewById(R.id.pageLoadingIndicator);

		submit = (ImageView) mParentLayout
				.findViewById(R.id.shippping_info_enter);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new sendShippingInfoToServer().execute(rewardLocateid);
			}
		});

		AppConstants.fontChangingMediumTextView(pageTitle, 50,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());
		// submit.setBackgroundResource(R.drawable.enter_btn_idle);
		submit.setEnabled(false);
		showShippingFields(myGoodieRewardsBean);
	}

	public static void showConfirmMsgDialog(String title, final String message,
			final Context context, final String rewardLocationId) {
		try {
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
			alt_bld.setMessage(message)
					.setCancelable(false)
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							})
					.setPositiveButton("Continue",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									ShippingInfo.getInstance().new confirmingShippingInfo()
											.execute(rewardLocationId);

								}
							});
			AlertDialog alert = alt_bld.create();
			// alert.setTitle(title);
			alert.setTitle(title);
			alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
			alert.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void showMsgDialog(String title, final String message,
			Context context) {
		try {
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
			alt_bld.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									TabHost tabHost = (TabHost) Rewards
											.getInstance().getParent()
											.findViewById(android.R.id.tabhost);
									if (tabHost != null) {
										tabHost.setCurrentTab(2);
										tabHost.setCurrentTab(3);
									}
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

	@Override
	public void notifyRefresh(String className) {
	}

	private void showShippingFields(
			final MyGoodieRewardsBean myGoodieRewardsBean) {
		final TextView pageDesc = new TextView(mHomePage);
		pageDesc.setText("Please provide valid contact info and shipping address below. Merchandise will be shipped to you in 2- 3 weeks. Information provided below cannot be edited.");

		LinearLayout choicesLayout = (LinearLayout) mParentLayout
				.findViewById(R.id.life_servey_helptable);

		LinearLayout.LayoutParams pageDescParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);// Ak
		pageDescParams.gravity = Gravity.CENTER_HORIZONTAL;
		pageDesc.setLayoutParams(pageDescParams);
		pageDesc.setGravity(Gravity.CENTER);
		AppConstants.fontMyriadMediumTextView(pageDesc, 16,
				AppConstants.COLORBLACKMUSHROOM, mHomePage.getAssets());

		choicesLayout.addView(pageDesc);

		// LinearLayout answers[] = new LinearLayout[myGoodieRewardsBean
		// .getArrayLengthOfAdditionalInfo()];

		ans = new EditText[myGoodieRewardsBean.getArrayLengthOfAdditionalInfo()];

		for (int i = 0; i < myGoodieRewardsBean
				.getArrayLengthOfAdditionalInfo(); i++) {
			ans[i] = new EditText(mHomePage);
			LinearLayout.LayoutParams paramms = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);// Ak
			// paramms.setMargins(30, 20, 30, 0);
			paramms.setMargins(0, 20, 0, 0);
			ans[i].setLayoutParams(paramms);
			ans[i].setFilters(new InputFilter[] { new InputFilter.LengthFilter(
					200) });
			ans[i].setGravity(Gravity.CENTER);
			ans[i].setEllipsize(TextUtils.TruncateAt.START);
			ans[i].setWidth(LayoutParams.WRAP_CONTENT);
			ans[i].setHeight(LayoutParams.WRAP_CONTENT);
			ans[i].setHint(myGoodieRewardsBean.getAddInfoAppText(i));
			ans[i].setFilters(new InputFilter[] { new InputFilter.LengthFilter(
					30) });
			ans[i].setBackgroundResource(R.drawable.promo_btn_gray);
			AppConstants.fontMyriadMediumTextView(ans[i], 18,
					AppConstants.COLORBLACKMUSHROOM, mHomePage.getAssets());

			// answers[j].setPadding(5, 0, 0, 0);
			// answers[i].addView(ans[i]);
			choicesLayout.addView(ans[i]);

			ans[i].addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					boolean isAllFilled = true;
					for (int j = 0; j < myGoodieRewardsBean
							.getArrayLengthOfAdditionalInfo(); j++) {
						if (ans[j].getText().toString().trim().length() == 0)
							isAllFilled = false;
					}
					if (isAllFilled) {
						// submit.setBackgroundResource(R.drawable.enter_btn);
						submit.setEnabled(true);
					} else {
						// submit.setBackgroundResource(R.drawable.enter_btn_idle);
						submit.setEnabled(false);
					}
				}
			});

			final int index = i;

			ans[i].setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus)
						ans[index].setBackgroundResource(R.drawable.formfield);
					else if (ans[index].getText().toString().equals(""))
						ans[index]
								.setBackgroundResource(R.drawable.promo_btn_gray);
				}
			});

		}

	}

	private class confirmingShippingInfo extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			pageProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			String rewardLocationId = params[0];
			String result = "";
			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");

			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
			nameparams
					.add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
			nameparams.add(new BasicNameValuePair("auth_token", authToken));
			nameparams.add(new BasicNameValuePair("reward_id",
					mMyGoodieRewardsBean.getId()));// id
			nameparams.add(new BasicNameValuePair("warn", "false"));
			nameparams.add(new BasicNameValuePair("lat", mHomePage
					.getGetLatLongObj().getLatitude() + ""));
			nameparams.add(new BasicNameValuePair("lng", mHomePage
					.getGetLatLongObj().getLongitude() + ""));
			nameparams
					.add(new BasicNameValuePair("location", rewardLocationId));// id
			for (int i = 0; i < mMyGoodieRewardsBean
					.getArrayLengthOfAdditionalInfo(); i++) {

				nameparams.add(new BasicNameValuePair("additional_info["
						+ mMyGoodieRewardsBean.getAddInfoId(i) + "]", ans[i]
						.getText().toString()));// id

			}
			result = WebHTTPMethodClass.executeHttPost("/rewards/claim",
					nameparams);

			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			pageProgressBar.setVisibility(View.GONE);
			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					if (resObject.getString("status").equals("true")) {
						String notice = resObject.getString("notice");
						showMsgDialog("", notice, mHomePage);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class sendShippingInfoToServer extends
			AsyncTask<String, Void, String> {

		private String rewardLocationId = "";

		@Override
		protected void onPreExecute() {
			pageProgressBar.setVisibility(View.VISIBLE);

		}

		@Override
		protected String doInBackground(String... params) {
			rewardLocationId = params[0];
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
			nameparams.add(new BasicNameValuePair("lat", mHomePage
					.getGetLatLongObj().getLatitude() + ""));
			nameparams.add(new BasicNameValuePair("lng", mHomePage
					.getGetLatLongObj().getLongitude() + ""));
			nameparams
					.add(new BasicNameValuePair("location", rewardLocationId));// id
			for (int i = 0; i < mMyGoodieRewardsBean
					.getArrayLengthOfAdditionalInfo(); i++) {

				nameparams.add(new BasicNameValuePair("additional_info["
						+ mMyGoodieRewardsBean.getAddInfoId(i) + "]", ans[i]
						.getText().toString()));// id

			}
			result = WebHTTPMethodClass.executeHttPost("/rewards/claim",
					nameparams);

			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			pageProgressBar.setVisibility(View.GONE);
			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String warnTitle = resObject.getString("warn_tile");
					String warnBody = resObject.getString("warn_body");
					showConfirmMsgDialog(warnTitle, warnBody, mHomePage,
							rewardLocationId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
