package com.i_just_call_to_say.activities.base;


import com.google.firebase.iid.FirebaseInstanceId;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.User;
import com.utility.orm_utility.DatabaseManager;
import com.utility.orm_utility.IDatabaseHelper;

import android.app.Activity;
import android.app.Application;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDexApplication;

public class RingToneBaseApplication extends MultiDexApplication implements IDatabaseHelper{

	public static final String DATABASE_NAME = "RingtoneApp.db";
	public static final int DATABASE_VERSION = 1;
    public static String device_token;
	public DatabaseManager databaseManager;
	public static Activity currentActivity;

	@Override
	public void onCreate() {
		super.onCreate();

		databaseManager = DatabaseManager.getDatabaseManager(this, this, DATABASE_NAME, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			System.out.println("---- onCreate---");
			TableUtils.createTableIfNotExists(connectionSource, Contacts.class);
			TableUtils.createTableIfNotExists(connectionSource, User.class);
			System.out.println("all database table is created");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, Contacts.class, true);
			TableUtils.dropTable(connectionSource, User.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
