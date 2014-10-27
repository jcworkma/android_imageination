package com.jackandabhishek.image_ination;

import java.io.FileNotFoundException;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

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
	public View
			onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_browsephotos, container, false);
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(MainActivity.BROWSEPHOTOS_FRAGMENT_POSITION);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		Button button = (Button) getActivity().findViewById(R.id.browse_gallery_button);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, 1);
			}
		});
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			Uri chosenImageUri = data.getData();
			
			// Bitmap mBitmap = null;
			// try {
			// mBitmap = Media.getBitmap(getActivity().getContentResolver(), chosenImageUri);
			// }
			// catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			ImageView imgview =
					(ImageView) getActivity().findViewById(R.id.browse_gallery_imageview);
			// imgview.setImageBitmap(mBitmap);
			// imgview.setImageDrawable(null);
			// imgview.setImageURI(null);
			// imgview.setImageURI(chosenImageUri);
			//
			// imgview.setImageBitmap(decodeSampledBitmapFromResource(chosenImageUri.getPath(), 100,
			// 100));
			try {
				imgview.setImageBitmap(decodeUri(chosenImageUri));
			}
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	// thank you to siamii on
	// http://stackoverflow.com/questions/2507898/how-to-pick-an-image-from-gallery-sd-card-for-my-app/2507973#2507973
	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
		
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(getActivity().getContentResolver()
				.openInputStream(selectedImage), null, o);
		
		// The new size we want to scale to
		final int REQUIRED_SIZE = 140;
		
		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		
		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(
				getActivity().getContentResolver().openInputStream(selectedImage), null, o2);
		
	}
}
