package com.i_just_call_to_say.activities.connections;

import java.util.ArrayList;

import android.os.Bundle;

import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.i_just_call_to_say.contactutility.PhoneContactsUtility;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.utility.orm_utility.Orm_SQLManager;

public class AllContactsActivity extends RingToneBaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);
		setTabSelected(RingToneBaseActivity.CONNECTIONS,"CONNECTIONS");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ArrayList<Contacts> phone_contacts_list=new ArrayList<Contacts>();
		ArrayList<Contacts> db_contact_list=new ArrayList<Contacts>();
		
		//////////////////Fetch Phone Number//////////////////
		PhoneContactsUtility.getListContacts(AllContactsActivity.this, phone_contacts_list,user.getPhone_number());
		
		
		///////////////////////Put phone numbers on DB//////////////
		db_contact_list=(ArrayList<Contacts>) ContactsTableManager.getSavedContactList(AllContactsActivity.this, ringToneBaseApplication);
		System.out.println("db_contact_list 1"+db_contact_list.size());
		if(db_contact_list.size()==0){
			Orm_SQLManager.insertCollectionIntoTable(Contacts.class, phone_contacts_list, AllContactsActivity.this, ringToneBaseApplication.databaseManager);
			db_contact_list.clear();
			db_contact_list=(ArrayList<Contacts>) ContactsTableManager.getSavedContactList(AllContactsActivity.this, ringToneBaseApplication);
		}
		
	
	}

}
