package com.escornabot.btremote;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BluetoothDevicesAdapter extends ArrayAdapter<BluetoothDevice> {

    private int layoutResourceId;

    public BluetoothDevicesAdapter(Context context, int layoutResourceId,
                                   List<BluetoothDevice> devices) {
        super(context, layoutResourceId, devices);
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResourceId, null);
        BluetoothDevice device = getItem(position);

        StringBuilder description = new StringBuilder();
        if (!device.getName().isEmpty()) {
            description.append(device.getName()).append("\n");
        }

        if (!device.getAddress().isEmpty()) {
            description.append(device.getAddress());
        }

        TextView descriptionView = (TextView) view.findViewById(R.id.description);
        descriptionView.setText(description.toString());

        return view;
    }
}
