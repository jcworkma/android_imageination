package com.jackandabhishek.image_ination;

import java.util.Random;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class ImageProcessor {
	
	private Bitmap inputBitmap;
	// private RenderScript mRS;
	//
	// private Allocation mInPixelsAllocation;
	// private Allocation mOutPixelsAllocation;
	
	public static final int NUMBER_OF_EFFECTS = 7;
	public static final int ORIGINAL = 0;
	public static final int CONTRAST = 1;
	public static final int SEPIA_BLUE = 2;
	public static final int SEPIA_GREEN = 3;
	public static final int SEPIA_RED = 4;
	public static final int SNOW_EFFECT = 5;
	public static final int REFLECTION = 6;
	
	// public static final int GREYSCALE = 4;
	// public static final int BLUR = 5;
	
	public ImageProcessor(EditImageActivity edit_image_activity, Bitmap toProcess) {
		// mRS = RenderScript.create(edit_image_activity);
		inputBitmap = toProcess;
		
		// mInPixelsAllocation = Allocation.createFromBitmap(mRS, toProcess);
		// mOutPixelsAllocation = Allocation.createFromBitmap(mRS, toProcess);
	}
	
	public Bitmap Original() {
		return inputBitmap;
	}
	
	/* Many thanks to http://xjaphx.wordpress.com/2011/06/21/image-processing-grayscale-image-on-the-fly/ */
	public Bitmap Greyscale() {
		
		// RenderScriptGreyscale greyscale =
		// new RenderScriptGreyscale(mRS, mInPixelsAllocation, mOutPixelsAllocation);
		// create output bitmap
		Bitmap bmOut =
				Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(),
						inputBitmap.getConfig());
		
		// bmOut = greyscale.Run(bmOut);
		return bmOut;
		
		/**
		 * Done without RenderScript:
		 */
		/*
		 * // constant factors final double GS_RED = 0.299; final double GS_GREEN = 0.587; final double GS_BLUE = 0.114;
		 * 
		 * // pixel information int A, R, G, B; int pixel;
		 * 
		 * // get image size int width = inputBitmap.getWidth(); int height = inputBitmap.getHeight();
		 * 
		 * // scan through every single pixel for (int x = 0; x < width; ++x) { for (int y = 0; y < height; ++y) { //
		 * get one pixel color pixel = inputBitmap.getPixel(x, y); // retrieve color of all channels A =
		 * Color.alpha(pixel); R = Color.red(pixel); G = Color.green(pixel); B = Color.blue(pixel); // take conversion
		 * up to one single value R = G = B = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B); // set new pixel color to
		 * output bitmap bmOut.setPixel(x, y, Color.argb(A, R, G, B)); } }
		 * 
		 * // return final image return bmOut;
		 */
	}
	
	public Bitmap Blur() {
		Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
		// ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(mRS, Element.U8_4(mRS));
		// Allocation tmpIn = Allocation.createFromBitmap(mRS, inputBitmap);
		// Allocation tmpOut = Allocation.createFromBitmap(mRS, outputBitmap);
		// theIntrinsic.setRadius(25.f);
		// theIntrinsic.setInput(tmpIn);
		// theIntrinsic.forEach(tmpOut);
		// tmpOut.copyTo(outputBitmap);
		return outputBitmap;
	}
	
	/* Many thanks to http://xjaphx.wordpress.com/2011/06/21/image-processing-contrast-image-on-the-fly/ */
	public Bitmap Contrast() {
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
	
	/* Many thanks to http://xjaphx.wordpress.com/2011/06/21/image-processing-photography-sepia-toning-effect/ */
	private Bitmap createSepiaToningEffect(Bitmap src, int depth, double red, double green,
			double blue) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// constant grayscale
		final double GS_RED = 0.3;
		final double GS_GREEN = 0.59;
		final double GS_BLUE = 0.11;
		// color information
		int A, R, G, B;
		int pixel;
		
		// scan through all pixels
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				// get color on each channel
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
				// apply grayscale sample
				B = G = R = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);
				
				// apply intensity level for sepid-toning on each channel
				R += (depth * red);
				if (R > 255) {
					R = 255;
				}
				
				G += (depth * green);
				if (G > 255) {
					G = 255;
				}
				
				B += (depth * blue);
				if (B > 255) {
					B = 255;
				}
				
				// set new pixel color to output image
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}
		
		// return final image
		return bmOut;
	}
	
	public Bitmap SepiaBlue() {
		return createSepiaToningEffect(inputBitmap, 150, .15, .15, .7);
	}
	
	public Bitmap SepiaGreen() {
		return createSepiaToningEffect(inputBitmap, 150, .15, .7, .15);
	}
	
	public Bitmap SepiaRed() {
		return createSepiaToningEffect(inputBitmap, 150, .7, .15, .15);
	}
	
	/* Many thanks to http://xjaphx.wordpress.com/2011/10/30/image-processing-snow-effect/ */
	public Bitmap SnowEffect() {
		int COLOR_MAX = 0xFF;
		// get image size
		int width = inputBitmap.getWidth();
		int height = inputBitmap.getHeight();
		int[] pixels = new int[width * height];
		// get pixel array from inputBitmap
		inputBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		// random object
		Random random = new Random();
		
		int R, G, B, index = 0, thresHold = 50;
		// iteration through pixels
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				// get current index in 2D-matrix
				index = y * width + x;
				// get color
				R = Color.red(pixels[index]);
				G = Color.green(pixels[index]);
				B = Color.blue(pixels[index]);
				// generate threshold
				thresHold = random.nextInt(COLOR_MAX);
				if (R > thresHold && G > thresHold && B > thresHold) {
					pixels[index] = Color.rgb(COLOR_MAX, COLOR_MAX, COLOR_MAX);
				}
			}
		}
		// output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
		return bmOut;
	}
	
	/* Many thanks to http://xjaphx.wordpress.com/2011/11/01/image-processing-image-reflection-effect/ */
	public Bitmap Reflection() {
		// gap space between original and reflected
		final int reflectionGap = 4;
		// get image size
		int width = inputBitmap.getWidth();
		int height = inputBitmap.getHeight();
		
		// this will not scale but will flip on the Y axis
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		
		// create a Bitmap with the flip matrix applied to it.
		// we only want the bottom half of the image
		Bitmap reflectionImage =
				Bitmap.createBitmap(inputBitmap, 0, height / 2, width, height / 2, matrix, false);
		
		// create a new bitmap with same width but taller to fit reflection
		Bitmap bitmapWithReflection =
				Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);
		
		// create a new Canvas with the bitmap that's big enough for
		// the image plus gap plus reflection
		Canvas canvas = new Canvas(bitmapWithReflection);
		// draw in the original image
		canvas.drawBitmap(inputBitmap, 0, 0, null);
		// draw in the gap
		Paint defaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
		// draw in the reflection
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		
		// create a shader that is a linear gradient that covers the reflection
		Paint paint = new Paint();
		LinearGradient shader =
				new LinearGradient(0, inputBitmap.getHeight(), 0, bitmapWithReflection.getHeight()
					+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		// set the paint to use this shader (linear gradient)
		paint.setShader(shader);
		// set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
		
		return bitmapWithReflection;
	}
}
