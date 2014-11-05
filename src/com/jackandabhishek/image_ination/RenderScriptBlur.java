package com.jackandabhishek.image_ination;

import android.content.res.Resources;
import android.renderscript.RenderScript;

public class RenderScriptBlur {
	
	private RenderScript mRS;
	private ScriptC_RenderScriptBlur mScript;
	
	public RenderScriptBlur(RenderScript rs, Resources res, int resId) {
		mRS = rs;
		mScript = new ScriptC_RenderScriptBlur(rs);
		// mRS.bindRootScript(mScript);
	}
}
