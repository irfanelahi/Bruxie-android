package com.ak.app.roti.snap;

import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.Snap;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
import com.example.component.RoundProgress;

public class ReceiptProgress {
	private static ReceiptProgress screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;
	private Handler serviceHandlerLoc;
	private TaskService myTaskLoc;
	private RoundProgress roundProgress;
	private int progress = 0;

	public static ReceiptProgress getInstance() {
		if (screen == null)
			screen = new ReceiptProgress();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.life_goalreceiptprogress, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

//	private String receiptId = "";
//	private String surveyId = "";
	public void onCreate(/*String receiptIds, String surveyIds*/) {
//		receiptId = receiptIds;
//		surveyId = surveyIds;
		mHomePage = Tabbars.getInstance();
		TextView titleTV = (TextView) mParentLayout
				.findViewById(R.id.completegoalreceiptprogress_text_wait);
		TextView redeemtitleTV = (TextView) mParentLayout
				.findViewById(R.id.completegoalreceiptprogress_text_uploading);

		AppConstants.fontGothamBoldTextView(titleTV, 24,
				AppConstants.COLORGREEN, mHomePage.getAssets());
		AppConstants.fontGothamMediumTextView(redeemtitleTV, 16,
				AppConstants.COLORGREY, mHomePage.getAssets());
		roundProgress = (RoundProgress) mParentLayout
				.findViewById(R.id.completegoalreceiptprogress_progress_bar);

		serviceHandlerLoc = new Handler();
		myTaskLoc = new TaskService();
		serviceHandlerLoc.postDelayed(myTaskLoc, 800);
		// mHomePage.showReceiptServeyPage(receiptId, surveyId);
	}

	class TaskService implements Runnable {
		public void run() {
			if (serviceHandlerLoc == null)
				return;
			try {
				progress += 10;
				if (progress < 101)
					roundProgress.setProgress(progress);
				else if(progress == 110)
				{
					new fetchLocationFromServer().execute("");
				}
				serviceHandlerLoc.postDelayed(this, 800);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class fetchLocationFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
//			if (result != null && !result.equals("")) {
				try {
					 boolean status = Snap.getInstance().showServeyPage(/*receiptId, surveyId*/);
					 if(status)
					 {
						 progress = 0;
						 serviceHandlerLoc = null;
					 }
				} catch (Exception e) {
					e.printStackTrace();
				}
//			}
		}
	}
}

// http://blog.mediarain.com/2011/04/android-custom-progressbar-with-rounded-corners/
// http://quickandroidblog.blogspot.in/2011/08/change-android-horizontal-progress-bar.html
// http://code.google.com/p/myandroidwidgets/
//
// http://googlerdiary.blogspot.in/2011/11/custom-seek-barloading-bar-android.html
// http://stackoverflow.com/questions/3480456/seek-bar-change-path-color-from-yellow-to-white
// http://stackoverflow.com/questions/7056803/making-a-custom-skinny-progressbar-seekbar
// http://stackoverflow.com/questions/2020882/how-to-change-progress-bars-progress-color-in-android
// http://www.mokasocial.com/2011/02/create-a-custom-styled-ui-slider-seekbar-in-android/