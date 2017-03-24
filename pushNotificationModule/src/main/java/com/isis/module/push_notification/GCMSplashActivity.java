package com.isis.module.push_notification;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.isis.module.push_notification.constants.Constants;
import com.isis.module.push_notification.interfaces.IPushResponce;
import com.isis.module.push_notification.utility.PushUtility;
import com.isis.module.push_notification.utility.ViewUtility;

public class GCMSplashActivity extends Activity {

	private int splash_time = 3000;
	public static String token;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void initView(int splashLayout, Class<?> classToOpenForNotification,
			Object iInteface, int splash_time, String senderID) {

		View splashLayoutView = ViewUtility.ViewgetLayoutView(this,
				splashLayout);
		this.splash_time = splash_time;

		if (splashLayoutView != null) {
			setContentView(splashLayoutView);

		}
		
	}

	public void callSplashWait() {
		new SplashWait().execute();
	}

	private void startNextActivity() {
		Constants.iPushResponce.gotoNextActivity();
//		finish();
	}

	private class SplashWait extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(splash_time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			startNextActivity();
			
		}
	}

}
