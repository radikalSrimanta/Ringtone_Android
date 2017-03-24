package com.isis.module.push_notification.utility;

import com.isis.module.push_notification.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogUtility {

	/**
	 * showMessageWithOk
	 * @param activity
	 * @param message
	 */
	public static void showMessageWithOk(final Activity activity,
			final String message) {

		activity.runOnUiThread(new Runnable() {

			public void run() {
				final AlertDialog.Builder alert = new AlertDialog.Builder(
						activity);
				alert.setTitle(R.string.app_name);
				alert.setMessage(message);
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						});
				alert.show();
			}
		});
	}
	
	/**
	 * showMessageWithOk (with callback)
	 * @param activity
	 * @param message
	 * @param alertDialogCallBack
	 */
	public static void showMessageWithOk(final Activity activity,
			final String message,final AlertDialogCallBack alertDialogCallBack) {

		activity.runOnUiThread(new Runnable() {

			public void run() {
				final AlertDialog.Builder alert = new AlertDialog.Builder(
						activity);
				alert.setTitle(R.string.app_name);
				alert.setMessage(message);
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								alertDialogCallBack.onSubmit();
							}
						});
				alert.show();
			}
		});
	}
	
	public static void showMessageWithOkCustom(final Context mContext,
			final String message,final AlertDialogCallBack alertCallback) {
		((Activity) mContext).runOnUiThread(new Runnable() {
			public void run() {
				final AlertDialog.Builder alert = new AlertDialog.Builder(
						mContext);
				alert.setTitle(R.string.app_name);
				TextView txt_m1;
				LinearLayout layout = new LinearLayout(mContext);
				layout.setOrientation(1);
				layout.setPadding(10, 1, 1, 1);
				txt_m1 = new TextView(mContext);
				txt_m1.setText(message);
				txt_m1.setTextColor(Color.WHITE);
				txt_m1.setTextSize(20);
				txt_m1.setGravity(1);
				txt_m1.setTypeface(null, 1);
				layout.addView(txt_m1);
				alert.setView(layout);
				alert.setPositiveButton("Okay",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
								alertCallback.onSubmit();
							}
						});
				alert.show();
			}
		});
	}
	
}
