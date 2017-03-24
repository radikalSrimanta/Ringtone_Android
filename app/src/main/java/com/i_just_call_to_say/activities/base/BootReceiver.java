package com.i_just_call_to_say.activities.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.i_just_call_to_say.activities.contacts.InitializeBackGroundService;

public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		startService(context);
	}
	
	
	public void startService(Context context){
		if(!RingToneBaseActivity.isMyServiceRunning(context)){
			Intent intent = new Intent(context,InitializeBackGroundService.class);
			context.startService(intent);
		}else{
		}
	}

}
