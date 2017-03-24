package com.isis.module.push_notification.utility;

import java.util.HashMap;
import java.util.Random;

import com.google.android.gcm.GCMRegistrar;
import com.isis.module.push_notification.GCMIntentService;
import com.isis.module.push_notification.GCMSplashActivity;
import com.isis.module.push_notification.R;
import com.isis.module.push_notification.constants.Constants;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * This is Push Notification Utility class Define below see the redme.txt file
 * for receiver and and uses permission details information
 * 
 * @author subha
 * 
 */
public class PushUtility {

	private Activity mActivity;

	public static String fetchC2DMToken(final Context mContext) {
		HashMap<String, Object> map = fetchC2DMRegistrationInfo(mContext);
		final String regId = map.get("registrationID").toString();
		return regId;
	}

	public static void saveC2DMRegistrationInfo(final Context mContext,
			String registrationId) {
		SharedPreferences c2dmpref = mContext.getSharedPreferences("GCM", 0);
		SharedPreferences.Editor editor = c2dmpref.edit();
		editor.putBoolean("registration", true);
		if (registrationId != null)
			editor.putString("registrationID", registrationId);
		editor.putBoolean("c2dmregistration", true);
		editor.putBoolean("userpermission", true);
		// Commit the edits!
		editor.commit();
	}

	public static void showNotification(Context context,
			final Class<?> mActivityClass, final int icon, final String title,
			final String message) {

		final int UPDATE_NOTIFICATION = 100;
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification updateComplete = new Notification();
		updateComplete.icon = icon;
		updateComplete.tickerText = title;
		updateComplete.when = System.currentTimeMillis();

		Intent notificationIntent = new Intent(context, mActivityClass);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		String contentTitle = title;
		String contentText = message;

		updateComplete.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		notificationManager.notify(UPDATE_NOTIFICATION, updateComplete);
	}

	public static void showNotificationWithCustom(Context context,
			final Class<?> mActivityClass, final int icon, final String title,
			final String message) {

		String ns = Context.NOTIFICATION_SERVICE;

		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);

		RemoteViews contentView = new RemoteViews(context.getPackageName(),
				R.layout.custom_notification_layout);
		contentView.setTextViewText(R.id.tv_PushTitle, title);
		contentView.setTextViewText(R.id.tv_PushMessage, message);
		contentView.setImageViewResource(R.id.iv_PushICON, icon);

		CharSequence tickerText = message;
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.contentView = contentView;

		Intent notificationIntent = new Intent(context, mActivityClass);

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.contentIntent = contentIntent;

