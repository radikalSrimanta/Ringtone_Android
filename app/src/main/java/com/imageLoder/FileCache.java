package com.imageLoder;

import java.io.File;
import java.io.FilenameFilter;

import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.AppConstant;
import com.i_just_call_to_say.dto.Contacts;

import android.content.Context;
import android.os.Environment;

public class FileCache {
    
    private File cacheDir;
    
    public FileCache(Context context){
        //Find the dir to save cached images
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
    
    public FileCache(Context context,String path){
        //Find the dir to save cached images
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            cacheDir=new File(Environment.getExternalStorageDirectory(),path);
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
    
    public File getFile(String url){
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(Math.abs(url.hashCode()))+".png";
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
//        try{
////	        if(f.exists())
////	        	f.delete();
//  //      	System.out.println("--- file is exist "+f.exists());
////        	 if(!f.exists())
////        		 f.createNewFile();
//        }catch(Exception e){}
        return f;
        
    }
    
    public static void deleteCache(Context context) {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
    
    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
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