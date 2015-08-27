package com.ak.app.roti.snap;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.Snap;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.app.roti.bean.LocationBean;
import com.akl.app.roti.bean.LocationOfferBean;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.RefreshListner;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;

public class ScanTutorial implements RefreshListner {
	private static ScanTutorial screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;
	public boolean hasRefresh = true;
	private RelativeLayout welcomeLinearLayout;
	LinearLayout lifePageCellContainerLayout;
	List<LocationBean> listThisWeekBean;
	private ProgressBar pageProgressBar;

	public static ScanTutorial getInstance() {
		if (screen == null)
			screen = new ScanTutorial();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.first_time_tutorial_page, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	public void onCreate() {

		mHomePage = Tabbars.getInstance();

		TextView getRewardsWord1 = (TextView) mParentLayout
				.findViewById(R.id.howtogetrewards_text1);
		TextView getRewardsWord2 = (TextView) mParentLayout
				.findViewById(R.id.howtogetrewards_text2);
		TextView textView1 = (TextView) mParentLayout
				.findViewById(R.id.textView1);
		RelativeLayout parentLayout = (RelativeLayout) mParentLayout
				.findViewById(R.id.parentLayout);
		ImageView closeBtn = (ImageView) mParentLayout
				.findViewById(R.id.closeBtn);
		pageProgressBar = (ProgressBar) mParentLayout
				.findViewById(R.id.greenProgressBar);
		
		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		
	
		TextView topTitle2 = (TextView) mParentLayout
				.findViewById(R.id.topTitle2);
		
	

		RelativeLayout backarrow;
		backarrow=(RelativeLayout)mParentLayout.findViewById(R.id.backarrow);
		backarrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Snap.getInstance().onBackPressed();
			
			}
		});
		

		
		
		AppConstants.gothamNarrowMediumTextView(topTitle2, 18, AppConstants.COLORWHITERGB,mHomePage.getAssets());

		
