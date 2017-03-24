 package com.i_just_call_to_say.activities.greetings;

import java.io.File;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.service.dreams.DreamService;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.AppConstant;
import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.ReceivedGreetingsCard;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.imageLoder.ImageLoader;
import com.utility.DateUtility;
import com.utility.ImageViewRounded;
import com.utility.view.CustomTextView;

public class ViewReceiveGreetingActivity extends RingToneBaseActivity {

	private ImageView img_image;
	private ImageViewRounded profile_image;
	private ReceivedGreetingsCard greetingsCard;
	private Button btn_back;
	private CustomTextView tv_sender_name, tv_time;
	private ProgressBar pb_loading;
	private ImageLoader greetingLoader;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_receive_greetings_layout);
		imageLoader = new ImageLoader(this);
		greetingLoader = new ImageLoader(this, AppConstant.GREETING_IMAGE_DIRECTORY);
		getIntentData();
		initView();
		setTabSelected(RingToneBaseActivity.VIEW_RECEIVE_GREETING, "");
		attachListner();
	}

	private void getIntentData() {
		try {
			Bundle bundle = getIntent().getExtras();
			greetingsCard = (ReceivedGreetingsCard) bundle.getSerializable("receive_greeting");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void initView() {
		img_image = (ImageView) findViewById(R.id.img_image);
		pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_sender_name = (CustomTextView) findViewById(R.id.tv_sender_name);
		tv_time = (CustomTextView) findViewById(R.id.tv_time);
		profile_image = (ImageViewRounded) findViewById(R.id.profile_image);
		
		tv_time.setText(DateUtility.convertDateToScocialNetworkingFormat(greetingsCard.getSending_time()));
		
		
		if(greetingsCard.getSender_phone_no() !=null){
			List<Contacts> contacts = ContactsTableManager.getConnectedContact(ringToneBaseApplication.databaseManager, ViewReceiveGreetingActivity.this, greetingsCard.getSender_phone_no());
			if(contacts.size()>0 && contacts!=null){
				if(contacts.get(0).getMy_image_thumb() !=null && !contacts.get(0).getMy_image_thumb().equals("")){
					System.out.println("contacts.get(0).getMy_image() " + contacts.get(0).getMy_image_thumb());
					if(contacts.get(0).getMy_image_thumb().contains("content://")){
						profile_image.setImageURI(Uri.parse(contacts.get(0).getMy_image_thumb()));
					}
					else{
						imageLoader.DisplayImage(contacts.get(0).getMy_image_thumb(), profile_image, R.drawable.no_image,false);
					}
				}else{
					imageLoader.DisplayImage(contacts.get(0).getMy_image_thumb(), profile_image, R.drawable.no_image,false);
				}
				tv_sender_name.setText(contacts.get(0).getContact_name());
			}
		}else{
			tv_sender_name.setText("Unknown");
			profile_image.setImageResource(R.drawable.no_image);
		}
		
		greetingLoader.DisplayImage(greetingsCard.getCard_image_url(), img_image,0,pb_loading,this);
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(new File(Environment.getExternalStorageDirectory(),AppConstant.GREETING_IMAGE_DIRECTORY),String.valueOf(Math.abs(greetingsCard.getCard_image_url().hashCode()))+".png"))));
//		imageLoader.DisplayImage(greetingsCard.getSender_image(), profile_image, R.drawable.no_image, false);
	}

	private void loadAdd() {
		AdView adView = (AdView) findViewById(R.id.ad_view);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}

	private void attachListner() {
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		img_image.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					System.out.println("******action down");
					File directory = new File(Environment.getExternalStorageDirectory(),AppConstant.GREETING_IMAGE_DIRECTORY);
					if(directory.exists() && directory.isDirectory()){
						String filename=String.valueOf(Math.abs(greetingsCard.getCard_image_url().hashCode()))+".png";
						File file = new File(directory, filename);
						if(file.exists()){
							System.out.println("*****file exists");
							Intent imageIntent =new Intent(Intent.ACTION_VIEW);
							imageIntent.setDataAndType(Uri.parse("file://" + Uri.fromFile(file)), "image/*");
							startActivity(imageIntent);
						}
					}
				}
				return true;
			}
		});
	}

	@Override
	public void onBackPressed() {
		Intent mIntent = new Intent(getApplicationContext(),GreetingsActivity.class);
		mIntent.putExtra("from", "viewgreeting");
		startActivity(mIntent);
		finish();
	}
}
