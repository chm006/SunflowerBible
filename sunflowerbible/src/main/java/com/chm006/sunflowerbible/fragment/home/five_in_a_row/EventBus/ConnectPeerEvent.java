package com.chm006.sunflowerbible.fragment.home.five_in_a_row.EventBus;

import android.bluetooth.BluetoothDevice;

import com.peak.salut.SalutDevice;

/**
 * Created by Administrator on 2016/1/25.
 */
public class ConnectPeerEvent {

    public SalutDevice mSalutDevice;
    public BluetoothDevice mBlueToothDevice;

    public ConnectPeerEvent(SalutDevice device, BluetoothDevice bluetoothDevice) {
        mSalutDevice = device;
        mBlueToothDevice = bluetoothDevice;
    }
}
