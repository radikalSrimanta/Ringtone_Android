package com.i_just_call_to_say.activities.loginregistrartion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.connections.ConnectionsActivity;
import com.i_just_call_to_say.activities.contacts.ContactsActivity;
import com.i_just_call_to_say.activities.loginregistrartion.SelectCountryDialog.OnItemSelectListener;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.utility.ActivityController;
import com.utility.CommonUtility;
import com.utility.CustomDialogUtility;
import com.utility.DialogUtility;
import com.utility.view.CustomEditText;

public class RegistrationActivity extends Activity {
	
	private CustomEditText et_user_name, et_mob_no;
	private Button btn_register, btn_cancel,et_country_code;
	private RegistrationApiManager registrationApiManager;
	private RingToneBaseApplication ringToneBaseApplication;
	private String userName, mobileNo, country_code;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);
		initView();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		ringToneBaseApplication = (RingToneBaseApplication) getApplication();
	}
	
	private void initView(){
		et_user_name = (CustomEditText) findViewById(R.id.et_user_name);
		et_mob_no = (CustomEditText) findViewById(R.id.et_mob_no);
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		et_country_code = (Button) findViewById(R.id.et_country_code);
	}
	
	public void onButtonClick(View v){
		switch (v.getId()) {
		case R.id.rl_parent:
			CommonUtility.hideKeyboard(RegistrationActivity.this);
			break;
		case R.id.btn_register:
			if (validateUserInputs()) {
				CommonUtility.hideKeyboard(RegistrationActivity.this);
				onRegister();
			}

			break;
		case R.id.btn_cancel:
			finish();
			break;
			
		case R.id.et_country_code:
			selectCountryCode();
			break;

		}
	}
	
	private void selectCountryCode() {
		final SelectCountryDialog countryDialog = new SelectCountryDialog(RegistrationActivity.this);
		countryDialog.setOnItemSelectListener(new OnItemSelectListener() {
			
			@Override
			public void onItemSelected(String country_code) {
				et_country_code.setText(country_code);
				countryDialog.dismiss();
			}
		});
		countryDialog.show();
	}

	private void changeTrimText() {
		userName = et_user_name.getText().toString().trim();
		mobileNo = et_mob_no.getText().toString().trim();
		country_code = et_country_code.getText().toString().trim();
		if ("".equals(userName) || "".equals(mobileNo)) {
			et_user_name.setText("");
			et_mob_no.setText("");
		}
	}
	
	private void showMessage(String message) {
		CustomDialogUtility.showMessageWithOk(message, this);
//		changeTrimText();
	}
	
	private boolean validateUserInputs() {
		boolean isValidate = false;
		String name = et_user_name.getText().toString().trim();
		String mob_no = et_mob_no.getText().toString().trim();
		String country_code = et_country_code.getText().toString().trim();
		
		if (name.equals("")) {
			showMessage("Name cannot be left blank");
			return isValidate;
		} else if (removeZero(mob_no).equals("")) {
			showMessage("Mobile No cannot be left blank");
			return isValidate;
		} else if(country_code.equals("")){
			showMessage("Country code cannot be left blank");
			return isValidate;
		}else {
			isValidate = true;
		}
		return isValidate;
	}
	
	
	private String removeZero(String ph_no){
		while(true){
			if(ph_no.indexOf("0") == 0){
				ph_no = ph_no.substring(1);
			}else{
				break;
			}
		}
		return ph_no;
	}
	

	private void onRegister() {
		String name = et_user_name.getText().toString();
		String mob_no = removeZero(et_mob_no.getText().toString());
		String country_code = et_country_code.getText().toString().trim();
//		String deviceToken = "APA91bEWh9k81qYi1pfeshQuvZQ2lBIKItUdxDIjq7M_TLZz68mAm0x_mpZfJEyZPRwmxDZO8KZKN8hP5OpXc1e3LhZ-TPmXzj0O8-kVApskK_A1atTqxYO9VPz5ZCCbYX0B2Gmwf8FM";
		String deviceToken = RingToneBaseApplication.device_token;
		String deviceType = "A";
		Object[] values = { name, mob_no, deviceToken, deviceType };
//		registrationApiManager = new RegistrationApiManager(this, values, ringToneBaseApplication, "Registering...");
		
		Bundle bundle = new Bundle();
		bundle.putString("name", name);	
		bundle.putString("mob_no", mob_no);	
		bundle.putString("country_code", country_code);	
		bundle.putString("deviceToken", deviceToken);
		bundle.putString("deviceType", deviceType);
				
		ActivityController.startNextActivity(this, TermAndCondition.class,bundle, false);
	}

	public void gotoNextPage() {
		List<Contacts> db_contact_list = (ArrayList<Contacts>) ContactsTableManager.getConnectedContactList(ringToneBaseApplication.databaseManager, RegistrationActivity.this, "C");
		if (db_contact_list.size() > 0)
			ActivityController.startNextActivity(this, ConnectionsActivity.class, true);
		else
			ActivityController.startNextActivity(this, ContactsActivity.class, true);
	}


}
