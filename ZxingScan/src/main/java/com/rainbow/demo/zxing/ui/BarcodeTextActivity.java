package com.rainbow.demo.zxing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.rainbow.demo.zxing.R;

public class BarcodeTextActivity extends BaseActivity {
    private TextView mTextContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_text);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String textDesc = intent.getStringExtra("textDesc");
        mTextContext.setText(textDesc);
    }

    private void initView() {
        initTitle(getString(R.string.scan_text_content));
        mTextContext = (TextView) findViewById(R.id.scan_text_tv);
    }


}
