package com.isis.module.push_notification.utility;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtility {

	public static void saveUser(final Activity activity, Object user,String preferenceName) {
		SharedPreferences myPrefs = activity.getSharedPreferences(
				preferenceName, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = myPrefs.edit();
		try {
			prefsEditor
					.putString(preferenceName, ObjectSerializer.serialize(user));
		} catch (IOException e) {
			e.printStackTrace();
		}
		prefsEditor.commit();
	}

	

	public static Object fetchSavedUser(final Activity activity,String preferenceName) {
		SharedPreferences myPrefs = activity.getSharedPreferences(
				"YummoUserPrefs", Context.MODE_PRIVATE);
		Object user = null;
		String strUser = myPrefs.getString(preferenceName, null);
		try {
			if (strUser != null) {
				user = (Object) ObjectSerializer.deserialize(strUser);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return user;
	}
}
