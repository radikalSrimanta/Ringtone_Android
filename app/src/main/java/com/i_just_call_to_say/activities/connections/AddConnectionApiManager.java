package com.i_just_call_to_say.activities.connections;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import com.google.gson.Gson;
import com.i_just_call_to_say.activities.base.AppConstant;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.loginregistrartion.TermAndCondition;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.i_just_call_to_say.dto.wrapper.UserDTOWrapper;
import com.utility.GetShortUrl;
import com.utility.URLShortener.URLShortenerListener;
import com.utility.constants.UrlConstants;
import com.utility.orm_utility.Orm_SQLManager;
import com.webservices.RequestHandler;
import com.webservices.TaskManager;


public class AddConnectionApiManager extends RequestHandler{

	private Activity mActivity;
	private RingToneBaseApplication ringToneBaseApplication;

	private String loadingMessage;
	private String mobileno;
	private Contacts contacts;
	private Object[] values;
	private IUpdateContacts iUpdateContacts;
	public interface updateContacts{
		public void updateContact();
	}

	public AddConnectionApiManager(Activity activity,Contacts contacts,RingToneBaseApplication ringToneBaseApplication,String loadingMessage,Object[] values,IUpdateContacts iUpdateContacts) {
		super(activity);
		mActivity = activity;
		this.iUpdateContacts=iUpdateContacts;
		this.ringToneBaseApplication=ringToneBaseApplication;
		this.contacts=contacts;
		this.values=values;
		this.loadingMessage = loadingMessage;
		
		callService();
	}

	private void callService(){
		TaskManager taskManager = new TaskManager(this, this, mActivity);
		taskManager.callService(loadingMessage);
	}

	@Override
	public String getWebServiceMethod() {
		// TODO Auto-generated method stub
		return UrlConstants.ADD_CONNECTION;
	}
	

	@Override
	public String[] getKeys() {
		// TODO Auto-generated method stub
		String[] keys={"access_token","approval_status","phone_number","device_token"};
		return keys;
	}

	@Override
	public Object[] getValues() {
		// TODO Auto-generated method stub
	
		return values;
	}

	@Override
	public void onSuccess(String response) {
		final JSONObject jsonObject;
//		try {
//			jsonObject = new JSONObject(response);
//			JSONObject result=jsonObject.optJSONObject("result");
//			final String connectionstatus=result.optString("connect_status");
//			final String connectionid=result.optString("connection_id");
//			
//			String connection_link = result.optString("connection_link");
//			if(connection_link!=null && !connection_link.equals("")){
//			GetShortUrl url = new GetShortUrl(mActivity, connection_link, new URLShortenerListener() {
//				
//				@Override
//				public void onComplete(String url) {
//						String message = " wants to connect with you via 'I Just Called To Say' app. Please download the app from here:"+AppConstant.APP_STORE_LINK +"\n Please click on the below link to accept the connection request:"+url;
//						ContactsTableManager.updateStatusOfContacts(connectionstatus,connectionid, contacts.getContact_number(), ringToneBaseApplication.databaseManager, mActivity);
//						if(iUpdateContacts != null)
//							iUpdateContacts.updateContacts(jsonObject.optString("status"),message);
//					}
//			});
//			url.execute();
//			}else{
//				ContactsTableManager.updateStatusOfContacts(connectionstatus,connectionid, contacts.getContact_number(), ringToneBaseApplication.databaseManager, mActivity);
//				if(iUpdateContacts != null)
//					iUpdateContacts.updateContacts(jsonObject.optString("status"),"");
//			}
			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		
		try {
			jsonObject = new JSONObject(response);
			JSONObject result=jsonObject.optJSONObject("result");
			String connectionstatus=result.optString("connect_status");
			String connectionid=result.optString("connection_id");
			
			String connection_link = result.optString("connection_link");
			String message = " wants to connect with you via 'I Just Called To Say' app. Please download the app from here:"+AppConstant.APP_STORE_LINK +"\n Please click on the below link to accept the connection request:"+connection_link;
			
			ContactsTableManager.updateStatusOfContacts(connectionstatus,connectionid, contacts.getContact_number(), ringToneBaseApplication.databaseManager, mActivity);
			if(iUpdateContacts != null)
				iUpdateContacts.updateContacts(jsonObject.optString("status"),message);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onFailure(String message, String errorCode) {
		super.onFailure(message, errorCode);
		if(iUpdateContacts != null)
			iUpdateContacts.onFailuepdateContacts(message);
	}


}
