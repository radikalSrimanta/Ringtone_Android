package com.i_just_call_to_say.activities.about;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.drm.DrmStore.RightsStatus;
import android.view.View;

import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.greetings.GetGreetinsOnCategory;
import com.i_just_call_to_say.activities.greetings.UpdateUserDetailsActivity;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.table_query_manager.UserTableManager;
import com.utility.CustomDialogUtility;
import com.utility.PreferenceUtility;
import com.utility.constants.UrlConstants;
import com.webservices.RequestHandler;
import com.webservices.TaskManager;

public class UpdateInAppPurchaseApiManager extends RequestHandler{
	private RingToneBaseActivity mActivity;
	private RingToneBaseApplication ringToneBaseApplication;
	//private User user;
	private String loadingMessage;
	private String mobileno;
	private Object[] values;
	
	public UpdateInAppPurchaseApiManager(RingToneBaseActivity activity,	RingToneBaseApplication ringToneBaseApplication, User user,	String loadingMessage, Object[] values) {
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
		return UrlConstants.UPDATE_PURCHASE;	
		}

	@Override
	public String[] getKeys() {
		String[] keys={"access_token","purchase_status","device_token"};
		return keys;
	}

	@Override
	public Object[] getValues() {
		return values;
	}

	@Override
	public void onSuccess(String response) {
		try {
			System.out.println("---> success "+response);
			JSONObject jsonObject=new JSONObject(response);
			JSONObject resultobject=jsonObject.getJSONObject("result");
			String purchase_status=resultobject.getString("purchase_status");
			if(purchase_status.equals("true")){
				//UserTableManager.updateUserStatus(mActivity.user.getAccess_token(), ringToneBaseApplication.databaseManager, mActivity, "true");
				//User.class
				RingToneBaseActivity.user = UserTableManager.getSavedUser(mActivity, ringToneBaseApplication);
				RingToneBaseActivity.user.setPurchase_status("true");
				PreferenceUtility.saveObjectInAppPreference(mActivity, RingToneBaseActivity.user, PreferenceUtility.USER_PROFILE);
				
				
				
				System.out.println("1  mActivity "+mActivity);
			   if(mActivity instanceof AboutRingToneActivity){
				   mActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						((AboutRingToneActivity)mActivity).btn_upgrade.setVisibility(View.GONE);
						((AboutRingToneActivity)mActivity).rl_footer.setVisibility(View.GONE);
					}
				});
				
			   }
			   if(mActivity instanceof UpdateUserDetailsActivity){
				   ((UpdateUserDetailsActivity)mActivity).update_serviceCallBack();
			   }
			   if(mActivity instanceof GetGreetinsOnCategory){
				   ((GetGreetinsOnCategory)mActivity).update_serviceCallBack();
			   }
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onFailure(String message, String errorCode) {
		super.onFailure(message, errorCode);
//		 if(mActivity instanceof UpdateUserDetailsActivity){
//			   CustomDialogUtility.showMessageWithOk(message, mActivity);
//		   }
	}
}
