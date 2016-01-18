package com.brailletouch.kariem.brailletouchtest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Kariem on 1/17/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trending {
    String text;
    int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
