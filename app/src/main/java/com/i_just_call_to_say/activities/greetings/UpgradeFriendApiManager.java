package com.i_just_call_to_say.activities.greetings;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.drm.DrmStore.RightsStatus;
import android.view.View;

import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.greetings.UpdateUserDetailsActivity;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.i_just_call_to_say.dto.table_query_manager.UserTableManager;
import com.utility.CustomDialogUtility;
import com.utility.constants.UrlConstants;
import com.webservices.RequestHandler;
import com.webservices.TaskManager;

public class UpgradeFriendApiManager extends RequestHandler{
	private RingToneBaseActivity mActivity;
	private RingToneBaseApplication ringToneBaseApplication;
	//private User user;
	private String loadingMessage;
	private String mobileno;

	private Object[] values;
	
	public UpgradeFriendApiManager(RingToneBaseActivity activity,
			RingToneBaseApplication ringToneBaseApplication,String loadingMessage, Object[] values) {
		super(activity);
		mActivity = activity;

		this.ringToneBaseApplication = ringToneBaseApplication;
		//this.user = user;
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
		return UrlConstants.UPGRADE_FRIEND;	
		}

	@Override
	public String[] getKeys() {
		String[] keys={"access_token","receiver_phone_no","device_token"};
		return keys;
	}

	@Override
	public Object[] getValues() {
		return values;
	}

	@Override
	public void onSuccess(String response) {
		try {
			JSONObject jsonObject=new JSONObject(response);
			JSONObject resultobject=jsonObject.getJSONObject("result");
			String purchase_status=resultobject.getString("purchase_status");
			if(purchase_status.equals("true")){
				if(mActivity instanceof UpdateUserDetailsActivity){
					((UpdateUserDetailsActivity)mActivity).upgradeFriend_serviceCallBack();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onFailure(String message, String errorCode) {
		super.onFailure(message, errorCode);
//		if(mActivity instanceof UpdateUserDetailsActivity)
//		CustomDialogUtility.showMessageWithOk(message, mActivity);
	}
}
