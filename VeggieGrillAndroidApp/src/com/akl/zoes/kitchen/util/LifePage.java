package com.akl.zoes.kitchen.util;
//package com.akl.zoes.kitchen.lifepage;
//
//import java.io.BufferedInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.content.res.AssetManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.AsyncTask;
//import android.preference.PreferenceManager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.akl.zoes.kitchen.activity.HomePage;
//import com.akl.zoes.kitchen.activity.R;
//import com.akl.zoes.kitchen.activity.R.drawable;
//import com.akl.zoes.kitchen.activity.R.id;
//import com.akl.zoes.kitchen.activity.R.layout;
//import com.akl.zoes.kitchen.bean.ThisWeekBean;
//import com.akl.zoes.kitchen.util.AppConstants;
//import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//
//public class LifePage {
//	private static LifePage screen;
//	private LinearLayout mParentLayout;
//	private LayoutInflater mInflater;
//	private HomePage mHomePage;
//
//	public static LifePage getInstance() {
//		if (screen == null)
//			screen = new LifePage();
//		return screen;
//	}
//
//	public LinearLayout setInflater(LayoutInflater inflater) {
//		mInflater = inflater;
//		LinearLayout lifePageMainLayout = (LinearLayout) mInflater.inflate(
//				R.layout.lifemainview, null);
//		mParentLayout = (LinearLayout) lifePageMainLayout
//				.findViewById(R.id.lifemainview_linear_parent);
//		return lifePageMainLayout;
//	}
//
//	public LinearLayout getScreenParentLayout() {
//		return mParentLayout;
//	}
//
//	public void onCreate() {
//		mParentLayout.removeAllViews();
//		mHomePage = HomePage.getInstance();
//		SharedPreferences mPreference = PreferenceManager
//				.getDefaultSharedPreferences(mHomePage);
//		boolean isCatSelected = mPreference.getBoolean(AppConstants.PREFCATEGORYSELECTED, false);
//		if(!isCatSelected)
//			createLifePageView();
//		else
//			createLifePageGoalView();
//	}
//
//	LinearLayout lifePageMainLayout;
//	LinearLayout lifePageCellContainerLayout;
//	List<ThisWeekBean> listThisWeekBean;
//
//	private void createLifePageView() {
//		lifePageMainLayout = (LinearLayout) mInflater.inflate(
//				R.layout.lifepage_thisweek, null);
//		lifePageCellContainerLayout = (LinearLayout) lifePageMainLayout
//				.findViewById(R.id.lifepage_thisweek_linear_parentview);
//		TextView chooseTextView = (TextView) lifePageMainLayout
//				.findViewById(R.id.lifepage_thisweek_text_choose);
//		// chooseTextView.setText("CHOOSE UPTO THREE");
//		AppConstants.fontHelveticaBoldTextView(chooseTextView, 18,
//				AppConstants.colorOrangeTextrgb, mHomePage.getAssets());
//		ImageView doneButtonView = (ImageView) lifePageMainLayout
//				.findViewById(R.id.lifepage_thisweek_button_done);
//		doneButtonView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (AppConstants.isNetworkAvailable(mHomePage)) {
//					String id = "";
//					if (listThisWeekBean != null && listThisWeekBean.size() > 0) {
//						for (int i = 0; i < listThisWeekBean.size(); i++) {
//							if (listThisWeekBean.size() > 1) {
//								id = id
//										+ listThisWeekBean.get(i)
//												.getCategoryId();
//								if (i < listThisWeekBean.size() - 1)
//									id = id + ",";
//							} else
//								id = id
//										+ listThisWeekBean.get(i)
//												.getCategoryId();
//						}
//					}
//					new submitSelectedCategoriesToServer().execute(id);
//				} else {
//					AppConstants.showMsgDialog("Alert",
//							"Please find a network connection", mHomePage);
//				}
//			}
//		});
//		if (AppConstants.isNetworkAvailable(mHomePage)) {
//			new fetchCategoriesFromServer().execute("");
//		} else
//			AppConstants.showMsgDialog("Alert",
//					"Please find a network connection", mHomePage);
//	}
//
//	private void createThisWeekView(List<ThisWeekBean> listThisWeekBean) {
//		for (int i = 0; i < listThisWeekBean.size(); i++) {
//			ThisWeekBean thisWeekBean = listThisWeekBean.get(i);
//			RelativeLayout cellViewMainLayout = (RelativeLayout) mInflater
//					.inflate(R.layout.lifepage_thisweek_goal_list, null);
//			TextView categoryNameTextView = (TextView) cellViewMainLayout
//					.findViewById(R.id.lifepage_thisweek_goal_list_text_option);
//			ImageView checkboxIM = (ImageView) cellViewMainLayout
//					.findViewById(R.id.lifepage_thisweek_goal_list_checkbox);
//			categoryNameTextView.setText(thisWeekBean.getCategoryName());
//			AppConstants.fontMyriadPro_BoldTextView(categoryNameTextView, 16,
//					AppConstants.colorBlackrgb, mHomePage.getAssets());
//			if (thisWeekBean.getIsSelected().equals("true"))
//				checkboxIM.setImageResource(R.drawable.checkmark);
//			else
//				checkboxIM.setImageResource(R.drawable.checkmark_icon);
//			checkboxIM.setTag(thisWeekBean);
//			checkboxIM.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					ThisWeekBean thisWeekBean = (ThisWeekBean) v.getTag();
//					ImageView checkboxIM = (ImageView) v;
//					if (thisWeekBean.getIsSelected().equals("true")) {
//						thisWeekBean.setIsSelected("false");
//						checkboxIM.setImageResource(R.drawable.checkmark_icon);
//					} else {
//						thisWeekBean.setIsSelected("true");
//						checkboxIM.setImageResource(R.drawable.checkmark);
//					}
//				}
//			});
//			lifePageCellContainerLayout.addView(cellViewMainLayout);
//			// if (i < 8 - 1)//billList.size()
//			createSeqUnderLine(lifePageCellContainerLayout);
//		}
//	}
//
//	RelativeLayout lifeGoalPageMainLayout;
//	LinearLayout lifeGoalPageCellContainerLayout;
//
//	private void createLifePageGoalView() {
//		lifeGoalPageMainLayout = (RelativeLayout) mInflater.inflate(
//				R.layout.lifepage_thisweek_goal, null);
//		lifeGoalPageCellContainerLayout = (LinearLayout) lifeGoalPageMainLayout
//				.findViewById(R.id.lifepage_thisweek_goal_linear_parentview);
//		TextView daysleftTextView = (TextView) lifePageMainLayout
//				.findViewById(R.id.lifepage_thisweek_goal_text_daysleft);
//		TextView daysleftTitleTextView = (TextView) lifePageMainLayout
//				.findViewById(R.id.lifepage_thisweek_goal_text_dayslefttitle);
//		AppConstants.fontHelveticaBoldTextView(daysleftTextView, 24,
//				AppConstants.colorOrangeHeaderrgb, mHomePage.getAssets());
//		AppConstants.fontHelveticaBoldTextView(daysleftTitleTextView, 18,
//				AppConstants.colorOrangeHeaderrgb, mHomePage.getAssets());
//		Button viewhistoryButtonView = (Button) lifeGoalPageMainLayout
//				.findViewById(R.id.lifepage_thisweek_goal_button_view_history);
//		viewhistoryButtonView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (AppConstants.isNetworkAvailable(mHomePage)) {
//					new fetchGoalshistoryFromServer().execute("");
//				} else {
//					AppConstants.showMsgDialog("Alert",
//							"Please find a network connection", mHomePage);
//				}
//			}
//		});
//		Button editgoalsButtonView = (Button) lifeGoalPageMainLayout
//				.findViewById(R.id.lifepage_thisweek_goal_button_edit_goals);
//		editgoalsButtonView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				createLifePageView();
//			}
//		});
//		if (AppConstants.isNetworkAvailable(mHomePage)) {
//			new fetchGoalsDeadlineFromServer().execute("");
//			new fetchGoalsFromServer().execute("");
//		} else
//			AppConstants.showMsgDialog("Alert",
//					"Please find a network connection", mHomePage);
//	}
//
//	private void createLifePageGoalView(List<ThisWeekBean> listThisWeekBean) {
//		for (int i = 0; i < listThisWeekBean.size(); i++) {
//			ThisWeekBean thisWeekBean = listThisWeekBean.get(i);
//			RelativeLayout cellViewLifePageMainLayout = (RelativeLayout) mInflater
//					.inflate(R.layout.lifepage_thisweek_goal_list, null);
//			TextView categoryNameTextView = (TextView) cellViewLifePageMainLayout
//					.findViewById(R.id.lifepage_thisweek_goal_list_text_option);
//			ImageView checkboxIM = (ImageView) cellViewLifePageMainLayout
//					.findViewById(R.id.lifepage_thisweek_goal_list_checkbox);
//			categoryNameTextView.setText(thisWeekBean.getCategoryName());
//			AppConstants.fontMyriadPro_BoldTextView(categoryNameTextView, 16,
//					AppConstants.colorBlackrgb, mHomePage.getAssets());
//			if (thisWeekBean.getIsSelected().equals("true"))
//				checkboxIM.setImageResource(R.drawable.checkmark);
//			else
//				checkboxIM.setImageResource(R.drawable.checkmark_icon);
//			checkboxIM.setTag(thisWeekBean);
//			checkboxIM.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					ThisWeekBean thisWeekBean = (ThisWeekBean) v.getTag();
//					ImageView checkboxIM = (ImageView) v;
//					if (thisWeekBean.getIsSelected().equals("true")) {
//						thisWeekBean.setIsSelected("false");
//						checkboxIM.setImageResource(R.drawable.checkmark_icon);
//					} else {
//						thisWeekBean.setIsSelected("true");
//						checkboxIM.setImageResource(R.drawable.checkmark);
//					}
//				}
//			});
//			lifeGoalPageCellContainerLayout.addView(cellViewLifePageMainLayout);
//			// if (i < 8 - 1)//billList.size()
//			createSeqUnderLine(lifeGoalPageCellContainerLayout);
//		}
//	}
//
//	public void createSeqUnderLine(LinearLayout mainLinearLayout) {
//		RelativeLayout underlineView = (RelativeLayout) mInflater.inflate(
//				R.layout.dottedlineview, null);
//		mainLinearLayout.addView(underlineView);
//	}
//
//	public static Drawable LoadBitmap(Context context, String filename) {
//		Drawable d = null;
//		try {
//			AssetManager assets = context.getResources().getAssets();
//			InputStream buf;
//			buf = new BufferedInputStream((assets.open("drawable/" + filename)));//
//			Bitmap bitmap = BitmapFactory.decodeStream(buf);
//			d = new BitmapDrawable(bitmap);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		// Drawable d = LoadBitmap(HomePage.getInstance(), "checkboxblack.png");
//		// chooseTextView.setCompoundDrawables(HomePage.getInstance().getResources().getDrawable
//		// (R.drawable.checkboxblack), null, null, null);
//		return d;
//	}
//
//	private class fetchCategoriesFromServer extends
//			AsyncTask<String, Void, String> {
//
//		@Override
//		protected void onPreExecute() {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//			String authToken = PreferenceManager.getDefaultSharedPreferences(
//					mHomePage).getString(AppConstants.PREFAUTH_TOKEN, "");
//			String result = WebHTTPMethodClass.httpGetService("/categories",
//					"auth_token=" + authToken + "&appkey="
//							+ AppConstants.APPKEY);
//			return result;
//		}
//
//		// {"status":true,"notice":"Your new password has been emailed to you.
//		// You should receive it in few minutes.","new_pass":"zb47nc"}
//		@Override
//		protected void onProgressUpdate(Void... values) {
//
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().dismiss();
//
//			lifePageCellContainerLayout.removeAllViews();
//			mParentLayout.removeAllViews();
//			if (result != null && !result.equals("")) {
//				try {
//					JSONObject resObject = new JSONObject(result);
//					String sucess = resObject.getString("status");
//					String notice = resObject.getString("notice");
//					if (sucess.equalsIgnoreCase("true")) {
//						// JSONObject responseObject = new JSONObject(result);
//						// result = responseObject.getString("result");
//						JSONArray jsonArray = resObject.getJSONArray(notice);
//						listThisWeekBean = new ArrayList<ThisWeekBean>();
//						for (int i = 0; i < jsonArray.length(); i++) {
//							ThisWeekBean thisWeekBean = new ThisWeekBean();
//							thisWeekBean.setCategoryName(jsonArray
//									.getJSONObject(i).getString("outlet_name"));
//							thisWeekBean.setIsSelected(jsonArray.getJSONObject(
//									i).getString("outlet_name"));
//							thisWeekBean.setCategoryId(jsonArray.getJSONObject(
//									i).getString("outlet_name"));
//							listThisWeekBean.add(thisWeekBean);
//						}
//						if (listThisWeekBean != null
//								&& listThisWeekBean.size() > 0) {
//							lifePageCellContainerLayout.removeAllViews();
//							createSeqUnderLine(lifePageCellContainerLayout);
//							createThisWeekView(listThisWeekBean);
//						}
//					}
//				} catch (Exception e) {
//
//				}
//			}
//			mParentLayout.addView(lifePageMainLayout);
//			parseInput(result);
//		}
//	}
//
//	private class submitSelectedCategoriesToServer extends
//			AsyncTask<String, Void, String> {
//
//		@Override
//		protected void onPreExecute() {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//
//			String authToken = PreferenceManager.getDefaultSharedPreferences(
//					mHomePage).getString(AppConstants.PREFAUTH_TOKEN, "");
//			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
//			nameparams
//					.add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
//			nameparams.add(new BasicNameValuePair("auth_token", authToken));
//			nameparams.add(new BasicNameValuePair("category_ids", params[0]));
//			String result = WebHTTPMethodClass.executeHttPost(
//					"/categories/select", nameparams);
//			return result;
//		}
//
//		@Override
//		protected void onProgressUpdate(Void... values) {
//
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().dismiss();
//			if (result != null && !result.equals("")) {
//				try {
//					JSONObject resObject = new JSONObject(result);
//					String sucess = resObject.getString("status");
////					String notice = resObject.getString("notice");
//					if (sucess != null && !sucess.equals("")
//							&& !sucess.equals("false")) {
//						SharedPreferences mPreference = PreferenceManager
//								.getDefaultSharedPreferences(mHomePage);
//						Editor prefsEditor = mPreference.edit();
//						prefsEditor.putBoolean(
//								AppConstants.PREFCATEGORYSELECTED, true);
//						prefsEditor.commit();
//						createLifePageGoalView();
//					}
//				} catch (Exception e) {
//
//				}
//			}
//		}
//	}
//
//	private class fetchGoalsFromServer extends AsyncTask<String, Void, String> {
//
//		@Override
//		protected void onPreExecute() {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//
//			String authToken = PreferenceManager.getDefaultSharedPreferences(
//					mHomePage).getString(AppConstants.PREFAUTH_TOKEN, "");
//			String result = WebHTTPMethodClass.httpGetService("/goals",
//					"auth_token=" + authToken + "&appkey="
//							+ AppConstants.APPKEY);
//			return result;
//		}
//
//		@Override
//		protected void onProgressUpdate(Void... values) {
//
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().dismiss();
//
//			lifeGoalPageCellContainerLayout.removeAllViews();
//			mParentLayout.removeAllViews();
//			if (result != null && !result.equals("")) {
//				try {
//					JSONObject resObject = new JSONObject(result);
//					String sucess = resObject.getString("status");
//					String notice = resObject.getString("notice");
//					if (sucess.equalsIgnoreCase("true")) {
//						// JSONObject responseObject = new JSONObject(result);
//						// result = responseObject.getString("result");
//						JSONArray jsonArray = resObject.getJSONArray(notice);
//						listThisWeekBean = new ArrayList<ThisWeekBean>();
//						for (int i = 0; i < jsonArray.length(); i++) {
//							ThisWeekBean thisWeekBean = new ThisWeekBean();
//							thisWeekBean.setCategoryName(jsonArray
//									.getJSONObject(i).getString("outlet_name"));
//							thisWeekBean.setIsSelected(jsonArray.getJSONObject(
//									i).getString("outlet_name"));
//							thisWeekBean.setCategoryId(jsonArray.getJSONObject(
//									i).getString("outlet_name"));
//							listThisWeekBean.add(thisWeekBean);
//						}
//						if (listThisWeekBean != null
//								&& listThisWeekBean.size() > 0) {
//							lifeGoalPageCellContainerLayout.removeAllViews();
//							createSeqUnderLine(lifeGoalPageCellContainerLayout);
//							createLifePageGoalView(listThisWeekBean);
//						}
//					}
//				} catch (Exception e) {
//
//				}
//			}
//			mParentLayout.addView(lifeGoalPageMainLayout);
//			parseInput(result);
//		}
//	}
//
//	private class fetchGoalsDeadlineFromServer extends
//			AsyncTask<String, Void, String> {
//
//		@Override
//		protected void onPreExecute() {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//
//			String authToken = PreferenceManager.getDefaultSharedPreferences(
//					mHomePage).getString(AppConstants.PREFAUTH_TOKEN, "");
//			String result = WebHTTPMethodClass.httpGetService(
//					"/goals/deadline", "auth_token=" + authToken + "&appkey="
//							+ AppConstants.APPKEY);
//			return result;
//		}
//
//		// {"status":true,"notice":"Your new password has been emailed to you.
//		// You should receive it in few minutes.","new_pass":"zb47nc"}
//		@Override
//		protected void onProgressUpdate(Void... values) {
//
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().dismiss();
//			parseInput(result);
//		}
//	}
//
//	private class fetchGoalshistoryFromServer extends
//			AsyncTask<String, Void, String> {
//
//		@Override
//		protected void onPreExecute() {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//
//			String authToken = PreferenceManager.getDefaultSharedPreferences(
//					mHomePage).getString(AppConstants.PREFAUTH_TOKEN, "");
//			String result = WebHTTPMethodClass.httpGetService("/goals/history",
//					"auth_token=" + authToken + "&appkey="
//							+ AppConstants.APPKEY);
//			return result;
//		}
//
//		// {"status":true,"notice":"Your new password has been emailed to you.
//		// You should receive it in few minutes.","new_pass":"zb47nc"}
//		@Override
//		protected void onProgressUpdate(Void... values) {
//
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (mHomePage != null && mHomePage.getProgressDialog() != null)
//				mHomePage.getProgressDialog().dismiss();
//			parseInput(result);
//		}
//	}
//
//	private void parseInput(String result) {
//		if (result != null && !result.equals("")) {
//			try {
//				JSONObject resObject = new JSONObject(result);
//				String sucess = resObject.getString("status");
//				String notice = resObject.getString("notice");
//
//				String errors = "";
//				String auth_token = "";
//				try {
//					auth_token = resObject.getString("auth_token");
//					SharedPreferences mPreference = PreferenceManager
//							.getDefaultSharedPreferences(mHomePage);
//					Editor prefsEditor = mPreference.edit();
//					prefsEditor.putString(AppConstants.PREFAUTH_TOKEN,
//							auth_token);
//					Log.d("auth_token", auth_token);
//					prefsEditor.commit();
//				} catch (Exception e) {
//				}
//				try {
//					errors = resObject.getString("errors");
//				} catch (Exception e) {
//				}
//				if (sucess != null && (!sucess.equals(""))) {// false
//					AppConstants
//							.showMsgDialog("ZoesKitchen", notice, mHomePage);
//				} else {
//					if (errors != null && (!errors.equals(""))) {
//						AppConstants.showMsgDialog("ZoesKitchen", errors
//								+ notice, mHomePage);
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//}
//
//// lifePageCellContainerLayout.removeAllViews();
//// createSeqUnderLine(lifePageCellContainerLayout);
//// for (int i = 0; i < 8; i++) {
//// createLifePageCellView(lifePageCellContainerLayout);
//// // if (i < 8 - 1)//billList.size()
//// createSeqUnderLine(lifePageCellContainerLayout);
//// }
//// mParentLayout.addView(lifePageMainLayout);
//// }
//
//// private void createLifePageCellView(LinearLayout lifePageMainLayout) {
//// LinearLayout cellMainLayout = (LinearLayout) mInflater.inflate(
//// R.layout.lifepage_thisweek_list, null);
//// TextView optionTextView = (TextView) cellMainLayout
//// .findViewById(R.id.lifepage_thisweek_list_text_option);
//// ImageView doneButtonView = (ImageView) cellMainLayout
//// .findViewById(R.id.lifepage_thisweek_list_checkbox);
//// doneButtonView.setOnClickListener(new OnClickListener() {
//// @Override
//// public void onClick(View v) {
//// ImageView optionTextView = (ImageView) v;
//// optionTextView.setImageResource(R.drawable.checkmark);
//// }
//// });
//// lifePageMainLayout.addView(cellMainLayout);
//// }
//
//// lifeGoalPageCellContainerLayout.removeAllViews();
//// createSeqUnderLine(lifeGoalPageCellContainerLayout);
//// for (int i = 0; i < 8; i++) {
//// createLifePageGoalCellView(lifeGoalPageCellContainerLayout);
//// // if (i < 8 - 1)//billList.size()
//// createSeqUnderLine(lifeGoalPageCellContainerLayout);
//// }
//// mParentLayout.addView(lifeGoalPageMainLayout);
//
//// private void createLifePageGoalCellView(LinearLayout lifePageMainLayout)
//// {
//// RelativeLayout cellMainLayout = (RelativeLayout) mInflater.inflate(
//// R.layout.lifepage_thisweek_goal_list, null);
//// TextView optionTextView = (TextView) cellMainLayout
//// .findViewById(R.id.lifepage_thisweek_goal_list_text_option);
//// ImageView doneButtonView = (ImageView) cellMainLayout
//// .findViewById(R.id.lifepage_thisweek_goal_list_checkbox);
//// doneButtonView.setOnClickListener(new OnClickListener() {
//// @Override
//// public void onClick(View v) {
//// ImageView optionTextView = (ImageView) v;
//// optionTextView.setImageResource(R.drawable.checkmark);
//// }
//// });
//// lifePageMainLayout.addView(cellMainLayout);
//// }