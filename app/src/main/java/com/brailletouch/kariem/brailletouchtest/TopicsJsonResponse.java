package com.brailletouch.kariem.brailletouchtest;

import java.util.List;

/**
 * Created by Kariem on 1/18/2016.
 */
public class TopicsJsonResponse {

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    List<Topic> topics;
}
