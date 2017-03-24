package com.utility.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class CustomAdView extends LinearLayout {

	private AdView adView;
	private Activity mActivity;

	public CustomAdView(Context context, AttributeSet attrs) {
		super(context, attrs);
		loadAdd(context);
	}

	public CustomAdView(Context context, AttributeSet attrs, int in) {
		super(context, attrs);
		loadAdd(context);
	}

	public CustomAdView(Context context) {
		super(context);
		loadAdd(context);
	}
	
	private void loadAdd(Context context){
		if (!isInEditMode()) {
			mActivity = (Activity) context;
			createAddView();
		}
	}

	private void createAddView() {
		String addID = "a152428127e228a";
		
		Log.d("ADD_ID", "AddID: "+addID);
		
		adView = new AdView(mActivity);
	    adView.setAdSize(AdSize.SMART_BANNER);
	    adView.setAdUnitId(addID);
		
	    removeAllViews();
		
		addView(adView);
		
		AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        .build();

	    // Start loading the ad in the background.
	    adView.loadAd(adRequest);
	}
}