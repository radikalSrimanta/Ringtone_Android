package com.i_just_call_to_say.dto.table_query_manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.dto.ContactStatusDto;
import com.i_just_call_to_say.dto.Contacts;
import com.utility.orm_utility.DatabaseManager;
import com.utility.orm_utility.Orm_SQLManager;

public class ContactsTableManager {
	
	public static List<Contacts>  getSavedContactList(Activity activity,RingToneBaseApplication ringToneBaseApplication){
		List<Contacts> contacts=new ArrayList<Contacts>();
		contacts = ((List<Contacts>)Orm_SQLManager.getAllTableObjects(Contacts.class, activity, ringToneBaseApplication.databaseManager));
		return contacts;
	}
	
	public static int updateStatusOfContacts(String connectionstatus,String connectionid,String contactNumber,DatabaseManager databaseManager,Activity activity){
		RuntimeExceptionDao<Contacts, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<Contacts, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(Contacts.class);
		try {
			UpdateBuilder<Contacts, Integer> updateBuilder = objRuntimeExceptionDao.updateBuilder(); 
			updateBuilder.where().eq("ContactNumber",contactNumber );
			updateBuilder.updateColumnValue("ContactStatus", connectionstatus); 
			updateBuilder.updateColumnValue("ConnectionsId", connectionid);
			return updateBuilder.update();
			//System.out.println("Query "+ updateBuilder.prepareStatementString().toString()+" "+updateBuilder.update());
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	public static int updateStatusOfContacts(Contacts contactsr,DatabaseManager databaseManager,Activity activity){
		String contact_image="";
		String contact_ringtone="";
		if(!contactsr.getContact_image().equals("")){
			contact_image=contactsr.getContact_image();
			System.out.println("get contact image"+contact_image);
		}
		if(!contactsr.getContact_ringtone().equals("")){
			contact_ringtone=contactsr.getContact_ringtone();
			System.out.println("get contact ringtone"+contact_ringtone);
		}
		
		RuntimeExceptionDao<Contacts, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<Contacts, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(Contacts.class);
		try {
			UpdateBuilder<Contacts, Integer> updateBuilder = objRuntimeExceptionDao.updateBuilder(); 
			updateBuilder.where().eq("ContactNumber",contactsr.getContact_number() );
			if(!contactsr.getContact_ringtone().equals("")){
				contact_ringtone=contactsr.getContact_ringtone();
				Log.e("RingTone>>>>",contact_ringtone);
				updateBuilder.updateColumnValue("ContactRingtone", contact_ringtone.replaceAll("https","http"));
				Log.e("RingTone2>>>>",contact_ringtone);
			}else
			   updateBuilder.updateColumnValue("ContactRingtone", contactsr.getContact_ringtone());

			if(!contactsr.getContact_image().equals("")){
				contact_image=contactsr.getContact_image();
				updateBuilder.updateColumnValue("ContactImage", contact_image.replaceAll("https","http"));
			}else
			//String ss1= contactsr.getContact_image();
			updateBuilder.updateColumnValue("ContactImage", contactsr.getContact_image());
//			if(!contactsr.getContact_name().equals(""))
//			updateBuilder.updateColumnValue("ContactName", contactsr.getContact_name()); 
			updateBuilder.updateColumnValue("ContactStatus", contactsr.getConnection_status()); 
			updateBuilder.updateColumnValue("ConnectionsId", contactsr.getConnection_id()); 
			updateBuilder.updateColumnValue("my_display_name", contactsr.getMy_name());
			String ss= contactsr.getMy_ringtone();
			 System.out.println("get contact ringtone 1"+ss.replaceAll("https","http"));
			updateBuilder.updateColumnValue("my_ringtone_file", ss.replaceAll("https","http"));

			updateBuilder.updateColumnValue("purchase_status", contactsr.isPurchase_status());
			if(!contactsr.getMy_image_thumb().equals(""))
				updateBuilder.updateColumnValue("my_image_path_thumb", contactsr.getMy_image_thumb()); 
			if(!contactsr.getMy_image_normal().equals(""))
				updateBuilder.updateColumnValue("my_image_path_normal", contactsr.getMy_image_normal());
//			if(!contactsr.getContact_image().equals(""))
//				updateBuilder.updateColumnValue("contact_image", contactsr.getContact_image());

			return updateBuilder.update();
			//System.out.println("Query "+ updateBuilder.prepareStatementString().toString()+" "+updateBuilder.update());
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static int updateContactInformation(Contacts updateContacts,DatabaseManager databaseManager,Activity activity){
		RuntimeExceptionDao<Contacts, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<Contacts, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(Contacts.class);
		try {
			UpdateBuilder<Contacts, Integer> updateBuilder = objRuntimeExceptionDao.updateBuilder(); 
			updateBuilder.where().eq("ContactNumber",updateContacts.getContact_number() );
			updateBuilder.updateColumnValue("my_display_name", updateContacts.getMy_name());
			String ss= updateContacts.getMy_ringtone();
			System.out.println("get contact ringtone 11"+ss.replaceAll("https","http"));
			System.out.println("get contact image 1"+updateContacts.getMy_image_thumb());
			updateBuilder.updateColumnValue("my_ringtone_file", ss.replaceAll("https","http"));
			updateBuilder.updateColumnValue("my_image_path_thumb", updateContacts.getMy_image_thumb());
			updateBuilder.updateColumnValue("my_image_path_normal", updateContacts.getMy_image_normal()); 
			return updateBuilder.update();
			//System.out.println("Query "+ updateBuilder.prepareStatementString().toString()+" "+updateBuilder.update());
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static int updateContactsStatus(ContactStatusDto statusDto,DatabaseManager databaseManager,Activity activity){
		
		RuntimeExceptionDao<Contacts, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<Contacts, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(Contacts.class);
		try {
			UpdateBuilder<Contacts, Integer> updateBuilder = objRuntimeExceptionDao.updateBuilder(); 
			updateBuilder.where().eq("ContactNumber",statusDto.getContact_number() );
			updateBuilder.updateColumnValue("ContactStatus", statusDto.getConnection_status()); 
			return updateBuilder.update();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static int updateContactsImage(String contactNumber,String imagePath,DatabaseManager databaseManager,Activity activity){
		
		RuntimeExceptionDao<Contacts, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<Contacts, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(Contacts.class);
		try {
			UpdateBuilder<Contacts, Integer> updateBuilder = objRuntimeExceptionDao.updateBuilder(); 
			updateBuilder.where().eq("ContactNumber",contactNumber);
			updateBuilder.updateColumnValue("my_image_path_thumb", imagePath);
//			updateBuilder.updateColumnValue("ContactImage", imagePath); 
			return updateBuilder.update();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	public static int updatePurchaseStatus(String contactNumber,boolean status,DatabaseManager databaseManager,Activity activity){
		
		RuntimeExceptionDao<Contacts, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<Contacts, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(Contacts.class);
		try {
			System.out.println("contact number -->"+ contactNumber +" purchase status -->"+ status );
			UpdateBuilder<Contacts, Integer> updateBuilder = objRuntimeExceptionDao.updateBuilder(); 
			updateBuilder.where().like("ContactNumber",contactNumber);
			updateBuilder.updateColumnValue("purchase_status", status); 
			return updateBuilder.update();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	public static List<Contacts> getConnectedContactList(DatabaseManager databaseManager, Activity activity,String connection_status) {
		List<Contacts> eventList = null;
		try {
			RuntimeExceptionDao<Contacts, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<Contacts, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(Contacts.class);
			QueryBuilder<Contacts, Integer> queryBuilder = objRuntimeExceptionDao.queryBuilder();
			Where where = queryBuilder.where();
			where.eq("ContactStatus", connection_status);
			eventList = queryBuilder.query();
			System.out.println("Query : " + queryBuilder.prepareStatementString().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventList;
	}
	
	public static List<Contacts> getConnectedContact(DatabaseManager databaseManager, Activity activity,String phone_number) {
		List<Contacts> eventList = null;
		try {
			RuntimeExceptionDao<Contacts, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<Contacts, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(Contacts.class);
			QueryBuilder<Contacts, Integer> queryBuilder = objRuntimeExceptionDao.queryBuilder();
			Where where = queryBuilder.where();
//			where.eq("ContactNumber", phone_number);
			where.like("ContactNumber", "%"+phone_number+"%");
			eventList = queryBuilder.query();
			System.out.println("Query : " + queryBuilder.prepareStatementString().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventList;
	}
	
	
	public static List<Contacts> getAllContactList(DatabaseManager databaseManager, Activity activity) {
		List<Contacts> eventList = null;
		try {
			RuntimeExceptionDao<Contacts, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<Contacts, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(Contacts.class);
			QueryBuilder<Contacts, Integer> queryBuilder = objRuntimeExceptionDao.queryBuilder();
//			Where where = queryBuilder.where();
			eventList = queryBuilder.query();
			System.out.println("Query : " + queryBuilder.prepareStatementString().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventList;
	}
	
	
	

}
