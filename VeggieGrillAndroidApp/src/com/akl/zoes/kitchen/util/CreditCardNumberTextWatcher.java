package com.akl.zoes.kitchen.util;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;

public class CreditCardNumberTextWatcher implements TextWatcher {

	private boolean isFormatting;
	private boolean deletingHyphen;
	private int hyphenStart;
	private boolean deletingBackward;

	@Override
	public void afterTextChanged(Editable text) {
		if (isFormatting)
			return;

		isFormatting = true;

		// If deleting hyphen, also delete character before or after it
		if (deletingHyphen && hyphenStart > 0) {
			Log.i("elang", "elang cc txt wathcer 1");
			if (deletingBackward) {
				Log.i("elang", "elang cc txt wathcer 2");
				if (hyphenStart - 1 < text.length()) {
					Log.i("elang", "elang cc txt wathcer 3");
					text.delete(hyphenStart - 1, hyphenStart);
				}
			} else if (hyphenStart < text.length()) {
				Log.i("elang", "elang cc txt wathcer 4");
				text.delete(hyphenStart, hyphenStart + 1);
			}
		}
		if (text.length() == 4 || text.length() == 9 || text.length() == 14) {
			Log.i("elang", "elang lenght : " + text.length());
			text.append('-');
		}

		isFormatting = false;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		if (isFormatting)
			return;

		// Make sure user is deleting one char, without a selection
		final int selStart = Selection.getSelectionStart(s);
		final int selEnd = Selection.getSelectionEnd(s);
		if (s.length() > 1 // Can delete another character
				&& count == 1 // Deleting only one character
				&& after == 0 // Deleting
				&& s.charAt(start) == '-' // a hyphen
				&& selStart == selEnd) { // no selection
			deletingHyphen = true;
			hyphenStart = start;
			// Check if the user is deleting forward or backward
			if (selStart == start + 1) {
				deletingBackward = true;
			} else {
				deletingBackward = false;
			}
		} else {
			deletingHyphen = false;
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
}
