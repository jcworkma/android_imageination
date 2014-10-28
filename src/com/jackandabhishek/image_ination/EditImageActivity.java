package com.jackandabhishek.image_ination;

import java.io.File;
import java.io.FileInputStream;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.jackandabhishek.image_ination.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e. status bar and navigation/system bar) with
 * user interaction.
 * 
 * @see SystemUiHider
 */
public class EditImageActivity extends Activity {
	
	public static final String IMAGE_KEY = "IMAGE_KEY";
	private Bitmap CurrentImage;
	private ImageProcessor imageProcessor;
	private int FilterIndex = 0;
	
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
		
		ImageView iv = (ImageView) findViewById(R.id.edit_image_view);
		iv.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
			
			@Override
			public void onSwipeLeft() {
				Toast.makeText(getApplicationContext(), "SWIPE LEFT!", Toast.LENGTH_SHORT).show();
				ApplyFilter(-1);
			}
			
			@Override
			public void onSwipeRight() {
				Toast.makeText(getApplicationContext(), "SWIPE RIGHT!", Toast.LENGTH_SHORT).show();
				ApplyFilter(1);
			}
		});
		
		CurrentImage = null;
		String filename = getIntent().getStringExtra(IMAGE_KEY);
		try {
			FileInputStream is = this.openFileInput(filename);
			CurrentImage = BitmapFactory.decodeStream(is);
			is.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		UpdateImage();
		
		imageProcessor = new ImageProcessor(this, CurrentImage);
		
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
	}
	
	private void RecycleImage() {
		CurrentImage.recycle();
	}
	
	private void UpdateImage() {
		ImageView iv = (ImageView) findViewById(R.id.edit_image_view);
		iv.setImageBitmap(CurrentImage);
	}
	
	private void ApplyFilter(int direction) {
		FilterIndex += direction;
		CurrentImage = imageProcessor.ApplyFilterToBitmap(FilterIndex);
		UpdateImage();
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
	
}
