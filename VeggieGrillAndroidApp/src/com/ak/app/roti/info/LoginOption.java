package com.ak.app.roti.info;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.R;
import com.ak.app.cb.activity.Rewards;
import com.ak.app.cb.activity.RotiHomeActivity;
import com.ak.app.cb.activity.Snap;
import com.ak.app.cb.activity.Tabbars;
import com.akl.zoes.kitchen.util.AppConstants;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class LoginOption {
	private static LoginOption screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	// private Tabbars mHomePage;
	private Activity mHomePage;
	public String pageDestination = "";

	// private ImageView logoutBtn;

	public static LoginOption getInstance() {
			screen = new LoginOption();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.info_loginoption, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	boolean isInfo = false;
	String mTabName;

	public void onCreate(boolean b, final String tabName) {
		isInfo = b;
		mTabName = tabName;
		mHomePage = Tabbars.getInstance();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Login Options").build());

//		if (mHomePage == null)
//			mHomePage = RotiHomeActivity.getInstance();

		//		ImageView signupFB = (ImageView) mParentLayout
//				.findViewById(R.id.loginoption_image_facebook);
		Button signupEmail = (Button) mParentLayout
				.findViewById(R.id.loginoption_image_signup);
		Button loginBtn = (Button) mParentLayout
				.findViewById(R.id.loginoption_image_login);
		ImageView returningusersText = (ImageView) mParentLayout
				.findViewById(R.id.loginoption_text_returningusers);
//		TextView welcomeText = (TextView) mParentLayout
//				.findViewById(R.id.welcomeText);

		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());

		TextView termofuse = (TextView) mParentLayout
				.findViewById(R.id.termTxt);
		TextView and = (TextView) mParentLayout.findViewById(R.id.andTxt);
		TextView privacy = (TextView) mParentLayout
				.findViewById(R.id.privacyTxt);
		TextView signuptermstextText2 = (TextView) mParentLayout
				.findViewById(R.id.loginoption_text_signuptermstext2);

//		final TextView signuptermstextText = (TextView) mParentLayout
//				.findViewById(R.id.loginoption_text_signuptermstext);

		AppConstants.gothamNarrowBookTextView(signuptermstextText2, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());

		AppConstants.gothamNarrowBookTextView(and, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());
		AppConstants.gothamNarrowBookTextView(termofuse, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());
		AppConstants.gothamNarrowBookTextView(privacy, 13,
				AppConstants.COLOR_LIGHT_GRAY_TEXT, mHomePage.getAssets());

//		AppConstants.fontDinLightTextView(welcomeText, 26,
//				AppConstants.COLORBLACKRGB, mHomePage.getAssets());

		String TIPS_HTML_TEXT = "By clicking the sign in or sign up buttons,<br/>you are indicating that you have read and<br/>agree to the"
				+ " <a href =\""
				+ AppConstants.URL_TERMS_OF_USE
				+ "\" ><font size=\"2\" color=\"#80b539\">terms of use</font></a> "
				+ "<font size=\"2\" color=\"#04090e\"> and </font>"
				+ " <a href =\""
				+ AppConstants.URL_PRIVACY_URL
				+ "\" ><font size=\"2\" color=\"#80b539\">privacy policy</font></a><font size=\"2\" color=\"FFFFFF\"> . </font>";
//		signuptermstextText.setText(Html.fromHtml(TIPS_HTML_TEXT));
//		signuptermstextText.setLinkTextColor(Color.parseColor("#80b539"));
//		signuptermstextText.setMovementMethod(LinkMovementMethod.getInstance());

		// Check text fonts and size in the textview and buttons
//		AppConstants.americanTypewriterTextView(returningusersText, 14,
//				AppConstants.COLORBLACKCORNERBAKERYINFO, mHomePage.getAssets());
//		signupFB.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// Info.getInstance().showFbLoginPage(isInfo);
//				if (tabName.equals("INFO") && Info.getInstance() != null) {
//					Info.getInstance().showFbLoginPage(isInfo);
//				} else if (tabName.equals("SNAP") && Snap.getInstance() != null) {
//					Snap.getInstance().showFbLoginPage(isInfo, pageDestination);
//				} else if (tabName.equals("ROTIHOMEACTIVITY")
//						&& RotiHomeActivity.getInstance() != null) {
//					RotiHomeActivity.getInstance().showFbLoginPage(isInfo,
//							pageDestination);
//				} else if (tabName.equals("REWARDS")
//						&& Rewards.getInstance() != null) {
//					Rewards.getInstance().showFbLoginPage(isInfo);
//				}
//			}
//		});
		// Signup feature implementation
		signupEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Info.getInstance().showSignUpPage(isInfo);
				if (tabName.equals("INFO") && Info.getInstance() != null) {
					Info.getInstance().showSignUpPage(isInfo);
				} else if (tabName.equals("SNAP") && Snap.getInstance() != null) {
					Snap.getInstance().showSignUpPage(isInfo, pageDestination);
				} else if (tabName.equals("ROTIHOMEACTIVITY")
						&& RotiHomeActivity.getInstance() != null) {
					RotiHomeActivity.getInstance().showSignUpPage(isInfo,
							pageDestination);
				} else if (tabName.equals("REWARDS")
						&& Rewards.getInstance() != null) {
					Rewards.getInstance().showSignUpPage(isInfo);
				}
			}
		});
		// Login feature implementation
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Info.getInstance().showLoginPage(isInfo);
				if (tabName.equals("INFO") && Info.getInstance() != null) {
					Info.getInstance().showLoginPage(isInfo);
				} else if (tabName.equals("SNAP") && Snap.getInstance() != null) {
					Snap.getInstance().showLoginPage(isInfo, pageDestination);
				} else if (tabName.equals("ROTIHOMEACTIVITY")
						&& RotiHomeActivity.getInstance() != null) {
					RotiHomeActivity.getInstance().showLoginPage(isInfo,
							pageDestination);
				} else if (tabName.equals("REWARDS")
						&& Rewards.getInstance() != null) {
					Rewards.getInstance().showLoginPage(isInfo);
				}
			}
		});

		privacy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String URL = AppConstants.URL_PRIVACY_URL;
				String title = "Privacy Policy";
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					// Info.getInstance().showTermsOfUsagePage(isInfo, URL,
					// mTabName);
					if (tabName.equals("INFO") && Info.getInstance() != null) {
						Info.getInstance().showTermsOfUsagePage(isInfo, URL,
								mTabName, title);
					} else if (tabName.equals("SNAP")
							&& Snap.getInstance() != null) {
						Snap.getInstance().showTermsOfUsagePage(isInfo, URL,
								mTabName, title);
					} else if (tabName.equals("ROTIHOMEACTIVITY")
							&& RotiHomeActivity.getInstance() != null) {
						RotiHomeActivity.getInstance().showTermsOfUsagePage(
								isInfo, URL, mTabName, title);
					} else if (tabName.equals("REWARDS")
							&& Rewards.getInstance() != null) {
						Rewards.getInstance().showTermsOfUsagePage(isInfo, URL,
								mTabName, title);
					}
				} else {
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
				}
			}
		});

		termofuse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String URL = AppConstants.URL_TERMS_OF_USE;
				String title = "Terms Of Use";
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					// Info.getInstance().showTermsOfUsagePage(isInfo, URL,
					// mTabName);
					if (tabName.equals("INFO") && Info.getInstance() != null) {
						Info.getInstance().showTermsOfUsagePage(isInfo, URL,
								mTabName, title);
					} else if (tabName.equals("SNAP")
							&& Snap.getInstance() != null) {
						Snap.getInstance().showTermsOfUsagePage(isInfo, URL,
								mTabName, title);
					} else if (tabName.equals("ROTIHOMEACTIVITY")
							&& RotiHomeActivity.getInstance() != null) {
						RotiHomeActivity.getInstance().showTermsOfUsagePage(
								isInfo, URL, mTabName, title);
					} else if (tabName.equals("REWARDS")
							&& Rewards.getInstance() != null) {
						Rewards.getInstance().showTermsOfUsagePage(isInfo, URL,
								mTabName, title);
					}
				} else {
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
				}
			}
		});
	}

	public void onCreate(boolean b, final String tabName, boolean isFromLovePage) {
		isInfo = b;
		mTabName = tabName;
		mHomePage = Tabbars.getInstance();

//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Main Sign up Page").build());

		if (mHomePage == null)
			mHomePage = RotiHomeActivity.getInstance();
//		ImageView signupFB = (ImageView) mParentLayout
//				.findViewById(R.id.loginoption_image_facebook);
		ImageView signupEmail = (ImageView) mParentLayout
				.findViewById(R.id.loginoption_image_signup);
		ImageView loginBtn = (ImageView) mParentLayout
				.findViewById(R.id.loginoption_image_login);
//		TextView welcomeText = (TextView) mParentLayout
//				.findViewById(R.id.welcomeText);
		TextView returningusersText = (TextView) mParentLayout
				.findViewById(R.id.loginoption_text_returningusers);

		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());

