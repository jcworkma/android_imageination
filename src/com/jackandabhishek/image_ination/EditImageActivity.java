package com.jackandabhishek.image_ination;

import java.io.FileInputStream;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
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
	
	PopupWindow filterPopup;
	PopupWindow savePopup;
	PopupWindow colorPopup;
	
	private PopupMenu filterPopupMenu;
	private PopupMenu savePopupMenu;
	private PopupMenu colorPopupMenu;
	
	/**
	 * Whether or not the system UI should be auto-hidden after {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = false;
	
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
		// iv.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
		//
		// @Override
		// public void onSwipeLeft() {
		// Toast.filterText(getApplicationContext(), "SWIPE LEFT!", Toast.LENGTH_SHORT).show();
		// ApplyFilter(-1);
		// }
		//
		// @Override
		// public void onSwipeRight() {
		// Toast.filterText(getApplicationContext(), "SWIPE RIGHT!", Toast.LENGTH_SHORT).show();
		// ApplyFilter(1);
		// }
		// });
		
		// InitPopupWindows();
		InitPopupMenus();
		
		// load image from intent
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
		
		// class used to apply filters
		imageProcessor = new ImageProcessor(this, CurrentImage);
		
		// all the rest = android default full-screen activity stuff
		setupActionBar();
		
		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = iv;
		
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;
					
					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime =
										getResources().getInteger(
												android.R.integer.config_shortAnimTime);
							}
							controlsView.animate().translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						}
						else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
						}
						
						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});
		
		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				}
				else {
					mSystemUiHider.show();
				}
			}
		});
		
		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		// findViewById(R.id.edit_image_filter).setOnTouchListener(mFilterButtonListener);
		// findViewById(R.id.edit_image_save).setOnTouchListener(mSaveButtonListener);
		// findViewById(R.id.edit_image_color).setOnTouchListener(mColorButtonListener);
	}
	
	private void InitPopupMenus() {
		// color menu
		colorPopupMenu =
				new PopupMenu(getApplicationContext(), findViewById(R.id.edit_image_color));
		colorPopupMenu.getMenu().add(Menu.NONE, 0, Menu.NONE, "None");
		colorPopupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "Red");
		colorPopupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "Green");
		colorPopupMenu.getMenu().add(Menu.NONE, 3, Menu.NONE, "Blue");
		colorPopupMenu.setOnMenuItemClickListener(colorMenuClicked);
		findViewById(R.id.edit_image_color).setOnClickListener(colorButtonClicked);
		// filter menu
		filterPopupMenu =
				new PopupMenu(getApplicationContext(), findViewById(R.id.edit_image_filter));
		filterPopupMenu.getMenu().add(Menu.NONE, ImageProcessor.ORIGINAL, Menu.NONE, "Original");
		filterPopupMenu.getMenu().add(Menu.NONE, ImageProcessor.CONTRAST, Menu.NONE, "Contrast");
		filterPopupMenu.getMenu()
				.add(Menu.NONE, ImageProcessor.SEPIA_BLUE, Menu.NONE, "Sepia Blue");
		filterPopupMenu.getMenu().add(Menu.NONE, ImageProcessor.SEPIA_GREEN, Menu.NONE,
				"Sepia Green");
		filterPopupMenu.getMenu().add(Menu.NONE, ImageProcessor.SEPIA_RED, Menu.NONE, "Sepia Red");
		filterPopupMenu.setOnMenuItemClickListener(filterMenuClicked);
		findViewById(R.id.edit_image_filter).setOnClickListener(filterButtonClicked);
		// save menu
		savePopupMenu = new PopupMenu(getApplicationContext(), findViewById(R.id.edit_image_save));
		savePopupMenu.getMenu().add(Menu.NONE, 0, Menu.NONE, "To Gallery");
		savePopupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "To Database");
		savePopupMenu.setOnMenuItemClickListener(saveMenuClicked);
		findViewById(R.id.edit_image_save).setOnClickListener(saveButtonClicked);
	}
	
	PopupMenu.OnMenuItemClickListener filterMenuClicked = new PopupMenu.OnMenuItemClickListener() {
		
		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
			switch (arg0.getItemId()) {
				case ImageProcessor.ORIGINAL:
					CurrentImage = imageProcessor.Original();
					break;
				// case ImageProcessor.BLUR:
				// CurrentImage = imageProcessor.Blur();
				// break;
				// case ImageProcessor.GREYSCALE:
				// CurrentImage = imageProcessor.Greyscale();
				// break;
				case ImageProcessor.CONTRAST:
					CurrentImage = imageProcessor.Contrast();
					break;
				case ImageProcessor.SEPIA_BLUE:
					CurrentImage = imageProcessor.SepiaBlue();
					break;
				case ImageProcessor.SEPIA_GREEN:
					CurrentImage = imageProcessor.SepiaGreen();
					break;
				case ImageProcessor.SEPIA_RED:
					CurrentImage = imageProcessor.SepiaRed();
					break;
				default:
					Toast.makeText(getApplicationContext(), "uh oh...", Toast.LENGTH_SHORT).show();
					break;
			}
			UpdateImage();
			return false;
		}
	};
	
	PopupMenu.OnMenuItemClickListener colorMenuClicked = new PopupMenu.OnMenuItemClickListener() {
		
		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
			switch (arg0.getItemId()) {
				default:
					Toast.makeText(getApplicationContext(), "too bad!", Toast.LENGTH_SHORT).show();
					break;
			}
			UpdateImage();
			return false;
		}
	};
	
	PopupMenu.OnMenuItemClickListener saveMenuClicked = new PopupMenu.OnMenuItemClickListener() {
		
		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
			switch (arg0.getItemId()) {
				default:
					Toast.makeText(getApplicationContext(), "what?", Toast.LENGTH_SHORT).show();
					break;
			}
			UpdateImage();
			return false;
		}
	};
	
	View.OnClickListener filterButtonClicked = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			filterPopupMenu.show();
			return;
		}
	};
	
	View.OnClickListener colorButtonClicked = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			colorPopupMenu.show();
			return;
		}
	};
	
	View.OnClickListener saveButtonClicked = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			savePopupMenu.show();
			return;
		}
	};
	
	View.OnClickListener saveGallery = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// insert code to save to gallery here
			return;
		}
	};
	
	View.OnClickListener saveDatabase = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// insert code to save to database here
			return;
		}
	};
	
	View.OnClickListener colorRed = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			return;
		}
	};
	
	View.OnClickListener colorGreen = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			return;
		}
	};
	
	View.OnClickListener colorBlue = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			return;
		}
	};
	
	View.OnClickListener filterGreyscale = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			CurrentImage = imageProcessor.Greyscale();
			UpdateImage();
			return;
		}
	};
	
	View.OnClickListener filterContrast = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			CurrentImage = imageProcessor.Greyscale();
			UpdateImage();
			return;
		}
	};
	
	View.OnClickListener filterSepiaBlue = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			CurrentImage = imageProcessor.Greyscale();
			UpdateImage();
			return;
		}
	};
	
	View.OnClickListener filterSepiaRed = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			CurrentImage = imageProcessor.Greyscale();
			UpdateImage();
			return;
		}
	};
	
	View.OnClickListener filterSepiaGreen = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			CurrentImage = imageProcessor.Greyscale();
			UpdateImage();
			return;
		}
	};
	
	private void RecycleImage() {
		CurrentImage.recycle();
	}
	
	private void UpdateImage() {
		ImageView iv = (ImageView) findViewById(R.id.edit_image_view);
		iv.setImageBitmap(CurrentImage);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
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
