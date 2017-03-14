package com.rainbow.demo.zxing.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainbow.demo.zxing.R;

public class BaseActivity extends Activity implements View.OnClickListener {

    private ImageView backImageView;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initTitle(String title) {
        backImageView = (ImageView) findViewById(R.id.back_iv);
        titleTextView = (TextView) findViewById(R.id.page_title_tv);
        if (backImageView != null) {
            backImageView.setOnClickListener(this);
        }
        if (titleTextView != null && !TextUtils.isEmpty(title)) {
            titleTextView.setText(title);
        }
    }

    protected void onBack() {
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_iv) {
            onBack();
        }
    }
}
