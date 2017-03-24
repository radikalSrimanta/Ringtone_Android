package com.i_just_call_to_say.activities.greetings;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.audiovisualizer.CDrawer;
import com.chipmunkrecord.ExtAudioRecorder;
import com.i_just_call_to_say.R;

public class CaptureAudioScreen extends Activity{//com.i_just_call_to_say.activities.greetings.CaptureAudioScreen

	private String outputFile;
	private Button startBtn;
	private Button stopBtn;
	private Button playBtn;
	private Button btn_back;
	private boolean isStopped = false;
	private ExtAudioRecorder exRecorder = null;
	private CDrawer.CDrawThread mDrawThread;
	private CDrawer mdrawer;
	private AudioTrack at;
	private FileInputStream fin;
	private DataInputStream dis;
	private PlayAudio playAudio;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.captureaudioscreen);
		initView();
		attachListener();
		readFromBundle();
		initializeRecorder();
		short[] buffer = new short[2048];
		setBuffer(buffer);
	}

	public void pauseVisualizer(){
        try {
			mdrawer.stopCDrawThread();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	@Override
	protected void onPause() {
		super.onPause();
	}
    
	public void setBuffer(short[] paramArrayOfShort) {
		mDrawThread = mdrawer.getThread();
		mDrawThread.setBuffer(paramArrayOfShort);
	}
    
	private void attachListener() {

		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				start(v);
			}
		});

		stopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stop(v);
				pauseVisualizer();
			}
		});

		playBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				play(v);
				pauseVisualizer();
			}
		});
		
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}
	
	@Override
	protected void onStop() {
		super.onStop();
		try {
			stopPlay(stopBtn);
			if (dis != null)
				dis.close();
			if (fin != null)
				fin.close();
			if (at != null && at.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
				at.flush();
				at.stop();
				at.release();
				at = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!isStopped) {
			Toast.makeText(getApplicationContext(),"Your recording is not saved", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onBackPressed() {
		try {
			
			if(exRecorder !=null && exRecorder.getState() == ExtAudioRecorder.State.RECORDING){
				exRecorder.stop();
				exRecorder.reset();
				exRecorder.release();
				exRecorder = null;
				File file = new File(outputFile);
				if(file.exists()){
					file.delete();
				}
				outputFile = "";
			}
			pauseVisualizer();
			stopPlay(stopBtn);
			if (dis != null)
				dis.close();
			if (fin != null)
				fin.close();
			if (at != null && at.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
				at.flush();
				at.stop();
				at.release();
				at = null;
			}
			Intent intent = new Intent();
			intent.putExtra("path", outputFile);
			System.out.println("path " + outputFile);
			setResult(RESULT_OK, intent);
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void initView(){
		 startBtn = (Button)findViewById(R.id.start);
		 stopBtn = (Button)findViewById(R.id.stop);
		 playBtn = (Button)findViewById(R.id.play);
		 btn_back = (Button) findViewById(R.id.btn_back);
		 mdrawer = (CDrawer) findViewById(R.id.drawer);
	}
	
	private void readFromBundle(){
		outputFile = getIntent().getStringExtra("file");
	}
	
	private void initializeRecorder(){
		exRecorder = ExtAudioRecorder.getInstanse(false);
		exRecorder.setCurrentActivity(this);
		exRecorder.setOutputFile(outputFile);
	}
	
	public void start(View view) {
		try {
			exRecorder.prepare();
			exRecorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		startBtn.setEnabled(false);
		stopBtn.setEnabled(true);
	}

	public void stop(View view) {
		try {
			mdrawer.setVisibility(View.INVISIBLE);
			mdrawer.Restart();
			isStopped = true;
			exRecorder.stop();
			exRecorder.reset();
			exRecorder.release();
			stopBtn.setEnabled(false);
			playBtn.setEnabled(true);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
	  
	public void play(View view) {
		try {
		   playAudio = new PlayAudio(outputFile);
		   playAudio.execute();
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	   
	public short bytesToShort(byte[] bytes) {
	     return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}
	
	private short[] short2byte(byte[] byteArray) {
		short[] shortArray = new short[byteArray.length / 2];
		ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortArray);
		return shortArray;
	}

	public void stopPlay(View view) {
		try {
			if(playAudio !=null)
				playAudio.cancel(true);
			if (at != null) {
				at.flush();
				at.stop();
				at.release();
				at = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class PlayAudio extends AsyncTask<Void, Void, Void>{

		private String outputFile;
		public PlayAudio(String outputFile){
			this.outputFile = outputFile;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			playBtn.setEnabled(false);
			mdrawer.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			int minBufferSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
		    int bufferSize = 10584;
		    at = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);
		    int i = 0;
		    byte[] s = new byte[bufferSize];
		    try {
		        fin = new FileInputStream(outputFile);
		        dis = new DataInputStream(fin);
		        at.play();
		        while((i = dis.read(s, 0, bufferSize)) > -1){
		            setBuffer(short2byte(s));
		        	at.write(s, 0, i);
		        }
		        at.flush();
		        at.stop();
		        at.release();
		        dis.close();
		        fin.close();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }				
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			playBtn.setEnabled(true);
			mdrawer.setVisibility(View.INVISIBLE);
		}
	}
	
	
}