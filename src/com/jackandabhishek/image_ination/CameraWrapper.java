package com.jackandabhishek.image_ination;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;

public class CameraWrapper {
	
	private final String TAG = "Image-ination CameraWrapper";
	
	private CameraStorage mStorage;
	public Camera mCamera;
	
	public CameraWrapper(int display_orientation) {
		// TODO Auto-generated constructor stub
		mCamera = GetCameraInstance();
		if (mCamera != null) {
			mCamera.setDisplayOrientation(display_orientation);
		}
		mStorage = new CameraStorage();
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
	
	public void TakePhoto() {
		mCamera.takePicture(null, null, mPicture);
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
			
			mStorage.SavePhoto(data);
			camera.startPreview();
		}
	};
	
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
	
}
