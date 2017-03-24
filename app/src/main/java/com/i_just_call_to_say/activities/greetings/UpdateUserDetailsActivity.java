package com.i_just_call_to_say.activities.greetings;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Inventory;
import com.android.vending.billing.util.Purchase;
import com.chipmunkrecord.WavFile;

import com.imageLoder.ImageLoader;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.about.PurchaseUtility;
import com.i_just_call_to_say.activities.about.UpdateInAppPurchaseApiManager;
import com.i_just_call_to_say.activities.about.PurchaseUtility.IUpgrade;
import com.i_just_call_to_say.activities.base.AppConstant;
import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.activities.connections.AddConnectionApiManager;
import com.i_just_call_to_say.activities.connections.IUpdateContacts;
import com.i_just_call_to_say.activities.contacts.UpdateProfileApiManager;
import com.i_just_call_to_say.activities.greetings.CheckUpdateService.LocalBinder;
import com.i_just_call_to_say.activities.greetings.CheckUpdateService.OnServiceDisconnectListener;
import com.i_just_call_to_say.contactutility.PhoneContactsUtility;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.table_query_manager.ContactsTableManager;
import com.utility.ActivityController;
import com.utility.AlertDialogCallBack;
import com.utility.CustomDialogUtility;
import com.utility.CustomeProgressDialogue;
import com.utility.view.CustomButton;
import com.utility.view.CustomEditText;
import com.utility.view.ImageViewRounded;

public class UpdateUserDetailsActivity extends RingToneBaseActivity  implements OnServiceDisconnectListener{
	private TextView tv_contact_no,tv_ringtone,tv_name,tv_profile_pic;
	private  ImageViewRounded imng_profile_picture;
	private Contacts contacts;
	private static final int RINGTONE_REQUEST = 10098;
	private static final int CHANGE_PROFILE_IMAGE_REQUEST = 10099;
	private static final int UPDATE_PROFILE_IMAGE_REQUEST = 10097;
	private String username="",ringtone_filepath="",image_filepath="";
	private MediaPlayer mediaPlayer;
	private String imagePath="";
	private ImageView iv_play;
	private ProgressDialog pd;
	private PurchaseUtility purchaseUtility;
	private CustomeProgressDialogue progressDialogue;
	private int explosion= 0;
	private float pitch = 1.1f;
	private int streamId = 0;
	private LinearLayout upgrade_friend;
	private CustomButton btn_update;
	private static int CALLBACK_FROM = 0;
	private MediaRecorder mRecorder;
	private final String CHIPMUNK_VOICE="chipmunkfile";
	private boolean isRecordedFile;
	private CheckUpdateService updateService;
	private boolean mBound = false;
	private int SERVICE_TIME_OUT = 7*60*1000,RECORD_AUDIO=1114;
	private Timer mTimer;
	private CreateMediaPlayer createMediaPlayer;
	
