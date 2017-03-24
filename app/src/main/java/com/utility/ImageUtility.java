package com.utility;

import com.i_just_call_to_say.R;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageUtility {

	public static void setMaskImage(Activity activity, ImageView imageView, Bitmap original, Bitmap mask){
//		Resources resources = activity.getResources();
//		Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Config.ARGB_8888);
//		Canvas c = new Canvas(result);
//		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
//		c.drawBitmap(original, 0, 0, null);
//		c.drawBitmap(mask, 0, 0, paint);
//		paint.setXfermode(null);
//		imageView.setImageBitmap(result);
		
		
//		Bitmap original = BitmapFactory.decodeResource(getResources(),R.drawable.nature); //create bitmap from drawable
//		Bitmap mask = BitmapFactory.decodeResource(getResources(),R.drawable.mask);&nbsp; //create bitmap from drawable
		Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Config.ARGB_8888); //create another bitmap with same height
		//and width of the previous bitmap
		Canvas mCanvas = new Canvas(result);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		mCanvas.drawBitmap(original, 0, 0, null);
		mCanvas.drawBitmap(mask, 0, 0, paint);
		paint.setXfermode(null);
		imageView.setImageBitmap(result);
		imageView.setScaleType(ScaleType.CENTER);
		imageView.setBackgroundResource(R.drawable.frame_holder);
	}
}
