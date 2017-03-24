package com.i_just_call_to_say.activities.base;

import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.about.AboutRingToneActivity;
import com.i_just_call_to_say.activities.connections.ConnectionsActivity;
import com.i_just_call_to_say.activities.contacts.ContactsActivity;
import com.i_just_call_to_say.activities.contacts.InitializeBackGroundService;
import com.i_just_call_to_say.activities.greetings.GreetingsActivity;
import com.i_just_call_to_say.activities.help.HelpActivity;
import com.i_just_call_to_say.activities.loginregistrartion.RegistrationActivity;
import com.i_just_call_to_say.dto.User;
import com.i_just_call_to_say.dto.table_query_manager.UserTableManager;
import com.imageLoder.ImageLoader;
import com.j256.ormlite.table.TableUtils;
import com.slidingmenu.lib.SlidingMenu;
import com.utility.ActivityController;
import com.utility.view.CustomButton;
import com.utility.view.CustomEditText;
import com.utility.view.CustomTextView;

public class RingToneBaseActivity extends Activity {

	public RingToneBaseApplication ringToneBaseApplication;
	private SlidingMenu slidingMenu;
	private CustomTextView tv_header;
	private CustomEditText et_search_box;
	public Button btn_header_people, btn_arrow,btn_back;
	public CustomButton btn_distrust;
	public Button btn_menu;
	private View view;
	public static User user;
	public static final int CONNECTIONS = 1;
	public static final int GREETINGS = 2;
	public static final int ABOUT = 3;
	public static final int GET_GREETINGS = 4;
	public static final int GET_SEND_GREETINGS = 5;
	public static final int CONTACT = 6;
	public static final int HELP = 7;
	public static final int SEND_CONTACT = 8;
	public static final int WEB_ACCOUNT_ACTIVATION = 9;
	public static final int UPDATE_USER_DETAILS = 10;
	public static final int VIEW_RECEIVE_GREETING = 11;
	public RelativeLayout ad_layout;
	public ImageLoader imageLoader;
	private int[] conactImage = { R.drawable.contact_0001,
			R.drawable.contact_0002, R.drawable.contact_0003,
			R.drawable.contact_0004, R.drawable.contact_0005, 
			R.drawable.contact_0006,R.drawable.contact_0007,
			R.drawable.contact_0008,R.drawable.contact_0009,
			R.drawable.contact_0010,R.drawable.contact_0011,
			R.drawable.contact_0012,R.drawable.contact_0013,
			R.drawable.contact_0014};
	
