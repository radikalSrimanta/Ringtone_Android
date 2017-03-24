package com.audiovisualizer;

/**
 *  This is the drawer for the visualizer
 *  @author Pontus Holmberg (EndLessMind)
 *  Email: the_mr_hb@hotmail.com
 **/

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CDrawer extends SurfaceView implements SurfaceHolder.Callback {// /com.soundvisualizer.CDrawer

	private Context mContext;
	private CDrawThread mDrawThread;
	private SurfaceHolder mHolder;
	private Boolean isCreated = false;

	/**
	 * This is where you instance the drawer You relly don't need to care about
	 * the parameters, they are set in the xml-layout
	 * 
	 * @param Apply the baseContext of you current acitivty
	 * @param AttributeSet
	 */
	public CDrawer(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		setZOrderOnTop(true);
		getHolder().setFormat(PixelFormat.RGBA_8888);
		System.out.println("CDrawer()");
		mHolder = getHolder();
		mContext = paramContext;
		mHolder.addCallback(this);
		mDrawThread = new CDrawThread(mHolder, paramContext, new Handler() {
			public void handleMessage(Message paramMessage) {
			}
		});
		setFocusable(true);
	}

	/**
	 * restarts the thread
	 * 
	 * @param Is the thread dead?
	 */
	public void Restart() {
		if (isCreated) {
			mHolder = getHolder();
			mHolder.addCallback(this);
			mDrawThread = new CDrawThread(mHolder, mContext, new Handler() {
				public void handleMessage(Message paramMessage) {
				}
			});
		}
	}

	public CDrawThread getThread() {
		return mDrawThread;
	}

	public void stopCDrawThread() {
		try {
			if (mDrawThread != null) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called when there's a change in the surface
	 */
	public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1,	int paramInt2, int paramInt3) {
		mDrawThread.setSurfaceSize(paramInt2, paramInt3);
	}

	/**
	 * Creates the surface
	 */
	public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
		isCreated = true;
	}

	/**
	 * Surface destroyd
	 */
	public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
		isCreated = false;
	}

	/**
	 * The Drawer Thread, subclass to cDrawer class We want to keep most of this
	 * process in a background thread, so the UI don't hang
	 * 
	 */
	public class CDrawThread /* extends Thread */{
		private Paint mBackPaint;
		private Bitmap mBackgroundImage;
		private short[] mBuffer;
		private int mCanvasHeight = 1;
		private Paint mLinePaint;
		private SurfaceHolder mSurfaceHolder;
		private int m_iScaler = 3;

		/**
		 * Instance the Thread All the parameters i handled by the cDrawer class
		 * 
		 * @param paramContext
		 * @param paramHandler
		 * @param arg4
		 */

		public CDrawThread(SurfaceHolder paramContext, Context paramHandler, Handler arg4) {
			mSurfaceHolder = paramContext;
			mLinePaint = new Paint();
			mLinePaint.setAntiAlias(true);
			mLinePaint.setARGB(255, 195, 0, 0);
			mBackPaint = new Paint();
			mBackPaint.setAntiAlias(true);
			mBackPaint.setColor(Color.parseColor("#F1F1F1"));
			mBuffer = new short[2048];
			mBackgroundImage = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		}

		/**
		 * Allow you to change the size of the waveform displayed on the screen
		 * Or scale of you so will
		 * 
		 * @return returns a new scale value
		 */
		public int ChangeSensitivity() {
			m_iScaler = (2 + m_iScaler);
			if (m_iScaler > 20)
				m_iScaler = 1;
			return m_iScaler;
		}

		/**
		 * Calculate and draws the line
		 * 
		 * @param Canvas
		 *            to draw on, handled by cDrawer class
		 */
		public void doDraw(Canvas paramCanvas) {
			if (mCanvasHeight == 1)
				mCanvasHeight = paramCanvas.getHeight();
			paramCanvas.drawPaint(mBackPaint);
			/**
			 * Set some base values as a starting point This could be considerd
			 * as a part of the calculation process
			 */
			int height = paramCanvas.getHeight();
			int BuffIndex = (mBuffer.length / 2 - paramCanvas.getWidth()) / 2;
			int width = paramCanvas.getWidth();
			int mBuffIndex = BuffIndex;
			int scale = height / m_iScaler;
			int StratX = 0;
			if (StratX >= width) {
				paramCanvas.save();
				return;
			}
			int cu1 = 0;
			/**
			 * Here is where the real calculations is taken in to action In this
			 * while loop, we calculate the start and stop points for both X and Y
			 * 
			 * The line is then drawer to the canvas with drawLine method
			 */
			while (StratX < width - 1) {
				try {
					int StartBaseY = mBuffer[(mBuffIndex - 1)] / scale;
					int StopBaseY = mBuffer[mBuffIndex] / scale;
					if (StartBaseY > height / 2) {
						StartBaseY = 2 + height / 2;
						int checkSize = height / 2;
						if (StopBaseY <= checkSize)
							return;
						StopBaseY = 2 + height / 2;
					}
					int StartY = StartBaseY + height / 2;
					int StopY = StopBaseY + height / 2;
					paramCanvas.drawLine(StratX, StartY, StratX + 1, StopY,	mLinePaint);
					cu1++;
					mBuffIndex++;
					StratX++;
					int checkSize_again = -1 * (height / 2);
					if (StopBaseY >= checkSize_again)
						continue;
					StopBaseY = -2 + -1 * (height / 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * Updated the Surface and redraws the new audio-data
		 */
		public void run() {
			Canvas localCanvas = null;
			try {
				localCanvas = mSurfaceHolder.lockCanvas(null);
				synchronized (mSurfaceHolder) {

					if (localCanvas != null)
						doDraw(localCanvas);

				}
			} finally {
				if (localCanvas != null)
					mSurfaceHolder.unlockCanvasAndPost(localCanvas);
			}

		}

		public void setBuffer(short[] paramArrayOfShort) {
			synchronized (mBuffer) {
				mBuffer = paramArrayOfShort;
				run();
				return;
			}
		}

		public void setSurfaceSize(int paramInt1, int paramInt2) {
			synchronized (mSurfaceHolder) {
				mCanvasHeight = paramInt2;
				mBackgroundImage = Bitmap.createScaledBitmap(mBackgroundImage,
						paramInt1, paramInt2, true);
				return;
			}
		}
	}
}