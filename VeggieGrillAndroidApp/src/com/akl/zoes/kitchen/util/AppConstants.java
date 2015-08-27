package com.akl.zoes.kitchen.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.ak.app.cb.activity.Info;
import com.ak.app.cb.activity.RotiHomeActivity;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.roti.info.PromoPage;

public class AppConstants {

	// INTENT_EXTRA CONSTATNS

	public static final String INTENT_EXTRA_HOME_TABNUMBER = "homepage.tab.number";
	public static final String INTENT_EXTRA_HOME_ISFROMTAB = "homepage.tab.isfromTab";

	// INTENT_EXTRA CONSTATNS

	// PREF CONSTATNS

	public static final String PREF_HOME_ISOPENHOMEPAGE = "PREFHOMEPAGE_ISOPEN";
	public static final String PREF_SNAP_ISNOTOPENSTARTPAGE = "PREFSNAPSTART_ISOPEN";
	public static final String IS_ORDER_ONLINE_POPUPOPEN = "IS_ORDER_ONLINE_POPUPOPEN";

	// PREF CONSTATNS

	// DIALOG CONSTATNS
	public static final String TAG_APP = "RotiTAG ** ";
	public static final int DIALOG_ALERT = 101;
	public static final int DIALOG_PROGRESS = 105;
	public static String PROGRESS_MSG = null;
	public static String DIALOG_MSG = "";
	public static String CONNECTION_FAILURE = "A connection failure occurred";
	public static String ClaimRewardPageHeader = "Are you done?";
	public static String ClaimRewardPageMessage = "Do NOT exit this page until the cashier has seen your 3-digit code. \n\n"
			+ "Tapping \"Continue\" will return you to My Goddies. You will not be able to access this code again.";
	public static String TIME_OUT = "The request timed out";
	public static final String DEVICE_TYPE = "android";
	public static final String REGISTERTYPE = "1";
	public static final String REGISTERTYPEFB = "2";

	public static final String EMAILCONTACT_US = "loremipsum@rlvt.net";
	public static final String EMAILFAQ_CONTACT_US = "CornerBakery@rlvt.net";// "zoes@rlvt.net";
	public static final String EMAILSUBJECT = "Corner Bakery Cafe promo code query";
	public static final String EMAILSUBJECTFAQ = "Corner Bakery Cafe query";
	public static String EMAILSUBJECT1 = "Corner Bakery Cafe";
	public static String EMAILBODY = "Corner Bakery Cafe";
	public static String EMAILSUBJECTFB = "Corner Bakery Cafe";
	public static String EMAILBODYFB = "Corner Bakery Cafe";
	public static String EMAILSUBJECTTWT = "Corner Bakery Cafe";
	public static String EMAILBODYTWT = "Corner Bakery Cafe";
	public static String CONSTANTTITLEMESSAGE = "My Corner";

	public static final String ERROR401SERVICES = "Please login with different id";
	public static String ERROR401 = "";
	public static final String ERRORLOCATIONSERVICES = "Please Turn On Location Services (in Settings) to allow My Corner to reward you!";
	public static final String ERRORNETWORKCONNECTION = "Could not connect to server, please check your network connection";
	public static final String ERRORGOALCOMPLETE = "Mark this goal as complete?";
	public static final String ERRORFAILEDAPI = "We are sorry! Something went wrong. Please try again. If problem persists, contact customer support from the info section of the app for further assistance.";

	// PUSH PARAMS

	public static final String PUSH_NOTIFICATION_KEY = "389484218706";
	public static final String PUSH_NOTIFICATION_TAG = "My Corner Message";
	public static final int PUSH_NOTIFICATION_ID = 1234;
	public static final String PUSH_NOTIFICATION_MESSAGE = "packageName.push.message";
	public static final String PUSH_NOTIFICATION_CLASS = "packageName.push.classname";

	// FACEBOOK PARAMS

	public static final String FACEBOOK_APPID = "382775391756573"; // ROTI
	public static final String[] FACEBOOK_PERMISSIONARR = new String[] {
			"read_stream", "email", "user_photos", "publish_checkins",
			"publish_stream", "offline_access", "photo_upload" };
	public static String FB_APPID = "382775391756573";
	public static final String POSTID = "postid";

	// FACEBOOK PARAMS

	// TWITTER KEY

	// Nekter
//	public static final String CONSUMER_KEY = "aCiBBN3oYTfiB0mLd4V6mqirl";
//	public static final String CONSUMER_SECRET = "GrFCTo0sMegT34lik308FqSECvBBe8Dpy7o9fJSnlkT931SL3k";
//	
	
