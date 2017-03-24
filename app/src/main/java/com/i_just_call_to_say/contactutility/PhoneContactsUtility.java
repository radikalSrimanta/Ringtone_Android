package com.i_just_call_to_say.contactutility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.CalendarContract.SyncState;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.text.format.Time;

import com.i_just_call_to_say.activities.base.AppConstant;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.RawContactInfoDto;
import com.utility.DateUtility;

public class PhoneContactsUtility {
	
	public static void getListContacts(Activity activity,List<Contacts> contacts_list, String userNumber){
		try {
//	/*Log append*/		CommonUtility.appendLog("Initial Contact Details:  \n\n");			
			String phone = "";
			ContentResolver cr = activity.getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.CommonDataKinds.Photo.PHOTO_URI,ContactsContract.Contacts.HAS_PHONE_NUMBER}, null, null, null);
			if(cur!=null && cur.getCount()>0){
				cur.moveToFirst();
				while (!cur.isAfterLast()){
					String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
					String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					String image_uri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI));
					if(Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0) {
						 Cursor cursorPhone =activity. getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},		 
					                		 ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
						if (cursorPhone != null && cursorPhone.getCount() > 0) {
							cursorPhone.moveToFirst();
							while (!cursorPhone.isAfterLast()) {
								phone = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								if (!userNumber.contains(phone.replaceAll("[\\s\\-()]", ""))) {
									Contacts contacts = new Contacts();
									contacts.setContact_id(id);
									contacts.setContact_name(name);
									System.out.println("name " + name + " id " + id+ " phone " +phone.replaceAll("[\\s\\-()]", "")+ " image uri " + image_uri);
									contacts.setMy_image_thumb(image_uri==null?"":image_uri);
									contacts.setMy_image_normal(image_uri==null?"":image_uri);
									contacts.setContact_number(phone.replaceAll("[\\s\\-()]", ""));
// /*Log append*/		CommonUtility.appendLog("Contact Name: " + name + 
//		 									"\n Contact Number "+ phone.replaceAll("[\\s\\-()]", "") +
//		 									"\n Contact id "+ id +
//		 									"\n Contact Image "+ image_uri +"\n\n");	
									contacts_list.add(contacts);
								}
								
								cursorPhone.moveToNext();
							}
							cursorPhone.close();
						}
					}
					else{
						phone="Not Valid";
					}
					cur.moveToNext();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getListContacts(Context activity,JSONArray contacts_list, String userNumber){
		try {
			String phone = "";
			ContentResolver cr = activity.getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID,ContactsContract.Contacts.HAS_PHONE_NUMBER}, null, null, null);
			if(cur!=null && cur.getCount()>0){
				cur.moveToFirst();
				while (!cur.isAfterLast()){
					String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
					if(Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0){
						Cursor cursorPhone =activity. getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},		 
		                		 ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
						if (cursorPhone != null && cursorPhone.getCount() > 0) {
							cursorPhone.moveToFirst();
							while (!cursorPhone.isAfterLast()) {
								phone = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								if (!userNumber.contains(phone.replaceAll("[\\s\\-()]", ""))) {
									JSONObject phone_number = new JSONObject();
									phone_number.put("contact_id", id);
									phone_number.put("phone_number", phone.replaceAll("[\\s\\-()]", ""));
									contacts_list.put(phone_number);
								}
								cursorPhone.moveToNext();
							}
							cursorPhone.close();
						}
					}
					else{
						phone="Not Valid";
					}
					cur.moveToNext();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static ArrayList<RawContactInfoDto> getRawContactInfo(Context context, String contact_id){
		
		ArrayList<RawContactInfoDto> rawInfoDtos;
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(ContactsContract.RawContacts.CONTENT_URI,new String[]{ContactsContract.RawContacts.ACCOUNT_NAME,ContactsContract.RawContacts.ACCOUNT_TYPE,ContactsContract.RawContacts._ID},ContactsContract.RawContacts.CONTACT_ID +"=?", new String[]{String.valueOf(contact_id)}, null);
	    if(cursor != null && cursor.getCount() >0){
	    	rawInfoDtos = new ArrayList<RawContactInfoDto>();
	    	cursor.moveToFirst();
		       while(!cursor.isAfterLast()){
		    	   RawContactInfoDto contactInfoDto = new RawContactInfoDto();
		    	   contactInfoDto.contact_id = contact_id;
		    	   contactInfoDto.contact_type = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE))==null?"":cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));
		    	   contactInfoDto.raw_contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts._ID));
		    	   contactInfoDto.account_name = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME));
		    	   cursor.moveToNext();
		    	   rawInfoDtos.add(contactInfoDto);
		       }
	       cursor.close();
	       return rawInfoDtos;
	    }
		return null;
	}
	
	public static String getContactsId(Context context, String userNumber){
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID}, ContactsContract.CommonDataKinds.Phone.NUMBER +" LIKE ?", new String[]{"%"+userNumber}, null);
		if(cursor !=null && cursor.getCount()>0){
			cursor.moveToFirst();
			String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
			System.out.println("contact id--->"+ contact_id);
			return contact_id;
		}
		return null;
	}
	
	public static Contacts getIntialContactInfo(Context context, String contactId){
		try{
		List<Contacts> contacts_list = new ArrayList<Contacts>();
		ContentResolver cr = context.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.CommonDataKinds.Photo.PHOTO_URI,ContactsContract.Contacts.HAS_PHONE_NUMBER}, ContactsContract.Contacts._ID + " = ?", new String[]{contactId}, null);
		if(cur!=null && cur.getCount()>0){
			cur.moveToFirst();
			while (!cur.isAfterLast()){
				String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				String image_uri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI));
				Contacts contacts = new Contacts();
				contacts.setContact_name(name);
				contacts.setMy_image_thumb(image_uri);
				contacts.setMy_image_normal(image_uri);
				contacts_list.add(contacts);
				cur.moveToNext();
			}
		}
		return contacts_list.get(0);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
	}
	
	public static void changeProfileImage(Context context, String path,	RawContactInfoDto contactInfoDto) {
		try {
			
			System.out.println("raw_contact_id--->"+contactInfoDto.raw_contact_id);
			Bitmap mBitmap = BitmapFactory.decodeFile(path);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			if (mBitmap != null) { // If an image is selected successfully
				mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			}
			try {
				stream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			ArrayList<ContentProviderOperation> ops1 = new ArrayList<ContentProviderOperation>();
			ops1.add(ContentProviderOperation
					.newInsert(Data.CONTENT_URI)
					.withValue(Data.RAW_CONTACT_ID,contactInfoDto.raw_contact_id )
					.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
					.withValue(Data.IS_PRIMARY, "1")
					.withValue(Data.IS_SUPER_PRIMARY, "1")
					.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray()).build());
			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY,	ops1);
			
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			ops.add(ContentProviderOperation
					.newUpdate(Data.CONTENT_URI)
					.withSelection(
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ? AND "
									+ Data.MIMETYPE
									+ "='"
									+ ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
									+ "'", new String[] { contactInfoDto.contact_id })
					.withValue(Data.IS_PRIMARY, "1")
					.withValue(Data.IS_SUPER_PRIMARY, "1")
					.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray()).build());
			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			
			
			
