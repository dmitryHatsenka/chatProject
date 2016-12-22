package com.example.dmitryhatsenka.chatexample;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1001;
    private WifiManager mWifiManager;
    private WifiReceiver mWifiReceiver;
    ListView mListView;
    Handler mHand;
    Runnable mScan;
    Button mStartBtn;
    Button mStopBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list_view);
        mStartBtn = (Button) findViewById(R.id.startBtn);
        mStopBtn = (Button) findViewById(R.id.stopBtn);
        mHand = new Handler();

        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mWifiReceiver = new WifiReceiver();

        registerReceiver(mWifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        if (!mWifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "Wifi is disabled..making it enabled",
                    Toast.LENGTH_LONG).show();
            mWifiManager.setWifiEnabled(true);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        }

        mScan = new Runnable() {
            @Override
            public void run() {
                mWifiManager.startScan();
                Toast.makeText(getApplicationContext(), "Scanning",
                        Toast.LENGTH_SHORT).show();
                mHand.postDelayed(mScan, 5000);
            }
        };
    }

    @Override
    protected void onResume() {
        if (mStartBtn.getVisibility() == View.GONE) {
            registerReceiver(mWifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mStartBtn.getVisibility() == View.GONE) {
            unregisterReceiver(mWifiReceiver);
        }

        super.onPause();
    }

    public void startScan(View view) {
        mHand.post(mScan);
        mStartBtn.setVisibility(View.GONE);
        mStopBtn.setVisibility(View.VISIBLE);
    }

    public void stopScan(View view) {
        mHand.removeCallbacks(mScan);
        mStartBtn.setVisibility(View.VISIBLE);
        mStopBtn.setVisibility(View.GONE);
    }

    class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> scanResults = mWifiManager.getScanResults();

                MyAdapter adapter = new MyAdapter(getApplicationContext(), R.layout.row, scanResults);

                mListView.setAdapter(adapter);
            }
        }
    }
}
