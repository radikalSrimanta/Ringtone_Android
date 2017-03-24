package com.i_just_call_to_say.activities.greetings;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.menu;
import android.app.Activity;
import com.google.gson.Gson;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.i_just_call_to_say.dto.wrapper.UserDTOWrapper;
import com.utility.AlertDialogCallBack;
import com.utility.CustomDialogUtility;
import com.utility.DialogUtility;
import com.utility.constants.UrlConstants;
import com.utility.orm_utility.Orm_SQLManager;
import com.webservices.RequestHandler;
import com.webservices.TaskManager;


public class SendGreetingsApiManager extends RequestHandler{

	private Activity mActivity;


	private String loadingMessage;
	
	private Object[] values;

	public interface updateContacts{
		public void updateContact();
	}

	public SendGreetingsApiManager(Activity activity,RingToneBaseApplication ringToneBaseApplication,String loadingMessage,Object[] values) {
		super(activity);
		mActivity = activity;
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
		return UrlConstants.SEND_CARDS;
	}


	@Override
	public String[] getKeys() {
		String[] keys={"access_token","category_id","card_id","connections_id","card_send_time","device_token"};
		return keys;
	}

	@Override
	public Object[] getValues() {
		return values;
	}

	@Override
	public void onSuccess(String response) {

	try {
		JSONObject object = new JSONObject(response);
		String status = object.getString("status");
		
		if(mActivity instanceof SendContactActivity)
			((SendContactActivity)mActivity).sendgreeting_serviceCallBack(status);
		
		if(mActivity instanceof GetSendGreetingsLargeActivity)
			CustomDialogUtility.showMessageWithOk(status, mActivity);
			
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}



}
