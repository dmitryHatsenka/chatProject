package com.example.dmitryhatsenka.chatexample;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends ArrayAdapter<ScanResult> {
    Context mContext;
    int mLayoutResourceId;
    List<ScanResult> mData;
    LayoutInflater mInflater;

    public MyAdapter(Context context, int layoutResourceId, List<ScanResult> data) {
        super(context, layoutResourceId, data);
        this.mContext = context;
        this.mLayoutResourceId = layoutResourceId;
        this.mData = data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ScanResult getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = mInflater.inflate(R.layout.row, parent, false);
        }

        ScanResult point = getItem(position);

        TextView ssidTextView = (TextView) view.findViewById(R.id.ssid);
        ssidTextView.setTextColor(Color.BLACK);
        TextView bssidTextView = (TextView) view.findViewById(R.id.bssid);
        bssidTextView.setTextColor(Color.BLACK);
        TextView levelTextView = (TextView) view.findViewById(R.id.level);
        levelTextView.setTextColor(Color.BLACK);
        TextView capabilitiesTextView = (TextView) view.findViewById(R.id.capabilities);
        capabilitiesTextView.setTextColor(Color.BLACK);

        if (point != null) {
            ssidTextView.setText("Name:" + " " + point.SSID);
            bssidTextView.setText("MAC:" + " " + point.BSSID);
            levelTextView.setText("Level:" + " " +
                    String.valueOf(WifiManager.calculateSignalLevel(point.level, 10)));
            capabilitiesTextView.setText(point.capabilities);
        }

        return view;
    }
}
