package com.imagecaptured;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.AppConstant;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class DialogChoosePictureWithRemove extends Activity {//com.imagecaptured.DialogChoosePicture

	private static final int VIDEO_PIC_REQUEST = 1551;
	private static final int CAMERA_PIC_REQUEST = 1337;
	private static final int PICTURE_GALLERY_REQUEST = 2572;
	private Button btnCancel, btnCamera, btnLibrary/*,btnVideo*/;
	public Bitmap mBitmap;
	String picturePath = "";
	private Uri uri;
	int customeLayoutId, cameraBtnId = -1, galleryBtnId = -1, cancelBtnId = -1;
	private File temp_dir = new File(Environment.getExternalStorageDirectory(),AppConstant.APP_IMAGE_DIRECTORY);
	private File temp_path;
	
	private OnClickListener buttonListener = new OnClickListener() {
		public void onClick(View v) {
			if(!temp_dir.exists()){
				temp_dir.mkdirs();
			}
			
			try {
				File noMediaFile = new File(temp_dir, ".nomedia");
				if(!noMediaFile.exists())
					noMediaFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			temp_path = new File(temp_dir,""+new Date().getTime());
			if (temp_path.exists())
				temp_path.delete();
			
			if (cameraBtnId == v.getId()) {
				
//				Intent cameraIntent = new Intent(
//						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//						Uri.fromFile(temp_path));
//				startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
				startCamera();
			} else if (galleryBtnId == v.getId()) {
				System.out.println("On galleryBtnId");
				
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, PICTURE_GALLERY_REQUEST);
								
				
			} else if (cancelBtnId == v.getId()) {
				System.out.println("On Cancle");
				finish();
			}/*else if(videoGalaryBtnID == v.getId()){
				Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
				//comma-separated MIME types
				mediaChooser.setType("video/*");
				startActivityForResult(mediaChooser, VIDEO_PIC_REQUEST);
			}*/
			
			System.out.println("On ID "+v.getId());
		}
	};
	
	private void startCamera(){
		Intent cameraActivity = new Intent(this,CameraActivity.class);
		cameraActivity.putExtra("path", temp_path.getAbsolutePath());
		startActivityForResult(cameraActivity,CAMERA_PIC_REQUEST);
	}

	public void onConfigurationChanged(
			android.content.res.Configuration newConfig) {
		Log.e("onConfigurationChanged ", "onConfigurationChanged happens");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("IMG", "Dialog Created");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Intent intent = getIntent();
		customeLayoutId = intent.getIntExtra("Custome Layout", -1);
		if (customeLayoutId != -1) {
			setContentView(customeLayoutId);
			cameraBtnId = intent.getIntExtra("CameraBtnId", -1);
			galleryBtnId = intent.getIntExtra("GalleryBtnId", -1);
			cancelBtnId = intent.getIntExtra("CancelBtnId", -1);
			if (cameraBtnId != -1 && galleryBtnId != -1 && cancelBtnId != -1)
				findControls(cameraBtnId, galleryBtnId, cancelBtnId/*,videoGalaryBtnID*/);
		} else {
//			setContentView(R.layout.choose_picture_main);
//			getWindow().setBackgroundDrawableResource(R.drawable.pic_chooser_bg);
//			cameraBtnId = R.id.btnChoosePicCamera;
//			galleryBtnId = R.id.btnChoosePicLibrary;
//			cancelBtnId = R.id.btnChoosePicCancel;
//			videoGalaryBtnID = R.id.btnChooseVedio;
//			
//			findControls(cameraBtnId, galleryBtnId, cancelBtnId,videoGalaryBtnID);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setBackgroundDrawableResource(android.R.color.transparent);
			setContentView(com.i_just_call_to_say.R.layout.imagepicker);
			//getWindow().setBackgroundDrawableResource(R.drawable.pic_chooser_bg);
			cameraBtnId = R.id.rel_camera;
			galleryBtnId = R.id.rel_gallery;
			cancelBtnId = R.id.rel_remove;
			findControls(cameraBtnId, galleryBtnId, cancelBtnId);
		}
		setListeners();
	}

	private void setListeners() {
		btnCamera.setOnClickListener(buttonListener);
		btnLibrary.setOnClickListener(buttonListener);
		btnCancel.setOnClickListener(buttonListener);
		//btnVideo.setOnClickListener(buttonListener);
	}

//	private void findControls(int cameraBtnId, int GalleryBtnId, int CancelBtnId, int videoGalaryBtnID) {
//		btnCancel = (Button) findViewById(CancelBtnId);
//		btnLibrary = (Button) findViewById(GalleryBtnId);
//		btnCamera = (Button) findViewById(cameraBtnId);
//		btnVideo = (Button) findViewById(videoGalaryBtnID);
//	}
	
	
	private void findControls(int cameraBtnId, int GalleryBtnId, int CancelBtnId) {
		btnCancel = (Button) findViewById(CancelBtnId);
		btnLibrary = (Button) findViewById(GalleryBtnId);
		btnCamera = (Button) findViewById(cameraBtnId);
	}

	public void onActivityResult(final int requestCode,
			int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Log.e("Request ", "Request " + requestCode + " " + resultCode + data);
			switch (requestCode) {
			case CAMERA_PIC_REQUEST:
				if (data != null) {
					picturePath = data.getStringExtra("path");
					finishAndSetResult(RESULT_OK, picturePath, false);
//					cursor.close();
				} else
					picturePath = temp_path.getAbsolutePath();
					finishAndSetResult(RESULT_OK, picturePath, false);
				break;
			case PICTURE_GALLERY_REQUEST:
				if (data != null) {
					if (Build.VERSION.SDK_INT <19){
						Uri selectedImage = data.getData();
						System.out.println("selectedImage "+selectedImage);
						String[] filePathColumn = { MediaStore.Images.Media.DATA };
						Cursor cursor = getContentResolver().query(selectedImage,	filePathColumn, null, null, null);
						cursor.moveToFirst();
						int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
						picturePath = cursor.getString(columnIndex);
						cursor.close();
						finishAndSetResult(RESULT_OK, picturePath, false);
					}else{
						try {
							InputStream imInputStream =  getContentResolver().openInputStream(data.getData());
							Bitmap bitmap = BitmapFactory.decodeStream(imInputStream);
							picturePath = saveGalaryImageOnLitkat(bitmap);
							
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						finishAndSetResult(RESULT_OK, picturePath, false);
					}
				} else{
					picturePath = temp_path.getAbsolutePath();
					finishAndSetResult(RESULT_CANCELED, picturePath, false);
				}
				break;
			case VIDEO_PIC_REQUEST:
				if(data != null){
					Uri vedioLocation = data.getData();
					this.uri = vedioLocation;
					System.out.println("Vedio Location : " + vedioLocation);
					String vedioFilePath = getRealPathFromURI(vedioLocation);
					finishAndSetResult(RESULT_OK,vedioFilePath,true);
				}else{
					finishAndSetResult(RESULT_CANCELED, "", true);
				}
				
			default:
				finish();
				break;
			}
		} else {
			finish();
			Log.w("DialogChoosePicture", "Warning: activity result not ok");
		}
	}
	
	
	private String saveGalaryImageOnLitkat(Bitmap bitmap){
		try{
			File cacheDir;

			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			cacheDir=new File(Environment.getExternalStorageDirectory(),getResources().getString(R.string.app_name));
			else
			cacheDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
			if(!cacheDir.exists())
			cacheDir.mkdirs();

			String filename=java.lang.System.currentTimeMillis()+".png";
			File file = new File(cacheDir, filename);
			temp_path = file.getAbsoluteFile();
			// if(!file.exists())
			file.createNewFile();
			System.out.println("screen shot "+bitmap);
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			return file.getAbsolutePath();
			}catch(Exception e){
				e.printStackTrace();
			}
		
		return null;

	}
		
		private Uri getUri() {
		    String state = Environment.getExternalStorageState();
		    if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
		        return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

		    return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		}

	public String getRealPathFromURI(Uri contentUri) {
	    String[] proj = { MediaStore.Images.Media.DATA };
	    Cursor cursor = managedQuery(contentUri, proj, null, null, null);
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
	
	public void finishAndSetResult(int result , String path, boolean isVideo) {
		Log.e("IMG", "finishAndSetResult called " + isVideo + "  Path: " + path);
		Intent intent = getIntent();
		intent.putExtra("path", path);
		intent.putExtra("isVideo", isVideo);
		setResult(result, intent);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e("IMG", "Dialog Destroyed");
	}
}