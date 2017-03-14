package com.rainbow.demo.zxing.util;

import android.content.Context;
import android.util.DisplayMetrics;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liangcaihong on 2016/9/27.
 */
public class BarcodeUtils {

    public static final int B_TEXT = 0;//纯文本
    public static final int B_URI = 1;//纯链接
    public static final int B_GOOD = 2;//商品


    /**
     * 通用解析方法
     *
     * @param url
     * @param regex
     * @return
     */
    private static String parserCommon(String url, String regex) {
        String message = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            message = matcher.group(0);
        }
        if (message != null && !message.equals("") && message.length() >= 3) {
            message = message.substring(2, message.length());
        }
        return message;
    }

    /**
     * 解析类型
     *
     * @param url
     * @return
     */
    public static String parserType(String url) {
        return parserCommon(url, "t=\\d+");
    }


    public static int getScanHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (metrics.widthPixels * 0.6);
    }

    public static int getRectMarginTop(Context context) {
        return ConvertUtil.dip2px(context, 170);
    }
}
