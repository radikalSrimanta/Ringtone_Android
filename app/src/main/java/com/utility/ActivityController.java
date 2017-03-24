package com.utility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ActivityController {

	//public static List<Activity> activities;
	
	public static void startNextActivity(Activity activity,Class<?> clazz,boolean isCleareActivityStack){
		if(isCleareActivityStack){
			cleareActivityStack();
		}
		Intent intent = new Intent(activity,clazz);
		if(isCleareActivityStack){
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		}
		startAndTracActivity(activity, intent);
		activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
	}
	
	
	
	public static void startNextActivity(Activity activity,Class<?> clazz, Bundle bundle,boolean isCleareActivityStack ){
		if(isCleareActivityStack){
			cleareActivityStack();
		}
		Intent intent = new Intent(activity,clazz);
		if(isCleareActivityStack){
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		}
		if(bundle != null)
			intent.putExtras(bundle);
		startAndTracActivity(activity,intent);
	}
	
	public static void startNextActivityForResult(Activity activity,Class<?> clazz, Bundle bundle,boolean isCleareActivityStack, int requestCode){
		if(isCleareActivityStack){
			cleareActivityStack();
		}
		Intent intent = new Intent(activity,clazz);
		if(isCleareActivityStack){
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		}
		if(bundle != null){
			intent.putExtras(bundle);
		}
		activity.startActivityForResult(intent, requestCode);
		//activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
	}
	
	
	private static void startAndTracActivity(Activity activity,Intent intent){
		addActivityToStack(activity);
		activity.startActivity(intent);
		activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
	}
	
	private static void addActivityToStack(Activity activity){
//		if(activities == null){
//			activities = new ArrayList<Activity>();
//		}
//		activities.add(activity);
	}
	
	private static void cleareActivityStack(){
//		if(activities != null){
//			Iterator<Activity>iterator = activities.iterator();
//			while (iterator.hasNext()) {
//				System.out.println("clear activity stack");
//				Activity activity = iterator.next();
//				activity.finish();
//				iterator.remove();
//			}
//		}
	}
}
