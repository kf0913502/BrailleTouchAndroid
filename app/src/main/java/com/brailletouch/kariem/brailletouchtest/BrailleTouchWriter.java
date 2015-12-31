package com.brailletouch.kariem.brailletouchtest;

import java.io.IOException;

/**
 * Created by Kariem on 12/30/2015.
 */
public abstract class BrailleTouchWriter {

    BrailleTouchTranslator mBrailleTouchTranslator;
    public BrailleTouchWriter( BrailleTouchTranslator brailleTouchTranslatorl)
    {
        mBrailleTouchTranslator = brailleTouchTranslatorl;
    }

    public abstract void write(char letter) throws IOException;



}
