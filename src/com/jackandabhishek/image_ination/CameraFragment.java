package com.jackandabhishek.image_ination;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

public class CameraFragment extends Fragment {
	
	private CameraWrapper mCamera;
	private CameraPreview mPreview;
	
	private PopupMenu saveTargetPopupMenu;
	
	private final String SAVING_TO = "Saving To: ";
	private int saveTarget;
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static CameraFragment newInstance() {
		CameraFragment fragment = new CameraFragment();
		return fragment;
	}
	
	public CameraFragment() {
		saveTarget = ImageStorage.GALLERY;
	}
	
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
	
	private void SetSaveTargetButtonText() {
		Button b = (Button) getActivity().findViewById(R.id.camera_target_button);
		if (saveTarget == ImageStorage.GALLERY) {
			b.setText(SAVING_TO + "Gallery");
		}
		else if (saveTarget == ImageStorage.DATABASE) {
			b.setText(SAVING_TO + "Database");
		}
		else {
			b.setText(SAVING_TO + "???");
		}
		
	}
	
	private void InitPopupMenu() {
		// set popup menu button text
		SetSaveTargetButtonText();
		// color menu
		saveTargetPopupMenu =
				new PopupMenu(getActivity().getApplicationContext(), getActivity().findViewById(
						R.id.camera_target_button));
		saveTargetPopupMenu.getMenu().add(Menu.NONE, ImageStorage.GALLERY, Menu.NONE, "Gallery");
		saveTargetPopupMenu.getMenu().add(Menu.NONE, ImageStorage.DATABASE, Menu.NONE, "Database");
		saveTargetPopupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				switch (arg0.getItemId()) {
					case ImageStorage.GALLERY:
						saveTarget = ImageStorage.GALLERY;
						break;
					case ImageStorage.DATABASE:
						saveTarget = ImageStorage.DATABASE;
						break;
					default:
						saveTarget = -1;
						break;
				}
				SetSaveTargetButtonText();
				return true;
			}
		});
	}
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(MainActivity.CAMERA_FRAGMENT_POSITION);
	}
	
	private void InitCameraWrapper() {
		
		mCamera = new CameraWrapper(getActivity());
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
		
		InitPopupMenu();
		
		// Add a listener to the Capture button
		Button saveTargetButton = (Button) getActivity().findViewById(R.id.camera_target_button);
		saveTargetButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				saveTargetPopupMenu.show();
				return;
			}
		});
		Button captureButton = (Button) getActivity().findViewById(R.id.camera_take_photo);
		captureButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCamera.TakePhoto(getActivity().getContentResolver(), saveTarget);
				// if (mCamera.GetSaveOperationResult()) {
				Toast.makeText(getActivity().getApplicationContext(), "Saved!", Toast.LENGTH_SHORT)
						.show();
				// }
				// else {
				// Toast.makeText(getActivity().getApplicationContext(), "error...",
				// Toast.LENGTH_SHORT).show();
				// }
				return;
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
