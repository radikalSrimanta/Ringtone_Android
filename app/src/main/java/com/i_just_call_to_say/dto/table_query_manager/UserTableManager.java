package com.i_just_call_to_say.dto.table_query_manager;

import java.sql.SQLException;
import java.util.List;



import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.dto.User;
import com.utility.PreferenceUtility;
import com.utility.orm_utility.DatabaseManager;
import com.utility.orm_utility.Orm_SQLManager;





import android.app.Activity;
import android.content.Context;



public class UserTableManager {
	
//	public static void savePreviousUserID(Activity activity,int userID){
//		PreferenceUtility.saveIntegerInPreference(activity, "PreviousID", userID);
//	}
//	
//	public static void saveEmailAndPassword(Activity activity,String emailID,String passowrd){
//		PreferenceUtility.saveStringInPreference(activity, "EmaiID", emailID);
//		PreferenceUtility.saveStringInPreference(activity, "Password", passowrd);
//	}
//	
//	public static int getPreviousUserID(Activity activity){
//		return PreferenceUtility.getIntegerFromPreference(activity, "PreviousID");
//	}
	
	public static User getSavedUser(Activity activity,RingToneBaseApplication ringToneBaseApplication){
		User user = null;
		user = (User) PreferenceUtility.getObjectInAppPreference(activity, PreferenceUtility.USER_PROFILE);
		/*List<User> users = ((List<User>)Orm_SQLManager.getAllTableObjects(User.class, activity, ringToneBaseApplication.databaseManager));
		System.out.println("user size "+users.size());
		if(users != null && users.size() > 0){
			user = users.get(0);
		}*/
		return user;
	}
	

	public static void deleteUserFromEventTable(String access_token,
			DatabaseManager databaseManager, Context context) {
		RuntimeExceptionDao<User, Integer> runtimeExceptionDao = (RuntimeExceptionDao<User, Integer>) databaseManager
				.getHelper(context).getRuntimeExceptionDao(User.class);
		DeleteBuilder<User, Integer> deleteBuilder = runtimeExceptionDao
				.deleteBuilder();
		Where where = deleteBuilder.where();
		try {
			where.in("Access_Token", access_token);
			deleteBuilder.delete();
			System.out.println("Query "+ deleteBuilder.prepareStatementString().toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	public static void updateUserStatus(String access_token,DatabaseManager databaseManager,Activity activity,String subscribed){
		RuntimeExceptionDao<User, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<User, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(User.class);
		try {
			UpdateBuilder<User, Integer> updateBuilder = objRuntimeExceptionDao.updateBuilder(); 
			updateBuilder.where().eq("Access_Token", access_token);
			updateBuilder.updateColumnValue("Purchase_Status", subscribed); 
			updateBuilder.update();
			System.out.println("Query "+ updateBuilder.prepareStatementString().toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
