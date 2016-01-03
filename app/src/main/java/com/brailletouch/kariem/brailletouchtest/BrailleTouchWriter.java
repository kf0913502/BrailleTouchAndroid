package com.brailletouch.kariem.brailletouchtest;

import java.io.IOException;

/**
 * Created by Kariem on 12/30/2015.
 */
public abstract class BrailleTouchWriter {

    BrailleTouchTranslator mBrailleTouchTranslator;
    private boolean mShiftPinsDown = false;

    public void setShiftPinsDown(boolean shiftPinsDown)
    {
        mShiftPinsDown = shiftPinsDown;
    }

    public boolean getShiftPinsDown()
    {
        return mShiftPinsDown;
    }
    public BrailleTouchWriter( BrailleTouchTranslator brailleTouchTranslatorl)
    {
        mBrailleTouchTranslator = brailleTouchTranslatorl;
    }

    public abstract void write(char letter) throws IOException;

    public int shiftPinsDown(int value)
    {
        int result=0;
        for (int i=0; i<3; i++)
            if ((BrailleTouchTranslator.mDots[i] & value) > 0)
                result += BrailleTouchTranslator.mDots[i+1];

        for (int i=4; i<7; i++)
            if ((BrailleTouchTranslator.mDots[i] & value) > 0)
                result += BrailleTouchTranslator.mDots[i+1];

        return result;
    }
    public int postProcessing(int value)
    {
        int result = value;
        if (mShiftPinsDown)
            result = shiftPinsDown(result);

        return result;


    }



}
