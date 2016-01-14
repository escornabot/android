package com.escornabot.btremote;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private BluetoothDevicesAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (btAdapter != null && btAdapter.isEnabled()) {
                    btAdapter.startDiscovery();
                }
            }
        });

        refreshLayout.setRefreshing(true);

        ListView devicesList = (ListView) findViewById(R.id.foundDevices);
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

        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    BroadcastReceiver btDeviceFoundReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(BluetoothDevice.ACTION_FOUND)) {
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
                refreshLayout.setRefreshing(false);
                Log.d("BTREMOTE", "discovery process finished");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
        adapter.notifyDataSetChanged();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(btDeviceFoundReceiver, filter);

        if (btAdapter != null && btAdapter.isEnabled()) {
            btAdapter.startDiscovery();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(btDeviceFoundReceiver);
    }
}
