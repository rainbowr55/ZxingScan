package com.rainbow.demo.zxing.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class ConvertUtil {

    /**
     * dip 转换为 px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float density = dm.density;
        return (int) (dipValue * density + 0.5f);

    }

}
