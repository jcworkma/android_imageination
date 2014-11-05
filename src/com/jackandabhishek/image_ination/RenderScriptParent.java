package com.jackandabhishek.image_ination;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;

public class RenderScriptParent {
	
	protected Allocation mInPixelsAllocation;
	protected Allocation mOutPixelsAllocation;
	
	protected RenderScript mRS;
	
	public RenderScriptParent(RenderScript rs, Allocation in, Allocation out) {
		mRS = rs;
		mInPixelsAllocation = in;
		mOutPixelsAllocation = out;
	}
}
