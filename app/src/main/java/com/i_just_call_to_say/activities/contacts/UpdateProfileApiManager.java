package com.i_just_call_to_say.activities.contacts;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.google.gson.Gson;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.greetings.UpdateUserDetailsActivity;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.utility.AlertDialogCallBack;
import com.utility.CustomDialogUtility;
import com.utility.constants.UrlConstants;
import com.webservices.RequestHandler;
import com.webservices.TaskManager;


public class UpdateProfileApiManager extends RequestHandler{

	private Activity mActivity;
	private RingToneBaseApplication ringToneBaseApplication;

	private String loadingMessage;
	private String mobileno;
	private Contacts contacts;
	private Object[] values;
	private String[] keyfile,keyvalue;
	public interface updateContacts{
		public void updateContact();
	}

	public UpdateProfileApiManager(Activity activity,RingToneBaseApplication ringToneBaseApplication,String loadingMessage,Object[] valuess,String[] keyfile,String[] keyvalue) {
		super(activity);
		this.ringToneBaseApplication = ringToneBaseApplication;
		mActivity = activity;
		this.values=valuess;
		this.loadingMessage = loadingMessage;
		this.keyfile=keyfile;
		this.keyvalue=keyvalue;
		callService();
	}

	private void callService(){
		TaskManager taskManager = new TaskManager(this, this, mActivity);
		System.out.println("keyfile"+keyfile.length+" keyvalue "+keyvalue.length);
		taskManager.callService(keyfile,keyvalue,loadingMessage);
	}

	@Override
	public String getWebServiceMethod() {
		return UrlConstants.UPDATE_CONNECTION;
	}

	@Override
	public String[] getKeys() {
		String[] keys={"access_token","connections_id","my_display_name","device_token","remove_my_image"};
		return keys;
	}

	@Override
	public Object[] getValues() {

		return values;
	}

	@Override
	public void onSuccess(String response) {
		try {
			if (mActivity instanceof UpdateUserDetailsActivity) {
				JSONObject object = new JSONObject(response);
				String status = object.getString("status");
				Gson gson = new Gson();
				Contacts updateContact = gson.fromJson(object.getString("contacts"), Contacts.class);
				System.out.println("my update display number"+updateContact.getContact_number());
				int update = ContactsTableManager.updateContactInformation(updateContact, ringToneBaseApplication.databaseManager,mActivity);
				System.out.println("update status----------> "+ update);
				((UpdateUserDetailsActivity)mActivity).updateservice_callBack(status);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onFailure(String message, String errorCode) {
		System.out.println("my update display number 1");
		if (mActivity instanceof UpdateUserDetailsActivity) {
			System.out.println("my update display number 11");
			((UpdateUserDetailsActivity)mActivity).updateservice_onFaluire();
		}
		super.onFailure(message, errorCode);
	}

}
