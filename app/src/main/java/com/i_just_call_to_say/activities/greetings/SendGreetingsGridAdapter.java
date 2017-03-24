package com.i_just_call_to_say.activities.greetings;

import java.util.ArrayList;

import com.imageLoder.ImageLoader;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.dto.GreetingsCard;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SendGreetingsGridAdapter extends ArrayAdapter<GreetingsCard>{
	private ImageLoader imageLoader;

	private ArrayList<GreetingsCard> greetingscardlist;
	private LayoutInflater layoutInflater;
	public SendGreetingsGridAdapter(Activity activity, ArrayList<GreetingsCard> greetingslist, ImageLoader imageLoader) {
		super(activity, 0,greetingslist);
		this.greetingscardlist=greetingslist;
		layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageLoader= imageLoader;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder viewHolder;

		if (view == null) {
			view = layoutInflater.inflate(R.layout.image_grid_row,parent, false);//b 1 2 3 b 4 5 6
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		GreetingsCard greetingsCard=greetingscardlist.get(position);
		imageLoader.DisplayImage(greetingsCard.getCard_imagethumbnail_url(), viewHolder.iv_GridImage, R.drawable.loading_image,false);
		return view;
	}
	
	private class ViewHolder {
		private ImageView iv_GridImage;
		public ViewHolder(View v) {
			iv_GridImage=(ImageView)v.findViewById(R.id.iv_GridImage);
		}
	}

}
