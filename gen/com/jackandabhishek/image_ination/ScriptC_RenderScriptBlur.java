/*
 * Copyright (C) 2011-2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This file is auto-generated. DO NOT MODIFY!
 * The source Renderscript file: C:\\Users\\Jack Workman\\android\\workspace\\Image-ination\\src\\com\\jackandabhishek\\image_ination\\RenderScriptBlur.rs
 */

package com.jackandabhishek.image_ination;

import android.renderscript.*;
import android.content.res.Resources;

/**
 * @hide
 */
public class ScriptC_RenderScriptBlur extends ScriptC {
    private static final String __rs_resource_name = "renderscriptblur";
    // Constructor
    public  ScriptC_RenderScriptBlur(RenderScript rs) {
        this(rs,
             rs.getApplicationContext().getResources(),
             rs.getApplicationContext().getResources().getIdentifier(
                 __rs_resource_name, "raw",
                 rs.getApplicationContext().getPackageName()));
    }

    public  ScriptC_RenderScriptBlur(RenderScript rs, Resources resources, int id) {
        super(rs, resources, id);
        __F32_4 = Element.F32_4(rs);
    }

    private Element __F32_4;
    private FieldPacker __rs_fp_F32_4;
    private final static int mExportVarIdx_bgColor = 0;
    private Float4 mExportVar_bgColor;
    public synchronized void set_bgColor(Float4 v) {
        mExportVar_bgColor = v;
        FieldPacker fp = new FieldPacker(16);
        fp.addF32(v);
        int []__dimArr = new int[1];
        __dimArr[0] = 4;
        setVar(mExportVarIdx_bgColor, fp, __F32_4, __dimArr);
    }

    public Float4 get_bgColor() {
        return mExportVar_bgColor;
    }

    public Script.FieldID getFieldID_bgColor() {
        return createFieldID(mExportVarIdx_bgColor, null);
    }

}