//			ArrayList<ContentProviderOperation> ops2 = new ArrayList<ContentProviderOperation>();
//			ops1.add(ContentProviderOperation
//					.newUpdate(ContactsContract.Contacts.CONTENT_URI)
//					.withValue(ContactsContract.Contacts._ID,raw_contact_id )
//					.withValue(ContactsContract.Contacts.Data.MIMETYPE,ContactsContract.Contacts.CONTENT_ITEM_TYPE)
//					.withValue(ContactsContract.Contacts.Photo.DISPLAY_PHOTO, stream.toByteArray())
//					.withValue(ContactsContract.Contacts.Photo.PHOTO, stream.toByteArray())
//					.withValue(ContactsContract.Contacts.Photo.CONTENT_DIRECTORY, stream.toByteArray())
//					.withValue(ContactsContract.Contacts.Data.IS_PRIMARY, "1")
//					.withValue(ContactsContract.Contacts.Data.IS_SUPER_PRIMARY, "1").build());
//			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY,	ops2);
////			
			
	//		ArrayList<ContentProviderOperation> ops2 = new ArrayList<ContentProviderOperation>();
	//		ops2.add(ContentProviderOperation
	//				.newUpdate(ContactsContract.S)
	//				.withValue(StreamItems.RAW_CONTACT_ID,raw_contact_id )
	//				.withValue(Data.IS_PRIMARY, "1")
		//			.withValue(Data.IS_SUPER_PRIMARY, "1")
	//				.withValue(StreamItems.StreamItemPhotos.CONTENT_DIRECTORY, stream.toByteArray()).build());
	//		context.getContentResolver().applyBatch(ContactsContract.AUTHORITY,	ops2);
			
			
