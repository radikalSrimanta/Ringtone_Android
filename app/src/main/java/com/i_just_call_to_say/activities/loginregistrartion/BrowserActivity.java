package com.i_just_call_to_say.activities.loginregistrartion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.connections.ConnectionsActivity;
import com.i_just_call_to_say.activities.contacts.ContactsActivity;
import com.i_just_call_to_say.activities.contacts.InitializeBackGroundService;
import com.i_just_call_to_say.activities.splash.SplashActivity;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.table_query_manager.UserTableManager;
import com.utility.ActivityController;
import com.utility.AlertDialogCallBack;
import com.utility.CommonUtility;
import com.utility.CustomDialogUtility;
import com.utility.DialogUtility;
import com.utility.constants.StatusConstants;

public class BrowserActivity extends RingToneBaseActivity{//com.i_just_call_to_say.activities.loginregistrartion.BrowserActivity

	private WebView wv_openurl;
	private ProgressDialog progressDialog;
	private User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.broweractivity);
		initView();
		readFromBundle();
		setTabSelected(RingToneBaseActivity.WEB_ACCOUNT_ACTIVATION, "I Just Call To Say");
		attachListener();
	}
	
	
	private void readFromBundle(){
		if(getIntent().getExtras() != null){
			progressDialog = null;
			loadwebView();
		}
	}
	
	private void loadwebView(){
		wv_openurl.getSettings().setJavaScriptEnabled(true);
		wv_openurl.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			
			@Override
			public void onLoadResource(WebView view, String url) {
				if(progressDialog == null ){
					progressDialog = new ProgressDialog(BrowserActivity.this,R.style.alertDialogTheme);
					progressDialog.setMessage("Loading...           ");
					progressDialog.show();
				}
			}
			
			
			@Override
			public void onPageFinished(WebView view, String url) {
				try {
					if(progressDialog != null && progressDialog.isShowing()){
						progressDialog.dismiss();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		wv_openurl.loadUrl(getIntent().getExtras().getString("url").toString());
	}
	private void initView(){
		initBaseView();
		wv_openurl = (WebView)findViewById(R.id.wv_openurl);
	}
	
	
	private void attachListener(){
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	@Override
		public void onBackPressed() {
			// call login service and 
		   // if success then move to home screen or contact screen
		   // or registration screen
		gotoNextActivity();
	}
	
	private void gotoNextActivity(){
		user = UserTableManager.getSavedUser(this, ringToneBaseApplication);
		if (user == null) {
			ActivityController.startNextActivity(this, RegistrationActivity.class,	true);
			finish();
		} else {
			login_callService();
		}
	}


	private void login_callService() {
		if (CommonUtility.checkConnectivity(this)) {
			String name = user.getUser_name();
			String country_code = user.getCountry_code();
			String mob_no = user.getPhone_number();
//			String deviceToken = "APA91bEWh9k81qYi1pfeshQuvZQ2lBIKItUdxDIjq7M_TLZz68mAm0x_mpZfJEyZPRwmxDZO8KZKN8hP5OpXc1e3LhZ-TPmXzj0O8-kVApskK_A1atTqxYO9VPz5ZCCbYX0B2Gmwf8FM";
			String deviceType = "A";
			Object[] values = { name,country_code, mob_no, RingToneBaseApplication.device_token, deviceType };
			LoginApiManager loginApiManager = new LoginApiManager(this, values, ringToneBaseApplication,"Validating User...    ");
		}
		else{
			DialogUtility.showMessageOkWithCallback("network_unavailable", this, new AlertDialogCallBack() {
				
				@Override
				public void onSubmitWithEditText(String text) {
				}
				
				@Override
				public void onSubmit() {
					if (!getIntent().getExtras().getString("url").toString().equalsIgnoreCase("account_activation")) {
						ActivityController.startNextActivity(BrowserActivity.this, ConnectionsActivity.class, true);
						finish();
					}else{
						ActivityController.startNextActivity(BrowserActivity.this, RegistrationActivity.class, true);
						finish();
					}
				}
				
				@Override
				public void onCancel() {
					
				}
			});
			
		}
	}
	
	public void login_serviceCallBackOnSuccess(){
		if(!RingToneBaseActivity.isMyServiceRunning(BrowserActivity.this)){
			Intent intent = new Intent(BrowserActivity.this,InitializeBackGroundService.class);
			startService(intent);
		}else{
			System.out.println("---   start already started service --------------");
		}
		ActivityController.startNextActivity(this, ContactsActivity.class, true);
		finish();
	}


	public void login_serviceCallBackOnFaluire(String message) {
		CustomDialogUtility.showCallbackMessageWithOk("invalid user".equalsIgnoreCase(message)? "Validation Failed":message, this, new AlertDialogCallBack() {			
			@Override
			public void onSubmitWithEditText(String text) {
			
			}
			
			@Override
			public void onSubmit() {
				ActivityController.startNextActivity(BrowserActivity.this, RegistrationActivity.class, true);
				finish();
			}
			
			@Override
			public void onCancel() {
			
			}
		});		
	}
	
	
}
