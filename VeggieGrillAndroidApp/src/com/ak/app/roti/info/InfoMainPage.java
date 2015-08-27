package com.ak.app.roti.info;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.RotiHomeActivity;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;

public class InfoMainPage {
	private static InfoMainPage screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;

	public static InfoMainPage getInstance() {
		if (screen == null)
			screen = new InfoMainPage();
		return screen;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.infomainpage, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	ImageView logoutBtn;

	public void onCreate() {
//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Info Page").build());

		mHomePage = Tabbars.getInstance();
		AppConstants.changeScreenBrightnessToDefault(mHomePage);
		ImageView settingBtn = (ImageView) mParentLayout
				.findViewById(R.id.infomainpage_text_settings);
		TextView aboutrotiText = (TextView) mParentLayout
				.findViewById(R.id.infomainpage_text_about_us);
		TextView locationsText = (TextView) mParentLayout
				.findViewById(R.id.infomainpage_text_locations);
		TextView socializeText = (TextView) mParentLayout
				.findViewById(R.id.infomainpage_text_socialize);
		TextView promocodeText = (TextView) mParentLayout
				.findViewById(R.id.infomainpage_text_promocode);
		TextView contactusText = (TextView) mParentLayout
				.findViewById(R.id.infomainpage_text_contactus);
		TextView menuText = (TextView) mParentLayout
				.findViewById(R.id.infomainpage_text_menu_online);
		TextView cleanseText = (TextView) mParentLayout
				.findViewById(R.id.infomainpage_text_cleanse);
		TextView faqText = (TextView) mParentLayout
				.findViewById(R.id.infomainpage_text_faq);
//		TextView infomainpage_text_tutorial = (TextView) mParentLayout
//				.findViewById(R.id.infomainpage_text_tutorial);
		TextView infomainpage_refer_friend = (TextView) mParentLayout
				.findViewById(R.id.infomainpage_refer_friend);
		TextView infomainpage_activity_history = (TextView) mParentLayout
				.findViewById(R.id.infomainpage_activity_history);
		TextView infomainpage_text_order_online = (TextView) mParentLayout
				.findViewById(R.id.infomainpage_text_order_online);
		TextView infomainpage_social_feed = (TextView) mParentLayout
				.findViewById(R.id.infomainpage_social_feed);

		RelativeLayout backarrow;
		backarrow=(RelativeLayout)mParentLayout.findViewById(R.id.backarrow);
		backarrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Info.getInstance().onBackPressed();
			
			}
		});
		

		TextView pageTitle = (TextView) mParentLayout
				.findViewById(R.id.pageTitle);
		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		
		AppConstants.gothamNarrowMediumTextView(topTitle, 18, AppConstants.COLORWHITERGB,mHomePage.getAssets());

		
	
		
		AppConstants.odinRoundedLightTextView(pageTitle, 26,
				AppConstants.COLORBLACKRGB, mHomePage.getAssets());

		
		
		
		AppConstants.gothamNarrowBookTextView(aboutrotiText, 16, AppConstants.COLORRGRAY,mHomePage.getAssets());
		
		AppConstants.gothamNarrowBookTextView(infomainpage_activity_history, 16, AppConstants.COLORRGRAY,mHomePage.getAssets());
		
		
		AppConstants.gothamNarrowBookTextView(locationsText, 16, AppConstants.COLORRGRAY,mHomePage.getAssets());
		
		
		
		AppConstants.gothamNarrowBookTextView(socializeText, 16, AppConstants.COLORRGRAY,mHomePage.getAssets());
		
		
		
		AppConstants.gothamNarrowBookTextView(promocodeText, 16, AppConstants.COLORRGRAY,mHomePage.getAssets());
		
		
		
		AppConstants.gothamNarrowBookTextView(contactusText, 16, AppConstants.COLORRGRAY,mHomePage.getAssets());
		
		
		
		
		
		
		AppConstants.gothamNarrowBookTextView(menuText, 16, AppConstants.COLORRGRAY,mHomePage.getAssets());
		
		
		
		
		
		AppConstants.gothamNarrowBookTextView(cleanseText, 16, AppConstants.COLORRGRAY,mHomePage.getAssets());
		
		
		
		AppConstants.gothamNarrowBookTextView(faqText, 16, AppConstants.COLORRGRAY,mHomePage.getAssets());
		
		
		
