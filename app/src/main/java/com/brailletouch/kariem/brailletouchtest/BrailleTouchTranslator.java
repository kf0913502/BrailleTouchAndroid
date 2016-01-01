package com.brailletouch.kariem.brailletouchtest;

import java.util.Objects;

/**
 * Created by Kariem on 12/30/2015.
 */
public abstract class BrailleTouchTranslator {
  //  final static int mDots[] = {1,2,4,64, 8, 16,32,128};
    final static int mDots[] = {128,32,16,8,64,4,2,1};
    public abstract int translateToBraille(Object letter);
}
