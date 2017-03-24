package com.utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.AppConstant;
import com.i_just_call_to_say.activities.contacts.ContactsActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;

public class GoogleImageProcessUtility extends AsyncTask<Void, Void, String> {

	
	private Context context;
	private String filePath;
	private OnProcessCompleteListener processCompleteListener;
	private ProgressDialog progressDialog;
	
	public GoogleImageProcessUtility(Activity context,String filePath, OnProcessCompleteListener processCompleteListener) {
		this.context = context;
		this.filePath = filePath;
		this.processCompleteListener =  processCompleteListener; 
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(context,R.style.alertDialogTheme);
		 progressDialog.setMessage("Please wait...      ");
		 progressDialog.setCancelable(false);
		 progressDialog.show();
	}
	
	@Override
	protected String doInBackground(Void... params) {
		return processImage();
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		System.out.println("picasa image path--->"+result);
		if(progressDialog !=null && progressDialog.isShowing())
			progressDialog.dismiss();
		processCompleteListener.onProcessComplete(result);
	}
	
	private String processImage() {
		String outputPath = "";
		try {
			if (filePath != null && filePath.startsWith("content:")) {
				filePath = getAbsoluteImagePathFromUri(Uri.parse(filePath));
			}
			if (filePath == null || TextUtils.isEmpty(filePath)) {
//				if (listener != null) {
//					listener.onError("Couldn't process a null file");
//				}
			} else if (filePath	.startsWith("content://com.google.android.gallery3d") || 
					filePath.startsWith("content://com.microsoft.skydrive.content.external")) {
					outputPath = processPicasaMedia(filePath, ".jpg");
			} else if (filePath.startsWith("content://com.google.android.apps.photos.content") || 
						filePath.startsWith("content://com.android.providers.media.documents") || 
						filePath.startsWith("content://com.google.android.apps.docs.storage")) {
					outputPath = processGooglePhotosMedia(filePath, ".jpg");
			} 
			return outputPath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private String saveScaledBitMap(String filepath) {
		FileOutputStream out = null;
		try {
			Bitmap bitmap = BitmapFactory.decodeFile(filePath, null);
			File downloadImageFile = new File(filePath);
			out = new FileOutputStream(downloadImageFile);
			if (bitmap != null) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
				bitmap = null;
				return downloadImageFile.getAbsolutePath();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Throwable ignore) {
			}
		}
		return null;
	}
	
	protected String getAbsoluteImagePathFromUri(Uri imageUri) {
		String[] proj = { MediaColumns.DATA, MediaColumns.DISPLAY_NAME };

		if (imageUri.toString().startsWith("content://com.android.gallery3d.provider")) {
			imageUri = Uri.parse(imageUri.toString().replace("com.android.gallery3d", "com.google.android.gallery3d"));
		}

		String filePath = "";
		String imageUriString = imageUri.toString();
		if (imageUriString.startsWith("content://com.google.android.gallery3d")
				|| imageUriString
						.startsWith("content://com.google.android.apps.photos.content")
				|| imageUriString
						.startsWith("content://com.android.providers.media.documents")
				|| imageUriString
						.startsWith("content://com.google.android.apps.docs.storage")
				|| imageUriString
						.startsWith("content://com.microsoft.skydrive.content.external")) {
			filePath = imageUri.toString();
		} else {
			Cursor cursor = context.getContentResolver().query(imageUri, proj,
					null, null, null);
			cursor.moveToFirst();
			filePath = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaColumns.DATA));
			cursor.close();
		}

		return filePath;
	}
	
	protected String processPicasaMedia(String path, String extension) throws Exception {
		try {
			InputStream inputStream = context.getContentResolver().openInputStream(Uri.parse(path));
			filePath = getDirectory() + File.separator + Calendar.getInstance().getTimeInMillis() + extension;
			BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(filePath));
			byte[] buf = new byte[2048];
			int len;
			while ((len = inputStream.read(buf)) > 0) {
				outStream.write(buf, 0, len);
			}
			inputStream.close();
			outStream.close();
			saveScaledBitMap(filePath);
			return filePath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	private String getDirectory(){
		File cacheDir;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			cacheDir = new File(Environment.getExternalStorageDirectory(), AppConstant.APP_IMAGE_DIRECTORY);
		else
			cacheDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		if (!cacheDir.exists())
			cacheDir.mkdirs();
		return cacheDir.getAbsolutePath();
		
	}

	
	protected String processGooglePhotosMedia(String path, String extension)
			throws Exception {
		String retrievedExtension = checkExtension(Uri.parse(path));
		if (retrievedExtension != null
				&& !TextUtils.isEmpty(retrievedExtension)) {
			extension = "." + retrievedExtension;
		}
		try {
			filePath = getDirectory()+ File.separator + Calendar.getInstance().getTimeInMillis() + extension;
			ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(Uri.parse(path),"r");
			FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
			InputStream inputStream = new FileInputStream(fileDescriptor);
			BufferedInputStream reader = new BufferedInputStream(inputStream);
			BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(filePath));
			byte[] buf = new byte[2048];
			int len;
			while ((len = reader.read(buf)) > 0) {
				outStream.write(buf, 0, len);
			}
			outStream.flush();
			outStream.close();
			inputStream.close();
			saveScaledBitMap(filePath);
			return filePath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public String checkExtension(Uri uri) {

		String extension = "";
		Cursor cursor = context.getContentResolver().query(uri, null, null,	null, null);
		try {
			if (cursor != null && cursor.moveToFirst()) {
				String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
				int position = displayName.indexOf(".");
				extension = displayName.substring(position + 1);
				int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
				String size = null;
				if (!cursor.isNull(sizeIndex)) {
					size = cursor.getString(sizeIndex);
				} else {
					size = "Unknown";
				}
			}
		} finally {
			cursor.close();
		}
		return extension;
	}
	
	public interface OnProcessCompleteListener{
		public void onProcessComplete(String path);
	}
	
}
