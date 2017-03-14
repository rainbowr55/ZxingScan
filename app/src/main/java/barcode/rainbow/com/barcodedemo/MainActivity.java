package barcode.rainbow.com.barcodedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView startScanTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startScanTv = (TextView) findViewById(R.id.start_scan_tv);
        startScanTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start_scan_tv) {
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivity(intent);
        }

    }
}
