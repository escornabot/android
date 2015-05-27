package com.escornabot.btremote;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {

    private BluetoothDevicesAdapter adapter;
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private ListView devicesList;

    private boolean discovering = false;
    BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devicesList = (ListView) findViewById(R.id.foundDevices);
        View emptyView = findViewById(R.id.empty);
        devicesList.setEmptyView(emptyView);

        adapter = new BluetoothDevicesAdapter(this, R.layout.item_bt_device,
                new ArrayList<BluetoothDevice>());
        devicesList.setAdapter(adapter);

        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = adapter.getItem(position);

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
                BluetoothDevice device = extras.getParcelable(BluetoothDevice.EXTRA_DEVICE);
                adapter.add(device);
                adapter.notifyDataSetChanged();
                Log.d("BTREMOTE", "new device found " + device.getName());
            } else if (action.equalsIgnoreCase(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                discovering = true;
                Log.d("BTREMOTE", "discovery process started ");
            } else if (action.equalsIgnoreCase(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                discovering = false;
                Log.d("BTREMOTE", "discovery process finished");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