	private boolean isNeedToStartBindService = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_details);
		checkpermession();
		imageLoader = new ImageLoader(this);
		getIntentData();
		initView();
		setTabSelected(RingToneBaseActivity.UPDATE_USER_DETAILS,contacts.getContact_name());
		attachListener();
		
	}

	private void checkpermession() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			Log.e("Permission", "reached1");

			if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO);

			} /*else {
				if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
					// Should we show an explanation?
					// No explanation needed, we can request the permission.



					// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
					// app-defined int constant. The callback method gets the
					// result of the request.

				}
			}
*/
		}
	}

	private void attachListener() {
		btn_distrust.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateUser_distrustCallService();
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UpdateUserDetailsActivity.this.setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView(){
		initBaseView();
		iv_play = (ImageView) findViewById(R.id.iv_play);
		tv_ringtone=(TextView)findViewById(R.id.tv_ringtone);
		tv_name=(TextView)findViewById(R.id.tv_name);
		tv_profile_pic=(TextView)findViewById(R.id.tv_profile_pic);
		upgrade_friend = (LinearLayout) findViewById(R.id.ll_07);
		imng_profile_picture=(ImageViewRounded)findViewById(R.id.imng_profile_picture);
		imng_profile_picture.setCircleBackground(R.drawable.update_img_holder_frame);
		imng_profile_picture.setCirclePadding(getResources().getInteger(R.integer.update_profile_circle_paddingWidth), getResources().getInteger(R.integer.update_profile_circle_paddingHeight));
		btn_update = (CustomButton) findViewById(R.id.btn_update);
		btn_update.setEnabled(false);
		setValue();
	}

	private void setValue(){
		System.out.println(" "+contacts.getConnection_id());
		iv_play.setImageResource(R.drawable.icon_play);
		if(contacts.isPurchase_status()){
			upgrade_friend.setVisibility(View.GONE);
		}else{
			upgrade_friend.setVisibility(View.VISIBLE);
		}
		
		if(contacts.getMy_name().equals("") || contacts.getMy_name() ==null){
			tv_name.setText(user.getUser_name());
		}
		else{
			tv_name.setText(contacts.getMy_name());
		}
		ringtone_filepath = contacts.getMy_ringtone();
		String d=contacts.getContact_image();
		//imageLoader.DisplayImage(d.replaceAll("https","http"), imng_profile_picture, R.drawable.no_image,false);
		if(contacts.getMy_image_normal() !=null && !contacts.getMy_image_normal().equals("")){
			if(contacts.getMy_image_normal().contains("content://")){
				imng_profile_picture.setImageURI(Uri.parse(contacts.getMy_image_normal()));
			}
			else{
				imageLoader.DisplayImage(contacts.getMy_image_normal(), imng_profile_picture, R.drawable.no_image,false);
			}
		}else{
			imageLoader.DisplayImage(contacts.getMy_image_normal(), imng_profile_picture, R.drawable.no_image,false);
		}


//		if(!ringtone_filepath.equals("") && ringtone_filepath != null){
//			System.out.println("ringtone_filepath "+ringtone_filepath);
//			loadAudio();
//			
//		}
		
	}
	
	 private String getOutputMediaFile(String contactNumber) {
		    File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+File.separator+"i_just_call_to_say");

		    if (!mediaStorageDir.exists()) {
		        if (!mediaStorageDir.mkdirs()) {
		            System.out.println( "Oops! Failed create directory");
		            return null;
		        }
		    }
		    String path = mediaStorageDir.getPath() + File.separator +contactNumber+"";
		    System.out.println("path "+ path);
		    return path;
	  }

	private void getIntentData(){
		contacts=(Contacts) getIntent().getExtras().getSerializable("contact");
		PhoneContactsUtility.getRawContactInfo(UpdateUserDetailsActivity.this, contacts.getContact_id());
		PhoneContactsUtility.getContactsId(this, contacts.getContact_number());
		username = contacts.getContact_name();
	}
	
	
	private void updateUser_distrustCallService(){
		Object[] values={user.getAccess_token(),"DENY",contacts.getContact_number(),RingToneBaseApplication.device_token};
		AddConnectionApiManager addConnectionApiManager=new AddConnectionApiManager(UpdateUserDetailsActivity.this, contacts, ringToneBaseApplication, "Please wait...       ",values,new IUpdateContacts() {

			@Override
			public void updateContacts(String message , String sms_msg) {
			
			CustomDialogUtility.showCallbackMessageWithOk("Connection distrusted successfully.", UpdateUserDetailsActivity.this,new AlertDialogCallBack() {
				
				@Override
				public void onSubmitWithEditText(String text) {
					
				}
				
				@Override
				public void onSubmit() {
					UpdateUserDetailsActivity.this.setResult(RESULT_OK);
					UpdateUserDetailsActivity.this.finish();
				}
				
				@Override
				public void onCancel() {
					
				}
			});
			
			}

			@Override
			public void onFailuepdateContacts(String message) {
				
			}
		});
		
	}
	
	

	public void onButtonClick(View v) {

		switch (v.getId()) {
		case R.id.ll_updateName:
			updateName();
			break;
			
		case R.id.tv_profile_pic:
			updateImage();
			break;
		
		case R.id.btn_update:
			updateDetails_callService();
			break;
			
		case R.id.tv_sendGreeting:
			sendGreetingCard();
			break;
			
		case R.id.tv_ringtone:
			updateRingtone();
			break;
			
		case R.id.fl_profileImage:
			changeProfileImage();
			break;
			
		case R.id.iv_play:
			playAudio();
			break;
			
		case R.id.iv_rid:
			synthesizeAudio();
			break;
			
		case R.id.tv_upgrade_friend:	
			upgradeFriend();
			break;
		default:
			break;
		}
		
	}

	private void upgradeFriend() {
		if(!contacts.isPurchase_status()){
			CustomDialogUtility.showCallbackMessageWithOkCancel("Are you sure you want to upgrade for your friend. Do you want to upgrade it now ?", UpdateUserDetailsActivity.this, new AlertDialogCallBack() {
				
				@Override
				public void onSubmitWithEditText(String text) {
					
				}
				
				@Override
				public void onSubmit() {
					try {
						purchaseUtility = PurchaseUtility.getPurchaseUtilityInstance(UpdateUserDetailsActivity.this,PurchaseUtility.USER_SKU, new IUpgrade() {
//							@Override
//							public void onUpgradeFinished() {
//								upgradeFriend_callService();
//							}

							@Override
							public void onPurchaseDetails(Inventory inv) {
								if (inv.hasPurchase(PurchaseUtility.USER_SKU)) {
									purchaseUtility.startConsumePurchase(inv.getPurchase(PurchaseUtility.USER_SKU));
								}else{
									purchaseUtility.purchaseItem(PurchaseUtility.USER_SKU);
								}
							}

							@Override
							public void onConsumeFinished(boolean isConsumed) {
//								if (isConsumed) {
//									purchaseUtility.purchaseItem(PurchaseUtility.USER_SKU);
//								} else {
//									Toast.makeText(getApplicationContext(), "Purchasing error please try again.", Toast.LENGTH_LONG).show();
//								}
							}


							@Override
							public void onUpgradeFinished(boolean isSuccess, IabResult result, Purchase info) {
								if (isSuccess) {
									purchaseUtility.startConsumePurchase(info);
									upgradeFriend_callService();
								} else {
//									purchaseUtility.purchaseItem(PurchaseUtility.USER_SKU);
									Toast.makeText(getApplicationContext(), "Purchasing unsuccessful please try again.", Toast.LENGTH_LONG).show();
								}
																
							}
						});
						purchaseUtility.initBillingHelper();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onCancel() {
					
				}
			});	
		}else{
			upgradeFriend_serviceCallBack();
		}
	}

	private void upgradeFriend_callService(){
		System.out.println("upgrade call service");
		Object[] values = {user.getAccess_token(),contacts.getContact_number(),RingToneBaseApplication.device_token};
		UpgradeFriendApiManager friendApiManager = new UpgradeFriendApiManager(this, ringToneBaseApplication, "Updating Purchase...       ", values);
	}
	
	public void upgradeFriend_serviceCallBack(){
		upgrade_friend.setVisibility(View.GONE);
		ContactsTableManager.updatePurchaseStatus(contacts.getContact_number(), true, ringToneBaseApplication.databaseManager, this);
	}
	
	
	private void synthesizeAudio() {
		if(!isRecordedFile){
			Toast.makeText(getApplicationContext(), "Please record an audio to convert into chipmunk voice.", Toast.LENGTH_LONG).show();
		}else if(ringtone_filepath.contains(CHIPMUNK_VOICE)){
			System.out.println("for chunk"+ringtone_filepath);
			Toast.makeText(getApplicationContext(), "This audio already converted into chipmunk voice.", Toast.LENGTH_LONG).show();
			//CustomDialogUtility.showMessageWithOk("This audio already converted into chipmunk voice", UpdateUserDetailsActivity.this);
		}else{
			CustomDialogUtility.showCallbackMessageWithOkCancel("Do you want convert this audio in chipmunk voice ?", UpdateUserDetailsActivity.this, new AlertDialogCallBack() {
			@Override
			public void onSubmitWithEditText(String text) {
				
			}
			
			@Override
			public void onSubmit() {
				/*	if(!user.getPurchase_status().equalsIgnoreCase("true") ){
					CustomDialogUtility.showCallbackMessageWithOkCancel("Upgrade to full version to play the synthesized audio. Do you want to upgrade it now ?", UpdateUserDetailsActivity.this, new AlertDialogCallBack() {
						@Override
						public void onSubmitWithEditText(String text) {
							
						}
						
						@Override
						public void onSubmit() {
							try {
								purchaseUtility = PurchaseUtility.getPurchaseUtilityInstance(UpdateUserDetailsActivity.this,PurchaseUtility.USER_SKU, new IUpgrade() {
//									@Override
//									public void onUpgradeFinished() {
//										CALLBACK_FROM = 1;
//										update_callService();
//									}

									@Override
									public void onPurchaseDetails(Inventory inv) {
										if (inv.hasPurchase(PurchaseUtility.USER_SKU)) {
											purchaseUtility.startConsumePurchase(inv.getPurchase(PurchaseUtility.USER_SKU));
										}else{
											purchaseUtility.purchaseItem(PurchaseUtility.USER_SKU);
										}
									}

									@Override
									public void onConsumeFinished(boolean isConsumed) {
//										if (isConsumed) {
//											purchaseUtility.purchaseItem(PurchaseUtility.USER_SKU);
//										} else {
//											Toast.makeText(getApplicationContext(), "Purchasing error please try again.", Toast.LENGTH_LONG).show();
//										}
									}

									@Override
									public void onUpgradeFinished(boolean isSuccess,IabResult result, Purchase info) {
										if (isSuccess) {
											purchaseUtility.startConsumePurchase(info);
											CALLBACK_FROM = 1;
											update_callService();
										} else {
//											purchaseUtility.purchaseItem(PurchaseUtility.USER_SKU);
											Toast.makeText(getApplicationContext(), "Purchasing unsuccessful please try again.", Toast.LENGTH_LONG).show();
										}
									}
								});
								purchaseUtility.initBillingHelper();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
						@Override
						public void onCancel() {
							
						}
					});	
				}else{
					new ConvertChipmunk(ringtone_filepath).execute();
				}*/
				System.out.println("for chunk"+ringtone_filepath);
				new ConvertChipmunk(ringtone_filepath).execute();
			  }
		
			@Override
			public void onCancel() {
				
			}
		});

		}
		
	}
	
	private class ConvertChipmunk extends AsyncTask<Void , Void, String>{

		private String path="";
		private ProgressDialog pd;
		
		public ConvertChipmunk(String path){
			this.path = path;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(UpdateUserDetailsActivity.this,R.style.alertDialogTheme);
			pd.setCancelable(false);
			pd.setMessage("Converting file...     ");
			pd.show();
		}
		
		@Override
		protected String doInBackground(Void... params) {
			String outputFilePath = getCreateFile().getAbsolutePath();
			WavFile.convertChipMunkVoice(path, outputFilePath);
			return outputFilePath;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(pd != null && pd.isShowing())
				pd.dismiss();
			ringtone_filepath = result;
			loadAudio();
			try {
				File file = new File(path);
				if (file.exists())
					file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("audio path"+ ringtone_filepath);
			CustomDialogUtility.showMessageWithOk("Audio has been successfully converted into chipmunk voice.", UpdateUserDetailsActivity.this);
		}
	}
	
	
	private File getCreateFile() {
		try {
			File temp_dir = new File(Environment.getExternalStorageDirectory(),	AppConstant.APP_RINGTONE_DIRECTORY);
			if (!temp_dir.exists()) {
				temp_dir.mkdirs();
			}
			File temp_path = new File(temp_dir, CHIPMUNK_VOICE + ".wav");
			if (temp_path.exists()) {
				temp_path.delete();
				temp_path.createNewFile();
			}
			return temp_path;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void playAudio() {
		startAudio();
	}
	
	private void loadAudio(){
		try {
			System.out.println("load audio 2");
			System.out.println("****  loadAudio   audioPath "+ ringtone_filepath);
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
			System.out.println("load audio 3");
			if(mediaPlayer != null)
				onStopMediaPlayer();

		    String r_path=ringtone_filepath.replace("https","http");

			mediaPlayer = MediaPlayer.create(UpdateUserDetailsActivity.this, Uri.parse(r_path));
			System.out.println("load audio 7");
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					onStopMediaPlayer();
					iv_play.setImageResource(R.drawable.icon_play);
					loadAudio();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	private void startAudio() {
		try {
			if (mediaPlayer == null) {
				createMediaPlayer = new CreateMediaPlayer();
				createMediaPlayer.execute();
			} else {
				if (mediaPlayer.isPlaying()) {
					iv_play.setImageResource(R.drawable.icon_play);
					mediaPlayer.pause();
				} else {
					iv_play.setImageResource(R.drawable.icon_pause);
					mediaPlayer.start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void onStopMediaPlayer() {
		System.out.println("load audio 4");
		if (mediaPlayer != null) {
			try {
				System.out.println("load audio 5");
				if (createMediaPlayer != null)
					System.out.println("load audio 6");
				createMediaPlayer.cancel(true);
				mediaPlayer.pause();
				mediaPlayer.stop();
				mediaPlayer.reset();
				mediaPlayer.release();
				mediaPlayer = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (!hasFocus) {
			if (createMediaPlayer != null)
				createMediaPlayer.cancel(true);
			if (mediaPlayer != null) {
				mediaPlayer.pause();
			}
			iv_play.setImageResource(R.drawable.icon_play);
		}
	}
	

	private void changeProfileImage() {
		
		if(!user.getPurchase_status().equalsIgnoreCase("true") ){
			CustomDialogUtility.showCallbackMessageWithOkCancel("Upgrade to full version to change the contact image. Do you want to upgrade it now ?", UpdateUserDetailsActivity.this, new AlertDialogCallBack() {
				
				@Override
				public void onSubmitWithEditText(String text) {
					
				}
				
				@Override
				public void onSubmit() {
					try {
						purchaseUtility = PurchaseUtility.getPurchaseUtilityInstance(UpdateUserDetailsActivity.this,PurchaseUtility.USER_SKU, new IUpgrade() {
//							@Override
//							public void onUpgradeFinished() {
//								CALLBACK_FROM = 2;
//								update_callService();
//							}

							@Override
							public void onPurchaseDetails(Inventory inv) {
								if (inv.hasPurchase(PurchaseUtility.USER_SKU)) {
									purchaseUtility.startConsumePurchase(inv.getPurchase(PurchaseUtility.USER_SKU));
								}else{
									purchaseUtility.purchaseItem(PurchaseUtility.USER_SKU);
								}
							}

							@Override
							public void onConsumeFinished(boolean isConsumed) {
//								if (isConsumed) {
//									purchaseUtility.purchaseItem(PurchaseUtility.USER_SKU);
//								} else {
//									Toast.makeText(getApplicationContext(), "Purchasing error please try again.", Toast.LENGTH_LONG).show();
//								}
							}

							@Override
							public void onUpgradeFinished(boolean isSuccess, IabResult result, Purchase info) {
								if (isSuccess) {
									purchaseUtility.startConsumePurchase(info);
									CALLBACK_FROM = 2;
									update_callService();
								} else {
									Toast.makeText(getApplicationContext(), "Purchasing unsuccessful please try again.", Toast.LENGTH_LONG).show();
//									purchaseUtility.purchaseItem(PurchaseUtility.USER_SKU);
								}
							}
							
						});
						purchaseUtility.initBillingHelper();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onCancel() {
					
				}
			});	
		}else{
			CALLBACK_FROM = 2;
			update_serviceCallBack();
		}
	}

	private void update_callService(){
		Object[] values={user.getAccess_token(),"YES",RingToneBaseApplication.device_token};
 	    UpdateInAppPurchaseApiManager updateInAppPurchaseApiManager=new UpdateInAppPurchaseApiManager(UpdateUserDetailsActivity.this, ringToneBaseApplication, user,"Updating Purchase...       ", values);
	}
	
	public void update_serviceCallBack() {
		initAdView();
		if (CALLBACK_FROM == 2) {
			CALLBACK_FROM = 0;
			Intent intent = new Intent(this,ImageSelectionProfilePopupDialog.class);
			startActivityForResult(intent, CHANGE_PROFILE_IMAGE_REQUEST);
		} else if (CALLBACK_FROM == 1) {
			CALLBACK_FROM = 0;
//			playModifiedVoice(ringtone_filepath);
//			if(ringtone_filepath.contains(CHIPMUNK_VOICE)){
//				CustomDialogUtility.showMessageWithOk("This audio already converted into chipmunk voice", UpdateUserDetailsActivity.this);
//			}else{
			new ConvertChipmunk(ringtone_filepath).execute();
//			}
		}
	}
	
	private void updateRingtone() {
		Intent intent=new Intent(this,AudioPopupDIalog.class);
		startActivityForResult(intent, RINGTONE_REQUEST);		
	}

	private void updateImage() {
		Intent intent=new Intent(this,UpdateProfileImagePopupDialog.class);
		startActivityForResult(intent, UPDATE_PROFILE_IMAGE_REQUEST);
	}

	private void updateName() {
		final Dialog dialog = new Dialog(this,R.style.picChooserTheme);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.update_name_dialog_layout);
		final CustomEditText et_name = (CustomEditText) dialog.findViewById(R.id.et_name);
		CustomButton btn_accept = (CustomButton) dialog.findViewById(R.id.btn_accept);
		CustomButton btn_cancel = (CustomButton) dialog.findViewById(R.id.btn_cancel);
		et_name.setText(tv_name.getText().toString().trim());
		btn_accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!tv_name.getText().toString().trim().equals(et_name.getText().toString().trim())) {
					UpdateUserDetailsActivity.this.username=et_name.getText().toString().trim();
					tv_name.setText(username);
					if(!btn_update.isEnabled())
					btn_update.setEnabled(true);
				}
				dialog.dismiss();
			}
		});
		
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}


	private void updateDetails_callService(){
		//startUpdateCheck()
		isNeedToStartBindService = false;
		String remove_image;
		String[] value=new String[3];
		value[0]=image_filepath;
		
		System.out.println("ringtone_filepath "+ringtone_filepath);
		
		if (ringtone_filepath.contains("http://") || ringtone_filepath.contains("content://")|| ringtone_filepath.length()==0) {
			value[1] = "";
		} else {
			value[1]=ringtone_filepath;
			isNeedToStartBindService = true;
			System.out.println("  new ringtone file is uploaded ");
		}
		
		if(image_filepath.length() > 0){
			System.out.println("  new image file is uploaded ");
			isNeedToStartBindService = true;
		}
		
		if(!contacts.getContact_name().equals(username)){
			System.out.println("  name is change "+username+"      contacts.getContact_name() "+contacts.getContact_name());
			isNeedToStartBindService = true;
		}	

		if(imagePath.equals("N")){
			value[2] = "";
			remove_image = "Y";
		}else{
			value[2] = imagePath;
			remove_image = "N";
		}

		System.out.println("image path "+image_filepath);
		System.out.println("ringtone_filepath "+ringtone_filepath + " value[1] "+value[1]);
		String[] key=new String[3];
		key[0]="change_my_profile_image";
		key[1]="ringtone_file";
		key[2]="change_my_image";
		
//		if(isNeedToStartBindService){
//			System.out.println("isNeedToStartBindService"+isNeedToStartBindService);
//			startUpdateCheck();
//		}
			

		Object[] values={user.getAccess_token(),contacts.getConnection_id(),contacts.getContact_name().equals(username)?"":username,RingToneBaseApplication.device_token, remove_image};
		System.out.println("connection_id-->"+values[0]+","+values[1]+","+values[2]+","+values[3]+","+values[4]);

		progressDialogue = new CustomeProgressDialogue(UpdateUserDetailsActivity.this);
		progressDialogue.show();
		UpdateProfileApiManager updateProfileApiManager = new UpdateProfileApiManager(UpdateUserDetailsActivity.this,((RingToneBaseApplication)getApplication()), "", values,key,value);
	}

	private void sendGreetingCard() {
		Bundle bundle=new Bundle();
		bundle.putSerializable("contact", (Serializable) contacts);
		ActivityController.startNextActivity(UpdateUserDetailsActivity.this, GreetingsActivity.class, bundle, false);
	}

	public void updateservice_onFaluire(){
	//	progressDialogue.dismiss();
		System.out.print("my update display number 2");
//		if(isNeedToStartBindService)
//		  unbindService(mConnection);
		startUpdateCheck();
	}
	
	public void updateservice_callBack(String message){
		System.out.print("update details 1");
		if(!isNeedToStartBindService){
			System.out.print("update details 2");
			//progressDialogue.dismiss();
			CustomDialogUtility.showCallbackMessageWithOk(message, this, new AlertDialogCallBack() {
				@Override
				public void onSubmitWithEditText(String text) {
					
				}
				
				@Override
				public void onSubmit() {
					deleteTempFile();	
					UpdateUserDetailsActivity.this.setResult(RESULT_OK);
					finish();					
				}
				
				@Override
				public void onCancel() {
					
				}
			});
		}else{
			System.out.print("update details");
			startUpdateCheck();
		}
	}
	
	
	private void  deleteTempFile(){
		File ringtone_file = new File(ringtone_filepath);
		if(ringtone_file.exists())
			ringtone_file.delete();
		File image_file = new File(imagePath);
		if(image_file.exists())
			image_file.delete();
		File profile_image = new File(image_filepath);
		if(profile_image.exists())
			profile_image.delete();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (!btn_update.isEnabled()) {
				btn_update.setEnabled(true);
			}
			if (requestCode == RINGTONE_REQUEST) {
				if (data != null) {
					ringtone_filepath = data.getStringExtra("filepath");
					isRecordedFile = data.getBooleanExtra("isRecordedFile", false);
					loadAudio();
				}
			} else if (requestCode == CHANGE_PROFILE_IMAGE_REQUEST) {
					imagePath = data.getStringExtra("path");
					if (!imagePath.equals("N"))
						imng_profile_picture.setImageURI(Uri.parse(imagePath));
					else{
						if(contacts.getMy_image_thumb()!=null && !contacts.getMy_image_thumb().contains("content://"))
						imng_profile_picture.setImageResource(R.drawable.no_image);
						else if(contacts.getMy_image_thumb()==null)
						Toast.makeText(getApplicationContext(),"Default image cannot be removed",Toast.LENGTH_LONG).show();
						else
						Toast.makeText(getApplicationContext(),"Contact image cannot be removed",Toast.LENGTH_LONG).show();
					}
				
			} else if (requestCode == UPDATE_PROFILE_IMAGE_REQUEST) {
				image_filepath = data.getStringExtra("path");
				System.out.println("UPDATE PROFILE image path " + image_filepath);
			}
			else{
				purchaseUtility.onActivityResult(requestCode, resultCode, data);
			}
		}
		
	}

	
	@Override
	protected void onStop() {
		super.onStop();
		onStopMediaPlayer();
	}
	

	public class CreateMediaPlayer extends AsyncTask<Void, Void, Boolean> {

		private String path = "";
		private ProgressDialog pd;

		public CreateMediaPlayer() {
			this.path = path;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pd = new ProgressDialog(UpdateUserDetailsActivity.this,R.style.alertDialogTheme);
			pd.setCancelable(false);
			pd.setMessage("Loading media file...      ");
			pd.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				System.out.println("load audio 1");
				loadAudio();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result == true){
				pd.dismiss();
				if(mediaPlayer != null){
					if(mediaPlayer.isPlaying()){
						iv_play.setImageResource(R.drawable.icon_play);
						mediaPlayer.pause();
					}else{
						iv_play.setImageResource(R.drawable.icon_pause);
						mediaPlayer.start();
					}
				}
						
			}else{
				pd.dismiss();
				CustomDialogUtility.showMessageWithOk("Loading media file failed.", UpdateUserDetailsActivity.this);
			}
				
		}
		
	}
	

	private void startUpdateCheck() {
		System.out.print("update details 3");
		startBindService();
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.print("update details 6");
				String msg ="Unable to update "+ contacts.getContact_name()+"'s profile. Please try again...";
				onServiceDisconnect(false,msg);
			}
		},  SERVICE_TIME_OUT);

	}
	
	private void disconnectService() {
		System.out.print("update details 8");
		try {
			unbindService(mConnection);
			mBound = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		disconnectService();
	}
	
    private void startBindService() {
		System.out.print("update details 4");
    	try {
    		Intent intent = new Intent(this, CheckUpdateService.class);
        	intent.putExtra("contact_number",contacts.getContact_number());
        	bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,  IBinder service) {
        	LocalBinder binder = (LocalBinder) service;
        	updateService = binder.getService();
        	updateService.onStartCommand(contacts.getContact_number());
        	updateService.setOnDisConnectListener(UpdateUserDetailsActivity.this);
        	mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	mBound = false;
        }
    };

	@Override
	public void onServiceDisconnect(final boolean status, final String message) {
		System.out.print("update details 7");
		disconnectService();
		if(mTimer!=null)
			mTimer.cancel();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (status) {
					System.out.print("update details 9");
					progressDialogue.dismiss();
					deleteTempFile();
					 CustomDialogUtility.showCallbackMessageWithOk(message, UpdateUserDetailsActivity.this, new AlertDialogCallBack() {
						@Override
						public void onSubmitWithEditText(String text) {
						}
						
						@Override
						public void onSubmit() {
							setResult(RESULT_OK);
							finish();
						}
						
						@Override
						public void onCancel() {
						}
					});
				}else{
					//progressDialogue.dismiss();
					System.out.print("update details 10");
					CustomDialogUtility.showMessageWithOk(message, UpdateUserDetailsActivity.this);
				}
			}
		});
    }
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

		if (requestCode == RECORD_AUDIO) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

			}

			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
}
