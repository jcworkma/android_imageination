package com.jackandabhishek.image_ination;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class CameraWrapper {
	
	private final String TAG = "Image-ination CameraWrapper";
	
	private ContentResolver contentResolver;
	private int saveTarget;
	private boolean saveResult;
	// private boolean finishedSaving;
	
	private ImageStorage mStorage;
	public Camera mCamera;
	private Activity holdingActivity;
	
	public CameraWrapper(Activity act) {
		saveResult = true;
		mCamera = GetCameraInstance();
		holdingActivity = act;
		mStorage = new ImageStorage();
	}
	
	public void SetCameraDisplayOrientation() {
		Camera.Parameters params = mCamera.getParameters();
		if (holdingActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			params.setRotation(90);
			
			/*
			 * params.set("orientation", "portrait"); params.set("rotation",90);
			 */
			mCamera.setParameters(params);
		}
		if (holdingActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			params.set("orientation", "landscape");
			params.set("rotation", 90);
		}
	}
	
	private Camera GetCameraInstance() {
		int cameraCount = 0;
		Camera c = null;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();
		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				try {
					c = Camera.open(camIdx);
				}
				catch (RuntimeException e) {
					Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
				}
			}
		}
		return c; // returns null if camera is unavailable
	}
	
	public boolean TakePhoto(ContentResolver cr, int save_to_target) {
		// once this is set back to true, we will let CameraFragment know result of saving op
		// saving op happens on background thread
		// finishedSaving = false;
		
		saveTarget = save_to_target;
		contentResolver = cr;
		mCamera.takePicture(null, null, mPicture);
		return saveResult;
	}
	
	public void reformatCamera(int width, int height) {
		Camera.Parameters params = mCamera.getParameters();
		if (holdingActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			mCamera.setDisplayOrientation(90);
			params.set("orientation", "portrait");
			params.set("rotation", 270);
		}
		if (holdingActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			mCamera.setDisplayOrientation(0);
			params.set("orientation", "landscape");
			params.set("rotation", 0);
		}
		mCamera.setParameters(params);
		return;
	}
	
	public void ShutDown() {
		mCamera.stopPreview();
		mCamera.setPreviewCallback(null);
		mCamera.release();
		mCamera = null;
	}
	
	// Executes when Camera takes a photo
	private PictureCallback mPicture = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// finishedSaving = true;
			
			Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length);
			if (saveTarget == ImageStorage.GALLERY)
				saveResult = mStorage.SaveBitmapToGallery(b, contentResolver);
			else if (saveTarget == ImageStorage.DATABASE)
				saveResult = mStorage.SaveBitmapToDatabase(b);
			else
				saveResult = false;
			
			// attempt to wait for end of saving operation - doesnt work
			// Thread t = new Thread(new PictureSaver(data));
			// t.run();
			//
			// while (!finishedSaving) {
			// try {
			// Thread.sleep(500, 0);
			// }
			// catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// }
			
			camera.startPreview();
			return;
		}
	};
	
	public boolean GetSaveOperationResult() {
		return saveResult;
	}
	
	// from: http://developer.android.com/guide/topics/media/camera.html#manifest
	/** Check if this device has a camera */
	public static boolean CheckCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		}
		else {
			// no camera on this device
			return false;
		}
	}
	
	/* Meant to solve camera saving feedback issue */
	/*
	 * private class PictureSaver implements Runnable {
	 * 
	 * private byte[] picture_data;
	 * 
	 * public PictureSaver(byte[] data) { super(); picture_data = data; }
	 * 
	 * @Override public void run() { finishedSaving = true; return; } }
	 */
	
}
