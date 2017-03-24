package com.i_just_call_to_say.activities.greetings;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.imageLoder.ImageLoader;
import com.j256.ormlite.table.TableUtils;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.contacts.ConnectionAdapter;
import com.i_just_call_to_say.activities.contacts.ContactsActivity;
import com.i_just_call_to_say.activities.contacts.NoContactAdapter;
import com.i_just_call_to_say.activities.contacts.ConnectionAdapter.OnNextButtonClickListener;
import com.i_just_call_to_say.activities.contacts.HomeApiManager;
import com.i_just_call_to_say.contactutility.PhoneContactsUtility;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.utility.AlertDialogCallBack;
import com.utility.CustomDialogUtility;
import com.utility.DateUtility;
import com.utility.orm_utility.Orm_SQLManager;
import com.utility.view.CustomEditText;
import com.utility.view.CustomTextView;

public class SendContactActivity extends RingToneBaseActivity {//com.i_just_call_to_say.activities.greetings.SendContactActivity
	
	private PullToRefreshListView list_contact;
	private ArrayList<Contacts> db_contact_list;
	ConnectionAdapter contactsAdapter;
	private HomeApiManager homeApiManager;
	private ArrayList<Contacts> phone_contacts_list;
	private String  cardCategoryId,card_id;
	private Contacts contacts;
	private SendGreetingsApiManager sendGreetingsApiManager ;
	private CustomEditText et_search;
	private int INITIAL_LOADING = 1, REFRESH_LOADING = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);//343434341233
		readFromBundle();
		initView();
		attachListner();
		setTabSelected(RingToneBaseActivity.SEND_CONTACT,"CONNECTIONS");
		saveContact(INITIAL_LOADING);
	}
	
	private void initView(){
		super.initBaseView();
		imageLoader = new ImageLoader(SendContactActivity.this);
		list_contact=(PullToRefreshListView) findViewById(R.id.list_contact);
		et_search = (CustomEditText) findViewById(R.id.et_search);
	}
	
	private void readFromBundle(){
		Bundle bundle=getIntent().getExtras();
		card_id=(String) bundle.getString("card_id");
		cardCategoryId=(String) bundle.getString("category_id");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void saveContact(int loadingType) {
		UpdateContact updateContact = new UpdateContact(loadingType);
		updateContact.execute();
//		phone_contacts_list = (ArrayList<Contacts>) ContactsTableManager.getSavedContactList(SendContactActivity.this,ringToneBaseApplication);
//		if(phone_contacts_list !=null && phone_contacts_list.size()>0 )
//			connection_callService();
	}
	
	
private class UpdateContact extends AsyncTask<Void, Void, Void>{
		
	private ProgressDialog progressDialog;
	private int loadingType;
	String message;

		public UpdateContact(int loadingType) {
			this.loadingType = loadingType;
			if (loadingType == INITIAL_LOADING) {
				message = "Please wait...      ";
			} else {
				message = "";
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				if (message.length() > 0) {
					progressDialog = new ProgressDialog(SendContactActivity.this,R.style.alertDialogTheme);
					progressDialog.setMessage(message);
					progressDialog.setCancelable(false);
					progressDialog.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		
		@Override
		protected Void doInBackground(Void... params) {
			phone_contacts_list=new ArrayList<Contacts>();
			PhoneContactsUtility.getListContacts(SendContactActivity.this, phone_contacts_list,user.getPhone_number());
			db_contact_list=(ArrayList<Contacts>) ContactsTableManager.getSavedContactList(SendContactActivity.this,ringToneBaseApplication);
            System.out.println("db_contact_list pre "+ db_contact_list.size());
			
//            Iterator<Contacts> phoneContactIterator = phone_contacts_list.iterator();
//			Iterator<Contacts> databaseContactIterator = db_contact_list.iterator();
//			
//			while(phoneContactIterator.hasNext()){
//				databaseContactIterator = db_contact_list.iterator();
//				Contacts phoneContact = phoneContactIterator.next();
//			
//					while (databaseContactIterator.hasNext()) {
//						Contacts databaseContact = databaseContactIterator.next();
//						if (phoneContact.getContact_number().equals(databaseContact.getContact_number())) {
//							phoneContact = databaseContact;
//							databaseContactIterator.remove();
//							break;
//						}
//					}
//			}
			
            for(int i =0 ;i<phone_contacts_list.size();i++){
				for(int j = 0; j<db_contact_list.size();j++){
					if(phone_contacts_list.get(i).getContact_number().equals(db_contact_list.get(j).getContact_number())){
						Contacts contacts = new Contacts();
						contacts =	phone_contacts_list.get(i);
						contacts.setConnection_id(db_contact_list.get(j).getConnection_id());
						contacts.setConnection_status(db_contact_list.get(j).getConnection_status());
						
						contacts.setMy_image_thumb(phone_contacts_list.get(j).getMy_image_thumb());
						contacts.setMy_image_normal(phone_contacts_list.get(j).getMy_image_normal());
						contacts.setContact_ringtone(phone_contacts_list.get(j).getContact_ringtone());
						
						contacts.setMy_name(db_contact_list.get(j).getMy_name());
						contacts.setMy_ringtone(db_contact_list.get(j).getMy_ringtone());
						contacts.setPurchase_status(db_contact_list.get(j).isPurchase_status());
						phone_contacts_list.remove(i);
						phone_contacts_list.add(i,contacts);
					}
				}
			}
            
			try {
				TableUtils.clearTable(ringToneBaseApplication.databaseManager.getHelper(getApplicationContext()).getConnectionSource(),Contacts.class);
				Orm_SQLManager.insertCollectionIntoTable(Contacts.class, phone_contacts_list, SendContactActivity.this, ringToneBaseApplication.databaseManager);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		  
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
//			super.onPostExecute(result);
			System.out.println("phone_contacts_list " + phone_contacts_list.size());
			if(progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
			
			if (phone_contacts_list.size() > 0) {
				connection_callService(loadingType);
			} else {
				service_callbackOnFaluire();
			}
		}
	}
	
	
	private void connection_callService(int loadingType) {
		String message;
		if(loadingType == INITIAL_LOADING){
			message = "Loading Connections...      ";
		}else{
			message ="";
		}
		Object[] values={user.getAccess_token(),getContactInArray().toString(),RingToneBaseApplication.device_token};
		homeApiManager=new HomeApiManager(this, ringToneBaseApplication, message, values,HomeApiManager.HOME_SERVICE_GET_CONNECTED_CONTACT);
	}
	
	private JSONArray getContactInArray() {
		try {
			JSONArray contactArray = new JSONArray();
			if (phone_contacts_list !=null && phone_contacts_list.size() > 0) {
				for (Contacts contacts : phone_contacts_list) {
					try {
						JSONObject phone_number = new JSONObject();
						phone_number.put("phone_number",contacts.getContact_number());
						contactArray.put(phone_number);
						return contactArray;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}


	private void attachListner(){
		list_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
					
					contacts=db_contact_list.get(position-1);
//					Bundle bundle=new Bundle();
//					bundle.putSerializable("contact", (Serializable) contacts);
//					ActivityController.startNextActivity(SendContactActivity.this, UpdateUserDetailsActivity.class, bundle, true);
					CustomDialogUtility.showCallbackMessageWithOkCancel("Are you sure you want to send the card ?", SendContactActivity.this, new AlertDialogCallBack() {
						
						@Override
						public void onSubmitWithEditText(String text) {
							
						}
						
						@Override
						public void onSubmit() {
							sendgreeting_callService();
						}
						
						@Override
						public void onCancel() {
							
						}
					});
				
			}
		});
		
		et_search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(db_contact_list !=null && db_contact_list.size()>0 && contactsAdapter != null){
					contactsAdapter.setContactlist(db_contact_list);
					contactsAdapter.getCustomFilter().filter(s.toString().trim());
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		list_contact.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				connection_callService(REFRESH_LOADING);
			}
		});
		
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();				
			}
		});
	}
	
	public void getContactList(){
		db_contact_list.clear();
		db_contact_list=(ArrayList<Contacts>) ContactsTableManager.getSavedContactList(SendContactActivity.this, ringToneBaseApplication);
		contactsAdapter=new ConnectionAdapter(SendContactActivity.this, db_contact_list,user, imageLoader);
		list_contact.setAdapter(contactsAdapter);
	}

	
	public void getAllContactList(List<Contacts> listcontacts) {
		list_contact.onRefreshComplete();
		if(listcontacts !=null && listcontacts.size()>0 ){
			db_contact_list=(ArrayList<Contacts>) listcontacts;
			publish_listView(db_contact_list);	
		}else{
			setNoContactAdapter("No connection found");
		}	
	}
	
	public void publish_listView(List<Contacts> listcontacts) {
		contactsAdapter=new ConnectionAdapter(SendContactActivity.this, listcontacts,user, imageLoader);
		list_contact.setAdapter(contactsAdapter);
		contactsAdapter.setOnNextButtonClickListener(new OnNextButtonClickListener() {
			
			@Override
			public void onNextButtonClick(Contacts contacts) {
				
			}
		});
	}
	
	public void service_callbackOnFaluire(){
		list_contact.onRefreshComplete();
		db_contact_list = (ArrayList<Contacts>) ContactsTableManager.getConnectedContactList(ringToneBaseApplication.databaseManager, this, "C");
		
		if (db_contact_list.size()==0) {
			setNoContactAdapter("No connection found");
		}else{
			db_contact_list.clear();
			publish_listView(db_contact_list);
		}
	}
	
	private void setNoContactAdapter(String message){
		List<String> noContactList = new ArrayList<String>();
		noContactList.add(message);
		NoContactAdapter noContactAdapter = new NoContactAdapter(this, noContactList);
		if (noContactAdapter!=null) {
			list_contact.setAdapter(noContactAdapter);
		}
	}
	
	private void sendgreeting_callService(){
		System.out.println("connection id "+contacts.getConnection_id());
		Object[] values = {user.getAccess_token(), cardCategoryId, card_id, contacts.getConnection_id(),DateUtility.getCurrentDateTimeInGMT(),RingToneBaseApplication.device_token};
		sendGreetingsApiManager = new SendGreetingsApiManager(SendContactActivity.this, ringToneBaseApplication,"Sending card...      ", values);
	}
	
	public void sendgreeting_serviceCallBack(String message){
		CustomDialogUtility.showCallbackMessageWithOk(message, this, new AlertDialogCallBack() {
				
				@Override
				public void onSubmitWithEditText(String text) {
					
				}
				
				@Override
				public void onSubmit() {
					finish();
				}
				
				@Override
				public void onCancel() {
					
				}
			});
	}
	
}
