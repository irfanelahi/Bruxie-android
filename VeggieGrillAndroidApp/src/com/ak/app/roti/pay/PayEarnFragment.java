package com.ak.app.roti.pay;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.PayEarnActivity;
import com.ak.app.cb.activity.R;
import com.ak.app.cb.activity.Rewards;
import com.ak.app.cb.activity.Snap;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.roti.snap.ScanBarcode;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.RefreshListner;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

public class PayEarnFragment implements RefreshListner  {

	private Tabbars mActivity= Tabbars.getInstance();
	private RelativeLayout barcodeLayout;
	private TextView textCode;
	private ImageView qrCode;
	private static PayEarnFragment screen;
	int brightness=0;
	private ImageView btnPaySetting;
	private ImageView btnManagePayment;
	private TextView textFooter;
	private TextView textPaySetting;
	private RelativeLayout rootView;
	private LayoutInflater mInflater;
	int b=0;
	private TextView textScan;
	private ImageView btnRefresh;

	private boolean isPayOptionOn = mActivity.getPreference().getBoolean(
			AppConstants.IS_PAY_OPTION_ON, false);


	
	public static PayEarnFragment getInstance() {
			screen = new PayEarnFragment();
			
			return screen;
	}

	
	public RelativeLayout setInflater(LayoutInflater inflater) {
		
		
		
		mInflater = inflater;
		rootView = (RelativeLayout) mInflater.inflate(
				R.layout.fragment_pay_earn, null);
		return rootView;
	}

	public RelativeLayout getScreenParentLayout() {
		return rootView;
	}

	public void oncreat()
	{
		
//		WindowManager.LayoutParams layout = mActivity.getWindow()
//                .getAttributes();
//        layout.screenBrightness = 1F;
//       mActivity.getWindow().setAttributes(layout);
//	
		
	
			//startActivity(new Intent(this,RefreshScreen.class));
		
		//mActivity.cPosition = 1;
	
		
		barcodeLayout = (RelativeLayout) rootView
				.findViewById(R.id.barcode_layout);
	
	barcodeLayout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
			
			@Override
			public void onViewDetachedFromWindow(View v) {
				// TODO Auto-generated method stub
			
				
			
			b++;
			}
			
			@Override
			public void onViewAttachedToWindow(View v) {
				// TODO Auto-generated method stub
				
				b++;
				
				if(b<2)
				{
					brightness=AppConstants.getbrightness(mActivity);
							
					
					AppConstants.changeScreenBrightness(mActivity);
				
				}
		
				else
				{
					
					AppConstants.setbrightness(brightness, mActivity);
					
					
					System.out.println("deatch"+b);
					
				}
				
							
			
			
			}
		});
	
		
		
	   TextView topTitle=(TextView) rootView.findViewById(R.id.topTitle);
	   AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mActivity.getAssets());
	
		textCode = (TextView) rootView.findViewById(R.id.text_code);
		qrCode = (ImageView) rootView.findViewById(R.id.qr_code);
		btnPaySetting = (ImageView) rootView.findViewById(R.id.btn_pay_setting);
		btnManagePayment = (ImageView) rootView
				.findViewById(R.id.btn_manage_payment);
		textFooter = (TextView) rootView.findViewById(R.id.footer_text);
		textPaySetting = (TextView) rootView
				.findViewById(R.id.pay_setting_text);
		textScan = (TextView) rootView.findViewById(R.id.scan_text);
		btnRefresh = (ImageView) rootView.findViewById(R.id.btn_refresh);

		AppConstants.gothamNarrowBookTextView(textPaySetting, 14,
				AppConstants.COLORWHITERGB, mActivity.getAssets());
	
	
		
		AppConstants.gothamNarrowBookTextView(textScan, 16,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mActivity.getAssets());

		AppConstants.gothamNarrowBookTextView(textCode, 14,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mActivity.getAssets());
		AppConstants.gothamNarrowBookTextView(textFooter, 16,
				AppConstants.COLOR_BLACK_PAGETITLE, mActivity.getAssets());

		changedResoureImageBtnPaySetting();

		btnPaySetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isPayOptionOn) {
					Editor edit = mActivity.getPreferenceEditor();
					edit.putBoolean(AppConstants.IS_PAY_OPTION_ON, true);
					edit.commit();
					isPayOptionOn = mActivity.getPreference().getBoolean(
							AppConstants.IS_PAY_OPTION_ON, false);
					changedResoureImageBtnPaySetting();
					new getPayCode().execute("");
					Log.i("elang", "elang jadi true");
				} else {
					Editor edit = mActivity.getPreferenceEditor();
					edit.putBoolean(AppConstants.IS_PAY_OPTION_ON, false);
					edit.commit();
					isPayOptionOn = mActivity.getPreference().getBoolean(
							AppConstants.IS_PAY_OPTION_ON, false);
					changedResoureImageBtnPaySetting();
					new getUserDetail().execute("");
					Log.i("elang", "elang jadi false");
				}

			}
		});

		btnManagePayment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//TODO:
				Snap.getInstance().showpaymentmangament();
				
				
