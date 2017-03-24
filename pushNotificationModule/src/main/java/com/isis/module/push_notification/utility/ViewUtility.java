package com.isis.module.push_notification.utility;

import android.app.Activity;
import android.view.View;

public class ViewUtility {

	public static View ViewgetLayoutView(Activity activity,int layout_id) {
		View v = null;
		v = View.inflate(activity, layout_id, null);
		return v;
	}

	public static View findViewByName(Activity activity,String viewName) {
		int id = activity.getResources().getIdentifier(viewName, "id", activity.getPackageName());
		View view = activity.findViewById(id);
		return view;
	}
}
