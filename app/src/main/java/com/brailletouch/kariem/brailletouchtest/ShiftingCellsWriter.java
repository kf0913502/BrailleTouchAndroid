package com.brailletouch.kariem.brailletouchtest;

import java.io.IOException;

/**
 * Created by Kariem on 1/1/2016.
 */
public class ShiftingCellsWriter extends BrailleTouchWriter{

    int mLastLeftCellRC=0;
    int mLastLeftCellLC=0;
    int mLastRightCellRC=0;
    int mLastRightCellLC=0;
    public ShiftingCellsWriter(BrailleTouchTranslator brailleTouchTranslatorl) {
        super(brailleTouchTranslatorl);
    }


    @Override
    public void write(char letter) throws IOException {
        int letterValue = mBrailleTouchTranslator.translateToBraille(letter);
        letterValue = postProcessing(letterValue);


        mLastRightCellRC = Util.leftPinsToRight(mLastRightCellLC);
        mLastRightCellLC = Util.rightPinsToLeft(mLastLeftCellRC);
        mLastLeftCellRC = Util.leftPinsToRight(mLastLeftCellLC);
        mLastLeftCellLC = Util.rightPinsToLeft(letterValue);


        BrailleTouchConnection.write(mLastRightCellLC + mLastRightCellRC);
        BrailleTouchConnection.write(mLastLeftCellLC + mLastLeftCellRC);

        mLastRightCellRC = Util.leftPinsToRight(mLastRightCellLC);
        mLastRightCellLC = Util.rightPinsToLeft(mLastLeftCellRC);
        mLastLeftCellRC = Util.leftPinsToRight(mLastLeftCellLC);
        mLastLeftCellLC = Util.getRightPins(letterValue);


        BrailleTouchConnection.write(mLastRightCellLC + mLastRightCellRC);
        BrailleTouchConnection.write(mLastLeftCellLC + mLastLeftCellRC);


        mLastRightCellRC = Util.leftPinsToRight(mLastRightCellLC);
        mLastRightCellLC = Util.rightPinsToLeft(mLastLeftCellRC);
        mLastLeftCellRC = Util.leftPinsToRight(mLastLeftCellLC);
        mLastLeftCellLC = 0;


        BrailleTouchConnection.write(mLastRightCellLC + mLastRightCellRC);
        BrailleTouchConnection.write(mLastLeftCellLC + mLastLeftCellRC);
    }
}
