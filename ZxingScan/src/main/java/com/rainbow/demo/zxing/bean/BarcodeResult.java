package com.rainbow.demo.zxing.bean;

/**
 * 扫码结果数据
 * Created by rainbow on 2016/9/28.
 */
public class BarcodeResult {
    public int type;//  B_TEXT = 0;//纯文本 B_URI = 1;//纯链接  B_GOOD = 2;//其他
    public String text;
    public String format;
    public String display;
    public long timestamp;

    public BarcodeResult() {
    }

    public BarcodeResult(String text, String format, String display, long timestamp, int type) {
        this.text = text;
        this.format = format;
        this.display = display;
        this.timestamp = timestamp;
        this.type = type;

    }
}
