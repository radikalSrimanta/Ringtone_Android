package com.i_just_call_to_say.activities.connections;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.contacts.HomeApiManager;
import com.i_just_call_to_say.activities.contacts.NoContactAdapter;
import com.i_just_call_to_say.activities.splash.SplashActivity;
import com.i_just_call_to_say.contactutility.PhoneContactsUtility;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.imageLoder.ImageLoader;
import com.j256.ormlite.table.TableUtils;
import com.utility.orm_utility.Orm_SQLManager;
import com.utility.view.CustomEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionsActivity extends RingToneBaseActivity {
	
	public PullToRefreshListView list_contact;
	private String card_id,category_id;
	private List<Contacts> db_contact_list;
	private boolean flag=false;
	private HomeApiManager homeApiManager;
	private ContactsAdapter contactsAdapter;
	private CustomEditText et_search;
	private ArrayList<Contacts> phone_contacts_list;
	private int INITIAL_LOADING = 1, REFRESH_LOADING = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection_list);
		initView();
		getIntentdata();
		attachListner();
		setTabSelected(RingToneBaseActivity.CONTACT,"CONTACTS");
		saveContact(INITIAL_LOADING);

	}
	
	private void saveContact(int loadingType) {
//		db_contact_list =(ArrayList<Contacts>) ContactsTableManager.getSavedContactList(this,ringToneBaseApplication);
//		if (db_contact_list!=null && db_contact_list.size()>0) {
//			publish_listView(db_contact_list);
//		}
		UpdateContact updateContact = new UpdateContact(loadingType);
		updateContact.execute();
	}

	private void connection_callService(int loadingType) {
		String message;
		if(loadingType == INITIAL_LOADING){
			message = "Loading Contacts...       ";
		}else{
			message ="";
		}
		//Object[] values={user.getAccess_token(),getContactInArray().toString(), SplashActivity.d_token};
		Object[] values={user.getAccess_token(),getContactInArray().toString(),RingToneBaseApplication.device_token};
		homeApiManager=new HomeApiManager(this, ringToneBaseApplication, message, values,HomeApiManager.HOME_SERVICE_GET_ALL_CONTACT);
	}
	
	private JSONArray getContactInArray() {
		JSONArray contactArray = new JSONArray();
		if (phone_contacts_list != null && phone_contacts_list.size() > 0) {
			for (Contacts contacts : phone_contacts_list) {
				try {
					JSONObject phone_number = new JSONObject();
					phone_number.put("phone_number", contacts.getContact_number());
					contactArray.put(phone_number);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return contactArray;
		}
		return null;
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	private void getIntentdata(){
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			flag=true;
			card_id=bundle.getString("card_id");
			category_id=bundle.getString("category_id");
		}
	}

	private void attachListner(){
		
		et_search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(db_contact_list !=null && db_contact_list.size()>0 && contactsAdapter != null){
					contactsAdapter.setContactlist(db_contact_list);
					contactsAdapter.getCustomFilter().filter(s.toString().trim());
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();			
			}
		});
		
		list_contact.setOnRefreshListener(new OnRefreshListener<ListView>() {
	
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				saveContact(REFRESH_LOADING);
//				connection_callService();
			}
		});
	 }
	
	private void initView(){
		super.initBaseView();
		imageLoader = new ImageLoader(ConnectionsActivity.this);
		list_contact = (PullToRefreshListView) findViewById(R.id.list_contact);
		et_search = (CustomEditText) findViewById(R.id.et_search);
	}

	public void onButtonClick(View v) {
		switch (v.getId()) {
		case R.id.root_layout:
			hideKeyboard();
			break;

		default:
			break;
		}
	}
	
	
	public void getAllContactList(List<Contacts> listcontacts) {
		list_contact.onRefreshComplete();
		if(listcontacts !=null && listcontacts.size()>0){
			db_contact_list.clear();
			db_contact_list = listcontacts;		
			publish_listView(db_contact_list);
		}else{
			setNoContactAdapter("No contact found");
		}
	}
	
	public void service_callbackOnFaluire(){
			list_contact.onRefreshComplete();
			setNoContactAdapter("No contact found");
	}
	
	
	private void setNoContactAdapter(String message){
		List<String> noContactList = new ArrayList<String>();
		noContactList.add(message);
		NoContactAdapter noContactAdapter = new NoContactAdapter(this, noContactList);
		if (noContactAdapter!=null) {
			list_contact.setAdapter(noContactAdapter);
		}
	}
	
	public void publish_listView(List<Contacts> listcontacts) {
		contactsAdapter=new ContactsAdapter(ConnectionsActivity.this, listcontacts,user,imageLoader);
		list_contact.setAdapter(contactsAdapter);
	}
	@Override
	public void onBackPressed() {
		finish();
	}
	
	private class UpdateContact extends AsyncTask<Void, Void, Void>{
		
		private ProgressDialog progressDialog;
		private int loadingType;
		String message;

		public UpdateContact(int loadingType) {
			this.loadingType = loadingType;
			if(loadingType == INITIAL_LOADING){
				message = "Please wait...      ";
			}else{
				message = "";
			}
			
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			 try {
				 if(message.length()>0){
				 progressDialog = new ProgressDialog(ConnectionsActivity.this,R.style.alertDialogTheme);
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
			PhoneContactsUtility.getListContacts(ConnectionsActivity.this, phone_contacts_list,user.getPhone_number());
			db_contact_list=(ArrayList<Contacts>) ContactsTableManager.getSavedContactList(ConnectionsActivity.this,ringToneBaseApplication);
            System.out.println("db_contact_list pre "+ db_contact_list.size());
			
//          Iterator<Contacts> phoneContactIterator = phone_contacts_list.iterator();
//			Iterator<Contacts> databaseContactIterator = db_contact_list.iterator();
			
//			while(phoneContactIterator.hasNext()){
//				
//				databaseContactIterator = db_contact_list.iterator();
//				Contacts phoneContact = phoneContactIterator.next();
//			
//					while (databaseContactIterator.hasNext()) {
//						Contacts databaseContact = databaseContactIterator.next();
//						if (phoneContact.getContact_number().equals(databaseContact.getContact_number())) {
//							phoneContact.setConnection_id(databaseContact.getConnection_id());
//							phoneContact.setConnection_status(databaseContact.getConnection_status());
//							phoneContact.setMy_image(databaseContact.getMy_image());
//							phoneContact.setMy_name(databaseContact.getMy_name());
//							phoneContact.setMy_ringtone(databaseContact.getMy_ringtone());
////							phoneContact = databaseContact;
//							databaseContactIterator.remove();
//							break;
//						}
//				}
//			}
			
			for(int i =0 ;i<phone_contacts_list.size();i++){
				for(int j = 0; j<db_contact_list.size();j++){
					if(phone_contacts_list.get(i).getContact_number().contains(db_contact_list.get(j).getContact_number())){
						Contacts contacts = new Contacts();
						contacts =	phone_contacts_list.get(i);
						contacts.setConnection_id(db_contact_list.get(j).getConnection_id());
						contacts.setConnection_status(db_contact_list.get(j).getConnection_status());
						
						
						contacts.setMy_image_thumb(phone_contacts_list.get(i).getMy_image_thumb());
						contacts.setMy_image_normal(phone_contacts_list.get(i).getMy_image_normal());
						contacts.setContact_ringtone(phone_contacts_list.get(i).getContact_ringtone());
						
						contacts.setMy_name(db_contact_list.get(j).getMy_name());
						contacts.setMy_ringtone(db_contact_list.get(j).getMy_ringtone());
						contacts.setMy_ringtone(db_contact_list.get(j).getMy_ringtone());
						contacts.setPurchase_status(db_contact_list.get(j).isPurchase_status());
						phone_contacts_list.remove(i);
						phone_contacts_list.add(i,contacts);
					}
				}
			}
			
			try {
				TableUtils.clearTable(ringToneBaseApplication.databaseManager.getHelper(getApplicationContext()).getConnectionSource(),Contacts.class);
				Orm_SQLManager.insertCollectionIntoTable(Contacts.class, phone_contacts_list, ConnectionsActivity.this, ringToneBaseApplication.databaseManager);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
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
	

}
