package com.share.twitter;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;

import com.akl.zoes.kitchen.util.AppConstants;

public class PrepareRequestTokenActivity extends Activity {
	final String TAG = getClass().getName();
	private final Handler mTwitterHandler = new Handler();
	private OAuthConsumer consumer;
	private OAuthProvider provider;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
			}
			this.consumer = new CommonsHttpOAuthConsumer(
					AppConstants.CONSUMER_KEY, AppConstants.CONSUMER_SECRET);
			this.provider = new CommonsHttpOAuthProvider(
					AppConstants.REQUEST_URL, AppConstants.ACCESS_URL,
					AppConstants.AUTHORIZE_URL);
		} catch (Exception e) {
			Log.e(TAG, "Error creating consumer / provider", e);
		}
		Log.i(TAG, "Starting task to retrieve request token.");
		new OAuthRequestTokenTask(this, consumer, provider).execute();
	}

	/**
	 * Called when the OAuthRequestTokenTask finishes (user has authorized the
	 * request token). The callback URL will be intercepted here.
	 */
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		final Uri uri = intent.getData();
		if (uri != null
				&& uri.getScheme().equals(AppConstants.OAUTH_CALLBACK_SCHEME)) {
			Log.i(TAG, "Callback received : " + uri);
			Log.i(TAG, "Retrieving Access Token");
			new RetrieveAccessTokenTask(this, consumer, provider, prefs)
					.execute(uri);
			finish();
		}
	}

	public class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Void> {
//		private Context context;
		private OAuthProvider provider;
		private OAuthConsumer consumer;
		private SharedPreferences prefs;
		public RetrieveAccessTokenTask(Context context, OAuthConsumer consumer,
				OAuthProvider provider, SharedPreferences prefs) {
//			this.context = context;
			this.consumer = consumer;
			this.provider = provider;
			this.prefs = prefs;
		}

		/**
		 * Retrieve the oauth_verifier, and store the oauth and
		 * oauth_token_secret for future API calls.
		 */
		@Override
		protected Void doInBackground(Uri... params) {
			final Uri uri = params[0];
			final String oauth_verifier = uri
					.getQueryParameter(OAuth.OAUTH_VERIFIER);

			try {
				provider.retrieveAccessToken(consumer, oauth_verifier);
				final Editor edit = prefs.edit();
				edit.putString(OAuth.OAUTH_TOKEN, consumer.getToken());
				edit.putString(OAuth.OAUTH_TOKEN_SECRET,
						consumer.getTokenSecret());
				edit.commit();

				String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
				String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
				consumer.setTokenWithSecret(token, secret);
				Log.i(TAG, "OAuth - Access Token Retrieved");
				mTwitterHandler.post(mUpdateTwitterNotification);
			} catch (Exception e) {
				Log.e(TAG, "OAuth - Access Token Retrieval Error", e);
			}
			return null;
		}
	}
	final Runnable mUpdateTwitterNotification = new Runnable() {
		public void run() {
//			HomePage.getInstance().setTwitterConnectView(
//					"", "");
			finish();
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Log.d(this.getClass().getName(), "back button pressed");
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
