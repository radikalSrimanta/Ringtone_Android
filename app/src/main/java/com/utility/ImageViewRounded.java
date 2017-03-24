package com.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageViewRounded extends ImageView {///com.utility.view.ImageViewRounded
	private Context mContext;

	public ImageViewRounded(Context context) {
		super(context);
		mContext = context;
	}

	public ImageViewRounded(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public ImageViewRounded(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		BitmapDrawable drawable = (BitmapDrawable) getDrawable();

		if (drawable == null) {
			return;
		}

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		
		int originalBitmapHeight = 0;
		int originalBitmapWidth = 0;
		
		Bitmap fullSizeBitmap = drawable.getBitmap();
		
		if (fullSizeBitmap.getWidth() > fullSizeBitmap.getHeight()) {
			
			originalBitmapHeight = fullSizeBitmap.getWidth();
			
			originalBitmapWidth = fullSizeBitmap.getWidth();
			
		} else {
			
			originalBitmapWidth = fullSizeBitmap.getHeight();
			
			originalBitmapHeight = fullSizeBitmap.getHeight();
		}

		int scaledWidth = getMeasuredWidth();
		int scaledHeight = getMeasuredHeight();
		
		Bitmap mScaledBitmap = null;
		
		if (scaledWidth == fullSizeBitmap.getWidth() && scaledHeight == fullSizeBitmap.getHeight()) {
			
			mScaledBitmap = fullSizeBitmap;
			
		} else if (scaledWidth < fullSizeBitmap.getWidth() || scaledHeight < fullSizeBitmap.getHeight()) {
			
			mScaledBitmap = Bitmap.createScaledBitmap(fullSizeBitmap, scaledWidth, scaledHeight, true /* filter */);
			
		} else if(scaledWidth > fullSizeBitmap.getWidth() || scaledHeight > fullSizeBitmap.getHeight()) {
			
			mScaledBitmap = Bitmap.createScaledBitmap(fullSizeBitmap, originalBitmapWidth, originalBitmapHeight, true);
			
		}

		// Bitmap roundBitmap = getRoundedCornerBitmap(mScaledBitmap);

		// Bitmap roundBitmap = getRoundedCornerBitmap(getContext(),
		// mScaledBitmap, 10, scaledWidth, scaledHeight, false, false,
		// false, false);
		// canvas.drawBitmap(roundBitmap, 0, 0, null);

		Bitmap circleBitmap = getCircleImage(mScaledBitmap);

		canvas.drawBitmap(circleBitmap, 0, 0, null);

	}

	private Bitmap getCircleImage(Bitmap bitmap) {
		
		Bitmap originalBitmap = getCircledBitmap(bitmap);
		Bitmap backgroundBitmap = getBackCircledBitmap();
		int positionLeft, positionTop;
		positionLeft = (backgroundBitmap.getWidth() - originalBitmap.getWidth()) / 2;
		positionTop = (backgroundBitmap.getHeight() - originalBitmap.getHeight()) / 2;
		
		Canvas canvas = new Canvas(backgroundBitmap);
		canvas.drawBitmap(originalBitmap, positionLeft, positionTop, null);
		return backgroundBitmap;
	}
	
	public Bitmap getRoundedCornerBitmap(Context context, Bitmap input,
			int pixels, int w, int h, boolean squareTL, boolean squareTR, boolean squareBL, boolean squareBR) {

		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final float densityMultiplier = context.getResources().getDisplayMetrics().density;

		final int color = 0xff424242;

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);

		// make sure that our rounded corner is scaled appropriately
		final float roundPx = pixels * densityMultiplier;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		// draw rectangles over the corners we want to be square
		if (squareTL) {
			canvas.drawRect(0, 0, w / 2, h / 2, paint);
		}
		if (squareTR) {
			canvas.drawRect(w / 2, 0, w, h / 2, paint);
		}
		if (squareBL) {
			canvas.drawRect(0, h / 2, w / 2, h, paint);
		}
		if (squareBR) {
			canvas.drawRect(w / 2, h / 2, w, h, paint);
		}

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(input, 0, 0, paint);

		return output;
	}

	
	public Bitmap getBackCircledBitmap() {
		Bitmap result = Bitmap.createBitmap(getMeasuredWidth(),	getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		int color = Color.parseColor("#FFFFFF");
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, getMeasuredHeight()/2, paint);
//		canvas.drawBitmap((BitmapFactory.decodeResource(getResources(), R.drawable.profile_picture_mask)), 0, 0, null);
		return result;
	}

	Bitmap getCircledBitmap(Bitmap bitmap) {
		
		Bitmap result = Bitmap.createBitmap(bitmap.getWidth(),	bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);

		int color = Color.WHITE;
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getHeight()/2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return result;
	}

}