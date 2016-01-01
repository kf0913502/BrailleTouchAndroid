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

    int shiftRightToLeft(int value)
    {
        int result=0;
        for (int i=0; i<4; i++)
            result +=((value & BrailleTouchTranslator.mDots[i])
                / BrailleTouchTranslator.mDots[i]) * BrailleTouchTranslator.mDots[i+4];

        return result;


    }


    int shiftLeftToRight(int value)
    {
        int result=0;
        for (int i=0; i<4; i++)
            result +=((value & BrailleTouchTranslator.mDots[i+4])
                    / BrailleTouchTranslator.mDots[i+4]) * BrailleTouchTranslator.mDots[i];

        return result;


    }

    int shiftRightToRight(int value)
    {
        int result=0;
        for (int i=0; i<4; i++)
            result += (value & BrailleTouchTranslator.mDots[i+4]);

        return result;
    }
    int shiftLeftToLeft(int value)
    {
        int result=0;
        for (int i=0; i<4; i++)
            result += (value & BrailleTouchTranslator.mDots[i]);

        return result;
    }

    @Override
    public void write(char letter) throws IOException {
        int letterValue = mBrailleTouchTranslator.translateToBraille(letter);
        mLastRightCellRC = shiftLeftToRight(mLastRightCellLC);
        mLastRightCellLC = shiftRightToLeft(mLastLeftCellRC);
        mLastLeftCellRC = shiftLeftToRight(mLastLeftCellLC);
        mLastLeftCellLC = shiftRightToLeft(letterValue);


        BrailleTouchConnection.write(mLastRightCellLC + mLastRightCellRC);
        BrailleTouchConnection.write(mLastLeftCellLC + mLastLeftCellRC);

        mLastRightCellRC = shiftLeftToRight(mLastRightCellLC);
        mLastRightCellLC = shiftRightToLeft(mLastLeftCellRC);
        mLastLeftCellRC = shiftLeftToRight(mLastLeftCellLC);
        mLastLeftCellLC = shiftRightToRight(letterValue);


        BrailleTouchConnection.write(mLastRightCellLC + mLastRightCellRC);
        BrailleTouchConnection.write(mLastLeftCellLC + mLastLeftCellRC);


        mLastRightCellRC = shiftLeftToRight(mLastRightCellLC);
        mLastRightCellLC = shiftRightToLeft(mLastLeftCellRC);
        mLastLeftCellRC = shiftLeftToRight(mLastLeftCellLC);
        mLastLeftCellLC = 0;


        BrailleTouchConnection.write(mLastRightCellLC + mLastRightCellRC);
        BrailleTouchConnection.write(mLastLeftCellLC + mLastLeftCellRC);
    }
}