//		AppConstants.gothamNarrowBookTextView(infomainpage_text_tutorial, 16, AppConstants.COLORRGRAY,mHomePage.getAssets());
		
		
		
		AppConstants.gothamNarrowBookTextView(infomainpage_refer_friend, 16, AppConstants.COLORRGRAY,mHomePage.getAssets());
		
		
		
		AppConstants.gothamNarrowBookTextView(infomainpage_social_feed, 16, AppConstants.COLORRGRAY,mHomePage.getAssets());
		
		
			
		RelativeLayout findLocationButton = (RelativeLayout) mParentLayout
				.findViewById(R.id.findLocationButton);
		RelativeLayout promoCodeButton = (RelativeLayout) mParentLayout
				.findViewById(R.id.promoCodeButton);
		RelativeLayout contactUsButton = (RelativeLayout) mParentLayout
				.findViewById(R.id.contactUsButton);
		RelativeLayout faqButton = (RelativeLayout) mParentLayout
				.findViewById(R.id.faqButton);
		RelativeLayout menuButton = (RelativeLayout) mParentLayout
				.findViewById(R.id.menuButton);
		RelativeLayout referFriendButton = (RelativeLayout) mParentLayout
				.findViewById(R.id.referFriendButton);
		RelativeLayout activityHistoryBtn = (RelativeLayout) mParentLayout
				.findViewById(R.id.activityHistoryBtn);
//		RelativeLayout tutorialButton = (RelativeLayout) mParentLayout
//				.findViewById(R.id.tutorialButton);
		RelativeLayout socializeButton = (RelativeLayout) mParentLayout
				.findViewById(R.id.socializeButton);
		RelativeLayout howToCleanseButton = (RelativeLayout) mParentLayout
				.findViewById(R.id.howToCleanseButton);
		RelativeLayout aboutUsButton = (RelativeLayout) mParentLayout
				.findViewById(R.id.aboutUsButton);
		RelativeLayout onlineOrderingButton = (RelativeLayout) mParentLayout
				.findViewById(R.id.onlineOrderingButton);
		RelativeLayout socialFeedButton = (RelativeLayout) mParentLayout
				.findViewById(R.id.socialFeedButton);

		socialFeedButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					Info.getInstance().showSocialWebView();
				} else {
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
				}
			}
		});

		activityHistoryBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (mHomePage.checkIfLogin()) {
						try {
							Info.getInstance().showViewActivityPage();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						Info info = Info.getInstance();
						info.setNextViewName("showViewActivityPage");
						info.showLoginOptionPage(true, "INFO");
					}
				}

			}
		});

		referFriendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (mHomePage.checkIfLogin()) {
						Info.getInstance().showReferFriendPage("info");
					} else {
						Info info = Info.getInstance();
						info.setNextViewName("showReferFriendPage");
						info.showLoginOptionPage(true, "INFO");

					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);

			}
		});

		// this is for get social page
		socializeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Info.getInstance().showGetSocialPage();
			}
		});
		settingBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Info.getInstance().showSettingsPage();
			}
		});
		aboutUsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Info.getInstance().showAboutRoti();
			}
		});
		faqButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Info.getInstance().showFaq();
			}
		});
		findLocationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AppConstants.CheckEnableGPS(mHomePage)) {
					Tabbars.getInstance().startGPS();
					if (AppConstants.isNetworkAvailable(mHomePage)) {
						Info.getInstance().showLocationPage();
					} else
						AppConstants.showMsgDialog("",
								AppConstants.ERRORNETWORKCONNECTION, mHomePage);
				} else
					AppConstants.showMsgDialog("",
							AppConstants.ERRORLOCATIONSERVICES, mHomePage);
			}
		});
