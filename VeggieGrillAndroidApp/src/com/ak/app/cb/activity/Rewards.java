package com.ak.app.cb.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ak.app.cb.activity.R;
import com.ak.app.roti.info.FbLogin;
import com.ak.app.roti.info.ForgetPassword;
import com.ak.app.roti.info.LoginOption;
import com.ak.app.roti.info.LoginPage;
import com.ak.app.roti.info.PromoPage;
import com.ak.app.roti.info.SignUp;
import com.ak.app.roti.info.TermsOfUsage;
import com.ak.app.roti.pay.RewardsFragment;
import com.ak.app.roti.reward.ClaimRewardPage;
import com.ak.app.roti.reward.ClaimRewardPageGift;
import com.ak.app.roti.reward.ConfirmationRewardRedeemedPage;
import com.ak.app.roti.reward.MyGoodies;
import com.ak.app.roti.reward.RewardRedeemedPage;
import com.ak.app.roti.reward.ShippingInfo;
import com.ak.app.roti.reward.SnapInstagramImage;
import com.ak.app.roti.reward.ViewActivity;
import com.ak.app.roti.snap.Servey;
import com.akl.app.roti.bean.MyGoodieRewardsBean;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.RefreshListner;
import com.akl.zoes.kitchen.util.RewardsBean;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class Rewards extends Activity {

	private static Rewards mRewardsPage;

	public static Rewards getInstance() {
		return mRewardsPage;
	}

	private LayoutInflater mInflater;
	private LinearLayout mParentLayout;
	private Boolean list;
	private Boolean check;
	private List<View> rewardViewList = new ArrayList<View>();

	// camera properties
	public Camera camera = null;
	public boolean inPreview = false;
	public int cameraid = CameraInfo.CAMERA_FACING_BACK;

	public Boolean isCameraShowing = false;
	public String igShareText = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		list = false;
		check = true;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rewardpage);
		mRewardsPage = this;
		mInflater = LayoutInflater.from(this);
		mParentLayout = (LinearLayout) findViewById(R.id.rewards_linear_container);
	}

	@Override
	protected void onResume() {
		super.onResume();
		camera = Camera.open(cameraid);
		com.facebook.AppEventsLogger.activateApp(getApplicationContext(),
				AppConstants.FACEBOOK_APPID);
		if (AppConstants.isNetworkAvailable(this)) {
			if (Tabbars.getInstance().checkIfLogin()) {
				if (isCameraShowing) {
					isCameraShowing = false;
					showRedeemedRewardPage(myGoodieRewardsBeanSt, mRewardCode);
				} else
					showRewardPage();
			} else {
				Rewards.getInstance().setNextViewName("showRewardPage");
				Rewards.getInstance().showLoginOptionPage(false, "REWARDS");
			}
		} else
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, this);
	}

	@Override
	protected void onPause() {
		if (inPreview) {
			camera.stopPreview();
		}

		camera.release();
		camera = null;
		inPreview = false;
		super.onPause();
	}

	public void showSnapInstagramImage() {
		SnapInstagramImage snapIgView = SnapInstagramImage.getInstance();
		RelativeLayout snapIgParentLayout = snapIgView.setInflater(mInflater);
		snapIgView.onCreate();
		setViewParams(snapIgParentLayout, "showSnapInstagramImage",
				rewardViewList);
	}

	public void showRewardPage() {
		rewardViewList.clear();
		RewardsFragment InfoView = RewardsFragment.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.oncreat();
		setViewParams(infoParentLayout, "showRewardPage", rewardViewList);
	}

	public void showViewActivityPage() {
		ViewActivity InfoView = ViewActivity.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate();
		setViewParams(infoParentLayout, "showViewActivityPage", rewardViewList);
	}

	ClaimRewardPage claimRewardPage;

	public void showClaimRewardPage(RewardsBean myGoodieRewardsBean) {
		// list = true;
		claimRewardPage = ClaimRewardPage.getInstance();
		RelativeLayout infoParentLayout = claimRewardPage
				.setInflater(mInflater);
		claimRewardPage.onCreate(myGoodieRewardsBean);
		setViewParams(infoParentLayout, "showClaimRewardPage", rewardViewList);
		isClaimReward = true;
	}

	public void showShippingInfoPage(MyGoodieRewardsBean myGoodieRewardsBean,
			String rewardLocationId) {
		ShippingInfo shippingInfoPage = ShippingInfo.getInstance();
		RelativeLayout shippingInfoParentLayout = shippingInfoPage
				.setInflater(mInflater);
		shippingInfoPage.onCreate(myGoodieRewardsBean, rewardLocationId);
		setViewParams(shippingInfoParentLayout, "showShippingInfoPage",
				rewardViewList);
	}

	// change if gifter true or false
	public void showClaimRewardPageGifter(
			RewardsBean myGoodieRewardsBean, String gifter) {
		ClaimRewardPageGift InfoView = ClaimRewardPageGift.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(myGoodieRewardsBean, gifter);
		setViewParams(infoParentLayout, "showClaimRewardPageGifter",
				rewardViewList);
	}

	public static RewardsBean myGoodieRewardsBeanSt;
	public String mRewardCode;

	public void showRedeemedRewardPage(
			RewardsBean mMyGoodieRewardsBean, String rewardCode) {
		myGoodieRewardsBeanSt = mMyGoodieRewardsBean;
		mRewardCode = rewardCode;
		RewardRedeemedPage rewardRedeemedView = RewardRedeemedPage
				.getInstance();
		RelativeLayout rewardRedeemedParentLayout = rewardRedeemedView
				.setInflater(mInflater);
		rewardRedeemedView.onCreate(mMyGoodieRewardsBean, rewardCode);
		setViewParams(rewardRedeemedParentLayout, "showRewardRedeemedPage",
				rewardViewList);
	}

	public void showConfirmationRedeemedRewardPage() {
		ConfirmationRewardRedeemedPage confirmationRewardRedeemedView = ConfirmationRewardRedeemedPage
				.getInstance();
		RelativeLayout confirmationRewardRedeemedParentLayout = confirmationRewardRedeemedView
				.setInflater(mInflater);
		confirmationRewardRedeemedView.onCreate();
		setViewParams(confirmationRewardRedeemedParentLayout,
				"showConfirmationRewardRedeemedPage", rewardViewList);
	}

	
	public void showPrmocode(String screen) {
		PromoPage confirmationRewardRedeemedView = PromoPage
				.getInstance();
		RelativeLayout confirmationRewardRedeemedParentLayout = confirmationRewardRedeemedView
				.setInflater(mInflater);
		confirmationRewardRedeemedView.onCreate(screen);
		setViewParams(confirmationRewardRedeemedParentLayout,
				"promocode", rewardViewList);
	}

	
	
	
	public void showResultPage() {
		ViewActivity InfoView = ViewActivity.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate();
		setViewParams(infoParentLayout, "showResultPage", rewardViewList);
	}

	private void setViewParams(View view, String tagName, List<View> viewList) {
		mParentLayout.removeAllViews();
		view.setTag(tagName);
		checkIfViewExist(view, rewardViewList);
		mParentLayout.addView(view);
	}

	@SuppressWarnings("unused")
	private void checkIfViewExist(View view, List<View> listView) {
		String currentViewName = (String) view.getTag();
		boolean isPresent = false;
		if (listView != null && listView.size() > 0) {
			for (int i = 0; i < listView.size(); i++) {
				String compareViewName = (String) listView.get(i).getTag();
				if (compareViewName.equalsIgnoreCase(currentViewName))
					isPresent = true;
				break;
			}
		}
		if (!isPresent)
			listView.add(view);
		// if(currentViewName.equals("showRewardPage"))
		// {
		// listView.remove(0);
		// listView.add(0,view);
		// }
	}

	public void showLoginOptionPage(boolean b, String tabName) {
		LoginOption loginOptionView = LoginOption.getInstance();
		RelativeLayout loginOptionParentLayout = loginOptionView
				.setInflater(mInflater);
		loginOptionView.onCreate(b, tabName);
		setViewParams(loginOptionParentLayout, "showLoginOptionPage",
				rewardViewList);
	}

	public void showFbLoginPage(boolean b) {
		FbLogin InfoView = FbLogin.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "REWARDS");
		setViewParams(infoParentLayout, "showInfoPage", rewardViewList);
	}

	public void showSignUpPage(boolean b) {
		SignUp InfoView = SignUp.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "REWARDS");
		setViewParams(infoParentLayout, "showFbLoginPage", rewardViewList);
	}

	public void showLoginPage(boolean b) {
		LoginPage InfoView = LoginPage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "REWARDS");
		setViewParams(infoParentLayout, "showLoginPage", rewardViewList);
	}

	public void showForgetPasswordPage(boolean b) {
		ForgetPassword InfoView = ForgetPassword.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, "REWARDS");
		setViewParams(infoParentLayout, "showForgetPasswordPage",
				rewardViewList);
	}

	public void showTermsOfUsagePage(boolean b, String URL, String tabName,
			String title) {
		TermsOfUsage InfoView = TermsOfUsage.getInstance();
		RelativeLayout infoParentLayout = InfoView.setInflater(mInflater);
		InfoView.onCreate(b, URL, tabName, title);
		setViewParams(infoParentLayout, "showInfoPage", rewardViewList);
	}

	private String nextViewName = "";

	public String getNextViewName() {
		return nextViewName;
	}

	public void setNextViewName(String nextViewName) {
		this.nextViewName = nextViewName;
	}

	public void setNextView() {
		try {
			if (nextViewName.equals("showRewardPage")) {
				// setRewardPageatTop();
				// rewardViewList.clear();
				showRewardPage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		nextViewName = "";
	}

	public void exitAppFunc() {
		RotiHomeActivity.getInstance().oPenTabView(
				0);

	
	}

	public boolean isClaimReward = false;

	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (isClaimReward) {
			claimRewardPage.handlePause(false);
			return ;
		}
		if (rewardViewList != null && rewardViewList.size() > 1) {
			handleBackButton();
		} else {
			exitAppFunc();
		}

	
	
	}
	
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			return true;
//			// if (rewardViewList != null && rewardViewList.size() > 1) {
//			// if (list == true) {
//			// ClaimRewardPage.getInstance().handleBack();
//			// check = false;
//			// } else if(check = false){
//			// handleBackButton();
//			// }
//			// } else {
//			//
//			// // ClaimRewardPage.getInstance().handleBack();
//			// if (list == true) {
//			// ClaimRewardPage.getInstance().handleBack();
//			// check = false;
//			// } else if(check = false) {
//			// exitAppFunc();
//			// }
//			// }
//			// // handleBackButton();
//			// return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// handleBackButton();
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	public void popPreviousView() {
		if (rewardViewList != null && rewardViewList.size() > 1) {
			rewardViewList.remove(rewardViewList.size() - 2);
		}
	}

	// private void setRewardPageatTop() {
	// mParentLayout.removeAllViews();
	// rewardViewList.remove(0);
	// }

	public boolean handleBackButton() {
		boolean isLast = false;
		isLast = setBackButtonHandledView(rewardViewList, 0);
		return isLast;
	}

	private boolean setBackButtonHandledView(List<View> listView, int status) {
		boolean isLast = false;
		if (listView != null && listView.size() > 1) {
			mParentLayout.removeAllViews();
			mParentLayout.addView(listView.get(listView.size() - 2));
			doRefresh((String) listView.get(listView.size() - 2).getTag());
			listView.remove(listView.size() - 1);
			isLast = true;
		} else if (listView != null && listView.size() == 1) {
			mParentLayout.removeAllViews();
			showRewardPage();
			// mParentLayout.addView(listView.get(0));
			// doRefresh((String) listView.get(0).getTag());
		} else if (listView != null && listView.size() == 0) {
			mParentLayout.removeAllViews();
			showRewardPage();
		} else
			isLast = false;
		return isLast;
	}

	private void doRefresh(String viewName) {
		if (viewName.equalsIgnoreCase("showRewardPage")) {
			showRewardPage();
			RefreshListner listner = MyGoodies.getInstance();
			if (listner != null)
				listner.notifyRefresh("showRewardPage");
		} else if (viewName.equalsIgnoreCase("showReceiptServeyPage")) {
			RefreshListner listner = Servey.getInstance();
			if (listner != null)
				listner.notifyRefresh("showReceiptServeyPage");
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Rewards Main Page").build());
	}

}
