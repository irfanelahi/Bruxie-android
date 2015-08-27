package com.ak.app.roti.info;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.Rewards;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.app.roti.bean.LocationBean;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.CustomProgressDialog;
import com.akl.zoes.kitchen.util.RefreshListner;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class LocationPage implements RefreshListner {
	private static LocationPage screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	LinearLayout lifePageCellContainerLayout;
	List<LocationBean> listThisWeekBean;
	public boolean isRequestFromOrderTab = false;
	private boolean isSearchedLocation = false;
	private boolean isLocationNearby = false;

	public static LocationPage getInstance() {
		if (screen == null)
			screen = new LocationPage();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.info_location, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	private Tabbars mHomePage;

	public void onCreate() {
		mHomePage = Tabbars.getInstance();
		AppConstants.changeScreenBrightnessToDefault(mHomePage);
//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Locations").build());

		lifePageCellContainerLayout = (LinearLayout) mParentLayout
				.findViewById(R.id.location_linear_parentview);

		TextView pageTitle = (TextView) mParentLayout
				.findViewById(R.id.pageTitle);
		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		TextView blackText = (TextView) mParentLayout
				.findViewById(R.id.black_text);
		TextView greenText = (TextView) mParentLayout
				.findViewById(R.id.green_text);

		LinearLayout locationNearbyText = (LinearLayout) mParentLayout
				.findViewById(R.id.locationNearbyText);

		AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());

		AppConstants.kingthingsTextView(pageTitle, 20,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());

		AppConstants.gothamNarrowBookTextView(blackText, 15,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());
		AppConstants.gothamNarrowBookTextView(greenText, 15,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());

		RelativeLayout backarrow;
		backarrow=(RelativeLayout)mParentLayout.findViewById(R.id.backarrow);
		backarrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Info.getInstance().onBackPressed();
			
			}
		});
		
	
		if (AppConstants.CheckEnableGPS(mHomePage)) {
			locationNearbyText.setVisibility(View.GONE);
			lifePageCellContainerLayout.setVisibility(View.VISIBLE);
		} else {
			locationNearbyText.setVisibility(View.VISIBLE);
			lifePageCellContainerLayout.setVisibility(View.GONE);
		}

		final EditText searchEdit = (EditText) mParentLayout
				.findViewById(R.id.searchEdit);
		LinearLayout searchButton = (LinearLayout) mParentLayout
				.findViewById(R.id.searchButton);

		AppConstants.gothamNarrowBookTextView(searchEdit, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());

		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) mHomePage
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
				// TODO Auto-generated method stub
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					isSearchedLocation = true;
					new fetchLocationFromServer(mHomePage).execute(searchEdit
							.getText().toString().trim());
				} else
					AppConstants.showMsgDialog("",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
			}
		});

		locationNearbyText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (AppConstants.CheckEnableGPS(mHomePage)) {
					isLocationNearby = true;
					refreshView();
				} else
					AppConstants.showMsgDialog("",
							AppConstants.ERRORLOCATIONSERVICES, mHomePage);
			}
		});
		// pageProgressBar = (ProgressBar) mParentLayout
		// .findViewById(R.id.pageLoadingIndicator);
		// TextView backText = (TextView) mParentLayout
		// .findViewById(R.id.location_text_back);
		// backText.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Info.getInstance().handleBackButton();
		// }
		// });

		refreshView();
	}

	@Override
	public void notifyRefresh(String className) {
		if (className.equalsIgnoreCase("showLocationPage"))
			refreshView();
	}

	public void refreshView() {
		// if (AppConstants.CheckEnableGPS(mHomePage)) {
		// Tabbars.getInstance().startGPS();
		if (AppConstants.isNetworkAvailable(mHomePage)) {
			// new fetchLocationFromServer().execute("");
			new fetchLocationFromServer(mHomePage).execute("");
		} else
			AppConstants.showMsgDialog("", AppConstants.ERRORNETWORKCONNECTION,
					mHomePage);
		// } else
		// AppConstants.showMsgDialog("", AppConstants.ERRORLOCATIONSERVICES,
		// mHomePage);
	}

	private class fetchLocationFromServer extends
			AsyncTask<String, Void, String> {

		public fetchLocationFromServer(Context ctx) {
			// progressDialog = CustomProgressDialog.ctor(ctx);
		}

		@Override
		protected void onPreExecute() {
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().show();

		}

		@Override
		protected String doInBackground(String... params) {
			// String result = WebHTTPMethodClass.httpGetService(
			// "/restaurants/nearby", "appkey=" + AppConstants.APPKEY
			// + "&lat="
			// + mHomePage.getGetLatLongObj().getLatitude()
			// + "&lng="
			// + mHomePage.getGetLatLongObj().getLongitude()
			// + "&locale=en&distance=0");
			// return result;

			String result = "";
			if (params[0].equals("")) {
				result = WebHTTPMethodClass.httpGetService(
						"/restaurants/nearby", "appkey=" + AppConstants.APPKEY
								+ "&lat="
								+ mHomePage.getGetLatLongObj().getLatitude()
								+ "&lng="
								+ mHomePage.getGetLatLongObj().getLongitude());
			} else {
				result = WebHTTPMethodClass.httpGetService(
						"/restaurants/nearby", "appkey=" + AppConstants.APPKEY
								+ "&lat="
								+ mHomePage.getGetLatLongObj().getLatitude()
								+ "&lng="
								+ mHomePage.getGetLatLongObj().getLongitude()
								+ "&search=" + URLEncoder.encode(params[0]));
			}
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().dismiss();

			lifePageCellContainerLayout.removeAllViews();
			if (isSearchedLocation) {
				isSearchedLocation = false;
				lifePageCellContainerLayout.setVisibility(View.VISIBLE);
			} else if (isLocationNearby) {
				isLocationNearby = false;
				lifePageCellContainerLayout.setVisibility(View.VISIBLE);
			}

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
							thisWeekBean.setOrderLink(jsonArray
									.getJSONObject(i).getString(
											"online_order_link"));
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

							// JSONArray jsonArray1 = jsonArray.getJSONObject(i)
							// .getJSONArray("available_offers");
							//
							// if (jsonArray1.length() > 0) {
							// List<LocationOfferBean> listLocationOfferBean =
							// new ArrayList<LocationOfferBean>();
							// for (int j = 0; j < jsonArray1.length(); j++) {
							// LocationOfferBean locationOfferBean = new
							// LocationOfferBean();
							// locationOfferBean
							// .setBonus_points(jsonArray1
							// .getJSONObject(j)
							// .getString("bonus_points"));
							// locationOfferBean
							// .setBonus_points_ftu(jsonArray1
							// .getJSONObject(j)
							// .getString(
							// "bonus_points_ftu"));
							// locationOfferBean.setChain_id(jsonArray1
							// .getJSONObject(j).getString(
							// "chain_id"));
							// locationOfferBean.setCreated_at(jsonArray1
							// .getJSONObject(j).getString(
							// "created_at"));
							// locationOfferBean.setDaysOfWeek(jsonArray1
							// .getJSONObject(j).getString(
							// "daysOfWeek"));
							// locationOfferBean.setDeleted_at(jsonArray1
							// .getJSONObject(j).getString(
							// "deleted_at"));
							// locationOfferBean
							// .setEffectiveDate(jsonArray1
							// .getJSONObject(j)
							// .getString("effectiveDate"));
							// locationOfferBean.setExpiryDate(jsonArray1
							// .getJSONObject(j).getString(
							// "expiryDate"));
							// locationOfferBean.setFineprint(jsonArray1
							// .getJSONObject(j).getString(
							// "fineprint"));
							// locationOfferBean.setId(jsonArray1
							// .getJSONObject(j).getString("id"));
							// locationOfferBean.setIsActive(jsonArray1
							// .getJSONObject(j).getString(
							// "isActive"));
							// locationOfferBean.setMultiplier(jsonArray1
							// .getJSONObject(j).getString(
							// "multiplier"));
							// locationOfferBean
							// .setName(jsonArray1
							// .getJSONObject(j)
							// .getString("name"));
							// locationOfferBean.setSurvey_id(jsonArray1
							// .getJSONObject(j).getString(
							// "survey_id"));
							// locationOfferBean.setTimeEnd(jsonArray1
							// .getJSONObject(j).getString(
							// "timeEnd"));
							// locationOfferBean.setTimeStart(jsonArray1
							// .getJSONObject(j).getString(
							// "timeStart"));
							// locationOfferBean.setUpdated_at(jsonArray1
							// .getJSONObject(j).getString(
							// "updated_at"));
							// listLocationOfferBean
							// .add(locationOfferBean);
							// }
							// thisWeekBean
							// .setListLocationOfferBean(listLocationOfferBean);
							// // thisWeekBean
							// // .setLocationOfferBean(locationOfferBean);
							// }
							listThisWeekBean.add(thisWeekBean);
						}
						if (listThisWeekBean != null
								&& listThisWeekBean.size() > 0) {
							// createSeqUnderLine(lifePageCellContainerLayout);
							createSelectLocationListView(listThisWeekBean);
						} else
							AppConstants.showMsgDialog("",
									"No results found. Please try again",
									mHomePage);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mHomePage);
			Tabbars.getInstance().stopGPS();
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
			// if (thisWeekBean.getListLocationOfferBean() != null
			// && thisWeekBean.getListLocationOfferBean().size() > 0) {
			RelativeLayout cellViewMainLayout = (RelativeLayout) mInflater
					.inflate(R.layout.foode_findzoes_list, null);
			TextView nameTextView = (TextView) cellViewMainLayout
					.findViewById(R.id.findzoes_list_text_name);
			TextView addressTextView = (TextView) cellViewMainLayout
					.findViewById(R.id.findzoes_list_text_address);

			TextView cellnumTextView = (TextView) cellViewMainLayout
					.findViewById(R.id.findzoes_list_text_cellnum);
			TextView distTextView = (TextView) cellViewMainLayout
					.findViewById(R.id.findzoes_list_text_dist);
			ImageView callIcon = (ImageView) cellViewMainLayout
					.findViewById(R.id.call_icon);
			ImageView mapImage = (ImageView) cellViewMainLayout
					.findViewById(R.id.map_image);