		public static final String CONSUMER_KEY = "5Sq5qqEE5t0foVnS0QrZPIuOS";
public static final String CONSUMER_SECRET = "7isVXkbKz2wph6DiWRPxvqFLQBEK03OHZvUuHMXcxWhNJidAZQ";
public static final String TWITTER_MEDIA_API_KEY = "0dcf25567ddfcf5a1491dd1d5e0cf4e8";
	public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";
	public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-twitter";
	public static final String OAUTH_CALLBACK_HOST = "callback";
	public static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME
			+ "://" + OAUTH_CALLBACK_HOST;
	// TWITTER KEY
	public static String LOCALE_HEADER_VALUE = "en";
	/* WEB SERVICE URL CONSTANTS */

	// Live
	
	
	//public static String APPKEY = "jmP2uoMb9YHSwo9P";
	
	
	
	public static String APPKEY = "MLJf8uDdlMUnuErX";
	public static final String ROOT_URL = "http://trelevant.herokuapp.com";
	public static final String ROOT_URL_HTTPS = "https://trelevant.herokuapp.com";
	// http://app.relevantmobile.com/url/7/roti-facebook
	public static String BASE_URL = ROOT_URL + "/api/v1"; // Live
	public static String BASE_URL_HTTPS = ROOT_URL_HTTPS + "/api/v1"; // Live
	public static String BASE_URL_NCR = "https://nfrel.herokuapp.com/api/vncr";
	public static String URL_TERMS_OF_USE = ROOT_URL + "/terms_of_use?appkey="
			+ APPKEY;
	public static String URL_PRIVACY_URL = ROOT_URL + "/privacy_policy?appkey="
			+ APPKEY;
	public static String URL_FB_PAGE = ROOT_URL + "/url/55/facebook";
	public static String URL_TWITTER_PAGE = ROOT_URL + "/url/55/twitter";
	public static String URL_ORDER_ONLINE = ROOT_URL
			+ "/url/17/online-ordering";
	public static String URL_MENU_ONLINE = ROOT_URL + "/url/55/menu";
	public static String URL_CALC_ONLINE = ROOT_URL
			+ "/url/13/nutritional-calculator";
	public static String URL_INSTAGRAM_PAGE = ROOT_URL + "url/55/instagram";
	public static String URL_FAQ = ROOT_URL + "/url/15/faq";
	public static String URL_HOW_TO_CLEANSE = ROOT_URL
			+ "/url/17/how-to-cleanse";
	public static String GA_TRACK_ID = "UA-48084549-33";
	// Live

	// frel

	// public static String BASE_URL = "http://frelevant.herokuapp.com/api/v1";
	// public static String BASE_URL_HTTPS =
	// "https://frelevant.herokuapp.com/api/v1";
	// public static String URL_TERMS_OF_USE =
	// "http://frelevant.herokuapp.com/terms_of_use";
	// public static String URL_PRIVACY_URL =
	// "http://frelevant.herokuapp.com/privacy_policy";
	// public static String URL_FB_PAGE = "https://www.facebook.com/rotiusa";
	// public static String URL_TWITTER_PAGE = "http://twitter.com/rotiusa";
	// frel

	// Trel

	// public static String APPKEY = "QLd55cGxr8AnsHDB";
	// public static String BASE_URL = "http://trelevant.herokuapp.com/api/v1";
	// public static String BASE_URL_HTTPS =
	// "https://trelevant.herokuapp.com/api/v1";
	// public static String URL_TERMS_OF_USE =
	// "http://trelevant.herokuapp.com/terms_of_use?appkey="
	// + APPKEY;
	// public static String URL_PRIVACY_URL =
	// "http://trelevant.herokuapp.com/privacy_policy?appkey="
	// + APPKEY;
	// public static String URL_FB_PAGE =
	// "http://trelevant.herokuapp.com/url/41/facebook";
	// public static String URL_TWITTER_PAGE =
	// "http://trelevant.herokuapp.com/url/41/twitter";
	// public static String URL_ORDER_ONLINE =
	// "http://trelevant.herokuapp.com/url/33/online-ordering";
	// public static String URL_MENU_ONLINE =
	// "http://trelevant.herokuapp.com/url/33/menu";
	// public static String URL_CALC_ONLINE =
	// "http://trelevant.herokuapp.com/url/26/nutritional-calculator";
	// public static String URL_FAQ =
	// "http://trelevant.herokuapp.com/url/41/faq";
	// public static String URL_INSTAGRAM_PAGE =
	// "http://trelevant.herokuapp.com/url/41/instagram";
	// public static String URL_HOW_TO_CLEANSE =
	// "http://trelevant.herokuapp.com/url/33/how-to-cleanse";
	// public static String GA_TRACK_ID = "UA-48084549-2";

	// Trel

	public static String CALL_US_NOW_CELL = "CALL_US_NOW_CELL_IDENTIFIER";
	public static String OFFER_LIST_CELL = "OFFER_LIST_CELL_IDENTIFIER";
	public static String CALL_US_NOW_CUSTOM_LABEL_NAME = "1";
	public static String CALL_US_NOW_CUSTOM_LABEL_DESCRIPTION = "2";
	public static String CALL_US_NOW_CUSTOM_LABEL_DISTANCE = "3";
	public static String CALL_US_NOW_CUSTOM_BUTTON_MOBILE = "4";

