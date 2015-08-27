package com.ak.app.roti.pay;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.Snap;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.app.roti.bean.LocationBean;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.RefreshListner;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;

public class PayEarnView implements RefreshListner {
	
	
	private static PayEarnView screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;
	public boolean hasRefresh = true;
	private ProgressBar pageProgressBar;
	// private RelativeLayout welcomeLinearLayout;
	LinearLayout lifePageCellContainerLayout;
	List<LocationBean> listThisWeekBean;

	public static PayEarnView getInstance() {
		if (screen == null)
			screen = new PayEarnView();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.snaplocation, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	public void onCreate() {

		mHomePage = Tabbars.getInstance();
		Tabbars.getInstance().startGPS();
		// mHomePage.hideTabs();
		lifePageCellContainerLayout = (LinearLayout) mParentLayout
				.findViewById(R.id.findzoes_linear_parentview);

		TextView pageTitle = (TextView) mParentLayout
				.findViewById(R.id.pageTitle);
		pageProgressBar = (ProgressBar) mParentLayout
				.findViewById(R.id.greenProgressBar);
		AppConstants.fontDinLightTextView(pageTitle, 26,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
		// welcomeLinearLayout = (RelativeLayout) mParentLayout
		// .findViewById(R.id.snap_start_RelativeLayout_snapstartparent);
		// TextView earnpointsText = (TextView) mParentLayout
		// .findViewById(R.id.snap_location_text_earnpoints);
		// TextView selectlocationText = (TextView) mParentLayout
		// .findViewById(R.id.snap_location_text_selectlocationtosubmitreceipt);

		// AppConstants.fontGothamBoldTextView(earnpointsText, 18,
		// AppConstants.COLORWHITERGB, mHomePage.getAssets());
		// AppConstants.fontGothamBoldTextView(selectlocationText, 14,
		// AppConstants.COLORWHITERGB, mHomePage.getAssets());

		boolean isTutorialShown = mHomePage.getPreference().getBoolean(
				AppConstants.PREF_SNAP_ISNOTOPENSTARTPAGE, false);

		if (isTutorialShown) {
			// welcomeLinearLayout.setVisibility(View.GONE);
			refreshView();
		} else {

			// welcomeLinearLayout.setVisibility(View.VISIBLE);
			Editor edit = mHomePage.getPreferenceEditor();
			edit.putBoolean(AppConstants.PREF_SNAP_ISNOTOPENSTARTPAGE, true);
			edit.commit();
			// welcomeLinearLayout.setVisibility(View.GONE);
			refreshView();
			// Snap.getInstance().showTutorialSnapPage();

		}
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
			String result = WebHTTPMethodClass.httpGetService(
					"/restaurants/nearby", "appkey=" + AppConstants.APPKEY
							+ "&lat="
							+ mHomePage.getGetLatLongObj().getLatitude()
							+ "&lng="
							+ mHomePage.getGetLatLongObj().getLongitude()
							+ "&locale=en&distance=0");
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
			// if (thisWeekBean.getListLocationOfferBean() != null
			// && thisWeekBean.getListLocationOfferBean().size() > 0) {
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
			AppConstants.fontDinMediumTextView(nameTextView, 18,
					AppConstants.COLORDARKGRAYRGB, mHomePage.getAssets());
			AppConstants.fontDinLightTextView(addressTextView, 14,
					AppConstants.COLORDARKGRAYRGB, mHomePage.getAssets());

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

						Snap.getInstance()
								.setNextViewName("showCameraViewPage");
						Snap.getInstance().showLoginOptionPage(false, "SNAP",
								"scanReceiptBarcode");
					} else {

						Snap.getInstance().showScanBarcodePage();
						thisWeekBeans = null;
					}
				}
			});
			// }

			// if (i == listThisWeekBean.size() - 1) {
			// RelativeLayout locationsLine = (RelativeLayout)
			// cellViewMainLayout
			// .findViewById(R.id.locationsLine);
			// ImageView locationsToCome = (ImageView) cellViewMainLayout
			// .findViewById(R.id.locationsToCome);
			// RelativeLayout blackLastLine = (RelativeLayout)
			// cellViewMainLayout
			// .findViewById(R.id.blackLastLine);
			// locationsLine.setVisibility(View.GONE);
			// blackLastLine.setVisibility(View.GONE);
			// locationsToCome.setVisibility(View.VISIBLE);
			// }
		}
	}

	LocationBean thisWeekBeans;
}
