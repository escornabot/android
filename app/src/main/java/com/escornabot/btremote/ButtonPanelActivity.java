package com.escornabot.btremote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;


public class ButtonPanelActivity extends ActionBarActivity {

    public static final String EXTRA_DEVICE = "EXTRA_DEVICE";
    private EscornabotController controller;

    private CommandListAdapter commandAdapter;
    private GridView commandBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_panel);

        Escornabot escornabot = getIntent().getParcelableExtra(EXTRA_DEVICE);
        controller = new EscornabotController(escornabot.getBtInterface());

        ImageButton upButton = (ImageButton) findViewById(R.id.up);
        ImageButton downButton = (ImageButton) findViewById(R.id.down);
        ImageButton leftButton = (ImageButton) findViewById(R.id.left);
        ImageButton rightButton = (ImageButton) findViewById(R.id.right);
        ImageButton goButton = (ImageButton) findViewById(R.id.go);
        ImageButton resetButton = (ImageButton) findViewById(R.id.reset);

        Switch interactiveModeSwitch = (Switch) findViewById(R.id.interactive_mode);
        interactiveModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                controller.setInteractive(isChecked);
                if (isChecked) {
                    commandBuffer.setVisibility(View.INVISIBLE);
                } else {
                    commandBuffer.setVisibility(View.VISIBLE);
                }
            }
        });

        commandBuffer = (GridView) findViewById(R.id.commandBuffer);
        commandAdapter = new CommandListAdapter(this, controller.getCommandQueue());
        commandBuffer.setAdapter(commandAdapter);

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommand(Command.up());
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommand(Command.down());
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommand(Command.left());
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommand(Command.right());
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    controller.go();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    controller.reset();
                    commandAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        TextView connectedToView = (TextView) findViewById(R.id.connectedTo);
        connectedToView.setText(getString(R.string.connected_to, escornabot.getName()));

        commandBuffer.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                System.out.println(event.getAction());

                if (event.getAction() == DragEvent.ACTION_DROP) {
                    View view = (View) event.getLocalState();
                    view.setVisibility(View.VISIBLE);
                } else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED) {
                    if (!event.getResult()) {
                        View view = (View) event.getLocalState();
                        Command command = (Command) view.getTag();
                        controller.removeCommand(command);
                        commandAdapter.notifyDataSetChanged();
                    }
                }

                return true;
            }
        });
    }

    private void addCommand(Command command) {
        controller.addCommand(command);
        commandAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        controller.close();
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
        if (id == R.id.action_disconnect) {
            controller.close();
            Intent returnToListIntent = new Intent(this, MainActivity.class);
            startActivity(returnToListIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