	// PREFERENCE VARIABLE

	public static String PREFAUTH_TOKEN = "authtoken";
	public static String PREFPUSHREGISTRATIONID = "registrationId";
	public static String PREFLOGOUTBUTTONTAG = "loginbuttonTag";
	public static String PREFCATEGORYSELECTED = "categoryselected";
	public static String PREFLOGINID = "prefloginid";

	
	public static final String IS_PAY_OPTION_ON = "IS_PAY_OPTION_ON";
	
	// METHODS

	private static int DEFAULT_BRIGHTNESS_VALUE = -1;
	public static int DEFAULT_BRIGHTNESS = -1;
	public static Boolean isBarcodePage = false;
	// Content resolver used as a handle to the system's settings
	private static ContentResolver cResolver;
	// Window object, that will store a reference to the current window
	private static Window window;

	public static void changeScreenBrightness(Activity object) {
		// Get the content resolver
		cResolver = object.getContentResolver();

		// Get the current window
		window = object.getWindow();

		try {
			// Get the current system brightness
			DEFAULT_BRIGHTNESS_VALUE = Settings.System.getInt(cResolver,
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			// Throw an error case it couldn't be retrieved
			Log.e("Error", "Cannot access system brightness");
			e.printStackTrace();
		}

		if (DEFAULT_BRIGHTNESS_VALUE < 200) {
			// Set the system brightness using the brightness variable value
			Settings.System.putInt(cResolver,
					Settings.System.SCREEN_BRIGHTNESS, 200);
		}
	}

	public static void setScreenBrightnessToDefault(Activity object) {
		// Get the content resolver
		cResolver = object.getContentResolver();

		// Get the current window
		window = object.getWindow();

		try {
			// Get the current system brightness
			DEFAULT_BRIGHTNESS = Settings.System.getInt(cResolver,
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			// Throw an error case it couldn't be retrieved
			Log.e("Error", "Cannot access system brightness");
			e.printStackTrace();
		}

	}

	public static void changeScreenBrightnessToDefault(Activity object) {
		// Get the content resolver
		cResolver = object.getContentResolver();

		// Get the current window
		window = object.getWindow();

		Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS,
				DEFAULT_BRIGHTNESS);

	}

	public static void reduceScreenBrightness(Activity object) {
		if (DEFAULT_BRIGHTNESS_VALUE > -1) {
			// Get the content resolver
			cResolver = object.getContentResolver();

			// Get the current window
			window = object.getWindow();

			// Set the system brightness using the brightness variable value
			Settings.System
					.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS,
							DEFAULT_BRIGHTNESS_VALUE);
		}
	}

