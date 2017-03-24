package com.isis.module.push_notification.utility;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.isis.module.push_notification.R;
import com.isis.module.push_notification.utility.view.CustomButton;
import com.isis.module.push_notification.utility.view.CustomTextView;

public class CustomDialogUtility {

	public static void showCallbackMessageWithOk(String message, Activity mContext,	final AlertDialogCallBack callback) {
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog_layout);
		dialog.setCancelable(false);
		
		CustomTextView et_name = (CustomTextView) dialog.findViewById(R.id.et_message);
		CustomButton btn_ok = (CustomButton) dialog.findViewById(R.id.btn_ok);
		
		et_name.setText(message);
		btn_ok.setVisibility(View.VISIBLE);
		
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callback.onSubmit();
				dialog.dismiss();
			}
		});

		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog.show();
			}
		});
	}
}
