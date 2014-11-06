package com.jackandabhishek.image_ination;

import java.io.FileInputStream;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
	private ImageStorage imageStorage;
	private ImageProcessor imageProcessor;
	
	private PopupMenu filterPopupMenu;
	private PopupMenu savePopupMenu;
	// private PopupMenu colorPopupMenu;
	
	// drawing path
	private Path drawPath;
	// drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	// initial color
	private int paintColor = 0xFF660000;
	// canvas
	private Canvas drawCanvas;
	// canvas bitmap
	private Bitmap canvasBitmap;
	
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	private SystemUiHider mSystemUiHider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_image);
		
		InitPopupMenus();
		loadImageFromIntent();
		UpdateImage();
		
		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.edit_image_view);
		
		// class used to apply filters
		imageProcessor = new ImageProcessor(this, CurrentImage);
		
		// class used to save images and their changes
		imageStorage = new ImageStorage();
		
		// all the rest = android default full-screen activity stuff
		setupActionBar();
		getActionBar().setTitle("Image-ination Edit");
		
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
					}
				});
		
	}
	
	private void loadImageFromIntent() {
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
	}
	
	private void InitPopupMenus() {
		// // color menu
		// colorPopupMenu =
		// new PopupMenu(getApplicationContext(), findViewById(R.id.edit_image_color));
		// colorPopupMenu.getMenu().add(Menu.NONE, 0, Menu.NONE, "None");
		// colorPopupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "Red");
		// colorPopupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "Green");
		// colorPopupMenu.getMenu().add(Menu.NONE, 3, Menu.NONE, "Blue");
		// colorPopupMenu.setOnMenuItemClickListener(colorMenuClicked);
		// findViewById(R.id.edit_image_color).setOnClickListener(colorButtonClicked);
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
		filterPopupMenu.getMenu().add(Menu.NONE, ImageProcessor.SNOW_EFFECT, Menu.NONE,
				"Snow Effect");
		filterPopupMenu.getMenu()
				.add(Menu.NONE, ImageProcessor.REFLECTION, Menu.NONE, "Reflection");
		filterPopupMenu.setOnMenuItemClickListener(filterMenuClicked);
		findViewById(R.id.edit_image_filter).setOnClickListener(filterButtonClicked);
		// save menu
		savePopupMenu = new PopupMenu(getApplicationContext(), findViewById(R.id.edit_image_save));
		savePopupMenu.getMenu().add(Menu.NONE, 0, Menu.NONE, "To Gallery");
		savePopupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "To Database");
		savePopupMenu.setOnMenuItemClickListener(saveMenuClicked);
		findViewById(R.id.edit_image_save).setOnClickListener(saveButtonClicked);
	}
	
	private void RecycleImage() {
		CurrentImage.recycle();
	}
	
	private void UpdateImage() {
		ImageView iv = (ImageView) findViewById(R.id.edit_image_view);
		iv.setImageBitmap(CurrentImage);
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
				case ImageProcessor.SNOW_EFFECT:
					CurrentImage = imageProcessor.SnowEffect();
					break;
				case ImageProcessor.REFLECTION:
					CurrentImage = imageProcessor.Reflection();
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
				case ImageStorage.GALLERY:
					if (imageStorage.SaveBitmapToGallery(CurrentImage, getContentResolver())) {
						Toast.makeText(getApplicationContext(), "Saved to Gallery!",
								Toast.LENGTH_SHORT).show();
					}
					else {
						Toast.makeText(getApplicationContext(), "Something went wrong...",
								Toast.LENGTH_SHORT).show();
					}
					break;
				case ImageStorage.DATABASE:
					if (imageStorage.SaveBitmapToDatabase(CurrentImage)) {
						Toast.makeText(getApplicationContext(), "Saved to Database!",
								Toast.LENGTH_SHORT).show();
					}
					else {
						Toast.makeText(getApplicationContext(), "Something went wrong...",
								Toast.LENGTH_SHORT).show();
					}
					break;
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
	
	// View.OnClickListener colorButtonClicked = new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// colorPopupMenu.show();
	// return;
	// }
	// };
	
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
	
	/**
	 * Custom view meant for adding drawing functionality to Edit Image Tutorial was followed here:
	 * http://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-interface-creation--mobile-19021 Error
	 * encountered when inflating view
	 * 
	 * @author Jack Workman
	 * 
	 */
	public class DrawingView extends View {
		
		public DrawingView(Context context, AttributeSet attrs) {
			super(context, attrs);
			setupDrawing();
		}
		
		private void setupDrawing() {
			drawPath = new Path();
			drawPaint = new Paint();
			drawPaint.setColor(paintColor);
			drawPaint.setAntiAlias(true);
			drawPaint.setStrokeWidth(20);
			drawPaint.setStyle(Paint.Style.STROKE);
			drawPaint.setStrokeJoin(Paint.Join.ROUND);
			drawPaint.setStrokeCap(Paint.Cap.ROUND);
			canvasPaint = new Paint(Paint.DITHER_FLAG);
		}
		
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			// view given size
			super.onSizeChanged(w, h, oldw, oldh);
			canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			drawCanvas = new Canvas(canvasBitmap);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
			canvas.drawPath(drawPath, drawPaint);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// detect user touch
			float touchX = event.getX();
			float touchY = event.getY();
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					drawPath.moveTo(touchX, touchY);
					break;
				case MotionEvent.ACTION_MOVE:
					drawPath.lineTo(touchX, touchY);
					break;
				case MotionEvent.ACTION_UP:
					drawCanvas.drawPath(drawPath, drawPaint);
					drawPath.reset();
					break;
				default:
					return false;
			}
			invalidate();
			return true;
		}
	}
	
}
