package com.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class CommonUtility {

	public static boolean checkConnectivity(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable() && ni.isConnected()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void appendLog(String text) {
		// String filePath =
		// Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)+"/log.txt";
		System.out.println(text);
		
		text = DateUtility.getCurrentDateTimeInGMT()+ ":  "+ text;
		
		File logFile = new File(Environment.getExternalStorageDirectory(),	"Ringtone_log.txt");
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.append(text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static boolean validateEmail(String emailID) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher;
		matcher = pattern.matcher(emailID);
		return matcher.matches();
	}

	public static void showKeyboard(EditText et,Activity activity) {
		try {
			InputMethodManager imm = (InputMethodManager) activity
					.getSystemService(Service.INPUT_METHOD_SERVICE);
			imm.showSoftInput(et, 0);
		} catch (Exception e) {
		}
	}

	public static void hideKeyboard(Activity activity) {
		try {
			InputMethodManager imm = (InputMethodManager) activity
					.getSystemService(Service.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(activity.getCurrentFocus()
					.getWindowToken(), 0);
		} catch (Exception e) {
		}
	}

	public static String urlDecoding(String value)
	{
		String value1="";
		try {
			value1=URLDecoder.decode(value, "UTF-8");

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value1;
	}

	public static String[] getArrayListFromSeperatorString(String seperator_text){
		System.out.println("seperator_text "+seperator_text);
		String[] array;
		if(seperator_text==null || "".equals(seperator_text))
		{
			array = new String[0];
		}
		else
		{
			array = seperator_text.split(",");

		}
		System.out.println("Result: " + Arrays.toString(array));
		return array;
	}

	public static String getDeviceId(Context context)
	{
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
		String deviceid = tm.getDeviceId();
		return deviceid;
	}

	public static  void setImage(RelativeLayout relayout,int image_h,int image_xh,int image_xxh,Activity activity){
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		System.out.println("xxxxx"+dm.heightPixels+" "+dm.widthPixels);
		if(dm.widthPixels==480){
			if(dm.heightPixels>=800){
				System.out.println("ffffffffffffffffffffffffff");
				relayout.setBackgroundResource(image_h);
			}
		}
		else if(dm.widthPixels==720){
			if(dm.heightPixels>=1280){
				System.out.println("ffffffffffffffffffffffffff");
				relayout.setBackgroundResource(image_xh);
			}
		}
		else if(dm.widthPixels==1080){
			if(dm.heightPixels>=1920){
				relayout.setBackgroundResource(image_xxh);
			}
		}
	}

	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	public static Bitmap resizeBitmapWithAQspectRatio(Bitmap bitmap, int required_height,int requerid_width) {
		Bitmap background = Bitmap.createBitmap((int) requerid_width,
				(int) required_height, Config.ARGB_4444);
		float originalWidth = bitmap.getWidth(), originalHeight = bitmap
				.getHeight();
		Canvas canvas = new Canvas(background);
		float scale = requerid_width / originalWidth;
		float xTranslation = 0.0f, yTranslation = (required_height - originalHeight
				* scale) / 2.0f;
		Matrix transformation = new Matrix();
		transformation.postTranslate(xTranslation, yTranslation);
		transformation.preScale(scale, scale);
		Paint paint = new Paint();
		paint.setFilterBitmap(true);
		canvas.drawBitmap(bitmap, transformation, paint);
		// System.out.println("height " + background.getHeight() + " width "
		// + background.getWidth());

		return background;
	}

	// decodes image and scales it to reduce memory consumption
	public static Bitmap decodeFile(int width , int height,String path) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, o);

			// Find the correct scale value. It should be the power of 2.
			int REQUIRED_Height = height;
			int REQUIRED_Widtht = width;
			int scale = 1;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			// System.out.println("Scale: width_tmp " + width_tmp +
			// " height_tmp "+ height_tmp);

			while (true) {
				if (width_tmp < REQUIRED_Widtht || height_tmp < REQUIRED_Height)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			// System.out.println("Scale: " + scale);
			return BitmapFactory.decodeFile(path, o2);
		} catch (Exception e) {
			// System.out.println("--- exception "+e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	
	// decodes image and scales it to reduce memory consumption
		public static Bitmap decodeFile(int width , int height,InputStream inputStream) {
			try {
				final BitmapFactory.Options options = new BitmapFactory.Options();
			    options.inJustDecodeBounds = true;
			    BitmapFactory.decodeStream(inputStream, null, options);

			    // Calculate inSampleSize
			    options.inSampleSize = calculateInSampleSize(options, width, height);

			    // Decode bitmap with inSampleSize set
			    options.inJustDecodeBounds = false;
			    return BitmapFactory.decodeStream(inputStream, null, options);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}




}
