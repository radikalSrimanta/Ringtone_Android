package com.i_just_call_to_say.activities.connections;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import com.google.gson.Gson;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.i_just_call_to_say.dto.wrapper.ContactsDTOWrapper;
import com.i_just_call_to_say.dto.wrapper.UserDTOWrapper;
import com.utility.constants.UrlConstants;
import com.utility.orm_utility.Orm_SQLManager;
import com.webservices.RequestHandler;
import com.webservices.TaskManager;


public class AllContactsApiManager extends RequestHandler{

	private Activity mActivity;


	private String loadingMessage;


	private Object[] values;



	public AllContactsApiManager(Activity activity,String loadingMessage,Object[] values) {
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
		// TODO Auto-generated method stub
		return UrlConstants.HOME;
	}


	@Override
	public String[] getKeys() {
		// TODO Auto-generated method stub
		String[] keys={"access_token"};
		return keys;
	}

	@Override
	public Object[] getValues() {
		// TODO Auto-generated method stub

		return values;
	}

	@Override
	public void onSuccess(String response) {
		Gson gson=new Gson();
		ContactsDTOWrapper contactsDTOWrapper = gson.fromJson(response, ContactsDTOWrapper.class);
		List<Contacts> contact_list=contactsDTOWrapper.getContact_list();
		for(Contacts contacts: contact_list){

		}





	}



}
