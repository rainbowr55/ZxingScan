package com.rainbow.demo.zxing.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainbow.demo.zxing.R;
import com.rainbow.demo.zxing.bean.BarcodeResult;
import com.rainbow.demo.zxing.util.BarcodeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BarcodeHistoryItemAdapter extends ArrayAdapter<BarcodeResult> {
    private final Context activity;

    public BarcodeHistoryItemAdapter(Context activity) {
        super(activity, R.layout.scan_record_list_item, new ArrayList<BarcodeResult>());
        this.activity = activity;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(activity, R.layout.scan_record_list_item, null);
            viewHolder.mScanUrlImage = (ImageView) view.findViewById(R.id.scan_record_uri);
            viewHolder.mScanBackground = (RelativeLayout) view.findViewById(R.id.scan_record_back_layout);
            viewHolder.mScanTitle = (TextView) view.findViewById(R.id.scan_record_name_tv);
            viewHolder.mScanContext = (TextView) view.findViewById(R.id.scan_record_bar_code_tv);
            viewHolder.mScanTime = (TextView) view.findViewById(R.id.scan_record_time_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        BarcodeResult barcodeResult = getItem(position);
        setData(viewHolder, barcodeResult);
        return view;
    }

    private void setData(ViewHolder viewHolder, BarcodeResult barcodeResult) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String dateTime = formatter.format(new Date(barcodeResult.timestamp));
        viewHolder.mScanTime.setText(dateTime);
        viewHolder.mScanUrlImage.setVisibility(View.GONE);
        if (barcodeResult.type == BarcodeUtils.B_URI) {
            viewHolder.mScanTitle.setText(R.string.scan_address);
            viewHolder.mScanBackground.setVisibility(View.VISIBLE);
            viewHolder.mScanUrlImage.setVisibility(View.VISIBLE);
            viewHolder.mScanUrlImage.setImageResource(R.drawable.captrure_history_ie);
            viewHolder.mScanContext.setVisibility(View.VISIBLE);
            viewHolder.mScanContext.setText(barcodeResult.text);
        } else {
            viewHolder.mScanTitle.setText(R.string.scan_text_content);
            viewHolder.mScanBackground.setVisibility(View.VISIBLE);
            viewHolder.mScanUrlImage.setVisibility(View.VISIBLE);
            viewHolder.mScanUrlImage.setImageResource(R.drawable.captrure_history_wenben);
            viewHolder.mScanContext.setVisibility(View.VISIBLE);
            viewHolder.mScanContext.setText(barcodeResult.text);
        }

    }


    private class ViewHolder {
        public ImageView mScanUrlImage;
        public RelativeLayout mScanBackground;
        public TextView mScanTitle;
        public TextView mScanContext;
        public TextView mScanTime;

    }
}
