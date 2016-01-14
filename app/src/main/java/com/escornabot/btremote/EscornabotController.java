package com.escornabot.btremote;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EscornabotController {
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream os;

    private List<Command> commandQueue;
    private boolean interactive = false;

    public EscornabotController(BluetoothDevice device) {
        this.device = device;
        commandQueue = new ArrayList<>();
    }

    public void open() {
        try {
            socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            os = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
        if (interactive) {
            try {
                sendCommand(Command.reset());
            } catch (Exception e) {
                e.printStackTrace();
            }
            commandQueue.clear();
        }
    }

    public void addCommand(Command command) {
        if (interactive) {
            try {
                sendCommand(command);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            commandQueue.add(command);
        }
    }

    public void reset() throws Exception {
        sendCommand(Command.reset());
        commandQueue.clear();
    }

    public void go() throws Exception {
        if (!interactive) {
            sendCommand(Command.reset());
            processQueue();
        }

        sendCommand(Command.go());
    }

    private void processQueue() throws Exception {
        for (Command command : commandQueue) {
            sendCommand(command);
        }
    }

    private void sendCommand(Command command) throws Exception {
        if (os != null) {
            os.write(command.getAtCommand().getBytes());
        }
    }

    public void removeCommand(Command command) {
        commandQueue.remove(command);
    }

    public List<Command> getCommandQueue() {
        return commandQueue;
    }
}
