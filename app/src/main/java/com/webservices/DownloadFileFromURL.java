package com.webservices;

import android.content.Context;
import android.os.AsyncTask;

import com.i_just_call_to_say.activities.contacts.DownloadUpdatePhoneContact;
import com.i_just_call_to_say.contactutility.PhoneContactsUtility;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.RawContactInfoDto;
import com.i_just_call_to_say.dto.User;
import com.utility.PreferenceUtility;
import com.utility.constants.UrlConstants;

import java.util.ArrayList;
import java.util.List;

public class DownloadFileFromURL extends AsyncTask<Void, Void, Void>{
	private List<Contacts> contactlist;
	private Context context;
	private DownloadUpdatePhoneContact downloadUpdatePhoneContact;
	private int current_attempt=0;
	private final int total_attempt = 3;
	private OnDownloadFinishListener downloadFinishListener;
	
	
	public  DownloadFileFromURL (List<Contacts> contactlist,Context context){
		this.contactlist = contactlist;
		this.context = context;
		downloadUpdatePhoneContact = new DownloadUpdatePhoneContact(context);
	}
	
	public OnDownloadFinishListener getDownloadFinishListener() {
		return downloadFinishListener;
	}

	public void setDownloadFinishListener(OnDownloadFinishListener downloadFinishListener) {
		this.downloadFinishListener = downloadFinishListener;
	}

	@Override
	protected Void doInBackground(Void... params) {
		for (Contacts contacts : contactlist) {
			if (contacts.getContact_ringtone().length()>0 || contacts.getContact_image().length()>0 || contacts.getContact_name().length()>0) {
				updateContact(contacts);
			}
		}
		return null;
	}
	
	private boolean updateContact(Contacts contacts){
		
		boolean ringtone_status=false,image_status=false,name_status=false;
		//new change
		ArrayList<RawContactInfoDto> raw_contact_info = PhoneContactsUtility.getRawContactInfo(context, contacts.getContact_id());
		try {
			contacts.setRaw_contact_id(raw_contact_info.get(0).raw_contact_id);
			contacts.setContact_type(raw_contact_info.get(0).raw_contact_id);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		current_attempt = 0;
		while (current_attempt < total_attempt && contacts.getContact_ringtone().length()>0) {
			if (ringtone_status=downloadUpdatePhoneContact.downloadRingTone(contacts)) {
				break;
			} else {
				current_attempt++;
			}
		}
		
		current_attempt = 0;
		while (current_attempt < total_attempt && contacts.getContact_image().length()>0) {
			if (image_status=downloadUpdatePhoneContact.downloadContactImage(contacts)) {
				break;
			} else {
				current_attempt++;
			}
		}
		
		current_attempt = 0;
		while (current_attempt < total_attempt && contacts.getContact_name().length()>0) {
			if (name_status = downloadUpdatePhoneContact.undateContactName(contacts)) {
				break;
			} else {
				current_attempt++;
			}
		}
		 
		 boolean isSendUpdate = true; 
		 
		 if(contacts.getContact_ringtone().length()>0 ){
			 if(!ringtone_status)
				 isSendUpdate = false;
		 }
		 
		 if(isSendUpdate && contacts.getContact_image().length()>0 ){
			 if(!image_status)
				 isSendUpdate = false;
		 }
		 
		 if(isSendUpdate && contacts.getContact_name().length()>0 ){
			 if(!name_status)
				 isSendUpdate = false;
		 }
		 
		 sendUpdateStatus(contacts , contacts.getContact_name().length() > 0 ? name_status :true , contacts.getContact_ringtone().length()>0 ? ringtone_status:true,
					 contacts.getContact_image().length() >0 ? image_status : true);
		 
		 return isSendUpdate;
	}
	
	
	@Override
	protected void onPostExecute(Void path) {
		System.out.println("All   download is completed and assigned successful ");
	}
	
	private final String DOWN_COMPLETED = "N";
	private final String DOWN_NOT_COMPLETED = "Y";
	
	
	private void sendUpdateStatus(Contacts contact , boolean isNameChanged , boolean isRingtoneChanged , boolean isProfileImageChange){
		System.out.println("sending update status");
		User user = (User) PreferenceUtility.getObjectInAppPreference(context, PreferenceUtility.USER_PROFILE);
		Object[] values={user.getAccess_token(),contact.getContact_number(),contact.getContact_image().length()>0?(contact.getContact_type().contains("sim")? "N":"N"):"N", user.getDeviceToken(),isNameChanged?DOWN_COMPLETED:DOWN_NOT_COMPLETED ,isRingtoneChanged?DOWN_COMPLETED:DOWN_NOT_COMPLETED,isProfileImageChange?DOWN_COMPLETED:DOWN_NOT_COMPLETED};
		RequestResponseService requestResponseService = new RequestResponseService(context, values);
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
			return UrlConstants.SEND_UPDATE_STATUS;
		}

		@Override
		public String[] getKeys() {
			String[] keys={"access_token","receiver_phone_no","is_sim_contact","device_token","isNameChanged","isRingtoneChanged","isProfileImageChange"};
			return keys;
		}

		@Override
		public Object[] getValues() {
			return values;
		}

		public void callService() {
			TaskManager taskManager = new TaskManager(this, this, context);
			taskManager.callServiceContext();
		}
		
		@Override
		public void onFailure(String message, String errorCode) {
			if(downloadFinishListener != null){
				downloadFinishListener.onDownloadFinish(false);
			}
			
		}
		
		@Override
		public void onSuccess(String response) {
			System.out.println("sending update response "+ response);
			if(downloadFinishListener != null){
				downloadFinishListener.onDownloadFinish(true);
			}
		}
	}
	
	public interface OnDownloadFinishListener{
		public void onDownloadFinish(boolean isSuccess);
	}
	
	
	
}
