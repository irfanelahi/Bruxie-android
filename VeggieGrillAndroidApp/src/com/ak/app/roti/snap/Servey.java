package com.ak.app.roti.snap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.app.cb.activity.RotiHomeActivity;
import com.ak.app.cb.activity.Snap;
import com.ak.app.cb.activity.Tabbars;
import com.ak.app.cb.activity.R;
import com.akl.app.roti.bean.Questions;
import com.akl.app.roti.bean.Surveybean;
import com.akl.zoes.kitchen.util.AppConstants;
import com.akl.zoes.kitchen.util.MultiSelectionSpinner;
import com.akl.zoes.kitchen.util.RefreshListner;
import com.akl.zoes.kitchen.util.WebHTTPMethodClass;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;
import com.google.gson.Gson;

public class Servey implements RefreshListner {
	private static Servey screen;
	private RelativeLayout mParentLayout;
	private LayoutInflater mInflater;
	private Tabbars mHomePage;
	private Button submit;
	private LinkedList<String> choiceId = new LinkedList<String>();
	private LinkedList<String> choiceString = new LinkedList<String>();
	private LinkedList<String> choiceQuestion = new LinkedList<String>();
	private JSONObject responseJson;
	private JSONArray responseArray;
	private Surveybean surveyArray[];
	private Gson gson = new Gson();
	private int textViewType;
	private String questionsId[];
	public String answersId[];
	private EditText ans[];
	private Spinner dropdown;
	private String surveyId = "";
	private String receiptId = "";
	private String questId = "", ansId = "";
	private String selecteditem;
	private MultiSelectionSpinner multiCheckboxSpinner;

	// private ProgressBar pageProgressBar;

	public static Servey getInstance() {
		if (screen == null)
			screen = new Servey();
		return screen;
	}

	public static void setInstance() {
		screen = null;
	}

	public RelativeLayout setInflater(LayoutInflater inflater) {
		mInflater = inflater;
		mParentLayout = (RelativeLayout) mInflater.inflate(
				R.layout.life_servey, null);
		return mParentLayout;
	}

	public RelativeLayout getScreenParentLayout() {
		return mParentLayout;
	}

	LinearLayout mServeyLinearLayout;

