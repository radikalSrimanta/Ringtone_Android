package com.utility;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.isis.module.push_notification.utility.ObjectSerializer;

public class PreferenceUtility {

	private static final String APP_PREFERENCENAME = "RINGTONE_APP_PREF";
	public static final String MY_PROFILE = "MY_PROFILE";
	public static final String TAB_NAME = "TAB_NAME";
	public static final String USER_PROFILE = "USER_PROFILE";
	public static final String CONNECTED_CONTACT = "CONNECTED_CONTACT";
	public static final String CONTACT = "CONTACT";
	public static final String CONTACT_LIST = "CONTACT_LIST";

	/**
	 * getAppSharedPreference returns Application Shared Preference
	 * 
	 * @param activity
	 * @return SharedPreferences
	 */
	public static SharedPreferences getAppSharedPreference(Activity activity) {
		SharedPreferences prefs = activity.getSharedPreferences(
				APP_PREFERENCENAME, Context.MODE_PRIVATE);
		return prefs;
	}
	
	public static SharedPreferences getAppSharedPreference(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				APP_PREFERENCENAME, Context.MODE_PRIVATE);
		return prefs;
	}

	/**
	 * getAppPrefsEditor
	 * 
	 * @param activity
	 * @return editor
	 */
	public static Editor getAppPrefsEditor(Activity activity) {
		SharedPreferences prefs = activity.getSharedPreferences(
				APP_PREFERENCENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = prefs.edit();
		return prefsEditor;

	}
	
	public static Editor getAppPrefsEditor(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				APP_PREFERENCENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = prefs.edit();
		return prefsEditor;

	}

	/**
	 * saveObjectInAppPreference save given Object into Application
	 * SharedPreference
	 * 
	 * @param activity
	 * @param object
	 * @param prefName
	 */
	public static void saveObjectInAppPreference(Activity activity,	Object object, String prefName) {
		SharedPreferences.Editor prefsEditor = getAppPrefsEditor(activity);
		try {
			prefsEditor.putString(prefName,
					object != null ? ObjectSerializer.serialize(object) : null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		prefsEditor.commit();
	}
	
	public static void saveObjectInAppPreference(Context context,	Object object, String prefName) {
		SharedPreferences.Editor prefsEditor = getAppPrefsEditor(context);
		try {
			prefsEditor.putString(prefName,
					object != null ? ObjectSerializer.serialize(object) : null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		prefsEditor.commit();
	}

	/**
	 * getObjectInAppPreference returns saved Object from Application
	 * SharedPreference
	 * 
	 * @param activity
	 * @param prefName
	 * @return Object
	 */
	public static Object getObjectInAppPreference(Activity activity,
			String prefName) {
		Object prefObjecct = null;
		SharedPreferences prefs = getAppSharedPreference(activity);
		String serializeString = prefs.getString(prefName, null);
		try {
			if (serializeString != null) {
				prefObjecct = (Object) ObjectSerializer
						.deserialize(serializeString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return prefObjecct;

	}
	
	
	public static Object getObjectInAppPreference(Context context,
			String prefName) {
		Object prefObjecct = null;
		SharedPreferences prefs = getAppSharedPreference(context);
		String serializeString = prefs.getString(prefName, null);
		try {
			if (serializeString != null) {
				prefObjecct = (Object) ObjectSerializer
						.deserialize(serializeString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return prefObjecct;

	}

	public static void saveStringInPreference(Activity activity,String key,String value) {
		SharedPreferences.Editor prefsEditor = getAppPrefsEditor(activity);
		prefsEditor.putString(key, value);
		prefsEditor.commit();
	}

	public static String getStringFromPreference(Activity activity,String key) {
		SharedPreferences prefs = getAppSharedPreference(activity);
		return prefs.getString(key, "");
	}
	
	public static void saveIntegerInPreference(Activity activity,String key,int value) {
		SharedPreferences.Editor prefsEditor = getAppPrefsEditor(activity);
		prefsEditor.putInt(key, value);
		prefsEditor.commit();
	}

	public static int getIntegerFromPreference(Activity activity,String key) {
		SharedPreferences prefs = getAppSharedPreference(activity);
		return prefs.getInt(key, 0);
	}
}
