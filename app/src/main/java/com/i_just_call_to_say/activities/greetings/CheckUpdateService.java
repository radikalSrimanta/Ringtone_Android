package com.i_just_call_to_say.activities.greetings;

import org.json.JSONObject;

import com.i_just_call_to_say.dto.User;
import com.utility.CommonUtility;
import com.utility.PreferenceUtility;
import com.utility.constants.UrlConstants;
import com.webservices.RequestHandler;
import com.webservices.TaskManager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class CheckUpdateService  extends Service{

	private final IBinder mBinder = new LocalBinder();
	private String contactNumber;
	private OnServiceDisconnectListener disconnectListener;
	public Handler mHandler;
	private final int CHECK_INTERVAL = 500;

	@Override
	public void onCreate() {
		super.onCreate();
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (contactNumber.length()>0) {
					connection_callService(contactNumber);
				}
			}
		};
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public void onStartCommand(final String contactNumber) {
		System.out.println("*******update start*******");
		this.contactNumber = contactNumber;
		if(mHandler!=null)
			mHandler.sendEmptyMessageDelayed(0, CHECK_INTERVAL);
	}
	
	private void connection_callService(String number) {
		System.out.println("connection call service");
		User user = (User) PreferenceUtility.getObjectInAppPreference(getApplicationContext(), PreferenceUtility.USER_PROFILE);
		Object[] values={user.getAccess_token(), number, user.getDeviceToken()};
		RequestResponseService requestResponseService = new RequestResponseService(getApplicationContext(), values);
	}
	
	class RequestResponseService extends  RequestHandler{
		private Object[] values;
		public RequestResponseService(Context activity, Object[] values) {
			super(null);
			this.values = values;
			callService();
		}

		@Override
		public String getWebServiceMethod() {
			return UrlConstants.GET_RECEIVE_STATUS;
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

		public void callService() {
			TaskManager taskManager = new TaskManager(this, this, getApplicationContext());
			taskManager.callServiceContext();
		}
		
		@Override
		public void onFailure(String message, String errorCode) {
			if(mHandler!=null)
			mHandler.sendEmptyMessageDelayed(0, CHECK_INTERVAL);
		}
		
		@Override
		public void onSuccess(String response) {
			try {
				System.out.println("check update response "+ response);
				JSONObject object = new JSONObject(response);
				boolean update_status =  object.getBoolean("update_status");
				String message = object.getString("status");
				if(update_status){
					System.out.println("*******update success*******");
					update_status = true;
					disconnectListener.onServiceDisconnect(update_status,message);
					stopSelf();
				}else{
					System.out.println("*******update faluire*******");
					update_status =false;
					if (mHandler!=null) {
						mHandler.sendEmptyMessageDelayed(0, CHECK_INTERVAL);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class LocalBinder extends Binder {
		CheckUpdateService getService() {
			return CheckUpdateService.this;
		}
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		System.out.println("**********onUnbind called*******");
		mHandler = null;
		stopSelf();
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("**********ondestroy called*******");
	}
	
	public void setOnDisConnectListener(OnServiceDisconnectListener disconnectListener){
		this.disconnectListener = disconnectListener;
	}
	
	interface OnServiceDisconnectListener{
		public void onServiceDisconnect(boolean status, String message);
	}
}
