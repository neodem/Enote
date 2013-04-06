
package com.neodem.enote;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity {

	public static final String PREFERENCES_ID = "preferences";
	public static final String SUBJECT_PREFIX = "subjectPrefix";
	public static final String GMAIL_USERNAME = "gmailUsername";
	public static final String GMAIL_PASSWORD = "gmailPassword";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
