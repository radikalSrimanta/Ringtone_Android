package com.utility;

import java.util.ArrayList;

import com.i_just_call_to_say.R;

import android.app.Activity;
import android.app.ProgressDialog;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SendMessages{
	
	private int mMessageSentParts;
	private int mMessageSentTotalParts;
	private int mMessageSentCount;
	private String SENT = "SMS_SENT";
	private String DELIVERED = "SMS_DELIVERED";
	private ArrayList<String> numbers;
	private Activity activity;
	private String message;
	boolean failed = false;
	private MessageResponseListener getResponse;
	private ProgressDialog pd;
	
	public SendMessages(Activity activity, ArrayList<String> phoneNumber, String message) {
		this.activity = activity;
		this.numbers = phoneNumber;
		this.message = message.trim() + "\n\nSent by "+activity.getString(R.string.app_name);
		startSendMessages();
	}
	
	private void startSendMessages() {
//		pd = new ProgressDialog(activity);
//		pd.setMessage("Sending message...");
//		pd.setCancelable(false);
//		pd.show();
		registerBroadCastReceivers();
		mMessageSentCount = 0;
		sendSMS(numbers.get(mMessageSentCount).toString().trim(), message);
	}

	private void sendNextMessage() {
		if (thereAreSmsToSend()) {
			sendSMS(numbers.get(mMessageSentCount).toString().trim(), message);
		} else {
			//Toast.makeText(activity, "All SMS have been sent", Toast.LENGTH_SHORT).show();
//			pd.dismiss(); 
			if(getResponse!=null)
			getResponse.onSuccess();
		}
	}

	private boolean thereAreSmsToSend(){
	    return mMessageSentCount < numbers.size();
	}

	private void sendSMS(final String phoneNumber, String message) {
	    String SENT = "SMS_SENT";
	    String DELIVERED = "SMS_DELIVERED";

	    SmsManager sms = SmsManager.getDefault();
	    ArrayList<String> parts = sms.divideMessage(message);
	    mMessageSentTotalParts = parts.size();

	    Log.i("Message Count", "Message Count: " + mMessageSentTotalParts);

	    ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
	    ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();

	    PendingIntent sentPI = PendingIntent.getBroadcast(activity, 0, new Intent(SENT), 0);
	    PendingIntent deliveredPI = PendingIntent.getBroadcast(activity, 0, new Intent(DELIVERED), 0);

	    for (int j = 0; j < mMessageSentTotalParts; j++) {
	        sentIntents.add(sentPI);
	        deliveryIntents.add(deliveredPI);
	    }

	    mMessageSentParts = 0;
	    sms.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, deliveryIntents);
	}

	String msg;
	private void registerBroadCastReceivers(){

		activity.registerReceiver(new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context arg0, Intent arg1) {
	        	
	            switch (getResultCode()) {
	            case Activity.RESULT_OK:

	                mMessageSentParts++;
	                if ( mMessageSentParts == mMessageSentTotalParts ) {
//	                	addToSentBox(numbers.get(mMessageSentCount).toString().trim(), message);
	                	mMessageSentCount++;
	                    sendNextMessage();
	                }
	               
	                //Toast.makeText(activity, "SMS sent", Toast.LENGTH_SHORT).show();
	                
	                break;
	            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	                //Toast.makeText(activity, "Generic failure", Toast.LENGTH_SHORT).show();
	                failed = true;
	                break;
	            case SmsManager.RESULT_ERROR_NO_SERVICE:
	                //Toast.makeText(activity, "No service", Toast.LENGTH_SHORT).show();
	            	 failed = true;
	                break;
	            case SmsManager.RESULT_ERROR_NULL_PDU:
	            	//getResponse.onFailed(msg);
	            	 failed = true;
	                //Toast.makeText(activity, "Null PDU", Toast.LENGTH_SHORT).show();
	                break;
	            case SmsManager.RESULT_ERROR_RADIO_OFF:
	            	//getResponse.onFailed(msg);
	            	 failed = true;
	                //Toast.makeText(activity, "Radio off", Toast.LENGTH_SHORT).show();
	                break;
	            }
				if (failed && getResponse != null) {
//					pd.dismiss();
//					addToDraft(numbers.get(mMessageSentCount).toString().trim(), message);
					getResponse.onFailed(msg);
					getResponse = null;
				}
	        }
	    }, new IntentFilter(SENT));

		activity.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {

				case Activity.RESULT_OK:
//					Toast.makeText(activity, "SMS delivered", Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
//					Toast.makeText(activity, "SMS not delivered", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

	}
	
	
	public void setOnMessageSendListener(MessageResponseListener getResponse) {
		this.getResponse = getResponse;
	}
	
	public interface MessageResponseListener {
		public abstract void onSuccess();

		public abstract void onFailed(String msg);

	}
	
	private void addToSentBox(String number, String message) {
		ContentValues values = new ContentValues();
		values.put("address", number);
		values.put("body", message);
		activity.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}
	
	private void addToDraft(String number, String message) {
		ContentValues values = new ContentValues();
		values.put("address", number);
		values.put("body", message);
		activity.getContentResolver().insert(Uri.parse("content://sms/draft"), values);
	}
	
}
