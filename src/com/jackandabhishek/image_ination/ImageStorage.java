package com.jackandabhishek.image_ination;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore.Images;

public class ImageStorage {
	
	private final String TAG = "Image-ination CameraStorage";
	
	public static final int GALLERY = 0;
	public static final int DATABASE = 1;
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
	public ImageStorage() {}
	
	private String generateTitle(int type) {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		if (type == MEDIA_TYPE_IMAGE)
			return "IMG_" + timeStamp + ".jpg";
		else if (type == MEDIA_TYPE_VIDEO)
			return "VID_" + timeStamp + ".mp4";
		else
			return "UNKNOWN_" + timeStamp + ".txt";
	}
	
	/* from http://stackoverflow.com/questions/649057/how-do-i-save-data-from-camera-to-disk-using-mediastore-on-android */
	public boolean SaveBitmapToGallery(Bitmap b, ContentResolver cr) {
		ContentValues values = new ContentValues();
		values.put(Images.Media.TITLE, generateTitle(MEDIA_TYPE_IMAGE));
		values.put(Images.Media.BUCKET_ID, "what is a bucket_id");
		values.put(Images.Media.DESCRIPTION, "saved from Image_ination");
		values.put(Images.Media.MIME_TYPE, "image/jpeg");
		// set date so that photo is sorted correctly by gallery
		long seconds = System.currentTimeMillis() / 1000;
		values.put(Images.Media.DATE_ADDED, seconds);
		values.put(Images.Media.DATE_TAKEN, seconds);
		values.put(Images.Media.DATE_MODIFIED, seconds);
		Uri uri = cr.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		OutputStream outstream;
		try {
			// attempt to save bitmap as jpeg
			outstream = cr.openOutputStream(uri);
			b.compress(Bitmap.CompressFormat.JPEG, 70, outstream);
			outstream.close();
		}
		catch (FileNotFoundException e) {
			return false;
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public boolean SaveBitmapToDatabase(Bitmap b) {
		return false;
	}
	
}