//		AppConstants.odinRoundedBoldTextView(topTitle, 18,
//				AppConstants.COLORWHITERGB, mHomePage.getAssets());

		AppConstants.fontDinLightTextView(getRewardsWord1, 30,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
		AppConstants.fontDinLightTextView(getRewardsWord2, 30,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
		AppConstants.americanTypewriterTextView(textView1, 16,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());

		closeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Snap.getInstance().handleBackButton();
			}
		});

	}

	@Override
	public void notifyRefresh(String className) {
		if (className.equalsIgnoreCase("showSelectLocationPage"))
			refreshView();
	}

	public void refreshView() {
		if (AppConstants.CheckEnableGPS(mHomePage)) {
			if (AppConstants.isNetworkAvailable(mHomePage)) {
				new fetchLocationFromServer().execute("");
			} else
				AppConstants.showMsgDialog("",
						AppConstants.ERRORNETWORKCONNECTION, mHomePage);
		} else
			AppConstants.showMsgDialog("", AppConstants.ERRORLOCATIONSERVICES,
					mHomePage);
	}

	private class fetchLocationFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// if (mHomePage != null && mHomePage.getProgressDialog() != null)
			// mHomePage.getProgressDialog().show();
			pageProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = WebHTTPMethodClass.httpGetService("/offers/nearby",
					"appkey=" + AppConstants.APPKEY + "&lat="
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
			pageProgressBar.setVisibility(View.GONE);

			Tabbars.getInstance().stopGPS();
			lifePageCellContainerLayout.removeAllViews();
			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					if (sucess.equalsIgnoreCase("true")) {
						JSONArray jsonArray = resObject
								.getJSONArray("restaurants");
						listThisWeekBean = new ArrayList<LocationBean>();
						for (int i = 0; i < jsonArray.length(); i++) {
							LocationBean thisWeekBean = new LocationBean();
							thisWeekBean.setAddress(jsonArray.getJSONObject(i)
									.getString("address"));
							thisWeekBean.setApp_display_text(jsonArray
									.getJSONObject(i).getString(
											"app_display_text"));
							thisWeekBean.setId(jsonArray.getJSONObject(i)
									.getString("id"));
							thisWeekBean.setLatitude(jsonArray.getJSONObject(i)
									.getString("latitude"));
							thisWeekBean.setLongitude(jsonArray
									.getJSONObject(i).getString("longitude"));
							thisWeekBean.setName(jsonArray.getJSONObject(i)
									.getString("name"));
							thisWeekBean
									.setPhone_number(jsonArray.getJSONObject(i)
											.getString("phone_number"));
							thisWeekBean.setZipcode(jsonArray.getJSONObject(i)
									.getString("zipcode"));

							String jsonstr = jsonArray.getJSONObject(i)
									.getString("today_open_hour");
							JSONObject resObject1 = new JSONObject(jsonstr);
							// String sucess1 =
							// resObject1.getString("close_at");
							thisWeekBean.setClose_at(resObject1
									.getString("close_at"));
							thisWeekBean.setDay_of_week(resObject1
									.getString("day_of_week"));
							thisWeekBean.setOpen_at(resObject1
									.getString("open_at"));

							JSONArray jsonArray1 = jsonArray.getJSONObject(i)
									.getJSONArray("available_offers");

							if (jsonArray1.length() > 0) {
								List<LocationOfferBean> listLocationOfferBean = new ArrayList<LocationOfferBean>();
								for (int j = 0; j < jsonArray1.length(); j++) {
									LocationOfferBean locationOfferBean = new LocationOfferBean();
									locationOfferBean
											.setBonus_points(jsonArray1
													.getJSONObject(j)
													.getString("bonus_points"));
									locationOfferBean
											.setBonus_points_ftu(jsonArray1
													.getJSONObject(j)
													.getString(
															"bonus_points_ftu"));
									locationOfferBean.setChain_id(jsonArray1
											.getJSONObject(j).getString(
													"chain_id"));
									locationOfferBean.setCreated_at(jsonArray1
											.getJSONObject(j).getString(
													"created_at"));
									locationOfferBean.setDaysOfWeek(jsonArray1
											.getJSONObject(j).getString(
													"daysOfWeek"));
									locationOfferBean.setDeleted_at(jsonArray1
											.getJSONObject(j).getString(
													"deleted_at"));
									locationOfferBean
											.setEffectiveDate(jsonArray1
													.getJSONObject(j)
													.getString("effectiveDate"));
									locationOfferBean.setExpiryDate(jsonArray1
											.getJSONObject(j).getString(
													"expiryDate"));
									locationOfferBean.setFineprint(jsonArray1
											.getJSONObject(j).getString(
													"fineprint"));
									locationOfferBean.setId(jsonArray1
											.getJSONObject(j).getString("id"));
									locationOfferBean.setIsActive(jsonArray1
											.getJSONObject(j).getString(
													"isActive"));
									locationOfferBean.setMultiplier(jsonArray1
											.getJSONObject(j).getString(
													"multiplier"));
									locationOfferBean
											.setName(jsonArray1
													.getJSONObject(j)
													.getString("name"));
									locationOfferBean.setSurvey_id(jsonArray1
											.getJSONObject(j).getString(
													"survey_id"));
									locationOfferBean.setTimeEnd(jsonArray1
											.getJSONObject(j).getString(
													"timeEnd"));
									locationOfferBean.setTimeStart(jsonArray1
											.getJSONObject(j).getString(
													"timeStart"));
									locationOfferBean.setUpdated_at(jsonArray1
											.getJSONObject(j).getString(
													"updated_at"));
									listLocationOfferBean
											.add(locationOfferBean);
								}
								thisWeekBean
										.setListLocationOfferBean(listLocationOfferBean);
								// thisWeekBean
								// .setLocationOfferBean(locationOfferBean);
							}
							listThisWeekBean.add(thisWeekBean);
						}
						if (listThisWeekBean != null
								&& listThisWeekBean.size() > 0) {
							// createSeqUnderLine(lifePageCellContainerLayout);
							createSelectLocationListView(listThisWeekBean);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			AppConstants.parseInput(result, mHomePage);
		}
	}

	public void createSeqUnderLine(LinearLayout mainLinearLayout) {
		RelativeLayout underlineView = (RelativeLayout) mInflater.inflate(
				R.layout.dottedlineview, null);
		mainLinearLayout.addView(underlineView);
	}

	private void createSelectLocationListView(
			List<LocationBean> listThisWeekBean) {
		for (int i = 0; i < listThisWeekBean.size(); i++) {
			LocationBean thisWeekBean = listThisWeekBean.get(i);
			if (thisWeekBean.getListLocationOfferBean() != null
					&& thisWeekBean.getListLocationOfferBean().size() > 0) {
				RelativeLayout cellViewMainLayout = (RelativeLayout) mInflater
						.inflate(R.layout.life_selectlocation, null);
				TextView nameTextView = (TextView) cellViewMainLayout
						.findViewById(R.id.locfindzoes_list_text_name);
				TextView addressTextView = (TextView) cellViewMainLayout
						.findViewById(R.id.locfindzoes_list_text_address);
				// TextView cellnumTextView = (TextView) cellViewMainLayout
				// .findViewById(R.id.locfindzoes_list_text_cellnum);
				// TextView cameraTextView = (TextView) cellViewMainLayout
				// .findViewById(R.id.locfindzoes_list_text_camera);
				// TextView distTextView = (TextView) cellViewMainLayout
				// .findViewById(R.id.locfindzoes_list_text_dist);
				nameTextView.setText("");
				addressTextView.setText("");
				// cellnumTextView.setText("");
				// distTextView.setText("");

				nameTextView.setText(thisWeekBean.getName());
				addressTextView.setText(thisWeekBean.getAddress());
				cellViewMainLayout.setTag(thisWeekBean);
				// cellnumTextView.setVisibility(View.INVISIBLE);
				// distTextView.setVisibility(View.INVISIBLE);

				try {
					double offerLatitude = Double.parseDouble(thisWeekBean
							.getLatitude());
					double offerLongitude = Double.parseDouble(thisWeekBean
							.getLongitude());
					double userLatitude = mHomePage.getGetLatLongObj()
							.getLatitude();
					double userLongitude = mHomePage.getGetLatLongObj()
							.getLongitude();
					Location offerloc = new Location(
							LocationManager.GPS_PROVIDER);
					Location userloc = new Location(
							LocationManager.GPS_PROVIDER);
					offerloc.setLatitude(offerLatitude);
					offerloc.setLongitude(offerLongitude);

					userloc.setLatitude(userLatitude);
					userloc.setLongitude(userLongitude);
					float dist = userloc.distanceTo(offerloc);
					double distance = dist;
					distance = distance * 0.0621371;
					int idistance = (int) distance;
					distance = idistance;
					distance = distance / 100;
					// distTextView.setText(distance + " ml");
				} catch (Exception e) {
				}
				try {
					String close_at = thisWeekBean.getClose_at();
					String open_at = thisWeekBean.getOpen_at();
					String med1 = "am";
					String med2 = "am";
					if (close_at.contains("PM")) {
						med1 = "pm";
					}
					if (open_at.contains("PM")) {
						med2 = "pm";
					}
					close_at = close_at.substring(0, 2);
					open_at = open_at.substring(0, 2);

					if ((close_at.charAt(0) + "").equals("0")) {
						close_at = close_at.substring(1, 2);
					}
					if ((open_at.charAt(0) + "").equals("0")) {
						open_at = open_at.substring(1, 2);
					}
					// cellnumTextView.setText(open_at + med2 + " - " + close_at
					// + med1);
				} catch (Exception e) {
				}

				AppConstants.fontGothamBoldTextView(nameTextView, 17,
						AppConstants.COLORGREEN, mHomePage.getAssets());
				AppConstants.fontGothamLightTextView(addressTextView, 12,
						AppConstants.COLORGREY, mHomePage.getAssets());
				// AppConstants.fontdinmediumTextView(cellnumTextView, 11,
				// AppConstants.COLORWHITERGB, mHomePage.getAssets());
				// AppConstants.fontdinmediumTextView(distTextView, 13,
				// AppConstants.COLORLOCATIONKATHTHI,
				// mHomePage.getAssets());
				lifePageCellContainerLayout.addView(cellViewMainLayout);
				// if (i < listThisWeekBean.size() - 1)
				// createSeqUnderLine(lifePageCellContainerLayout);
				cellViewMainLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						thisWeekBeans = (LocationBean) v.getTag();
						// mHomePage.showTabs();
						// mHomePage.hideTabs();
						Tabbars.getInstance().setLocationBean(thisWeekBeans);
						// boolean logoutStatus = mHomePage.getPreference()
						// .getBoolean(AppConstants.PREFLOGOUTBUTTONTAG, true);
						if (/* logoutStatus && */!mHomePage.checkIfLogin()) {

							Snap.getInstance().setNextViewName(
									"showCameraViewPage");
							Snap.getInstance().showLoginOptionPage(false,
									"SNAP");
						} else {
							Snap.getInstance()
									.showCameraViewPage(thisWeekBeans);
							thisWeekBeans = null;
						}
					}
				});
			}
		}
	}

	LocationBean thisWeekBeans;
}
