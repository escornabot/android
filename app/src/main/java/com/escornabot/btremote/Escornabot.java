package com.escornabot.btremote;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

public class Escornabot implements Parcelable {
    private String name;
    private BluetoothDevice btInterface;

    public Escornabot() {
    }

    public Escornabot(BluetoothDevice btDevice) {
        btInterface = btDevice;

        StringBuilder description = new StringBuilder();
        if (btDevice.getName() != null && !btDevice.getName().isEmpty()) {
            description.append(btDevice.getName()).append("\n");
        }

        if (btDevice.getAddress() != null && !btDevice.getAddress().isEmpty()) {
            description.append(btDevice.getAddress());
        }
        
        name = description.toString();
    }

    protected Escornabot(Parcel in) {
        name = in.readString();
        btInterface = in.readParcelable(BluetoothDevice.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(btInterface, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Escornabot> CREATOR = new Creator<Escornabot>() {
        @Override
        public Escornabot createFromParcel(Parcel in) {
            return new Escornabot(in);
        }

        @Override
        public Escornabot[] newArray(int size) {
            return new Escornabot[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BluetoothDevice getBtInterface() {
        return btInterface;
    }

    public void setBtInterface(BluetoothDevice btInterface) {
        this.btInterface = btInterface;
    }
}
