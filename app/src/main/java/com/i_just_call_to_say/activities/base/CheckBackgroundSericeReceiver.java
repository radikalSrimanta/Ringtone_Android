package com.i_just_call_to_say.activities.base;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.i_just_call_to_say.activities.contacts.DownloadService;

public class CheckBackgroundSericeReceiver extends BroadcastReceiver{//com.i_just_call_to_say.activities.base.CheckLockScreenReceiver

	@Override
	public void onReceive(Context context, Intent intent) {
		startService(context);
	}
	
	public static boolean isMyServiceRunning(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (DownloadService.class.getName().equals(	service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
	private void startService(Context context){
		Intent intent = new Intent(context, DownloadService.class);
		context.startService(intent);
	}
	

}
