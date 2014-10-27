package com.jackandabhishek.image_ination;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.jackandabhishek.image_ination.R.id;

public class CameraFragment extends Fragment {
	
	private CameraWrapper mCamera;
	private CameraPreview mPreview;
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static CameraFragment newInstance() {
		CameraFragment fragment = new CameraFragment();
		return fragment;
	}
	
	public CameraFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater infl, ViewGroup container, Bundle savedInstanceState) {
		View rootView = infl.inflate(R.layout.fragment_camera, container, false);
		
		if (!CameraWrapper.CheckCameraHardware(getActivity().getApplicationContext())) {
			// camera does not exist on this device
			Toast.makeText(getActivity().getApplicationContext(), "No Cameras found",
					Toast.LENGTH_LONG).show();
			System.exit(1);
		}
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(MainActivity.CAMERA_FRAGMENT_POSITION);
	}
	
	private void InitCameraWrapper() {
		
		mCamera = new CameraWrapper(90);
		if (mCamera.mCamera == null) {
			// camera is in use
			Toast.makeText(getActivity().getApplicationContext(), "Camera is in use",
					Toast.LENGTH_LONG).show();
			System.exit(1);
		}
	}
	
	private void InitCameraPreview() {
		
		mPreview = new CameraPreview(getActivity().getApplicationContext(), mCamera);
		FrameLayout preview = (FrameLayout) getActivity().findViewById(R.id.camera_preview);
		preview.addView(mPreview);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		InitCameraWrapper();
		InitCameraPreview();
		
		// Add a listener to the Capture button
		Button captureButton = (Button) getActivity().findViewById(id.camera_button);
		captureButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// get an image from the camera
				mCamera.TakePhoto();
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroy();
		
		if (mCamera != null) {
			if (mCamera.mCamera != null) {
				mCamera.ShutDown();
			}
		}
	}
	
}
