package com.rainbow.demo.zxing.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rainbow.demo.zxing.R;
import com.rainbow.demo.zxing.adapter.BarcodeHistoryItemAdapter;
import com.rainbow.demo.zxing.bean.BarcodeResult;
import com.rainbow.demo.zxing.util.BarcodeHandler;
import com.google.zxing.client.android.history.HistoryManager;

import java.util.List;

/**
 * 扫码历史记录
 * Created by raibow on 2016/9/28.
 */
public class BarcodeHistoryActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private HistoryManager historyManager;
    private ArrayAdapter<BarcodeResult> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_barcode_history);
        this.historyManager = new HistoryManager(this);
        initView();

    }

    private void initView() {
        initTitle(getString(R.string.scan_history));
        adapter = new BarcodeHistoryItemAdapter(this);
        ListView historyListView = (ListView) findViewById(R.id.capture_barcode_history_list_view);
        historyListView.setAdapter(adapter);

        historyListView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadHistoryItems();
    }


    private void reloadHistoryItems() {
        List<BarcodeResult> items = historyManager.getHistoryItems();
        adapter.clear();
        for (BarcodeResult item : items) {
            adapter.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter != null && adapter.getItem(position) != null) {
            BarcodeResult barcodeResult = adapter.getItem(position);
            BarcodeHandler barcodeHandler = new BarcodeHandler(this);
            barcodeHandler.handleBarcodeResult(barcodeResult, true);
        }
    }
}
