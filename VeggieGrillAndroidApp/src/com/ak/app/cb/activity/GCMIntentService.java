/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ak.app.cb.activity;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;

import com.akl.zoes.kitchen.util.AppConstants;
import com.google.android.gcm.GCMBaseIntentService;

/**
 * {@link IntentService} responsible for handling GCM messages.
 */
@SuppressLint("HandlerLeak")
public class GCMIntentService extends GCMBaseIntentService {

//	static String appKey = "267674638353";

	public GCMIntentService() {
		super(AppConstants.PUSH_NOTIFICATION_KEY);
		mC2DMReceiver = this;
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	@SuppressWarnings("unused")
	private static void generateNotification(Context context, String message) {
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	protected void onError(Context arg0, String arg1) {

	}

	@Override
	protected void onMessage(Context arg0, Intent intent) {

		try {
			Log.d("GCM", "RECIEVED A MESSAGE");
			String StrMessage = intent.getStringExtra("alert");
			
			Log.e("StrMessage  :", StrMessage);
			if (StrMessage != null/* && bedge != null*/) {
				messageContent = StrMessage;
				Log.e("messageContent:", messageContent);
			}

			if (Tabbars.getInstance() != null
					&& !Tabbars.getInstance().isInBackGround) {
				tabHandler.sendEmptyMessage(0);
//			} else if (RotiHomeActivity.getInstance() != null
//					&& !RotiHomeActivity.getInstance().isInBackGround) {
//				tabHandler.sendEmptyMessage(0);
			}else {
				startPendingIntent();
			}
			notifyUserForPushMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void notifyUserForPushMessage() {
		vibrateDevice();
	}

	private void vibrateDevice() {
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		long milliseconds = 1000;
		v.vibrate(milliseconds);
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {

	}

	private String messageContent = "";
	private static GCMIntentService mC2DMReceiver;

	public static GCMIntentService getC2DMReceiverInstance() {
		return mC2DMReceiver;
	}

	private void startPendingIntent() {
		Intent pushIntent = null;
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon_h,
				AppConstants.PUSH_NOTIFICATION_TAG, System.currentTimeMillis());
		pushIntent = startMyMessageScreen(true);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				pushIntent, 0);
		notification.setLatestEventInfo(this, messageContent, "Message",
				contentIntent);

		notification.defaults |= Notification.DEFAULT_SOUND;
		long[] vibrate = { 0, 100, 200, 300 };
		notification.vibrate = vibrate;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		nm.notify(AppConstants.PUSH_NOTIFICATION_TAG,
				AppConstants.PUSH_NOTIFICATION_ID, notification);
	}

	private Intent startMyMessageScreen(boolean hasReturn) {
		Intent pushIntent = null;
		pushIntent = new Intent(this, Tabbars.class);
		pushIntent.putExtra(AppConstants.PUSH_NOTIFICATION_MESSAGE, messageContent);
		pushIntent.putExtra(AppConstants.PUSH_NOTIFICATION_CLASS, "C2DMRECEIVER");
		pushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return pushIntent;
	}

	private Handler tabHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.i("PushEndpointDemo.getInstance()= ", "handleMessage 1");
			if (Tabbars.getInstance() != null)
				Tabbars.getInstance().setMessage(messageContent/*, bedgeNumber*/);
//			else if (RotiHomeActivity.getInstance() != null)
//				RotiHomeActivity.getInstance().setMessage(messageContent/*, bedgeNumber*/);
		}
	};
}
