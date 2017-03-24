package com.i_just_call_to_say.activities.contacts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncAdapterType;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.wrapper.ContactsDTOWrapper;
import com.utility.CommonUtility;
import com.utility.PreferenceUtility;
import com.utility.constants.UrlConstants;
import com.webservices.DownloadFileFromURL;
import com.webservices.DownloadFileFromURL.OnDownloadFinishListener;
import com.webservices.RequestHandler;
import com.webservices.TaskManager;

public class RingtoneDownloadService extends Service {

	private ArrayList<String> phone_number_list;
	private String contactNumber;


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		onHandleIntents(intent);
		System.out.println("call push page down");
		return START_STICKY;
	}

	protected void onHandleIntents(Intent intent) {
		Log.e("Service", "Start");

		try {
			contactNumber = (String) intent.getStringExtra("contact_number");
			phone_number_list = (ArrayList<String>) PreferenceUtility.getObjectInAppPreference(getApplicationContext(), PreferenceUtility.CONTACT_LIST);
			if (phone_number_list == null)
				phone_number_list = new ArrayList<String>();
			phone_number_list.add(contactNumber);
			if (contactNumber != null) {
				Log.e("Service", "Start2");
				connection_callService(contactNumber);
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void connection_callService(String number) {
		User user = (User) PreferenceUtility.getObjectInAppPreference(getApplicationContext(), PreferenceUtility.USER_PROFILE);
		Object[] values = {user.getAccess_token(), number, user.getDeviceToken()};
		RequestResponseService requestResponseService = new RequestResponseService(getApplicationContext(), values);


	}

	class RequestResponseService extends RequestHandler {
		private Object[] values;

		public RequestResponseService(Context activity, Object[] values) {
			super(null);
			this.values = values;
			callService();
		}

		@Override
		public String getWebServiceMethod() {
			return UrlConstants.GET_USER_DETAILS;
		}

		@Override
		public String[] getKeys() {
			String[] keys = {"access_token", "receiver_phone_no", "device_token"};
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
			Log.e("ServiceResponse", response);
			try {
				Gson gson = new Gson();
				ContactsDTOWrapper contactsDTOWrapper = gson.fromJson(response, ContactsDTOWrapper.class);
				if (contactsDTOWrapper.getContact_list() != null && contactsDTOWrapper.getContact_list().size() > 0) {
					startDownloadingServices(setContactId(contactsDTOWrapper.getContact_list()));
				}
				else
					System.out.println("No contact found in the service");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private List<Contacts> setContactId(List<Contacts> connectedList) {
		List<Contacts> contactList = (List<Contacts>) PreferenceUtility.getObjectInAppPreference(getApplicationContext(), PreferenceUtility.CONNECTED_CONTACT);
		Iterator<Contacts> savedContactIterator = contactList.iterator(); // receive from perference
		Contacts receiveFromService = connectedList.get(0);

		while (savedContactIterator.hasNext()) {
			Contacts newContacts = (Contacts) savedContactIterator.next();
			if (receiveFromService.getContact_number().equals(newContacts.getContact_number())) {
				receiveFromService.setContact_id(newContacts.getContact_id());
				break;
			}
		}
		connectedList.clear();
		connectedList.add(receiveFromService);
		return connectedList;
	}

	private void startDownloadingServices(final List<Contacts> connected_list) {
		try {
			System.out.println("start download");
			if (connected_list != null && connected_list.size() > 0) {
				Contacts contacts = connected_list.get(0);
				if (contacts.getContact_ringtone().length() > 0 || contacts.getContact_image().length() > 0 || contacts.getContact_name().length() > 0) {
					PreferenceUtility.saveObjectInAppPreference(getApplicationContext(), contacts, PreferenceUtility.CONTACT);
					DownloadFileFromURL downloadFileFromURL = new DownloadFileFromURL(connected_list, getApplicationContext());
					downloadFileFromURL.setDownloadFinishListener(new OnDownloadFinishListener() {

						@Override
						public void onDownloadFinish(boolean isSuccess) {
							Iterator<String> contactIterator = phone_number_list.iterator();
							while (contactIterator.hasNext()) {
								if (contactIterator.next().equals(contactNumber))
									contactIterator.remove();
							}
							PreferenceUtility.saveObjectInAppPreference(getApplicationContext(), phone_number_list, PreferenceUtility.CONTACT_LIST);
							requestSyncForAccounts();
							stopSelf();
						}
					});
					downloadFileFromURL.execute();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void requestSyncForAccounts() {
		SyncAdapterType[] syncAdapters = ContentResolver.getSyncAdapterTypes();
		Bundle extras = new Bundle();
		extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
		extras.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, true);


		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
		for (Account account : accounts) {
			for (int j = 0; j < syncAdapters.length; j++) {
				SyncAdapterType sa = syncAdapters[j];
	            if (ContentResolver.getSyncAutomatically(account, sa.authority)) {
	                ContentResolver.requestSync(account, sa.authority, extras);
	            }
	        }
	    }
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("Ringtone download service destroyed");
	}
	
}

		
