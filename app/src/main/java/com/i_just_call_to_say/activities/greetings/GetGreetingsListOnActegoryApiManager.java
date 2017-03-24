package com.i_just_call_to_say.activities.greetings;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import com.google.gson.Gson;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.GreetingsCard;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.i_just_call_to_say.dto.wrapper.GreetingsCardDTOWrapper;
import com.i_just_call_to_say.dto.wrapper.UserDTOWrapper;
import com.utility.constants.UrlConstants;
import com.utility.orm_utility.Orm_SQLManager;
import com.webservices.RequestHandler;
import com.webservices.TaskManager;


public class GetGreetingsListOnActegoryApiManager extends RequestHandler{

	private Activity mActivity;
	private RingToneBaseApplication ringToneBaseApplication;

	private String loadingMessage;
	private String mobileno;
	private Contacts contacts;
	private Object[] values;

	public interface updateContacts{
		public void updateContact();
	}

	public GetGreetingsListOnActegoryApiManager(Activity activity,RingToneBaseApplication ringToneBaseApplication,String loadingMessage,Object[] values) {
		super(activity);
		mActivity = activity;
	
		this.ringToneBaseApplication=ringToneBaseApplication;
	
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
		return UrlConstants.GET_GREETINGS_LIST;
	}
	

	@Override
	public String[] getKeys() {
		// TODO Auto-generated method stub
		String[] keys={"access_token","category_id","device_token"};
		return keys;
	}

	@Override
	public Object[] getValues() {
		// TODO Auto-generated method stub
	
		return values;
	}

	@Override
	public void onSuccess(String responses) {
		System.out.println("call update");
		String response=responses.replace("https","http");
		Gson gson=new Gson();
		GreetingsCardDTOWrapper greetingsCardDTOWrapper=gson.fromJson(response, GreetingsCardDTOWrapper.class);
		List<GreetingsCard> greetingscardlist= greetingsCardDTOWrapper.getGreetingscardlist();
		((GetGreetinsOnCategory)mActivity).getAllGreetingsList(greetingscardlist);
	

	}



}
