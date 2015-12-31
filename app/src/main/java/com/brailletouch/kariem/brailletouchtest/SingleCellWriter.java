package com.brailletouch.kariem.brailletouchtest;

import java.io.IOException;

/**
 * Created by Kariem on 12/30/2015.
 */
public class SingleCellWriter extends BrailleTouchWriter {

    private int readingCell;

    final int LEFT_CELL=0;
    final int RIGHT_CELL=1;

    public SingleCellWriter(BrailleTouchTranslator brailleTouchTranslatorl) {
        super(brailleTouchTranslatorl);
    }



    @Override
    public void write(char letter) throws IOException {
        if (readingCell == LEFT_CELL)
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
        return readingCell;
    }

    public void setReadingCell(int readingCell) {
        this.readingCell = readingCell;
    }
}
