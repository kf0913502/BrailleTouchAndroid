package com.brailletouch.kariem.brailletouchtest;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by Kariem on 12/30/2015.
 */
public class EnglishTranslator extends BrailleTouchTranslator{

    final static int dots[] = {1,2,4,64, 8, 16,32,128};

    static HashMap<Character, Integer> englishDictionary = new HashMap<Character, Integer>(){{
        put('a',dots[0]);
        put('b',dots[0] + dots[1]);
        put('c',dots[4] + dots[0]);
        put('d',dots[0] + dots[4] + dots[5]);
        put('e',dots[0] + dots[5]);
        put('f',dots[0] + dots[1] + dots[4]);
        put('g',dots[0] + dots[1] + dots[4] + dots[5]);
        put('h',dots[0] + dots[1] + dots[5]);
        put('i',dots[1] + dots[4]);
        put('j',dots[1] + dots[4] + dots[5]);
        put('k',dots[0] + dots[2]);
        put('l',dots[0] + dots[1] + dots[2]);
        put('m',dots[0] + dots[2] + dots[4]);
        put('n',dots[0] + dots[2] + dots[4] + dots[5]);
        put('o',dots[0] + dots[2] + dots[5]);
        put('p',dots[0] + dots[1] + dots[2] + dots[4]);
        put('q',dots[0] + dots[1] + dots[2] + dots[4] + dots[5]);
        put('r',dots[0] + dots[1] + dots[2] + dots[5]);
        put('s',dots[1] + dots[2] + dots[4]);
        put('t',dots[1] + dots[2] + dots[4] + dots[5]);
        put('u',dots[0] + dots[2] + dots[6]);
        put('v',dots[0] + dots[1] + dots[2] + dots[6]);
        put('w',dots[1] + dots[4] + dots[5] + dots[6]);
        put('x', dots[0] + dots[2] + dots[4] + dots[6]);
        put('y',dots[0] + dots[2] + dots[4] + dots[5] + dots[6]);
        put('z',dots[0] + dots[2] + dots[5] + dots[6]);



    }};
    public int translateToBraille(Object letter)
    {
        Log.v("BrailleTouchTest", "Printing " + Character.toString((char)letter));
        return englishDictionary.get(Character.toLowerCase((char)letter));
    }
}
