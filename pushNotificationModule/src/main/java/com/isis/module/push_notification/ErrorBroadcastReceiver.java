package com.isis.module.push_notification;

import com.isis.module.push_notification.constants.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ErrorBroadcastReceiver extends BroadcastReceiver{

	private OnRegisterErrorListener errorListener;
	
	public ErrorBroadcastReceiver(OnRegisterErrorListener errorListener) {
		this.errorListener = errorListener;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().toString().equals(Constants.CUSTOM_ACTION)) {
			System.out.println("register error receiver");
			if(errorListener!=null)
				errorListener.onRegisterError();
		}
		
	}
	
	public interface OnRegisterErrorListener{
		public void onRegisterError();
	}

}
