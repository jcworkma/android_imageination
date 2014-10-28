package com.jackandabhishek.image_ination;

import java.io.FileNotFoundException;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

public class ImageScaler {
	
	private ImageScaler() {}
	
	private static Point GetImageViewDimensions(ImageView iv) {
		Rect outrect = new Rect();
		iv.getDrawingRect(outrect);
		return new Point(outrect.right, outrect.bottom);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, Point reqSize) {
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
	
	public static Bitmap decodeSampledBitmapFromUri(ContentResolver contentResolver,
			Uri selectedImage, ImageView iv) {
		
		Point reqSize = GetImageViewDimensions(iv);
		
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImage), null,
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
			return BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImage), null,
					options);
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
}
