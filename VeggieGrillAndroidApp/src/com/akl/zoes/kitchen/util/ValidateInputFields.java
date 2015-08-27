package com.akl.zoes.kitchen.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class ValidateInputFields {
	public static boolean isEmailValid(String mail) {
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		Pattern p = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);

		Matcher m = p.matcher(mail);
		if (m.matches() && mail.trim().length() > 0) {
			return true;
		}
		return false;
	}

	public static boolean isAtLeastOneNonAlphabetic(String field) {
		String expression = "^(?=.*[\\d!@#$%\\^*()_\\-+=\\[{\\]};:|\\./])(?=.*[a-z]).{6,20}$";
		Pattern p = Pattern.compile(expression);
		Matcher m = p.matcher(field);
		if (m.matches()) {
			return true;
		}
		return false;
	}

	public static boolean isMobileNumberValid(String mobileNum) {
		String expression = "^[987]\\d{9}$";
		Pattern p = Pattern.compile(expression);
		// pattern=/^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
		Matcher m = p.matcher(mobileNum);
		if (m.matches() && mobileNum.trim().length() > 0) {
			return true;
		}
		return false;
	}

	public static boolean isNameValid(String name) {
		if (/* m.matches() && */name.trim().length() > 0) {
			return true;
		}
		return false;
	}

	public static boolean isFieldEmpty(String field) {
		if (field.trim().length() > 0) {
			return true;
		}
		return false;
	}

	public static boolean isFieldSize(String field) {
		if (field.trim().length() > 3) {
			return true;
		}
		return false;
	}

	public static boolean isPhoneFieldSize(String field) {
		if (field.trim().length() > 2) {
			return true;
		}
		return false;
	}

	public static boolean isTPhoneFieldSize(String field) {
		if (field.trim().length() > 3) {
			return true;
		}
		return false;
	}

	public static boolean isDateFieldSize(String field) {
		Log.e("field", "" + field.trim().length());
		if (field.trim().length() > 15) {
			return true;
		}
		return false;
	}
}
