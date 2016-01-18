package com.brailletouch.kariem.brailletouchtest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Kariem on 1/17/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PinnedTweet {
    Tweet tweet;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Tweet getTweet() {

        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }
}
