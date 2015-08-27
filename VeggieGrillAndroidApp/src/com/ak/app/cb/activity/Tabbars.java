package com.ak.app.cb.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.app.roti.info.webView;
import com.ak.app.roti.reward.RewardRedeemedPage;
import com.ak.app.roti.snap.ScanBarcode;
import com.akl.app.roti.bean.LocationBean;
import com.akl.app.roti.bean.MyGoodieRewardsBean;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.CustomProgressDialog;
import com.akl.zoes.kitchen.util.FacebookDialog;
import com.akl.zoes.kitchen.util.GetLatLongFromGPS;
import com.akl.zoes.kitchen.util.RewardsBean;
import com.akl.zoes.kitchen.util.TwitterDialog;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
import com.dg.android.facebook.BaseRequestListener;
import com.dg.android.facebook.SessionEvents;
import com.dg.android.facebook.SessionEvents.AuthListener;
import com.dg.android.facebook.SessionEvents.LogoutListener;
import com.dg.android.facebook.SessionStore;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.google.android.gcm.GCMRegistrar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.share.twitter.PrepareRequestTokenActivity;
import com.share.twitter.TwitterUtils;

public class Tabbars extends TabActivity {
	private static final String[] mClassNameArr = { "RotiHomeActivity",
			"Rewards", "Snap", "Location", "Info" };
	private static final String[] TABS = { "RotiHomeActivity", "Rewards",
			"Snap", "Location", "Info" };
	private String mPackageName = "com.ak.app.cb.activity";
	private String userid;
	private int mTabNumber;
	private SharedPreferences mPreference;
	private TabHost tabs;
	public boolean isInBackGround;
	private Editor prefsEditor;
	public String maskedAccountNumber=" " ;
	public String expiringDateCardNumber=" ";

	// private ProgressDialog progressDialog;
//	private GetLatLongFromGPS getLatLongObj;
	private Facebook mFacebook;
	private AsyncFacebookRunner mAsyncRunner;
	private SessionListener mSessionListener = new SessionListener();
	// private Activity mActivity;
	private String messageContent;
	private String mFromClassName;
	private String androidOS = Build.VERSION.RELEASE;
	private String model = Build.MODEL;
	private String manufacturer = Build.MANUFACTURER;
	public String pageDestinationAfterFbLogin = "";
	// public SimpleFacebook mSimpleFacebook;
	public String mediaShare;
	private static Twitter twitter;
	private static RequestToken requestToken;
	static final String TWITTER_CALLBACK_URL = "oauth://t4jcallback_cb";
	private static SharedPreferences mSharedPreferences;
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLoggedIn";
	private String processSaveTwitterTokenFrom;
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	private String mediumShare;
	public boolean doNotFinishAllActivities = false;
	private ProgressBar greenProgressBar;

	static Tabbars mTabbars;

	public Boolean isAccessIg = false;

	// camera instances
	// public static byte[] bytes;
	// public static byte[] bytesSecond;

	public static RewardsBean tampMyGoodieRewardsBean;
	public static String tampRewardCode;

	public void logoutFromTwitter() {
		// Clear the shared preferences
		Editor e = mSharedPreferences.edit();
		e.remove(PREF_KEY_OAUTH_TOKEN);
		e.remove(PREF_KEY_OAUTH_SECRET);
		e.remove(PREF_KEY_TWITTER_LOGIN);
		e.commit();

	}

	public static Tabbars getInstance() {
		return mTabbars;
	}

	public boolean checkIfLogin() {
		String authToken = mPreference.getString(AppConstants.PREFAUTH_TOKEN,
				"");
		if (authToken.equals(""))
			return false;
		else
			return true;
	}

	public SharedPreferences getPreference() {
		return mPreference;
	}

	public Editor getPreferenceEditor() {
		return prefsEditor;
	}

	public GetLatLongFromGPS getGetLatLongObj() {
		return new GetLatLongFromGPS(this);
	}

	// public ProgressDialog getProgressDialog() {
	// return progressDialog;
	// }

	public void startGPS() {
//		if (getLatLongObj != null)
//			getLatLongObj.startGPS();
	}

