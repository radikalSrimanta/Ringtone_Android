package com.i_just_call_to_say.activities.contacts;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import com.imageLoder.SingleImageDownload;
import com.i_just_call_to_say.activities.base.AppConstant;
import com.i_just_call_to_say.contactutility.PhoneContactsUtility;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.RawContactInfoDto;
import com.utility.CommonUtility;

public class DownloadUpdatePhoneContact {

	private Context context;

	public DownloadUpdatePhoneContact(Context context) {
		super();
		this.context = context;
	}
	
	
	public boolean downloadRingTone(Contacts contacts){
		String path = "";
		int count;
			if (contacts.getContact_ringtone().length()>0) {
					try {
						if (!CommonUtility.checkConnectivity(context)) {
							return false;
						}
						deleteFileIfExist(contacts);
						if (android.os.Build.VERSION.SDK_INT > 9) {
							StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
							StrictMode.setThreadPolicy(policy);
						}
						URL url = new URL(contacts.getContact_ringtone().replaceAll("https","http"));
						URLConnection conection = url.openConnection();
						conection.connect();

						int lenghtOfFile = conection.getContentLength();
						InputStream input = new BufferedInputStream(url.openStream(), 8192);
						System.out.println("contact ringtone url");
						path = getOutputMediaFile(contacts.getContact_number(), contacts.getContact_ringtone());

						if (path == null)
							return false;

						OutputStream output = new FileOutputStream(path);

						byte data[] = new byte[1024];

						long total = 0;

						while ((count = input.read(data)) != -1) {
							total += count;
							output.write(data, 0, count);
						}
						output.flush();
						output.close();
						input.close();
						
						if(contacts !=null && contacts.getContact_id().length() > 0)
							assignRingtoneToContact(contacts.getContact_id(), new File(path));
						else
							assignRingtoneToContact(PhoneContactsUtility.getContactsId(context,		contacts.getContact_number()),		new File(path));
						return true;
					
					} catch (Exception e) {
						return false;
					}
			}else{
				return true;
			}
			
	}
	
	
	public boolean undateContactName(Contacts contacts) {
			try {
				if(contacts.getContact_name() != null && contacts.getContact_name().length() > 0){
//					if(contacts !=null && contacts.getContact_id().length() > 0)
//						PhoneContactsUtility.changeContactName(context, contacts.getContact_id(),contacts.getContact_name());
//					else
					ArrayList<RawContactInfoDto> raw_contact_info = PhoneContactsUtility.getRawContactInfo(context, contacts.getContact_id());
					for (RawContactInfoDto rawContactInfoDto : raw_contact_info) {
						PhoneContactsUtility.changeContactName(context, rawContactInfoDto, contacts.getContact_name());
					}
					
						
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return false;
		}
	
	
	public boolean downloadContactImage(Contacts contacts) {
		try {
			SingleImageDownload imageDownload =  new SingleImageDownload(context);; 
			if(contacts.getContact_image().length()>0){
				if (!CommonUtility.checkConnectivity(context)) {
					return false;
				}
					String downloadImagePath = imageDownload.downloadImage(contacts);
				
				if(downloadImagePath != null && downloadImagePath.length() > 0){
//						if(contacts !=null && contacts.getRaw_contact_id().length() > 0)
//							PhoneContactsUtility.changeProfileImage(context, downloadImagePath ,contacts.getRaw_contact_id());
//						else{
			//**********************************************new change	*********************************************			
							ArrayList<RawContactInfoDto> raw_contact_info = PhoneContactsUtility.getRawContactInfo(context, contacts.getContact_id());
							for (RawContactInfoDto rawContactInfoDto : raw_contact_info) {
								PhoneContactsUtility.changeProfileImage(context, downloadImagePath ,rawContactInfoDto);
							}
							
//						}
						return true;
				}
			}
		} catch (Exception e) {
		  return false;
		}	
		return false;
	}
	
	
	private boolean isEclairOrLater() {
		return Build.VERSION.SDK_INT >= 5;
	}

	private Uri getContactContentUri() {
		if (isEclairOrLater()) {
			return Uri.parse("content://com.android.contacts/contacts");
		} else {
			return android.provider.Contacts.People.CONTENT_URI;
		}
	}
		    
	private void assignRingtoneToContact(String contactId, File outFile) {

		long fileSize = outFile.length();
		String mimeType = "audio/mpeg";

		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, outFile.getAbsolutePath());
		values.put(MediaStore.MediaColumns.TITLE, AppConstant.APP_FOLDER_NAME);
		values.put(MediaStore.MediaColumns.SIZE, fileSize);
		values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);

		values.put(MediaStore.Audio.Media.ARTIST, "artist");
		values.put(MediaStore.Audio.Media.DURATION, 2);

		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
		values.put(MediaStore.Audio.Media.IS_ALARM, false);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);

		// Insert it into the database
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(outFile.getAbsolutePath());
		System.out.println("ringtone uri " + uri);
		final Uri newUri = context.getContentResolver().insert(uri, values);
		if (newUri != null) {
			Uri contactUri = Uri.withAppendedPath(getContactContentUri(), contactId);
			ContentValues localContentValues = new ContentValues();
			localContentValues.put(ContactsContract.Data.RAW_CONTACT_ID, contactId);
			localContentValues.put(ContactsContract.Data.IS_PRIMARY, 1);
			localContentValues.put(ContactsContract.Data.IS_SUPER_PRIMARY, 1);
			localContentValues.put(ContactsContract.Data.CUSTOM_RINGTONE, newUri.toString());
			context.getContentResolver().update(contactUri, localContentValues, null, null);
		}
	}
		    
	  private String getOutputMediaFile(String contactNumber,String path) {
		  	File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+File.separator+AppConstant.APP_RINGTONE_DIRECTORY);
			if (!mediaStorageDir.exists()) {
				if (!mediaStorageDir.mkdirs()) {
					System.out.println("Oops! Failed create directory");
					return null;
				}
			}
		    String ringTone = path.substring(path.lastIndexOf("/"));
		    System.out.println("ringTone "+ringTone.replaceAll("/", ""));
		    File  mediaFile = new File(mediaStorageDir.getPath() + File.separator +contactNumber +"_"+ ringTone.replaceAll("/", ""));
		    System.out.println("mediaFile.toString"+mediaFile.toString());
		    return mediaFile.toString();
	  }
	  
	  
	  private String getFilePath(String url){
		  File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+File.separator+AppConstant.APP_FOLDER_NAME);
		  String ringTone = url.substring(url.lastIndexOf("/"));		  	
		  return mediaStorageDir.getPath() + File.separator + ringTone;
	  }
	  
	  private boolean isFileExists(String path){
		  File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+File.separator+AppConstant.APP_FOLDER_NAME);
		  String ringTone = path.substring(path.lastIndexOf("/"));		  	
		  File  mediaFile = new File(mediaStorageDir.getPath() + File.separator + ringTone);
		  if (mediaFile.exists()) {
				//System.out.println("file delete");
				return true;
		  }
		    	  
		  return false;
	  }
	
	
	  private void deleteFileIfExist(final Contacts contacts){
		try {
			File[] files = null;
			File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + File.separator + AppConstant.APP_RINGTONE_DIRECTORY);
			if (mediaStorageDir.exists() && mediaStorageDir.isDirectory()) {
				files = mediaStorageDir.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.contains(contacts.getContact_number());
					}
				});

				if (files != null && files.length > 0) {
					for (File file : files) {
						file.delete();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	  
}
