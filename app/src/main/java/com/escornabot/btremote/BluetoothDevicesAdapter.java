package com.escornabot.btremote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BluetoothDevicesAdapter extends ArrayAdapter<Escornabot> {

    private int layoutResourceId;

    public BluetoothDevicesAdapter(Context context, int layoutResourceId,
                                   List<Escornabot> devices) {
        super(context, layoutResourceId, devices);
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResourceId, null);
        Escornabot device = getItem(position);

        TextView descriptionView = (TextView) view.findViewById(R.id.description);
        descriptionView.setText(device.getName());

        return view;
    }
}
