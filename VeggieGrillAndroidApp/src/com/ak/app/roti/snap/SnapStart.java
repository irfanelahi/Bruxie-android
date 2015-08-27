//package com.ak.app.roti.snap;
//
//import android.graphics.Color;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.text.style.ForegroundColorSpan;
//import android.text.style.StyleSpan;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.TextView.BufferType;
//
//import com.ak.app.neckter.activity.R;
//import com.ak.app.neckter.activity.Info;
//import com.ak.app.neckter.activity.Tabbars;
//import com.akl.zoes.kitchen.util.AppConstants;
//
//public class SnapStart {
//	private static SnapStart screen;
//	private RelativeLayout mParentLayout;
//	private LayoutInflater mInflater;
//	private Tabbars mHomePage;
//	public boolean hasRefresh = true;
//
//	public static SnapStart getInstance() {
//		if (screen == null)
//			screen = new SnapStart();
//		return screen;
//	}
//
//	public RelativeLayout setInflater(LayoutInflater inflater) {
//		mInflater = inflater;
//		mParentLayout = (RelativeLayout) mInflater.inflate(R.layout.snap_start,
//				null);
//		return mParentLayout;
//	}
//
//	public RelativeLayout getScreenParentLayout() {
//		return mParentLayout;
//	}
//
//	public void onCreate() {
//		mHomePage = Tabbars.getInstance();
////		ImageView crossBtn = (ImageView) mParentLayout
////				.findViewById(R.id.snap_start_image_cross);
////		TextView eatenjoyText = (TextView) mParentLayout
////				.findViewById(R.id.snap_start_text_eatenjoy);
////		TextView snaptakepicText = (TextView) mParentLayout
////				.findViewById(R.id.snap_start_text_snaptakepic);
////		TextView claimuseyourText = (TextView) mParentLayout
////				.findViewById(R.id.snap_start_text_claimuseyour);
////		crossBtn.setVisibility(View.INVISIBLE);
////		TextView backText = (TextView) mParentLayout
////				.findViewById(R.id.snappage_text_back);
////		backText.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				Info.getInstance().handleBackButton();
////			}
////		});
//		TextView howtogetrewardsText = (TextView) mParentLayout
//				.findViewById(R.id.snap_start_image_howtogetrewards);
//
//		AppConstants.fontATSackersHeavyGothicTextViewBold(howtogetrewardsText, 16,
//				AppConstants.COLORWHITERGB, mHomePage.getAssets());
//
////		AppConstants.fontdinmediumTextView(eatenjoyText, 16,
////				AppConstants.COLORABOUTKATHTHI, mHomePage.getAssets());
////		AppConstants.fontdinmediumTextView(snaptakepicText, 16,
////				AppConstants.COLORABOUTKATHTHI, mHomePage.getAssets());
////		AppConstants.fontdinmediumTextView(claimuseyourText, 16,
////				AppConstants.COLORABOUTKATHTHI, mHomePage.getAssets());
//
//		SpannableString text = new SpannableString(
//				mHomePage.getString(R.string.eatenjoy));
//		SpannableString text1 = new SpannableString(
//				mHomePage.getString(R.string.snaptakepic));
//		SpannableString text2 = new SpannableString(
//				mHomePage.getString(R.string.claimuseyour));
//		text.setSpan(
//				new ForegroundColorSpan(Color.parseColor("#"
//						+ AppConstants.COLORABOUTRED)), 0, 4, 0);
//		text.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 4,
//				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////		eatenjoyText.setText(text, BufferType.SPANNABLE);
//		text1.setSpan(
//				new ForegroundColorSpan(Color.parseColor("#"
//						+ AppConstants.COLORABOUTRED)), 0, 5, 0);
//		text1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 5,
//				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////		snaptakepicText.setText(text1, BufferType.SPANNABLE);
//		text2.setSpan(
//				new ForegroundColorSpan(Color.parseColor("#"
//						+ AppConstants.COLORABOUTRED)), 0, 6, 0);
//		text2.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 6,
//				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////		claimuseyourText.setText(text2, BufferType.SPANNABLE);
//	}
//}
