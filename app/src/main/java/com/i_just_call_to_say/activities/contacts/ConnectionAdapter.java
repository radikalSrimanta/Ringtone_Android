package com.i_just_call_to_say.activities.contacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.imageLoder.ImageLoader;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.connections.ConnectionsActivity;
import com.i_just_call_to_say.activities.connections.ContactsAdapter.CustomFilter;
import com.i_just_call_to_say.activities.greetings.SendContactActivity;


import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.User;
import com.utility.view.ImageViewRounded;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ConnectionAdapter extends ArrayAdapter<Contacts> {

	private Activity activity;
	private List<Contacts> contactlist;
	private LayoutInflater layoutInflater;
	public static String contactidd;
	private ImageLoader imageLoader;
	private User user;
	private OnNextButtonClickListener nextButtonClickListener;
	
	public ConnectionAdapter(Activity activity,List<Contacts> contactlist,User user, ImageLoader imageLoader) {
		super(activity,0,contactlist);
		this.activity = activity;
		this.contactlist = contactlist;
		System.out.println("contactlist"+contactlist.size());//95238568
		this.user=user;
		layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageLoader= imageLoader;
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
			view = layoutInflater.inflate(R.layout.connection_list_row,parent, false);//b 1 2 3 b 4 5 6
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		final Contacts contacts = contactlist.get(position);
		viewHolder.btn_next.setTag(contacts);
		viewHolder.tv_name.setText(contacts.getContact_name());
		viewHolder.tv_number.setText(contacts.getContact_number());
//		String uurl=contacts.getContact_image();
//		String convertedurl = uurl.replaceAll("https", "http");
		//imageLoader.DisplayImage(convertedurl, viewHolder.img_display, R.drawable.no_image,false);

		if(contacts.getMy_image_thumb() !=null && !contacts.getMy_image_thumb().equals("")){
			System.out.println("adpter--->  "+contacts.getMy_image_thumb()+" ,," +contacts.getMy_image_thumb() +contacts.getContact_image());
			if(contacts.getMy_image_thumb().contains("content://")){
				System.out.println("--->adpter  1");
				String uurl=contacts.getContact_image();
           		String convertedurl = uurl.replaceAll("https", "http");
				viewHolder.img_display.setImageURI(Uri.parse(contacts.getMy_image_thumb()));
			}
			else{
				String uurl=contacts.getContact_image();
				String convertedurl = uurl.replaceAll("https", "http");
				imageLoader.DisplayImage(convertedurl, viewHolder.img_display, R.drawable.no_image,false);
			}
		}
		else{
			String uurl=contacts.getContact_image();
			String convertedurl = uurl.replaceAll("https", "http");
			imageLoader.DisplayImage(convertedurl, viewHolder.img_display, R.drawable.no_image,false);
		}
//		if(contacts.getContact_image() !=null && !contacts.getContact_image().equals("")){
//			imageLoader.DisplayImage(contacts.getContact_image(), viewHolder.img_display, R.drawable.no_image,false);
//		}

		
		viewHolder.btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(nextButtonClickListener!=null)
				nextButtonClickListener.onNextButtonClick((Contacts)v.getTag());
			}
		});
		
		return view;
	}
	
	private class ViewHolder {

			private TextView tv_name,tv_number;
			private ImageViewRounded img_display;
			private Button btn_next;
			public ViewHolder(View v) {
				tv_name=(TextView)v.findViewById(R.id.tv_name);
				tv_number=(TextView)v.findViewById(R.id.tv_number);
				img_display=(ImageViewRounded)v.findViewById(R.id.img_display);
				btn_next=(Button)v.findViewById(R.id.btn_next);
				img_display.setCirclePadding(activity.getResources().getInteger(R.integer.contact_list_row_img_display_padding), activity.getResources().getInteger(R.integer.contact_list_row_img_display_padding));
			}
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
	
	public class CustomFilter extends Filter{

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			String searchStr = constraint.toString();
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
			if (activity instanceof ContactsActivity){
				((ContactsActivity) activity).publish_listView(updateContactList);
			}else{
				((SendContactActivity) activity).publish_listView(updateContactList);	
			}
//				((ContactsActivity) activity).publish_listView(updateContactList);
//			else if (activity instanceof ContactsActivity)
//				((ContactsActivity) activity).publish_listView(updateContactList);
		}
	}
	
	public void setOnNextButtonClickListener(OnNextButtonClickListener nextButtonClickListener) {
		this.nextButtonClickListener = nextButtonClickListener;
	}
	
	public interface OnNextButtonClickListener{
		public void onNextButtonClick(Contacts contacts);
	}

//	public void updateContact (String contactId, String newName, Activity act) 
//		    throws RemoteException, OperationApplicationException{
//
//		        //ASSERT: @contactId alreay has a work phone number 
//		        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>(); 
//		        String selectPhone = Data.CONTACT_ID + "=? AND " + Data.MIMETYPE + "='"  + 
//		                        Phone.CONTENT_ITEM_TYPE + "'" + " AND " + Phone.TYPE + "=?";
//		        String[] phoneArgs = new String[]{contactId, String.valueOf(Phone.TYPE_WORK)}; 
//		        ops.add(ContentProviderOperation.newUpdate(Data.CONTENT_URI)
//		                .withSelection(selectPhone, phoneArgs)
//		                .withValue(Phone.DISPLAY_NAME, newName)
//		                .build()); 
//		        act.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
//		    }
	
}