		Random randInt = new Random();
	    int id = randInt.nextInt(100) - 1;
		mNotificationManager.notify(id, notification);
	}
	
	
	public static void showNotificationWithCustom(Context context,
			final Class<?> mActivityClass, Bundle bundle,final int icon, final String title,
			final String message) {
System.out.println("call push page");
		String ns = Context.NOTIFICATION_SERVICE;

		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);

		RemoteViews contentView = new RemoteViews(context.getPackageName(),
				R.layout.custom_notification_layout);
		contentView.setTextViewText(R.id.tv_PushTitle, title);
		contentView.setTextViewText(R.id.tv_PushMessage, message);
		contentView.setImageViewResource(R.id.iv_PushICON, icon);

		CharSequence tickerText = message;
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.contentView = contentView;
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		long[] pattern = { 500, 500, 500, 500, 500, 500, 500, 500, 500 };
		notification.vibrate = pattern;
		notification.sound = alarmSound;
		Intent notificationIntent = new Intent(context, mActivityClass);
        notificationIntent.putExtras(bundle);  
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.contentIntent = contentIntent;
		Random randInt = new Random();
	    int id = randInt.nextInt(100) - 1;
		mNotificationManager.notify(id, notification);
	}

	public static HashMap<String, Object> fetchC2DMRegistrationInfo(
			final Context mContext) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		SharedPreferences c2dmpref = mContext.getSharedPreferences("GCM", 0);
		boolean registration = c2dmpref.getBoolean("registration", false);
		final String regId = c2dmpref.getString("registrationID", "");
		final boolean c2dmRegistration = c2dmpref.getBoolean(
				"c2dmregistration", false);
		final boolean userPermission = c2dmpref.getBoolean("userpermission",
				false);

		map.put("registration", registration);
		map.put("registrationID", regId);
		map.put("c2dmregistration", c2dmRegistration);
		map.put("userpermission", userPermission);

		return map;
	}

	public static void resetC2DMInfo(Context mContext) {
		SharedPreferences c2dmpref = mContext.getSharedPreferences("GCM", 0);
		SharedPreferences.Editor editor = c2dmpref.edit();
		editor.putBoolean("registration", false);
		editor.putString("registrationID", null);
		editor.putBoolean("c2dmregistration", false);
		editor.putBoolean("userpermission", false);
		// Commit the edits!
		editor.commit();
	}

	public static void unregisterGCM(Activity activity) {
		GCMRegistrar.unregister(activity);
	}

	public PushUtility(Activity activity) {
		mActivity = activity;
	}

	public void C2dmRegistration() {

		String registerationID = PushUtility.fetchC2DMToken(mActivity);
		Log.e("RegistrationID", "111 " + registerationID);
		if (registerationID.equals("")) {
			// GCMRegistrar.checkDevice(this);
			// GCMRegistrar.checkManifest(this);
			final String regId = GCMRegistrar.getRegistrationId(mActivity);
			if (regId.equals("")) {
				System.out.println("-- checkConnectivity: "
						+ checkConnectivity(mActivity));
				if (checkConnectivity(mActivity)) {
					GCMRegistrar.register(mActivity, GCMIntentService.SENDER_ID);
					new GCMRegistrationTask().execute();
				} else {
					CustomDialogUtility.showCallbackMessageWithOk("A problem has been encountered! Please check your internet connection and try again.", mActivity, 
									new AlertDialogCallBack() {

										@Override
										public void onSubmit() {
											mActivity.finish();
										}

										@Override
										public void onCancel() {

										}
									});
				}

			} else {
				saveC2DMRegistrationInfo(mActivity, regId);
				Log.v("DROP_COUNT", "Already registered");
			}
		} else {
			((GCMSplashActivity) mActivity).callSplashWait();
		}
	}

	private boolean checkConnectivity(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable() && ni.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	class GCMRegistrationTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// String token = PushUtility.fetchC2DMToken(mActivity);
			while (PushUtility.fetchC2DMToken(mActivity).equalsIgnoreCase("")) {
				// Log.v("DROP_COUNT", PushUtility.fetchC2DMToken(mActivity)
				// + "-->> Waiting   !!!");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			// new SplashWait().execute();
			sendDeviceTokenToServer();
		}

		@Override
		protected void onPreExecute() {

		}
	}

	public void sendDeviceTokenToServer() {
		HashMap<String, Object> c2dmInfoMap = fetchC2DMRegistrationInfo(mActivity);
		// final String regId = "android";
		String regId = (String) c2dmInfoMap.get("registrationID");
		Log.e("GCM ", regId);
		regId = regId.equalsIgnoreCase("") ? "android" : regId;
		System.out.println("REG ID: " + regId);
		String deviceId = Utility.getDeviceID(mActivity);
		deviceId = (deviceId == null ? "android" : deviceId);
		if (Utility.checkConnectivity(mActivity)) {
			SendTokenToServer();
		} else {
			showAlertDialog();
		}
	}

	private void SendTokenToServer() {
		String deviceToken = fetchC2DMToken(mActivity);
		String deviceID = Utility.getDeviceID(mActivity);
		Log.d("GCM", "Token " + fetchC2DMToken(mActivity));
		Constants.iPushResponce.sendTokenToServer(deviceToken, deviceID);
		//Constants.iPushResponce.sendTokenToServer(, deviceID);
		((GCMSplashActivity) mActivity).callSplashWait();
	}

	private void showAlertDialog() {
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				CustomDialogUtility.showCallbackMessageWithOk("A problem has been encountered! Please check your internet connection and try again.",mActivity,
							new AlertDialogCallBack() {

							@Override
							public void onSubmit() {

							}

							@Override
							public void onCancel() {

							}
						});
			}
		});

	}

}
