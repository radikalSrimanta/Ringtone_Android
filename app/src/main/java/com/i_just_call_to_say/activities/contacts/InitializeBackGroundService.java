package com.i_just_call_to_say.activities.contacts;

import java.util.Calendar;

import com.i_just_call_to_say.activities.base.CheckBackgroundSericeReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class InitializeBackGroundService extends Service{//com.i_just_call_to_say.activities.contacts.InitializeBackGroundService

	// restart service every 30 seconds 
	private static final int REPEAT_TIME = 1000 * 30;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_STICKY;
	}
	
	@Override
		public void onCreate() {
			super.onCreate();
			System.out.println("CheckBackgroundService");
			initializeAlarmManager();
		}
	
	private void initializeAlarmManager(){
		 System.out.println("initializeAlarmManager");
		 AlarmManager service = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
	     Intent i = new Intent(getApplicationContext(), CheckBackgroundSericeReceiver.class);
	     PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 0, i,    PendingIntent.FLAG_UPDATE_CURRENT);
	     Calendar cal = Calendar.getInstance();
	    // service.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, cal.getTimeInMillis(),  REPEAT_TIME, pending);
	     service.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), REPEAT_TIME, pending);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
