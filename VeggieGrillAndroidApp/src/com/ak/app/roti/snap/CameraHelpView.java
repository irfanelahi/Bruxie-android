package com.ak.app.roti.snap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.Snap;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;

public class CameraHelpView {
	private static CameraHelpView screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;

	public static CameraHelpView getInstance() {
		if (screen == null)
			screen = new CameraHelpView();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.life_phototip, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	public void onCreate() {
		mHomePage = Tabbars.getInstance();
		TextView rateTextView = (TextView) mParentLayout
				.findViewById(R.id.phototip_text_phottotip);
		TextView reciptTextView = (TextView) mParentLayout
				.findViewById(R.id.phototip_text_pleaserecipt);
		TextView useTextView = (TextView) mParentLayout
				.findViewById(R.id.phototip_text_pleaseuse);
		TextView makeTextView = (TextView) mParentLayout
				.findViewById(R.id.phototip_text_pleasemake);
		TextView backText = (TextView) mParentLayout
				.findViewById(R.id.phototip_text_back);

		AppConstants.fontGothamMediumTextView(rateTextView, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());
		AppConstants.fontGothamMediumTextView(reciptTextView, 16,
				AppConstants.COLORGREY, mHomePage.getAssets());
		AppConstants.fontGothamMediumTextView(useTextView, 16,
				AppConstants.COLORGREY, mHomePage.getAssets());
		AppConstants.fontGothamMediumTextView(makeTextView, 16,
				AppConstants.COLORGREY, mHomePage.getAssets());

		backText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Snap.getInstance().showCameraViewPage(Tabbars.getInstance().getLocationBean()
						/*Snap.getInstance().getLocationBean()*/);
			}
		});
	}
}
