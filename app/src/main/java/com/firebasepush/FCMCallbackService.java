package com.firebasepush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.about.AboutRingToneActivity;
import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.contacts.RingtoneDownloadService;
import com.i_just_call_to_say.activities.splash.SplashActivity;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.ReceivedGreetingsCard;
import com.i_just_call_to_say.dto.table_query_manager.UserTableManager;
import com.isis.module.push_notification.utility.PushUtility;
import com.utility.PreferenceUtility;

import java.util.Map;
import java.util.Random;

/**
 * Created by Arpan on 11/11/2016.
 */

public class FCMCallbackService extends FirebaseMessagingService {
    private static final String TAG = "FCMCallbackService";
    public static String NOTIFICATION_TYPE = "notification_type";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Log.d(TAG, "From:" + remoteMessage.getNotification().getBody());
//        System.out.println("notification frimfirebase 11"+remoteMessage.getNotification().getBody() );
        //Log.d(TAG, "Message Body:" + remoteMessage.getNotification().getBody());
     /*   Map<String, String> data = remoteMessage.getData();
        String body = data.get("body");
        String notification_for=data.get("notification_for");*/
       // sendNotification(remoteMessage.getNotification());
        sendNotification(remoteMessage);
    }

   /* private void sendNotification(RemoteMessage.Notification notification) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(this, FCMCallbackService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        System.out.println("notification frimfirebase"+notification.toString()+notification.getBody());

        *//*Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
              //  .setSmallIcon(getNotificationIcon())
                .setLargeIcon(largeIcon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notification.getBody()))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());*//*
    }
*/

