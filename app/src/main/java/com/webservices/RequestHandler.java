package com.webservices;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;

import com.j256.ormlite.logger.Log;
import com.j256.ormlite.table.TableUtils;
import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.i_just_call_to_say.activities.greetings.UpdateUserDetailsActivity;
import com.i_just_call_to_say.activities.loginregistrartion.TermAndCondition;
import com.i_just_call_to_say.activities.splash.SplashActivity;
import com.i_just_call_to_say.dto.Contacts;
import com.utility.CustomDialogUtility;
import com.utility.DialogUtility;
import com.webservices.interfaces.IRequestCaller;
import com.webservices.interfaces.IServerResponse;

public abstract class RequestHandler implements IRequestCaller, IServerResponse {

	private Activity activity;

	/*public RequestHandler(Activity activity) {
		mActivity =activity;
	}*/
	public RequestHandler(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onFailure(String message,String errorCode) {
		try {
			showMessage(message,errorCode);
			System.out.println("error service"+message+errorCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showMessage(String message, String errorCode) {
		try {
			if (activity != null) {
				if (activity instanceof Activity) {
						CustomDialogUtility.showMessageWithOk(message,(Activity) activity);
						if (activity instanceof SplashActivity	|| activity instanceof TermAndCondition) {

						} else { if (errorCode.equals("530")) {	((RingToneBaseActivity) activity).logoutFromDevice();
								activity.finish();
							} else {

							}
						}
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

  


}
