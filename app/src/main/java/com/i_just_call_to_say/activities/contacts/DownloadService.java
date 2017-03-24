package com.i_just_call_to_say.activities.contacts;

import java.util.List;

import org.json.JSONArray;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.google.gson.Gson;
import com.i_just_call_to_say.contactutility.PhoneContactsUtility;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.wrapper.ContactsDTOWrapper;
import com.utility.CommonUtility;
import com.utility.PreferenceUtility;
import com.utility.constants.UrlConstants;
import com.webservices.DownloadFileFromURL;
import com.webservices.RequestHandler;
import com.webservices.TaskManager;

public class DownloadService extends Service {
	private JSONArray phone_contacts_list;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new ModifyUpdateService().execute();
		return START_STICKY;
	}
	
	private void connection_callService() {
		User user = (User) PreferenceUtility.getObjectInAppPreference(getApplicationContext(), PreferenceUtility.USER_PROFILE);
		Object[] values={user.getAccess_token(), phone_contacts_list.toString(), user.getDeviceToken()};
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
			return UrlConstants.GET_UPDATED_USER_DETAILS;
		}

		@Override
		public String[] getKeys() {
			String[] keys={"access_token","contacts","device_token"};
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
			
		}
		
		@Override
		public void onSuccess(String response) {
			try {
				Gson gson = new Gson();
				ContactsDTOWrapper contactsDTOWrapper =gson.fromJson(response, ContactsDTOWrapper.class);
				PreferenceUtility.saveObjectInAppPreference(getApplicationContext(), contactsDTOWrapper.getContact_list(), PreferenceUtility.CONNECTED_CONTACT);
				startDownloadingServices(contactsDTOWrapper.getContact_list());
			} catch (Exception e) {
				System.out.println("exception "+e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	private void startDownloadingServices(final List<Contacts> connected_list) {
		try {
			if(connected_list != null && connected_list.size() > 0){
				DownloadFileFromURL downloadFileFromURL = new DownloadFileFromURL(connected_list, getApplicationContext());
				downloadFileFromURL.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class ModifyUpdateService extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			phone_contacts_list = new JSONArray();
			User user = (User) PreferenceUtility.getObjectInAppPreference(getApplicationContext(), PreferenceUtility.USER_PROFILE);
			PhoneContactsUtility.getListContacts(getApplicationContext(), phone_contacts_list,user.getPhone_number());
			if (phone_contacts_list!=null && phone_contacts_list.length() > 0) {
				connection_callService();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}
}

		