//			 ContentValues values = new ContentValues();

			
//		    Uri rawContactPhotoUri = Uri.withAppendedPath(ContentUris.withAppendedId(RawContacts.CONTENT_URI, Long.parseLong(raw_contact_id)),RawContacts.DisplayPhoto.CONTENT_DIRECTORY);
//		     try {
//		         AssetFileDescriptor fd = context.getContentResolver().openAssetFileDescriptor(rawContactPhotoUri, "rw");
//		         OutputStream os = fd.createOutputStream();
//		         os.write(stream.toByteArray());
//		         os.close();
//		         fd.close();
//		     } catch (IOException e) {
//		    	 e.printStackTrace();
//		     }

			
//						
//			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
//			ops.add(ContentProviderOperation
//					.newUpdate(Data.CONTENT_URI)
//					.withSelection(	ContactsContract.Data.CONTACT_ID
//									+ " = ? AND " + Data.MIMETYPE + "='"
//									+ ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
//									+ "'", new String[] { contact_id })
//					.withValue(Data.IS_PRIMARY, "1")
//					.withValue(Data.IS_SUPER_PRIMARY, "1")
//					.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray()).build());
//			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY,	ops);
//			
			System.out.println("image set complete-->"+ contactInfoDto.raw_contact_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void changeContactName(Context context, RawContactInfoDto rawContactInfoDto, String contactName) {
		try {
			
			ArrayList<ContentProviderOperation> ops1 = new ArrayList<ContentProviderOperation>();
			ops1.add(ContentProviderOperation
					.newInsert(Data.CONTENT_URI)
					.withValue(Data.RAW_CONTACT_ID,rawContactInfoDto.raw_contact_id )
					.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
					.withValue(Data.IS_PRIMARY, "1")
					.withValue(Data.IS_SUPER_PRIMARY, "1")
					.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactName)
					.withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,"")
					.withValue(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME,"").build());
			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY,	ops1);
			
			System.out.println("change contact name "+contactName + " contact id " + rawContactInfoDto.contact_id);
			
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			ops.add(ContentProviderOperation
					.newUpdate(Data.CONTENT_URI)
					.withSelection(
							ContactsContract.CommonDataKinds.Phone._ID
									+ " = ? AND "
									+ Data.MIMETYPE
									+ "='"
									+ ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
									+ "'", new String[] { rawContactInfoDto.contact_id })
					.withValue(Data.IS_PRIMARY, "1")
					.withValue(Data.IS_SUPER_PRIMARY, "1")
					.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactName)
					.withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,"")
					.withValue(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME,"").build());
			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			
//			
//			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
//			ops.add(ContentProviderOperation
//					.newUpdate(Data.CONTENT_URI)
//					.withSelection(
//							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
//									+ " = ? AND "
//									+ Data.MIMETYPE
//									+ "='"
//									+ ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
//									+ "'", new String[] {rawContactInfoDto.contact_id })
//									.withValue(Data.IS_PRIMARY, "1")
//					.withValue(Data.IS_SUPER_PRIMARY, "1")
//					.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactName)
//					.withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,"")
//					.withValue(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME,"").build());
//			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getFileName(final Contacts contacts){
		try {
			File[] files = null;
			File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + File.separator + AppConstant.APP_IMAGE_DIRECTORY);
			if (mediaStorageDir.exists() && mediaStorageDir.isDirectory()) {
				files = mediaStorageDir.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.contains(contacts.getContact_number());
					}
				});

				if (files != null && files.length > 0) {
					return files[0].getAbsolutePath();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
			
	
		
		
	


