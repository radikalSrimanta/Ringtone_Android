package com.i_just_call_to_say.activities.splash;

import java.util.List;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.isis.module.push_notification.ErrorBroadcastReceiver;
import com.isis.module.push_notification.ErrorBroadcastReceiver.OnRegisterErrorListener;
import com.isis.module.push_notification.GCMSplashActivity;
import com.isis.module.push_notification.constants.Constants;
import com.isis.module.push_notification.interfaces.IPushResponce;
import com.isis.module.push_notification.utility.PushUtility;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.AppConstant;
import com.i_just_call_to_say.activities.base.MyMessageBroadcastReceiver;
import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.contacts.ContactsActivity;
import com.i_just_call_to_say.activities.contacts.DownloadService;
import com.i_just_call_to_say.activities.contacts.InitializeBackGroundService;
import com.i_just_call_to_say.activities.greetings.ViewReceiveGreetingActivity;
import com.i_just_call_to_say.activities.loginregistrartion.BrowserActivity;
import com.i_just_call_to_say.activities.loginregistrartion.LoginApiManager;
import com.i_just_call_to_say.activities.loginregistrartion.RegistrationActivity;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.ReceivedGreetingsCard;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.table_query_manager.UserTableManager;
import com.isis.module.push_notification.utility.Utility;
import com.utility.ActivityController;
import com.utility.AlertDialogCallBack;
import com.utility.CommonUtility;
import com.utility.CustomDialogUtility;
import com.utility.DialogUtility;
import com.utility.PreferenceUtility;
import com.utility.orm_utility.Orm_SQLManager;
import com.utility.view.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends GCMSplashActivity implements IPushResponce, OnRegisterErrorListener {
    private RingToneBaseApplication ringToneBaseApplication;
    private static final int SLEEP_TIME = 1000;
    private String deviceid;
    private String devicetype = "A";
    private List<Contacts> db_contact_list;
    private User user;
    private ProgressBar pb_validating;
    private CustomTextView tv_1;
    //	private String longUrl;
    private ErrorBroadcastReceiver broadcastReceiver;
    public static String d_token;
    private int PERMISSION_REQUEST_INTERNET = 112;
    private int PERMISSION_REQUEST_WRITE_SETTINGS = 116;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String json_token = FirebaseInstanceId.getInstance().getToken();
        RingToneBaseApplication.device_token = json_token;
        System.out.println("FCM Device Token:" + json_token);
        ringToneBaseApplication = (RingToneBaseApplication) getApplication();
        broadcastReceiver = new ErrorBroadcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(com.isis.module.push_notification.constants.Constants.CUSTOM_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
        String GCM_SENDERID = getString(R.string.gcm_senderID);
        //String deviceID = Utility.getDeviceID(this);
        //Constants.iPushResponce.sendTokenToServer(d_token, deviceID);
//		PushUtility.saveC2DMRegistrationInfo(this,"APA91bEWh9k81qYi1pfeshQuvZQ2lBIKItUdxDIjq7M_TLZz68mAm0x_mpZfJEyZPRwmxDZO8KZKN8hP5OpXc1e3LhZ-TPmXzj0O8-kVApskK_A1atTqxYO9VPz5ZCCbYX0B2Gmwf8FM");
        try {
            initView(R.layout.splash, SplashActivity.class, this, SLEEP_TIME, GCM_SENDERID);
            pb_validating = (ProgressBar) findViewById(R.id.pb_validating);
            tv_1 = (CustomTextView) findViewById(R.id.tv_1);

            //Constants.iPushResponce.sendTokenToServer(d_token, deviceID);
        } catch (Exception e) {
            CustomDialogUtility.showCallbackMessageWithOk(AppConstant.SERVER_ERROR_MESSAGE, SplashActivity.this, new AlertDialogCallBack() {

                @Override
                public void onSubmitWithEditText(String text) {
                }

                @Override
                public void onSubmit() {
                    SplashActivity.this.finish();
                }

                @Override
                public void onCancel() {
                }
            });
        }

        checkinternetpermession();
    }

    private void checkinternetpermession() {
        Log.e("Permission", "reached");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.e("Permission", "reached1");

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                readFromBundle();

            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 111);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.

                }
            }

        }else{
            readFromBundle();
        }
    }

    private boolean isOpenBrowserActivity() {

        if (getIntent() == null || getIntent().getData() == null)
            return false;
        else if (getIntent().getData() != null) {
            String msg_url = getIntent().getData().toString();
            // String msg_url = longUrl;
            System.out.println(" msg_url " + msg_url);
            if (msg_url == null)
                return false;
            else if (msg_url.indexOf("connection_accept") > 0)
                return false;
            else
                return true;
        } else
            return true;
    }

    private void readFromBundle() {
        /*String GCM_SENDERID = getString(R.string.gcm_senderID);
        Constants.GCM_SENDER_ID =GCM_SENDERID ;
        Constants.iPushResponce = (IPushResponce) this;
        PushUtility pushUtility = new PushUtility(this);
        pushUtility.C2dmRegistration();*/
        if (isOpenBrowserActivity()) {
            Bundle bundle = new Bundle();
            bundle.putString("url", getIntent().getData().toString());
//			bundle.putString("url", longUrl);
            ActivityController.startNextActivity(this, BrowserActivity.class, bundle, true);
        } else {
            user = UserTableManager.getSavedUser(this, ringToneBaseApplication);
            //System.out.println("user " + user.getUser_name());
            if (user == null) {
                System.out.println("user " + user);
                ActivityController.startNextActivity(this, RegistrationActivity.class, true);
                finish();
            } else {
                //login_callService(RingToneBaseApplication.device_token);
                login_callService(RingToneBaseApplication.device_token);
            }
        }
    }

    @Override
    public void gotoNextActivity() {
        //RingToneBaseApplication.device_token = PushUtility.fetchC2DMToken(this);

        /*try {

			JSONObject jaa=new JSONObject(json_token);
			d_token=jaa.getString("token");
			System.out.println("FCM Device Token:" + d_token);
			RingToneBaseApplication.device_token = d_token;
			//RingToneBaseApplication.device_token=d_token;
		} catch (JSONException e) {
			e.printStackTrace();
		}*/

        System.out.println("------------gotoNextActivity------------");
//		if(getIntent().getData()!=null){
//			LargeUrl largeUrl = new LargeUrl(SplashActivity.this,getIntent().getData().toString(), new URLShortenerListener() {
//				
//				@Override
//				public void onComplete(String url) {
//					longUrl = url;
//					readFromBundle();
//				}
//			});
//			largeUrl.execute();
//		}else
//		readFromBundle();
    }


    private void login_callService(String deviceToken) {

        if (CommonUtility.checkConnectivity(this)) {
            pb_validating.setVisibility(View.VISIBLE);
            tv_1.setVisibility(View.VISIBLE);
            String name = user.getUser_name();
            String mob_no = user.getPhone_number();
            String country_code = user.getCountry_code();
            String deviceType = "A";
            Object[] values = {name, country_code, mob_no, deviceToken, deviceType};
            user.setDeviceToken(deviceToken);
            System.out.println("user " + name + country_code + mob_no + deviceType + deviceToken);
            //user.setAccess_token(deviceToken);
            //Orm_SQLManager.insertIntoTable(User.class, user, this, ringToneBaseApplication.databaseManager);
            PreferenceUtility.saveObjectInAppPreference(this, user, PreferenceUtility.USER_PROFILE);
            LoginApiManager loginApiManager = new LoginApiManager(this, values, ringToneBaseApplication, "Validating User...    ");
            System.out.println("user 2");
        } else {
            CustomDialogUtility.showCallbackMessageWithOk(AppConstant.SERVER_ERROR_MESSAGE, this, new AlertDialogCallBack() {

                @Override
                public void onSubmitWithEditText(String text) {
                }

                @Override
                public void onSubmit() {
                    ActivityController.startNextActivity(SplashActivity.this, RegistrationActivity.class, true);
                    finish();
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    public void login_serviceCallBackOnSuccess() {
        System.out.println("user login");
        pb_validating.setVisibility(View.GONE);
        tv_1.setVisibility(View.GONE);
        ReceivedGreetingsCard receivedGreetingsCard = null;
        Contacts contacts = null;
        String notification_type = "";
        try {
            notification_type = getIntent().getStringExtra(MyMessageBroadcastReceiver.NOTIFICATION_TYPE).trim();
            receivedGreetingsCard = (ReceivedGreetingsCard) getIntent().getExtras().getSerializable("receive_greeting");
            contacts = (Contacts) getIntent().getExtras().getSerializable("contacts");
        } catch (Exception e) {
        }
        if (receivedGreetingsCard != null && notification_type.equalsIgnoreCase("greetingcard")) {
            System.out.println("user login1");
            ActivityController.startNextActivity(this, ViewReceiveGreetingActivity.class, getIntent().getExtras(), true);
        } else if (receivedGreetingsCard != null && notification_type.equalsIgnoreCase("ringtone")) {
            System.out.println("user login2");
            ActivityController.startNextActivity(this, ContactsActivity.class, getIntent().getExtras(), true);
        } else if (contacts != null && notification_type.equalsIgnoreCase("updateapp")) {
            System.out.println("user login3");
            ActivityController.startNextActivity(this, ContactsActivity.class, getIntent().getExtras(), true);
        } else {
            System.out.println("user login4");
            Bundle bundle = new Bundle();
            bundle.putString(MyMessageBroadcastReceiver.NOTIFICATION_TYPE, "invitation");
            bundle.putString("url", getIntent().getDataString());
//			bundle.putString("url", longUrl);
            ActivityController.startNextActivity(this, ContactsActivity.class, bundle, true);
        }


        if (!RingToneBaseActivity.isMyServiceRunning(SplashActivity.this)) {
            System.out.println("---   start service -----------");
            Intent intent = new Intent(SplashActivity.this, InitializeBackGroundService.class);
            startService(intent);
        } else {
            System.out.println("---   start already started service --------------");
        }

        finish();
    }

//	
//	public void login_serviceCallBackOnSuccess(){
//		pb_validating.setVisibility(View.GONE);
//		tv_1.setVisibility(View.GONE);
//		Bundle bundle = new Bundle();
//		bundle.putString("url", getIntent().getDataString());
//		ActivityController.startNextActivity(this, ContactsActivity.class,bundle, true);
//		finish();
//	}

    public void login_serviceCallBackOnFaluire(String msg) {

        pb_validating.setVisibility(View.GONE);
        tv_1.setVisibility(View.GONE);
        CustomDialogUtility.showCallbackMessageWithOk(msg, this, new AlertDialogCallBack() {
            @Override
            public void onSubmitWithEditText(String text) {

            }

            @Override
            public void onSubmit() {
                ActivityController.startNextActivity(SplashActivity.this, RegistrationActivity.class, true);
                finish();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void sendTokenToServer(String deviceToken, String deviceID) {

    }

    @Override
    public void onRegisterError() {
        CustomDialogUtility.showCallbackMessageWithOk(AppConstant.SERVER_ERROR_MESSAGE, SplashActivity.this, new AlertDialogCallBack() {

            @Override
            public void onSubmitWithEditText(String text) {
            }

            @Override
            public void onSubmit() {
                SplashActivity.this.finish();
            }

            @Override
            public void onCancel() {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 111) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.GET_ACCOUNTS},
                            10);

                }
            }

        } else if (requestCode == 10) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {


                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.SEND_SMS},
                            1111);


                }

            }

        } else if (requestCode == 1111) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            1112);
                }

            }
        } else if (requestCode == 1112) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1113);
                }
            }
        } else if (requestCode == 1113) {
            Log.e("Step","1");
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1114);
                }else{
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_CONTACTS},
                                1115);
                    }else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_CONTACTS},
                                1116);
                    }else{
                        readFromBundle();
                    }

                }
            }

        } else if (requestCode == 1114) {
            Log.e("Step","2");
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            1115);
                }else{
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_CONTACTS},
                                1116);
                    }else{
                        readFromBundle();
                    }
                }
            }

        } else if (requestCode == 1115) {
            Log.e("Step","3");
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_CONTACTS},
                            1116);
                }else{
                    readFromBundle();
                }
            }

        } else if (requestCode == 1116) {
            Log.e("Step","4");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFromBundle();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
