
package com.neodem.enote;

import java.io.IOException;
import java.net.SocketException;
import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Enote extends Activity {

	private static final String ID_TITLE = "TITLE";

	private static final String ID_BODY = "BODY";

	private EditText mSubjectText;

	private EditText mBodyText;

	private String mSubjectPrefix = DEFAULT_PREFIX;

	private String mGmailUsername = "";

	private String mGmailPassword = "";

	private static final DateFormat DF = DateFormat.getDateInstance(DateFormat.LONG);

	private static final String DEFAULT_PREFIX = "note : ";

	private static final int PREFS_ID = 0;

	private Gmail mail;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// setup the send button
		Button sendButton = (Button) findViewById(R.id.send);
		sendButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				final String address = mGmailUsername + "@gmail.com";
				try {
					String subject = mSubjectText.getText().toString();
					String body = mBodyText.getText().toString();
					mail.send(address, subject, body);
					Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(), "Message Send Failed", Toast.LENGTH_SHORT).show();
				} 
				finish();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		getPrefs();

		mail = new Gmail(mGmailUsername, mGmailPassword);

		// get a pointer to the subject text box and fill it with default value
		mSubjectText = (EditText) findViewById(R.id.title);
		StringBuffer b = new StringBuffer();
		b.append(mSubjectPrefix);
		b.append(DF.format(new Date()));
		mSubjectText.setText(b);

		// get a pointer to the body text box and set it so that we have focus
		mBodyText = (EditText) findViewById(R.id.body);
		mBodyText.requestFocus();

		// if this was due to being sent 'share' data, we insert it
		Intent intent = getIntent();
		String action = intent.getAction();
		if ("android.intent.action.SEND".equals(action)) {
			String url = intent.getStringExtra(android.content.Intent.EXTRA_TEXT);
			String subject = intent.getStringExtra(android.content.Intent.EXTRA_SUBJECT);

			mBodyText.setText(url);
			if (subject != null && !subject.equals("")) {
				mSubjectText.setText(subject);
			}
		}
	}

	private void getPrefs() {
		// Get the xml/preferences.xml preferences
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		mSubjectPrefix = prefs.getString(Preferences.SUBJECT_PREFIX, DEFAULT_PREFIX);
		mGmailUsername = prefs.getString(Preferences.GMAIL_USERNAME, "");
		mGmailPassword = prefs.getString(Preferences.GMAIL_PASSWORD, "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, PREFS_ID, 0, R.string.prefs).setIcon(android.R.drawable.ic_menu_preferences);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case PREFS_ID:
			startActivity(new Intent(this, Preferences.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(ID_TITLE, mSubjectText.getText().toString());
		outState.putString(ID_BODY, mBodyText.getText().toString());
	}
}