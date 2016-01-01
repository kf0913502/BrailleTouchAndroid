package com.brailletouch.kariem.brailletouchtest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;


public class MainActivity extends ActionBarActivity {

    int ROWS_COUNT = 5;
    int COL_COUNT = 5;
    boolean mRefreshCells=true;
    boolean mShiftPins = true;
    static HashMap<String, BrailleTouchWriter> mReadingModesDictionary = new HashMap<String, BrailleTouchWriter>(){{
        put("Single Cell (Left)", new SingleCellWriter(new EnglishTranslator(), SingleCellWriter.LEFT_CELL));
        put("Single Cell (Right)", new SingleCellWriter(new EnglishTranslator(), SingleCellWriter.RIGHT_CELL));
        put ("Mirror Cells", new MirroredWriter(new EnglishTranslator()));
        put ("Alternate Cells", new AlternateCellWriter(new EnglishTranslator()));
        put ("Shifting Cells", new ShiftingCellsWriter(new EnglishTranslator()));

    }};


    String mReadingModeChoice = "Single Cell (Left)";
    char currentLetter=0;
    BrailleTouchWriter mBrailleTouchWriter;
    ArrayList<View> mTextViews = new ArrayList<View>();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    private boolean inRegion(float x, float y, View v) {
        int coordBuffer[] = new int[2];
        v.getLocationOnScreen(coordBuffer);
        return coordBuffer[0] + v.getWidth() > x &&    // right edge
                coordBuffer[1] + v.getHeight() > y &&   // bottom edge
                coordBuffer[0] < x &&                   // left edge
                coordBuffer[1] < y;                     // top edge
    }
    int insideView(ArrayList<View> views, float x, float y)
    {
        for (int i=0; i<views.size(); i++)
        {
            if (inRegion(x,y,views.get(i)))
                return i;
        }

        return -1;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBrailleTouchWriter = mReadingModesDictionary.get("Single Cell (Left)");
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.FILL_PARENT, 1.0f);

        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setLayoutParams(rowParams);
        tableLayout.setStretchAllColumns(true);
        tableLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (BrailleTouchConnection.mConnected) {
                    int viewIndex = insideView(mTextViews,event.getRawX(),event.getRawY());
                    if (viewIndex != -1)
                    try {
                        char letter = ((TextView) mTextViews.get(viewIndex)).getText().charAt(0);
                        if (event.getAction() == MotionEvent.ACTION_UP && mRefreshCells)
                        {
                            mBrailleTouchWriter.write(' ');
                            currentLetter = ' ';
                            return true;
                        }
                        if (letter != currentLetter)
                       {
                            currentLetter = letter;
                            mBrailleTouchWriter.write( letter );
                        }
                    } catch (IOException e) {
                        Log.e("BrailleTouchTest", "Could not write to cells " + e);
                    }
                }
                return true;
            }
        });
        for (int i=0; i< ROWS_COUNT; i++)
        {
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER);
            tableRow.setLayoutParams(tableParams);
            tableRow.setFocusable(true);
            tableRow.setFocusableInTouchMode(true);
            for (int j=0; j<COL_COUNT; j++)
            {
                TextView textView = new TextView(this);
                textView.setTextSize(25);
                textView.setGravity(Gravity.CENTER);
                final char letter = (char)('A' + i*COL_COUNT + j);
                textView.setText(Character.toString(letter));
                textView.setLayoutParams(rowParams);
                tableRow.addView(textView);
                mTextViews.add(textView);

            }
            tableLayout.addView(tableRow);
        }

        setContentView(tableLayout);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.connect) {
            startActivity(new Intent(this, ConnectActivity.class));
        }
        else if (id == R.id.readingMode)
        {

            final Dialog b = new Dialog(this);
            b.setContentView(R.layout.dialog_readingmodes);

            ListView LV = (ListView) b.findViewById(R.id.readingModeLV);
            ArrayList<String> data = new ArrayList<String>();
            data.addAll(mReadingModesDictionary.keySet());
            final ReadingModesAdapter adapter = new ReadingModesAdapter(this, data, mReadingModeChoice, LV);
            LV.setAdapter(adapter);
            b.setTitle("Reading Modes");

            Button readingModesSubmit = (Button) b.findViewById(R.id.readingModeSubmit);
            readingModesSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mReadingModeChoice = adapter.getChoice();
                    mBrailleTouchWriter = mReadingModesDictionary.get(mReadingModeChoice);
                    b.dismiss();
                }
            });
            b.show();


        }
        else if (id == R.id.Settings)
        {
            final Dialog b = new Dialog(this);
            b.setContentView(R.layout.dialog_settings);

            final CheckBox shiftCheck = (CheckBox)b.findViewById(R.id.Shiftheck);
            final CheckBox refreshCheck = (CheckBox)b.findViewById(R.id.RefreshCheck);

            shiftCheck.setChecked(mShiftPins);
            refreshCheck.setChecked(mRefreshCells);

            Button settingsSubmit = (Button)b.findViewById(R.id.SettingsSubmit);
            b.setTitle("Settings");
            b.show();

            settingsSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRefreshCells = refreshCheck.isChecked();
                    mShiftPins = shiftCheck.isChecked();
                    b.dismiss();
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }


    private class ReadingModesAdapter extends ArrayAdapter<String>
    {


        ArrayList<String> mData;
        String mChoice;

        public String getChoice()
        {
            return mChoice;
        }


        ListView mLV;
        public ReadingModesAdapter(Context context, ArrayList<String> data, String choice, ListView LV)
        {
            super(context,0,data);
            mData = data;
            mChoice = choice;
            mLV = LV;
        }

        public ReadingModesAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.spinner_readingmodes, parent, false);
            final TextView label=(TextView)row.findViewById(R.id.ReadingModeTxt);
            label.setText(mData.get(position));

            RadioButton radioButton = (RadioButton)row.findViewById(R.id.ReadingModeRadio);
            if (mChoice.equals(label.getText()))
                radioButton.setChecked(true);
            else radioButton.setChecked(false);




            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mChoice = label.getText().toString();


                    android.os.Handler h = new android.os.Handler(Looper.getMainLooper());
                            h.post(new Runnable() {
                        @Override
                        public void run() {

                            notifyDataSetInvalidated();
                            notifyDataSetChanged();
                            mLV.invalidateViews();
                            mLV.refreshDrawableState();
                        }
                    });

                }
            });
            return row;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}
