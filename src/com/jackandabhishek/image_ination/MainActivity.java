package com.jackandabhishek.image_ination;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.jackandabhishek.image_ination.R.id;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	
	private final String TAG = "Image-ination MainActivity";
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final String DCIM = Environment.getExternalStoragePublicDirectory(
			Environment.DIRECTORY_DCIM).toString();
	public static final String DIRECTORY = DCIM + "/Imageination";
	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	
	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	
	private Camera mCamera;
	private CameraPreview mPreview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mNavigationDrawerFragment =
				(NavigationDrawerFragment) getFragmentManager().findFragmentById(
						R.id.navigation_drawer);
		mTitle = getTitle();
		
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		if (!checkCameraHardware(getApplicationContext())) {
			// camera does not exist on this device
			Toast.makeText(getApplicationContext(), "No Cameras found", Toast.LENGTH_LONG).show();
			System.exit(1);
		}
	}
	
	public void onResume() {
		super.onResume();
		
		mCamera = getCameraInstance();
		
		if (mCamera == null) {
			// camera is in use
			Toast.makeText(getApplicationContext(), "Camera is in use", Toast.LENGTH_LONG).show();
			System.exit(1);
		}
		mCamera.setDisplayOrientation(90);
		
		mPreview = new CameraPreview(getApplicationContext(), mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);
		
		// Add a listener to the Capture button
		Button captureButton = (Button) findViewById(id.camera_button);
		captureButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// get an image from the camera
				mCamera.takePicture(null, null, mPicture);
			}
		});
	}
	
	public void onStop() {
		super.onStop();
		
		// mCamera.release();
	}
	
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).commit();
	}
	
	public void onSectionAttached(int number) {
		switch (number) {
			case 1:
				mTitle = getString(R.string.title_section1_takephoto);
				break;
			case 2:
				mTitle = getString(R.string.title_section2_browsegallery);
				break;
			case 3:
				mTitle = getString(R.string.title_section3_otherstuff);
				break;
		}
	}
	
	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// from: http://developer.android.com/guide/topics/media/camera.html#manifest
	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		}
		else {
			// no camera on this device
			return false;
		}
	}
	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
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
					Log.e("Image-ination MainActivity",
							"Camera failed to open: " + e.getLocalizedMessage());
				}
			}
		}
		return c; // returns null if camera is unavailable
	}
	
	// Create blank image/video file in app's image dir in internal storage and return path
	private File CreateMediaFilePathInInternalStorage(int type) {
		
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(generateFilepath("IMG_" + timeStamp + ".jpg"));
		}
		else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(generateFilepath("VID_" + timeStamp + ".mp4"));
		}
		else {
			return null;
		}
		
		// return full path to media file
		return mediaFile;
	}
	
	public static void writeFile(File file, byte[] data) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(data);
		}
		catch (Exception e) {
			Log.e("Image-ination MainActivity", "Failed to write data", e);
		}
		finally {
			try {
				out.close();
			}
			catch (Exception e) {}
		}
	}
	
	private String generateFilepath(String title) {
		return DIRECTORY + '/' + title;
	}
	
	private PictureCallback mPicture = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			
			// // File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
			// File pictureFile = CreateMediaFilePathInInternalStorage(MEDIA_TYPE_IMAGE);
			// if (pictureFile == null) {
			// Log.d(TAG, "Error creating media file, check storage permissions.");
			// return;
			// }
			//
			// try {
			// FileOutputStream fos = new FileOutputStream(pictureFile);
			// fos.write(data);
			// fos.close();
			//
			// // inserts image into gallery
			// Images.Media.insertImage(getContentResolver(), pictureFile.getAbsolutePath(),
			// pictureFile.getName(), "Photo taken by Image-ination");
			//
			// Toast.makeText(getApplicationContext(), "Picture Saved!", Toast.LENGTH_SHORT)
			// .show();
			// }
			// catch (FileNotFoundException e) {
			// Log.d(TAG, "File not found: " + e.getMessage());
			// Toast.makeText(getApplicationContext(), "Error saving picture...",
			// Toast.LENGTH_SHORT).show();
			// }
			// catch (IOException e) {
			// Log.d(TAG, "Error accessing file: " + e.getMessage());
			// Toast.makeText(getApplicationContext(), "Error saving picture...",
			// Toast.LENGTH_SHORT).show();
			// }
			
			writeFile(CreateMediaFilePathInInternalStorage(MEDIA_TYPE_IMAGE), data);
			Toast.makeText(getApplicationContext(), "Picture Saved!", Toast.LENGTH_SHORT).show();
			
			camera.startPreview();
		}
	};
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		
		/**
		 * The fragment argument representing the section number for this fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		
		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}
		
		public PlaceholderFragment() {}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			return rootView;
		}
		
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}
	
}
