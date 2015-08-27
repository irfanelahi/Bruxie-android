package com.ak.app.roti.snap;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.app.cb.activity.RotiHomeActivity;
import com.ak.app.cb.activity.Snap;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.app.roti.bean.LocationBean;
import com.akl.app.roti.bean.LocationOfferBean;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.RefreshListner;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class ScanBarcode implements RefreshListner {
	private static ScanBarcode screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;
	int responseCode;
	private boolean isStatus;
	private String receiptId;
	// private ImageView enterBtn;
	private String surveyId;
	static AlertDialog.Builder alertDialogBuilder;
	public boolean hasRefresh = true;
	// private EditText enterBarcodeEdit;
	private RelativeLayout welcomeLinearLayout;
	LinearLayout lifePageCellContainerLayout;
	List<LocationBean> listThisWeekBean;
	private JSONObject responseJson;
	private ProgressBar pageProgressBar;
	private ImageView provideFeedback;

	public static ScanBarcode getInstance() {
		if (screen == null)
			screen = new ScanBarcode();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.activity_scan, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	TextView desc;
	ImageView QRCode;
	TextView userCode;
	TextView locationLink;

	public void onCreate() {
//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Scan Barcode").build());
		mHomePage = Tabbars.getInstance();

		if (Snap.getInstance().isReceiptSurvey == true) {
			Snap.getInstance().isReceiptSurvey = false;
		}

		TextView pageTitle = (TextView) mParentLayout
				.findViewById(R.id.pageTitle);
		// TextView scanRegisterText = (TextView) mParentLayout
		// .findViewById(R.id.scanRegisterText); / removed by herlangga
		desc = (TextView) mParentLayout.findViewById(R.id.desc);
		locationLink = (TextView) mParentLayout
				.findViewById(R.id.location_text);
		userCode = (TextView) mParentLayout.findViewById(R.id.userCode);

		QRCode = (ImageView) mParentLayout.findViewById(R.id.QRCode);
		// TextView barcodeResult = (TextView) mParentLayout
		// .findViewById(R.id.barcodeResult);

		// EAN13CodeBuilder bb = new EAN13CodeBuilder("124958761310");
		// barcodeResult.setText(bb.getCode());

		provideFeedback = (ImageView) mParentLayout
				.findViewById(R.id.provideFeedback);
		pageProgressBar = (ProgressBar) mParentLayout
				.findViewById(R.id.greenProgressBar);
		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.odinRoundedBoldTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());

		// AppConstants.fontEANBarcodeTextView(barcodeResult, 150,
		// AppConstants.COLORBLACKRGB, mHomePage.getAssets());
		AppConstants.fontDinLightTextView(pageTitle, 26,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
		// AppConstants.fontDinLightTextView(scanRegisterText, 18,
		// AppConstants.COLORGRAYNEKTER, mHomePage.getAssets()); by herlangga

		AppConstants.americanTypewriterTextView(desc, 14,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());
		AppConstants.odinRoundedBoldTextView(locationLink, 14,
				AppConstants.COLORGREENCORNERBAKERY, mHomePage.getAssets());

		locationLink.setEnabled(false);
		locationLink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					AppConstants.reduceScreenBrightness(mHomePage);
					RotiHomeActivity.getInstance().oPenTabView(3);
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
			}
		});

		provideFeedback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (Tabbars.getInstance() != null
						&& Tabbars.getInstance().checkIfLogin()) {
					TabHost tabHost = (TabHost) Snap.getInstance().getParent()
							.findViewById(android.R.id.tabhost);
					if (tabHost != null)
						tabHost.setCurrentTab(1);// showMyGoodiesPage();
					Snap.getInstance().showScanBarcodePage();
				} else {
					Snap.getInstance()
							.setNextViewName("viewMyGoodiesImageView");
					Snap.getInstance().showLoginOptionPage(false, "SNAP");
				}

			}
		});

		new getUserDetail().execute("");
		// refreshView();

	}

	/**************************************************************
	 * getting from com.google.zxing.client.android.encode.QRCodeEncoder
	 * 
	 * See the sites below http://code.google.com/p/zxing/
	 * http://code.google.com
	 * /p/zxing/source/browse/trunk/android/src/com/google/
	 * zxing/client/android/encode/EncodeActivity.java
	 * http://code.google.com/p/zxing
	 * /source/browse/trunk/android/src/com/google/
	 * zxing/client/android/encode/QRCodeEncoder.java
	 */

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width,
			int img_height) throws WriterException {
		String contentsToEncode = contents;
		if (contentsToEncode == null) {
			return null;
		}
		Map<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(contentsToEncode);
		if (encoding != null) {
			hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result;
		try {
			result = writer.encode(contentsToEncode, format, img_width,
					img_height, hints);
		} catch (IllegalArgumentException iae) {
			// Unsupported format
			return null;
		}
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}

	Boolean isGetAPI = true;

	private class getUserDetail extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (mHomePage != null && mHomePage.getProgressDialog() != null) {
				try {
					mHomePage.getProgressDialog().show();
				} catch (Exception e) {
					Log.i("elang", "elang _>" + e);
				}
			}
			userCode.setVisibility(View.INVISIBLE);
			QRCode.setVisibility(View.INVISIBLE);
			desc.setVisibility(View.INVISIBLE);
			locationLink.setVisibility(View.INVISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {

			String result = "";
			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			result = WebHTTPMethodClass.httpGetService("/user/code",
					"auth_token=" + authToken + "&appkey="
							+ AppConstants.APPKEY);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {

			if (result != null && !result.equals(""))
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					if (sucess.equalsIgnoreCase("true")) {
						// barcode data
						String barcode_data = resObject.getString("usercode")
								.toString();

						// barcode image
						Bitmap bitmap = null;

						RelativeLayout barcodeLayout = (RelativeLayout) mParentLayout
								.findViewById(R.id.barcodeLayout);

						AppConstants.americanTypewriterTextView(userCode, 15,
								AppConstants.COLORBLACKCORNERBAKERYINFO,
								mHomePage.getAssets());
						AppConstants.changeScreenBrightness(mHomePage);
						AppConstants.isBarcodePage = true;

						try {

							int width = barcodeLayout.getWidth() + 100;
							int height = Integer.valueOf(width / 3);
							bitmap = encodeAsBitmap(barcode_data.toUpperCase(),
									BarcodeFormat.CODE_39, width, height);
							QRCode.setImageBitmap(bitmap);
						} catch (WriterException e) {

							Toast.makeText(mHomePage, e.getMessage(),
									Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}
						userCode.setText("Code: "
								+ resObject.getString("usercode").toString());
						desc.setVisibility(View.VISIBLE);
						locationLink.setVisibility(View.VISIBLE);
						locationLink.setEnabled(true);
						QRCode.setVisibility(View.VISIBLE);
						userCode.setVisibility(View.VISIBLE);

					}

				} catch (Exception e) {
					e.printStackTrace();
					isGetAPI = false;
				}
			else
				isGetAPI = false;

			if (isGetAPI == false) {
				isGetAPI = true;
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mHomePage);
			}
			if (mHomePage.getProgressDialog() != null
					&& mHomePage.getProgressDialog().isShowing())
				mHomePage.getProgressDialog().dismiss();

		}
	}

	public class SubmitReceiptTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.i("Register", "Register Button Response " + result);
			String message = "";
			try {
				JSONObject responseJson = new JSONObject(result);
				if (responseCode == 401) {
					Log.i("Response Json Failure:",
							"" + responseJson.toString());
					message = "401";
				} else if (responseJson.getBoolean("status") == true) {
					isStatus = true;
					receiptId = responseJson.getString("receipt_id");
					surveyId = responseJson.getString("survey_id");
					showSuccessMessageDialog(responseJson.getString("notice"),
							Tabbars.getInstance(), receiptId, surveyId);

				}

				else if (responseJson.getBoolean("status") == false) {
					isStatus = false;
					Log.i("Response Json Failure:", "" + responseJson);
					AppConstants.showMessageDialogWithNewIntent(
							responseJson.getString("notice"),
							Tabbars.getInstance());
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (message != null && message.length() > 0
					&& message.equals("401")) {
				try {
					AppConstants.showMessageDialogWithNewIntent(
							AppConstants.ERROR401SERVICES,
							Tabbars.getInstance());// mCameraActivityPage
				} catch (Exception e) {
					AppConstants.showMessageDialogWithNewIntent(
							AppConstants.ERROR401SERVICES,
							Tabbars.getInstance());
				}
			}

			Snap.getInstance().parseInput(result, Tabbars.getInstance());
			Snap.getInstance().checkReciptSubmitionStatus(isStatus, receiptId,
					surveyId);
		}

		@Override
		protected String doInBackground(String... params) {
			String sResponse = uploadData(params[0]);
			return sResponse;
		}

	}

	public String uploadData(String url) {
		String sResponse = "";
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();

			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");

			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpGet, localContext);
			sResponse = AppConstants.inputStreamToString(
					response.getEntity().getContent()).toString();
			Log.i("ResponseString", sResponse);
			responseCode = response.getStatusLine().getStatusCode();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sResponse;
	}

	private static void showSuccessMessageDialog(String message,
			Context context, final String receiptId, final String surveyId) {
		if (alertDialogBuilder == null) {
			alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder
					.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {
									alertDialogBuilder = null;
									dialog.cancel();
									// Snap.getInstance().showReceiptServeyPage(
									// receiptId, surveyId);
								}
							});
			AlertDialog alert = alertDialogBuilder.create();
			alert.show();
		}
	}

	Boolean isGetApiSurvey = true;

	private class fetchServeyIdAsyncTask extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().show();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			String errorMessage = "";
			try {
				responseJson = new JSONObject(result.toString());
				if (responseJson.getBoolean("status") == true) {
					AppConstants.isBarcodePage = false;
					// Snap.getInstance().showReceiptServeyPage("",
					// responseJson.getString("survey_id"));

					Log.i("FeaturedEventActivity ", "User detail  "
							+ responseJson);
					errorMessage = "";
				} else if (responseJson.getBoolean("status") == false) {
					AppConstants.isBarcodePage = false;
					Snap.getInstance().showLoyaltyPage();
				} else if (responseJson.getBoolean("status") == false
						&& !(responseJson.isNull("errorMsg"))) {
					Log.i("Response Json Failure:",
							"" + responseJson.toString());
					errorMessage = responseJson.getString("errorMsg");
				} else if (responseJson.getBoolean("status") == false) {
					errorMessage = responseJson.getString("message");
				}
			} catch (Exception e) {
				isGetApiSurvey = false;
			}

			if (isGetApiSurvey == false) {
				isGetApiSurvey = true;
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mHomePage);
			}

			if (errorMessage != null && errorMessage.length() > 0
					&& errorMessage.equals("401")) {
				AppConstants.showMessageDialogWithNewIntent(
						AppConstants.ERROR401SERVICES, mHomePage);
			} else if (errorMessage != null && errorMessage.length() > 0) {
				AppConstants.showMsgDialog("", errorMessage, mHomePage);
			}

			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().dismiss();
		}

		@Override
		protected String doInBackground(String... arg0) {
			String result = fetchServeyId();
			return result;
		}
	}

	private String fetchServeyId() {
		String line = "";
		try {
			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			String param = "appkey=" + AppConstants.APPKEY + "&auth_token="
					+ authToken;
			// param += "&locale=" + AppConstants.LOCALE_HEADER_VALUE;
			String params = null;

			Log.i("request", "url " + param + "  param " + params);
			line = WebHTTPMethodClass.httpGetService("/survey/", param);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return line;
	}

	@Override
	public void notifyRefresh(String className) {
		if (className.equalsIgnoreCase("showScanBarcodePage"))
			refreshView();
	}

	public void onStartPageCreateCreate() {
		mHomePage = Tabbars.getInstance();

		TextView howtogetrewardsText = (TextView) mParentLayout
				.findViewById(R.id.snap_start_image_howtogetrewards);
		ImageView skippageText = (ImageView) mParentLayout
				.findViewById(R.id.snap_start_text_back);
		TextView texttitle = (TextView) mParentLayout
				.findViewById(R.id.titleTutorial);
		TextView textspend = (TextView) mParentLayout
				.findViewById(R.id.spendText);
		TextView taketext = (TextView) mParentLayout
				.findViewById(R.id.takeapictext);
		TextView awesomeText = (TextView) mParentLayout
				.findViewById(R.id.awesomeText);

		AppConstants.fontATSackersHeavyGothicTextViewBold(howtogetrewardsText,
				16, AppConstants.COLORWHITERGB, mHomePage.getAssets());
		AppConstants.fontGothamBoldTextView(texttitle, 22,
				AppConstants.COLORGREEN, mHomePage.getAssets());
		AppConstants.fontGothamMediumTextView(textspend, 16,
				AppConstants.COLORGREY, mHomePage.getAssets());
		AppConstants.fontGothamMediumTextView(taketext, 16,
				AppConstants.COLORGREY, mHomePage.getAssets());
		AppConstants.fontGothamMediumTextView(awesomeText, 16,
				AppConstants.COLORGREY, mHomePage.getAssets());

		skippageText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Snap.getInstance().showSnapLocationPage();
				Editor edit = mHomePage.getPreferenceEditor();
				edit.putBoolean(AppConstants.PREF_SNAP_ISNOTOPENSTARTPAGE, true);
				edit.commit();
				welcomeLinearLayout.setVisibility(View.GONE);
				refreshView();
			}
		});
		refreshView();

	}

	public void refreshView() {

		if (AppConstants.isNetworkAvailable(mHomePage)) {
			new getUserDetail().execute("");
		} else
			AppConstants.showMsgDialog("", AppConstants.ERRORNETWORKCONNECTION,
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
