package com.rainbow.demo.zxing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rainbow.demo.zxing.R;


public class BarcodeInputActivity extends BaseActivity implements View.OnClickListener {

    private EditText captureEditText;
    private TextView goScanBtn;
    private TextView sureSearchBarcodeBtn;
    private ImageView backImageView;
    private ImageView captureHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_barcode_input_layout);
        initView();
        initListener();
    }

    private void initView() {
        backImageView = (ImageView) findViewById(R.id.capture_input_back);
        captureHistory = (ImageView) findViewById(R.id.capture_input_history);
        captureEditText = (EditText) findViewById(R.id.capture_input_barcode_et);
        goScanBtn = (TextView) findViewById(R.id.capture_go_scan_barcode);
        sureSearchBarcodeBtn = (TextView) findViewById(R.id.capture_barcode_input_sure);
    }

    private void initListener() {
        goScanBtn.setOnClickListener(this);
        sureSearchBarcodeBtn.setOnClickListener(this);
        backImageView.setOnClickListener(this);
        captureHistory.setOnClickListener(this);
        captureEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String barcode = captureEditText.getText().toString();
                if (!TextUtils.isEmpty(barcode)) {
                    sureSearchBarcodeBtn.setEnabled(true);
                } else {
                    sureSearchBarcodeBtn.setEnabled(false);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.capture_input_back) {
            this.finish();
        } else if (id == R.id.capture_input_history) {
            Intent intent = new Intent(this, BarcodeHistoryActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.capture_go_scan_barcode) {
            this.finish();
        } else if (id == R.id.capture_barcode_input_sure) {
            String barCode = captureEditText.getText().toString();
            Toast.makeText(this, "您输入的条码是：" + barCode, Toast.LENGTH_LONG).show();
        }
    }
}
