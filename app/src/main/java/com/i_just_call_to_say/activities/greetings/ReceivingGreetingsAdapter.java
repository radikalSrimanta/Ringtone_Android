package com.i_just_call_to_say.activities.greetings;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.imageLoder.ImageLoader;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.contactutility.PhoneContactsUtility;
import com.i_just_call_to_say.dto.CardCategory;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.GreetingsCard;
import com.i_just_call_to_say.dto.ReceivedGreetingsCard;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.utility.DateUtility;
import com.utility.DialogUtility;
import com.utility.constants.StatusConstants;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReceivingGreetingsAdapter extends ArrayAdapter<ReceivedGreetingsCard>{
	private Activity activity;
	private List<ReceivedGreetingsCard> greetingscardlist;
	private LayoutInflater layoutInflater;
	public static String contactidd;
	private User user;
	private ImageLoader imageLoader;

	public ReceivingGreetingsAdapter(Activity activity,List<ReceivedGreetingsCard> greetingscardlist,User user, ImageLoader imageLoader) {
		super(activity,0,greetingscardlist);
		this.activity = activity;
		this.greetingscardlist = greetingscardlist;
		sortGreetingCardList();
		this.user=user;
		layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageLoader = imageLoader;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		System.out.println("getview "+position);
		View view = convertView;
		final ViewHolder viewHolder;

		if (view == null) {
			view = layoutInflater.inflate(R.layout.received_greetings_list_row,parent, false);//b 1 2 3 b 4 5 6
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		ReceivedGreetingsCard greetingsCard=greetingscardlist.get(position);

		
		if(greetingsCard.getSender_phone_no()!=null){
			System.out.println("ph no ->"+greetingsCard.getSender_phone_no());
			List<Contacts> contacts = ContactsTableManager.getConnectedContact(((RingToneBaseApplication)activity.getApplication()).databaseManager,activity, greetingsCard.getSender_phone_no());
			System.out.println("ph no -> 1"+contacts.size());
			if(contacts.size()>0 && contacts!=null){
				viewHolder.tv_sender_name.setText(contacts.get(0).getContact_name());
			}
		}else {
			    System.out.println("ph no ->"+"unknown");
				viewHolder.tv_sender_name.setText("Unknown");
		}
		viewHolder.tv_sender_time.setText(DateUtility.convertDateToScocialNetworkingFormat(greetingsCard.getSending_time()));
		imageLoader.DisplayImage(greetingsCard.getCard_imagethumbnail_url(),viewHolder.img_received, R.drawable.loading_image, false);	
		return view;
	}

	private class ViewHolder {
		private TextView tv_sender_name,tv_sender_time;
		private ImageView img_received;
		
		public ViewHolder(View v) {
			tv_sender_name=(TextView)v.findViewById(R.id.tv_sender_name);
			tv_sender_time=(TextView)v.findViewById(R.id.tv_sender_time);
			img_received=(ImageView)v.findViewById(R.id.img_received);
		}
	}
	
	private void sortGreetingCardList() {
		Collections.sort(greetingscardlist,new Comparator<ReceivedGreetingsCard>() {

			@Override
			public int compare(ReceivedGreetingsCard lhs, ReceivedGreetingsCard rhs) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					return df.parse(rhs.getSending_time()).compareTo(df.parse(lhs.getSending_time()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}
		} );
	}
	
	public class DateBySort implements Comparator<String> {

		@Override
		public int compare(String lhs, String rhs) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return df.parse(rhs).compareTo(df.parse(lhs));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}
	}


}
