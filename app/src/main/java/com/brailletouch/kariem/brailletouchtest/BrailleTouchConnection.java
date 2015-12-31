package com.brailletouch.kariem.brailletouchtest;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Kariem on 12/30/2015.
 */
public class BrailleTouchConnection {

    static boolean mConnected = false;
    private static BluetoothSocket mSocket;
    private static BluetoothDevice mDevice;

    static Runnable mReadingTask = new Runnable() {
        @Override
        public void run() {
            while(mSocket.isConnected())
            {
                String message="";

                while(true)
                {
                    char letter = 0;
                    try {
                        letter = (char)mSocket.getInputStream().read();
                    } catch (IOException e) {
                        Log.v("BrailleTouchTest", "Cannot read message from Ardunio " + e);
                    }
                    message += Character.toString(letter);
                    if (letter == '\n')
                        break;
                }

                Log.v("BrailleTouchTest", message);
            }
        }
    };

    public static void write(int value) throws IOException {


            mSocket.getOutputStream().write(value);

    }


    public static void socketCreated(BluetoothSocket socket)
    {
        mConnected = true;
        mSocket = socket;
        Thread T = new Thread(mReadingTask);
        T.start();
    }


    public static void disconnectSocket() throws IOException {
        if (mSocket.isConnected())
        {
            mSocket.close();
            mConnected = false;
            mSocket = null;
        }
    }
    public static BluetoothDevice getDevice() {
        return mDevice;
    }

    public static void setDevice(BluetoothDevice Device) {
        BrailleTouchConnection.mDevice = Device;
    }
}
