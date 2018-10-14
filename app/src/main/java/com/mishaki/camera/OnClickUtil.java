package com.mishaki.camera;

import android.view.View;

/**
 * Administrator on 2017/4/29.
 */

public class OnClickUtil {
    public static void setOnclick(View.OnClickListener implOnclick,View... param){
        for (View v : param){
            v.setOnClickListener(implOnclick);
        }
    }
}