	public void stopGPS() {
//		if (getLatLongObj != null) { // PP
//			getLatLongObj.stopLocationListening();
//		}
	}

	
	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}
	
	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	
	public Bitmap encodeAsBitmap(String contents, BarcodeFormat format,
			int img_width, int img_height) throws WriterException {
		String contentsToEncode = contents;
		if (contentsToEncode == null) {
			return null;
		}
		Map<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(contentsToEncode);
		if (encoding != null) {
			hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result;
		try {
			result = writer.encode(contentsToEncode, format, img_width,
					img_height, hints);
		} catch (IllegalArgumentException iae) {
			// Unsupported format
			return null;
		}
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	
	
	public InputFilter spaceFilter = new InputFilter() {

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			for (int i = start; i < end; i++) {
				if (Character.isSpaceChar(source.charAt(i))) {
					return "";
				}
			}
			return null;
		}
	};

	
	private class updateTwitterStatusToRedeem extends
			AsyncTask<String, String, String> {

		Boolean isSent = false;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			if (getProgressDialog() != null)
				getProgressDialog().show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			Log.d("Tweet Text", "> " + args[0]);
			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(AppConstants.CONSUMER_KEY);
				builder.setOAuthConsumerSecret(AppConstants.CONSUMER_SECRET);

				// Access Token
				String access_token = mSharedPreferences.getString(
						PREF_KEY_OAUTH_TOKEN, "");
				// Access Token Secret
				String access_token_secret = mSharedPreferences.getString(
						PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = new AccessToken(access_token,
						access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build())
						.getInstance(accessToken);

				// Update status
				twitter4j.Status response = twitter.updateStatus(status);

				Log.d("Status", "> " + response.getText());
				isSent = true;
			} catch (TwitterException e) {
				// Error in updating status
				Log.d("Twitter Update Error", e.getMessage());
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {

			// dismiss the dialog after getting all products
			getProgressDialog().dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					getProgressDialog().dismiss();
					if (isSent) {
						showShareMsgDialog("", "Your post has been shared!",
								Tabbars.this);
						mediumShare = "2";
						new requestUserInteractionToServer().execute("");
					} else
						showShareMsgDialog("", "Your post has been cancelled!",
								Tabbars.this);
				}
			});
		}

	}

	public void returnhome()
	{
		
		RotiHomeActivity.getInstance().showHomePage(true);
		
		
	}
	
	
	private void postToTwitterToRedeem(String text) {

		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.setTitle("Post to Twitter");
		// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.twitterdialog);

		Button dialogButton = (Button) dialog
				.findViewById(R.id.twitter_but_tweet);
		TextView twitter_cancel = (TextView) dialog
				.findViewById(R.id.twitter_but_back);
		final EditText twitter_edit_message = (EditText) dialog
				.findViewById(R.id.twitter_edit_message);
		twitter_edit_message.setText(text);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// post on user's wall.

				new updateTwitterStatusToRedeem().execute(twitter_edit_message
						.getText().toString());
				dialog.dismiss();
			}
		});

		twitter_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});

		dialog.show();

	}

	public void loginToTwitterToRedeem() {
		// Check if already logged in
		if (!isTwitterLoggedInAlready()) {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(AppConstants.CONSUMER_KEY);
			builder.setOAuthConsumerSecret(AppConstants.CONSUMER_SECRET);
			twitter4j.conf.Configuration configuration = builder.build();

			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			new loginTwitterAccount().execute("reward redeemed");

		} else {
			new fetchStatusShareRequestServer().execute("");
		}
	}

	// ProgressDialog pDialog;

	private class loginTwitterAccount extends AsyncTask<String, Void, String> {
		private String processFrom = "";

		@Override
		protected void onPreExecute() {
			// pDialog = new ProgressDialog(Tabbars.this);
			// pDialog.setMessage("Loading...");
			// pDialog.show();
			// greenProgressBar.setVisibility(View.VISIBLE);
			Tabbars.getInstance().getProgressDialog().show();
		}

		@Override
		protected String doInBackground(String... params) {
			processFrom = params[0];
			try {
				// Shared Preferences
				requestToken = twitter
						.getOAuthRequestToken(TWITTER_CALLBACK_URL);

			} catch (TwitterException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			if(requestToken!=null)
			{
			
			Intent twitterIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(requestToken.getAuthenticationURL()));
			startActivity(twitterIntent);
			}
			if (processFrom.equals(""))
				ReferFriend.firstTimeTwitterLoggedin = true;
			else
				RewardRedeemedPage.firstTimeTwitterLoggedin = true;
			Tabbars.getInstance().getProgressDialog().dismiss();
			// pDialog.dismiss();
			// greenProgressBar.setVisibility(View.GONE);
		}
	}

	private void postToTwitter(String text) {

		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.setTitle("Post to Twitter");
		// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.twitterdialog);

		Button dialogButton = (Button) dialog
				.findViewById(R.id.twitter_but_tweet);
		TextView twitter_cancel = (TextView) dialog
				.findViewById(R.id.twitter_but_back);
		final EditText twitter_edit_message = (EditText) dialog
				.findViewById(R.id.twitter_edit_message);
		twitter_edit_message.setText(text);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// post on user's wall.

				new updateTwitterStatus().execute(twitter_edit_message
						.getText().toString(), "refer friend");
				dialog.dismiss();
			}
		});

		twitter_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});

		dialog.show();

	}

	/**
	 * Function to update status
	 * */
	public class updateTwitterStatus extends AsyncTask<String, String, String> {

		Boolean isSent = false;
		String requestFrom;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			// if (getProgressDialog() != null)
			// getProgressDialog().show();
			greenProgressBar.setVisibility(View.VISIBLE);
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			Log.d("Tweet Text", "> " + args[0]);
			String status = args[0];
			requestFrom = args[1];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(AppConstants.CONSUMER_KEY);
				builder.setOAuthConsumerSecret(AppConstants.CONSUMER_SECRET);

				// Access Token
				String access_token = mSharedPreferences.getString(
						PREF_KEY_OAUTH_TOKEN, "");
				// Access Token Secret
				String access_token_secret = mSharedPreferences.getString(
						PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = new AccessToken(access_token,
						access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build())
						.getInstance(accessToken);

				// Update status
				twitter4j.Status response = twitter.updateStatus(status);

				Log.d("Status", "> " + response.getText());
				isSent = true;
			} catch (TwitterException e) {
				// Error in updating status
				Log.d("Twitter Update Error", e.getMessage());
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {

			// dismiss the dialog after getting all products
			// getProgressDialog().dismiss();
			greenProgressBar.setVisibility(View.GONE);
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// getProgressDialog().dismiss();
					greenProgressBar.setVisibility(View.GONE);
					if (isSent) {
						showShareMsgDialog("", "Your post has been shared!",
								Tabbars.this);
						if (requestFrom.equals("social share")) {
							mediumShare = "2";
							new requestUserInteractionToServer().execute("");
						}
					} else
						showShareMsgDialog("", "Your post has been cancelled!",
								Tabbars.this);
				}
			});
		}

	}

	private class requestUserInteractionToServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {

			String authToken = getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			String appKey = AppConstants.APPKEY;

			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
			nameparams.add(new BasicNameValuePair("auth_token", authToken));
			nameparams.add(new BasicNameValuePair("appkey", appKey));
			nameparams.add(new BasicNameValuePair("medium_id", mediumShare));
			String result = WebHTTPMethodClass.executeHttPost(
					"/social_shares/user_interaction", nameparams);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
		}
	}

	public void showShareMsgDialog(String title, final String message,
			Context context) {
		try {
			if (alertDialogBuilder == null) // {
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
								}
							});
			AlertDialog alert = alertDialogBuilder.create();
			if (title.equals("")) {
				alert.setTitle(AppConstants.CONSTANTTITLEMESSAGE);
				alert.setIcon(AlertDialog.BUTTON_NEGATIVE);
			} else {
				alert.setTitle(title);
				alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
			}
			alert.show();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to login twitter
	 * */
	public void loginToTwitter(boolean isSocialShare) {
		// Check if already logged in
		if (!isTwitterLoggedInAlready()) {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(AppConstants.CONSUMER_KEY);
			builder.setOAuthConsumerSecret(AppConstants.CONSUMER_SECRET);
			twitter4j.conf.Configuration configuration = builder.build();

			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();
			if (isSocialShare)
				new loginTwitterAccount().execute("social share");
			else
				new loginTwitterAccount().execute("");

		} else {
			if (!isSocialShare) {
				mediaShare = "twitter";
				new fetchReferralRequestServer().execute("");
			} else
				new fetchStatusShareRequestServer().execute("");
		}
	}

	/**
	 * Check user already logged in your application using twitter Login flag is
	 * fetched from Shared Preferences
	 * */
	public boolean isTwitterLoggedInAlready() {
		// return twitter login status from Shared Preferences
		return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	}

	private class saveTwitterToken extends AsyncTask<Uri, Void, String> {

		@Override
		protected void onPreExecute() {
			// pDialog = new ProgressDialog(Tabbars.this);
			// pDialog.setMessage("Loading...");
			// pDialog.show();
			greenProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(Uri... params) {

			// oAuth verifier
			String verifier = params[0]
					.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

			try {
				// Get the access token
				AccessToken accessToken = twitter.getOAuthAccessToken(
						requestToken, verifier);

				// Shared Preferences
				Editor e = mSharedPreferences.edit();

				// After getting access token, access token secret
				// store them in application preferences

				e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
				e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
				// Store login status - true
				e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
				e.commit(); // save changes

				Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

				// Getting user details from twitter
				// For now i am getting his name only
				long userID = accessToken.getUserId();
				User user = twitter.showUser(userID);
				String username = user.getName();

			} catch (Exception e) {
				// Check log for login errors
				Log.e("Twitter Login Error", "> " + e.getMessage());
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			if (processSaveTwitterTokenFrom.equals("refer friend")) {
				RotiHomeActivity.getInstance().oPenTabView(0);
				RotiHomeActivity.getInstance().showReferFriendPage("roti");
			} else {
				RotiHomeActivity.getInstance().oPenTabView(1);
				Rewards.getInstance().showRedeemedRewardPage(
						tampMyGoodieRewardsBean, tampRewardCode);
			}

			// pDialog.dismiss();
			greenProgressBar.setVisibility(View.GONE);
		}
	}

	public class fetchStatusShareRequestServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (getProgressDialog() != null)
				getProgressDialog().show();
			// greenProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			String authToken = getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			String result = WebHTTPMethodClass.httpGetService("/social_shares",
					"appkey=" + AppConstants.APPKEY);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			if (getProgressDialog() != null)
				getProgressDialog().dismiss();
			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					if (sucess != null && !sucess.equals("")
							&& sucess.equals("true")) {
						try {
							if (resObject.has("social_share_program_id")) {

								postToTwitterToRedeem(resObject
										.getString("twitter_share_text"));

							}
						} catch (Exception e) {
						}
					} else {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
			}
			// getProgressDialog().hide();
			greenProgressBar.setVisibility(View.GONE);
		}
	}

	private ProgressDialog progressDialog;

	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}

