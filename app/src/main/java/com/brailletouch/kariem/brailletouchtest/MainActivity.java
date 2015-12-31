package com.brailletouch.kariem.brailletouchtest;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    int ROWS_COUNT = 5;
    int COL_COUNT = 5;

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

        mBrailleTouchWriter = new SingleCellWriter(new EnglishTranslator());
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

        return super.onOptionsItemSelected(item);
    }
}
