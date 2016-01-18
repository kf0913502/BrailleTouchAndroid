package com.brailletouch.kariem.brailletouchtest;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by Kariem on 12/30/2015.
 */
public class EnglishTranslator extends BrailleTouchTranslator{



    static HashMap<Character, Integer> englishDictionary = new HashMap<Character, Integer>(){{
        put('a', mDots[0]);
        put('b', mDots[0] + mDots[1]);
        put('c', mDots[4] + mDots[0]);
        put('d', mDots[0] + mDots[4] + mDots[5]);
        put('e', mDots[0] + mDots[5]);
        put('f', mDots[0] + mDots[1] + mDots[4]);
        put('g', mDots[0] + mDots[1] + mDots[4] + mDots[5]);
        put('h', mDots[0] + mDots[1] + mDots[5]);
        put('i', mDots[1] + mDots[4]);
        put('j', mDots[1] + mDots[4] + mDots[5]);
        put('k', mDots[0] + mDots[2]);
        put('l', mDots[0] + mDots[1] + mDots[2]);
        put('m', mDots[0] + mDots[2] + mDots[4]);
        put('n', mDots[0] + mDots[2] + mDots[4] + mDots[5]);
        put('o', mDots[0] + mDots[2] + mDots[5]);
        put('p', mDots[0] + mDots[1] + mDots[2] + mDots[4]);
        put('q', mDots[0] + mDots[1] + mDots[2] + mDots[4] + mDots[5]);
        put('r', mDots[0] + mDots[1] + mDots[2] + mDots[5]);
        put('s', mDots[1] + mDots[2] + mDots[4]);
        put('t', mDots[1] + mDots[2] + mDots[4] + mDots[5]);
        put('u', mDots[0] + mDots[2] + mDots[6]);
        put('v', mDots[0] + mDots[1] + mDots[2] + mDots[6]);
        put('w', mDots[1] + mDots[4] + mDots[5] + mDots[6]);
        put('x', mDots[0] + mDots[2] + mDots[4] + mDots[6]);
        put('y', mDots[0] + mDots[2] + mDots[4] + mDots[5] + mDots[6]);
        put('z', mDots[0] + mDots[2] + mDots[5] + mDots[6]);
        put(' ', 0);



    }};
    public int translateToBraille(Object letter)
    {
        Log.v("BrailleTouchTest", "Printing " + Character.toString((char)letter));
        if (englishDictionary.containsKey(Character.toLowerCase((char)letter)))
        return englishDictionary.get(Character.toLowerCase((char)letter));
        else return 0;
    }
}
