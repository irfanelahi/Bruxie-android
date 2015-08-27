package com.share.twitter;

import oauth.signpost.OAuth;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.content.SharedPreferences;

import com.akl.zoes.kitchen.util.AppConstants;
//import twitter4j.media.ImageUpload;
//import twitter4j.media.ImageUploadFactory;
//import twitter4j.media.MediaProvider;

public class TwitterUtils {

	public static boolean isAuthenticated(SharedPreferences prefs) {
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		try {
			AccessToken a = new AccessToken(token, secret);
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(AppConstants.CONSUMER_KEY,
					AppConstants.CONSUMER_SECRET);
			twitter.setOAuthAccessToken(a);
			twitter.getAccountSettings();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void sendTweets(SharedPreferences prefs, String msg)
			throws Exception {
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

		AccessToken a = new AccessToken(token, secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(AppConstants.CONSUMER_KEY,
				AppConstants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
		twitter.updateStatus(msg);
	}

	public static void sendTweet(SharedPreferences prefs, String path,
			String msg) throws Exception {
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		twitter4j.conf.Configuration conf = new ConfigurationBuilder()
				.setMediaProviderAPIKey(AppConstants.TWITTER_MEDIA_API_KEY)
				.setOAuthConsumerKey(AppConstants.CONSUMER_KEY)
				.setOAuthConsumerSecret(AppConstants.CONSUMER_SECRET)
				.setOAuthAccessToken(token).setOAuthAccessTokenSecret(secret)
				.build();

//		ImageUpload upload = new ImageUploadFactory(conf)
//				.getInstance(MediaProvider.TWITPIC);
		String url = "";
//		try {
//			url = upload.upload(new File(path));
//			Log.w("uploaded url", url);
//		} catch (TwitterException e) {
//
//			e.printStackTrace();
//		}
		AccessToken a = new AccessToken(token, secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(AppConstants.CONSUMER_KEY,
				AppConstants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
		twitter.updateStatus(msg + "  " + url);
	}

	public static String getTwitterAuthToken(SharedPreferences prefs) {
		return prefs.getString(OAuth.OAUTH_TOKEN, "");
	}

	public static String getTwitterAuthSectrt(SharedPreferences prefs) {
		return prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
	}
}