	public static boolean isNetworkAvailable(Context context) {
		boolean isNetworkEnable = false;
		try {
			ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.getState() == NetworkInfo.State.CONNECTED
					|| connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
							.getState() == NetworkInfo.State.CONNECTING) {
				isNetworkEnable = true;
			} else if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
					.getState() == NetworkInfo.State.CONNECTED
					|| connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
							.getState() == NetworkInfo.State.CONNECTING) {
				isNetworkEnable = true;
			} else
				isNetworkEnable = false;
		} catch (Exception e) {
			isNetworkEnable = false;
			e.printStackTrace();
		}
		return isNetworkEnable;
	}


	
	public static int  getbrightness(Context con)
	{
		
		int curBrightnessValue=0;
		try {
		    curBrightnessValue=android.provider.Settings.System.getInt(
		    		con.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
	System.out.println("vale"+curBrightnessValue);
		
		} catch (SettingNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		
		
return curBrightnessValue;
		
		
		
	
		
	}
	
	public static void setbrightness(int n,Context con)
	{
		android.provider.Settings.System.putInt(con.getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS, n);
	
		
	}
	public static String getDeviceID(Context context) {
		String deviceId = "";
		try {
			final TelephonyManager tm = (TelephonyManager) context
					./* getBaseContext(). */getSystemService(Context.TELEPHONY_SERVICE);

			final String tmDevice, tmSerial, androidId;
			tmDevice = "" + tm.getDeviceId();
			tmSerial = "" + tm.getSimSerialNumber();
			androidId = ""
					+ android.provider.Settings.Secure.getString(
							context.getContentResolver(),
							android.provider.Settings.Secure.ANDROID_ID);
			UUID deviceUuid = new UUID(androidId.hashCode(),
					((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
			deviceId = deviceUuid.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceId;
	}

	public static boolean CheckEnableGPS(Context context) {
		boolean isGPSEnabled = false;
		try {
			String provider = Settings.Secure.getString(
					context.getContentResolver(),
					Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			if (!provider.equals("")) {
				isGPSEnabled = true;// GPS Enabled
			} else {
				isGPSEnabled = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return isGPSEnabled;
	}

	private static boolean isConfirm = false;

	public static boolean showConfirmMsgDialog(String title,
			final String message, final String billid, Context context) {
		isConfirm = false;
		try {
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
			alt_bld.setMessage(message)
					.setCancelable(false)
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									isConfirm = false;
									dialog.cancel();
								}
							})
					.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									isConfirm = true;
									dialog.cancel();
									// new
									// deleteBillFromServer().execute(billid);
								}
							});
			AlertDialog alert = alt_bld.create();
			// alert.setTitle(title);
			alert.setTitle(CONSTANTTITLEMESSAGE);
			alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
			alert.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isConfirm;
	}

	public static Boolean check = false;

	public static void parseInput(String result, Activity mHomePage) {
		if (result != null && !result.equals("")) {
			try {
				result = checkFor401Error(result);
				if (result.equals(""))
					return;
				JSONObject resObject = new JSONObject(result);
				String sucess = "";// resObject.getString("status");
				String notice = "";
				if (resObject.has("status"))
					sucess = resObject.getString("status");
				if (resObject.has("notice")) {
					notice = resObject.getString("notice");
					if (!notice.equals("")
							&& notice.equals("Unauthorized API request.")) {
						// mHomePage.showLoginOptionPage(false);//TODO
						return;
					}
				}
				if (resObject.has("message"))
					notice = resObject.getString("message");

				String errors = "";
				String auth_token = "";
				try {
					if (resObject.has("auth_token")) {
						auth_token = resObject.getString("auth_token");
						SharedPreferences mPreference = PreferenceManager
								.getDefaultSharedPreferences(mHomePage);
						Editor prefsEditor = mPreference.edit();
						prefsEditor.putString(AppConstants.PREFAUTH_TOKEN,
								auth_token);
						Log.d("auth_token", auth_token);
						prefsEditor.commit();
					}
				} catch (Exception e) {
				}
				try {
					if (resObject.has("errors")) {
						errors = resObject.getString("errors");
					}
				} catch (Exception e) {
				}
				if (sucess != null && !sucess.equals("") && !notice.equals("")) {// false
					if (sucess.equals("true")) {
						check = true;
					} else {
						check = false;
					}
					showMsgDialog(CONSTANTTITLEMESSAGE, notice, mHomePage);

				} else {
					if (errors != null && (!errors.equals(""))) {
						showMsgDialog(CONSTANTTITLEMESSAGE, errors + notice,
								mHomePage);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static String checkFor401Error(String responseData) {
		try {
			String message = "";
			JSONObject responseJson = new JSONObject(responseData);
			if (AppConstants.ERROR401.equalsIgnoreCase("401")) {
				Log.i("Response Json Failure:", "" + responseData.toString());
				AppConstants.showMessageDialogWithNewIntent(
						AppConstants.ERROR401SERVICES,
						RotiHomeActivity.getInstance());// TODO
				AppConstants.ERROR401 = "";
				responseData = "";
			} else if (responseJson.has("status")
					&& responseJson.getBoolean("status") == false) {
				Log.i("Response Json Failure:", "" + responseJson);
				if (responseJson.has("notice"))
					message = responseJson.getString("notice");
				if (message.equals("") && responseJson.has("message"))
					message = responseJson.getString("message");
				if (!message.equals("")
						&& message.equals("Unauthorized API request.")) {
					AppConstants.showMessageDialogWithNewIntent(message,
							RotiHomeActivity.getInstance());// TODO
					responseData = "";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		AppConstants.ERROR401 = "";
		return responseData;
	}

	static AlertDialog.Builder alertDialogBuilder;

	public static void showMsgDialog(String title, final String message,
			Context context) {
		try {
			if (alertDialogBuilder == null) {
				alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder
						.setMessage(message)
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										alertDialogBuilder = null;
										dialog.cancel();
										if (check == true) {
											if (PromoPage.getInstance().isPromoPage == true) {
												Info.getInstance()
														.handleBackButton();
												PromoPage.getInstance().isPromoPage = false;
											}
										}
									}
								});
				AlertDialog alert = alertDialogBuilder.create();
				if (title.equals("")) {
					alert.setTitle(CONSTANTTITLEMESSAGE);
					alert.setIcon(AlertDialog.BUTTON_NEGATIVE);
				} else {
					alert.setTitle(title);
					alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
				}
				alert.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showMessageDialogWithNewIntent(String message,
			Context context) {
		if (alertDialogBuilder == null) {
			alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder
					.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {
									alertDialogBuilder = null;
									dialog.cancel();
									// HomePage.getInstance().showLoginOptionPage(
									// false);//TODO
								}
							});
			AlertDialog alert = alertDialogBuilder.create();
			alert.show();
		}
	}

	public static void setViewEndable(View v, boolean value) {
		  if(value) {
		   v.setEnabled(true);
		   v.setAlpha(1f);
		  } else {
		   v.setEnabled(false);
		   v.setAlpha(0.5f);
		  }
		 }
	
	// Fast Implementation
	public static StringBuilder inputStreamToString(InputStream is)
			throws IOException {
		String line = "";
		StringBuilder total = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}
		rd.close();
		return total;
	}

	public static Bitmap getBitmap(String url, /* String imageView, */
			String type) {
		Bitmap bm = null;
		try {
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(new FlushedInputStream(is));
			if (type.equals("0")/* == 0 */)
				saveImageToSdCard(url);
			bis.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return bm;
	}

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			try {
				while (totalBytesSkipped < n) {
					long bytesSkipped = in.skip(n - totalBytesSkipped);
					if (bytesSkipped == 0L) {
						int b = read();
						if (b < 0) {
							break; // we reached EOF
						} else {
							bytesSkipped = 1; // we read one byte
						}
					}
					totalBytesSkipped += bytesSkipped;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return totalBytesSkipped;
		}
	}

	private static void saveImageToSdCard(String imageView) {
		InputStream input = null;
		try {
			input = new URL((String) imageView).openStream();
			File storagePath = Environment.getExternalStorageDirectory();
			String path = storagePath.getAbsolutePath() + "/corner bakery";
			storagePath = new File(path);
			if (!storagePath.exists())
				storagePath.mkdir();
			File imageFile = new File(storagePath.getAbsolutePath()
					+ "/daily.png");
			if (imageFile.exists())
				imageFile.delete();
			OutputStream output = new FileOutputStream(new File(storagePath,
					"daily.png"));
			try {
				byte[] buffer = new byte[1024];
				int bytesRead = 0;
				while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
					output.write(buffer, 0, bytesRead);
				}
			} finally {
				output.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void createImages(Bitmap bitmapOrg, ImageView imageView
	/* ,int type */) {
		try {
			imageView.setImageBitmap(bitmapOrg);
			int width = bitmapOrg.getWidth(); /* 140 */
			int height = bitmapOrg.getHeight();
			Display display = RotiHomeActivity.getInstance().getWindowManager()// TODO
					.getDefaultDisplay();

			int maxWidth = display.getWidth() - 0;
			int maxHeight = display.getHeight() - 0;
			double newWidth = maxWidth - 0;
			double newHeight = maxHeight - 0;

			if (width <= newWidth && height <= newHeight) {
				BitmapDrawable bmd = new BitmapDrawable(bitmapOrg);
				imageView.setImageDrawable(bmd);
				imageView.setScaleType(ScaleType.FIT_CENTER);
				return;
			}
			double ratio = (double) width / height;
			if (width > newWidth || height > maxWidth/* newHeight */) {
				if (ratio > 1) {
					newHeight = newWidth / ratio;
				} else {
					newWidth = newHeight * ratio;
				}
			}
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			Matrix matrix = new Matrix();

			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,
					height, matrix, true);
			imageView.setImageBitmap(resizedBitmap);
			BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
			imageView.setImageDrawable(bmd);
			imageView.setScaleType(ScaleType.FIT_CENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Date makeDate(String dateString) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.parse(dateString);
	}

	public static int DateDiff(Date date1, Date date2) {
		int diffDay = diff(date1, date2);
		System.out.println("Different Day : " + diffDay);
		return diffDay--;
	}

	private static int diff(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(date1);
		c2.setTime(date2);
		int diffDay = 0;

		if (c1.before(c2)) {
			diffDay = countDiffDay(c1, c2);
		} else {
			diffDay = countDiffDay(c2, c1);
		}

		return diffDay;
	}

	private static int countDiffDay(Calendar c1, Calendar c2) {
		int returnInt = 0;
		while (!c1.after(c2)) {
			c1.add(Calendar.DAY_OF_MONTH, 1);
			returnInt++;
		}
		if (returnInt > 0) {
			returnInt = returnInt - 1;
		}
		return (returnInt);
	}

	// METHODS

	// FONTS

	// private static String DINEngschriftStdfont = "DINEngschriftStd.otf";//

	public static final String COLORORANGETEXTRGB = "DF821D";
	public static final String COLORBROWNTEXTRGB = "3F2513";
	public static final String COLORDARKGRAYRGB = "666666";
	public static final String COLORWHITERGB = "FFFFFF";
	public static final String COLORBLACKRGB = "000000";
	public static final String COLORRGRAY = "333333";
	
	public static final String COLORGRAYTEXTFIELD = "E1E1E1";
	public static final String COLORREDHEXA = "EC3945";
	// public static final String COLORGRAYNEKTER = "999999";
	// public static final String COLORSMOOTHGRAYNEKTER = "F6F6F6";
	public static final String COLORBLACKCORNERBAKERYINFO = "04090e";
	public static final String COLORGREYCORNERBAKERY2 = "666666";
	public static final String COLORGREYCORNERBAKERY3 = "969595";
	public static final String COLORGREYCORNERBAKERY = "000000"; // updated to
																	// be black
	public static final String COLORGREENCORNERBAKERY = "BA1019";
	public static final String COLORREDCORNERBAKERY = "b80232";
	public static final String COLORBLACCORNERBAKERY2 = "fffce4";
	public static final String COLORWHITECORNERBKERY = "ededed";
	
	
	/*
	 * Bruxie Colors - START
	 */
	public static final String COLOR_BLACK_PAGETITLE = "040809";
	public static final String COLOR_GRAY_TEXT = "040809";
	public static final String COLOR_RED_TEXT = "cd2026";
	public static final String COLOR_LIGHT_GRAY_TEXT = "8a8a8a";
	public static final String COLOR_LIGHT_GRAYBLACK = "111821";
	
	/*
	 * Bruxie Colors - END
	 */

	// public static void fontDINEngschriftStdTextView(TextView textView,
	// int size, String color, AssetManager assetManager) {
	// Typeface face = Typeface.createFromAsset(assetManager, ""
	// + DINEngschriftStdfont);
	// textView.setTextSize(size);
	// color = "#" + color;
	// textView.setTextColor(Color.parseColor(color));
	// textView.setTypeface(face/* ,Typeface.BOLD */);
	// }

	// public static void fontDINEngschriftStdBoldTextView(TextView textView,
	// int size, String color, AssetManager assetManager) {
	// Typeface face = Typeface.createFromAsset(assetManager, ""
	// + DINEngschriftStdfont);
	// textView.setTextSize(size);
	// color = "#" + color;
	// textView.setTextColor(Color.parseColor(color));
	// textView.setTypeface(face, Typeface.BOLD);
	// }

	public static void setTextViewAttribute(TextView textView, int size,
			String color, AssetManager assetManager/* , Typeface face */) {
		textView.setTextSize(size);
		color = "#" + color;
		textView.setTextColor(Color.parseColor(color));
	}

	public static void setTextViewAttributeBold(TextView textView, int size,
			String color, AssetManager assetManager/* , Typeface face */) {
		textView.setTextSize(size);
		color = "#" + color;
		textView.setTextColor(Color.parseColor(color));
		textView.setTypeface(null, Typeface.BOLD);
	}

	private static String ATSackersHeavyGothic = "ATSackersHeavyGothic.ttf";
	private static String bertholdcitybold = "berthold-city-bold.ttf";
	private static String dinbold = "din-bold.ttf";
	private static String DINEngschriftRegular = "DINEngschrift-Regular.ttf";
	private static String DINEngschriftStd = "DINEngschriftStd.ttf";
	private static String dinmedium = "din-medium.ttf";
	private static String GothamBold = "gotham_bold.ttf";
	private static String GothamLigth = "gotham_light.ttf";
	private static String GothamMedium = "gotham_medium.ttf";
	private static String MyriadMedium = "MyriadStdTilt.ttf";
	private static String ChangingMedium = "Changing.ttf";
	private static String DinBold = "din-bold.ttf";
	private static String DinLight = "din-light.ttf";
	private static String DinMedium = "din-medium.ttf";
	// added by Herlangga
	private static String odinRoundedBold = "odin-rounded.bold.otf";
	private static String odinRoundedLight = "odin-rounded.light.otf";
	private static String odinRoundedRegular = "odin-rounded.regular.otf";
	private static String americanTypewriter = "ufonts.com_american-typewriter.ttf";
	private static String arial = "arial.ttf";
	private static String arialBd = "arialbd.ttf";
	// added by Herlangga
	
	/*
	 * FONTS for Bruxie - START
	 */
	
	public static String kingthings = "Kingthings_Trypewriter_2.ttf";
	public static String gothamNarrowBlack = "GothamNarrow-Black.otf";
	public static String gothamNarrowBold = "GothamNarrow-Bold.otf";
	public static String gothamNarrowBook = "GothamNarrow-Book.otf";
	public static String gothamNarrowLight = "GothamNarrow-Light.otf";
	public static String gothamNarrowMedium = "GothamNarrow-Medium.otf";
	
	/*
	 * FONTS for Bruxie - END
	 */

	public static final String COLORKATHTHI = "5f3032";
	public static final String COLORLOCATIONKATHTHI = "5a2a2b";
	public static final String COLORABOUTRED = "902e26";
	public static final String COLORABOUTKATHTHI = "2d0a0a";
	public static final String COLORGREEN = "87c748";
	public static final String COLORGREY = "7d7d7d";
	public static final String COLORORANGE = "f58022";
	public static final String COLORDARKGREEB = "458a2a";
	public static final String COLORDARKGREY = " 464646t";
	public static final String COLORBLACKMUSHROOM = "0f0f0f";
	public static final String COLORYELLOWMUSHROOM = "ffdb1e";
	public static final String COLORREDMUSHROOM = "AA0005";
	public static final String COLORMAGENTAMUSHROOM = "880052";
	public static final String COLORAQUAMUSHROOM = "0C60A6";
	public static String CUSTOMER_ID = "customer_id";
	
	// added by Herlangga
	public static void fontArialBd(TextView textView, int size, String color,
			AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, "" + arialBd);
		setTextViewAttribute(textView, size, color, face);
	}

	
	public static class logoutAccount extends AsyncTask<String, Void, String> {
		private String nextView = "";
		
		@Override
		protected String doInBackground(String... params) {
			nextView = params[0];
			String authToken = Tabbars.getInstance().getPreference()
					.getString(PREFAUTH_TOKEN, "");
			String result = WebHTTPMethodClass.httpGetService("/user/logout",
					"auth_token=" + authToken + "&appkey=" + APPKEY);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					if (sucess != null && !sucess.equals("")
							&& sucess.equals("true")) {
						Editor prefsEditor = Tabbars.getInstance()
								.getPreferenceEditor();
						prefsEditor.putString(PREFLOGINID, "");
						prefsEditor.putBoolean(PREFLOGOUTBUTTONTAG, true);
						prefsEditor.putString(PREFAUTH_TOKEN, "");
						prefsEditor.putString(CUSTOMER_ID, "");
						prefsEditor.commit();
						if (nextView.equals("showRewardPage")) {
							//Tabbars.getInstance().setDisplayView(0);
							//MainActivity.getInstance().setDisplayView(3);
						} else if (nextView.equals("showManageAccountPage")) {
							//MainActivity.getInstance().setDisplayView(0);
							//MainActivity.getInstance().setDisplayView(7);
						}
						showMsgDialog("", ERROR401SERVICES,
								Tabbars.getInstance());
						ERROR401 = "";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	
	public static void fontArial(TextView textView, int size, String color,
			AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, "" + arial);
		setTextViewAttributeBold(textView, size, color, face);
	}

	public static void americanTypewriterTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ americanTypewriter);
		setTextViewAttributeBold(textView, size, color, face);
	}

	public static void gothamNarrowBookTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ gothamNarrowBook);
		setTextViewAttribute(textView, size, color, face);
	}
	
	public static void gothamNarrowBoldTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ gothamNarrowBold);
		setTextViewAttribute(textView, size, color, face);
	}
	
	
	public static void gothamNarrowMediumTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ gothamNarrowMedium);
		setTextViewAttribute(textView, size, color, face);
	}
	
	public static void kingthingsTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ kingthings);
		setTextViewAttribute(textView, size, color, face);
	}
	
	
	public static void odinRoundedBoldTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ odinRoundedBold);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void odinRoundedLightTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ odinRoundedLight);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void odinRoundedRegularTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ odinRoundedRegular);
		setTextViewAttribute(textView, size, color, face);
	}

	// added by Herlangga

	public static void fontDinMediumTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, "" + DinMedium);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontDinBoldTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, "" + DinBold);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontDinLightTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, "" + DinLight);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontEANBarcodeTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, "EanP72Tt.ttf");
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontMyriadMediumTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ MyriadMedium);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontChangingMediumTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ ChangingMedium);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontGothamBoldTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, "" + GothamBold);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontGothamLightTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface
				.createFromAsset(assetManager, "" + GothamLigth);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontGothamMediumTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ GothamMedium);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontATSackersHeavyGothicTextView(TextView textView,
			int size, String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ ATSackersHeavyGothic);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontbertholdcityboldTextView(TextView textView,
			int size, String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ bertholdcitybold);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontdinboldTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, "" + dinbold);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontDINEngschriftRegularTextView(TextView textView,
			int size, String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ DINEngschriftRegular);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontDINEngschriftStdTextView(TextView textView,
			int size, String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ DINEngschriftStd);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontdinmediumTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, "" + dinmedium);
		setTextViewAttribute(textView, size, color, face);
	}

	private static void setTextViewAttribute(TextView textView, int size,
			String color, Typeface face) {
		textView.setTextSize(size);
		color = "#" + color;
		textView.setTextColor(Color.parseColor(color));
		textView.setTypeface(face/* ,Typeface.BOLD */);
		// tv.setTextColor(Color.parseColor("#000000"))
	}

	public static void fontATSackersHeavyGothicTextViewBold(TextView textView,
			int size, String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ ATSackersHeavyGothic);
		setTextViewAttributeBold(textView, size, color, face);
	}

	public static void fontdinmediumTextViewBold(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, "" + dinmedium);
		setTextViewAttributeBold(textView, size, color, face);
	}

	private static void setTextViewAttributeBold(TextView textView, int size,
			String color, Typeface face) {
		textView.setTextSize(size);
		color = "#" + color;
		textView.setTextColor(Color.parseColor(color));
		textView.setTypeface(face, Typeface.BOLD);
		// tv.setTextColor(Color.parseColor("#000000"))
	}

	// FONTS
	
	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
}

