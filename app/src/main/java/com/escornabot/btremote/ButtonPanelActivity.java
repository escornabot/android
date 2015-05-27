package com.escornabot.btremote;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;


public class ButtonPanelActivity extends ActionBarActivity {

    public static final String EXTRA_DEVICE = "EXTRA_DEVICE";
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream os;

    private ImageButton upButton;
    private ImageButton downButton;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private ImageButton goButton;
    private ImageButton resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_panel);

        device = getIntent().getParcelableExtra(EXTRA_DEVICE);


        try {
            socket = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }

        upButton = (ImageButton) findViewById(R.id.up);
        downButton = (ImageButton) findViewById(R.id.down);
        leftButton = (ImageButton) findViewById(R.id.left);
        rightButton = (ImageButton) findViewById(R.id.right);
        goButton = (ImageButton) findViewById(R.id.go);
        resetButton = (ImageButton) findViewById(R.id.reset);

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    os.write("n\n".getBytes());
                    Log.d("BTRemote", "sent instructions n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    os.write("s\n".getBytes());
                    Log.d("BTRemote", "sent instructions s");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    os.write("w\n".getBytes());
                    Log.d("BTRemote", "sent instructions w");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    os.write("e\n".getBytes());
                    Log.d("BTRemote", "sent instructions e");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    os.write("g\n".getBytes());
                    Log.d("BTRemote", "sent instructions g");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    os.write("r\n".getBytes());
                    Log.d("BTRemote", "sent instructions r");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            socket.connect();
            os = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_button_panel, menu);
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
