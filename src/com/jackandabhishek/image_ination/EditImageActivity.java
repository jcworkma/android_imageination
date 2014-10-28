package com.jackandabhishek.image_ination;

import java.io.File;
import java.net.URI;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import com.jackandabhishek.image_ination.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e. status bar and navigation/system bar) with
 * user interaction.
 * 
 * @see SystemUiHider
 */
public class EditImageActivity extends Activity implements OnClickListener {
	
	public static final String IMAGE_KEY = "IMAGE_KEY";
	private Uri CurrentPhoto;
	
	// much thanks to gav from http://stackoverflow.com/questions/937313/android-basic-gesture-detection
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	
	/**
	 * Whether or not the system UI should be auto-hidden after {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;
	
	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after user interaction before hiding the system
	 * UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	
	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise, will show the system UI visibility upon
	 * interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;
	
	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	
	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_edit_image);
		// setupActionBar();
		
		// final View controlsView = findViewById(R.id.fullscreen_content_controls);
		// final View contentView = findViewById(R.id.edit_image_view);
		
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		// mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
		// mSystemUiHider.setup();
		// mSystemUiHider
		// .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
		//
		// // Cached values.
		// int mControlsHeight;
		// int mShortAnimTime;
		//
		// @Override
		// @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
		// public void onVisibilityChange(boolean visible) {
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
		// // If the ViewPropertyAnimator API is available
		// // (Honeycomb MR2 and later), use it to animate the
		// // in-layout UI controls at the bottom of the
		// // screen.
		// if (mControlsHeight == 0) {
		// mControlsHeight = controlsView.getHeight();
		// }
		// if (mShortAnimTime == 0) {
		// mShortAnimTime =
		// getResources().getInteger(
		// android.R.integer.config_shortAnimTime);
		// }
		// controlsView.animate().translationY(visible ? 0 : mControlsHeight)
		// .setDuration(mShortAnimTime);
		// }
		// else {
		// // If the ViewPropertyAnimator APIs aren't
		// // available, simply show or hide the in-layout UI
		// // controls.
		// controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
		// }
		//
		// if (visible && AUTO_HIDE) {
		// // Schedule a hide().
		// delayedHide(AUTO_HIDE_DELAY_MILLIS);
		// }
		// }
		// });
		
		// Set up the user interaction to manually show or hide the system UI.
		// contentView.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View view) {
		// if (TOGGLE_ON_CLICK) {
		// mSystemUiHider.toggle();
		// }
		// else {
		// mSystemUiHider.show();
		// }
		// }
		// });
		
		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		// findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
		
		// Gesture detection
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};
		
		Intent intent = getIntent();
		String path_to_image = intent.getStringExtra(IMAGE_KEY);
		CurrentPhoto = Uri.parse(path_to_image);
		File f = new File(path_to_image);
		if (f.exists()) {
			Toast.makeText(getApplicationContext(), "EXISTS", Toast.LENGTH_SHORT).show();
		}
	}
	
	// @Override
	// protected void onPostCreate(Bundle savedInstanceState) {
	// super.onPostCreate(savedInstanceState);
	//
	// // Trigger the initial hide() shortly after the activity has been
	// // created, to briefly hint to the user that UI controls
	// // are available.
	// delayedHide(100);
	// }
	
	@Override
	public void onStart() {
		super.onStart();
		// Do this for each view added to the grid
		ImageView imageView = (ImageView) findViewById(R.id.edit_image_view);
		imageView.setOnClickListener(this);
		imageView.setOnTouchListener(gestureListener);
		imageView.setImageBitmap(null);
		imageView.setImageBitmap(ImageScaler.decodeSampledBitmapFromUri(getContentResolver(),
				CurrentPhoto, imageView));
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// TODO: If Settings has multiple levels, Up should navigate up
			// that hierarchy.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the system UI. This is to prevent the jarring
	 * behavior of controls going away while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};
	
	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};
	
	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
	@Override
	public void onClick(View v) {
		Toast.makeText(getApplicationContext(), "TOUCH!", Toast.LENGTH_SHORT).show();
		// Filter f = (Filter) v.getTag();
		// EditImageActivity.show(this, input, f);
		
	}
	
	class MyGestureDetector extends SimpleOnGestureListener {
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					Toast.makeText(EditImageActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
				}
				else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					Toast.makeText(EditImageActivity.this, "Right Swipe", Toast.LENGTH_SHORT)
							.show();
				}
			}
			catch (Exception e) {
				// nothing
			}
			return false;
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
	}
}
