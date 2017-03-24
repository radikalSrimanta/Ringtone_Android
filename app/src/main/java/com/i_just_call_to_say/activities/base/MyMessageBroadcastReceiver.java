package com.i_just_call_to_say.activities.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.gson.Gson;
import com.isis.module.push_notification.PushBroadcastReceiver;
import com.isis.module.push_notification.utility.PushUtility;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.about.AboutRingToneActivity;
import com.i_just_call_to_say.activities.contacts.RingtoneDownloadService;
import com.i_just_call_to_say.activities.splash.SplashActivity;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.ReceivedGreetingsCard;
import com.i_just_call_to_say.dto.table_query_manager.UserTableManager;
import com.utility.CommonUtility;
import com.utility.PreferenceUtility;

public class MyMessageBroadcastReceiver extends PushBroadcastReceiver{//com.i_just_call_to_say.activities.base.MyMessageBroadcastReceiver

	
	public static String NOTIFICATION_TYPE = "notification_type"; 
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("GCM", "MyMessageBroadcastReceiver: " + intent.toString());
		String message = intent.getStringExtra("message");
		System.out.println("  message" + message);
		System.out.println("MyMessageBroadcastReceiver  receive push message");
		if(intent != null){
			saveMyMessage(context,intent);
		}
	}
	
	private void saveMyMessage(Context context, Intent intent) {
		String title = intent.getStringExtra("title");
		String message = intent.getStringExtra("message");
		System.out.println("---> title " + title + "  message" + message);
		//
		String msg_split[] = message.split("@#@");
		// PushNotification pushNotification = new
		// PushNotification(msg_split[0], msg_split[2]);
		message = msg_split[0];

//		showMessage(context, message);
		
		Bundle bundle = new Bundle();
		try {
			Gson gson = new Gson();
			bundle.putString(NOTIFICATION_TYPE, msg_split[1]);
			if (msg_split[1].trim().equalsIgnoreCase("greetingcard")) {
				ReceivedGreetingsCard receivedGreetingsCard = gson.fromJson(msg_split[2], ReceivedGreetingsCard.class);
				bundle.putSerializable("receive_greeting", receivedGreetingsCard);
				PushUtility.showNotificationWithCustom(context, SplashActivity.class, bundle, R.drawable.ic_launcher,title, message);
				
				
			} else if (msg_split[1].trim().equalsIgnoreCase("ringtone")) {
				System.out.println("phone number " + msg_split[2]);
				Intent serviceIntent = new Intent(context, RingtoneDownloadService.class);
				serviceIntent.putExtra("contact_number", msg_split[2]);
//				context.startService(serviceIntent);
//				PushUtility.showNotificationWithCustom(context, SplashActivity.class, bundle,	R.drawable.ic_launcher, title, message);
				
			} else if (msg_split[1].trim().equalsIgnoreCase("updateapp")) {
				try {
					RingToneBaseActivity mActivity = (RingToneBaseActivity) RingToneBaseApplication.currentActivity;
					if (mActivity != null) {
						/*UserTableManager.updateUserStatus(RingToneBaseActivity.user.getAccess_token(),
										mActivity.ringToneBaseApplication.databaseManager,
										mActivity, "true");*/
						RingToneBaseActivity.user = UserTableManager.getSavedUser(mActivity,mActivity.ringToneBaseApplication);
						
						RingToneBaseActivity.user.setPurchase_status("true");
						PreferenceUtility.saveObjectInAppPreference(mActivity, RingToneBaseActivity.user, PreferenceUtility.USER_PROFILE);
						
						mActivity.initAdView();
						if (mActivity instanceof AboutRingToneActivity) {
							((AboutRingToneActivity) mActivity).btn_upgrade.setVisibility(View.GONE);
						}
					}
					Contacts contacts = gson.fromJson(msg_split[2], Contacts.class);
					bundle.putSerializable("contacts", contacts);
					PushUtility.showNotificationWithCustom(context, SplashActivity.class, bundle, R.drawable.ic_launcher, title, message);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void showMessage(Context context,String message){
		System.out.println("start intent");
		Intent mIntent = new Intent(context, PushDialogActivity.class);
		mIntent.putExtra("message", message);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(mIntent);
	}
	
}
