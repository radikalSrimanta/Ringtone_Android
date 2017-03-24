package com.i_just_call_to_say.activities.greetings;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.imageLoder.ImageLoader;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.connections.ConnectionsActivity;
import com.i_just_call_to_say.activities.contacts.ContactsActivity;
import com.i_just_call_to_say.dto.CardCategory;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.GreetingsCard;
import com.utility.ActivityController;
import com.utility.AlertDialogCallBack;
import com.utility.CustomDialogUtility;
import com.utility.DateUtility;
import com.utility.view.CustomTextView;

public class GetSendGreetingsLargeActivity extends RingToneBaseActivity{
	
	private ImageView img_image;
	private GreetingsCard greetingsCard;
	private CardCategory cardCategory;
	private Contacts contacts;
	private CustomTextView tv_header;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_send_large_image);
		System.out.println("large ");

		getIntentData();
	//	initView();
//		setTabSelected(RingToneBaseActivity.GET_SEND_GREETINGS,cardCategory.getCategory_name());
		clickListner();
	}

	private void getIntentData(){
		try {
			Bundle bundle=getIntent().getExtras();
			greetingsCard=(GreetingsCard) bundle.getSerializable("GreetingsCard");
			cardCategory=(CardCategory) bundle.getSerializable("cardcategory");
			contacts = (Contacts) bundle.getSerializable("contacts");
			initView();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void initView(){
//		initBaseView();
		img_image=(ImageView)findViewById(R.id.img_image);
		btn_arrow = (Button) findViewById(R.id.btn_arrow);
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_header = (CustomTextView) findViewById(R.id.tv_header);
		tv_header.setText(cardCategory.getCategory_name());
		imageLoader=new ImageLoader(this);
		ProgressBar pb_loading = (ProgressBar) findViewById(R.id.pb_loading1);
		System.out.println("large 1 ");
		//imageLoader.DisplayImage(greetingsCard.getCard_image_url(),img_image, 0, pb_loading, this);
		imageLoader.DisplayImage(greetingsCard.getCard_image_url(), img_image, R.drawable.loading_image,false);
	}

	private void clickListner(){
		btn_arrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (contacts!=null) {
					CustomDialogUtility.showCallbackMessageWithOkCancel("Are you sure you want to send the card ?", GetSendGreetingsLargeActivity.this, new AlertDialogCallBack() {
						
						@Override
						public void onSubmitWithEditText(String text) {
							
						}
						
						@Override
						public void onSubmit() {
							sendgreeting_callService();
						}
						
						@Override
						public void onCancel() {
							
						}
					});
				}else{
					Bundle bundle=new Bundle();
					bundle.putString("category_id", cardCategory.getCategory_id());
					bundle.putString("card_id", greetingsCard.getCard_id());
					ActivityController.startNextActivity(GetSendGreetingsLargeActivity.this, SendContactActivity.class, bundle, false);
				}
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	private void sendgreeting_callService(){
		Object[] values = {user.getAccess_token(), cardCategory.getCategory_id(), greetingsCard.getCard_id(), contacts.getConnection_id(),DateUtility.getCurrentDateTimeInGMT(),RingToneBaseApplication.device_token};
		SendGreetingsApiManager sendGreetingsApiManager = new SendGreetingsApiManager(GetSendGreetingsLargeActivity.this, ringToneBaseApplication,"Sending card...      ", values);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
