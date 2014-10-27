package com.jackandabhishek.image_ination;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

public class CameraStorage {
	
	private final String TAG = "Image-ination CameraStorage";
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
	public static final String DCIM = Environment.getExternalStoragePublicDirectory(
			Environment.DIRECTORY_DCIM).toString();
	public static final String DIRECTORY = DCIM + "/Imageination";
	
	public CameraStorage() {
		// TODO Auto-generated constructor stub
	}
	
	private String generateFilepath(String title) {
		return DIRECTORY + '/' + title;
	}
	
	// Create blank image/video file in app's image dir in internal storage and return path
	@SuppressLint("SimpleDateFormat")
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
	
	public boolean SavePhoto(byte[] data) {
		
		WriteFile(CreateMediaFilePathInInternalStorage(MEDIA_TYPE_IMAGE), data);
		// Toast.makeText(getApplicationContext(), "Picture Saved!", Toast.LENGTH_SHORT).show();
		return true;
	}
	
	public boolean WriteFile(File file, byte[] data) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(data);
		}
		catch (Exception e) {
			Log.e(TAG, "Failed to write data", e);
			return false;
		}
		finally {
			try {
				out.close();
			}
			catch (Exception e) {
				return false;
			}
		}
		return true;
	}
	
}
