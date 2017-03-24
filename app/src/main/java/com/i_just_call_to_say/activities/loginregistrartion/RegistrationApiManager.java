package com.i_just_call_to_say.activities.loginregistrartion;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.j256.ormlite.table.TableUtils;
import com.i_just_call_to_say.activities.base.AppConstant;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.connections.ConnectionsActivity;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.wrapper.UserDTOWrapper;
import com.utility.GetShortUrl;
import com.utility.PreferenceUtility;
import com.utility.TinyUrl;
import com.utility.URLShortener.URLShortenerListener;
import com.utility.constants.UrlConstants;
import com.utility.orm_utility.Orm_SQLManager;
import com.webservices.RequestHandler;
import com.webservices.TaskManager;

public class RegistrationApiManager extends RequestHandler{

	private Activity mActivity;
	private RingToneBaseApplication ringToneBaseApplication;
	private Object[] values;
	private String loadingMessage;
	

	public RegistrationApiManager(Activity activity, Object[] values,
			RingToneBaseApplication ringToneBaseApplication, String loadingMessage) {
		super(activity);
		mActivity = activity;
		this.ringToneBaseApplication = ringToneBaseApplication;
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
		return UrlConstants.Registration;
	}

	@Override
	public String[] getKeys() {
		String[] keys = { "name","country_code", "phone_number", "device_token", "device_type" };
		return keys;
	}

	@Override
	public Object[] getValues() {
		return values;
	}

	@Override
	public void onSuccess(String response) {
		Gson gson = new Gson();
		UserDTOWrapper userDtoWrapper = gson.fromJson(response,	UserDTOWrapper.class);
		final User user = userDtoWrapper.getUser();
		String msg = "";
		
		try {
			 msg = new JSONObject(response).optString("status");
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		
		try {
			//TableUtils.clearTable(ringToneBaseApplication.databaseManager.getHelper(mActivity.getApplicationContext()).getConnectionSource(),User.class);
			//Orm_SQLManager.insertIntoTable(User.class, user, mActivity,	ringToneBaseApplication.databaseManager);
			PreferenceUtility.saveObjectInAppPreference(mActivity, user, PreferenceUtility.USER_PROFILE);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		String activationLink = null;
		try {
			activationLink = new JSONObject(response).getJSONObject("result").getString("activation_link");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		if(mActivity instanceof TermAndCondition){
//			GetShortUrl url = new GetShortUrl(mActivity, activationLink, new URLShortenerListener() {
//				@Override
//				public void onComplete(String url) {
//					((TermAndCondition) mActivity).sendSMS(user.getCountry_code()+user.getPhone_number(), AppConstant.REGISTRATION_ACTIVATION_MSG + url);
//				}
//			});
//			url.execute();
//			Toast.makeText(mActivity, "activation link "+ activationLink, Toast.LENGTH_LONG).show();
//			System.out.println("activation link --->"+ activationLink);
			
			((TermAndCondition) mActivity).sendSMS(user.getCountry_code()+user.getPhone_number(), AppConstant.REGISTRATION_ACTIVATION_MSG + activationLink);
			((TermAndCondition) mActivity).registration_serviceCallBack(msg);
		}
		
	}


}
