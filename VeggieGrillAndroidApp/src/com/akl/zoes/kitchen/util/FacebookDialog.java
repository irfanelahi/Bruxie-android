package com.akl.zoes.kitchen.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;

public class FacebookDialog extends Dialog {
	EditText editFacebookMsg;

	// String postText;

	public FacebookDialog(Context context, final String scoreText,
			final String fromClass) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.facebookdialog);

		TextView viaTextView = (TextView) findViewById(R.id.facebook_text_via);
		viaTextView.setText("Via Roti");

		TextView scoreTextView = (TextView) findViewById(R.id.facebook_text_score);
		scoreTextView.setText(scoreText + " ");

		editFacebookMsg = (EditText) findViewById(R.id.facebook_edit_message);
		// if(!message.equals(""))
		// editStockName.setText(message);

		Button cancelButton = (Button) findViewById(R.id.facebook_but_skip);
		cancelButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		Button okbutton = (Button) findViewById(R.id.facebook_but_publish);
		okbutton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (editFacebookMsg != null) {
					String postText = editFacebookMsg.getText().toString();
					postText += "\n" + scoreText;
					Tabbars.getInstance().setPostText(postText);
				}
				dismiss();
			}
		});
	}
}
