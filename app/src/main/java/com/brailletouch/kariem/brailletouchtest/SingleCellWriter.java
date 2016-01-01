package com.brailletouch.kariem.brailletouchtest;

import java.io.IOException;

/**
 * Created by Kariem on 12/30/2015.
 */
public class SingleCellWriter extends BrailleTouchWriter {

    private int mReadingCell;

    public static final int LEFT_CELL=0;
    public static final int RIGHT_CELL=1;

    public SingleCellWriter(BrailleTouchTranslator brailleTouchTranslatorl, int readingCell) {
        super(brailleTouchTranslatorl);
        mReadingCell = readingCell;

    }



    @Override
    public void write(char letter) throws IOException {
        if (mReadingCell == LEFT_CELL)
        {

            BrailleTouchConnection.write(0);
            BrailleTouchConnection.write(mBrailleTouchTranslator.translateToBraille(letter));
        }
        else
        {
            BrailleTouchConnection.write(mBrailleTouchTranslator.translateToBraille(letter));
            BrailleTouchConnection.write(0);

        }
    }

    public int getReadingCell() {
        return mReadingCell;
    }

    public void setReadingCell(int readingCell) {
        this.mReadingCell = readingCell;
    }
}