//		tutorialButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Info.getInstance().showHowTo();
//			}
//		});

		promoCodeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (mHomePage.checkIfLogin()) {
						Info.getInstance().showPromoPage("info");
					} else {
						Info.getInstance().setNextViewName("showPromoPage");
						Info.getInstance().showLoginOptionPage(false, "INFO");
					}
				} else {
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
				}
			}
		});

		contactUsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onContactUsCreate();
			}
		});

		menuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Tracker tracker = GoogleAnalytics.getInstance(mHomePage)
//						.getTracker(AppConstants.GA_TRACK_ID);
//				tracker.send(MapBuilder.createAppView()
//						.set(Fields.SCREEN_NAME, "Menu").build());
				Info.getInstance().showWebView(AppConstants.URL_MENU_ONLINE);
			}
		});

		howToCleanseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Tracker tracker = GoogleAnalytics.getInstance(mHomePage)
//						.getTracker(AppConstants.GA_TRACK_ID);
//				tracker.send(MapBuilder.createAppView()
//						.set(Fields.SCREEN_NAME, "How To Cleanse").build());
				Info.getInstance().showWebView(AppConstants.URL_HOW_TO_CLEANSE);
			}
		});

		onlineOrderingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Tracker tracker = GoogleAnalytics.getInstance(mHomePage)
//						.getTracker(AppConstants.GA_TRACK_ID);
//				tracker.send(MapBuilder.createAppView()
//						.set(Fields.SCREEN_NAME, "Online Ordering").build());
				Info.getInstance().showWebView(AppConstants.URL_ORDER_ONLINE);
			}
		});

	}

	private void showconfirmDialog() {
		try {
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(mHomePage);
			alt_bld.setMessage(
					"If this is your first time ordering with us, after selecting your order items you must choose 'Create Account' or 'Checkout as Guest'.")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									Info.getInstance().showWebView(
											AppConstants.URL_ORDER_ONLINE);

									Editor edit = mHomePage
											.getPreferenceEditor();
									edit.putBoolean(
											AppConstants.IS_ORDER_ONLINE_POPUPOPEN,
											true);
									edit.commit();
								}
							});
			AlertDialog alert = alt_bld.create();
			alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
			alert.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public   void onContactUsCreate() {
		mHomePage.doNotFinishAllActivities = true;
		
		
		final Intent emailIntent = new Intent(
				android.content.Intent.ACTION_SEND);
		emailIntent.setType("message/rfc822");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { AppConstants.EMAILCONTACT_US });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				AppConstants.EMAILSUBJECTFAQ);
		emailIntent
				.putExtra(android.content.Intent.EXTRA_TEXT, getDeviceName());
		mHomePage.startActivity(Intent.createChooser(emailIntent, "Email"));
	}

	public String getDeviceName() {
		String texts = "\n\n";

		try {
			PackageInfo pInfo;
			pInfo = mHomePage.getPackageManager().getPackageInfo(
					mHomePage.getPackageName(), 0);
			String version = pInfo.versionName;
			String androidOS = Build.VERSION.RELEASE;
			String manufacturer = Build.MANUFACTURER;
			String model = Build.MODEL;
			if (model.startsWith(manufacturer)) {
				texts = texts + capitalize(model);
			} else {
				texts = texts + capitalize(manufacturer) + " " + model;
			}
			texts = texts + " " + androidOS + " \nApp Version " + version;
			String email = mHomePage.getPreference().getString(
					AppConstants.PREFLOGINID, "");
			if (!email.equals(""))
				texts = texts + "  \nEmail used " + email;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return texts;
	}

	private String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}
}
