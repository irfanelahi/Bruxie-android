package com.ak.app.cb.activity;

import com.ak.app.roti.pay.PayEarnFragment;
import com.akl.zoes.kitchen.util.AppConstants;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PaymentTutorialFragment  {

	private Tabbars mActivity;
	private TextView textSubTitle;
	private TextView textSubTitle2;
	private TextView textTut1;
	private TextView textTut2;
	private static PaymentTutorialFragment screen;

	private RelativeLayout rootView;
	
	private LayoutInflater mInflater;
	
	private ImageView btnManagePayment;

	public PaymentTutorialFragment() {
	}
	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		rootView = (RelativeLayout) mInflater.inflate(
				R.layout.fragment_payment_tutorial, null);
		return rootView;
	}

	public static PaymentTutorialFragment getInstance() {
		screen = new PaymentTutorialFragment();
	return screen;
}

	public RelativeLayout getScreenParentLayout() {
		return rootView;
	}

	public void oncreat()
	{
	
		mActivity = Tabbars.getInstance();
		//mActivity.setPageTitle("PAY / EARN", "Pay Tutorial");

		textSubTitle = (TextView) rootView.findViewById(R.id.text_sub_title);
		textSubTitle2 = (TextView) rootView.findViewById(R.id.text_sub_title2);
		textTut1 = (TextView) rootView.findViewById(R.id.text_step1);
		textTut2 = (TextView) rootView.findViewById(R.id.text_step2);
		btnManagePayment = (ImageView) rootView
				.findViewById(R.id.btn_manage_payment);

AppConstants.kingthingsTextView(textSubTitle, 16, AppConstants.COLOR_BLACK_PAGETITLE, mActivity.getAssets());
AppConstants.gothamNarrowBookTextView(textSubTitle2, 16, AppConstants.COLORBLACKRGB,mActivity.getAssets());
AppConstants.gothamNarrowBookTextView(textTut1, 16, AppConstants.COLORBLACKRGB,mActivity.getAssets());
AppConstants.gothamNarrowBookTextView(textTut2, 16, AppConstants.COLORBLACKRGB,mActivity.getAssets());
TextView topTitle = (TextView) rootView
.findViewById(R.id.topTitle);
AppConstants.gothamNarrowMediumTextView(topTitle, 18,
AppConstants.COLORWHITERGB, mActivity.getAssets());
		
		//	AppConstants.fontAmaticBold(textSubTitle, 23, AppConstants.colorBlack,
		//mActivity.getAssets());
//		AppConstants.fontAmaticBold(textSubTitle2, 23, AppConstants.colorBlack,
//				mActivity.getAssets());
//		AppConstants.fontrobotoCondensedRegular(textTut1, 14,
//				AppConstants.COLOR_BLACK_PAGETITLE, mActivity.getAssets());
//		AppConstants.fontrobotoCondensedRegular(textTut2, 14,
//				AppConstants.colorBlack, mActivity.getAssets());

		btnManagePayment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		
				Snap.getInstance().showpaymentmangament();
				
				//		mActivity.setDisplayView(7);
			}
		});
	
	
	}
}
