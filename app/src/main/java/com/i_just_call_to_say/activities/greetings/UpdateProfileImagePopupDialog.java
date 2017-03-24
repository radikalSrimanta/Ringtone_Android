package com.i_just_call_to_say.activities.greetings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.imagecaptured.CameraActivity;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.ILayoutInterface.IAddImageInterface;
import com.i_just_call_to_say.activities.base.AppConstant;
import com.utility.AlertDialogCallBack;
import com.utility.CommonUtility;
import com.utility.DialogUtility;
import com.utility.GoogleImageProcessUtility;
import com.utility.GoogleImageProcessUtility.OnProcessCompleteListener;

public class UpdateProfileImagePopupDialog extends Activity implements IAddImageInterface{//com.i_just_call_to_say.activities.greetings.ImageSelectionProfilePopupDialog

	private static final int PICTURE_GALLERY_REQUEST = 2572;
	private static final int CAMERA_PIC_REQUEST = 1337;
	public String picturePath = "";
	private RelativeLayout  rel_gallery,rel_camera;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.change_picture_dialog_layout);
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void initView(){
		rel_gallery =(RelativeLayout)findViewById(R.id.rel_gallery);
		rel_camera=(RelativeLayout)findViewById(R.id.rel_camera);
		
		rel_gallery.setBackgroundResource(R.drawable.btn_image_01_dark);
		rel_gallery.setTag(R.drawable.btn_image_01_dark);
		
		rel_camera.setBackgroundResource(R.drawable.btn_takepic_01);
		rel_camera.setTag(R.drawable.btn_takepic_01);
	}

	private void switchBackground(){
		if(Integer.parseInt(""+rel_gallery.getTag()) == R.drawable.btn_image_01_dark){
			rel_gallery.setBackgroundResource(R.drawable.btn_image_01);
			rel_gallery.setTag(R.drawable.btn_image_01);
		}else{
			rel_gallery.setBackgroundResource(R.drawable.btn_image_01_dark);
			rel_gallery.setTag(R.drawable.btn_image_01_dark);
		}
		
		if(Integer.parseInt(""+rel_camera.getTag()) == R.drawable.btn_takepic_01){
			rel_camera.setBackgroundResource(R.drawable.btn_takepic_01_dark);
			rel_camera.setTag(R.drawable.btn_takepic_01_dark);
		}else{
			rel_camera.setBackgroundResource(R.drawable.btn_takepic_01);
			rel_camera.setTag(R.drawable.btn_takepic_01);
		}
	}
	

	private String saveScaledBitMap(Bitmap bitmap, String extension) {
		FileOutputStream out = null;
		try {

			File cacheDir;

			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				cacheDir = new File(Environment.getExternalStorageDirectory(), AppConstant.APP_IMAGE_DIRECTORY);
			else
				cacheDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
			if (!cacheDir.exists())
				cacheDir.mkdirs();
			
			try {
				File noMediaFile = new File(cacheDir, ".nomedia");
				if(!noMediaFile.exists())
					noMediaFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}

			String filename = java.lang.System.currentTimeMillis() + extension;// ".png";
			File file = new File(cacheDir, filename);
			//temp_path = file.getAbsoluteFile();
			// if(!file.exists())
			file.createNewFile();

			out = new FileOutputStream(file);
			if (bitmap != null) {
				//System.out.println("captured bitmap height " + bitmap.getHeight() + "     weight: " + bitmap.getWidth());
				bitmap.compress(Bitmap.CompressFormat.JPEG, AppConstant.COMPRESS, out);
				bitmap = null;
				return file.getAbsolutePath();
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
	
	private int checkFileSize(String filepath) {
		try {
			File filenew = new File(filepath);
			int file_size = Integer.parseInt(String.valueOf(filenew.length() / 1024));
			// System.out.println("Size of : " + file_size);
			return file_size;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private void sendImagePath( String filePath , final boolean isVideo){
		int fileSize = checkFileSize(filePath);
		System.out.println("upload file size is  "+fileSize +"      in mb: "+(fileSize/2014));
		if(fileSize < 1024 * 2){
			////finishAndSetResult(RESULT_OK,filePath,true);
			finishAndSetResult((filePath == null || filePath.length() == 0) ? RESULT_CANCELED:RESULT_OK, filePath, isVideo);
		}else{
			
			DialogUtility.showMessageOkWithCallback("You cannot upload images/videos of size larger then 1 MB.", this, new AlertDialogCallBack() {
				
				@Override
				public void onSubmit() {
					finishAndSetResult(RESULT_CANCELED, "", isVideo);//9038472331
					
				}
				
				@Override
				public void onCancel() {
					finishAndSetResult(RESULT_CANCELED, "", isVideo);
				}

				@Override
				public void onSubmitWithEditText(String text) {
					
				}
			});
		}
	}
	
	
	public void finishAndSetResult(int result , String path, boolean isVideo) {
		System.out.println("IMG finishAndSetResult called " + isVideo + "  Path: " + path);
		Intent intent = getIntent();
		intent.putExtra("path", path);
		intent.putExtra("isVideo", isVideo);
		
		//if(isVideo){
			if(path != null && path.length()>0)
				setResult(result, intent);
			else
				setResult(RESULT_CANCELED);
			finish();
//		}else{
//			Bundle bundle = new Bundle();
//			bundle.putString("data", path);
//			ActivityController.startNextActivityForResult(this, ImageModificationDialog.class, bundle, false,MODIFY_SELECT_IMAGE);
//		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		System.out.println("onActivityResult");
		if(resultCode == RESULT_OK){
			switch (requestCode) {
			case CAMERA_PIC_REQUEST:
				if (data != null) {
					picturePath = data.getStringExtra("path");
					System.out.println("**********path->"+picturePath);
					picturePath = saveScaledBitMap(CommonUtility.decodeFile(AppConstant.IMAGE_WIDTH, AppConstant.IMAGE_HEIGHT, picturePath),picturePath.substring(picturePath.indexOf("."), picturePath.length()));
					sendImagePath(picturePath,false);
				} 
				break;
			case PICTURE_GALLERY_REQUEST:
				if (data != null && data.getData() !=null) {
					if (Build.VERSION.SDK_INT <19){
						Uri selectedImage = data.getData();
						String[] filePathColumn = { MediaStore.Images.Media.DATA };
						Cursor cursor = getContentResolver().query(selectedImage,	filePathColumn, null, null, null);
						cursor.moveToFirst();
						int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
						picturePath = cursor.getString(columnIndex);
						cursor.close();
						if(picturePath != null){
							picturePath = saveScaledBitMap(CommonUtility.decodeFile(AppConstant.IMAGE_WIDTH, AppConstant.IMAGE_HEIGHT, picturePath),picturePath.substring(picturePath.indexOf("."), picturePath.length()));
							if(picturePath != null){
								sendImagePath(picturePath,false);
							}else
								Toast.makeText(UpdateProfileImagePopupDialog.this, "No image is selected", 2000).show();
						}else{
							GoogleImageProcessUtility googleImageProcessUtility = new GoogleImageProcessUtility(UpdateProfileImagePopupDialog.this,selectedImage.toString(),new OnProcessCompleteListener() {
								
								@Override
								public void onProcessComplete(String path) {
									if(path != null){
										sendImagePath(path,false);
									}else{
										Toast.makeText(UpdateProfileImagePopupDialog.this, "No image is selected", 2000).show();
									}
								}
							});
							googleImageProcessUtility.execute();
						}
					}else{
						try {
							InputStream imInputStream =  getContentResolver().openInputStream(data.getData());
							//Bitmap bitmap = CommonUtility.decodeFile(IMAGE_WIDTH, IMAGE_HEIGHT,imInputStream);
							BitmapFactory.Options options = new BitmapFactory.Options();
							options.inSampleSize = 2;
							options.inPreferredConfig = Config.RGB_565;
							Bitmap bitmap = BitmapFactory.decodeStream(imInputStream, null,options);
							picturePath = saveGalaryImageOnLitkat(bitmap);
							
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
					if(picturePath != null)
						sendImagePath(picturePath,false);
					else{
						Toast.makeText(UpdateProfileImagePopupDialog.this, "No image is selected", 2000).show();
					}
				}
				break;
		}
		}
		
	}
	
	
	private String saveGalaryImageOnLitkat(Bitmap bitmap){
		try{
			File cacheDir;
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				cacheDir=new File(Environment.getExternalStorageDirectory(),AppConstant.APP_IMAGE_DIRECTORY);
			else
				cacheDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
			if(!cacheDir.exists())
				cacheDir.mkdirs();
			
			try {
				File noMediaFile = new File(cacheDir, ".nomedia");
				if(!noMediaFile.exists())
					noMediaFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String filename=java.lang.System.currentTimeMillis()+".jpg";
			File file = new File(cacheDir, filename);
			//temp_path = file.getAbsoluteFile();
			// if(!file.exists())
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, AppConstant.COMPRESS, out);
			return file.getAbsolutePath();
			}catch(Exception e){
				e.printStackTrace();
			}
		
		return null;

	}
	
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader cursorLoader = new CursorLoader(this,contentUri, proj, null, null, null);        
		Cursor cursor = cursorLoader.loadInBackground();
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index); 
	}
	
	private void callSetResult(String filepath){
		System.out.println("filepathnew"+filepath);
		Intent intent=new Intent();
		intent.putExtra("filepath", filepath);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	private void startImageCaptured(){
		Intent cameraActivity = new Intent(this,CameraActivity.class);
		cameraActivity.putExtra("path", getCreateFile().getAbsolutePath());
		startActivityForResult(cameraActivity,CAMERA_PIC_REQUEST);
	}
	
	private void loadImageFromAlbum(){
		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, PICTURE_GALLERY_REQUEST);
	}
	
	
	private File getCreateFile(){
		File temp_dir = new File(Environment.getExternalStorageDirectory(),AppConstant.APP_IMAGE_DIRECTORY);
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
		
		File temp_path = new File(temp_dir,java.lang.System.currentTimeMillis()+".jpg");
		if (temp_path.exists())
			temp_path.delete();
		return temp_path;
	}

	@Override
	public void onGalleryClick(View v) {
		switchBackground();
		loadImageFromAlbum();
	}

	@Override
	public void onCameraClick(View v) {
		switchBackground();
		startImageCaptured();
	}

	@Override
	public void onRemoveClick(View v) {
		
	}
	

}
