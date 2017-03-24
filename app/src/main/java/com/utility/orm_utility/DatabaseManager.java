package com.utility.orm_utility;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;

public class DatabaseManager {

	// name of the database file for your application -- change to something
	// appropriate for your app
	public static String DATABASE_NAME = RingToneBaseApplication.DATABASE_NAME;
	
	// any time you make changes to your database objects, you may have to
	// increase the database version
	public static int DATABASE_VERSION = RingToneBaseApplication.DATABASE_VERSION;

	public static IDatabaseHelper iDatabaseHelper;

	private static DatabaseManager databaseManager;
	
	private DatabaseHelper databaseHelper = null;

	public static synchronized DatabaseManager getDatabaseManager(
			Context context, IDatabaseHelper iDatabaseHelper,
			String databaeName, int databaseVersion) {
		if (databaseManager == null) {
			databaseManager = new DatabaseManager(context, iDatabaseHelper,
					databaeName, databaseVersion);
		}
		return databaseManager;
	}

	private DatabaseManager(Context context, IDatabaseHelper iDatabaseHelper,
			String databaeName, int databaseVersion) {
		DatabaseManager.iDatabaseHelper = iDatabaseHelper;
		DATABASE_NAME = databaeName;
		DATABASE_VERSION = databaseVersion;
		new DatabaseHelper(context);
	}

	public DatabaseHelper getHelper(Context context) {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(context,
					DatabaseHelper.class);
		}
		return databaseHelper;
	}

	// releases the helper once usages has ended
	public void releaseHelper(DatabaseHelper helper) {
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

}
