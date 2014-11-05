package com.jackandabhishek.image_ination;

import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	
	private final String TAG = "Image-ination CameraPreview";
	private SurfaceHolder mHolder;
	private CameraWrapper mCamera;
	
	public CameraPreview(Context context, CameraWrapper camera) {
		super(context);
		mCamera = camera;
		
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		// mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the preview.
		try {
			mCamera.mCamera.setPreviewDisplay(holder);
			mCamera.mCamera.startPreview();
		}
		catch (IOException e) {
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.
		
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}
		
		// stop preview before making changes
		try {
			mCamera.mCamera.stopPreview();
		}
		catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}
		
		// set preview size and make any resize, rotate or
		// reformatting changes here
		mCamera.reformatCamera(width, height);
		
		// start preview with new settings
		try {
			mCamera.mCamera.setPreviewDisplay(mHolder);
			mCamera.mCamera.startPreview();
			
		}
		catch (Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}
}
