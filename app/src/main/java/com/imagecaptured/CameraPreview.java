package com.imagecaptured;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Activity context;

    public CameraPreview(Activity context, Camera camera) {
        super(context);
        mCamera = camera;
        this.context = context;
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
        	resetPreview();
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            //Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }
    
    
    protected void setDisplayOrientation(Camera camera, int angle){
        Method downPolymorphic;
        try
        {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
            if (downPolymorphic != null)
                downPolymorphic.invoke(camera, new Object[] { angle });
        }
        catch (Exception e1){
        }
    }
    
    public void resetPreview(){
    	 // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null || mCamera == null){
          // preview surface does not exist
          return;
        }

        
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }
        Camera.Parameters param = mCamera.getParameters();
        Camera.CameraInfo info  = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = context.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
        case Surface.ROTATION_0:
            degrees = 0;
            break;
        case Surface.ROTATION_90:
            degrees = 90;
            break;
        case Surface.ROTATION_180:
            degrees = 180;
            break;
        case Surface.ROTATION_270:
            degrees = 270;
            break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        //params.setRotation(result);
        
        
        
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
          {
              param.set("orientation", "portrait");
              param.set("rotation", result);
          }
          if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
          {
              param.set("orientation", "landscape");
              param.set("rotation", result);
          }
        
        if(param.getSupportedFlashModes() != null && param.getSupportedFlashModes().contains(Parameters.FLASH_MODE_AUTO)){
        	param.setFlashMode(Parameters.FLASH_MODE_AUTO);
        }
        
        if(param.getSupportedFocusModes() != null && param.getSupportedFocusModes().contains(Parameters.FOCUS_MODE_AUTO)){
        	param.setFlashMode(Parameters.FOCUS_MODE_AUTO);
        }
        param.set("jpeg-quality", 100);
        param.setPictureFormat(PixelFormat.JPEG);
        param.setJpegQuality(100);
        param.setJpegThumbnailQuality(100);
       // System.out.println("---+ "+getWidth()+"   "+getHeight());
       // param.setPictureSize(getWidth(), getHeight());
        param.setRotation(result);
        
        mCamera.setParameters(param);
        mCamera.setDisplayOrientation(result);
        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
           // Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
    
    

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    	mCamera.release();mCamera = null;
    	//mHolder.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    	
    	resetPreview();
    	
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

//        if (mHolder.getSurface() == null){
//          // preview surface does not exist
//          return;
//        }
//
//        // stop preview before making changes
//        try {
//            mCamera.stopPreview();
//        } catch (Exception e){
//          // ignore: tried to stop a non-existent preview
//        }
//
//        // set preview size and make any resize, rotate or
//        // reformatting changes here
//
//        // start preview with new settings
//        try {
//            mCamera.setPreviewDisplay(mHolder);
//            mCamera.startPreview();
//
//        } catch (Exception e){
//           // Log.d(TAG, "Error starting camera preview: " + e.getMessage());
//        }
    }
}