//			RelativeLayout phoneNumberLayout = (RelativeLayout) cellViewMainLayout
//					.findViewById(R.id.phone_number_layout);
			ImageView orderLayout = (ImageView) cellViewMainLayout
					.findViewById(R.id.order_layout);

			nameTextView.setText("");
			addressTextView.setText("");

			cellnumTextView.setText("");
			distTextView.setText("");

			nameTextView.setText(thisWeekBean.getName());
			addressTextView.setText(thisWeekBean.getAddress());

			try {
				double offerLatitude = Double.parseDouble(thisWeekBean
						.getLatitude());
				double offerLongitude = Double.parseDouble(thisWeekBean
						.getLongitude());
				double userLatitude = mHomePage.getGetLatLongObj()
						.getLatitude();
				double userLongitude = mHomePage.getGetLatLongObj()
						.getLongitude();
				Location offerloc = new Location(LocationManager.GPS_PROVIDER);
				Location userloc = new Location(LocationManager.GPS_PROVIDER);
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
				DecimalFormat df = new DecimalFormat("0.#");

				distTextView.setText(df.format(distance) + " miles");
				mapImage.setTag(userLatitude + "," + userLongitude + "?q="
						+ thisWeekBean.getAddress());
				mapImage.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							mHomePage.doNotFinishAllActivities = true;
							String offerLatLong = (String) v.getTag();
							Intent searchAddress = new Intent(
									Intent.ACTION_VIEW, Uri.parse("geo:"
											+ offerLatLong));
							mHomePage.startActivity(searchAddress);
						} catch (Exception activityException) {
							Log.e("url Phonenum", "" + "Call failed");
						}
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				String urlPhonenum = thisWeekBean.getPhone_number();
				cellnumTextView.setText(urlPhonenum);
				cellnumTextView.setTag(urlPhonenum);
				callIcon.setTag(urlPhonenum);

				final String orderUrl = thisWeekBean.getOrderLink();
				orderLayout.setTag(orderUrl);
				orderLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						// try {
						if (AppConstants.CheckEnableGPS(mHomePage)) {
							Tabbars.getInstance().startGPS();

							if (Info.getInstance() != null
									&& !isRequestFromOrderTab) {
								Info.getInstance().showOrderWeb(orderUrl);
							} else {
								isRequestFromOrderTab = false;
								com.ak.app.cb.activity.Location.getInstance()
										.showOrderWeb(orderUrl);
							}
						} else
							AppConstants.showMsgDialog("",
									AppConstants.ERRORLOCATIONSERVICES,
									mHomePage);

						// }catch (Exception e){
						// Log.d("----------- order", orderUrl);
						// }
					}
				});

				cellnumTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							mHomePage.doNotFinishAllActivities = true;
							Log.d("----------------------", "test");
							String urlPhonenum = (String) v.getTag();
							urlPhonenum = "tel:" + urlPhonenum;
							Log.e("hphone-no", urlPhonenum);
							Intent intent = new Intent(Intent.ACTION_DIAL, Uri
									.parse(urlPhonenum));
							mHomePage.startActivity(intent);
						} catch (Exception activityException) {
							Log.e("url Phonenum", "" + "Call failed");
						}
					}
				});

				callIcon.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							Log.d("----------------------", "test");
							String urlPhonenum = (String) v.getTag();
							urlPhonenum = "tel:" + urlPhonenum;
							Log.e("hphone-no", urlPhonenum);
							Intent intent = new Intent(Intent.ACTION_DIAL, Uri
									.parse(urlPhonenum));
							mHomePage.startActivity(intent);
							mHomePage.doNotFinishAllActivities = true;
						} catch (Exception activityException) {
							Log.e("url Phonenum", "" + "Call failed");
						}
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}

			AppConstants.gothamNarrowMediumTextView(nameTextView, 14,
					AppConstants.COLOR_RED_TEXT,
					mHomePage.getAssets());
			
			AppConstants.gothamNarrowBookTextView(addressTextView, 12,
					AppConstants.COLOR_LIGHT_GRAY_TEXT,
					mHomePage.getAssets());

			AppConstants.gothamNarrowBookTextView(cellnumTextView, 11,
					AppConstants.COLORBLACKMUSHROOM, mHomePage.getAssets());
			
			AppConstants.gothamNarrowBookTextView(distTextView, 14,
					AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());
			lifePageCellContainerLayout.addView(cellViewMainLayout);
			// if (i < listThisWeekBean.size() - 1)
			// createSeqUnderLine(lifePageCellContainerLayout);
			// }
		}
	}
}