// FONTS

// public static void fontDINEngschriftStdTextView(TextView textView,
// int size, String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + DINEngschriftStdfont);
// // setTextViewAttribute(textView, size, color, face);
// textView.setTextSize(size);
// color = "#" + color;
// textView.setTextColor(Color.parseColor(color));
// textView.setTypeface(face/* ,Typeface.BOLD */);
// }
//
// public static void fontDINEngschriftStdBoldTextView(TextView textView,
// int size, String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + DINEngschriftStdfont);
// // setTextViewAttribute(textView, size, color, face);
// textView.setTextSize(size);
// color = "#" + color;
// textView.setTextColor(Color.parseColor(color));
// textView.setTypeface(face, Typeface.BOLD);
// }
//
// public static void setTextViewAttribute(TextView textView, int size,
// String color, AssetManager assetManager/* , Typeface face */) {
// textView.setTextSize(size);
// color = "#" + color;
// textView.setTextColor(Color.parseColor(color));
// // textView.setTypeface(face/* ,Typeface.BOLD */);
// // tv.setTextColor(Color.parseColor("#000000"))
// }
//
// public static void setTextViewAttributeBold(TextView textView, int size,
// String color, AssetManager assetManager/* , Typeface face */) {
// textView.setTextSize(size);
// color = "#" + color;
// textView.setTextColor(Color.parseColor(color));
// textView.setTypeface(null, Typeface.BOLD);
// // tv.setTextColor(Color.parseColor("#000000"))
// }

