package com.i_just_call_to_say.activities.greetings;

import java.io.File;
import java.io.IOException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.audio_trimmer.CheapSoundFile;
import com.audio_trimmer.Utilities;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.AppConstant;

public class AudioPopupDIalog extends Activity {

	private RelativeLayout rel_audio,rel_record;
	private static final int AUDIO_REQEST=1001;
	private static final int RECORD_REQUEST=1002;
	private boolean mStartRecording;
	private MediaRecorder mRecorder = null;
	private String filepath;
	private File imagefile;
	private boolean isRecordedFile = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.audiopicker);
		initView();
	}
	
	private void initView(){
		rel_audio=(RelativeLayout)findViewById(R.id.rel_audio);
		rel_record=(RelativeLayout)findViewById(R.id.rel_record);
		
		rel_audio.setBackgroundResource(R.drawable.btn_audio_01_dark);
		rel_audio.setTag(R.drawable.btn_audio_01_dark);
		
		rel_record.setBackgroundResource(R.drawable.btn_record_01);
		rel_record.setTag(R.drawable.btn_record_01);
	}

	private void switchBackground(){
		if(Integer.parseInt(""+rel_audio.getTag()) == R.drawable.btn_audio_01_dark){
			rel_audio.setBackgroundResource(R.drawable.btn_audio_01);
			rel_audio.setTag(R.drawable.btn_audio_01);
		}else{
			rel_audio.setBackgroundResource(R.drawable.btn_audio_01_dark);
			rel_audio.setTag(R.drawable.btn_audio_01_dark);
		}
		
		if(Integer.parseInt(""+rel_record.getTag()) == R.drawable.btn_record_01){
			rel_record.setBackgroundResource(R.drawable.btn_record_01_dark);
			rel_record.setTag(R.drawable.btn_record_01_dark);
		}else{
			rel_record.setBackgroundResource(R.drawable.btn_record_01);
			rel_record.setTag(R.drawable.btn_record_01);
		}
	}
	
	
	
	public void onAudioClick(View v) {
		switchBackground();
		Intent intent = new Intent();
	    intent.setAction(android.content.Intent.ACTION_PICK);
	    intent.setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
	    startActivityForResult(intent, AUDIO_REQEST);
	}

	
	public void onRecordClick(View v) {
		switchBackground();
		Intent intent = new Intent(this , CaptureAudioScreen.class);
		intent.putExtra("file", getCreateFile().getAbsolutePath());
		startActivityForResult(intent, RECORD_REQUEST);
	}
	
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
		}
	
	private File getCreateFile(){
		File temp_dir = new File(Environment.getExternalStorageDirectory(),AppConstant.APP_RINGTONE_DIRECTORY);
		if(!temp_dir.exists()){
		temp_dir.mkdirs();
		}
		File temp_path = new File(temp_dir,"voice_record.wav");
		if (temp_path.exists())
		temp_path.delete();
		return temp_path;
	}
	
	private void startRecording() {
		filepath=Environment.getExternalStorageDirectory()+File.separator+getString(R.string.app_name);
		File file=new File(filepath);
		if(!file.exists()){
			file.mkdir();
		}
		
		imagefile=new File(file,System.currentTimeMillis()+".3gp");
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setAudioEncodingBitRate(60000);
		mRecorder.setAudioSamplingRate(60000);

		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

		mRecorder.setOutputFile(imagefile.getAbsolutePath());
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mRecorder.setMaxDuration(10000);
		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e("AAA", "prepare() failed");
		}
		mRecorder.start();
	}
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == RESULT_OK) {
				if (requestCode == RECORD_REQUEST) {
					if (data != null) {
						String savedUri = data.getStringExtra("path");
						System.out.println("savedUri "+savedUri);
//						callSetResult(trimAudioFile(savedUri));
						isRecordedFile = true;
						new AudioTrimming(savedUri).execute();
					}
				} else if (requestCode == AUDIO_REQEST) {
					Uri uri = data.getData();
					if (uri != null) {
						String ringTonePath = getPath(uri);
						isRecordedFile = false;
						System.out.println("ringTonePath " + getPath(uri));
//						callSetResult(trimAudioFile(ringTonePath));
						new AudioTrimming(ringTonePath).execute();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		finish();
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
		intent.putExtra("isRecordedFile", isRecordedFile);
		setResult(RESULT_OK, intent);
		finish();
	}

	
	
	private class AudioTrimming extends AsyncTask<Void, Void, String>{

		private String fileName;
		private ProgressDialog pd;
		
		public AudioTrimming(String filePath){
			this.fileName = filePath;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(AudioPopupDIalog.this,R.style.alertDialogTheme);
			pd.setCancelable(false);
			pd.setMessage("Please wait...       ");
			pd.show();
		}
		
		@Override
		protected String doInBackground(Void... params) {
			return trimAudioFile(fileName);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pd.dismiss();
			callSetResult(result);
		}
		
		private String getExtensionFromFilename(String filename) {
			return filename.substring(filename.lastIndexOf('.'), filename.length());
		}

		private File makeRingtoneFilename(String extension) {
			File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+File.separator+AppConstant.APP_RINGTONE_DIRECTORY);
			if (!mediaStorageDir.exists()) {
				if (!mediaStorageDir.mkdirs()) {
					System.out.println("Oops! Failed create directory");
					return null;
				}
			}
			File mediaFile = new File(mediaStorageDir.getPath()+ File.separator + "trim_audio" + extension);
			return mediaFile;
		 }
		
		private String trimAudioFile(String filepath) {
			try {
				MediaPlayer mp = MediaPlayer.create(AudioPopupDIalog.this, Uri.parse(filepath));
				if(mp !=null){
					int duration = mp.getDuration()/1000;
				
					if(duration>30){
					CheapSoundFile.ProgressListener listener = new CheapSoundFile.ProgressListener() {
					    public boolean reportProgress(double frac) {
	
					    return true; 
					    }
					};
					
					CheapSoundFile cheapSoundFile = CheapSoundFile.create(filepath,listener);
					System.out.println("sample rate "+cheapSoundFile.getSampleRate() +" sample/frame "+ cheapSoundFile.getSamplesPerFrame());
					int startFrame = Utilities.secondsToFrames(0.0,cheapSoundFile.getSampleRate(), cheapSoundFile.getSamplesPerFrame());
					int endFrame = Utilities.secondsToFrames(30.0, cheapSoundFile.getSampleRate(),cheapSoundFile.getSamplesPerFrame());
					File outFile = makeRingtoneFilename(getExtensionFromFilename(filepath));
					cheapSoundFile.WriteFile(outFile, startFrame, endFrame-startFrame);
					afterSavingRingtone(outFile.getAbsolutePath(), outFile, 30);
					return outFile.getAbsolutePath();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return filepath;
		}
	}
	
	private void afterSavingRingtone(String outPath, File outFile, int duration) {
		long fileSize = outFile.length();
		String mimeType = "audio/mpeg";
		String artist = "I Just Call To Say";
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, outPath);
		values.put(MediaStore.MediaColumns.TITLE,artist);
		values.put(MediaStore.MediaColumns.SIZE, fileSize);
		values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);

		values.put(MediaStore.Audio.Media.ARTIST, artist);
		values.put(MediaStore.Audio.Media.DURATION, duration);

		values.put(MediaStore.Audio.Media.IS_RINGTONE,3);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION,2);
		values.put(MediaStore.Audio.Media.IS_ALARM,1);
		values.put(MediaStore.Audio.Media.IS_MUSIC,	0);

		// Insert it into the database
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(outPath);
		final Uri newUri = getContentResolver().insert(uri, values);
		setResult(RESULT_OK, new Intent().setData(newUri));
	}
 }
