package com.i_just_call_to_say.activities.loginregistrartion;

import android.app.Activity;

import com.google.gson.Gson;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.splash.SplashActivity;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.wrapper.UserDTOWrapper;
import com.utility.PreferenceUtility;
import com.utility.constants.UrlConstants;
import com.utility.orm_utility.Orm_SQLManager;
import com.webservices.RequestHandler;
import com.webservices.TaskManager;


public class LoginApiManager extends RequestHandler{

	private Activity mActivity;
	private RingToneBaseApplication ringToneBaseApplication;
	private Object[] values;
	private String loadingMessage;


	public LoginApiManager(Activity activity,Object[] values,RingToneBaseApplication ringToneBaseApplication,String loadingMessage) {
		super(activity);
		mActivity = activity;
		this.ringToneBaseApplication=ringToneBaseApplication;
		this.values = values;
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
		return UrlConstants.Login;
	}

	@Override
	public String[] getKeys() {
		// TODO Auto-generated method stub
		String[] keys={"name","country_code","phone_number","device_token","device_type"};
		return keys;
	}

	@Override
	public Object[] getValues() {
		// TODO Auto-generated method stub
		System.out.println("user login55"+values.toString());
		return values;
	}

	@Override
	public void onSuccess(String response) {
		System.out.println("user login5"+response);
		Gson gson=new Gson();
		UserDTOWrapper userDtoWrapper = gson.fromJson(response, UserDTOWrapper.class);
		User user = userDtoWrapper.getUser();
		/*Orm_SQLManager.insertIntoTable(User.class, user, mActivity, ringToneBaseApplication.databaseManager);*/
		PreferenceUtility.saveObjectInAppPreference(mActivity, user, PreferenceUtility.USER_PROFILE);
		if(mActivity instanceof TermAndCondition){
			((TermAndCondition)mActivity).login_serviceCallBackOnSuccess();
		}
		else if(mActivity instanceof SplashActivity){
			((SplashActivity)mActivity).login_serviceCallBackOnSuccess();
		}
		else if(mActivity instanceof BrowserActivity){
			((BrowserActivity)mActivity).login_serviceCallBackOnSuccess();
		}
		
	}

	@Override
	public void onFailure(String message, String errorCode) {
		System.out.println("user login5"+message+errorCode);
		super.onFailure(message, errorCode);
		
		if(mActivity instanceof TermAndCondition){
			((TermAndCondition)mActivity).login_serviceCallBackOnFaluire(message);
		}
		else if(mActivity instanceof SplashActivity){
			((SplashActivity)mActivity).login_serviceCallBackOnFaluire(message);
		}
		else if(mActivity instanceof BrowserActivity){
			((BrowserActivity)mActivity).login_serviceCallBackOnFaluire(message);
		}
	}
	

}
