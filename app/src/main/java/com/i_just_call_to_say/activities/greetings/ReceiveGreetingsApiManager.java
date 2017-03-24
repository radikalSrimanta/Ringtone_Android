package com.i_just_call_to_say.activities.greetings;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.View;

import com.google.gson.Gson;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.ReceivedGreetingsCard;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.i_just_call_to_say.dto.wrapper.ContactsDTOWrapper;
import com.i_just_call_to_say.dto.wrapper.ReceivedGreetingDTOWrapper;
import com.i_just_call_to_say.dto.wrapper.UserDTOWrapper;
import com.utility.constants.UrlConstants;
import com.utility.orm_utility.Orm_SQLManager;
import com.webservices.RequestHandler;
import com.webservices.TaskManager;


public class ReceiveGreetingsApiManager extends RequestHandler{

	private Activity mActivity;
	private RingToneBaseApplication ringToneBaseApplication;

	private String loadingMessage;
	private String mobileno;
	private Contacts contacts;
	private Object[] values;

	public interface updateContacts{
		public void updateContact();
	}

	public ReceiveGreetingsApiManager(Activity activity,RingToneBaseApplication ringToneBaseApplication,String loadingMessage,Object[] values) {
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
		return UrlConstants.RECEIEVE_GREETINGS;
	}
	

	@Override
	public String[] getKeys() {
		String[] keys={"access_token","device_token"};
		return keys;
	}

	@Override
	public Object[] getValues() {
		return values;
	}

	@Override
	public void onSuccess(String response) {
		System.out.println("receive");
		Gson gson=new Gson();
		ReceivedGreetingDTOWrapper receivedGreetingDTOWrapper = gson.fromJson(response, ReceivedGreetingDTOWrapper.class);
		List<ReceivedGreetingsCard> greeting_list=receivedGreetingDTOWrapper.getGreetingscardlist();
		((GreetingsActivity)mActivity).receivingCardDetails(greeting_list);
		
	}
	
	@Override
	public void onFailure(String message, String errorCode) {
		((GreetingsActivity)mActivity).tv_msg.setVisibility(View.VISIBLE);
//		super.onFailure(message, errorCode);
	}

}
