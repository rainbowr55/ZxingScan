package com.rainbow.demo.zxing.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.rainbow.demo.zxing.bean.BarcodeResult;
import com.rainbow.demo.zxing.ui.BarcodeTextActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.android.history.HistoryManager;
import com.google.zxing.client.android.result.ResultHandler;
import com.google.zxing.client.result.ParsedResultType;

/**
 * 扫码的业务处理
 * Created by liangcaihong on 2016/9/28.
 */
public class BarcodeHandler {

    protected Context context;
    protected HistoryManager historyManager;

    public BarcodeHandler(Context context) {
        this.context = context;
        historyManager = new HistoryManager(context);
    }


    public void handleText(String msg, BarcodeResult barcodeResult, boolean isHistory) {
        if (!isHistory) {
            historyManager.addHistoryItem(barcodeResult);
        }
        //跳到文本展示页面
        Intent intent = new Intent();
        intent.putExtra("textDesc", msg);
        intent.setClass(context, BarcodeTextActivity.class);
        context.startActivity(intent);
    }

    public void handleBarcodeResult(BarcodeResult barcodeResult, boolean isHistory) {
        if (barcodeResult == null) {
            return;
        }
        int type = barcodeResult.type;
        String msg = barcodeResult.text;


        if (type == BarcodeUtils.B_URI) {//链接
            handleURI(msg, barcodeResult, isHistory);
        } else if (type == BarcodeUtils.B_GOOD) {//条码
            Toast.makeText(context, "扫描条码内容：" + msg, Toast.LENGTH_LONG);
            //handleProduct(msg, barcodeResult, isHistory);
        } else {//文本
            handleText(msg, barcodeResult, isHistory);
        }

    }

    public void handleBarcodeResult(Result rawResult, ResultHandler resultHandler, boolean isHistory) {
        ParsedResultType resultType = resultHandler.getType();
        String msg = resultHandler.getDisplayContents().toString();
        int type;
        BarcodeFormat barcodeFormat = rawResult.getBarcodeFormat();
        if (barcodeFormat == BarcodeFormat.QR_CODE) {//二维码
            if (resultType == ParsedResultType.URI) {//链接
                type = BarcodeUtils.B_URI;
            } else {//文本
                type = BarcodeUtils.B_TEXT;
            }
        } else {//不是二维码当做条码处理
            type = BarcodeUtils.B_GOOD;
        }
        BarcodeResult barcodeResult = getBarcodeResult(msg, rawResult, resultHandler, type);
        handleBarcodeResult(barcodeResult, isHistory);
    }

    private BarcodeResult getBarcodeResult(String msg, Result rawResult, ResultHandler resultHandler, int type) {
        BarcodeResult barcodeResult = new BarcodeResult();
        barcodeResult.type = type;
        if (rawResult != null && resultHandler != null) {
            barcodeResult.text = rawResult.getText();
            barcodeResult.format = rawResult.getBarcodeFormat().toString();
            barcodeResult.display = resultHandler.getDisplayContents().toString();
            barcodeResult.timestamp = System.currentTimeMillis();
        } else {
            barcodeResult.text = msg;
            barcodeResult.format = BarcodeFormat.EAN_13.toString();
            barcodeResult.display = msg;
        }

        return barcodeResult;
    }


    /**
     * 处理扫码的链接
     *
     * @param msg           链接内容
     * @param barcodeResult
     * @param isHistory     是否是从历史记录点击来的 true 不再保存历史 false 保存扫码历史到本地
     */
    public void handleURI(String msg, BarcodeResult barcodeResult, boolean isHistory) {
        String type = BarcodeUtils.parserType(msg);
        if (!isHistory) {
            historyManager.addHistoryItem(barcodeResult);
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(msg);
        intent.setData(content_url);
        context.startActivity(intent);
    }


}
