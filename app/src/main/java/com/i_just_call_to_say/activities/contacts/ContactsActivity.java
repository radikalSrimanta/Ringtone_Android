package com.i_just_call_to_say.activities.contacts;

import java.io.Serializable;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.i_just_call_to_say.activities.splash.SplashActivity;
import com.imageLoder.ImageLoader;
import com.j256.ormlite.table.TableUtils;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.MyMessageBroadcastReceiver;
import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.connections.AddConnectionApiManager;
import com.i_just_call_to_say.activities.connections.AddConnectionApiManager.updateContacts;
import com.i_just_call_to_say.activities.connections.ConnectionsActivity;
import com.i_just_call_to_say.activities.connections.IUpdateContacts;
import com.i_just_call_to_say.activities.contacts.ConnectionAdapter.OnNextButtonClickListener;
import com.i_just_call_to_say.activities.greetings.UpdateUserDetailsActivity;
import com.i_just_call_to_say.contactutility.PhoneContactsUtility;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.ReceivedGreetingsCard;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.utility.ActivityController;
import com.utility.AlertDialogCallBack;
import com.utility.CustomDialogUtility;
import com.utility.orm_utility.Orm_SQLManager;
import com.utility.view.CustomButton;
import com.utility.view.CustomEditText;
import com.utility.view.CustomTextView;

public class ContactsActivity extends RingToneBaseActivity {
	
	public PullToRefreshListView list_contact;
	private ArrayList<Contacts> db_contact_list;
	private ConnectionAdapter contactsAdapter;
	private HomeApiManager homeApiManager;
	private ArrayList<Contacts> phone_contacts_list;
	private CustomEditText et_search;
	private final int CONNECTION_LIST_REQUEST_CODE=100;
	private final int VIEW_PROFILE_REQUEST_CODE=101;
	private int INITIAL_LOADING = 1, REFRESH_LOADING = 2;
	private int PERMISSION_REQUEST_Camera=113,PERMISSION_REQUEST_WRITEEXTERNALSTORAGE=114,PERMISSION_REQUEST_READEXTERNALSTORAGE=114,PERMISSION_REQUEST_SMSACCESS=118;
    private int READCONTACT=120,WRITECONTACT=121;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);//343434341233
		initView();
		readFromBundle();
		attachListner();
		setTabSelected(RingToneBaseActivity.CONNECTIONS,"CONNECTIONS");
