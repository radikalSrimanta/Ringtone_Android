package com.i_just_call_to_say.activities.loginregistrartion;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.contacts.ContactsActivity;
import com.i_just_call_to_say.dto.User;
import com.utility.ActivityController;
import com.utility.AlertDialogCallBack;
import com.utility.CustomDialogUtility;
import com.utility.SendMessages;
import com.utility.SendMessages.MessageResponseListener;

public class TermAndCondition extends Activity {
	
	private Button btn_register;
	private EditText et_user_name,et_mob_no;
	private RingToneBaseApplication ringToneBaseApplication;
	private RegistrationApiManager registrationApiManager;
	private Object[] values;
	private final static int SMS_REQUEST_CODE = 100;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terms_conditions);
		readFromBundle();
		initView();
	}
	
	private void initView() {
		AdView adView = (AdView) findViewById(R.id.ad_view);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}

	private void readFromBundle() {
		String name =  (String) getIntent().getExtras().getSerializable("name");
		String mob_no =  (String) getIntent().getExtras().getSerializable("mob_no");
		String country_code =  (String) getIntent().getExtras().getSerializable("country_code");
		String deviceToken =  (String) getIntent().getExtras().getSerializable("deviceToken");
		String deviceType =  (String) getIntent().getExtras().getSerializable("deviceType");
	    Object[] values = { name,country_code, mob_no, deviceToken, deviceType };
		System.out.println("login" + name + country_code + mob_no+deviceToken+deviceType);
	    this.values = values;	    
	}

	@Override
	protected void onStart() {
		super.onStart();
		ringToneBaseApplication=(RingToneBaseApplication)getApplication();
	}

	
	public void onRegisterClick(View v) {
		registration_callService();
	}

	
	public void onCancelClick(View v) {
		finish();
	}
	
	private void registration_callService()	{
		registrationApiManager = new RegistrationApiManager(this, values, ringToneBaseApplication, "Please wait while registering...   ");
	}
	
	public void registration_serviceCallBack(String msg){
//		sendSMS((String) values[1]+values[2],msg);
		CustomDialogUtility.showCallbackMessageWithOk("Please wait for the SMS to confirm and register your account.", this , new AlertDialogCallBack() {
			@Override
			public void onSubmit() {
				try {
//					Intent intent = new Intent(Intent.ACTION_MAIN);
//					intent.addCategory(Intent.CATEGORY_LAUNCHER);
//					intent.setClassName("com.android.mms", "com.android.mms.ui.ConversationComposer");
////					intent.setType("vnd.android-dir/mms-sms");
//					startActivityForResult(intent,SMS_REQUEST_CODE);
			//		tryOpenSMSConversation();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onCancel() {
			}

			@Override
			public void onSubmitWithEditText(String text) {
			}
		});	
	}
	
	public void sendSMS(String number, String msg) {
		System.out.println("number "+ number);
		ArrayList<String> numberList = new ArrayList<String>();
		numberList.add(number);
		SendMessages message = new SendMessages(this, numberList, msg);
		message.setOnMessageSendListener(new MessageResponseListener() {
			
			@Override
			public void onSuccess() {
			}
			
			@Override
			public void onFailed(String msg) {
			}
		});
		numberList.clear();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == SMS_REQUEST_CODE){
			//login_callService();
		}
	}
	
	private void login_callService() {
      LoginApiManager loginApiManager = new LoginApiManager(this, values, ringToneBaseApplication,"Validating User...      ");
	}
	
	public void login_serviceCallBackOnSuccess() {
		ActivityController.startNextActivity(TermAndCondition.this, ContactsActivity.class, true);
		finish();
	}

	public void login_serviceCallBackOnFaluire(String msg){
		CustomDialogUtility.showCallbackMessageWithOk("invalid user".equalsIgnoreCase(msg)? "Please activate your account":msg, this, new AlertDialogCallBack() {			
			@Override
			public void onSubmitWithEditText(String text) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSubmit() {
				ActivityController.startNextActivity(TermAndCondition.this, RegistrationActivity.class, true);
				finish();
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	private boolean tryOpenSMSConversation() {
		boolean isWorking = false;
		Intent intent = new Intent(Intent.ACTION_MAIN);
		// DEFAULT ANDROID DEVICES
		intent.setComponent(new ComponentName("com.android.mms",
				"com.android.mms.ui.ConversationList"));
		isWorking = tryActivityIntent(this, intent);
		if (!isWorking) {
			// SAMSUNG DEVICES S3|S4|NOTE 2 etc.
			intent.setComponent(new ComponentName("com.android.mms",
					"com.android.mms.ui.ConversationComposer"));
			isWorking = tryActivityIntent(this, intent);
		}
		if (!isWorking) {
			// OPENS A NEW CREATE MESSAGE
			intent = new Intent(Intent.ACTION_MAIN);
			intent.setType("vnd.android-dir/mms-sms");
			isWorking = tryActivityIntent(this, intent);
		}
		if (!isWorking) {
			// TODO try something else
		}
		return isWorking;
	}

	public static boolean tryActivityIntent(Activity context,	Intent activityIntent) {

		// Verify that the intent will resolve to an activity
		try {
			if (activityIntent.resolveActivity(context.getPackageManager()) != null) {
				// context.startActivity(activityIntent);
				 context.startActivityForResult(activityIntent, SMS_REQUEST_CODE);
				return true;
			}
		} catch (SecurityException e) {
			return false;
		}
		return false;
	}


}