	public void onCreate(String receiptIds, String surveyIds,
			String restaurantNames, String receiptDates) {
		mHomePage = Tabbars.getInstance();
		AppConstants.changeScreenBrightnessToDefault(mHomePage);
//		Tracker tracker = GoogleAnalytics.getInstance(mHomePage).getTracker(
//				AppConstants.GA_TRACK_ID);
//		tracker.send(MapBuilder.createAppView()
//				.set(Fields.SCREEN_NAME, "Survey").build());

		ImageView skipBtn = (ImageView) mParentLayout.findViewById(R.id.skipBtn);
		RelativeLayout surveyLayout = (RelativeLayout) mParentLayout
				.findViewById(R.id.survey_layout);

		String headerText = null;
		String deadline = receiptDates;
		java.util.Date d1 = null;

		try {
			d1 = AppConstants.makeDate(deadline);
			SimpleDateFormat curFormater = new SimpleDateFormat("MM/dd/yy");// "MM-dd-yy"
			String currentTime = curFormater.format(d1);
			headerText = "How was your visit to " + restaurantNames + " on "
					+ currentTime + "?";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		// dateText.setText("EXPIRES " + currentTime);

		TextView pageTitle = (TextView) mParentLayout
				.findViewById(R.id.life_servey_text_phottotip);
		pageTitle.setText(headerText);
		// pageProgressBar = (ProgressBar) mParentLayout
		// .findViewById(R.id.pageLoadingIndicator);
		mHomePage.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		TextView topTitle = (TextView) mParentLayout
				.findViewById(R.id.topTitle);
		AppConstants.gothamNarrowMediumTextView(topTitle, 18,
				AppConstants.COLORWHITERGB, mHomePage.getAssets());

//		AppConstants.odinRoundedBoldTextView(skipBtn, 18,
//				AppConstants.COLORWHITERGB, mHomePage.getAssets());
//		
		
//		AppConstants.odinRoundedRegularTextView(pageTitle, 18,
//				AppConstants.COLORBLACKRGB, mHomePage.getAssets());


		surveyId = surveyIds;
		receiptId = receiptIds;
		Log.i("elang", "elang survey 1");
		// Snap.getInstance().handleBackButton();
		RotiHomeActivity.getInstance().handleBackButton();
		Log.i("elang", "elang survey 2");
		mServeyLinearLayout = (LinearLayout) mParentLayout
				.findViewById(R.id.life_servey_linear_parent);
		// TextView backText = (TextView) mParentLayout
		// .findViewById(R.id.life_servey_text_back);
		skipBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) mHomePage
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mHomePage.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				RotiHomeActivity.getInstance().new skipSurvey().execute("");
				
				
				RotiHomeActivity.getInstance().handleBackButton();
			}
		});

		surveyLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) mHomePage
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mHomePage.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

			}
		});

		submit = (Button) mParentLayout
				.findViewById(R.id.life_servey_surveysubmit);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) mHomePage
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mHomePage.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				if (AppConstants.isNetworkAvailable(mHomePage)) {
					if (mHomePage.checkIfLogin()) {
						new SendServeyDataToServerAsyncTask().execute();
						// Snap.getInstance().showReceiptServeyPage();
					} else {
						Snap.getInstance().setNextViewName("Servey");
						Snap.getInstance().showLoginOptionPage(false, "SNAP");
					}
				} else
					AppConstants.showMsgDialog("Alert",
							AppConstants.ERRORNETWORKCONNECTION, mHomePage);
			}
		});
		submit.setBackgroundResource(R.drawable.submit_btn_inactive);
		submit.setEnabled(false);
		mHomePage.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		// pageProgressBar = (ProgressBar) mParentLayout
		// .findViewById(R.id.pageLoadingIndicator);

		refreshView();
	}

	@Override
	public void notifyRefresh(String className) {
		if (className.equalsIgnoreCase("showReceiptServeyPage"))
			refreshView();
	}

	public void refreshView() {
		if (AppConstants.isNetworkAvailable(mHomePage)) {
			if (mHomePage.checkIfLogin()) {
				new fetchServeyDataAsyncTask().execute("");
			} else {
				Snap.getInstance().setNextViewName("Servey");
				Snap.getInstance().showLoginOptionPage(false, "SNAP");
			}
		} else
			AppConstants.showMsgDialog("Alert",
					AppConstants.ERRORNETWORKCONNECTION, mHomePage);
	}

	private boolean checkForDropDown() {
		for (int j = 0; j < surveyArray[0].getQuestions().length; j++)
			if (surveyArray[0].getQuestions()[j].getQuestion_type().equals("3")
					&& answersId[j].equals(""))
				return false;

		return true;
	}

	private void activateButtonSubmit(Questions[] questions, String questionType) {
		if (questions.length == 1) {
			if (questionType.equals("2")) {
				if (checkForRadiobuttons()) {
					submit.setBackgroundResource(R.drawable.submit_btn);
					submit.setEnabled(true);
				} else {
					submit.setBackgroundResource(R.drawable.submit_btn_inactive);
					submit.setEnabled(false);
				}
			} else if (questionType.equals("3")) {
				if (checkForDropDown()) {
					submit.setBackgroundResource(R.drawable.submit_btn);
					submit.setEnabled(true);
				} else {
					submit.setBackgroundResource(R.drawable.submit_btn_inactive);
					submit.setEnabled(false);
				}
			}
		} else {
			boolean isAllFieldsValid = true;
			for (int j = 0; j < questions.length; j++)
				if (questions[j].getQuestion_type().equals("2")
					&& answersId[j]==null) {
				//	Toast.makeText(mHomePage, "sds", Toast.LENGTH_LONG).show();
					
					
					isAllFieldsValid = false;
					break;
				
				} else if (questions[j].getQuestion_type().equals("5")
						&& answersId[j].equals("")) {
					isAllFieldsValid = false;
					break;
				}

			if (isAllFieldsValid) {
				submit.setBackgroundResource(R.drawable.survey_btn_submit);
				submit.setEnabled(true);
			} else {
				submit.setBackgroundResource(R.drawable.submit_btn_inactive);
				submit.setEnabled(false);
			}
		}
	}

	public void activateButtonSubmit(Questions[] questions,
			String questionType, String selectedItems) {
		if (questions.length == 1) {
			if (questionType.equals("5")) {
				if (!selectedItems.toString().trim().equals("")) {
					submit.setBackgroundResource(R.drawable.survey_btn_submit);
					submit.setEnabled(true);
				} else {
					submit.setBackgroundResource(R.drawable.submit_btn_inactive); // button
					// disable
					submit.setEnabled(false);
				}
			}
		} else {
			boolean isAllFieldsValid = true;
			for (int j = 0; j < questions.length; j++)
				if (questions[j].getQuestion_type().equals("2")
						&& answersId[j].equals("")) {
					isAllFieldsValid = false;
					break;
				} else if (questions[j].getQuestion_type().equals("5")
						&& answersId[j].equals("")) {
					isAllFieldsValid = false;
					break;
				}

			if (isAllFieldsValid) {
				submit.setBackgroundResource(R.drawable.survey_btn_submit);
				submit.setEnabled(true);
			} else {
				submit.setBackgroundResource(R.drawable.submit_btn_inactive); // button
				// disable
				submit.setEnabled(false);
			}

		}
	}

	private class fetchServeyDataAsyncTask extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			choiceId.clear();
			choiceString.clear();
			choiceQuestion.clear();
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().show();
			// pageProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			String errorMessage = "";
			try {
				responseJson = new JSONObject(result.toString());
				if (responseJson.getBoolean("status") == true) {
					responseArray = new JSONArray(
							responseJson.getString("survey"));
					surveyArray = gson.fromJson(responseArray.toString(),
							Surveybean[].class);

					textViewType = 0;

					for (int i = 0; i < surveyArray.length; i++) {
						Log.i("survey ID ", "id" + surveyArray[i].getId());
						questionsId = new String[surveyArray[i].getQuestions().length];
						answersId = new String[surveyArray[i].getQuestions().length];
						for (int j = 0; j < surveyArray[i].getQuestions().length; j++) {
							Log.i("question ID",
									"id "
											+ surveyArray[i].getQuestions()[j]
													.getId());
							questionsId[j] = surveyArray[i].getQuestions()[j]
									.getId();
							if (surveyArray[i].getQuestions()[j]
									.getQuestion_type().equals("2")) {
								for (int k = 0; k < surveyArray[i]
										.getQuestions()[j]
										.getQuestion_choices().length; k++) {
									Log.i("question_choices ID ",
											"id"
													+ surveyArray[i]
															.getQuestions()[j]
															.getQuestion_choices()[k]
															.getId());
								}
							} else {
								textViewType++;
							}

						}
					}

					Log.i("FeaturedEventActivity ", "User detail  "
							+ responseJson);
					errorMessage = "";
				} else if (responseJson.getBoolean("status") == false
						&& !(responseJson.isNull("errorMsg"))) {
					Log.i("Response Json Failure:",
							"" + responseJson.toString());
					errorMessage = responseJson.getString("errorMsg");
				} else if (responseJson.getBoolean("status") == false) {
					errorMessage = responseJson.getString("message");
				}
			} catch (Exception e) {
			}
			if (errorMessage != null && errorMessage.length() > 0
					&& errorMessage.equals("401")) {
				AppConstants.showMessageDialogWithNewIntent(
						AppConstants.ERROR401SERVICES, mHomePage);
			} else if (errorMessage != null && errorMessage.length() > 0) {
				AppConstants.showMsgDialog("", errorMessage, mHomePage);
			} else {
				try {
					ShowServeyData();
				} catch (Exception e) {
				}
			}
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().dismiss();

		}

		@Override
		protected String doInBackground(String... arg0) {
			String result = fetchServeyDataRequest();
			return result;
		}
	}

	private void ShowServeyData() {
		int rowForTF = 0;
		for (int i = 0; i < surveyArray.length; i++) {
			final TextView[] myTextViews = new TextView[surveyArray[i]
					.getQuestions().length];
			LinearLayout choicesLayout = (LinearLayout) mParentLayout
					.findViewById(R.id.life_servey_helptable);
			LinearLayout questions[] = new LinearLayout[surveyArray[i]
					.getQuestions().length];
			LinearLayout answers[] = new LinearLayout[surveyArray[i]
					.getQuestions().length];

			ans = new EditText[textViewType];

			Log.i("survey ID ", "id" + surveyArray[i].getId());
			String qstype = "test";
			for ( int j = 0; j < surveyArray[i].getQuestions().length; j++) {
				final int questionIndex = j;
				// if (j != 0
				// && !qstype.equals(surveyArray[i].getQuestions()[j]
				// .getQuestion_type())) {
				//
				// LinearLayout.LayoutParams params_line = new
				// LinearLayout.LayoutParams(
				// LayoutParams.WRAP_CONTENT,
				// LayoutParams.WRAP_CONTENT);
				// params_line.setMargins(0, 20, 0, 0);
				// ImageView line = new ImageView(mHomePage);
				// line.setImageResource(R.drawable.dotted_line_1);
				// line.setLayoutParams(params_line);
				// choicesLayout.addView(line);
				// }
				qstype = surveyArray[i].getQuestions()[j].getQuestion_type();
final int l=j;
				myTextViews[j] = new TextView(mHomePage);
				questions[j] = new LinearLayout(mHomePage);
				answers[j] = new LinearLayout(mHomePage);
				LinearLayout ll = new LinearLayout(mHomePage);

				ll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));// Ak
				ll.setOrientation(LinearLayout.HORIZONTAL);
				ll.setPadding(5, 0, 0, 5);

				answers[j].setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				answers[j].setGravity(Gravity.CENTER_VERTICAL);
				myTextViews[j].setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));// Ak
				Typeface font = Typeface.createFromAsset(mHomePage.getAssets(),
						"ufonts.com_american-typewriter.ttf");
				myTextViews[j].setPadding(5, 10, 0, 5);
				//myTextViews[j].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
				//myTextViews[j].setTypeface(font, Typeface.NORMAL);
			//	myTextViews[j].setTextColor(Color.parseColor("#04090e"));
				AppConstants.kingthingsTextView(myTextViews[j], 14, AppConstants.COLOR_BLACK_PAGETITLE, mHomePage.getAssets());
				
				
				myTextViews[j].setText(surveyArray[0].getQuestions()[j]
						.getText());
				questions[j].setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				questions[j].addView(myTextViews[j]);
				choicesLayout.addView(questions[j]);

				Log.i("question ID",
						"id " + surveyArray[i].getQuestions()[j].getId());

				if (surveyArray[i].getQuestions()[j].getQuestion_type().equals(
						"2")) {
					answers[j].setPadding(0, 18, 0, 0);
					ImageView thumbsDown;
					thumbsDown = new ImageView(mHomePage);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
					params.setMargins(0, 0, 0, 0);
					params.weight = 1;
					thumbsDown.setLayoutParams(params);

					thumbsDown.setImageResource(R.drawable.thumbs_down);
					thumbsDown.setScaleType(ImageView.ScaleType.FIT_CENTER);
					answers[j].addView(thumbsDown);

				
		 RatingBar ratingBar=new RatingBar(mHomePage);
		 params = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 0, 0, 0);
		
			//	params.weight = 1;
		//	final int item = k;
			final int row = j;
			final int table = i;
						
			ratingBar.setLayoutParams(params);	
			
			ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
				public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
