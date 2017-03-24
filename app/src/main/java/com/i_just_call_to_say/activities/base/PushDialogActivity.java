package com.i_just_call_to_say.activities.base;

import com.i_just_call_to_say.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PushDialogActivity  extends Activity{

	private TextView tv_message;
	String message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.push_dialog_layout);
		System.out.println("push dialog");
		readFromBundle();
		
		initView();
		
	}
	private void readFromBundle(){
		message = (String) getIntent().getStringExtra("message");
	}
	private void initView(){
		tv_message = (TextView) findViewById(R.id.tv_message);
		tv_message.setText(message);
	}

	public void onButtonClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cross:
			finish();
			break;
		default:
			break;
		}
	}
	
}