//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//		doNotFinishAllActivities = false;
//	}

	public void hideSoftKeyboard() {
		InputMethodManager imm = (InputMethodManager) Tabbars.getInstance()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(Tabbars.getInstance().getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabbar);
		mTabbars = this;
		// mActivity = this;
		// context = this;
		mPreference = PreferenceManager
				.getDefaultSharedPreferences(Tabbars.this);
		prefsEditor = mPreference.edit();
		doNotFinishAllActivities = false;
		progressDialog = CustomProgressDialog.ctor(this);
		// progressDialog = new ProgressDialog(this);
		// progressDialog.setMessage("Loading...");
		greenProgressBar = (ProgressBar) findViewById(R.id.greenProgressBar);
//		getLatLongObj = GetLatLongFromGPS.getinstance(this);
//		if (getLatLongObj != null)
//			getLatLongObj.startGPS();

		mFacebook = new Facebook(AppConstants.FACEBOOK_APPID);
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);

		SessionStore.restore(mFacebook, this);
		SessionEvents.addAuthListener(mSessionListener);
		SessionEvents.addLogoutListener(mSessionListener);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			userid = bundle.getString("id");
			mTabNumber = bundle.getInt("TABNUMBER", 0);
		}
		if (userid == null)
			userid = "";
		if (bundle != null) {
			mTabNumber = bundle.getInt(
					AppConstants.INTENT_EXTRA_HOME_TABNUMBER, -1);
		}
		tabs = getTabHost();

		for (int i = 0; i < TABS.length; i++) {
			TabHost.TabSpec tab = tabs.newTabSpec(TABS[i]);
			ComponentName oneActivity = new ComponentName(mPackageName,
					mPackageName + "." + mClassNameArr[i]);
			Intent mIntent = new Intent();
			if (i == 0)
				mIntent.putExtra(AppConstants.INTENT_EXTRA_HOME_ISFROMTAB, true);
			mIntent.setComponent(oneActivity);
			tab.setContent(mIntent);
			tab.setIndicator(new MyTabIndicator(this, TABS[i]));

			tabs.addTab(tab);

		}
		int tabNo = 0;
		Log.e("mTabNumber MalindoAirTabs ", mTabNumber + "");
		if (mTabNumber != -1) {
			try {
				tabNo = mTabNumber;// Integer.parseInt(mTabNumber);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		tabs.setCurrentTab(tabNo);

		// click on seleccted tab
		int numberOfTabs = tabs.getTabWidget().getChildCount();
		for (int t = 0; t < numberOfTabs; t++) {
			tabs.getTabWidget().getChildAt(t)
					.setOnTouchListener(new View.OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							if (event.getAction() == MotionEvent.ACTION_UP) {
								// String currentSelectedTag =
								// tabs.getCurrentTabTag();
								// String currentTag = (String)v.getTag();
								// Log.d("3333333" +
								// this.getClass().getSimpleName(),
								// "currentSelectedTag: " + currentSelectedTag +
								// " currentTag: " + currentTag);
							}
							return false;
						}
					});
		}

		getTabWidget().getChildAt(4).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (webView.getInstance().isCheckWebView == true) {
					Info.getInstance().handleBackButton();
					webView.getInstance().isCheckWebView = false;
					webView.getInstance().isWebView = false;
				} else {
					boolean isFirstPage = false;
					while (!isFirstPage) {
						try {
							isFirstPage = !Info.getInstance()
									.handleBackButton();
						} catch (Exception e) {
							isFirstPage = true;
						}

					}
					if (isFirstPage)
						RotiHomeActivity.getInstance().oPenTabView(4);
				}

			}
		});

		getTabWidget().getChildAt(0).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				boolean isFirstPage = false;
				while (!isFirstPage) {
					isFirstPage = !RotiHomeActivity.getInstance()
							.handleBackButton();

				}
				if (isFirstPage)
					RotiHomeActivity.getInstance().oPenTabView(0);
			}
		});

		// getTabWidget().getChildAt(1).setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v) {
		// if (AppConstants.isNetworkAvailable(RotiHomeActivity
		// .getInstance())) {
		// if (RotiHomeActivity.getInstance().checkIfLogin()) {
		// RotiHomeActivity.getInstance().oPenTabView(1);
		// } else {
		// try {
		// Rewards.getInstance().handleBackButton();
		// } catch (Exception e) {
		// } finally {
		// RotiHomeActivity.getInstance().oPenTabView(1);
		// Rewards.getInstance().showLoginOptionPage(true,
		// "REWARDS");
		// }
		//
		// }
		// } else
		// AppConstants.showMsgDialog("Alert",
		// AppConstants.ERRORNETWORKCONNECTION,
		// RotiHomeActivity.getInstance());
		//
		// }
		// });

		getTabWidget().getChildAt(2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				boolean isTutorialShown = mTabbars.getPreference().getBoolean(
						AppConstants.PREF_SNAP_ISNOTOPENSTARTPAGE, false);

				if (!isTutorialShown) {

					RotiHomeActivity.getInstance().oPenTabView(2);
					Snap.getInstance().showScanTutorialPage();
					Editor editSession = mTabbars.getPreferenceEditor();
					editSession.putBoolean(
							AppConstants.PREF_SNAP_ISNOTOPENSTARTPAGE, true);
					editSession.commit();

				} else
					RotiHomeActivity.getInstance().oPenTabView(2);

			}
		});

		tabs.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// set back all text color to white
				for (int i = 0; i < tabs.getTabWidget().getChildCount(); i++) {
					TextView tv = (TextView) tabs.getTabWidget().getChildAt(i)
							.findViewById(R.id.tabbar_textView);
					tv.setTextColor(Color.parseColor("#ffffff"));
				}
				hideSoftKeyboard();
				// set selected tab text color to orange
				TextView tabTextView = (TextView) tabs.getTabWidget()
						.getChildAt(tabs.getCurrentTab())
						.findViewById(R.id.tabbar_textView);
				tabTextView.setTextColor(Color.parseColor("#f58022"));

				if (webView.getInstance().isCheckWebView == true) {
					Info.getInstance().handleBackButton();
					webView.getInstance().isCheckWebView = false;
					webView.getInstance().isWebView = false;
				}

				if (!Tabbars.getInstance().checkIfLogin())
					if (tabs.getCurrentTab() == 1) {
						try {
							Rewards.getInstance().handleBackButton();
						} catch (Exception e) {
						} finally {
							RotiHomeActivity.getInstance().oPenTabView(1);
							Rewards.getInstance().showLoginOptionPage(false,
									"REWARDS");
						}

					}

			}
		});
		String regId = mPreference.getString(
				AppConstants.PREFPUSHREGISTRATIONID, "");
		if (regId.equals("")) {
			registerPushAccount();
		}
		if (bundle != null) {
			messageContent = bundle
					.getString(AppConstants.PUSH_NOTIFICATION_MESSAGE);
			mFromClassName = bundle
					.getString(AppConstants.PUSH_NOTIFICATION_CLASS);
			if (mFromClassName != null
					&& mFromClassName.equalsIgnoreCase("C2DMRECEIVER")
					&& messageContent != null
					&& !messageContent.equalsIgnoreCase("")) {
				Log.e("messageContent:6", messageContent);
				setMessage(messageContent/* , bedgeNumber */);
				prefsEditor.putString(AppConstants.PUSH_NOTIFICATION_MESSAGE,
						messageContent);
				prefsEditor.putString(AppConstants.PUSH_NOTIFICATION_CLASS,
						mFromClassName);
				prefsEditor.commit();
			}
		}

		// Shared Preferences for twitter
		mSharedPreferences = getApplicationContext().getSharedPreferences(
				"MyPref", 0);

	}

	public void showTabs() {
		tabs.getTabWidget().setVisibility(TabWidget.VISIBLE);
	}

	//
	// Hide Tabs method
	public void hideTabs() {
		tabs.getTabWidget().setVisibility(TabWidget.GONE);
	}

	public TabHost getTabs() {
		return tabs;
	}

	public void SetTabs() {
		tabs = getTabHost();
		if (tabs != null)
			tabs.setCurrentTab(2);
	}

	public void SetcurrentTabs(int tabnum) {
		tabs = getTabHost();
		if (tabs != null)
			tabs.setCurrentTab(tabnum);
	}

	@Override
	protected void onResume() {
		super.onResume();
		com.facebook.AppEventsLogger.activateApp(getApplicationContext(),
				AppConstants.FACEBOOK_APPID);
		Log.i("elang", "elang on resume tabbars");
		if (!isAccessIg)
			doNotFinishAllActivities = false;

		isAccessIg = false;

		isInBackGround = false;
//		getLatLongObj = GetLatLongFromGPS.getinstance(this); // PP
//		if (getLatLongObj != null) { // PP
//			if (getLatLongObj.mbUpdatesStopped == true) {
//				// getLatLongObj.startGPS();
//			}
//		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// from twitter callback
		if (ReferFriend.firstTimeTwitterLoggedin
				|| GetSocial.firstTimeTwitterLoggedin
				|| RewardRedeemedPage.firstTimeTwitterLoggedin) {

			/**
			 * This if conditions is tested once is redirected from twitter
			 * page. Parse the uri to get oAuth Verifier
			 * */
			if (!mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false)) {
				Uri uri = getIntent().getData();
				if (uri != null
						&& uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
					if (ReferFriend.firstTimeTwitterLoggedin)
						processSaveTwitterTokenFrom = "refer friend";
					else
						processSaveTwitterTokenFrom = "reward redeemed";

					new saveTwitterToken().execute(uri);
				} else {
					RotiHomeActivity.getInstance().oPenTabView(0);
					if (ReferFriend.firstTimeTwitterLoggedin)
						RotiHomeActivity.getInstance().showReferFriendPage("roti");
					else {
						RotiHomeActivity.getInstance().oPenTabView(1);
						Rewards.getInstance().showRedeemedRewardPage(
								tampMyGoodieRewardsBean, tampRewardCode);
					}
				}
			} else {
				RotiHomeActivity.getInstance().oPenTabView(0);
				if (ReferFriend.firstTimeTwitterLoggedin)
					RotiHomeActivity.getInstance().showReferFriendPage("roti");
				else {
					RotiHomeActivity.getInstance().oPenTabView(1);
					Rewards.getInstance().showRedeemedRewardPage(
							tampMyGoodieRewardsBean, tampRewardCode);
				}
			}

		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
		isInBackGround = true;
		// if (progressDialog != null)
		// progressDialog.dismiss();
		greenProgressBar.setVisibility(View.GONE);
		// if (mCamera != null) {
		// mCamera.release();
		// }
//		if (getLatLongObj != null) { // PP
//			// if (getLatLongObj.mbUpdatesStopped == true) {
//			getLatLongObj.stopLocationListening();
//			// }
//		}

		if (!doNotFinishAllActivities) {
			boolean isFirstPage = false;
			while (!isFirstPage)
				try {
					isFirstPage = !RotiHomeActivity.getInstance()
							.handleBackButton();
				} catch (Exception e) {
					isFirstPage = true;
				}

			finish();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i("elang", "elang on stop tabbars donotexit : "
				+ doNotFinishAllActivities);
		if (!doNotFinishAllActivities) {
			boolean isFirstPage = false;
			while (!isFirstPage)
				try {
					isFirstPage = !RotiHomeActivity.getInstance()
							.handleBackButton();
				} catch (Exception e) {
					isFirstPage = true;
				}
			finish();
		}
	}

	public static class MyTabIndicator extends LinearLayout {
		public MyTabIndicator(Context context, String label) {
			super(context);
			View tab = LayoutInflater.from(context).inflate(
					R.layout.tab_indicator, this);
			ImageView tabimage = (ImageView) tab
					.findViewById(R.id.tabbar_imageView);
			TextView tabTextView = (TextView) tab
					.findViewById(R.id.tabbar_textView);
			if (label.equalsIgnoreCase("RotiHomeActivity")) {
				tabimage.setBackgroundResource(R.drawable.tab_home);
//				tabTextView.setText("Home");
//				tabTextView.setTextColor(Color.parseColor("#f58022"));
			} else if (label.equalsIgnoreCase("Rewards")) {
				tabimage.setBackgroundResource(R.drawable.tab_rewards);
//				tabTextView.setText("Love");
			} else if (label.equalsIgnoreCase("Snap")) {
				tabimage.setBackgroundResource(R.drawable.tab_snap);
//				tabTextView.setText("Snap");
			} else if (label.equalsIgnoreCase("Location")) {
				tabimage.setBackgroundResource(R.drawable.tab_socialize);
//				tabTextView.setText("Feed");
			} else if (label.equalsIgnoreCase("Info")) {
				tabimage.setBackgroundResource(R.drawable.tab_info);
//				tabTextView.setText("Info");
			}
		}
	}

	public void registerPushAccount() {
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		if (GCMRegistrar.isRegistered(this)) {
			Log.d("info", GCMRegistrar.getRegistrationId(this));
		}
		String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			GCMRegistrar.register(this, AppConstants.PUSH_NOTIFICATION_KEY);
			Log.d("info", GCMRegistrar.getRegistrationId(this));
			regId = GCMRegistrar.getRegistrationId(this);
		} else {
			Log.d("info", "already registered as" + regId);
		}
		prefsEditor.putString(AppConstants.PREFPUSHREGISTRATIONID, regId);
		Log.d("info", regId);
		prefsEditor.commit();
	}

	public void setMessage(String message/* , String bedgeNumber */) {
		try {
			showPushDialog(AppConstants.PUSH_NOTIFICATION_TAG, message);
			clearNotificationStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showPushDialog(String title, final String message) {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage(message).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						messageContent = "";
					}
				});
		AlertDialog alert = alt_bld.create();
		alert.setTitle(title);
		alert.setIcon(AlertDialog.BUTTON_POSITIVE);
		alert.show();
	}

	public void clearNotificationStatus() {
		try {
			NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			nm.cancel(AppConstants.PUSH_NOTIFICATION_TAG,
					AppConstants.PUSH_NOTIFICATION_ID);
			nm.cancelAll();
			prefsEditor.putString(AppConstants.PUSH_NOTIFICATION_MESSAGE, "");
			prefsEditor.putString(AppConstants.PUSH_NOTIFICATION_CLASS, "");
			prefsEditor.commit();
		} catch (Exception e) {
		}
	}

	public void fetchReferralRequest() {
		new fetchReferralRequestServer().execute("");
	}

	public class fetchReferralRequestServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (getProgressDialog() != null)
				getProgressDialog().show();
			// greenProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			String authToken = getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			String result = WebHTTPMethodClass.httpGetService(
					"/referral/email", "auth_token=" + authToken + "&appkey="
							+ AppConstants.APPKEY);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		// /referral/email result =
		// {"status":true,"email_title":"join us","email_body":"sign up with this promocode  CGAPYXXL ","referral_code":"CGAPYXXL","start_date":"2012-11-22","end_date":"2013-01-31","referral_program_title":"vfxv","incentive_title":"+30"}

		@Override
		protected void onPostExecute(String result) {
			if (getProgressDialog() != null)
				getProgressDialog().dismiss();
			// greenProgressBar.setVisibility(View.GONE);
			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					if (sucess != null && !sucess.equals("")
							&& sucess.equals("true")) {
						String email_title = "";
						String email_body = "";
						// String incentive_title = "";
						try {
							if (mediaShare == "twitter"
									&& resObject.has("email_title")) {
								mediaShare = "";
								postToTwitter(resObject
										.getString("twitter_text"));

							} else {

								if (resObject.has("email_title")) {
									email_title = resObject
											.getString("email_title");
									email_body = resObject
											.getString("email_body");
									// incentive_title =
									// resObject.getString("incentive_title");
								}
								final Intent emailIntent = new Intent(
										android.content.Intent.ACTION_SEND);
								emailIntent.setType("plain/text");
								emailIntent.putExtra(
										android.content.Intent.EXTRA_EMAIL,
										new String[] { "" });
								emailIntent.putExtra(
										android.content.Intent.EXTRA_SUBJECT,
										email_title);
								emailIntent.putExtra(
										android.content.Intent.EXTRA_TEXT,
										email_body);
								startActivity(Intent.createChooser(emailIntent,
										"Email"));
							}
						} catch (Exception e) {
						}
						// Editor prefsEditor = mHomePage.getPreferenceEditor();
						// prefsEditor.putBoolean(
						// AppConstants.PREFREFERRAL_CODE, false);
						// prefsEditor.commit();
					}
				} catch (Exception e) {
					// AppConstants.showMsgDialog("Alert",
					// AppConstants.ERRORFAILEDAPI, Tabbars.this);
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORFAILEDAPI, Tabbars.this);
					e.printStackTrace();
				}
			} else
				AppConstants.showMsgDialog("Alert",
						AppConstants.ERRORFAILEDAPI, Tabbars.this);
			AppConstants.parseInput(result, Tabbars.this);
		}
	}

	LocationBean mlocationBean;

	public LocationBean getLocationBean() {
		return mlocationBean;
	}

	public void setLocationBean(LocationBean locationBean) {
		mlocationBean = locationBean;
	}

	// FACEBOOK

	private final Handler mFacebookHandler = new Handler();
	private final Handler mTwitterHandler = new Handler();
	private String mPostText = "";
	private String mTweetMessage;
	protected String proficScore = "0";
	// Context context;
	String fbemail = "";
	String fn = "";
	String ln = "";
	String zipcode = "";

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("elang", "elang request code " + requestCode);
		if (requestCode == 99) {

			if (resultCode == RESULT_OK) {
				// tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
				// tvResult.setText(intent.getStringExtra("SCAN_RESULT"));
				String authToken = getPreference().getString(
						AppConstants.PREFAUTH_TOKEN, "");
				String barcode = data.getStringExtra("SCAN_RESULT");
				String url = AppConstants.BASE_URL + "/receipts/upload?appkey="
						+ AppConstants.APPKEY + "&auth_token=" + authToken
						+ "&barcode=" + barcode + "&offer_id=31";
				if (AppConstants.isNetworkAvailable(this)) {
					if (checkIfLogin()) {
						ScanBarcode.getInstance().new SubmitReceiptTask()
								.execute(url);
					} else {
						Snap.getInstance().showLoginOptionPage(false, "SNAP");
					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, this);
			}
			// else if (resultCode == RESULT_CANCELED) {
			// tvStatus.setText("Press a button to start a scan.");
			// tvResult.setText("Scan cancelled.");
			// }
		} else if (requestCode == 9) {
			doNotFinishAllActivities = false;
		} else {

			mFacebook.authorizeCallback(requestCode, resultCode, data);

		}
	}

	private class SessionListener implements AuthListener, LogoutListener {

		public void onAuthSucceed() {
			SessionStore.save(mFacebook, mTabbars);
			// if (progressDialog != null && progressDialog.isShowing())
			// progressDialog.cancel();
			greenProgressBar.setVisibility(View.GONE);
		}

		public void onAuthFail(String error) {
			// if (progressDialog != null && progressDialog.isShowing())
			// progressDialog.cancel();
			greenProgressBar.setVisibility(View.GONE);
		}

		public void onLogoutBegin() {
		}

		public void onLogoutFinish() {
			SessionStore.clear(mTabbars);
			// if (progressDialog != null && progressDialog.isShowing())
			// progressDialog.cancel();
			greenProgressBar.setVisibility(View.GONE);
		}
	}

	boolean hasPost = false;
	String mReferalCode = "";

	public void postMessage(boolean isPost, String mrefer) {
		hasPost = isPost;
		mReferalCode = mrefer;
		if (mFacebook.isSessionValid()) {
			if (hasPost)
				showPostMessageDialog();
			else
				getFBEmail(mReferalCode);
			hasPost = false;
		} else {
			mFacebook.authorize(mTabbars, AppConstants.FACEBOOK_PERMISSIONARR,
					new LoginDialogListener());
		}
	}

	/**
	 * FB work here:
	 * 
	 */
	private final class LoginDialogListener implements DialogListener {

		public void onComplete(Bundle values) {
			Log.i("inside>>", "LoginDialogListener calles......");
			SessionEvents.onLoginSuccess();
			if (hasPost)
				showPostMessageDialog();
			else
				getFBEmail(mReferalCode);
			hasPost = false;
		}

		public void onFacebookError(FacebookError error) {
			Log.i("inside>>",
					"onFacebookError calles......" + error.getMessage());
			SessionEvents.onLoginError(error.getMessage());
		}

		public void onError(DialogError error) {
			Log.i("inside>>", "onError calles......");
			SessionEvents.onLoginError(error.getMessage());
		}

		public void onCancel() {
			Log.i("inside>>", "onCancel calles......");
			SessionEvents.onLoginError("Action Canceled");
		}
	}

	private void getFBEmail(String mrefer) {
		Tabbars.this.runOnUiThread(new Runnable() {

			public void run() {
				// if (progressDialog != null && !progressDialog.isShowing())
				// progressDialog.show();
				// greenProgressBar.setVisibility(View.VISIBLE);
				fbProgressDialog = CustomProgressDialog.ctor(Tabbars.this);
				fbProgressDialog.show();
			}
		});
		mAsyncRunner.request("me", new SampleRequestListener());
	}

	public class SampleRequestListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			try {
				// process the response here: executed in background thread
				Log.d("Facebook-Example", "Response: " + response.toString());
				JSONObject json;
				try {
					json = Util.parseJson(response);
					fbemail = json.getString("email");
					fn = json.getString("first_name");
					ln = json.getString("last_name");
				} catch (FacebookError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// prefsEditor.putString(AppConstants.PREFLOGINID, fbemail);
				prefsEditor.putString("FBEMAIL", fbemail);
				prefsEditor.putString("fn", fn);
				prefsEditor.putString("ln", ln);
				prefsEditor.commit();
				Log.i("FB User Email:>", "FB User Email is: " + fbemail);
				// then post the processed result back to the UI thread
				// if we do not do this, an runtime exception will be generated
				// e.g. "CalledFromWrongThreadException: Only the original
				// thread that created a view hierarchy can touch its views."

				Tabbars.this.runOnUiThread(new Runnable() {

					public void run() {
						String[] param = new String[] { fbemail, fn, ln,
								zipcode };
						// new LoginAsyncTask().execute();
						new submitSignUpFBDetailsToServer().execute(param);
					}
				});
				//
			} catch (Exception e) {

				String method = "DELETE";
				Bundle params = new Bundle();
				params.putString("permission", "");

				mAsyncRunner.request("/me/permissions", params, method,
						new RequestListener() {
							@Override
							public void onComplete(String response, Object state) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onFacebookError(FacebookError e,
									Object state) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onFileNotFoundException(
									FileNotFoundException e, Object state) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onIOException(IOException e,
									Object state) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onMalformedURLException(
									MalformedURLException e, Object state) {
								// TODO Auto-generated method stub

							}
						}, null);
				try {
					String respons = mFacebook.logout(Tabbars.this);
					if (respons != null && !respons.equals(""))
						SessionEvents.onLogoutFinish();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				Log.w("Facebook-Example", "JSON Error in response");
				final String errorMessage = getString(R.string.reward_acess_email);
				Tabbars.this.runOnUiThread(new Runnable() {

					public void run() {
						// if (progressDialog != null
						// && progressDialog.isShowing())
						// progressDialog.dismiss();
						// greenProgressBar.setVisibility(View.GONE);
						fbProgressDialog.dismiss();
						AppConstants.showMsgDialog("", errorMessage, mTabbars);
					}
				});
			}
		}
	}

	public void logoutWithFB() {
		// mfacebookconnector.logout();
		// mfacebookconnector.logout();
		try {
			String response = mFacebook.logout(Tabbars.this);
			if (response != null && !response.equals(""))
				SessionEvents.onLogoutFinish();
			response = mFacebook.logout(Tabbars.this);
			if (response != null && !response.equals(""))
				SessionEvents.onLogoutFinish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ProgressDialog fbProgressDialog;

	private class submitSignUpFBDetailsToServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// if (getProgressDialog() != null)
			// getProgressDialog().show();
			// greenProgressBar.setVisibility(View.VISIBLE);
			if (fbProgressDialog != null && !fbProgressDialog.isShowing())
				fbProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String regId = mPreference.getString(
					AppConstants.PREFPUSHREGISTRATIONID, "");
			// String phone = mPreference.getString("phoneNumber", "");
			String day = mPreference.getString("day_dob", "");
			String month = mPreference.getString("month_dob", "");
			String year = mPreference.getString("year_dob", "");
			String marketing_optin = mPreference.getString("marketing_optin",
					"");
			String zipcode = mPreference.getString("zipcode", "");

			String firstName = mPreference.getString("first_name", "");
			String lastName = mPreference.getString("last_name", "");

			String android_id = mPreference.getString("android_id", "");
			List<NameValuePair> nameparams = new ArrayList<NameValuePair>();
			nameparams.add(new BasicNameValuePair("email", params[0]));
			nameparams.add(new BasicNameValuePair("first_name", firstName));
			nameparams.add(new BasicNameValuePair("last_name", lastName));
			nameparams.add(new BasicNameValuePair("zipcode", zipcode));
			// nameparams.add(new BasicNameValuePair("phone_number", phone));
			nameparams.add(new BasicNameValuePair("dob_day", day));
			nameparams.add(new BasicNameValuePair("dob_month", month));
			nameparams.add(new BasicNameValuePair("dob_year", year));
			nameparams.add(new BasicNameValuePair("android_id", android_id));
			nameparams.add(new BasicNameValuePair("marketing_optin",
					marketing_optin));
			// nameparams.add(new BasicNameValuePair("email",
			// "aj@dailygobble.com"));
			nameparams.add(new BasicNameValuePair("password", ""));
			nameparams.add(new BasicNameValuePair("latitude",
					getGetLatLongObj().getLatitude() + ""));
			nameparams.add(new BasicNameValuePair("longitude",
					getGetLatLongObj().getLongitude() + ""));
			nameparams.add(new BasicNameValuePair("register_device_type",
					AppConstants.DEVICE_TYPE));
			nameparams.add(new BasicNameValuePair("register_type",
					AppConstants.REGISTERTYPEFB));
			nameparams
					.add(new BasicNameValuePair("appkey", AppConstants.APPKEY));
			nameparams
					.add(new BasicNameValuePair("referral_code", mReferalCode));
			nameparams.add(new BasicNameValuePair("sign_in_device_type",
					AppConstants.DEVICE_TYPE));
			nameparams.add(new BasicNameValuePair("device_id", AppConstants
					.getDeviceID(Tabbars.this)));
			nameparams.add(new BasicNameValuePair("device_token", regId));
			nameparams.add(new BasicNameValuePair("phone_model", manufacturer
					+ " " + model));
			nameparams.add(new BasicNameValuePair("os", androidOS));
			// nameparams.add(new BasicNameValuePair("device_id", AppConstants
			// .getDeviceID(mActivity)));
			String result = WebHTTPMethodClass.executeHttPost("/user/signup",
					nameparams);// loginService();
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			mReferalCode = "";
			if (result != null && !result.equals("")) {
				try {
					JSONObject resObject = new JSONObject(result);
					String sucess = resObject.getString("status");
					String notice = resObject.getString("notice");
					if (sucess != null && !sucess.equals("")
							&& sucess.equals("true")) {
						prefsEditor
								.putString(AppConstants.PREFLOGINID, fbemail);
						prefsEditor.putBoolean(
								AppConstants.PREFLOGOUTBUTTONTAG, false);

						String auth_token = "";

						try {
							if (resObject.has("auth_token")) {
								auth_token = resObject.getString("auth_token");
								if (resObject.has("notice")) {
									notice = resObject.getString("notice");
								}
								prefsEditor
										.putString(AppConstants.PREFAUTH_TOKEN,
												auth_token);
								Log.d("auth_token", auth_token);
							}
						} catch (Exception e) {
						}
						prefsEditor.commit();
						// mHomePage.handleBackButton();
						if (tabs.getCurrentTab() == 0
								&& RotiHomeActivity.getInstance() != null) {
							RotiHomeActivity.getInstance().handleBackButton();
							RotiHomeActivity.getInstance().handleBackButton();
							if (pageDestinationAfterFbLogin
									.equals("referFriend"))
								RotiHomeActivity.getInstance()
										.showReferFriendPage("roti");
							else
								RotiHomeActivity.getInstance().setNextView();

						}
						if (tabs.getCurrentTab() == 1
								&& Rewards.getInstance() != null) {
							Rewards.getInstance().handleBackButton();
							Rewards.getInstance().handleBackButton();
							Rewards.getInstance().setNextView();
						}
						if (tabs.getCurrentTab() == 2
								&& Snap.getInstance() != null) {
							Snap.getInstance().handleBackButton();
							Snap.getInstance().handleBackButton();
							if (pageDestinationAfterFbLogin
									.equals("scanReceiptBarcode"))
								Snap.getInstance().showScanBarcodePage();
							else
								Snap.getInstance().setNextView();
						}
						if (tabs.getCurrentTab() == 4
								&& Info.getInstance() != null) {
							Info.getInstance().handleBackButton();
							Info.getInstance().handleBackButton();
							Info.getInstance().setNextView();
						}
					} else if (sucess != null && !sucess.equals("")
							&& sucess.equals("false")) {
						AppConstants.showMsgDialog("Alert", notice,
								Tabbars.this);
					} else {
						AppConstants.parseInput(result, Tabbars.this);
					}
				} catch (Exception e) {
					e.printStackTrace();
					AppConstants.parseInput(result, Tabbars.this);
				}
			}
			// if (getProgressDialog() != null)
			// getProgressDialog().dismiss();
			// greenProgressBar.setVisibility(View.GONE);
			fbProgressDialog.dismiss();
		}
	}

	private void showPostMessageDialog() {
		FacebookDialog fbDialog = new FacebookDialog(Tabbars.this,
				getFacebookMsg(), "");
		fbDialog.show();
	}

	public void setPostText(String postText) {
		this.mPostText = postText;
		postMessageInThreadFromDialog();
	}

	public void postMessageInThreadFromDialog() {
		// if (progressDialog != null && !progressDialog.isShowing())
		// progressDialog.show();
		greenProgressBar.setVisibility(View.VISIBLE);
		Thread t = new Thread() {
			public void run() {
				try {
					String facebookMessage = /*
											 * AppConstants.EMAILSUBJECT1 +
											 * " , " + AppConstants.EMAILBODY +
											 * " :  " +
											 */mPostText;
					File storagePath = Environment
							.getExternalStorageDirectory();
					String path = storagePath.getAbsolutePath() + "/Zoe";
					storagePath = new File(path);
					if (!storagePath.exists())
						storagePath.mkdir();
					File imageFile = new File(storagePath.getAbsolutePath()
							+ "/daily.png");
					byte[] data = null;
					if (imageFile.exists()) {
						Bitmap bi = BitmapFactory.decodeFile(imageFile
								.getAbsolutePath());
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						bi.compress(Bitmap.CompressFormat.JPEG, 80, baos);
						data = baos.toByteArray();
						postImageOnwall(facebookMessage, data);
					}
				} catch (Exception ex) {
					Log.e(AppConstants.TAG_APP, "Error sending msg", ex);
				}
				mFacebookHandler.post(mUpdateFacebookNotification);
			}
		};
		t.start();
	}

	public void postImageOnwall(String msg, byte[] imgbites) {
		if (mFacebook.isSessionValid()) {
			Bundle parameters = new Bundle();
			parameters.putByteArray("picture", imgbites);
			parameters.putString("message", msg);
			mAsyncRunner.request("me/photos", parameters, "POST",
					new SampleUploadListener(), null);
		}
	}

	class SampleUploadListener implements RequestListener {
		@Override
		public void onComplete(String response, Object state) {
			Editor prefsEditor;
			Log.w("Facebook-Example", "Response: " + response.toString());
			JSONObject statusObject;
			try {
				statusObject = new JSONObject(response);
				String postid = statusObject.getString("id");
				prefsEditor = mPreference.edit();
				prefsEditor.putString(AppConstants.POSTID, postid);
				prefsEditor.commit();

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onIOException(IOException e, Object state) {
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
		}
	}

	final Runnable mUpdateFacebookNotification = new Runnable() {
		public void run() {
			// if (progressDialog != null && progressDialog.isShowing())
			// progressDialog.dismiss();
			greenProgressBar.setVisibility(View.GONE);
			// Toast.makeText(getBaseContext(), "Facebook updated !",
			// Toast.LENGTH_LONG).show();
			showMsgDialog("", "Your Facebook message has been posted!",
					Tabbars.this);
		}
	};

	private String getFacebookMsg() {
		String facebookMessage = AppConstants.EMAILSUBJECTFB + "  "
				+ AppConstants.EMAILBODYFB + " ";
		return facebookMessage;//
	}

	// FACEBOOK

	// TWITTER

	public void postTweetMsg(String businessName, String businessID) {
		new RetieveTweetMsgOperation().execute();
	}

	private class RetieveTweetMsgOperation extends
			AsyncTask<Void, Void, Boolean> {
		private final ProgressDialog progressDialog = new ProgressDialog(
				Tabbars.this);

		@Override
		protected void onPreExecute() {
			this.progressDialog.setMessage("Retrieving URL...");
			this.progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean b = TwitterUtils.isAuthenticated(mPreference);
			return b;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (this.progressDialog != null && this.progressDialog.isShowing())
				this.progressDialog.dismiss();
			if (result) {
				startSendingMessage();
			} else {
				Intent i = new Intent(getApplicationContext(),
						PrepareRequestTokenActivity.class);
				i.putExtra("CLASSNAME", "VIEWMAP");
				startActivity(i);
			}
		}
	}

	private String getTweetMsg() {
		String facebookMessage = AppConstants.EMAILSUBJECTTWT + "  "
				+ AppConstants.EMAILBODYTWT + "  ";
		return facebookMessage;
	}

	public void postTweetMsgfromTwitterDialog(String tweetMessage) {
		mTweetMessage = tweetMessage;
		// if (progressDialog != null && !progressDialog.isShowing())
		// progressDialog.show();
		greenProgressBar.setVisibility(View.VISIBLE);
		sendTweet();
	}

	protected void startSendingMessage() // open dialog
	{
		TwitterDialog twit = new TwitterDialog(Tabbars.this, mPreference,
				getTweetMsg(), "VIEWLIST");
		twit.show();
	}

	public void sendTweet() {
		Thread t = new Thread() {
			public void run() {
				boolean b = false;
				try {
					String facebookMessage = /*
											 * AppConstants.EMAILSUBJECT1 +
											 * " , " + AppConstants.EMAILBODY +
											 * " :  " +
											 */mTweetMessage;
					File storagePath = Environment
							.getExternalStorageDirectory();
					String path = storagePath.getAbsolutePath() + "/Zoe";
					storagePath = new File(path);
					if (!storagePath.exists())
						storagePath.mkdir();
					File imageFile = new File(storagePath.getAbsolutePath()
							+ "/daily.png");
					if (imageFile.exists())
						TwitterUtils.sendTweet(mPreference,
								imageFile.getAbsolutePath(), facebookMessage);
					b = true;

				} catch (Exception ex) {
					b = false;
					ex.printStackTrace();
				}
				if (b)
					mTwitterHandler.post(mUpdateTwitterNotification);
				else
					mTwitterHandler.post(mUpdateTwitterNotificationwrongData);
			}
		};
		t.start();
	}

	final Runnable mUpdateTwitterNotification = new Runnable() {
		public void run() {
			// if (progressDialog != null && progressDialog.isShowing())
			// progressDialog.dismiss();
			greenProgressBar.setVisibility(View.GONE);
			// Toast.makeText(getBaseContext(), "Tweet sent !",
			// Toast.LENGTH_LONG)
			// .show();
			showMsgDialog(AppConstants.CONSTANTTITLEMESSAGE,
					"Your tweet has been posted!", Tabbars.this);
		}
	};

	final Runnable mUpdateTwitterNotificationwrongData = new Runnable() {
		public void run() {
			// if (progressDialog != null && progressDialog.isShowing())
			// progressDialog.dismiss();
			greenProgressBar.setVisibility(View.GONE);
			Toast.makeText(getBaseContext(), "Error Duplicate Data !",
					Toast.LENGTH_LONG).show();
		}
	};

	public void setTwitterConnectView(String sharingDealNameStr,
			String sharingBusinessIDStr) {
		startSendingMessage();
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
									}
								});
				AlertDialog alert = alertDialogBuilder.create();
				// alert.setTitle(title);
				if (title.equals("")) {
					alert.setTitle(AppConstants.CONSTANTTITLEMESSAGE);
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

	// TWITTER

	public void postToWall() {
		// post on user's wall.
		mFacebook.dialog(Tabbars.getInstance(), "feed", new DialogListener() {

			@Override
			public void onFacebookError(FacebookError e) {
				showShareMsgDialog("", "Your post has been cancelled!",
						mTabbars);
			}

			@Override
			public void onError(DialogError e) {
				showShareMsgDialog("", "Your post has been cancelled!",
						mTabbars);
			}

			@Override
			public void onComplete(Bundle values) {

				if (values.getString("post_id") != null) {
					showShareMsgDialog("", "Your post has been shared!",
							mTabbars);
				}
			}

			@Override
			public void onCancel() {
				showShareMsgDialog("", "Your post has been cancelled!",
						mTabbars);
			}
		});

	}
}
