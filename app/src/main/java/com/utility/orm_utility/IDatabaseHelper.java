package com.utility.orm_utility;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;

public interface IDatabaseHelper {

	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource);
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,int oldVersion, int newVersion);
	public void close();
	
}
