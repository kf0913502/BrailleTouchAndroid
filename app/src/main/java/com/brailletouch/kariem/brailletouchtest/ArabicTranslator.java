package com.brailletouch.kariem.brailletouchtest;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by Kariem on 12/30/2015.
 */
public class ArabicTranslator extends BrailleTouchTranslator{



    static HashMap<Character, Integer> arabicDictionary = new HashMap<Character, Integer>(){{
        put('\u0627', mDots[0]);
         put('\u0628', mDots[0] + mDots[1]);
         put('\u062A', mDots[1] + mDots[2] + mDots[4] + mDots[5]);
          put('\u062B', mDots[0] + mDots[4] + mDots[5] + mDots[6]);
         put('\u062C', mDots[1]  + mDots[4]+ mDots[5]);
         put('\u062D', mDots[0]  + mDots[5]+ mDots[6]);
         put('\u062E', mDots[0] + mDots[2] + mDots[4]+ mDots[6]);
         put('\u062F', mDots[0] + mDots[4] + mDots[5]);
         put('\u0630', mDots[1] + mDots[2] + mDots[4]+ mDots[6]);
         put('\u0631', mDots[0] + mDots[1] + mDots[2]+ mDots[5]);
         put('\u0632', mDots[0] + mDots[2] + mDots[4]+ mDots[5]);
         put('\u0633', mDots[1] + mDots[2]+ mDots[4]);
        put('\u0634', mDots[0] + mDots[4] + mDots[6]);
         put('\u0635', mDots[0] + mDots[1]+ mDots[2] + mDots[4]+ mDots[6]);
         put('\u0636', mDots[0] + mDots[1] + mDots[4] + mDots[6]);
         put('\u0637', mDots[1] + mDots[2] + mDots[4] + mDots[5] + mDots[6]);
         put('\u0638', mDots[0] + mDots[1] + mDots[2] + mDots[4] + mDots[5] + mDots[6]);
         put('\u0639', mDots[0] + mDots[1] + mDots[2] + mDots[5] + mDots[6]);
         put('\u063A', mDots[0] + mDots[1] + mDots[6]);
        put('\u0641', mDots[0] + mDots[1] + mDots[4] );
        put('\u0642', mDots[0] + mDots[1] + mDots[2] + mDots[4] + mDots[5]);
        put('\u0643', mDots[0] + mDots[2]);
        put('\u0644', mDots[0] + mDots[1] + mDots[2]);
        put('\u0645', mDots[0] + mDots[2] + mDots[4]);
         put('\u0646', mDots[0] + mDots[2] + mDots[4] + mDots[5]);
         put('\u0647', mDots[0] + mDots[1] + mDots[5]);
         put('\u0648', mDots[1] + mDots[5] + mDots[4] + mDots[6]);
         put('\u0624', mDots[0] + mDots[1] + mDots[5] + mDots[6]);
         put('\u064A', mDots[1] + mDots[4]);
         put('\u0629', mDots[0] + mDots[6]);
         put('\u0623', mDots[2] + mDots[4]);
        put('\u0623', mDots[2] + mDots[4]);
        put(' ', 0);



    }};
    public int translateToBraille(Object letter)
    {
        Log.v("BrailleTouchTest", "Printing char: " + Character.toString((char)letter) + " with value " + (int)((Character)letter).charValue());
        if (!arabicDictionary.containsKey(letter))
            return 0;
        else
        return arabicDictionary.get(letter);
    }
}
