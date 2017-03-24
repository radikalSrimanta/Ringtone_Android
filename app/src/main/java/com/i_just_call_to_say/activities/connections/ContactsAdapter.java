package com.i_just_call_to_say.activities.connections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.LinearLayout;

import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.imageLoder.ImageLoader;
import com.utility.CustomDialogUtility;
import com.utility.PreferenceUtility;
import com.utility.SendMessages;
import com.utility.SendMessages.MessageResponseListener;
import com.utility.constants.StatusConstants;
import com.utility.view.CustomButton;
import com.utility.view.CustomTextView;
import com.utility.view.ImageViewRounded;
public class ContactsAdapter extends ArrayAdapter<Contacts>{
	private Activity activity;
	private List<Contacts> contactlist;
	private LayoutInflater layoutInflater;
	public static String contactidd;
	private ImageLoader imageLoader;
	private User user;

	public ContactsAdapter(Activity activity,List<Contacts> contactlist,User user,ImageLoader imageLoader) {
		super(activity,0,contactlist);
		this.activity = activity;
		this.contactlist = contactlist;
		//System.out.println("contactlist"+contactlist.size());//95238568
		this.user=user;
		layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageLoader = imageLoader;
		sortContactList(contactlist);
	}

	
	
	public List<Contacts> getContactlist() {
		return contactlist;
	}



	public void setContactlist(List<Contacts> contactlist) {
		this.contactlist = contactlist;
	}



	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		System.out.println("getview "+position);
		View view = convertView;
		final ViewHolder viewHolder;

		if (view == null) {
			view = layoutInflater.inflate(R.layout.contact_list_row,parent, false);//b 1 2 3 b 4 5 6
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.tv_req_connect.setVisibility(View.GONE);
		viewHolder.btn_connect.setVisibility(View.GONE);
		viewHolder.ll_accept_deny.setVisibility(View.GONE);
		
		final Contacts contacts = contactlist.get(position);
//		imageLoader.DisplayImage(contacts.getContact_image(), viewHolder.img_display, R.drawable.no_image,false);
		System.out.println("status for image"+contacts.getConnection_status()+contacts.getMy_image_thumb());
		contactidd=contacts.getContact_id();
		viewHolder.tv_name.setText(contacts.getContact_name());
		viewHolder.tv_number.setText(contacts.getContact_number());
		
		if(contacts.getMy_image_thumb() !=null && !contacts.getMy_image_thumb().equals("")){
			System.out.println("********image thumb  "+contacts.getMy_image_thumb());
			if(contacts.getMy_image_thumb().contains("content://")){
			viewHolder.img_display.setImageURI(Uri.parse(contacts.getMy_image_thumb()));
			}
			else{
				imageLoader.DisplayImage(contacts.getMy_image_thumb(), viewHolder.img_display, R.drawable.no_image,true);
			}
		}else{
			imageLoader.DisplayImage(contacts.getMy_image_thumb(), viewHolder.img_display, R.drawable.no_image,true);
		}
		
		if(contacts.getConnection_status().equals(StatusConstants.REQUEST_FOR_CONNECT)){
			viewHolder.tv_req_connect.setVisibility(View.VISIBLE);
			viewHolder.btn_connect.setVisibility(View.VISIBLE);
			viewHolder.btn_connect.setText("ReConnect");	
			viewHolder.ll_accept_deny.setVisibility(View.GONE);
//			viewHolder.tv_connected.setVisibility(View.GONE);
		}else if(contacts.getConnection_status().equals(StatusConstants.NOT_CONNECT)){
			viewHolder.tv_req_connect.setVisibility(View.GONE);
			viewHolder.btn_connect.setText("Connect");
			viewHolder.btn_connect.setVisibility(View.VISIBLE);
			viewHolder.ll_accept_deny.setVisibility(View.GONE);
//			viewHolder.tv_connected.setVisibility(View.GONE);
		}else if(contacts.getConnection_status().equals(StatusConstants.PENDING)){
			viewHolder.tv_req_connect.setVisibility(View.GONE);
			viewHolder.btn_connect.setVisibility(View.GONE);
//			viewHolder.btn_connect.setText("Connect");
			viewHolder.tv_connected.setVisibility(View.GONE);
			viewHolder.ll_accept_deny.setVisibility(View.VISIBLE);
		}else if(contacts.getConnection_status().equals(StatusConstants.CONNECT)){
			viewHolder.tv_req_connect.setVisibility(View.VISIBLE);
			viewHolder.tv_req_connect.setText("Connected");
			viewHolder.btn_connect.setVisibility(View.GONE);
//			viewHolder.btn_connect.setText("Connect");
			viewHolder.ll_accept_deny.setVisibility(View.GONE);
//			viewHolder.tv_connected.setVisibility(View.VISIBLE);
		}else{
			viewHolder.tv_req_connect.setVisibility(View.GONE);
			viewHolder.btn_connect.setVisibility(View.VISIBLE);
			viewHolder.btn_connect.setText("Connect");
			viewHolder.tv_connected.setVisibility(View.GONE);
			viewHolder.ll_accept_deny.setVisibility(View.GONE);
		}
		viewHolder.img_display.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.out.println("click"+position);
			}
		});
		viewHolder.btn_connect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int status = PreferenceUtility.getIntegerFromPreference(activity, "warning");
				System.out.println("warning "+ status);
				if (status == 0)
					sendContactRequest(contacts);
				else
					add_connection_service("SEND", contacts);
			}
		});
		
		viewHolder.btn_Acept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				add_connection_service("ACCEPT",contacts);				
			}
		});
		
		viewHolder.btn_Deny.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				add_connection_service("DENY",contacts);				
			}
		});
		
		
		return view;
	}

	private class ViewHolder {

		private CustomTextView tv_name,tv_req_connect,tv_connected,tv_number;
		private ImageViewRounded img_display;
		private Button btn_connect,btn_Acept,btn_Deny;
		private LinearLayout ll_accept_deny;

		public ViewHolder(View v) {
			tv_req_connect=(CustomTextView)v.findViewById(R.id.tv_req_connect);
			tv_connected=(CustomTextView)v.findViewById(R.id.tv_connected);
			tv_name=(CustomTextView)v.findViewById(R.id.tv_name);
			tv_number=(CustomTextView)v.findViewById(R.id.tv_number);
			img_display=(ImageViewRounded)v.findViewById(R.id.img_display);
			img_display.setCirclePadding(activity.getResources().getInteger(R.integer.contact_list_row_img_display_padding), activity.getResources().getInteger(R.integer.contact_list_row_img_display_padding));
			btn_connect=(Button)v.findViewById(R.id.btn_connect);
			ll_accept_deny=(LinearLayout)v.findViewById(R.id.ll_accept_Deny);
			btn_Acept=(Button)v.findViewById(R.id.btn_Acept);
			btn_Deny=(Button)v.findViewById(R.id.btn_Deny);
		}
	}
	
	
	private void sendContactRequest(final Contacts contacts) {
		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.warning_layout);
		dialog.setCancelable(false);
		
		CustomTextView et_message = (CustomTextView) dialog.findViewById(R.id.et_message);
		CustomButton btn_accept = (CustomButton) dialog.findViewById(R.id.btn_accept);
		CustomButton btn_cancel = (CustomButton) dialog.findViewById(R.id.btn_cancel);
		CheckBox cb_showWaring = (CheckBox) dialog.findViewById(R.id.cb_showWaring);
		
