package com.ak.app.cb.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.R;
import com.ak.app.roti.snap.CameraLiveView;
import com.akl.app.roti.bean.LocationBean;
import com.akl.app.roti.bean.LocationOfferBean;
import com.akl.zoes.kitchen.util.AppConstants;

public class CameraActivity extends Activity// implements SurfaceHolder.Callback
{
	private static CameraActivity mCameraActivityPage;
	private ImageView captureBtn;
	private TextView uploadBtn;
	private TextView cancelBtn;
	private TextView previewText;
	private Tabbars mHomePage;

	LocationBean locationBean;

	public static CameraActivity getInstance() {
		return mCameraActivityPage;
	}

	RelativeLayout parentRelativeLayout;
	private CameraLiveView cameraLiveView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.snap_camera);
		mHomePage = Tabbars.getInstance();
		mCameraActivityPage = this;
		locationBean = Tabbars.getInstance().getLocationBean();// Snap.getInstance().getLocationBean();
		if (locationBean.getListLocationOfferBean() != null
				&& locationBean.getListLocationOfferBean().size() < 1)
			finish();
		TextView titleTextView = (TextView) findViewById(R.id.camera_text_title);
		parentRelativeLayout = (RelativeLayout) findViewById(R.id.camera_relative_parent);
		captureBtn = (ImageView) findViewById(R.id.camera_image_capture);
		ImageView helpView = (ImageView) findViewById(R.id.camera_image_help);
		previewText = (TextView) findViewById(R.id.camera_text_preview);
		uploadBtn = (TextView) findViewById(R.id.camera_text_upload);
		cancelBtn = (TextView) findViewById(R.id.camera_text_retake);

		AppConstants.fontGothamBoldTextView(titleTextView, 16,
				AppConstants.COLORWHITERGB, getAssets());
		AppConstants.fontdinmediumTextViewBold(previewText, 14,
				AppConstants.COLORWHITERGB, getAssets());
		AppConstants.fontdinmediumTextViewBold(uploadBtn, 13,
				AppConstants.COLORWHITERGB, getAssets());
		AppConstants.fontdinmediumTextViewBold(cancelBtn, 13,
				AppConstants.COLORWHITERGB, getAssets());
		hidePreviewButtons();

		helpView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
				// mHomePage.showTabs();
				Snap.getInstance().showCameraHelpViewPage();
			}
		});
		uploadBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String filename = android.text.format.DateFormat.format(
						"yyyyMMddhhmmss", new java.util.Date()).toString()
						+ ".jpeg";
				Log.i("File Name", "file name is :" + filename);
				String url = AppConstants.BASE_URL + "/receipts?appkey="
						+ AppConstants.APPKEY /*
											 */;
				if (AppConstants.isNetworkAvailable(mCameraActivityPage)) {
					if (mHomePage.checkIfLogin()) {
						new SubmitReceiptTask().execute(url,
								mCameraActivityPage.getExternalCacheDir()
										+ "/receipt_upload.jpeg", filename);
					} else {
						Snap.getInstance().showLoginOptionPage(false, "SNAP");
					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION,
							mCameraActivityPage);
				// mHomePage.hideTabs();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hidePreviewButtons();
				// mHomePage.showTabs();
				cameraLiveView.startPreview();
			}
		});
		captureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// mHomePage.hideTabs();
				cameraLiveView.takePicture();
			}
		});

		// Camera View
		cameraLiveView = (CameraLiveView) findViewById(R.id.camera_live_view);
		cameraLiveView.setShutterCallback(shutterCallback);
		cameraLiveView.setRawCallback(rawCallback);
		cameraLiveView.setJpegCallback(jpegCallback);

		PackageManager pm = mCameraActivityPage.getPackageManager();
		if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
				&& pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
			// do something..
			Log.e("Camera is ..", "supported the auto focus mode");
		}
	}

	private static String TAG = "CustomCameraActivity";
	ShutterCallback shutterCallback = new ShutterCallback() {

		public void onShutter() {
			Log.d(TAG, "onShutter'd");
		}
	};

	/** Handles data for raw picture */
	PictureCallback rawCallback = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG, "onPictureTaken - raw");
		}
	};

	/** Handles data for jpeg picture */
	PictureCallback jpegCallback = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			showPreviewButtons();
			FileOutputStream outStream = null;
			try {
				/**
				 * writing the file on memory.
				 */
				outStream = new FileOutputStream(getExternalCacheDir()
						+ "/receipt_upload.jpeg");
				outStream.write(data);
				outStream.close();
				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
			Log.d(TAG, "onPictureTaken - jpeg");
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		if (cameraLiveView != null) {
			cameraLiveView.onPause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		cameraLiveView.onResume();
	}

	private void showPreviewButtons() {
		previewText.setVisibility(View.VISIBLE);
		uploadBtn.setVisibility(View.VISIBLE);
		cancelBtn.setVisibility(View.VISIBLE);
		captureBtn.setVisibility(View.GONE);
	}

	private void hidePreviewButtons() {
		previewText.setVisibility(View.GONE);
		uploadBtn.setVisibility(View.GONE);
		cancelBtn.setVisibility(View.GONE);
		captureBtn.setVisibility(View.VISIBLE);
	}

	class CustomShutter implements Camera.ShutterCallback {

		public void onShutter() {
		}
	}

	private boolean isStatus;
	private String receiptId;
	private String surveyId;

	private class SubmitReceiptTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			uploadBtn.setEnabled(false);
			finish();
			Snap.getInstance()
					.showReceiptProgressPage(/* receiptId, surveyId */);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.i("Register", "Register Button Response " + result);
			String message = "";
			try {
				JSONObject responseJson = new JSONObject(result);
				if (/* response.getStatusLine().getStatusCode() */responseCode == 401) {
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
			String sResponse = uploadData(params[0], params[1], params[2]);
			return sResponse;
		}

	}

	public String uploadData(String url, String path, String fileName) {
		String sResponse = "";
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url);
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			List<LocationOfferBean> listLocationOfferBean = locationBean
					.getListLocationOfferBean();
			LocationOfferBean locationOfferBean = listLocationOfferBean.get(0);
			FormBodyPart token = new FormBodyPart("auth_token", new StringBody(
					authToken));
			FormBodyPart offer = new FormBodyPart("offer", new StringBody(
					locationOfferBean.getId()));
			FormBodyPart restaurant = new FormBodyPart("restaurant",
					new StringBody(locationBean.getId()));

			FileInputStream in = new FileInputStream(path);
			int imageSize = (int) (in.available() / 1024);
			long freememorySize = Runtime.getRuntime().freeMemory();
			int memorySize = (int) (Runtime.getRuntime().freeMemory() / 1024);
			Log.v("TAG******", "Image size is " + imageSize
					+ " Memory Size is " + memorySize);
			Bitmap image = null;
			try {
				if (imageSize > 500 && memorySize > 1000) {
					image = bitmapDecode(path);
				} else {
					image = bitmapDecodeHor(path);
					Log.e("Image image resizedBitmap", image.getWidth() + " "
							+ image.getHeight());
				}
			} catch (Exception e) {
				Log.e("Image", e.getMessage(), e);
			}

			entity.addPart(token);
			entity.addPart(offer);
			entity.addPart(restaurant);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			image.compress(CompressFormat.JPEG, 50, bos);
			byte[] data = bos.toByteArray();

			entity.addPart("image", new ByteArrayBody(data, "image/jpeg",
					fileName));

			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost, localContext);
			sResponse = AppConstants.inputStreamToString(
					response.getEntity().getContent()).toString();
			Log.i("ResponseString", sResponse);
			responseCode = response.getStatusLine().getStatusCode();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sResponse;
	}

	int responseCode;

	public Bitmap bitmapDecode(String path) {
		try {
			InputStream in = new FileInputStream(path);

			// decode image size (decode metadata only, not the whole image)
			BitmapFactory.Options options = new BitmapFactory.Options();
			in = new FileInputStream(path);
			options = new BitmapFactory.Options();
			options.inPreferredConfig = Config.RGB_565;
			// calc rought re-size (this is no exact resize)

			int imageSize = (int) (in.available() / 1024);
			int memorySize = (int) (Runtime.getRuntime().freeMemory() / 1024);
			Log.v("TAG******", "Image size is " + imageSize
					+ " Memory Size is " + memorySize);

			if (imageSize > 500 && memorySize > 1000) {
				options.inSampleSize = 2;
				Bitmap roughBitmap = BitmapFactory.decodeStream(in, null,
						options);
				// Bitmap roughBitmap = BitmapFactory.decodeStream(in);

				// calc exact destination size
				Matrix m = new Matrix();
				// RectF inRect = new RectF(0, 0, roughBitmap.getWidth(),
				// roughBitmap.getHeight());
				// RectF outRect = new RectF(0, 0, 1024, 768);
				// m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
				float[] values = new float[9];
				m.getValues(values);
				Matrix mat = new Matrix();
				mat.preRotate(90);// /in degree
				// resize bitmap
				Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap,
						(int) (roughBitmap.getWidth() * values[0]),
						(int) (roughBitmap.getHeight() * values[4]), true);

				// roughBitmap.recycle();

				Bitmap rotateImage = Bitmap.createBitmap(resizedBitmap, 0, 0,
						resizedBitmap.getWidth(), resizedBitmap.getHeight(),
						mat, true);

				// roughBitmap = null;
				// save image
				try {
					FileOutputStream out = new FileOutputStream(path);
					resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
					roughBitmap.recycle();
					roughBitmap = null;
					resizedBitmap.recycle();
					resizedBitmap = null;
					return rotateImage;
				} catch (Exception e) {
					Log.e("Image", e.getMessage(), e);
					return rotateImage;
				}
			} else {
				// Bitmap rotateImage = Bitmap.createBitmap(path);
			}

		} catch (IOException e) {
			Log.e("Image", e.getMessage(), e);
		}
		return null;
	}

	public Bitmap bitmapDecodeHor(String path) {
		try {
			// int inWidth = 0;
			// int inHeight = 0;

			InputStream in = new FileInputStream(path);

			// decode image size (decode metadata only, not the whole image)
			BitmapFactory.Options options = new BitmapFactory.Options();
			BitmapFactory.decodeStream(in, null, options);
			in.close();
			in = null;

			// save width and height
			// inWidth = options.outWidth;
			// inHeight = options.outHeight;

			// decode full image pre-resized
			in = new FileInputStream(path);
			options = new BitmapFactory.Options();
			options.inPreferredConfig = Config.RGB_565;
			// calc rought re-size (this is no exact resize)

			int imageSize = (int) (in.available() / 1024);
			int memorySize = (int) (Runtime.getRuntime().freeMemory() / 1024);
			Log.v("TAG******", "Image size is " + imageSize
					+ " Memory Size is " + memorySize);

			// if (imageSize > 500 && memorySize > 1000) {
			// options.inSampleSize = Math.max(inWidth / 1024, inHeight / 768);
			// decode full image
			Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

			// calc exact destination size
			Matrix m = new Matrix();
			// RectF inRect = new RectF(0, 0, roughBitmap.getWidth(),
			// roughBitmap.getHeight());
			// RectF outRect = new RectF(0, 0, 1024, 768);
			// m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
			float[] values = new float[9];
			m.getValues(values);
			Matrix mat = new Matrix();
			mat.preRotate(90);// /in degree
			// resize bitmap
			Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap,
					(int) (roughBitmap.getWidth() * values[0]),
					(int) (roughBitmap.getHeight() * values[4]), true);

			Bitmap rotateImage = Bitmap.createBitmap(resizedBitmap, 0, 0,
					resizedBitmap.getWidth(), resizedBitmap.getHeight(), mat,
					true);

			// save image
			try {
				FileOutputStream out = new FileOutputStream(path);
				// Log.e("Image resizedBitmap", resizedBitmap.getWidth() + " " +
				// resizedBitmap.getHeight());

				resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
				roughBitmap.recycle();
				roughBitmap = null;
				resizedBitmap.recycle();
				resizedBitmap = null;
				return rotateImage;
			} catch (Exception e) {
				Log.e("Image", e.getMessage(), e);
				return rotateImage;
			}
		} catch (IOException e) {
			Log.e("Image", e.getMessage(), e);
		}
		return null;
	}
}
