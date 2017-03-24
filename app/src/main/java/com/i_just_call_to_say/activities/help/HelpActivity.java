package com.i_just_call_to_say.activities.help;

import java.util.regex.Pattern;

import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.utility.view.CustomTextView;

public class HelpActivity extends RingToneBaseActivity {

	private AdView adView;
	private CustomTextView tv_help;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_layout);
		initView();
		setTabSelected(RingToneBaseActivity.HELP, "HELP");
	}

	private void initView() {
		initBaseView();	
		tv_help = (CustomTextView) findViewById(R.id.tv_help);
		setHashTag(tv_help);
		
	}
	
	
	private void setHashTag(TextView hashView) {
		Pattern tagMatcher = Pattern.compile("[#]+[A-Za-z0-9-_]+\\b");

		// Scheme for Linkify, when a word matched tagMatcher pattern,
		// that word is appended to this URL and used as content URI
		String newActivityURL = "content://com.socialmedia.sociavize.event.HashEventListScreen";

		// Attach Linkify to TextView
		Linkify.addLinks(hashView, Linkify.ALL);
//		Linkify.addLinks(hashView, tagMatcher, newActivityURL);

	}
}
