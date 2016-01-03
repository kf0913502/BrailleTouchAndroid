package com.brailletouch.kariem.brailletouchtest;

import java.io.IOException;

/**
 * Created by Kariem on 1/1/2016.
 */
public class MirroredWriter extends BrailleTouchWriter {

    public MirroredWriter(BrailleTouchTranslator brailleTouchTranslatorl) {
        super(brailleTouchTranslatorl);
    }

    @Override
    public void write(char letter) throws IOException {
        int value = mBrailleTouchTranslator.translateToBraille(letter);
        value = postProcessing(value);
        BrailleTouchConnection.write(value);
        BrailleTouchConnection.write(value);

    }
}
