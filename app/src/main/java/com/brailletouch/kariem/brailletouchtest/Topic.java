package com.brailletouch.kariem.brailletouchtest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Kariem on 1/17/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Topic {
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Tweet[] getTweets() {
        return tweets;
    }

    public void setTweets(Tweet[] tweets) {
        this.tweets = tweets;
    }

    PinnedTweet pinnedTweet;
    Header header;

    public PinnedTweet getPinnedTweet() {
        return pinnedTweet;
    }

    public void setPinnedTweet(PinnedTweet pinnedTweet) {
        this.pinnedTweet = pinnedTweet;
    }

    Tweet [] tweets;
}
