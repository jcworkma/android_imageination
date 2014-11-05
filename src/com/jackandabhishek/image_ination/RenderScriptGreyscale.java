package com.jackandabhishek.image_ination;

import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

public class RenderScriptGreyscale extends RenderScriptParent {
	
	private ScriptC_RenderScriptGreyscale mScript;
	
	public RenderScriptGreyscale(RenderScript rs, Allocation in, Allocation out) {
		super(rs, in, out);
		mScript = new ScriptC_RenderScriptGreyscale(rs);
	}
	
	public Bitmap Run(Bitmap output) {
		mScript.forEach_root(mInPixelsAllocation, mOutPixelsAllocation);
		mOutPixelsAllocation.copyTo(output);
		return output;
	}
	
}