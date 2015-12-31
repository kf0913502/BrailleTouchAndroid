package com.brailletouch.kariem.brailletouchtest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class ConnectActivity extends Activity {


    public int getPixelValue(int dp) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        final ExpandableListView devices = (ExpandableListView) findViewById(R.id.devices);
        devices.setDividerHeight(10);
        DisplayMetrics metrics = new DisplayMetrics();
        int width = metrics.widthPixels;
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        devices.setIndicatorBoundsRelative(1050, 1090);



        final ExpandableBluetoothListAdapter devicesAdapter = new ExpandableBluetoothListAdapter(BluetoothAdapter.getDefaultAdapter(), this);
        devices.setAdapter(devicesAdapter);

        Button btnScan = (Button)findViewById(R.id.button_scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devicesAdapter.scanDevices();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connect, menu);
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

class ExpandableBluetoothListAdapter extends BaseExpandableListAdapter
{
    BluetoothAdapter mBTAdapater;
    Set<BluetoothDevice> mBondedDevices;
    Set<BluetoothDevice>mOtherDevices;

    private void connectSocket(BluetoothDevice BTDevice)
    {
        AsyncTask<BluetoothDevice, Void, BluetoothSocket> connectSocket = new AsyncTask<BluetoothDevice, Void, BluetoothSocket>() {
            @Override
            protected BluetoothSocket doInBackground(BluetoothDevice... params) {

                BluetoothConnection BTConnection = new BluetoothConnection(params[0]);
                BluetoothSocket socket = null;
                try {
                    socket = BTConnection.createSocket();

                } catch (Exception e) {
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        Log.e("BrailleTouchTest", "Cannot close socket during creation " + e);
                        return null;
                    }
                    Log.e("BrailleTouchTest", "Socket Creation Failed " + e);
                    return null;
                }

                try {
                    socket.connect();
                } catch (IOException e) {
                    Log.e("BrailleTouchTest", "Socket Connection Failed " + e);
                    try {
                        socket.close();
                        return null;
                    } catch (IOException e1) {
                        Log.e("BrailleTouchTest", "Cannot close socket during connection " + e);
                        return null;
                    }
                }
                BrailleTouchConnection.setDevice(params[0]);
                return socket;
            }

            @Override
            protected void onPostExecute(BluetoothSocket socket)
            {
                BrailleTouchConnection.socketCreated(socket);
                notifyDataSetChanged();
            }

        };

        connectSocket.execute(BTDevice);
    }



    BroadcastReceiver mListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == BluetoothDevice.ACTION_FOUND)
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mOtherDevices.add(device);
                }
            }
        }
    };

    public void scanDevices()
    {
        if (mBTAdapater.isDiscovering()) {
            mBTAdapater.cancelDiscovery();
        }

        if (mBTAdapater.isDiscovering())
            mBTAdapater.cancelDiscovery();
        // Request discover from BluetoothAdapter
        mBTAdapater.startDiscovery();
    }
    public ExpandableBluetoothListAdapter(BluetoothAdapter BTAdapater, Context context)
    {

        mBTAdapater = BTAdapater;
        mBondedDevices = BTAdapater.getBondedDevices();
        mOtherDevices = new HashSet<BluetoothDevice>();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mListener, intentFilter);

    }


    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0)
            return mBondedDevices.size();
        else return mOtherDevices.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        if (groupPosition == 0)
            return "Paired";
        else return "Others";
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        if (groupPosition == 0)
            return mBondedDevices.toArray()[childPosition];
        else return mOtherDevices.toArray()[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        RelativeLayout layout = new RelativeLayout(parent.getContext());
        TextView txt = new TextView(parent.getContext());
        txt.setTextSize(18);
        txt.setId(1);
        txt.setPadding(20, 0, 0, 0);
        RelativeLayout.LayoutParams txtLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        txtLP.addRule(RelativeLayout.CENTER_VERTICAL);
        layout.addView(txt, txtLP);

        convertView = layout;
        convertView.setBackgroundResource(android.R.drawable.dark_header);


        TextView lblListHeader = (TextView) convertView
                .findViewById(1);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setTextColor(Color.WHITE);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final BluetoothDevice BTDevice = (BluetoothDevice)getChild(groupPosition,childPosition);
        final String childText = (BTDevice == null ?  "null" : BTDevice.getName()) + " : " + BTDevice.getAddress();

        RelativeLayout layout = new RelativeLayout(parent.getContext());
        TextView txt = new TextView(parent.getContext());
        txt.setTextSize(18);
        txt.setId(2);
        txt.setPadding(20, 0, 0, 0);
        RelativeLayout.LayoutParams txtLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        txtLP.addRule(RelativeLayout.CENTER_VERTICAL);
        layout.addView(txt, txtLP);

        ImageButton connectDevice = new ImageButton(parent.getContext());
        connectDevice.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (BrailleTouchConnection.mConnected && BTDevice.equals(BrailleTouchConnection.getDevice()))
        {
            connectDevice.setImageResource(R.drawable.disconnect);
            connectDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        BrailleTouchConnection.disconnectSocket();
                        notifyDataSetChanged();
                    } catch (IOException e) {
                        Log.e("BrailleTouchTest", "Cannot close socket " + e);

                    }
                }
            });
        }
        else
        {
            connectDevice.setImageResource(R.drawable.connect);
            connectDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    connectSocket(BTDevice);
                }
            });
        }
        connectDevice.setFocusable(false);
        connectDevice.setFocusableInTouchMode(false);
        connectDevice.setBackgroundColor(Color.TRANSPARENT);




        RelativeLayout.LayoutParams connectDeviceLP = new RelativeLayout.LayoutParams(new LinearLayout.LayoutParams(120,120));
        connectDeviceLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        connectDeviceLP.setMargins(0, 0, 10, 0);

        layout.addView(connectDevice, connectDeviceLP);
        convertView = layout;

        TextView txtListChild = (TextView) convertView
                .findViewById(2);

        txtListChild.setText(childText);

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}