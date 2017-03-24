package com.isis.module.push_notification.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class Utility {

	/**
	 * getDeviceID
	 * 
	 * @param activity
	 * @return deviceId
	 */
	public static String getDeviceID(Activity activity) {
		  // int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
		TelephonyManager manager = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = manager.getDeviceId();
		return deviceId = (deviceId == null ? "000" : deviceId);
	}

	/**
	 * checkConnectivity
	 * 
	 * @param activity
	 * @return true or false
	 */
	public static boolean checkConnectivity(Activity activity) {
		ConnectivityManager cm = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable() && ni.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * isEmailValid (for below Android 2.2)
	 * 
	 * @param email
	 * @return true / false
	 */
	public static boolean isValidEmailAddress(String email) {
		Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher emailMatcher = emailPattern.matcher(email);
		return emailMatcher.matches();
	}

	/**
	 * isEmailValid (for above Android 2.2)
	 * 
	 * @param email
	 * @return true / false
	 */
	public static boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

}