//		checkpermession();
	}

	private void checkpermession(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

				// Should we show an explanation?
				if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("camera access needed");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setMessage("please confirm camera");//TODO put real question
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@TargetApi(Build.VERSION_CODES.M)
						@Override
						public void onDismiss(DialogInterface dialog) {
							requestPermissions(new String[]
									{Manifest.permission.CAMERA}, PERMISSION_REQUEST_Camera);
						}
					});
					builder.show();
					// Show an expanation to the user *asynchronously* -- don't block
					// this thread waiting for the user's response! After the user
					// sees the explanation, try again to request the permission.

				} else {

					// No explanation needed, we can request the permission.

					ActivityCompat.requestPermissions(this,
							new String[]{Manifest.permission.CAMERA},
							PERMISSION_REQUEST_Camera);

					// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
					// app-defined int constant. The callback method gets the
					// result of the request.
				}
			}
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
				if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Storge access needed");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setMessage("please confirm Storage access");//TODO put real question
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@TargetApi(Build.VERSION_CODES.M)
						@Override
						public void onDismiss(DialogInterface dialog) {requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITEEXTERNALSTORAGE);
						}
					});
					builder.show();
					// Show an expanation to the user *asynchronously* -- don't block
					// this thread waiting for the user's response! After the user
					// sees the explanation, try again to request the permission.

				} else {

					// No explanation needed, we can request the permission.

					ActivityCompat.requestPermissions(this,
							new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
							PERMISSION_REQUEST_WRITEEXTERNALSTORAGE);

					// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
					// app-defined int constant. The callback method gets the
					// result of the request.
				}
			}
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
				if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Stroage access needed");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setMessage("please confirm Storage");//TODO put real question
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@TargetApi(Build.VERSION_CODES.M)
						@Override
						public void onDismiss(DialogInterface dialog) {requestPermissions(new String[]
								{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_READEXTERNALSTORAGE);
						}
					});
					builder.show();
					// Show an expanation to the user *asynchronously* -- don't block
					// this thread waiting for the user's response! After the user
					// sees the explanation, try again to request the permission.

				} else {

					// No explanation needed, we can request the permission.

					ActivityCompat.requestPermissions(this,
							new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
							PERMISSION_REQUEST_READEXTERNALSTORAGE);

					// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
					// app-defined int constant. The callback method gets the
					// result of the request.
				}
			}
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
				if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("sms access needed");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setMessage("please confirm sms access");//TODO put real question
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@TargetApi(Build.VERSION_CODES.M)
						@Override
						public void onDismiss(DialogInterface dialog) {
							requestPermissions(new String[]
									{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SMSACCESS);
						}
					});
					builder.show();
					// Show an expanation to the user *asynchronously* -- don't block
					// this thread waiting for the user's response! After the user
					// sees the explanation, try again to request the permission.

				} else {

					// No explanation needed, we can request the permission.

					ActivityCompat.requestPermissions(this,
							new String[]{Manifest.permission.SEND_SMS},
							PERMISSION_REQUEST_SMSACCESS);

					// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
					// app-defined int constant. The callback method gets the
					// result of the request.
				}
			}
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
				if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Contact access needed");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setMessage("please confirm Contact access");//TODO put real question
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@TargetApi(Build.VERSION_CODES.M)
						@Override
						public void onDismiss(DialogInterface dialog) {
							requestPermissions(new String[]
									{Manifest.permission.READ_CONTACTS}, READCONTACT);
						}
					});
					builder.show();
					// Show an expanation to the user *asynchronously* -- don't block
					// this thread waiting for the user's response! After the user
					// sees the explanation, try again to request the permission.

				} else {

					// No explanation needed, we can request the permission.

					ActivityCompat.requestPermissions(this,
							new String[]{Manifest.permission.READ_CONTACTS},
							READCONTACT);

					// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
					// app-defined int constant. The callback method gets the
					// result of the request.
				}
			}
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED){
				if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Contact access needed");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setMessage("please confirm Contact access");//TODO put real question
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@TargetApi(Build.VERSION_CODES.M)
						@Override
						public void onDismiss(DialogInterface dialog) {
							requestPermissions(new String[]
									{Manifest.permission.WRITE_CONTACTS}, WRITECONTACT);
						}
					});
					builder.show();
					// Show an expanation to the user *asynchronously* -- don't block
					// this thread waiting for the user's response! After the user
					// sees the explanation, try again to request the permission.

				} else {

					// No explanation needed, we can request the permission.

					ActivityCompat.requestPermissions(this,
							new String[]{Manifest.permission.WRITE_CONTACTS},
							WRITECONTACT);

					// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
					// app-defined int constant. The callback method gets the
					// result of the request.
				}
			}
		}
	}

	private void initView(){
		super.initBaseView();
		imageLoader = new ImageLoader(ContactsActivity.this);
		list_contact=(PullToRefreshListView) findViewById(R.id.list_contact);
		et_search = (CustomEditText) findViewById(R.id.et_search);
	}
	
	public void onButtonClick(View v) {
		switch (v.getId()) {
		case R.id.root_layout:
		case R.id.parent:
			System.out.println("hide keyboard");
			hideKeyboard();
			break;

		default:
			break;
		}
	}
	
	private void readFromBundle(){
		try{
			Bundle bundle = getIntent().getExtras();
			if(bundle != null){
				String notification_type = bundle.getString(MyMessageBroadcastReceiver.NOTIFICATION_TYPE); 
				//System.out.println("notification_type "+notification_type);
				if(notification_type.trim().equalsIgnoreCase("ringtone")){
					//System.out.println("***  contacts ***");
					ReceivedGreetingsCard receiveCard = (ReceivedGreetingsCard) bundle.getSerializable("receive_greeting");
					//System.out.println("contacts "+contacts);
					List<Contacts> contacts = ContactsTableManager.getConnectedContact(ringToneBaseApplication.databaseManager, ContactsActivity.this, receiveCard.getSender_phone_no());
					String sender_name = receiveCard.getSender_phone_no();
					if(contacts.size()>0 && contacts!=null){
						sender_name = contacts.get(0).getContact_name();
					}
					
					CustomDialogUtility.showCallbackMessageWithOk("You Received a ringtone from "+sender_name, ContactsActivity.this, new AlertDialogCallBack() {
						
						@Override
						public void onSubmitWithEditText(String text) {	}
						
						@Override
						public void onSubmit() {
//							saveContact();
						}
						@Override
						public void onCancel() {}
					});
					
					return;
				}if(notification_type.trim().equalsIgnoreCase("updateapp")){
					Contacts contacts = (Contacts) bundle.getSerializable("contacts");
					System.out.println("contacts "+contacts.getContact_name());
					CustomDialogUtility.showCallbackMessageWithOk("Your app has been upgraded successfully by "+contacts.getContact_name(), ContactsActivity.this, new AlertDialogCallBack() {
						@Override
						public void onSubmitWithEditText(String text) {	}
						
						@Override
						public void onSubmit() {
//							saveContact();
						}
						
						@Override
						public void onCancel() {}
					});
					
				}
					
				String url = bundle.getString("url");
				
				System.out.println("url "+url +"    notification_type: "+notification_type);
				
				if(url != null && url.indexOf("connection_accept/") > 0 && notification_type.equalsIgnoreCase("invitation")){
					
				  String body = url.substring(url.indexOf("connection_accept/")+"connection_accept/".length()); 
				  String bodyArr [] = body.split("/");
				  if(bodyArr.length>2)
					  sendContactRequest(bodyArr[0], bodyArr[2], bodyArr[1]);
				}else
					saveContact(INITIAL_LOADING);
			}else
				saveContact(INITIAL_LOADING);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private void add_connection_service(final String connection_type,final Contacts contacts){
		Object[] values={user.getAccess_token(),connection_type,contacts.getContact_number(),ringToneBaseApplication.device_token};
		RingToneBaseApplication ringToneBaseApplication=(RingToneBaseApplication)(this).getApplication();
		AddConnectionApiManager addConnectionApiManager=new AddConnectionApiManager(this, contacts, ringToneBaseApplication, "Connecting...         ",values,new IUpdateContacts() {

			@Override
			public void updateContacts(String message , String sms_msg) {
				CustomDialogUtility.showCallbackMessageWithOk(message, ContactsActivity.this,new AlertDialogCallBack() {
					
					@Override
					public void onSubmitWithEditText(String text) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onSubmit() {
						saveContact(INITIAL_LOADING);
						
					}
					
					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						
					}
				});
				
			}

			@Override
			public void onFailuepdateContacts(String message) {
				saveContact(INITIAL_LOADING);
			}
		});
	}
	
	
	private void sendContactRequest(String senderName , String connectionId , String phoneNumber) {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog_layout);
		dialog.setCancelable(false);
		
		CustomTextView et_message = (CustomTextView) dialog.findViewById(R.id.et_message);
		CustomButton btn_accept = (CustomButton) dialog.findViewById(R.id.btn_accept);
		CustomButton btn_cancel = (CustomButton) dialog.findViewById(R.id.btn_cancel);
		btn_accept.setVisibility(View.VISIBLE);
		btn_cancel.setVisibility(View.VISIBLE);
		System.out.println("senderName  "+senderName);
		et_message.setText(URLDecoder.decode(senderName)+" wants to connect with you via 'I Just Called To Say' app. Do you want to accept ?");
		btn_accept.setText("Yes");
		btn_cancel.setText("No");
		
		final Contacts contacts = new Contacts();
		contacts.setConnection_id(connectionId);
		contacts.setContact_number(URLDecoder.decode(phoneNumber));//phoneNumber.replaceAll("%2B", "+"));
		contacts.setContact_name(senderName);
			
		btn_accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				add_connection_service("ACCEPT", contacts);
				dialog.dismiss();
			}
		});	
		
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				add_connection_service("DENY",contacts);
				dialog.dismiss();
			}
		});

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog.show();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void saveContact(int loadingType) {
		UpdateContact updateContact = new UpdateContact(loadingType);
		updateContact.execute();
	}
	
	private void connection_callService(int loadingType) {
		String message;
		if(loadingType == INITIAL_LOADING){
			message = "Loading Connections...      ";
		}else{
			message ="";
		}
		//Object[] values={user.getAccess_token(),getContactInArray().toString(),RingToneBaseApplication.device_token};
		Object[] values={user.getAccess_token(),getContactInArray().toString(),RingToneBaseApplication.device_token};
		System.out.println("aaa" + user.getAccess_token()+","+getContactInArray().toString()+","+ RingToneBaseApplication.device_token);
		homeApiManager=new HomeApiManager(this, ringToneBaseApplication,message , values,HomeApiManager.HOME_SERVICE_GET_CONNECTED_CONTACT);
	}

	private JSONArray getContactInArray() {
		JSONArray contactArray = new JSONArray();
		if (phone_contacts_list != null && phone_contacts_list.size() > 0) {
			for (Contacts contacts : phone_contacts_list) {
				try {
					JSONObject phone_number = new JSONObject();
					phone_number.put("phone_number",contacts.getContact_number());
					contactArray.put(phone_number);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return contactArray;
		}
		return null;
	}

	
	private void attachListner(){
		list_contact.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
					hideKeyboard();
					if(db_contact_list!=null && db_contact_list.size()>0){
					Contacts contacts= db_contact_list.get(position-1);
					Bundle bundle=new Bundle();
					bundle.putSerializable("contact", (Serializable) contacts);
//					ActivityController.startNextActivity(ContactsActivity.this, UpdateUserDetailsActivity.class, bundle, false);
					ActivityController.startNextActivityForResult(ContactsActivity.this, UpdateUserDetailsActivity.class, bundle, false, VIEW_PROFILE_REQUEST_CODE);
					
					}
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
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	  
		btn_header_people.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityController.startNextActivityForResult(ContactsActivity.this, ConnectionsActivity.class,null,false,CONNECTION_LIST_REQUEST_CODE);
			}
		});
		
		list_contact.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//				connection_callService();
				saveContact(REFRESH_LOADING);
			}
		});
		
	}
	
	public void getContactList(){
		db_contact_list.clear();
		db_contact_list=(ArrayList<Contacts>) ContactsTableManager.getSavedContactList(ContactsActivity.this, ringToneBaseApplication);
		contactsAdapter=new ConnectionAdapter(ContactsActivity.this, db_contact_list,user,imageLoader);
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
	
	private void startRingtoneDownload(){
		if (!isMyServiceRunning(getApplicationContext())) {
			Intent intent = new Intent(getApplicationContext(),RingtoneDownloadService.class);
			intent.putExtra("contact_list", this.db_contact_list);
			startService(intent);
		}
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
	
	
	public void publish_listView(List<Contacts> listcontacts) {
		contactsAdapter=new ConnectionAdapter(ContactsActivity.this, listcontacts,user, imageLoader);
		list_contact.setAdapter(contactsAdapter);
		contactsAdapter.setOnNextButtonClickListener(new OnNextButtonClickListener() {
			
			@Override
			public void onNextButtonClick(Contacts contacts) {
				Bundle bundle=new Bundle();
				bundle.putSerializable("contact", (Serializable) contacts);
				ActivityController.startNextActivityForResult(ContactsActivity.this, UpdateUserDetailsActivity.class, bundle, false, VIEW_PROFILE_REQUEST_CODE);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//System.out.println("request code "+ CONNECTION_LIST_REQUEST_CODE);
	
		if (requestCode==CONNECTION_LIST_REQUEST_CODE) {
//			connection_callService();
			db_contact_list= getConnectedList();
			if(db_contact_list.size()>0){
				publish_listView(db_contact_list);
			}else{
				service_callbackOnFaluire();
			}
		}		
		if(requestCode==VIEW_PROFILE_REQUEST_CODE && resultCode == RESULT_OK){
			try {
//				ArrayList<Contacts>	connected_list = getConnectedList();
//				if(connected_list!= null && connected_list.size()>0){
//					list_contact.setVisibility(View.VISIBLE);
//					tv_contact.setVisibility(View.GONE);
//					publish_listView(getConnectedList());
//				}else
//					service_callbackOnFaluire();
//				connection_callService();
				saveContact(INITIAL_LOADING);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(requestCode==VIEW_PROFILE_REQUEST_CODE && resultCode == RESULT_CANCELED){
			db_contact_list= getConnectedList();
			if(db_contact_list.size()>0){
				publish_listView(db_contact_list);
			}else{
				service_callbackOnFaluire();
			}
		}
	}
	
	private ArrayList<Contacts> getConnectedList(){
		ArrayList<Contacts>	connected_list = (ArrayList<Contacts>) ContactsTableManager.getConnectedContactList(ringToneBaseApplication.databaseManager, this, "C");
		return connected_list;
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
				 progressDialog = new ProgressDialog(ContactsActivity.this, R.style.alertDialogTheme);
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
			PhoneContactsUtility.getListContacts(ContactsActivity.this, phone_contacts_list,user.getPhone_number());
			db_contact_list=(ArrayList<Contacts>) ContactsTableManager.getSavedContactList(ContactsActivity.this,ringToneBaseApplication);
//          Iterator<Contacts> phoneContactIterator = phone_contacts_list.iterator();
//			Iterator<Contacts> databaseContactIterator = db_contact_list.iterator();
//			
//			while(phoneContactIterator.hasNext()){
//				databaseContactIterator = db_contact_list.iterator();
//				Contacts phoneContact = phoneContactIterator.next();
//					while (databaseContactIterator.hasNext()) {
//						Contacts databaseContact = databaseContactIterator.next();
//						if (phoneContact.getContact_number().equals(databaseContact.getContact_number())) {
//							phoneContact = databaseContact;
//							databaseContactIterator.remove();
//							break;
//						}
//				}
//			}
//			
			for(int i =0 ;i<phone_contacts_list.size();i++){
				for(int j = 0; j<db_contact_list.size();j++){
					if(phone_contacts_list.get(i).getContact_number().equals(db_contact_list.get(j).getContact_number())){
						Contacts contacts = new Contacts();
						contacts =	phone_contacts_list.get(i);
						contacts.setConnection_id(db_contact_list.get(j).getConnection_id());
						contacts.setConnection_status(db_contact_list.get(j).getConnection_status());
						
						contacts.setMy_image_thumb(phone_contacts_list.get(i).getMy_image_thumb());
						contacts.setMy_image_normal(phone_contacts_list.get(i).getMy_image_normal());
						contacts.setContact_ringtone(phone_contacts_list.get(i).getContact_ringtone());
						contacts.setMy_name(db_contact_list.get(j).getMy_name());
						contacts.setMy_ringtone(db_contact_list.get(j).getMy_ringtone());
						contacts.setPurchase_status(db_contact_list.get(j).isPurchase_status());
						System.out.println("ringtone rrl +"+i +db_contact_list.get(j).getMy_name()+ phone_contacts_list.get(i).getContact_ringtone()+","+db_contact_list.get(j).getMy_ringtone());
						phone_contacts_list.remove(i);
						phone_contacts_list.add(i,contacts);
					}
				}
			}
			
			try {
				TableUtils.clearTable(ringToneBaseApplication.databaseManager.getHelper(getApplicationContext()).getConnectionSource(),Contacts.class);
				Orm_SQLManager.insertCollectionIntoTable(Contacts.class, phone_contacts_list, ContactsActivity.this, ringToneBaseApplication.databaseManager);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
			
			if (phone_contacts_list.size() > 0) {
				connection_callService(loadingType);
			} else {
				service_callbackOnFaluire();
			}
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

		if (requestCode==PERMISSION_REQUEST_Camera){
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
			}
		}
		if (requestCode==PERMISSION_REQUEST_WRITEEXTERNALSTORAGE){
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED){

			}
		}
		if (requestCode==PERMISSION_REQUEST_READEXTERNALSTORAGE){
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED){

			}
		}
		if (requestCode==PERMISSION_REQUEST_SMSACCESS){
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED){

			}
		}
		if (requestCode==READCONTACT){
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED){

			}
		}
		if (requestCode==WRITECONTACT){
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED){

			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

	}
	
	
}