//    private int getNotificationIcon() {
//        boolean useSilhouette = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
//      //  return useSilhouette ? R.drawable.snap_black_white : R.drawable.ic_launcher;
//    }



   /* private void sendNotification(RemoteMessage.Notification notification) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(this, FCMCallbackService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        System.out.println("notification frimfirebase"+notification.toString()+notification.getBody());
        String title = notification.getTitle();
        String messag=notification.getBody();
        String msg_split[] = messag.split("@#@");
        String message = msg_split[0];
        System.out.println("notification frimfirebase 14"+message);
        System.out.println("notification frimfirebase15"+ msg_split[1]);
        Bundle bundle = new Bundle();
        try {
            Gson gson = new Gson();
            bundle.putString(NOTIFICATION_TYPE, msg_split[1].trim());
            if (msg_split[1].trim().equalsIgnoreCase("greetingcard")) {
                ReceivedGreetingsCard receivedGreetingsCard = gson.fromJson(msg_split[2], ReceivedGreetingsCard.class);
                bundle.putSerializable("receive_greeting", receivedGreetingsCard);
                PushUtility.showNotificationWithCustom(getApplicationContext(), SplashActivity.class, bundle, R.drawable.ic_launcher, title, message);


            } else if (msg_split[1].trim().equalsIgnoreCase("ringtone")) {
                System.out.println("phone number " + msg_split[2]);
                Intent serviceIntent = new Intent(getApplicationContext(), RingtoneDownloadService.class);
                serviceIntent.putExtra("contact_number", msg_split[2]);
                getApplicationContext().startService(serviceIntent);
				PushUtility.showNotificationWithCustom(getApplicationContext(), SplashActivity.class, bundle,	R.drawable.ic_launcher, title, message);

            } else if (msg_split[1].trim().equalsIgnoreCase("updateapp")) {
                try {
                    RingToneBaseActivity mActivity = (RingToneBaseActivity) RingToneBaseApplication.currentActivity;
                    if (mActivity != null) {
						*//*UserTableManager.updateUserStatus(RingToneBaseActivity.user.getAccess_token(),
									//	mActivity.ringToneBaseApplication.databaseManager,
									//	mActivity, "true");*//*
                        RingToneBaseActivity.user = UserTableManager.getSavedUser(mActivity, mActivity.ringToneBaseApplication);
                        RingToneBaseActivity.user.setPurchase_status("true");
                        PreferenceUtility.saveObjectInAppPreference(mActivity, RingToneBaseActivity.user, PreferenceUtility.USER_PROFILE);
                        mActivity.initAdView();
                        if (mActivity instanceof AboutRingToneActivity) {
                            ((AboutRingToneActivity) mActivity).btn_upgrade.setVisibility(View.GONE);
                        }
                    }
                    Contacts contacts = gson.fromJson(msg_split[2], Contacts.class);
                    bundle.putSerializable("contacts", contacts);
                    PushUtility.showNotificationWithCustom(getApplicationContext(), SplashActivity.class, bundle, R.drawable.ic_launcher, title, message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
       *//* String msg=msg_split[1].trim();
        System.out.println("notification frimfirebase 16"+msg);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(notification.getTitle())
                .setContentText( msg)
                .setAutoCancel(true)
                .setSmallIcon(getNotificationIcon())
                //  .setLargeIcon(largeIcon)
                //  .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        System.out.println("notification frimfirebase"+notification.getTitle());
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());*//*
    }*/
    private void sendNotification(RemoteMessage notification) {
        Map<String, String> data = notification.getData();
        String body = data.get("body");
        System.out.println("phone number " + body);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(this, FCMCallbackService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

      //  System.out.println("notification frimfirebase"+notification.toString()+notification.getBody());
//        String title = notification.getTitle();
//        String messag=notification.getBody();
        String msg_split[] = body.split("@#@");
        String message = msg_split[0];
        System.out.println("notification frimfirebase 14"+message);
        System.out.println("notification frimfirebase15"+ msg_split[1]);
        Bundle bundle = new Bundle();
        try {
            Gson gson = new Gson();
            bundle.putString(NOTIFICATION_TYPE, msg_split[1].trim());
            if (msg_split[1].trim().equalsIgnoreCase("greetingcard")) {
                ReceivedGreetingsCard receivedGreetingsCard = gson.fromJson(msg_split[2], ReceivedGreetingsCard.class);
                bundle.putSerializable("receive_greeting", receivedGreetingsCard);
               // PushUtility.showNotificationWithCustom(getApplicationContext(), SplashActivity.class, bundle, R.drawable.ic_launcher, "Ringtone", message);
                showpush(getApplicationContext(), SplashActivity.class, bundle, R.drawable.ic_launcher, "greetingcard", message);

            } else if (msg_split[1].trim().equalsIgnoreCase("ringtone")) {
                System.out.println("phone number " + msg_split[2]);
                Intent serviceIntent = new Intent(getApplicationContext(), RingtoneDownloadService.class);
                serviceIntent.putExtra("contact_number", msg_split[2]);
                Log.e("Service", "Start1");
                getApplicationContext().startService(serviceIntent);
              //  PushUtility.showNotificationWithCustom(getApplicationContext(), SplashActivity.class, bundle,	R.drawable.ic_launcher,"Ringtone", message);
                //showpush(getApplicationContext(), SplashActivity.class, bundle,	R.drawable.ic_launcher,"Ringtone", message);

            } else if (msg_split[1].trim().equalsIgnoreCase("updateapp")) {
                try {
                    RingToneBaseActivity mActivity = (RingToneBaseActivity) RingToneBaseApplication.currentActivity;
                    if (mActivity != null) {
						/*UserTableManager.updateUserStatus(RingToneBaseActivity.user.getAccess_token(),
									//	mActivity.ringToneBaseApplication.databaseManager,
									//	mActivity, "true");*/
                        RingToneBaseActivity.user = UserTableManager.getSavedUser(mActivity, mActivity.ringToneBaseApplication);
                        RingToneBaseActivity.user.setPurchase_status("true");
                        PreferenceUtility.saveObjectInAppPreference(mActivity, RingToneBaseActivity.user, PreferenceUtility.USER_PROFILE);
                        mActivity.initAdView();
                        if (mActivity instanceof AboutRingToneActivity) {
                            ((AboutRingToneActivity) mActivity).btn_upgrade.setVisibility(View.GONE);
                        }
                    }
                    Contacts contacts = gson.fromJson(msg_split[2], Contacts.class);
                    bundle.putSerializable("contacts", contacts);
                   // PushUtility.showNotificationWithCustom(getApplicationContext(), SplashActivity.class, bundle, R.drawable.ic_launcher, "Ringtone", message);
                    showpush(getApplicationContext(), SplashActivity.class, bundle, R.drawable.ic_launcher, "updateapp", message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
       /* String msg=msg_split[1].trim();
        System.out.println("notification frimfirebase 16"+msg);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(notification.getTitle())
                .setContentText( msg)
                .setAutoCancel(true)
                .setSmallIcon(getNotificationIcon())
                //  .setLargeIcon(largeIcon)
                //  .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        System.out.println("notification frimfirebase"+notification.getTitle());
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());*/
    }

    private void showpush(Context context,final Class<?> mActivityClass, Bundle bundle,final int icon, final String title, final String message) {
            System.out.println("call push page");
            String ns = Context.NOTIFICATION_SERVICE;

            NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(ns);
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
//            View contentView = inflater.inflate(R.layout.custom_notification_layout, null);

            RemoteViews contentView = new RemoteViews(getApplicationContext().getPackageName(),
                    com.isis.module.push_notification.R.layout.custom_notification_layout);
//            TextView tv_PushTitle=(TextView) contentView.findViewById(R.id.tv_PushTitle);
//            TextView tv_PushMessage=(TextView) contentView.findViewById(R.id.tv_PushMessage);
//            ImageView iv_PushICON=(ImageView)contentView.findViewById(R.id.iv_PushICON);
            contentView.setTextViewText(R.id.tv_PushTitle, title);
            contentView.setTextViewText(com.isis.module.push_notification.R.id.tv_PushMessage, message);
            contentView.setImageViewResource(com.isis.module.push_notification.R.id.iv_PushICON, icon);
//           tv_PushTitle.setText(title);
//           tv_PushMessage.setText(title);
//           iv_PushICON.setImageResource(icon);

            System.out.println("call push page 1");
            CharSequence tickerText = message;
            long when = System.currentTimeMillis();

            Notification notification = new Notification(icon, tickerText, when);
            notification.contentView = contentView;

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            long[] pattern = { 500, 500, 500, 500, 500, 500, 500, 500, 500 };
            notification.vibrate = pattern;
            notification.sound = alarmSound;
            Intent notificationIntent = new Intent(getApplicationContext(), mActivityClass);
            notificationIntent.putExtras(bundle);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.contentIntent = contentIntent;
            Random randInt = new Random();
            int id = randInt.nextInt(100) - 1;
            System.out.println("call push page 2");
            mNotificationManager.notify(10, notification);
        /*Intent intent= new Intent(this, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("gsdg")
                .setContentText( "sdf")
                .setAutoCancel(true)
                .setSmallIcon(getNotificationIcon())
                //  .setLargeIcon(largeIcon)
                //  .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
              //  .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
      //  System.out.println("notification frimfirebase"+notification.getTitle());
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());*/
    }

    private int getNotificationIcon() {
        boolean useSilhouette = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        return useSilhouette ? R.drawable.ic_launcher : R.drawable.ic_launcher;
    }
}
