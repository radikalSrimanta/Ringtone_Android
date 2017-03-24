package com.i_just_call_to_say.activities.contacts;

import java.util.List;

import com.i_just_call_to_say.R;

import com.utility.view.CustomTextView;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class NoContactAdapter extends ArrayAdapter<String> {

	private List<String> list;
	private LayoutInflater inflater;

	public NoContactAdapter(Activity activity, List<String> list) {
		super(activity,0, list);
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
		System.out.println("list.get(position) --1" + list.get(0));
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		System.out.println("getview " + position);
		View view = convertView;
		final ViewHolder viewHolder;

		if (view == null) {
			view = inflater.inflate(R.layout.no_contact_item_layout, parent,
					false);// b 1 2 3 b 4 5 6
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_name.setText(list.get(position));
		return view;
	}

	private class ViewHolder {
		private CustomTextView tv_name;

		public ViewHolder(View v) {
			tv_name = (CustomTextView) v.findViewById(R.id.tv_name);
		}
	}
}
