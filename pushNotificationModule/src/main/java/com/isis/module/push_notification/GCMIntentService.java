package com.isis.module.push_notification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;


import com.google.android.gcm.GCMBaseIntentService;
import com.isis.module.push_notification.constants.Constants;
import com.isis.module.push_notification.utility.PushUtility;

public class GCMIntentService extends GCMBaseIntentService {
	private final String TAG = "GCM";

	public static final String SENDER_ID = Constants.GCM_SENDER_ID;
	public GCMIntentService() {
		// Email address currently not used by the C2DM Messaging framework
		super();
	}

	@Override
	public void onRegistered(Context context, String registrationId) {
		// The registrationId should be send to your applicatioin server.
		// We just log it to the LogCat view
		// We will copy it from there
		Log.e(TAG, "Registration ID arrived!!!"+registrationId);
		Log.e(TAG, "Registration ID: "+PushUtility.fetchC2DMRegistrationInfo(this).get("registrationID"));
		if (registrationId != null)
			Log.e(TAG, registrationId);

		/**
		 * Save registration id to shared preferences
		 */

		PushUtility.saveC2DMRegistrationInfo(this, registrationId);

		File f1 = Environment.getExternalStorageDirectory();
		File f2 = new File(f1, "Test.bat");
		try {
			if (!f2.exists())
				f2.createNewFile();
			FileOutputStream fis = new FileOutputStream(f2);
			ObjectOutputStream ois = new ObjectOutputStream(fis);
			ois.writeUTF(registrationId);
			ois.flush();
			ois.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	};

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.e(TAG, "Message: Fantastic!!!");
		String pushupMsg = intent.getStringExtra("msg");
		String pushupTitle = intent.getStringExtra("title");
//		PushUtility.showNotification(context, Constants.classToOpenForNotification, R.drawable.ic_launcher, pushupTitle, pushupMsg);
		broadcastIntentGCM(pushupTitle, pushupMsg);
	}
	
	private void broadcastIntentGCM(String title , String msg)
	{	
	   Intent intent = new Intent();
	   intent.putExtra("title", title);
	   intent.putExtra("message", msg);
	   intent.setAction("com.push_notification.CUSTOM_PUSH_INTENT");
	   sendBroadcast(intent);
	}
	
	

	@Override
	public void onError(Context context, String errorId) {
		Log.e(TAG, "Error occured!!!");
		Log.e(TAG, errorId);
		System.exit(0);
	}

	@Override
	protected void onUnregistered(Context context, String arg1) {
		PushUtility.saveC2DMRegistrationInfo(this, "");

	}
}