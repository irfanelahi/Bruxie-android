package com.ak.app.cb.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ak.app.cb.activity.R;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.Utility;

public class SplashScreen extends Activity {
	private Timer my_timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_im);
		boolean isNetworkAvailable = Utility
				.isNetworkAvailable(SplashScreen.this);

		if (isNetworkAvailable) {
			my_timer = new Timer();
			my_timer.schedule(new TimerTask() {
				public void run() {
					Intent i = new Intent(SplashScreen.this, Tabbars.class);
					startActivity(i);
					finish();
				}
			}, 2000);
		} else {
			AppConstants.DIALOG_MSG = "Could not connect. Check your connection and try again later.";
			showDialog(AppConstants.DIALOG_ALERT);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (my_timer != null) {
			my_timer.cancel();
			my_timer = null;
		}
	}
}
