package com.imagecaptured;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.i_just_call_to_say.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CameraActivity extends Activity{//com.imagecaptured.CameraActivity

	private Camera mCamera;
	private CameraPreview mPreview;
	private FrameLayout camera_preview;
	private ImageView iv_preview,iv_preview_back;
	private String fileName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cameraactivity);
		
		initView();
		
		updateButtonStatus(false);
		if(getIntent() != null){
			fileName = getIntent().getExtras().getString("path"); 
		}
	}


	private void initView() {
		camera_preview = (FrameLayout) findViewById(R.id.camera_preview);
		iv_preview = (ImageView)findViewById(R.id.iv_preview);
		iv_preview_back = (ImageView)findViewById(R.id.iv_preview_back);
		
	}
	
	private void initCamera(){
		// Create an instance of Camera
		mCamera = getCameraInstance();
		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera);
		
		if(camera_preview != null){
			camera_preview.removeAllViews();
			camera_preview.addView(mPreview);
		}
		
		
	}


	@Override
	protected void onResume() {
		super.onResume();
		initCamera();
	}


	private void updateButtonStatus(boolean isSnapCaptured){
		if(isSnapCaptured){
			((Button)findViewById(R.id.button_capture)).setVisibility(View.GONE);
			iv_preview_back.setVisibility(View.VISIBLE);
			iv_preview.setVisibility(View.VISIBLE);
			((LinearLayout)findViewById(R.id.btn_footer)).setVisibility(View.VISIBLE);
		}else{
			((Button)findViewById(R.id.button_capture)).setVisibility(View.VISIBLE);
			iv_preview_back.setVisibility(View.GONE);
			iv_preview.setVisibility(View.GONE);
			((LinearLayout)findViewById(R.id.btn_footer)).setVisibility(View.GONE);
		}
	}



	public void onButtonClick(View view){
		switch (view.getId()) {
		case R.id.button_capture:
			mCamera.takePicture(null, null, mPicture);
			break;
		case R.id.button_cancel:
			mPreview.resetPreview();
			updateButtonStatus(false);	
			break;
		case R.id.button_save:
			realeaseCamera();
			new MySavingAsycTask().execute();
			break;

		}
	}


	class MySavingAsycTask extends AsyncTask<Void, Void, Void>{
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			runOnUiThread(new Runnable() {
				public void run() {
					progressDialog = new ProgressDialog(CameraActivity.this,R.style.alertDialogTheme);
					progressDialog.setMessage("Saving...        ");
					progressDialog.setCancelable(false);
					progressDialog.show();
				}
			});
			System.out.println("Image start saving");
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			runOnUiThread(new Runnable() {
				public void run() {
					if(progressDialog.isShowing())
						progressDialog.dismiss();
					System.out.println("image save completed");
					finishAndSetResult(RESULT_OK, fileName);
				}
			});
		}

		@Override
		protected Void doInBackground(Void... params) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable)iv_preview.getDrawable();
			if(bitmapDrawable != null)
				saveScaledBitMap(fileName,bitmapDrawable.getBitmap());
			return null;
		}

	}


	class MyLodingAsycTask extends AsyncTask<Void, Void, Void>{
		private ProgressDialog progressDialog;
		private byte [] data;

		public MyLodingAsycTask(byte [] picData){
			data = picData;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			runOnUiThread(new Runnable() {
				public void run() {
					progressDialog = new ProgressDialog(CameraActivity.this,R.style.alertDialogTheme);
					progressDialog.setMessage("Loading...        ");
					progressDialog.setCancelable(false);
					progressDialog.show();
				}
			});
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			runOnUiThread(new Runnable() {
				public void run() {
					if(progressDialog.isShowing())
						progressDialog.dismiss();
					closeCamera();
				}
			});
		}

		@Override
		protected Void doInBackground(Void... params) {
			File pictureFile = new File(fileName);
			try {
				if(!pictureFile.exists() )
					pictureFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	public void finishAndSetResult(int result , String path) {
		Intent intent = getIntent();
		intent.putExtra("path", path);
		setResult(result, intent);
		finish();
	}


	private void realeaseCamera(){
		if(mCamera != null)
			mCamera.release();
		mCamera = null;
	}


	private Bitmap decodeFile(int width, int height, String filePath) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, o);

			// Find the correct scale value. It should be the power of 2.
			int REQUIRED_Height = height;
			int REQUIRED_Widtht = width;
			int scale = 1;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			// System.out.println("Scale:  width_tmp  " + width_tmp +
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
			return BitmapFactory.decodeFile(filePath, o2);
		} catch (Exception e) {
			// System.out.println("--- exception "+e.toString());
			e.printStackTrace();
		}
		return null;
	}

	public void saveScaledBitMap(String filePath,Bitmap bitmap){
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			bitmap = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Throwable ignore) {
			}
		}
	}

	private void closeCamera(){
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		Bitmap returnBitmap = decodeFile(width,height,fileName);
		if(returnBitmap != null)
			exifToDegrees(fileName, returnBitmap, iv_preview);
		updateButtonStatus(true);
	}

	private  void exifToDegrees(String filePath , Bitmap  bitmap , ImageView imageView) { 
		Matrix matrix = new Matrix();
		try{
			int exifOrientation = 1;
			ExifInterface exif = new ExifInterface(filePath);
			exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

			switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				matrix.postRotate(90);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				matrix.postRotate(180);	
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				matrix.postRotate(270);
				break;
			}
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		}catch(Throwable e){
			e.printStackTrace();
		}

		imageView.setImageBitmap(bitmap);
	}



	@Override
	protected void onPause() {
		super.onPause();
		//closeCamera();
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		}
		catch (Exception e){
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}


	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			new MyLodingAsycTask(data).execute();
		}
	};

}
