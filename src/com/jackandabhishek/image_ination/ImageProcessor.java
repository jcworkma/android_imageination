package com.jackandabhishek.image_ination;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlend;
import android.renderscript.ScriptIntrinsicBlur;

public class ImageProcessor {
	
	private Bitmap inputBitmap;
	private RenderScript mRS;
	private Allocation mInPixelsAllocation;
	private Allocation mInPixelsAllocation2;
	private Allocation mOutPixelsAllocation1;
	private Allocation mOutPixelsAllocation2;
	private EditImageActivity act;
	
	private final int BLUR = 1;
	private final int CONTRAST = 2;
	
	public ImageProcessor(EditImageActivity edit_image_activity, Bitmap toProcess) {
		act = edit_image_activity;
		mRS = RenderScript.create(act);
		
		inputBitmap = toProcess;
		
		mInPixelsAllocation = Allocation.createFromBitmap(mRS, toProcess);
		mInPixelsAllocation2 = Allocation.createFromBitmap(mRS, toProcess);
		mOutPixelsAllocation1 = Allocation.createFromBitmap(mRS, toProcess);
		mOutPixelsAllocation2 = Allocation.createFromBitmap(mRS, toProcess);
	}
	
	private Bitmap Blend() {
		
		ScriptIntrinsicBlend mBlend = ScriptIntrinsicBlend.create(mRS, Element.U8_4(mRS));
		
		Allocation image1;
		Allocation image2;
		
		image1 = Allocation.createTyped(mRS, mInPixelsAllocation.getType());
		image2 = Allocation.createTyped(mRS, mInPixelsAllocation2.getType());
		image1.copy2DRangeFrom(0, 0, mInPixelsAllocation.getType().getX(), mInPixelsAllocation
				.getType().getY(), mInPixelsAllocation, 0, 0);
		image2.copy2DRangeFrom(0, 0, mInPixelsAllocation2.getType().getX(), mInPixelsAllocation2
				.getType().getY(), mInPixelsAllocation2, 0, 0);
		
		mBlend.forEachDst(image1, image2);
		
		mOutPixelsAllocation1.copy2DRangeFrom(0, 0, image2.getType().getX(), image2.getType()
				.getY(), image2, 0, 0);
		
		Bitmap outputBitmap = inputBitmap;
		mOutPixelsAllocation1.copyTo(outputBitmap);
		return outputBitmap;
	}
	
	private Bitmap Blur() {
		Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
		ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(mRS, Element.U8_4(mRS));
		Allocation tmpIn = Allocation.createFromBitmap(mRS, inputBitmap);
		Allocation tmpOut = Allocation.createFromBitmap(mRS, outputBitmap);
		theIntrinsic.setRadius(25.f);
		theIntrinsic.setInput(tmpIn);
		theIntrinsic.forEach(tmpOut);
		tmpOut.copyTo(outputBitmap);
		return outputBitmap;
	}
	
	private Bitmap Contrast() {
		// image size
		int value = 50;
		int width = inputBitmap.getWidth();
		int height = inputBitmap.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, inputBitmap.getConfig());
		// color information
		int A, R, G, B;
		int pixel;
		// get contrast value
		double contrast = Math.pow((100 + value) / 100, 2);
		
		// scan through all pixels
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get pixel color
				pixel = inputBitmap.getPixel(x, y);
				A = Color.alpha(pixel);
				// apply filter contrast for every channel R, G, B
				R = Color.red(pixel);
				R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if (R < 0) {
					R = 0;
				}
				else if (R > 255) {
					R = 255;
				}
				
				G = Color.red(pixel);
				G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if (G < 0) {
					G = 0;
				}
				else if (G > 255) {
					G = 255;
				}
				
				B = Color.red(pixel);
				B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if (B < 0) {
					B = 0;
				}
				else if (B > 255) {
					B = 255;
				}
				
				// set new pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}
		
		// return final image
		return bmOut;
	}
	
	public Bitmap ApplyFilterToBitmap(int filter) {
		switch (filter) {
			case BLUR:
				return Blur();
			case CONTRAST:
				return Contrast();
			default:
				return null;
		}
	}
}
