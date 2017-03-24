package com.i_just_call_to_say.activities.greetings;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import com.google.gson.Gson;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.dto.CardCategory;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.i_just_call_to_say.dto.wrapper.CategoryDTOWrapper;
import com.i_just_call_to_say.dto.wrapper.UserDTOWrapper;
import com.utility.constants.UrlConstants;
import com.utility.orm_utility.Orm_SQLManager;
import com.webservices.RequestHandler;
import com.webservices.TaskManager;


public class GetCardCategoryApiManager extends RequestHandler{

	private Activity mActivity;
	private RingToneBaseApplication ringToneBaseApplication;

	private String loadingMessage;
	private String mobileno;
	private Contacts contacts;
	private Object[] values;

	public interface updateContacts{
		public void updateContact();
	}

	public GetCardCategoryApiManager(Activity activity,RingToneBaseApplication ringToneBaseApplication,String loadingMessage,Object[] values) {
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
		return UrlConstants.GET_CARD_CATEGORY;
	}
	

	@Override
	public String[] getKeys() {
		// TODO Auto-generated method stub
		String[] keys={"access_token","device_token"};
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
	CategoryDTOWrapper categoryDTOWrapper = gson.fromJson(response, CategoryDTOWrapper.class);
	List<CardCategory> cardcategorylist=categoryDTOWrapper.getCategory_list();
	((GreetingsActivity)mActivity).getCateryList(cardcategorylist);
	

	}



}
