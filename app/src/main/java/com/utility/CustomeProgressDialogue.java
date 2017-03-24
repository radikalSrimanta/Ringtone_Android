package com.utility;

import com.i_just_call_to_say.R;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.view.Window;
import android.widget.RelativeLayout;

public class CustomeProgressDialogue {
	private  int counter=0;
	private Handler handler=new Handler();
	private Activity activity;
	private Dialog dialog;
	private Runnable mRunnable;
	
	private final int[] image_name = { R.drawable.animation0001,
			R.drawable.animation0002, R.drawable.animation0003,
			R.drawable.animation0004, R.drawable.animation0005,
			R.drawable.animation0006, R.drawable.animation0007,
			R.drawable.animation0008, R.drawable.animation0009,
			R.drawable.animation0010, R.drawable.animation0011,
			R.drawable.animation0012, R.drawable.animation0013,
			R.drawable.animation0014, R.drawable.animation0015,
			R.drawable.animation0016, R.drawable.animation0017,
			R.drawable.animation0018, R.drawable.animation0019,
			R.drawable.animation0020, R.drawable.animation0021,
			R.drawable.animation0022, R.drawable.animation0023,
			R.drawable.animation0024, R.drawable.animation0025,
			R.drawable.animation0026, R.drawable.animation0027,
			R.drawable.animation0028, R.drawable.animation0029,
			R.drawable.animation0030, R.drawable.animation0031,
			R.drawable.animation0032, R.drawable.animation0033,
			R.drawable.animation0034, R.drawable.animation0035,
			R.drawable.animation0036, R.drawable.animation0037,
			R.drawable.animation0038, R.drawable.animation0039,
			R.drawable.animation0040, R.drawable.animation0041,
			R.drawable.animation0042, R.drawable.animation0043,
			R.drawable.animation0044, R.drawable.animation0045,
			R.drawable.animation0046, R.drawable.animation0047,
			R.drawable.animation0048, R.drawable.animation0049,
			R.drawable.animation0050, R.drawable.animation0051,};
	
	
	public CustomeProgressDialogue(Activity activity){
		this.activity = activity;
	}
	
	
	public  void show() {
		dialog = new Dialog(activity,R.style.picChooserTheme);
		final int SLEEP_TIME = 0;
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custome_progress_dialogue);
		dialog.setCancelable(false);
		
		final RelativeLayout rl_1=(RelativeLayout) dialog.findViewById(R.id.rl_1);
		
		mRunnable = new Runnable() {
			@Override
			public void run() {
				rl_1.setBackgroundResource(image_name[counter]);
				counter++;	
				if (counter>=image_name.length) {
					
					counter=0;
				}
				handler.postDelayed(this, SLEEP_TIME);
			}
		};
		handler.postDelayed(mRunnable, SLEEP_TIME);
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog.show();
			}
		});
		
	}
	
	public  void dismiss(){
		if (dialog!=null && dialog.isShowing()) {
			handler.removeCallbacks(mRunnable);
			dialog.dismiss();
		}
	}
}
