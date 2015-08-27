package com.ak.app.cb.activity;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ak.app.cb.activity.R;
import com.akl.app.roti.bean.LocationBean;
import com.akl.app.roti.bean.LocationOfferBean;
import com.akl.zoes.kitchen.util.AppConstants;

public class OriScanActivity extends Activity {

	private static OriScanActivity mCameraActivityPage;
	private Tabbars mHomePage;
	int responseCode;
	private boolean isStatus;
	private String receiptId;
	private String surveyId;
	LocationBean locationBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.snap_camera);

		mHomePage = Tabbars.getInstance();
		mCameraActivityPage = this;
		locationBean = Tabbars.getInstance().getLocationBean();// Snap.getInstance().getLocationBean();
		if (locationBean.getListLocationOfferBean() != null
				&& locationBean.getListLocationOfferBean().size() < 1)
			finish();
		TextView pageTitle = (TextView) findViewById(R.id.pageTitle);
		ImageView signup_image_signup = (ImageView) findViewById(R.id.signup_image_signup);
		ImageView scanBarcode = (ImageView) findViewById(R.id.scanBarcode);
		AppConstants.fontChangingMediumTextView(pageTitle, 40,
				AppConstants.COLORWHITERGB, getAssets());

		signup_image_signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				EditText enterBarcodeEdit = (EditText) findViewById(R.id.enterBarcodeEdit);
				String barcodeValue = enterBarcodeEdit.getText().toString();
				Log.i("EEEEEEEEEE", barcodeValue);
				String url = AppConstants.BASE_URL + "/receipts/upload?appkey="
						+ AppConstants.APPKEY + "&barcode=" + barcodeValue /*
											 */;
				if (AppConstants.isNetworkAvailable(mCameraActivityPage)) {
					if (mHomePage.checkIfLogin()) {
						new SubmitReceiptTask().execute(url);
					} else {
						Snap.getInstance().showLoginOptionPage(false, "SNAP");
					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION,
							mCameraActivityPage);
			}
		});

		scanBarcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(
						"com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
				startActivityForResult(intent, 0);
			}
		});
	}

	private class SubmitReceiptTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			finish();
		}

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
				}

				else if (responseJson.getBoolean("status") == false) {
					isStatus = false;
					Log.i("Response Json Failure:", "" + responseJson);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// if (message != null && message.length() > 0
			// && message.equals("401")) {
			// try {
			// AppConstants.showMessageDialogWithNewIntent(
			// AppConstants.ERROR401SERVICES,
			// Tabbars.getInstance());// mCameraActivityPage
			// } catch (Exception e) {
			// AppConstants.showMessageDialogWithNewIntent(
			// AppConstants.ERROR401SERVICES,
			// Tabbars.getInstance());
			// }
			// }

			String teks = isStatus ? "Benar" : "Salah";

			Log.i("isStatus", teks);

			// Snap.getInstance().parseInput(result, Tabbars.getInstance());
			// Snap.getInstance().checkReciptSubmitionStatus(isStatus,
			// receiptId,
			// surveyId);
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
			List<LocationOfferBean> listLocationOfferBean = locationBean
					.getListLocationOfferBean();

			HttpGet httpGet = new HttpGet(url + "&auth_token=" + authToken
					+ "&offer_id=25&restaurant=" + locationBean.getId());
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {

				String contents = data.getStringExtra("SCAN_RESULT");
				String format = data.getStringExtra("SCAN_RESULT_FORMAT");
				String url = AppConstants.BASE_URL + "/receipts/upload?appkey="
						+ AppConstants.APPKEY + "&barcode=" + contents /*
											 */;

				new SubmitReceiptTask().execute(url);
				// Handle successful scan

			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
				Log.i("App", "Scan unsuccessful");
			}
		}
	}

}
