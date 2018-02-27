package com.escornabot.btremote;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;


    private BluetoothDevicesAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private View searchingView;

    BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        searchingView = findViewById(R.id.searching);

        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lookForDevices();
            }
        });

        refreshLayout.setRefreshing(true);

        ListView devicesList = findViewById(R.id.foundDevices);
        View emptyView = findViewById(R.id.empty);
        devicesList.setEmptyView(emptyView);

        adapter = new BluetoothDevicesAdapter(this, R.layout.item_bt_device,
                new ArrayList<Escornabot>());

        // Escornabot testBot = new Escornabot();
        // testBot.setName("Test bot");
        // adapter.add(testBot);

        devicesList.setAdapter(adapter);

        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Escornabot device = adapter.getItem(position);

                try {
                    Intent startControlPanel = new Intent(getBaseContext(), ButtonPanelActivity.class);
                    startControlPanel.putExtra(ButtonPanelActivity.EXTRA_DEVICE, device);

                    startActivity(startControlPanel);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Cannot connect to escornabot", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    BroadcastReceiver btDeviceFoundReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(BluetoothDevice.ACTION_FOUND)) {
                searchingView.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
                Bundle extras = intent.getExtras();

                Escornabot escornabot = new Escornabot(
                        (BluetoothDevice) extras.getParcelable(BluetoothDevice.EXTRA_DEVICE));
                if (escornabot.getName().isEmpty()) {
                    escornabot.setName(getString(R.string.unknow_device));
                }

                adapter.add(escornabot);
                adapter.notifyDataSetChanged();

                Log.d("BTREMOTE", "new device found " + escornabot.getName());
            } else if (action.equalsIgnoreCase(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                Log.d("BTREMOTE", "discovery process started ");
            } else if (action.equalsIgnoreCase(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                searchingView.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);

                refreshLayout.setRefreshing(false);
                Log.d("BTREMOTE", "discovery process finished");
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    btAdapter.startDiscovery();
                } else {
                    Toast.makeText(this, R.string.not_permission, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
        adapter.notifyDataSetChanged();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(btDeviceFoundReceiver, filter);

        lookForDevices();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(btDeviceFoundReceiver);
    }

    private void lookForDevices() {
        if (btAdapter != null && btAdapter.isEnabled()) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            } else {
                btAdapter.startDiscovery();
            }
        }
    }
}
