package com.imageLoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.imageLoder.Utils;
import com.i_just_call_to_say.activities.base.AppConstant;
import com.i_just_call_to_say.dto.Contacts;
import com.utility.CommonUtility;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

public class SingleImageDownload {
	private File cacheDir;
	private Context context;
	
	public SingleImageDownload(Context context){
		this.context = context;
		 if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
	            cacheDir=new File(Environment.getExternalStorageDirectory(),AppConstant.APP_IMAGE_DIRECTORY);
	        else
	            cacheDir=context.getCacheDir();
	        if(!cacheDir.exists())
	            cacheDir.mkdirs();
	        
	        try {
				File noMediaFile = new File(cacheDir, ".nomedia");
				if(!noMediaFile.exists())
					noMediaFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	private File getFile(Contacts contact){
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=contact.getContact_number()+"_"+String.valueOf(Math.abs(contact.getContact_image().hashCode()))+".png";
        File f = new File(cacheDir, filename);
        try{
	        if(f.exists())
	        	f.delete();
        	 if(!f.exists())
        		 f.createNewFile();
        }catch(Exception e){}
        return f;
        
    }
	
	
	public String  downloadImage(Contacts contact){
		try {
			String downloadImageUrl = contact.getContact_image().replaceAll("https","http");
			deleteFileIfExist(contact);
			File downloadImageFile = getFile(contact);
			downloadImageFile.createNewFile();
			URL imageUrl = new URL(downloadImageUrl);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(90000);
			conn.setReadTimeout(90000);
			conn.setInstanceFollowRedirects(true);
			HttpURLConnection.setFollowRedirects(true);
			
			boolean redirect = false;
			 
			// normally, 3xx is redirect
			int status = conn.getResponseCode();
			if (status != HttpURLConnection.HTTP_OK) {
				if (status == HttpURLConnection.HTTP_MOVED_TEMP
					|| status == HttpURLConnection.HTTP_MOVED_PERM
						|| status == HttpURLConnection.HTTP_SEE_OTHER)
				redirect = true;
			}
			
			if (redirect) {
				 
				// get redirect url from "location" header field
				String newUrl = conn.getHeaderField("Location");
		 
				// open the new connnection again
				conn = (HttpURLConnection) new URL(newUrl).openConnection();
						 
				//System.out.println("Redirect to URL : " + newUrl);
		 
			}
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(downloadImageFile);
			Utils.CopyStream(is, os);
			os.close();
			saveScaledBitMap(CommonUtility.decodeFile(AppConstant.IMAGE_WIDTH, AppConstant.IMAGE_WIDTH, downloadImageFile.getAbsolutePath()),downloadImageFile);
			return downloadImageFile.getAbsolutePath();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	private String saveScaledBitMap(Bitmap bitmap, File downloadImageFile) {
		FileOutputStream out = null;
		try {

			out = new FileOutputStream(downloadImageFile);
			if (bitmap != null) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, AppConstant.COMPRESS, out);
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
	
	 private void deleteFileIfExist(final Contacts contacts){
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
