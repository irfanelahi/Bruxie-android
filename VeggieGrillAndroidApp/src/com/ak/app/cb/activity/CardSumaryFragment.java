package com.ak.app.cb.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.akl.zoes.kitchen.util.AppConstants;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CardSumaryFragment  {

	private Tabbars mActivity =  Tabbars.getInstance();
	private TextView textCardNumber;
	private TextView textExpiringDate;
	private LayoutInflater mInflater;
	private RelativeLayout rootView;
	
	private static CardSumaryFragment screen;

	private ImageView btnUpdate;

	public CardSumaryFragment() {
	}
	public static CardSumaryFragment getInstance() {
		
			screen = new CardSumaryFragment();
		return screen;
	}

	

public RelativeLayout setInflater(LayoutInflater inflater) {
	mInflater = inflater;
	rootView = (RelativeLayout) mInflater.inflate(
			R.layout.fragment_card_summary, null);
	return rootView;
}

public RelativeLayout getScreenParentLayout() {
	return rootView;
}

	public void oncreat()
	{
	
		
		
		textCardNumber = (TextView) rootView
				.findViewById(R.id.text_card_number);
		textExpiringDate = (TextView) rootView
				.findViewById(R.id.text_expiring_date);
		btnUpdate = (ImageView) rootView.findViewById(R.id.btn_update);
		
		AppConstants.fontDinLightTextView(textCardNumber, 14,
				AppConstants.COLORBLACKRGB, mActivity.getAssets());
		AppConstants.fontDinLightTextView(textExpiringDate, 14,
				AppConstants.COLORBLACKRGB, mActivity.getAssets());
		
		

//		AppConstants.fontAmaticBold(textCardNumber, 20,
//				AppConstants.colorBlack, mActivity.getAssets());
//		AppConstants.fontAmaticBold(textExpiringDate, 20,
//				AppConstants.colorBlack, mActivity.getAssets());

		textCardNumber.setText("Card Number: "
				+ mActivity.maskedAccountNumber
						.replaceAll("(.{4})(?!$)", "$1 "));

		String dtStart = "2010-10-15T09:27:37Z";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat monthYearFormat = new SimpleDateFormat("MM/yyyy");
		try {
			Date date = format.parse(mActivity.expiringDateCardNumber);
			String monthYear = monthYearFormat.format(date);
			textExpiringDate.setText("Expiring: " + monthYear);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		btnUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				mActivity.setDisplayView(12);

			}
		});
	

	}

}