//		final TextView signuptermstextText = (TextView) mParentLayout
//				.findViewById(R.id.loginoption_text_signuptermstext);
//		AppConstants.americanTypewriterTextView(signuptermstextText, 13,
//				AppConstants.COLORBLACKCORNERBAKERYINFO, mHomePage.getAssets());
//		AppConstants.fontDinLightTextView(welcomeText, 26,
//				AppConstants.COLORBLACKRGB, mHomePage.getAssets());

		String TIPS_HTML_TEXT = "By clicking the sign in or sign up buttons,<br/>you are indicating that you have read and<br/>agree to the"
				+ " <a href =\""
				+ AppConstants.URL_TERMS_OF_USE
				+ "\" ><font size=\"2\" color=\"#80b539\">terms of use</font></a> "
				+ "<font size=\"2\" color=\"#04090e\"> and </font>"
				+ " <a href =\""
				+ AppConstants.URL_PRIVACY_URL
				+ "\" ><font size=\"2\" color=\"#80b539\">privacy policy</font></a><font size=\"2\" color=\"#04090e\"> . </font>";
//		signuptermstextText.setText(Html.fromHtml(TIPS_HTML_TEXT));
//		signuptermstextText.setLinkTextColor(Color.parseColor("#80b539"));
//		signuptermstextText.setMovementMethod(LinkMovementMethod.getInstance());

		// Check text fonts and size in the textview and buttons
		AppConstants.americanTypewriterTextView(returningusersText, 14,
				AppConstants.COLORBLACKCORNERBAKERYINFO, mHomePage.getAssets());