//		et_message.setText("Warning Message");
		
		cb_showWaring.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					PreferenceUtility.saveIntegerInPreference(activity, "warning",1);
				} else {
					PreferenceUtility.saveIntegerInPreference(activity, "warning",0);
				}
			}
		});
			
		btn_accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				add_connection_service("SEND",contacts);
				dialog.dismiss();
			}
		});
		
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				dialog.dismiss();
			}
		});

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog.show();
			}
		});
	}
	
	private void add_connection_service(final String connection_type,final Contacts contacts){
		Object[] values={user.getAccess_token(),connection_type,contacts.getContact_number(),RingToneBaseApplication.device_token};
		RingToneBaseApplication ringToneBaseApplication=(RingToneBaseApplication)(activity).getApplication();
		AddConnectionApiManager addConnectionApiManager=new AddConnectionApiManager(activity, contacts, ringToneBaseApplication, "Connecting...       ",values,new IUpdateContacts() {

			@Override
			public void updateContacts(String message , String sms_msg) {
			
			CustomDialogUtility.showMessageWithOk(message, activity);
				contactlist.clear();
				contactlist=ContactsTableManager.getSavedContactList(activity, (RingToneBaseApplication)activity.getApplication());
				System.out.println("contactlist 111"+contactlist.size());
				notifyDataSetChanged();
				((ConnectionsActivity)activity).getAllContactList(contactlist);
				if(connection_type.equalsIgnoreCase("SEND")){
					sms_msg = "Hi,\n "+user.getUser_name()+sms_msg;
					sendSMS(contacts.getContact_number(), sms_msg);
				}
				
			}

			@Override
			public void onFailuepdateContacts(String message) {
				
			}
		});
	}

	private void sortContactList(List<Contacts> contactlist){
	  Collections.sort(contactlist,new Comparator<Contacts>() {
		@Override
		public int compare(Contacts lhs, Contacts rhs) {
			return lhs.getContact_name().compareToIgnoreCase(rhs.getContact_name());
		}
	});
	}
	
	public CustomFilter getCustomFilter() {
		return new CustomFilter();
	}
	
	
	private void sendSMS(String number, String msg) {
		ArrayList<String> numberList = new ArrayList<String>();
		numberList.add(number);
		SendMessages message = new SendMessages(activity, numberList, msg);
		message.setOnMessageSendListener(new MessageResponseListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailed(String msg) {
				// TODO Auto-generated method stub
				
			}
		});
		numberList.clear();
	}
	
	public class CustomFilter extends Filter{

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			String searchStr = constraint.toString();
			System.out.println("searchStr "+searchStr);
			FilterResults filterResults = new FilterResults();
			List<Contacts> tempList = new ArrayList<Contacts>();
			if (searchStr != null && searchStr.length() > 0) {
				for (Contacts contacts : contactlist ) {
					if (contacts.getContact_name() !=null && contacts.getContact_number() !=null && contacts.getContact_name().toLowerCase(Locale.ENGLISH).startsWith(searchStr.toLowerCase(Locale.ENGLISH))) {
						tempList.add(contacts);
					}
				}
				filterResults.values = tempList;
			}else
				filterResults.values = contactlist;
			
			return filterResults;
		}

		@Override
		protected void publishResults(CharSequence constraint,FilterResults results) {
			ArrayList<Contacts> updateContactList = (ArrayList<Contacts>) results.values;
			if (activity instanceof ConnectionsActivity)
				((ConnectionsActivity) activity).publish_listView(updateContactList);
//			else if (activity instanceof ContactsActivity)
//				((ContactsActivity) activity).publish_listView(updateContactList);
		}
		
	}
	
}