//				MainActivity.getInstance().setNextViewName("PayEarnFragment");
//				mActivity.setDisplayView(13);
			}
		});
		btnManagePayment.setVisibility(View.GONE);

		btnRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new getUserDetail().execute("");
			//		Snap.getInstance().showcardsummery();
				
			}
		});
		
		
		
		RelativeLayout backarrow;
		backarrow=(RelativeLayout)rootView.findViewById(R.id.backarrow);
		backarrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Snap.getInstance().onBackPressed();
				
			
			}
		});
		
		
	new getUserDetail().execute("");
		
	//	new getPayCode().execute("");
	//	return rootView;
	}

	private void changedResoureImageBtnPaySetting() {
		if (!isPayOptionOn) {
			btnPaySetting.setImageResource(R.drawable.payearn_off);
			textScan.setText("SCAN AT THE REGISTER");
		} else {
			btnPaySetting.setImageResource(R.drawable.payearn_on);
			textScan.setText("SCAN AT THE REGISTER");
		}
	}

	private class getUserDetail extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (mActivity != null&&!mActivity.isFinishing() && mActivity.getProgressDialog() != null
					&& !mActivity.getProgressDialog().isShowing())
				mActivity.getProgressDialog().show();

			qrCode.setVisibility(View.GONE);
			textCode.setVisibility(View.GONE);
		}

		@Override
		protected String doInBackground(String... params) {

			String result = "";
			String authToken = mActivity.getPreference().getString(
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
			if (mActivity != null &&!mActivity.isFinishing()&& mActivity.getProgressDialog() != null
					&& mActivity.getProgressDialog().isShowing())
				mActivity.getProgressDialog().dismiss();
			if (result != null && !result.equals(""))
				try {
					JSONObject resObject = new JSONObject(result);
					Log.i("elang", "elang result -> " + resObject);
					String sucess = resObject.getString("status");
					if (sucess.equalsIgnoreCase("true")) {

						// barcode data
						String barcode_data = resObject.getString("usercode");

						// barcode image
						Bitmap bitmap = null;

						Drawable d = mActivity.getResources().getDrawable(
								R.drawable.qr_code_box);

						try {

							int width = d.getIntrinsicWidth();
							int height = Integer.valueOf(width / 3);
							bitmap = mActivity.encodeAsBitmap(barcode_data,
									BarcodeFormat.QR_CODE, width, width);
							qrCode.setImageBitmap(bitmap);

						} catch (WriterException e) {
							e.printStackTrace();
						}

						textCode.setText(barcode_data);
						qrCode.setVisibility(View.VISIBLE);
						textCode.setVisibility(View.VISIBLE);
						btnManagePayment.setVisibility(View.GONE);

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			else
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mActivity);

		}
	}

	private class getPayCode extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (mActivity != null&&!mActivity.isFinishing() && mActivity.getProgressDialog() != null
					&& !mActivity.getProgressDialog().isShowing())
				mActivity.getProgressDialog().show();

			qrCode.setVisibility(View.GONE);
			textCode.setVisibility(View.GONE);
		}

		@Override
		protected String doInBackground(String... params) {

			String result = "";
			String authToken = mActivity.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			result = WebHTTPMethodClass.httpGetServiceNCR("/user/pay_code",
					"auth_token=" + authToken + "&appkey="
							+ AppConstants.APPKEY);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			if (mActivity != null &&!mActivity.isFinishing()&& mActivity.getProgressDialog() != null
					&& mActivity.getProgressDialog().isShowing())
				mActivity.getProgressDialog().dismiss();
			if (result != null && !result.equals(""))
				try {
					JSONObject resObject = new JSONObject(result);
					
	                 String sucess = resObject.getString("status");
					if (sucess.equalsIgnoreCase("true")) {

						// barcode data
						String payCode = resObject.getString("code");
						
						if (payCode != "null" && payCode != "") {
							if (!isPayOptionOn) {
								new getUserDetail().execute("");
								btnManagePayment.setVisibility(View.GONE);
							} else {
								//btnManagePayment.setVisibility(View.VISIBLE);

								// barcode image
								Bitmap bitmap = null;

								Drawable d = mActivity.getResources().getDrawable(
										R.drawable.qr_code_box);

								try {

									int width = d.getIntrinsicWidth();
									int height = Integer.valueOf(width / 3);
									bitmap = mActivity
											.encodeAsBitmap(payCode,
													BarcodeFormat.QR_CODE,
													width, width);
									qrCode.setImageBitmap(bitmap);

								} catch (WriterException e) {
									e.printStackTrace();
								}

								textCode.setText(payCode);
								qrCode.setVisibility(View.VISIBLE);
								textCode.setVisibility(View.VISIBLE);
							}
						} else {
							if (!isPayOptionOn)
								new getUserDetail().execute("");
							else {
								Editor edit = mActivity.getPreferenceEditor();
								edit.putBoolean(AppConstants.IS_PAY_OPTION_ON,
										false);
								edit.commit();
								isPayOptionOn = mActivity.getPreference()
										.getBoolean(
												AppConstants.IS_PAY_OPTION_ON,
												false);
								Log.i("elang", "elang 1 ->" + isPayOptionOn);
								
								Snap.getInstance().showTutorial();
								
//							mActivity.setDisplayView(11);
								//TODO:
							}
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			else
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, mActivity);

		}
	}

	@Override
	public void notifyRefresh(String className) {
		// TODO Auto-generated method stub
		
	}

}
