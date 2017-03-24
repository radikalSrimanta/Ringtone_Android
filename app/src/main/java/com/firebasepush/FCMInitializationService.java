package com.firebasepush;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Arpan on 11/11/2016.
 */

public class FCMInitializationService extends FirebaseInstanceIdService {
    private static final String TAG = "FCMInitializationService";

    @Override
    public void onTokenRefresh() {
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        RingToneBaseApplication.device_token =fcmToken ;
        sendRegistrationToServer(fcmToken);
      /*  System.out.println("FCM Device Token:" + fcmToken);
        try {
            JSONObject jaa=new JSONObject(fcmToken);
             String d_token=jaa.getString("token");
            System.out.println("FCM Device Token:" + d_token);
            RingToneBaseApplication.device_token =d_token ;
            //RingToneBaseApplication.device_token=d_token;
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        //PreferenceManager.getDefaultSharedPreferences(this).edit().putString(PreferenceData.SNAPP, fcmToken).commit();
        //Save or send FCM registration token
    }

    private void sendRegistrationToServer(String fcmToken) {

    }
}