//		signupFB.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// Info.getInstance().showFbLoginPage(isInfo);
//				if (tabName.equals("INFO") && Info.getInstance() != null) {
//					Info.getInstance().showFbLoginPage(isInfo);
//				} else if (tabName.equals("SNAP") && Snap.getInstance() != null) {
//					Snap.getInstance().showFbLoginPage(isInfo, pageDestination);
//				} else if (tabName.equals("ROTIHOMEACTIVITY")
//						&& RotiHomeActivity.getInstance() != null) {
//					RotiHomeActivity.getInstance().showFbLoginPage(isInfo,
//							pageDestination);
//				} else if (tabName.equals("REWARDS")
//						&& Rewards.getInstance() != null) {
//					Rewards.getInstance().showFbLoginPage(isInfo);
//				}
//			}
//		});
		// Signup feature implementation
		signupEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Info.getInstance().showSignUpPage(isInfo);
				if (tabName.equals("INFO") && Info.getInstance() != null) {
					Info.getInstance().showSignUpPage(isInfo);
				} else if (tabName.equals("SNAP") && Snap.getInstance() != null) {
					Snap.getInstance().showSignUpPage(isInfo, pageDestination);
				} else if (tabName.equals("ROTIHOMEACTIVITY")
						&& RotiHomeActivity.getInstance() != null) {
					RotiHomeActivity.getInstance().showSignUpPage(isInfo,
							pageDestination);
				} else if (tabName.equals("REWARDS")
						&& Rewards.getInstance() != null) {
					Rewards.getInstance().showSignUpPage(isInfo);
				}
			}
		});
		// Login feature implementation
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Info.getInstance().showLoginPage(isInfo);
				if (tabName.equals("INFO") && Info.getInstance() != null) {
					Info.getInstance().showLoginPage(isInfo, true);
				} else if (tabName.equals("SNAP") && Snap.getInstance() != null) {
					Snap.getInstance().showLoginPage(isInfo, pageDestination);
				} else if (tabName.equals("ROTIHOMEACTIVITY")
						&& RotiHomeActivity.getInstance() != null) {
					RotiHomeActivity.getInstance().showLoginPage(isInfo,
							pageDestination);
				} else if (tabName.equals("REWARDS")
						&& Rewards.getInstance() != null) {
					Rewards.getInstance().showLoginPage(isInfo);
				}
			}
		});
	}
}
