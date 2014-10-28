package com.jackandabhishek.image_ination;

import java.io.FileNotFoundException;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoBrowserFragment extends Fragment {
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static PhotoBrowserFragment newInstance() {
		PhotoBrowserFragment fragment = new PhotoBrowserFragment();
		return fragment;
	}
	
	public PhotoBrowserFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater infl, ViewGroup container, Bundle savedInstanceState) {
		View rootView = infl.inflate(R.layout.fragment_browsephotos, container, false);
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(MainActivity.BROWSEPHOTOS_FRAGMENT_POSITION);
		LaunchGallery();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		Button button = (Button) getActivity().findViewById(R.id.browse_gallery_button);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LaunchGallery();
			}
		});
		
		((Button) getActivity().findViewById(R.id.edit_image_button))
				.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						LaunchEditImageActivity(v);
					}
				});
		
	}
	
	public void LaunchGallery() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, 1);
	}
	
	public void LaunchEditImageActivity(View v) {
		Intent intent = new Intent(getActivity(), EditImageActivity.class);
		// EditText editText = (EditText) findViewById(R.id.edit_message);
		// String message = editText.getText().toString();
		// intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
	
	private void ClearImageView() {
		ImageView imgview = (ImageView) getActivity().findViewById(R.id.browse_gallery_imageview);
		imgview.setImageBitmap(null);
	}
	
	private void SetImageView(Bitmap bitmap) {
		ImageView imgview = (ImageView) getActivity().findViewById(R.id.browse_gallery_imageview);
		imgview.setImageBitmap(bitmap);
	}
	
	private Point GetImageViewDimensions() {
		ImageView iv = (ImageView) getActivity().findViewById(R.id.browse_gallery_imageview);
		iv.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		Rect outrect = new Rect();
		iv.getDrawingRect(outrect);
		return new Point(outrect.right, outrect.bottom);
	}
	
	/*
	 * private Point GetDimensions() { Display display = getActivity().getWindowManager().getDefaultDisplay(); Point
	 * size = new Point(); display.getSize(size); // width = size.x // height = size.y return size; }
	 */
	
	public int calculateInSampleSize(BitmapFactory.Options options, Point reqSize) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		
		if (height > reqSize.y || width > reqSize.x) {
			
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			
			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqSize.y
				&& (halfWidth / inSampleSize) > reqSize.x) {
				inSampleSize *= 2;
			}
		}
		
		return inSampleSize;
	}
	
	public Bitmap decodeSampledBitmapFromResource(Uri selectedImage, Point reqSize) {
		
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeStream(
					getActivity().getContentResolver().openInputStream(selectedImage), null,
					options);
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqSize);
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		try {
			return BitmapFactory.decodeStream(
					getActivity().getContentResolver().openInputStream(selectedImage), null,
					options);
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			// clear imageview to free memory
			ClearImageView();
			Uri chosenImageUri = data.getData();
			// Bitmap bitmap = decodeUri(chosenImageUri);
			Bitmap bitmap =
					decodeSampledBitmapFromResource(chosenImageUri, GetImageViewDimensions());
			// Bitmap bitmap = scaleImage(chosenImageUri);
			if (bitmap != null) {
				SetImageView(bitmap);
			}
		}
	}
	
}
