package com.i_just_call_to_say.activities.greetings;

import java.util.Date;
import java.util.List;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.dto.CardCategory;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.utility.DialogUtility;
import com.utility.constants.StatusConstants;
import com.utility.view.CustomTextView;

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



public class SendCategoryAdapter extends ArrayAdapter<CardCategory>{
	private Activity activity;
	private List<CardCategory> cardcategList;
	private LayoutInflater layoutInflater;
	public static String contactidd;
	private User user;

	public SendCategoryAdapter(Activity activity,List<CardCategory> cardcategList,User user) {
		super(activity,0,cardcategList);
		this.activity = activity;
		this.cardcategList = cardcategList;
		System.out.println("contactlist"+cardcategList.size());//95238568
		this.user=user;
		layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		System.out.println("getview "+position);
		View view = convertView;
		final ViewHolder viewHolder;

		if (view == null) {
			view = layoutInflater.inflate(R.layout.send_greetings_list_row,parent, false);//b 1 2 3 b 4 5 6
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		CardCategory cardCategory=cardcategList.get(position);

		viewHolder.tv_category.setText(cardCategory.getCategory_name());
	
		return view;
	}

	private class ViewHolder {

		private CustomTextView tv_category;
		private Button btn_next;
		
		public ViewHolder(View v) {
			tv_category=(CustomTextView)v.findViewById(R.id.tv_category);
			attachListener();
		}
		
		private void attachListener(){
			
		}
	}
	
}
