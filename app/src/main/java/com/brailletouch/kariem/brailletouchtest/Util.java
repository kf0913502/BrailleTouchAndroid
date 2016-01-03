package com.brailletouch.kariem.brailletouchtest;

/**
 * Created by Kariem on 1/3/2016.
 */
public class Util {

    public static int rightPinsToLeft(int value)
    {
        int result=0;
        for (int i=0; i<4; i++)
            result +=((value & BrailleTouchTranslator.mDots[i])
                / BrailleTouchTranslator.mDots[i]) * BrailleTouchTranslator.mDots[i+4];

        return result;


    }

    public static int leftPinsToRight(int value)
    {
        int result=0;
        for (int i=0; i<4; i++)
            result +=((value & BrailleTouchTranslator.mDots[i+4])
                    / BrailleTouchTranslator.mDots[i+4]) * BrailleTouchTranslator.mDots[i];

        return result;


    }

    public static int getRightPins(int value)
    {
        int result=0;
        for (int i=0; i<4; i++)
            result += (value & BrailleTouchTranslator.mDots[i+4]);

        return result;
    }

    public static int getLeftPins(int value)
    {
        int result=0;
        for (int i=0; i<4; i++)
            result += (value & BrailleTouchTranslator.mDots[i]);

        return result;
    }
}
