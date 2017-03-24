package com.i_just_call_to_say.activities.contacts;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.connections.ConnectionsActivity;
import com.i_just_call_to_say.activities.greetings.SendContactActivity;
import com.i_just_call_to_say.dto.ContactStatusDto;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.i_just_call_to_say.dto.wrapper.ContactStatusWrapper;
import com.i_just_call_to_say.dto.wrapper.ContactsDTOWrapper;
import com.utility.PreferenceUtility;
import com.utility.constants.UrlConstants;
import com.webservices.RequestHandler;
import com.webservices.TaskManager;


public class HomeApiManager extends RequestHandler {

	private Activity mActivity;
	private RingToneBaseApplication ringToneBaseApplication;

	private String loadingMessage;

	private List<Contacts> contactlst;
	private Object[] values;
	private int status;
	public static final int HOME_SERVICE_GET_ALL_CONTACT = 1;
	public static final int HOME_SERVICE_GET_CONNECTED_CONTACT = 0;
	private ImageView imageView;
	

	public interface updateContacts {
		public void updateContact();
	}

	public HomeApiManager(Activity activity,RingToneBaseApplication ringToneBaseApplication,String loadingMessage,Object[] values,int status) {
		super(activity);
		mActivity = activity;
		this.ringToneBaseApplication=ringToneBaseApplication;
		this.values=values;
		this.loadingMessage = loadingMessage;
		this.status=status;
		imageView = new ImageView(mActivity);
		callService();
	}

	private void callService(){
		TaskManager taskManager = new TaskManager(this, this, mActivity);
		taskManager.callService(loadingMessage);
	}

	@Override
	public String getWebServiceMethod() {
		switch (status) {
		case HOME_SERVICE_GET_ALL_CONTACT:
			return UrlConstants.CONNECTION_LIST;
		case HOME_SERVICE_GET_CONNECTED_CONTACT:
			return UrlConstants.HOME;
		}
		return null;
		
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

	@Override
	public void onSuccess(String response) {
		Gson gson = new Gson();
		if(contactlst!=null && contactlst.size()>0)
		contactlst.clear();
		switch (status) {
		case HOME_SERVICE_GET_CONNECTED_CONTACT:
			ArrayList<Contacts> updateContactList = new ArrayList<Contacts>();
			try {
				ContactsDTOWrapper contactsDTOWrapper =gson.fromJson(response, ContactsDTOWrapper.class);
				for (Contacts contacts : contactsDTOWrapper.getContact_list()) {
					System.out.println("my name "+ contacts.getMy_name());
					if (contacts.getContact_number() !=null) {
						ContactsTableManager.updateStatusOfContacts(contacts, ringToneBaseApplication.databaseManager,mActivity);
						if(contacts.isSend_status()){
							updateContactList.add(contacts);
						}
					}
					
				}
			
				contactlst=ContactsTableManager.getConnectedContactList(ringToneBaseApplication.databaseManager, mActivity, "C");
				PreferenceUtility.saveObjectInAppPreference(mActivity, contactlst, PreferenceUtility.CONNECTED_CONTACT);

				if (mActivity instanceof ContactsActivity) {
					System.out.println("update contact 1");
					((ContactsActivity)mActivity).getAllContactList(contactlst);
				}else if(mActivity instanceof SendContactActivity){
					System.out.println("update contact 2");
					((SendContactActivity)mActivity).getAllContactList(contactlst);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			break;
		case HOME_SERVICE_GET_ALL_CONTACT:
			ContactStatusWrapper contactStatusWrapper = gson.fromJson(response, ContactStatusWrapper.class);
			for (ContactStatusDto contactStatusDto : contactStatusWrapper.getContact_list()) {
				ContactsTableManager.updateContactsStatus(contactStatusDto, ringToneBaseApplication.databaseManager,mActivity);
				if(!contactStatusDto.getMy_image().equals(""))
				ContactsTableManager.updateContactsImage(contactStatusDto.getContact_number(), contactStatusDto.getMy_image(),  ringToneBaseApplication.databaseManager, mActivity);
//				if(count==0){
//					Orm_SQLManager.insertIntoTable(Contacts.class, contacts, mActivity, ringToneBaseApplication.databaseManager);
//				}
			}
			contactlst=ContactsTableManager.getSavedContactList(mActivity, ringToneBaseApplication);
			((ConnectionsActivity)mActivity).getAllContactList(contactlst);
			break;
		default:
			break;
		}		
	}
	
	private void startUpdateService(ArrayList<Contacts> updateContactList) {
		Intent updateIntent = new Intent(mActivity,RingtoneDownloadService.class);
		updateIntent.putExtra("update_list", updateContactList);
		updateIntent.putExtra("from", "home");
		mActivity.startService(updateIntent);
	}

	@Override
	public void onFailure(String message, String errorCode) {
		if (mActivity instanceof ConnectionsActivity){
			((ConnectionsActivity)mActivity).service_callbackOnFaluire();
		}
		if (mActivity instanceof SendContactActivity){
			((SendContactActivity)mActivity).service_callbackOnFaluire();
		}
		
		if(mActivity instanceof ContactsActivity)
			((ContactsActivity)mActivity).service_callbackOnFaluire();
	
		if(!errorCode.equalsIgnoreCase("430"))
		super.onFailure(message, errorCode);
		
	}
}
