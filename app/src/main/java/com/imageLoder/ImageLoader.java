package com.imageLoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.i_just_call_to_say.activities.ILayoutInterface.IGetImagePath;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageLoader {

	public MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private boolean isRoundelCorners;
	public String image_path;
	//private IGetImagePath iGetImagePath;
	public float roundPx = 12;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;
	public Context mContext;
	public OnImageLoaderComplete listner;
	//public ProgressBar progBar;
	private ImageView.ScaleType mScaleType = ImageView.ScaleType.FIT_XY;
    //private boolean isLocalImage = false; 
	
	public ImageLoader(Context context) {
		this.isRoundelCorners = false;
		fileCache = new FileCache(context);
		mContext = context;
		executorService = Executors.newFixedThreadPool(5);
//		fileCache.clear();
		memoryCache.clear();
	}

	public ImageLoader(Context context, String path) {
		this.isRoundelCorners = false;
		fileCache = new FileCache(context,path);
		mContext = context;
		executorService = Executors.newFixedThreadPool(5);
//		fileCache.clear();
		memoryCache.clear();
	}
	
	public ImageLoader(Context context, boolean isRoundelCorners) {
		this.isRoundelCorners = isRoundelCorners;
		fileCache = new FileCache(context);
		//fileCache.clear();
		memoryCache.clear();
		executorService = Executors.newFixedThreadPool(5);
		mContext = context;
	}

	//int stub_id = 0;

	public void DisplayImage(final String url1, ImageView imageView, int stubID,final ProgressBar progressBar, Activity mActivity) {
		final String url=url1.replace("https","http");
		try{
			//this.stub_id = stubID;
			imageViews.put(imageView, url);
			Bitmap bitmap = memoryCache.get(url);
			//System.out.println("bitmap "+bitmap);
			if (bitmap != null) {
				bitmap = getAspectBitmap(bitmap, imageView,0);
//				if (isRoundelCorners) {
//					bitmap = getRoundedCornerBitmap(bitmap);
//				} else {
//					bitmap = memoryCache.get(url);
//				}
				//imageView.setBackgroundColor(android.R.color.transparent); 
				//	imageView.setBackgroundColor(stubID);
					imageView.setImageBitmap(bitmap);
			} else {
				//System.out.println("progressBar "+progressBar);
				if(progressBar != null){
					mActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							progressBar.setProgress(90);
							progressBar.setVisibility(View.VISIBLE);
							progressBar.setTag(url);
							//System.out.println("--progress bar added");
						}
					});
					
				}
				
				imageView.setTag(url);
				queuePhoto(url, imageView,-1,-1,false,stubID,progressBar);
				imageView.setImageResource(stubID);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	
	public void DisplayImage(String url1, ImageView imageView, int stubID , boolean localImage) {
		try{
			String url=url1.replace("https","http");
			//this.stub_id = stubID;
			//url=ural;
			imageViews.put(imageView, url);
			Bitmap bitmap = memoryCache.get(url);
			if (bitmap != null) {
				bitmap = getAspectBitmap(bitmap, imageView,0);
//				if (isRoundelCorners) {
//					bitmap = getRoundedCornerBitmap(bitmap);
//				} else {
//					bitmap = memoryCache.get(url);
//				}
				
					//imageView.setBackgroundColor(android.R.color.transparent);
					imageView.setImageBitmap(bitmap);
				
				
			} else {
				imageView.setTag(url);
				queuePhoto(url, imageView,-1,-1,localImage,stubID,null);
				imageView.setImageResource(stubID);
			}
		}catch(Exception e){
			
		}
		catch (Throwable e) {}
	}
	
	public void DisplayImageWithCallBack(String url, ImageView imageView, int stubID , boolean localImage, IGetImagePath iGetImagePath) {
		try{
			//this.iGetImagePath = iGetImagePath;
			//System.out.println("11 this.iGetImagePath "+this.iGetImagePath);
			//this.stub_id = stubID;
			imageViews.put(imageView, url);
			System.out.println("url "+url);
			imageView.setTag(url);
			queuePhoto(url, imageView,-1,-1,localImage,stubID,null,iGetImagePath);
			imageView.setImageResource(stubID);
			
			System.out.println("-----");
		}catch(Exception e){}
		catch (Throwable e) {}
	}
	
	private Bitmap getAspectBitmap(Bitmap bitmap, ImageView imageView ,  int backgroundImageID) {
		int resizeWidth = imageView.getWidth();
		int resizeHeight = imageView.getHeight();
		//System.out.println("resizeWidth "+resizeWidth +"    resizeHeight:"+resizeHeight);
		if((resizeWidth == 0 || resizeHeight == 0) && backgroundImageID > 0){
			Bitmap bitmap2 = BitmapFactory.decodeResource(mContext.getResources(), backgroundImageID);
			resizeWidth = bitmap2.getWidth()  ;
			resizeHeight = bitmap2.getHeight() ;
			//System.out.println("**************************update by background image dimention *********************");
		}
		
		//System.out.println("resizeWidth "+resizeWidth +"    resizeHeight:"+resizeHeight);
		if (resizeHeight > bitmap.getHeight())
			resizeHeight = bitmap.getHeight();
		if (resizeWidth > bitmap.getWidth())
			resizeWidth = bitmap.getWidth();
		double div_res = 1.0;

		if (bitmap.getHeight() > bitmap.getWidth()) {
			div_res = ((double) bitmap.getWidth() / (double) bitmap.getHeight());
			resizeWidth = (int) (div_res * resizeHeight);
		} else if (bitmap.getHeight() < bitmap.getWidth()) {
			div_res = ((double) bitmap.getHeight() / (double) bitmap.getWidth());
			resizeHeight = (int) (div_res * resizeWidth);
		}
		Bitmap bitmap_temp = getResizedBitmap(bitmap, resizeHeight, resizeWidth);
		if (bitmap_temp != null)
			bitmap = bitmap_temp;
		return bitmap;
	}
	
	public void DisplayImage(final String url, final ImageView imageView,final ProgressBar progressBar , Activity mActivity,int maskID, ImageView.ScaleType scaleType, int backgroundImageID, int stubID,boolean isLocal) {
		try{
			this.mScaleType = scaleType;
			imageViews.put(imageView, url);
			Bitmap bitmap = memoryCache.get(url);
			if (bitmap != null) {
				
				bitmap = getAspectBitmap(bitmap, imageView,maskID);
//				if(maskID != -1){
//					//System.out.println("** masking is doen here");
//					makeMaskImage(imageView, bitmap, maskID, scaleType, backgroundImageID);
//				}else{
					imageView.setImageBitmap(bitmap);
				//}
			} else {
				if(progressBar != null){
					mActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							progressBar.setProgress(90);
							progressBar.setVisibility(View.VISIBLE);
							progressBar.setTag(url);
						}
					});
					
				}
				imageView.setTag(url);
				queuePhoto(url, imageView,maskID,backgroundImageID,isLocal,stubID,progressBar);
//				if(maskID != -1){
//					Bitmap no_image = BitmapFactory.decodeResource(mActivity.getResources(),stubID);
//					makeMaskImage(imageView, no_image, maskID, scaleType, backgroundImageID);
//				}else{
//					imageView.setImageResource(stubID);
//				}
				
				if(stubID > 0){
					Bitmap no_image = BitmapFactory.decodeResource(mActivity.getResources(),stubID);
					imageView.setImageBitmap(no_image);
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	
	public interface OnImageLoaderComplete{
		public abstract void onLoadComplete(int index);
	}
	
	public void setOnImageLoaderComplete(OnImageLoaderComplete listener){
		this.listner = listener;	
	}
	

	

	private void queuePhoto(String url, ImageView imageView,int mMask_ID , int backgroundImage_ID,boolean flag, int stu_id,ProgressBar bar) {
		if(url != null && url.length() > 0){
			PhotoToLoad p = new PhotoToLoad(url, imageView,mMask_ID,backgroundImage_ID,flag,stu_id,bar);
			executorService.submit(new PhotosLoader(p));
		}
	}
	
	private void queuePhoto(String url, ImageView imageView,int mMask_ID , int backgroundImage_ID,boolean flag, int stu_id,ProgressBar bar,IGetImagePath iGetImagePath) {
		if(url != null && url.length() > 0){
			PhotoToLoad p = new PhotoToLoad(url, imageView,mMask_ID,backgroundImage_ID,flag,stu_id,bar,iGetImagePath);
			executorService.submit(new PhotosLoader(p));
		}
	}
	
	
	
	private Bitmap getBitmapFromLocalDirectory(File file,PhotoToLoad photoToLoad){
		try{
			System.out.println("--> file "+file.getAbsolutePath());
			if(!file.exists()){
				file.createNewFile();
				return null;
			}/*else if(mMaskID == -1){
				Drawable drawable = Drawable.createFromPath(file.getAbsolutePath());
				System.out.println("-- receive from "+ drawable);
				if(drawable != null)
					return ((BitmapDrawable)drawable).getBitmap();
			}*/else
				return decodeFile(file);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private Bitmap getBitmap(String url,PhotoToLoad photoToLoad) {
		File f = fileCache.getFile(url);
		// from SD cache
		
		if(photoToLoad.isLocalImage)
			f = new File(url);
		image_path = f.getAbsolutePath();
		System.out.println("image_path ** "+image_path);
		Bitmap b = getBitmapFromLocalDirectory(f,photoToLoad);
		
		if (b != null)
			return b;

		// from web
		try {
			//System.out.println("---- downloading image "+url+"    isLocalImage:"+isLocalImage);
			Bitmap bitmap = null;
			if(!photoToLoad.isLocalImage){
				URL imageUrl = new URL(url);
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
				OutputStream os = new FileOutputStream(f);
				Utils.CopyStream(is, os);
				os.close();
				//bitmap = decodeFile(f);
			}else{
				System.out.println("local file "+f);
			}
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			//System.out.println("-----------exception "+ex.toString());
			ex.printStackTrace();
			return null;
		}
		//return downloadImage(f,url);
	}
	
	public static Bitmap getResizedBitmap(Bitmap bitmap, int newHeight, int newWidth) {
		try{
//		    int width = bm.getWidth();
//		    int height = bm.getHeight();
//		    float scaleWidth = ((float) newWidth) / width;
//		    float scaleHeight = ((float) newHeight) / height;
//		    // CREATE A MATRIX FOR THE MANIPULATION
//		    Matrix matrix = new Matrix();
//		    // RESIZE THE BIT MAP
//		    matrix.postScale(scaleWidth, scaleHeight);
//	
//		    // "RECREATE" THE NEW BITMAP
//		    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		    
		    
		    
		    Bitmap resizedBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);

		    float ratioX = newWidth / (float) bitmap.getWidth();
		    float ratioY = newHeight / (float) bitmap.getHeight();
		    float middleX = newWidth / 2.0f;
		    float middleY = newHeight / 2.0f;
		    Matrix scaleMatrix = new Matrix();
		    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
		    Canvas canvas = new Canvas(resizedBitmap);
		    canvas.setMatrix(scaleMatrix);
		    canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
		    return resizedBitmap;
		}catch(Throwable e){
			return bitmap;
		}
		
	}
	
//	private void getImage(String url){
//		try{
//		//System.out.println("*** getImage "+url);
//			URL aURL = new URL(url);
//	        URLConnection conn = aURL.openConnection();
//	        conn.setUseCaches(true);
//	        conn.connect(); 
//	        InputStream is = conn.getInputStream(); 
//	        BufferedInputStream bis = new BufferedInputStream(is); 
//	        Bitmap bm = BitmapFactory.decodeStream(bis);
//	        //System.out.println("------ bnbbnbb "+bm);
//	        bis.close(); 
//	        is.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
	
	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			//System.out.println("bitmap REQUIRED_Height "+REQUIRED_Height+"   "+REQUIRED_Widtht);
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			int REQUIRED_Height = 300;
			int REQUIRED_Widtht = 300;
			int scale = 0;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
//			if(mMaskID != -1){
//				Bitmap mask = BitmapFactory.decodeResource(mContext.getResources(),mMaskID);
//				REQUIRED_Height = mask.getHeight();
//				REQUIRED_Widtht = mask.getWidth();
//				scale = 1;
//			}
			
			
			while (true) {
				if (width_tmp  < REQUIRED_Widtht
						|| height_tmp  < REQUIRED_Height)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;
		public int mMaskID = -1,backgroundImageID = -1; 
		public boolean isLocalImage = false;
		public int stub_id = 0;
		public ProgressBar progBar;
		public IGetImagePath iGetImagePath;

//		public PhotoToLoad(String u, ImageView i) {
//			url = u;
//			imageView = i;
//		}
//		public PhotoToLoad(String u, ImageView i,int mMask_ID , int backgroundImage_ID) {
//			url = u;
//			imageView = i;
//			mMaskID = mMask_ID;
//			backgroundImageID = backgroundImage_ID;
//		}
		
		public PhotoToLoad(String u, ImageView i,int mMask_ID , int backgroundImage_ID , boolean flag , int stu_id,ProgressBar bar) {
			url = u;
			imageView = i;
			mMaskID = mMask_ID;
			backgroundImageID = backgroundImage_ID;
			isLocalImage = flag;
			stub_id = stu_id;
			progBar = bar;
		}
		
		
		public PhotoToLoad(String u, ImageView i,int mMask_ID , int backgroundImage_ID , boolean flag , int stu_id,ProgressBar bar,IGetImagePath iGetImagePath) {
			url = u;
			imageView = i;
			mMaskID = mMask_ID;
			backgroundImageID = backgroundImage_ID;
			isLocalImage = flag;
			stub_id = stu_id;
			progBar = bar;
			this.iGetImagePath = iGetImagePath;
		}
		
	}
	
	
//	class PhotosLoaderWithoutNoImage implements Runnable {
//		PhotoToLoad photoToLoad;
//
//		PhotosLoaderWithoutNoImage(PhotoToLoad photoToLoad) {
//			this.photoToLoad = photoToLoad;
//		}
//
//		@Override
//		public void run() {
//			if (imageViewReused(photoToLoad))
//				return;
//			Bitmap bmp = getBitmap(photoToLoad.url);
//			memoryCache.put(photoToLoad.url, bmp);
//			if (imageViewReused(photoToLoad))
//				return;
//			BitmapDisplayerWithoutNoImage bd = new BitmapDisplayerWithoutNoImage(bmp, photoToLoad);
//			Activity a = (Activity) photoToLoad.imageView.getContext();
//			a.runOnUiThread(bd);
//		}
//	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			System.out.println("photoToLoad.iGetImagePath  "+photoToLoad.iGetImagePath);
			Bitmap bmp = getBitmap(photoToLoad.url,photoToLoad);
			System.out.println("bmp  "+bmp +"   photoToLoad.iGetImagePath: "+photoToLoad.iGetImagePath);
			if(bmp != null){
				memoryCache.put(photoToLoad.url, bmp);
				if(photoToLoad.iGetImagePath != null){
					photoToLoad.iGetImagePath.getImagePath(image_path);
					return;
				}
			}
//				if (imageViewReused(photoToLoad))
//					return;
			//System.out.println("11  bmp  "+bmp);
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				Activity a = (Activity) photoToLoad.imageView.getContext();
				a.runOnUiThread(bd);
				//System.out.println("222  bmp  "+bmp);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		//System.out.println("--- tag "+tag +" check:  "+(tag.equalsIgnoreCase(photoToLoad.url))+"     photoToLoad.url:"+photoToLoad.url);
		if (tag == null || !tag.equalsIgnoreCase(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			System.out.println("RUN  %%%%%%%%%%%%");
//			if (imageViewReused(photoToLoad))
//				return;
			photoToLoad.imageView.invalidate();
			if(listner != null)
				listner.onLoadComplete(0);
			//System.out.println("iGetImagePath "+iGetImagePath);
			
			if (bitmap != null) {
				bitmap = getAspectBitmap(bitmap, photoToLoad.imageView,photoToLoad.mMaskID);
				/*if (isRoundelCorners) {
					bitmap = getRoundedCornerBitmap(bitmap);
				}else*///07-03 06:12:04.045: I/System.out(1573): -- no_image null    stub_id:17170445

//				if(photoToLoad.mMaskID != -1 && photoToLoad.backgroundImageID != -1){
//					makeMaskImage(photoToLoad.imageView, bitmap, photoToLoad.mMaskID, mScaleType, photoToLoad.backgroundImageID);
//				}else{
//					//photoToLoad.imageView.setBackgroundColor(android.R.color.transparent);
//					photoToLoad.imageView.setImageBitmap(bitmap);
//				}
				photoToLoad.imageView.setImageBitmap(bitmap);
			} else {
//				if(photoToLoad.mMaskID != -1){
//					Bitmap no_image = BitmapFactory.decodeResource(mContext.getResources(),photoToLoad.stub_id);
//					makeMaskImage(photoToLoad.imageView, no_image, photoToLoad.mMaskID, mScaleType, photoToLoad.backgroundImageID);
//				}else{
//					photoToLoad.imageView.setImageResource(photoToLoad.stub_id);
//				}
				photoToLoad.imageView.setImageResource(photoToLoad.stub_id);
			}
			if(photoToLoad.progBar != null )
			  System.out.println(photoToLoad.imageView.getTag()+"  "+photoToLoad.progBar.getTag());
			if(photoToLoad.progBar != null && photoToLoad.progBar.getTag() != null && photoToLoad.imageView.getTag() != null && photoToLoad.progBar.getTag().equals(photoToLoad.imageView.getTag())){
				photoToLoad.progBar.setVisibility(View.GONE);
				
			}
		}
	}
	
	
//	private void makeMaskImage(ImageView mImageView, Bitmap original, int maskID, ImageView.ScaleType scaleType, int backgroundImageID)
//	{
//		
//		Bitmap mask = BitmapFactory.decodeResource(mContext.getResources(),maskID);
//		Bitmap scaledBitmap = original;
//		//System.out.println("original.getWidth()  "+original.getWidth()+"     original.getHeight(): "+original.getHeight());
//		if(mask.getWidth() > original.getWidth() || mask.getHeight() > original.getHeight()){
//			//System.out.println("recreate the bitmap ****");
//			//scaledBitmap = Bitmap.createScaledBitmap(original, mask.getWidth(), mask.getHeight(), false);
//			//scaledBitmap = getResizedBitmap(original, mask.getHeight(), mask.getWidth());
//			//System.out.println("scaledBitmap.getWidth()  "+scaledBitmap.getWidth()+"     scaledBitmap.getHeight(): "+scaledBitmap.getHeight());
//		}
//		
//		Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Config.ARGB_8888);
//		//System.out.println("result.getWidth()  "+result.getWidth()+"     result.getHeight(): "+result.getHeight());
//		Canvas mCanvas = new Canvas(result);
//		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
//		if(mask.getWidth() > original.getWidth() || mask.getHeight() > original.getHeight()){
//			mCanvas.drawBitmap(scaledBitmap, 0, 0, null);
//		}else{
//			mCanvas.drawBitmap(original, 0, 0, null);
//		}
//		mCanvas.drawBitmap(mask, 0, 0, paint);
//		paint.setXfermode(null);
//		//mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//		mImageView.setImageBitmap(original);
//		mImageView.setBackgroundResource(backgroundImageID);
//		
//		
//	}
	
	//07-26 15:30:20.871: I/System.out(5654): Response111: {"errorCode":0,"user_profile_image_url":"http://isisdev.net/sociavize/development/upload/profileimage/thumb/1406282417_1406368803274.jpg","msg":"Timeline saved successfully."}  200

//		// Used to display bitmap in the UI thread
//		class BitmapDisplayerWithoutNoImage implements Runnable {
//			Bitmap bitmap;
//			PhotoToLoad photoToLoad;
//
//			public BitmapDisplayerWithoutNoImage(Bitmap b, PhotoToLoad p) {
//				bitmap = b;
//				photoToLoad = p;
//			}
//
//			public void run() {
//				if (imageViewReused(photoToLoad))
//					return;
//				if (bitmap != null) {
//					if (isRoundelCorners) {
//						bitmap = getRoundedCornerBitmap(bitmap);
//					}
//					photoToLoad.imageView.setImageBitmap(bitmap);
//				} else {
//					photoToLoad.imageView.setImageResource(stub_id);
//				}
//
//			}
//		}

	private Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),	bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}
	
	public void terminateService(){
		if(executorService != null && !executorService.isTerminated())
			executorService.shutdownNow();
	}
	
	public void restartService(){
		if(executorService != null && !executorService.isTerminated())
			executorService.shutdownNow();
		executorService = Executors.newFixedThreadPool(5);
	}

}