//Toast.makeText(mHomePage, String.valueOf(rating), Toast.LENGTH_LONG).show();
					//txtRatingValue.setText(String.valueOf(rating));
Log.i("Row------", "row#=" + l);
rating=(int)rating-1;			
if(rating>-1)
{
//rb[z].setImageResource(R.drawable.radial_selected);
Log.i("selected z------", "" + (int)rating);

Log.i("selected ID------",
		""
				+ surveyArray[table]
						.getQuestions()[row]
						.getQuestion_choices()[(int)rating]
						.getId());



answersId[row] = surveyArray[table]
		.getQuestions()[row]
		.getQuestion_choices()[(int)rating]
		.getId();

Log.i("selected anis------",answersId[row]);



activateButtonSubmit(surveyArray[0]
		.getQuestions(), surveyArray[0]
		.getQuestions()[questionIndex]
		.getQuestion_type());

				
				
}
else
{

	answersId[row]=null;
	
	activateButtonSubmit(surveyArray[0]
			.getQuestions(), surveyArray[0]
			.getQuestions()[questionIndex]
			.getQuestion_type());



}
				
				
				
				
				
				}
			});
			
			
			
			LayerDrawable stars = (LayerDrawable) ratingBar
					.getProgressDrawable();
			stars.getDrawable(2).setColorFilter(Color.parseColor("#f5b417"),
					PorterDuff.Mode.SRC_ATOP); // for filled stars
			stars.getDrawable(1).setColorFilter(Color.parseColor("#eaeaea"),
					PorterDuff.Mode.SRC_ATOP); // for half filled stars
			stars.getDrawable(0).setColorFilter(Color.parseColor("#eaeaea"),
					PorterDuff.Mode.SRC_ATOP); // for empty stars

			
			
			answers[j].addView(ratingBar);

		 
		 final ImageView[] rb = new ImageView[surveyArray[i]
							.getQuestions()[j].getQuestion_choices().length];

					// Display display =
					// HomePage.getInstance().getWindowManager()
					// .getDefaultDisplay();
					// int newWidth = display.getWidth();
					// int leftmargin = 5;
					// if (newWidth > 485) {
					// leftmargin = 10;
					// }

					double d = surveyArray[i].getQuestions()[j]
							.getQuestion_choices().length;
					int mid = (int) Math.ceil(d / 2.0);
