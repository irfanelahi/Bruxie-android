package com.akl.zoes.kitchen.util;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;


public class TwitterDialog extends Dialog 
{	
	TextView cancelButton;
	EditText editTwitterMsg;
//	String tweetMessage;
	SharedPreferences prefs;
	public TwitterDialog(Context context, SharedPreferences pref, String scoreText, final String fromClass)
	{
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.twitterdialog);	
		prefs = pref;
		editTwitterMsg = (EditText)findViewById(R.id.twitter_edit_message);
		if(!scoreText.equals(""))
			editTwitterMsg.setText(scoreText + " ");
		
		cancelButton = (TextView)findViewById(R.id.twitter_but_back);
		cancelButton.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				cancelButton.setBackgroundColor(Color.parseColor("#55dddd33"));
				dismiss();
			}
		});
		
		Button tweetButton = (Button)findViewById(R.id.twitter_but_tweet);
		tweetButton.setOnClickListener(new Button.OnClickListener()
		{		
			@Override
			public void onClick(View v) 
			{
				if(editTwitterMsg != null)
				{
					String tweetMessage = editTwitterMsg.getText().toString();
					Tabbars.getInstance().postTweetMsgfromTwitterDialog(tweetMessage);
				}
				dismiss();
			}
		});			
	}

	final String TAG = getClass().getName();
}
