package com.utility;





import com.i_just_call_to_say.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

public class DialogUtility {
	
	public static void showMessageWithOk(final String message,
			final Context mActivity) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
		dialog.setMessage(message);
		dialog.setTitle(R.string.app_name);
		dialog.setNeutralButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	public static void showMessageWithOk(final String message,
			final Activity mActivity) {
		try {
			if(mActivity!=null){
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
					dialog.setMessage(message);
					dialog.setTitle(R.string.app_name);
					dialog.setNeutralButton("OK", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					dialog.show();
				}
			});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showMessageWithOk(final String title,final String message,
			final Activity mActivity) {
		try {
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
					dialog.setMessage(message);
					dialog.setTitle(title);
					dialog.setNeutralButton("OK", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					dialog.show();
				}
			});
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	public static void showMessageOkWithCallback(String message,
			Activity mContext, final AlertDialogCallBack callback) {
		final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setMessage(message);
		dialog.setCancelable(false);
		dialog.setTitle(R.string.app_name);
		dialog.setNegativeButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onSubmit();
			}
		});
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog.show();
			}
		});
	}
	
	public static void showMessageWithOkCancelCallback(String message,
			Activity mContext, final AlertDialogCallBack callback) {
		final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setMessage(message);
		dialog.setCancelable(false);
		dialog.setTitle(R.string.app_name);
		
		dialog.setPositiveButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onCancel();
				
			}
		});
		
		dialog.setNegativeButton("Ok", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onSubmit();
			}
		});
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog.show();
			}
		});
	}
	
	public static void showMessageWithOkCancelCallback(String message,
			Context mContext, final AlertDialogCallBack callback) {
		final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setMessage(message);
		dialog.setCancelable(false);
		dialog.setTitle(R.string.app_name);
		
		dialog.setPositiveButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onCancel();
				
			}
		});
		
		dialog.setNegativeButton("Ok", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onSubmit();
			}
		});
		((Activity) mContext).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog.show();
			}
		});
	}

	public static void showCallbackMessage(String message, Activity mContext,
			final AlertDialogCallBack callback) {
		final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setMessage(message);
		dialog.setTitle(R.string.app_name);
		dialog.setNegativeButton("No", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onCancel();
				
			}
		});

		dialog.setPositiveButton("Yes", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onSubmit();
			}
		});

		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog.show();
			}
		});
	}
	
	public static void showCallbackMessageWithEdittext(String message, Activity mContext,
			final AlertDialogCallBack callback) {
		final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setMessage(message);
		dialog.setTitle(R.string.app_name);
		final EditText edi_text=new EditText(mContext);
		dialog.setView(edi_text);
		
		
		dialog.setNegativeButton("No", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onCancel();
			
			}
		});

		dialog.setPositiveButton("Yes", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onSubmitWithEditText(edi_text.getText().toString());
			}
		});

		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog.show();
			}
		});
	}
	
	public static void showCallbackMessageWithEdittext(String message, Activity mContext,int inputtype,
			final AlertDialogCallBack callback) {
		final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setMessage(message);
		dialog.setTitle(R.string.app_name);
		final EditText edi_text=new EditText(mContext);
		edi_text.setInputType(inputtype);
		dialog.setView(edi_text);
		
		
		dialog.setNegativeButton("Yes", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onSubmitWithEditText(edi_text.getText().toString());
				
				
			}
		});

		dialog.setPositiveButton("No", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onCancel();
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
