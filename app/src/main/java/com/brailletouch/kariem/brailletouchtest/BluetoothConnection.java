package com.brailletouch.kariem.brailletouchtest;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.lang.reflect.Method;

/**
 * Created by Kariem on 12/30/2015.
 */
public class BluetoothConnection {
    BluetoothDevice mDevice;
    public BluetoothConnection(BluetoothDevice device)
    {
        mDevice = device;
    }


    public BluetoothSocket createSocket() throws Exception {
        Method method;

        method = mDevice.getClass().getMethod("createRfcommSocket", new Class[] { int.class } );
        return (BluetoothSocket) method.invoke(mDevice, 1);
    }
}