// private static String myriadPro_Boldfont = "MyriadPro-Bold.otf";
// private static String myriadPro_Semiboldfont = "MyriadPro-Semibold.otf";
// private static String helveticafont = "Helvetica.ttf";
// private static String helveticaBoldfont = "HelveticaBold.ttf";
// private static String helveticaNeueBoldItalicfont =
// "HelveticaNeueBoldItalic.ttf";
// private static String helveticaNeueItalicfont =
// "HelveticaNeueItalic.otf";
// private static String helveticaNeueRomanfont = "HelveticaNeueRoman.otf";
// private static String verdanaItalicfont = "verdanai.ttf";//
// VerdanaItalic.ttf
// private static String verdanafont = "VerdanaRef.ttf";//

// public static void fontMyriadPro_BoldTextView(TextView textView, int
// size,
// String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + myriadPro_Boldfont);
// setTextViewAttribute(textView, size, color, face);
// }
//
// public static void fontMyriadPro_SemiboldfontTextView(TextView textView,
// int size, String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + myriadPro_Semiboldfont);
// setTextViewAttribute(textView, size, color, face);
// }
//
// public static void fontVerdanaItalicTextView(TextView textView, int size,
// String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + verdanaItalicfont);
// setTextViewAttribute(textView, size, color, face);
// }
//
// public static void fontVerdanaTextView(TextView textView, int size,
// String color, AssetManager assetManager) {
// Typeface face = Typeface
// .createFromAsset(assetManager, "" + verdanafont);
// setTextViewAttribute(textView, size, color, face);
// }
//
// public static void fontHelveticaTextView(TextView textView, int size,
// String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + helveticafont);
// setTextViewAttribute(textView, size, color, face);
// }
//
// public static void fontHelveticaBoldTextView(TextView textView, int size,
// String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + helveticaBoldfont);
// setTextViewAttribute(textView, size, color, face);
// }
//
// public static void fontHelveticaNeueBoldItalicTextView(TextView textView,
// int size, String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + helveticaNeueBoldItalicfont);
// setTextViewAttribute(textView, size, color, face);
// }
//
// public static void fontHelveticaNeueItalicTextView(TextView textView,
// int size, String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + helveticaNeueItalicfont);
// setTextViewAttribute(textView, size, color, face);
// }
//
// public static void fontHelveticaNeueRomanTextView(TextView textView,
// int size, String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + helveticaNeueRomanfont);
// setTextViewAttribute(textView, size, color, face);
// }
//

// private static void setTextViewAttribute(TextView textView, int size,
// String color, Typeface face) {
// textView.setTextSize(size);
// color = "#" + color;
// textView.setTextColor(Color.parseColor(color));
// textView.setTypeface(face/* ,Typeface.BOLD */);
// // tv.setTextColor(Color.parseColor("#000000"))
// }
// public static final String COLORORANGEHEADERRGB = "FF931B";// :
// 255,140,27
// public static final String COLORORANGETEXTRGB = "FF7F00";// : 255,127,0
// public static final String COLORDARKGRAYRGB = "555555";// : 85,85,85
// public static final String COLORBLACKRGB = "000000";// : 85,85,85

// FONTS