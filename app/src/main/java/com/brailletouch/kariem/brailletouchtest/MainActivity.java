package com.brailletouch.kariem.brailletouchtest;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    static TopicsJsonResponse mTopics;
    static int mRows = 5;
    static int mCols = 5;
    static int nTopics=0;
    static int currenTopic=0;
    static int mCurrentWordIndex=0;
    static String mText = "hello this is braille touch";
    static boolean fitWordToScreen=true;
    static boolean tweetsLoaded = false;
    static boolean mRefreshCells=false;
    static boolean mShiftPins = false;
    static BrailleTouchTranslator brailleTouchTranslator = new ArabicTranslator();
    static HashMap<String, BrailleTouchWriter> mReadingModesDictionary = new HashMap<String, BrailleTouchWriter>(){{
        put("Single Cell (Left)", new SingleCellWriter(brailleTouchTranslator, SingleCellWriter.LEFT_CELL));
        put("Single Cell (Right)", new SingleCellWriter(brailleTouchTranslator, SingleCellWriter.RIGHT_CELL));
        put ("Mirror Cells", new MirroredWriter(brailleTouchTranslator));
        put ("Alternate Cells", new AlternateCellWriter(brailleTouchTranslator));
        put ("Shifting Cells", new ShiftingCellsWriter(brailleTouchTranslator));
        put("Single Cell (Center)", new SingleCellWriter(brailleTouchTranslator, SingleCellWriter.CENTER_CELL));


    }};

    AsyncHTTPTask<Map<String, String>,TopicsJsonResponse> getTweets;
    static String mReadingModeChoice = "Single Cell (Left)";
    static char currentLetter=0;
    static BrailleTouchWriter mBrailleTouchWriter;


    ArrayList<View> mTextViews = new ArrayList<View>();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    private void refreshText() {




        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mCols; j++) {
                if (mTextViews.get(mRows * i + j) == null)
                    break;
                TextView textView = (TextView) mTextViews.get(mRows * i + j);
                char letter = ' ';


                if (fitWordToScreen)
                {
                    String [] words = mText.split(" ");
                    if (words[mCurrentWordIndex].length() > j)
                        letter = words[mCurrentWordIndex].charAt(j);
                    else if (mCurrentWordIndex != words.length - 1)
                        letter = '~';
                    else if (currenTopic != mTopics.getTopics().size() -1)
                        letter = '^';


                }
                else
                if (i * mCols + j < mText.length())
                    letter = mText.charAt(i * mCols + j);


                textView.setText(Character.toString(letter));
            }
        }
    }



    private int inRegion(float x, float y, View v) {
        int coordBuffer[] = new int[2];
        v.getLocationOnScreen(coordBuffer);
        if (coordBuffer[0] + v.getWidth() > x &&    // right edge
                coordBuffer[1] + v.getHeight()/2 > y &&   // bottom edge
                coordBuffer[0] < x &&                   // left edge
                coordBuffer[1] < y)                     // top edge
        return 0;
        else         if (coordBuffer[0] + v.getWidth() > x &&    // right edge
                coordBuffer[1] + v.getHeight() > y &&   // bottom edge
                coordBuffer[0] < x &&                   // left edge
                coordBuffer[1] < y)                     // top edge
            return 1;
        else return -1;

    }
    Pair<Integer, Integer> insideView(ArrayList<View> views, float x, float y)
    {
        for (int i=0; i<views.size(); i++)
        {
            int inside =0;
            inside = inRegion(x,y,views.get(i));
            if (inside != -1)
                return new Pair<Integer, Integer>(i, inside);
        }

        return null;
    }



    private void createLayout()
    {
        if (fitWordToScreen)
        {
            mRows = 1;
            if (mText.split(" ").length > 0)
                mCols = mText.split(" ")[mCurrentWordIndex].length() + 1;
            else mCols = 5;
        }


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
                    Pair<Integer, Integer> viewIndex = insideView(mTextViews,event.getRawX(),event.getRawY());
                    if (viewIndex != null)
                        try {
                            char letter = ((TextView) mTextViews.get(viewIndex.first)).getText().charAt(0);
                            if (event.getAction() == MotionEvent.ACTION_UP && mRefreshCells)
                            {
                                mBrailleTouchWriter.write(' ');
                                currentLetter = ' ';
                                return true;
                            }
                            if (letter != currentLetter ||
                                    (mShiftPins && mBrailleTouchWriter.getShiftPinsDown() != (viewIndex.second == 1)))
                            {
                                currentLetter = letter;
                                if (mShiftPins)
                                    mBrailleTouchWriter.setShiftPinsDown(viewIndex.second == 1);
                                else mBrailleTouchWriter.setShiftPinsDown(false);
                                if (letter == '~')
                                {
                                    mCurrentWordIndex++;
                                    recreate();
                                }
                                else if (letter == '^')
                                {
                                    currenTopic++;
                                    mCurrentWordIndex = 0;
                                    recreate();
                                }
                                else
                                mBrailleTouchWriter.write( letter );
                            }
                        } catch (IOException e) {
                            Log.e("BrailleTouchTest", "Could not write to cells " + e);
                        }
                }
                return true;
            }
        });
        for (int i=0; i< mRows; i++)
        {
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER);
            tableRow.setLayoutParams(tableParams);
            tableRow.setFocusable(true);
            tableRow.setFocusableInTouchMode(true);
            for (int j=0; j< mCols; j++)
            {
                TextView textView = new TextView(this);
                textView.setTextSize(25);
                textView.setGravity(Gravity.CENTER);
                textView.setLayoutParams(rowParams);
                tableRow.addView(textView);
                mTextViews.add(textView);

            }
            tableLayout.addView(tableRow);
        }


        setContentView(tableLayout);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (tweetsLoaded)
        {
            mText = mTopics.getTopics().get(currenTopic).getPinnedTweet().getTweet().getText();
        }
        if (!tweetsLoaded) {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            getTweets = new AsyncHTTPTask<Map<String, String>, TopicsJsonResponse>();
            getTweets.setHttpParams("http://www.tweetmogaz.com/solr/events/all", "GET",
                    new Callback<TopicsJsonResponse>() {
                        @Override
                        public void notify(TopicsJsonResponse param) {

                            if (param == null) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Could not load tweets", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                return;
                            }
                            tweetsLoaded = true;
                            mTopics = param;

                            mText = param.getTopics().get(0).getPinnedTweet().getTweet().getText();
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {

                                    recreate();
                                }
                            });

                            for (Topic t : param.getTopics())
                                Log.v("BrailleTouchTest", t.getPinnedTweet().getTweet().getText());
                        }
                    }, TopicsJsonResponse.class);

            HashMap<String, String> params = new HashMap<>();
            params.put("class", "egypt");
            params.put("search_peroid", "48");

            getTweets.execute(params);
        }

        mBrailleTouchWriter = mReadingModesDictionary.get(mReadingModeChoice);
        createLayout();
        refreshText();
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
        else if (id == R.id.ReloadTweets)
        {
            tweetsLoaded = false;
            currenTopic = 0;
            mCurrentWordIndex = 0;
            recreate();
        }
        else if (id == R.id.EnterText)
        {
            final Dialog b = new Dialog(this);
            b.setContentView(R.layout.dialog_entertext);
            final EditText txt = (EditText)b.findViewById(R.id.EnterTextEdit);
            txt.setText(mText);
            b.setTitle("Enter Text");
            b.show();
            Button btn = (Button)b.findViewById(R.id.EnterTextSubmit);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
 //                       mText = txt.getText().toString();
                        mTopics = new TopicsJsonResponse();
                        mTopics.setTopics(new ArrayList<Topic>());
                        mTopics.topics.add(new Topic());
                        mTopics.topics.get(0).setPinnedTweet(new PinnedTweet());
                        mTopics.topics.get(0).getPinnedTweet().setTweet(new Tweet());
                        mTopics.topics.get(0).getPinnedTweet().getTweet().setText(txt.getText().toString());
                        recreate();

                    }
                    catch (Exception e)
                    {
                        System.out.println("Can't refresh text : " + e);
                    }

                    b.dismiss();
                }
            });

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

            ArrayList<String> cols = new ArrayList<String>();
            ArrayList<String> rows = new ArrayList<String>();

            for (int i=1; i<10; i++)
            {
                cols.add(Integer.toString(i));
                rows.add(Integer.toString(i));
            }

            final Spinner colSpinner = (Spinner)b.findViewById(R.id.ColumnSpinner);
            ArrayAdapter<String> colAdapter  = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,cols);
            colAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            colSpinner.setAdapter(colAdapter);

            final Spinner rowSpinner = (Spinner)b.findViewById(R.id.RowsSpinner);
            ArrayAdapter<String> rowAdapter  = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,rows);
            rowAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            rowSpinner.setAdapter(rowAdapter);

            ArrayList<String> languages = new ArrayList<>();
            languages.add("English");
            languages.add("Arabic");
            final Spinner languageSpinner = (Spinner)b.findViewById(R.id.LanguageSpinner);
            ArrayAdapter<String> languageAdapter  = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,languages);
            languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            languageSpinner.setAdapter(languageAdapter);

            languageSpinner.setSelection(mBrailleTouchWriter.getmBrailleTouchTranslator().getClass() == EnglishTranslator.class ? 0 : 1);
            colSpinner.setSelection(mCols - 1);
            rowSpinner.setSelection(mRows-1);
            b.setTitle("Settings");
            b.show();

            settingsSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRefreshCells = refreshCheck.isChecked();
                    mShiftPins = shiftCheck.isChecked();
                    mCols = colSpinner.getSelectedItemPosition()+1;
                    mRows = rowSpinner.getSelectedItemPosition()+1;
                    mBrailleTouchWriter.setmBrailleTouchTranslator(languageSpinner.getSelectedItemPosition() == 0 ? new EnglishTranslator() : new ArabicTranslator());
                    recreate();
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
