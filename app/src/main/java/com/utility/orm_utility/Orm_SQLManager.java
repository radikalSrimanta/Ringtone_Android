package com.utility.orm_utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;

import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class Orm_SQLManager {

	public static int insertIntoTable(Class<?> clazz, Object object,
			Activity activity, DatabaseManager databaseManager) {
		RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager
				.getHelper(activity).getRuntimeExceptionDao(clazz);
		CreateOrUpdateStatus  createOrUpdateStatus = runtimeExceptionDao.createOrUpdate(object);
		int status = createOrUpdateStatus.getNumLinesChanged();
		return status;
	}

	public static void insertCollectionIntoTable(Class<?> clazz,final List<?> objects, Activity activity,DatabaseManager databaseManager) {
		final RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(clazz);
		runtimeExceptionDao.callBatchTasks(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				for (Object object : objects) {
					runtimeExceptionDao.createOrUpdate(object);
				}
				return null;
			}

		});
	}
	
	public static void insertCollectionIntoTableForString(Class<?> clazz,
			final List<?> objects, Activity activity,
			DatabaseManager databaseManager) {
		final RuntimeExceptionDao<Object, String> runtimeExceptionDao = (RuntimeExceptionDao<Object, String>) databaseManager
				.getHelper(activity).getRuntimeExceptionDao(clazz);
		runtimeExceptionDao.callBatchTasks(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				for (Object object : objects) {
					runtimeExceptionDao.createOrUpdate(object);
				}
				return null;
			}

		});
	}
	
	public static void insertCollectionIntoTable(Class<?> clazz,
			final Collection<?> objects, Activity activity,
			DatabaseManager databaseManager) {
		final RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager
				.getHelper(activity).getRuntimeExceptionDao(clazz);
		runtimeExceptionDao.callBatchTasks(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				for (Object object : objects) {
					runtimeExceptionDao.createOrUpdate(object);
				}
				return null;
			}

		});
	}
	

	public static Object getAllTableObjects(Class<?> clazz,Activity activity, DatabaseManager databaseManager) {
		RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(clazz);
		return runtimeExceptionDao.queryForAll();
	}
	
	public static Object getAllTableObjects(Class<?> clazz,Context context, DatabaseManager databaseManager) {
		RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager.getHelper(context).getRuntimeExceptionDao(clazz);
		return runtimeExceptionDao.queryForAll();
	}
	
	public static void truncateTable(Class<?> clazz,Context context, DatabaseManager databaseManager) throws SQLException {
		RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager.getHelper(context).getRuntimeExceptionDao(clazz);
		DeleteBuilder<Object, Integer> deleteBuilder = runtimeExceptionDao.deleteBuilder();
		int index;
		try {
			index = deleteBuilder.delete();
			System.out.println("deleted index "+index);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
		
		}
	
	public static Object getAllTableObjectsByOrder(
			DatabaseManager databaseManager, Activity activity,String orderByColumName,boolean isAscending,
			Class<?> clazz) {
		Object response = null;
		try {
			RuntimeExceptionDao<Object, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(clazz);
			QueryBuilder<Object, Integer> queryBuilder = objRuntimeExceptionDao
					.queryBuilder();
			queryBuilder.orderBy(orderByColumName, isAscending/**isAscending*/);
			response = queryBuilder.query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public static Object getTableObjectsByOrder(
			DatabaseManager databaseManager, Activity activity, String column,String orderByColumName,boolean isAscending,
			int id, Class<?> clazz) {
		Object response = null;
		try {
			RuntimeExceptionDao<Object, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(clazz);
			QueryBuilder<Object, Integer> queryBuilder = objRuntimeExceptionDao
					.queryBuilder();
			Where where = queryBuilder.where();
			where.eq(column, id);
			queryBuilder.orderBy(orderByColumName, isAscending/**isAscending*/);
			response = queryBuilder.query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	/**
	* Remove those rows that id not supplied in rowIdContainner
	* 
	* @param rowIdContainner Hold all the id with separator
	* @param separator separate name
	* @param columnName
	* @param clazz
	* @param activity
	* @param databaseManager
	*/
	public static void keepSeletedIdRemoveOther(String rowIdContainner,
			String separator, String columnName, Class<?> clazz,
			Activity activity, DatabaseManager databaseManager) {
		try {
			String[] strArray = rowIdContainner.split(",");
			List<Object> keepsIdArray = new ArrayList<Object>();
			if (strArray != null && strArray.length > 0) {
				for (String id : strArray)
					keepsIdArray.add(id);
				RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager
						.getHelper(activity).getRuntimeExceptionDao(clazz);
				DeleteBuilder<Object, Integer> deleteBuilder = runtimeExceptionDao
						.deleteBuilder();
				Where where = deleteBuilder.where();
				where.notIn(columnName, keepsIdArray);
				System.out.println("Query "
						+ deleteBuilder.prepareStatementString());
				deleteBuilder.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	* Remove those rows that id supplied in rowIdContainner
	* 
	* @param rowIdContainner Hold all the id with separator
	* @param separator separate name
	* @param columnName
	* @param clazz
	* @param activity
	* @param databaseManager
	*/
	public static void removeSeletedId(String rowIdContainner,
			String separator, String columnName, Class<?> clazz,
			Activity activity, DatabaseManager databaseManager) {
		try {
			String[] strArray = rowIdContainner.split(",");
			List<Object> keepsIdArray = new ArrayList<Object>();
			if (strArray != null && strArray.length > 0) {
				for (String id : strArray)
					keepsIdArray.add(id);
				RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager
						.getHelper(activity).getRuntimeExceptionDao(clazz);
				DeleteBuilder<Object, Integer> deleteBuilder = runtimeExceptionDao
						.deleteBuilder();
				Where where = deleteBuilder.where();
				where.in(columnName, keepsIdArray);
				System.out.println("Query "
						+ deleteBuilder.prepareStatementString());
				deleteBuilder.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static Object getSelectedColumn(
			DatabaseManager databaseManager, Activity activity, String column,
			int id, Class<?> clazz) {
		Object response = null;
		try {
			RuntimeExceptionDao<Object, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager
					.getHelper(activity).getRuntimeExceptionDao(clazz);
			QueryBuilder<Object, Integer> queryBuilder = objRuntimeExceptionDao
					.queryBuilder();
			Where where = queryBuilder.where();
			where.eq(column, id);
			System.out.println("Query: " + queryBuilder.prepareStatementString().toString());
			response = queryBuilder.query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	



	
	
//	public static void innerJOINGROUPS(Activity activity, DatabaseManager databaseManager){
//		String SQLquery = "SELECT `Group`.* FROM `Group`INNER JOIN `Image` ON `Group`.`id` = `Image`.`image_id`";
//		Cursor cursor = databaseManager.getHelper(activity).getWritableDatabase().rawQuery(SQLquery, null);
//		System.out.println("Val: " + cursor.getCount());
//		if(cursor.moveToFirst()){
//			System.out.println("Val: " + cursor.getColumnIndex("name")); 
//		}
//	}

}
