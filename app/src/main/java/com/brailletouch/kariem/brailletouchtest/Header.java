package com.brailletouch.kariem.brailletouchtest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Kariem on 1/17/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Header {

    String headline;
    Trending [] trending;

    public Trending[] getTrending() {
        return trending;
    }

    public void setTrending(Trending[] trending) {
        this.trending = trending;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getEvent_url() {
        return event_url;
    }

    public void setEvent_url(String event_url) {
        this.event_url = event_url;
    }

    public String getTopic_Date() {
        return topic_Date;
    }

    public void setTopic_Date(String topic_Date) {
        this.topic_Date = topic_Date;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public boolean isLoad_more() {
        return load_more;
    }

    public void setLoad_more(boolean load_more) {
        this.load_more = load_more;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public int getTotalFound() {
        return totalFound;
    }

    public void setTotalFound(int totalFound) {
        this.totalFound = totalFound;
    }

    public String getEvent_image() {
        return event_image;
    }

    public void setEvent_image(String event_image) {
        this.event_image = event_image;
    }

    public int getTopicid() {
        return topicid;
    }

    public void setTopicid(int topicid) {
        this.topicid = topicid;
    }

    String keywords;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    String event_url;
    String topic_Date;
    String labels;
    boolean load_more;
    boolean isApproved;
    int totalFound;
    String event_image;
    int topicid;
}