	private Handler mHandler;
	private Runnable mRunnable;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RingToneBaseApplication.currentActivity = this;
		ringToneBaseApplication = (RingToneBaseApplication) getApplication();
		user = UserTableManager.getSavedUser(this, ringToneBaseApplication);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ringToneBaseApplication = (RingToneBaseApplication) getApplication();
		user = UserTableManager.getSavedUser(this, ringToneBaseApplication);
	}
	
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		setSlidingMenu();
	}

	private void setSlidingMenu() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = getDisplaySize(display);
		int width = size.x;
		// int height = size.y;

		float screen_width = getSliderWidth();

		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setBehindOffset((int) ((float) width * screen_width));
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ViewGroup myRoot = (ViewGroup) getWindow().getDecorView();
		view = layoutInflater.inflate(R.layout.sliding_menu, myRoot, false);

		slidingMenu.setMenu(view);

	}

	public void initBaseView() {
		tv_header = (CustomTextView) findViewById(R.id.tv_header);
		btn_header_people = (Button) findViewById(R.id.btn_header_people);
		btn_arrow = (Button) findViewById(R.id.btn_arrow);
		et_search_box = (CustomEditText) findViewById(R.id.et_search_box);
		tv_header.setVisibility(View.VISIBLE);
		btn_menu = (Button) findViewById(R.id.btn_menu);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_distrust = (CustomButton) findViewById(R.id.btn_distrust);
	}
	
	int i = 0;
	private void setAnimation(){
		final int delay = 50;
		if(mHandler == null)
		mHandler = new Handler();
		if(mRunnable == null)
		mRunnable = new Runnable() {
			
			@Override
			public void run() {
				btn_header_people.setBackgroundResource(conactImage[i]);	
				i++;
				if(i>=conactImage.length){
					i=0;
				}
				mHandler.postDelayed(mRunnable, delay);
			}
		};
		mHandler.postDelayed(mRunnable, delay);
	}
	
	private void resetAnimation(){
		if(mRunnable != null && mHandler !=null ){
			mHandler.removeCallbacks(mRunnable);
			mHandler = null ;
			mRunnable = null;
		}
	}
	
	public void initAdView(){
		
//		System.out.println("ad_layout " +ad_layout);
//		if (user.getPurchase_status().equalsIgnoreCase("true")) {
//			ad_layout.setVisibility(View.GONE);
//		}
		ad_layout = (RelativeLayout) findViewById(R.id.include_02);
		if(user.getPurchase_status().equalsIgnoreCase("false")){
		AdView adView = (AdView) findViewById(R.id.ad_view);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		}
		else{
			ad_layout.setVisibility(View.GONE);
		}
	}
	
	public static boolean isMyServiceRunning(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (InitializeBackGroundService.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	
	public void setTabSelected(int tab, String header) {
			resetAnimation();
		if(tab!= WEB_ACCOUNT_ACTIVATION )
			initAdView();
		
		switch (tab) {
		case CONNECTIONS:
			((RadioButton) view.findViewById(R.id.btn_connections)).setChecked(true);
			tv_header.setText(header);
			btn_menu.setVisibility(View.VISIBLE);
			btn_header_people.setVisibility(View.VISIBLE);
			setAnimation();
			break;
		case GREETINGS:
			((RadioButton) view.findViewById(R.id.btn_greetings)).setChecked(true);
//			btn_menu.setVisibility(View.VISIBLE);
			tv_header.setText(header);
			break;
		case ABOUT:
			((RadioButton) view.findViewById(R.id.btn_about)).setChecked(true);
			tv_header.setText(header);
			btn_menu.setVisibility(View.VISIBLE);
			break;
		case GET_GREETINGS:
			tv_header.setText(header);
			btn_menu.setVisibility(View.GONE);
			btn_back.setVisibility(View.VISIBLE);
			break;
		case GET_SEND_GREETINGS:
			tv_header.setText(header);
			btn_menu.setVisibility(View.GONE);
			btn_back.setVisibility(View.VISIBLE);
			btn_arrow.setVisibility(View.VISIBLE);
			break;
		case CONTACT:
			((RadioButton) view.findViewById(R.id.btn_connections)).setChecked(true);
			tv_header.setText(header);
			btn_back.setVisibility(View.VISIBLE);
			break;
		case HELP:
			((RadioButton) view.findViewById(R.id.btn_help)).setChecked(true);
			tv_header.setText(header);
			btn_menu.setVisibility(View.VISIBLE);
			break;
		case SEND_CONTACT:
			((RadioButton) view.findViewById(R.id.btn_greetings)).setChecked(true);
			btn_back.setVisibility(View.VISIBLE);
			tv_header.setText(header);
			break;
		case WEB_ACCOUNT_ACTIVATION:
			btn_back.setVisibility(View.VISIBLE);
			tv_header.setText(header);
			break;
		case UPDATE_USER_DETAILS:
			btn_back.setVisibility(View.VISIBLE);
			tv_header.setText(header);
			btn_distrust.setVisibility(View.VISIBLE);
			break;
			
		case VIEW_RECEIVE_GREETING:
			((RadioButton) view.findViewById(R.id.btn_greetings)).setChecked(true);
			break;

		default:
			break;
		}
		
	}

	 public void logoutFromDevice(){
		try {
			TableUtils.clearTable(ringToneBaseApplication.databaseManager.getHelper(getApplicationContext()).getConnectionSource(),User.class);
			ActivityController.startNextActivity(this, RegistrationActivity.class, true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  }

	
	@SuppressLint("NewApi")
	public static Point getDisplaySize(final Display display) {
		final Point point = new Point();
		try {
			display.getSize(point);
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		return point;
	}

	private float getSliderWidth() {
		float wdith = 0.7f;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		if (metrics.densityDpi <= 240) {
			wdith = 0.67f;
		} else if (metrics.densityDpi <= 340 && metrics.densityDpi >= 300) {
			wdith = 0.7f;
		} else {
			// wdith = 0.4f;
		}

		return wdith;
	}

	public void showKeyboard(EditText et) {
		try {
			InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
			imm.showSoftInput(et, 0);
		} catch (Exception e) {
		}
	}

	public void hideKeyboard() {
		try {
			InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
		}
	}

	
	public void onMenuButtonClick(View v) {
		if (slidingMenu != null) {
			slidingMenu.toggle(true);
		}
	}

	
	public void onConnectionButtonClick(View v) {

		ActivityController.startNextActivity(RingToneBaseActivity.this, ContactsActivity.class,true);
		finish();
	}
	
	public void onGreetingsButtonClick(View v) {
		ActivityController.startNextActivity(RingToneBaseActivity.this, GreetingsActivity.class,true);
		finish();
	}
	
	public void onAboutButtonClick(View v) {
		ActivityController.startNextActivity(RingToneBaseActivity.this, AboutRingToneActivity.class,true);
		finish();
	}
	
	public void onHelpButtonClick(View v) {
		ActivityController.startNextActivity(RingToneBaseActivity.this, HelpActivity.class,true);
		finish();

	}
	
	public void onContactPeopleClick(View v) {
		ActivityController.startNextActivity(this, ConnectionsActivity.class,false);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(imageLoader!=null){
			imageLoader.clearCache();
			imageLoader = null;
		}
	}
	
	
}