//					for (int k = 0; k < surveyArray[i].getQuestions()[j]
//							.getQuestion_choices().length; k++) {
//						Log.i("question_choices ID ",
//								"id"
//										+ surveyArray[i].getQuestions()[j]
//												.getQuestion_choices()[k]
//												.getId());
//						final int item = k;
//						final int row = j;
//						final int table = i;
//						rb[k] = new ImageView(mHomePage);
//						params = new LinearLayout.LayoutParams(
//								LayoutParams.FILL_PARENT,
//								LayoutParams.WRAP_CONTENT);
//						params.setMargins(0, 0, 10, 0);
//						params.weight = 1;
//						rb[k].setLayoutParams(params);
//						rb[k].setScaleType(ImageView.ScaleType.FIT_CENTER);
//
//						if (k == (mid - 1)) {
//							rb[item].setImageResource(R.drawable.radial_btn);// radial_selected
//							answersId[j] = "";
//						} else {
//							rb[item].setImageResource(R.drawable.radial_btn);
//						}
//
//						rb[k].setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View arg0) {
//								Log.i("Row------", "row#=" + row);
//								Log.i("Click------", "" + item);
//								for (int z = 0; z < surveyArray[table]
//										.getQuestions()[row]
//										.getQuestion_choices().length; z++) {
//									if (item == z) {
//										rb[z].setImageResource(R.drawable.radial_selected);
//										Log.i("selected z------", "" + z);
//										Log.i("selected ID------",
//												""
//														+ surveyArray[table]
//																.getQuestions()[row]
//																.getQuestion_choices()[z]
//																.getId());
//										answersId[row] = surveyArray[table]
//												.getQuestions()[row]
//												.getQuestion_choices()[z]
//												.getId();
//
//										activateButtonSubmit(surveyArray[0]
//												.getQuestions(), surveyArray[0]
//												.getQuestions()[questionIndex]
//												.getQuestion_type());
//										// if (checkForRadiobuttons()) {
//										// //
//										// submit.setBackgroundResource(R.drawable.enter_btn);
//										// submit.setEnabled(checkForRadiobuttons());
//										// } else {
//										// //
//										// submit.setBackgroundResource(R.drawable.enter_btn_idle);
//										// submit.setEnabled(checkForRadiobuttons());
//										// }
//										// }
//									} else {
//										Log.i("UNselected z------", "" + z);
//										rb[z].setImageResource(R.drawable.radial_btn);
//									}
//								}
//							}
//						});
//					//	answers[j].addView(rb[k]);
//					}
//
//					
					ImageView thumbsUp;
					thumbsUp = new ImageView(mHomePage);
					thumbsUp.setImageResource(R.drawable.thumbs_up);

					params = new LinearLayout.LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
					params.setMargins(0, 0, 20, 0);
					params.weight = 1;
					thumbsUp.setScaleType(ImageView.ScaleType.FIT_CENTER);
					thumbsUp.setLayoutParams(params);
					answers[j].addView(thumbsUp);
					choicesLayout.addView(answers[j]);
				} else if (surveyArray[i].getQuestions()[j].getQuestion_type()
						.equals("3")) {
					Spinner dropdown = new Spinner(mHomePage);
					TextView n1 = new TextView(mHomePage);
					TextView n2 = new TextView(mHomePage);
					n1.setTextColor(Color.parseColor("#464646"));
					n2.setTextColor(Color.parseColor("#464646"));
					n1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
					n1.setTypeface(null, Typeface.BOLD);
					n2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
					n2.setTypeface(null, Typeface.BOLD);

					n1.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					n2.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					dropdown.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					dropdown.setBackgroundResource(R.drawable.survey_drop_down);
					LinearLayout li = new LinearLayout(mHomePage);
					li.setOrientation(LinearLayout.VERTICAL);
					li.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

					LinearLayout.LayoutParams parammss = new LinearLayout.LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
					LinearLayout.LayoutParams parammsss = new LinearLayout.LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
					LinearLayout.LayoutParams parammssss = new LinearLayout.LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);// Ak
					// parammss.setMargins(left, top, right, bottom)
					parammss.setMargins(10, 0, 10, 0);
					parammsss.setMargins(10, 0, 10, 0);

					// parammssss.setMargins(left, top, right, bottom)
					parammssss.setMargins(0, 0, 0, 0);
					n1.setGravity(Gravity.LEFT);
					n2.setGravity(Gravity.LEFT);

					final String[] items = new String[surveyArray[i]
							.getQuestions()[j].getQuestion_choices().length];
					final String[] data = new String[surveyArray[i]
							.getQuestions()[j].getQuestion_choices().length];
					for (int v = 0; v < surveyArray[i].getQuestions()[j]
							.getQuestion_choices().length; v++) {

						items[v] = surveyArray[i].getQuestions()[j]
								.getQuestion_choices()[v].getLabel();

						data[v] = surveyArray[i].getQuestions()[j]
								.getQuestion_choices()[v].getId();
						final int row = j;
						dropdown.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView adapter,
									View v, int i, long lng) {
								// selecteditem =
								// adapter.getItemAtPosition(i).toString();
								answersId[row] = data[i];
//								activateButtonSubmit(surveyArray[0]
//										.getQuestions(), surveyArray[0]
//										.getQuestions()[questionIndex]
//										.getQuestion_type());
//							
							}

							@Override
							public void onNothingSelected(
									AdapterView<?> parentView) {

							}
						});
					}

					ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
							mHomePage, R.layout.custom_simple_spinner_item,
							items) {
						public View getView(int position, View convertView,
								ViewGroup parent) {
							View v = super.getView(position, convertView,
									parent);

							Typeface externalFont = Typeface.createFromAsset(
									mHomePage.getAssets(),
									"ufonts.com_american-typewriter.ttf");
							((TextView) v).setTypeface(externalFont);
							((TextView) v).setTextColor(Color
									.parseColor("#04090e"));

							return v;
						}

						public View getDropDownView(int position,
								View convertView, ViewGroup parent) {
							View v = super.getDropDownView(position,
									convertView, parent);

							// Typeface externalFont = Typeface.createFromAsset(
							// mHomePage.getAssets(),
							// "ufonts.com_american-typewriter.ttf");
							// ((TextView) v).setTypeface(externalFont);
							// v.setBackgroundColor(Color.GREEN);

							return v;
						}
					};
					dataAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					dropdown.setAdapter(dataAdapter);

					n1.setText(items[0] + " = Extremely likely, "
							+ items[items.length - 1] + " = Not at all likely");
					// n2.setText(items[items.length - 1] +
					// " = Not at all likely");
					n1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
					n1.setTypeface(font, Typeface.NORMAL);
					n1.setTextColor(Color.parseColor("#04090e"));

					// li.addView(n1, parammsss);
					// li.addView(n2, parammsss);
					li.addView(dropdown, parammssss);
					answers[j].addView(li);
					choicesLayout.addView(answers[j]);
				} else if (surveyArray[i].getQuestions()[j].getQuestion_type()
						.equals("5")) {
					answersId[j] = "";
					int totalMultipleCheckboxAnswers = surveyArray[i]
							.getQuestions()[j].getQuestion_choices().length;
					String[] optionLabels = new String[totalMultipleCheckboxAnswers];
					String[] multipleChckBoxData = new String[totalMultipleCheckboxAnswers];
					for (int v = 0; v < totalMultipleCheckboxAnswers; v++) {

						optionLabels[v] = surveyArray[i].getQuestions()[j]
								.getQuestion_choices()[v].getLabel();
						multipleChckBoxData[v] = surveyArray[i].getQuestions()[j]
								.getQuestion_choices()[v].getId();

					}

					LayoutInflater multiCheckBoxButtonInflater = LayoutInflater
							.from(mHomePage);
					RelativeLayout multiCheckBoxButton = (RelativeLayout) multiCheckBoxButtonInflater
							.inflate(R.layout.multiple_checkbox_button, null);
					multiCheckboxSpinner = (MultiSelectionSpinner) multiCheckBoxButton
							.findViewById(R.id.multiCheckboxSpinner);

					multiCheckboxSpinner.setItems(optionLabels,
							multipleChckBoxData, surveyArray[0].getQuestions(),
							surveyArray[0].getQuestions()[questionIndex]
									.getQuestion_type(), j);

					LinearLayout.LayoutParams paramms = new LinearLayout.LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);// Ak
					// paramms.setMargins(30, 20, 30, 0);
					paramms.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
					paramms.setMargins(0, 8, 0, 0);
					multiCheckBoxButton.setLayoutParams(paramms);
					answers[j].addView(multiCheckBoxButton);
					choicesLayout.addView(answers[j]);
				} else {
					ans[rowForTF] = new EditText(mHomePage);
					LinearLayout.LayoutParams paramms = new LinearLayout.LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);// Ak
					// paramms.setMargins(30, 20, 30, 0);
					paramms.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
					paramms.setMargins(0, 20, 0, 0);
					ans[rowForTF].setLayoutParams(paramms);
					ans[rowForTF]
							.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
									200) });
					ans[rowForTF].setGravity(Gravity.CENTER | Gravity.LEFT);
					ans[rowForTF].setWidth(LayoutParams.FILL_PARENT);
					ans[rowForTF].setHeight(100);
					ans[rowForTF]
							.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
									| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
					// ans[rowForTF].setSingleLine(false);
					ans[rowForTF].setPadding(5, 4, 4, 5);
					ans[rowForTF].setHint(" Comments");

					// Typeface font =
					// Typeface.createFromAsset(mHomePage.getAssets(),
					// "ufonts.com_american-typewriter.ttf");

					ans[rowForTF].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
					ans[rowForTF].setTypeface(font, Typeface.NORMAL);
					ans[rowForTF].setTextColor(Color.parseColor("#04090e"));

					ans[rowForTF]
							.setBackgroundResource(R.drawable.form_field_2);

					// answers[j].setPadding(5, 0, 0, 0);
					answers[j].addView(ans[rowForTF]);
					choicesLayout.addView(answers[j]);
					rowForTF++;
				}

			}
		}
		// if (checkForRadiobuttons()) {
		// // submit.setBackgroundResource(R.drawable.enter_btn);
		// submit.setEnabled(checkForRadiobuttons());
		// } else {
		// // submit.setBackgroundResource(R.drawable.enter_btn_idle);
		// submit.setEnabled(checkForRadiobuttons());
		// }
	}

	private void buildNewCommentBox() {

	}

	private String fetchServeyDataRequest() {
		String line = "";
		try {
			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			String param = "appkey=" + AppConstants.APPKEY + "&auth_token="
					+ authToken;
			// param += "&locale=" + AppConstants.LOCALE_HEADER_VALUE;
			String params = null;

			Log.i("request", "url " + param + "  param " + params);
			line = WebHTTPMethodClass.httpGetService("/survey/" + "79",
					param);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return line;
	}

	private boolean checkForRadiobuttons() {
		boolean isSubmit = true;
		for (int i = 0; i < surveyArray.length; i++) {
			for (int j = 0; j < surveyArray[i].getQuestions().length; j++) {
				if (surveyArray[i].getQuestions()[j].getQuestion_type().equals(
						"2")) {
					if (answersId[j].equals("")) {
						isSubmit = false;
						// AppConstants.showMsgDialog("Alert",
						// "Please select all values", mHomePage);
						break;
					}
				}
			}
		
		
	}
		return isSubmit;
	}

	private class SendServeyDataToServerAsyncTask extends
			AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().show();
			// pageProgressBar.setVisibility(View.VISIBLE);
			int tf = 0;
			String multiDropDownQSId = "";
			for (int i = 0; i < surveyArray.length; i++) {
				for (int j = 0; j < surveyArray[i].getQuestions().length; j++) {
					if (surveyArray[i].getQuestions()[j].getQuestion_type()
							.equals("2")) {
						// ques
					} else if (surveyArray[i].getQuestions()[j]
							.getQuestion_type().equals("3")) {
						// ques
					} else if (surveyArray[i].getQuestions()[j]
							.getQuestion_type().equals("5")) {
						multiDropDownQSId = surveyArray[i].getQuestions()[j]
								.getId();
					} else {
						answersId[j] = ans[tf].getText().toString();
						tf++;
					}
				}
			}

			for (int i = 0; i < questionsId.length; i++) {
				questId = questId + questionsId[i] + ",";
			}

			for (int i = 0; i < answersId.length; i++) {
				if (i == answersId.length - 1) {
					try {
						ansId = ansId + "answers[" + questionsId[i] + "]="
								+ URLEncoder.encode(answersId[i], "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					try {
						ansId = ansId + "answers[" + questionsId[i] + "]="
								+ URLEncoder.encode(answersId[i], "UTF-8")
								+ "&";
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
			questId = questId.substring(0, questId.length() - 1);

			Log.i("the Q#", "the Q# = " + questId);
			Log.i("the A#", "the A# = " + ansId);
		}

		// title: Please ensure information provided is right. ?
		// isi: The merchandise will be shipped to the address provided.
		// cancel continue

		// Claim updated. Enjoy your reward.
		// Ok

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (mHomePage != null && mHomePage.getProgressDialog() != null)
				mHomePage.getProgressDialog().dismiss();
			// pageProgressBar.setVisibility(View.GONE);
			String errorMessage = "";
			try {
				if (responseJson.getBoolean("status") == true) {
					errorMessage = responseJson.getString("message");
				} else if (responseJson.getBoolean("status") == false) {
					errorMessage = responseJson.getString("message");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			RotiHomeActivity.getInstance().submitbtn();
			// AlertDialog.Builder builder = new AlertDialog.Builder(mHomePage);
			// builder.setMessage(errorMessage)
			// .setCancelable(false)
			// .setPositiveButton("OK",
			// new DialogInterface.OnClickListener() {
			//
			// public void onClick(DialogInterface dialog,
			// int id) {
			// // mHomePage.isServey = false;
			// Snap.getInstance().submitbtn();
			// // Snap.getInstance()
			// // .showReceiptCompletePage();
			// dialog.cancel();
			// }
			// });
			// AlertDialog alert = builder.create();
			// alert.show();

		}

		@Override
		protected Void doInBackground(String... arg0) {
			SendServeyDataToServer();
			return null;
		}
	}

	private void SendServeyDataToServer() {
		try {
			String params;
			String authToken = mHomePage.getPreference().getString(
					AppConstants.PREFAUTH_TOKEN, "");
			params = "appkey=" + AppConstants.APPKEY + "&auth_token="
					+ authToken + "&receiptId=" + receiptId + "&rewardId=0&"
					+ ansId;
			// params += "&locale=" + AppConstants.LOCALE_HEADER_VALUE;
			Log.i("request", "url " + params + "  param " + params);
			String line = WebHTTPMethodClass.httpGetService("/survey/"
					+ surveyId + "/answer", params);
			responseJson = new JSONObject(line.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
