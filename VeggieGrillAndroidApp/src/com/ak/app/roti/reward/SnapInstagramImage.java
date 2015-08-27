package com.ak.app.roti.reward;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.akl.app.roti.bean.MyGoodieRewardsBean;

import com.akl.zoes.kitchen.util.AppConstants;
import com.ak.app.cb.activity.R;
import com.ak.app.cb.activity.Rewards;
import com.ak.app.cb.activity.Tabbars;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SnapInstagramImage {
	private static SnapInstagramImage screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;

	Bitmap combineBitmap;

	private static final int IMAGE_CAPTURE = 0;
	private Button startBtn;
	private ImageView cancelBtn;
	private Uri imageUri;
	private ImageView rotateBtn;
	Bitmap imageBitmap;
	private static SnapInstagramImage snapInstagramImageInstance;

	// camera instances

	private SurfaceView preview = null;
	private SurfaceHolder previewHolder = null;

	ImageView image;
	Bitmap bmp, itembmp;
	static Bitmap mutableBitmap;
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	File imageFileName = null;
	File imageFileFolder = null;
	private MediaScannerConnection msConn;
	Display d;
	int screenhgt, screenwdh;

	/**
	 * Called when the activity is first created. sets the content and gets the
	 * references to the basic widgets on the screen like {@code Button} or
	 * {@link ImageView}
	 */

	public static SnapInstagramImage getInstance() {
		if (screen == null)
			screen = new SnapInstagramImage();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(R.layout.surface,
				null);
		return mParentLayout;
	}

	public void onCreate() {

		mHomePage = Tabbars.getInstance();

		rotateBtn = (ImageView) mParentLayout.findViewById(R.id.rotateBtn);
		startBtn = (Button) mParentLayout.findViewById(R.id.captureBtn);
		cancelBtn = (ImageView) mParentLayout.findViewById(R.id.cancelBtn);
		snapInstagramImageInstance = this;

		// startCamera();

		cancelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Rewards.getInstance().camera.takePicture(null, null,
						photoCallback);
				Rewards.getInstance().inPreview = false;

			}
		});

		rotateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (Rewards.getInstance().inPreview) {
					Rewards.getInstance().camera.stopPreview();
				}

				Rewards.getInstance().camera.release();
				Rewards.getInstance().camera = null;
				Rewards.getInstance().inPreview = false;
				if (Rewards.getInstance().cameraid == CameraInfo.CAMERA_FACING_FRONT) {
					Rewards.getInstance().cameraid = CameraInfo.CAMERA_FACING_BACK;
					Rewards.getInstance().camera = Camera.open(Rewards
							.getInstance().cameraid);
					Rewards.getInstance().camera.setDisplayOrientation(90);
				} else {
					Rewards.getInstance().cameraid = CameraInfo.CAMERA_FACING_FRONT;
					Rewards.getInstance().camera = Camera.open(Rewards
							.getInstance().cameraid);
					Rewards.getInstance().camera.setDisplayOrientation(90);

				}

				resetDisplay();
				//

			}
		});

		preview = (SurfaceView) mParentLayout.findViewById(R.id.surface);

		previewHolder = preview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		previewHolder.setFixedSize(mHomePage.getWindow().getWindowManager()
				.getDefaultDisplay().getWidth(), mHomePage.getWindow()
				.getWindowManager().getDefaultDisplay().getWidth());

	}

	//
	// public void onPause() {
	// if (inPreview) {
	// camera.stopPreview();
	// }
	//
	// camera.release();
	// camera = null;
	// inPreview = false;
	// // super.onPause();
	// }
	//
	// public void onResume() {
	// // super.onResume();
	// camera = Camera.open(cameraid);
	// };

	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub

			// no-op
			Log.d("", "in surface change 3");

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub

			Log.d("", "in surface change 1");

			try {
				Rewards.getInstance().camera.setPreviewDisplay(previewHolder);
			} catch (Throwable t) {
				Log.e("PreviewDemo-surfaceCallback",
						"Exception in setPreviewDisplay()", t);
				Toast.makeText(mHomePage, t.getMessage(), Toast.LENGTH_LONG)
						.show();
			}

		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub

			Log.d("", "in surface change 2");
			Camera.Parameters parameters = Rewards.getInstance().camera
					.getParameters();
			Camera.Size size = getBestPreviewSize(width, height, parameters);

			if (size != null) {
				parameters.setPreviewSize(size.width, size.height);
				Rewards.getInstance().camera.setDisplayOrientation(90);

				Rewards.getInstance().camera.setParameters(parameters);
				Rewards.getInstance().camera.startPreview();
				Rewards.getInstance().inPreview = true;
			}

		}
	};

	private Camera.Size getBestPreviewSize(int width, int height,
			Camera.Parameters parameters) {
		Camera.Size result = null;
		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;
					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}
		return (result);
	}

	public void resetDisplay() {
		try {
			Rewards.getInstance().camera.setPreviewDisplay(previewHolder);
			Rewards.getInstance().camera.startPreview();
			previewHolder.addCallback(surfaceCallback);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	Camera.PictureCallback photoCallback = new Camera.PictureCallback() {
		public void onPictureTaken(final byte[] data, final Camera camera) {
			if (mHomePage.getProgressDialog() != null
					&& !mHomePage.getProgressDialog().isShowing())
				mHomePage.getProgressDialog().show();
			new Thread() {
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (Exception ex) {
					}
					onPictureTake(data, camera);
				}
			}.start();
		}
	};

	public void onPictureTake(byte[] data, Camera camera) {

		bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
		Matrix rotateRight = new Matrix();
		if (bmp.getHeight() > bmp.getWidth()) {

			// rotateRight.preRotate(90);
			Log.d("", "leftwrihdfhadf i");
			float[] mirrorY = { 1, 0, 0, 0, 1, 0, 0, 0, -1 };
			rotateRight = new Matrix();
			Matrix matrixMirrorY = new Matrix();
			matrixMirrorY.setValues(mirrorY);

			// rotateRight.postConcat(matrixMirrorY);

			rotateRight.preRotate(270);
		} else {

			// rotateRight.preRotate(90);
			if (Rewards.getInstance().cameraid != CameraInfo.CAMERA_FACING_FRONT) {

				float[] mirrorY = { -1, 0, 0, 0, 1, 0, 0, 0, 1 };
				rotateRight = new Matrix();
				Matrix matrixMirrorY = new Matrix();
				matrixMirrorY.setValues(mirrorY);

				// rotateRight.postConcat(matrixMirrorY);

				rotateRight.preRotate(90);
			} else {

				float[] mirrorY = { -1, 0, 0, 0, 1, 0, 0, 0, 1 };
				rotateRight = new Matrix();
				Matrix matrixMirrorY = new Matrix();
				matrixMirrorY.setValues(mirrorY);

				// rotateRight.postConcat(matrixMirrorY);

				rotateRight.preRotate(270);

			}
		}

		mutableBitmap = bmp.copy(Bitmap.Config.RGB_565, true);
		// mutableBitmap = Bitmap.createBitmap(mutableBitmap, 0, 0,
		// mutableBitmap.getWidth(), mutableBitmap.getHeight(),
		// rotateRight, true);
		try {
			if (mutableBitmap.getWidth() >= mutableBitmap.getHeight()) {
				mutableBitmap = Bitmap.createBitmap(
						mutableBitmap,
						mutableBitmap.getWidth() / 2
								- mutableBitmap.getHeight() / 2, 0,
						mutableBitmap.getHeight(), mutableBitmap.getHeight());

			} else {

				mutableBitmap = Bitmap.createBitmap(
						mutableBitmap,
						0,
						mutableBitmap.getHeight() / 2
								- mutableBitmap.getWidth() / 2,
						mutableBitmap.getWidth(), mutableBitmap.getWidth());
			}
		} catch (Exception e) {

			Log.i("elang", "elang -> " + mutableBitmap.getHeight());
		}

		// try {
		// Intent shareIntent = new Intent(getApplicationContext(),
		// PreviewActivity.class);
		//
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		// mHomePage.bytes = stream.toByteArray();

		combineBitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0,
				stream.toByteArray().length);

		new saveCombinedImageToDevice().execute("");

		// mHomePage.displayView(44);
		//
		//
		// shareIntent.putExtra("BitmapImage", bytes);
		// startActivity(shareIntent);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	private class saveCombinedImageToDevice extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (mHomePage.getProgressDialog() != null
					&& !mHomePage.getProgressDialog().isShowing())
				mHomePage.getProgressDialog().show();
		}

		@Override
		protected String doInBackground(String... params) {
			DateFormat dateFormatter = new SimpleDateFormat(
					"yyyy-MM-dd-hh-mm-ss");

			String fileName = "Zoes_share_" + dateFormatter.format(new Date())
					+ ".jpg";

			return savePicture(combineBitmap, fileName);
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null && !result.equals(""))
				AppConstants.showMsgDialog("", result, mHomePage);

			if (mHomePage.getProgressDialog() != null
					&& mHomePage.getProgressDialog().isShowing())
				mHomePage.getProgressDialog().dismiss();
		}
	}

	private Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "photo booth", null);
		return Uri.parse(path);
	}

	private String getRealPathFromURI(Uri uri) {
		Cursor cursor = mHomePage.getContentResolver().query(uri, null, null,
				null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cursor.getString(idx);
	}

	private String savePicture(Bitmap bm, String imgName) {
		OutputStream fOut = null;
		String result = "Error occured while saving image!";
		String strDirectory = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES + "/Zoes").toString();

		File storagePath = new File(strDirectory);
		if (!storagePath.exists())
			storagePath.mkdir();

		File f = new File(strDirectory, imgName);
		try {
			fOut = new FileOutputStream(f);

			/** Compress image **/
			bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();

			/** Update image to gallery **/
			insertImage(mHomePage.getContentResolver(), f.getAbsolutePath(),
					f.getName(), f.getName());
			result = "Image saved successfully!";
		} catch (Exception e) {
			result = e.getMessage().toString();
			e.printStackTrace();
		}

		return result;
	}

	private static final void insertImage(ContentResolver cr, String imagePath,
			String name, String description) throws FileNotFoundException {
		// Check if file exists with a FileInputStream
		FileInputStream stream = new FileInputStream(imagePath);
		try {
			Bitmap bm = BitmapFactory.decodeFile(imagePath);
			SnapInstagramImage.insertImage(cr, bm, name, description);
			bm.recycle();
			return;
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
			}
		}
	}

	private static final void insertImage(ContentResolver cr, Bitmap source,
			String title, String description) {

		ContentValues values = new ContentValues();
		values.put(Images.Media.TITLE, title);
		values.put(Images.Media.DISPLAY_NAME, title);
		values.put(Images.Media.DESCRIPTION, description);
		values.put(Images.Media.MIME_TYPE, "image/jpeg");
		// Add the date meta data to ensure the image is added at the front of
		// the gallery
		values.put(Images.Media.DATE_ADDED, System.currentTimeMillis());
		values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());

		Uri url = null;

		try {
			url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					values);

			if (source != null) {
				OutputStream imageOut = cr.openOutputStream(url);
				try {
					source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
				} finally {
					imageOut.close();
				}

				long id = ContentUris.parseId(url);
				// Wait until MINI_KIND thumbnail is generated.
				Bitmap miniThumb = Images.Thumbnails.getThumbnail(cr, id,
						Images.Thumbnails.MINI_KIND, null);
				// This is for backward compatibility.
				storeThumbnail(cr, miniThumb, id, 50F, 50F,
						Images.Thumbnails.MICRO_KIND);
			} else {
				cr.delete(url, null, null);
				url = null;
			}
		} catch (Exception e) {
			if (url != null) {
				cr.delete(url, null, null);
				url = null;
			}
		}

	}

	private static final Bitmap storeThumbnail(ContentResolver cr,
			Bitmap source, long id, float width, float height, int kind) {

		// create the matrix to scale it
		Matrix matrix = new Matrix();

		float scaleX = width / source.getWidth();
		float scaleY = height / source.getHeight();

		matrix.setScale(scaleX, scaleY);

		Bitmap thumb = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), matrix, true);

		ContentValues values = new ContentValues(4);
		values.put(Images.Thumbnails.KIND, kind);
		values.put(Images.Thumbnails.IMAGE_ID, (int) id);
		values.put(Images.Thumbnails.HEIGHT, thumb.getHeight());
		values.put(Images.Thumbnails.WIDTH, thumb.getWidth());

		Uri url = cr.insert(Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

		try {
			OutputStream thumbOut = cr.openOutputStream(url);
			thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
			thumbOut.close();
			return thumb;
		} catch (FileNotFoundException ex) {
			return null;
		} catch (IOException ex) {
			return null;
		}
	}

}
