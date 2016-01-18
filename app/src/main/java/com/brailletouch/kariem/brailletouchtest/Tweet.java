package com.brailletouch.kariem.brailletouchtest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Kariem on 1/17/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet {

    JSONText [] replicas;
    long created_date;
    JSONText [] hashtags_list;
    String text;
    JSONText [] urls_list;
    JSONText [] media_url_list;
    long last_retweet_date;
    String created_date_h;
    String tweet_id;
    JSONText [] media_list;
    int score;
    String type;
    int solrScore;
    int customScore;

    public String getTweet_id() {
        return tweet_id;
    }

    public void setTweet_id(String tweet_id) {
        this.tweet_id = tweet_id;
    }

    public long getCreated_date() {
        return created_date;
    }

    public void setCreated_date(long created_date) {
        this.created_date = created_date;
    }

    public JSONText[] getHashtags_list() {
        return hashtags_list;
    }

    public void setHashtags_list(JSONText[] hashtags_list) {
        this.hashtags_list = hashtags_list;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public JSONText[] getUrls_list() {
        return urls_list;
    }

    public void setUrls_list(JSONText[] urls_list) {
        this.urls_list = urls_list;
    }

    public JSONText[] getMedia_url_list() {
        return media_url_list;
    }

    public void setMedia_url_list(JSONText[] media_url_list) {
        this.media_url_list = media_url_list;
    }

    public long getLast_retweet_date() {
        return last_retweet_date;
    }

    public void setLast_retweet_date(long last_retweet_date) {
        this.last_retweet_date = last_retweet_date;
    }

    public String getCreated_date_h() {
        return created_date_h;
    }

    public void setCreated_date_h(String created_date_h) {
        this.created_date_h = created_date_h;
    }

    public JSONText[] getMedia_list() {
        return media_list;
    }

    public void setMedia_list(JSONText[] media_list) {
        this.media_list = media_list;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSolrScore() {
        return solrScore;
    }

    public void setSolrScore(int solrScore) {
        this.solrScore = solrScore;
    }

    public int getCustomScore() {
        return customScore;
    }

    public void setCustomScore(int customScore) {
        this.customScore = customScore;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(int retweet_count) {
        this.retweet_count = retweet_count;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public JSONText[] getUrl_elongated_list() {
        return url_elongated_list;
    }

    public void setUrl_elongated_list(JSONText[] url_elongated_list) {
        this.url_elongated_list = url_elongated_list;
    }

    public JSONText[] getLabels_list() {
        return labels_list;
    }

    public void setLabels_list(JSONText[] labels_list) {
        this.labels_list = labels_list;
    }

    public User getUsersList() {
        return usersList;
    }

    public void setUsersList(User usersList) {
        this.usersList = usersList;
    }

    public JSONText[] getReplicas() {

        return replicas;
    }

    public void setReplicas(JSONText[] replicas) {
        this.replicas = replicas;
    }

    int retweet_count;
    String user_id;
    JSONText [] url_elongated_list;
    JSONText [] labels_list;

    public JSONText[] getMentions_list() {
        return mentions_list;
    }

    public void setMentions_list(JSONText[] mentions_list) {
        this.mentions_list = mentions_list;
    }

    JSONText [] mentions_list;
    User  usersList;


}
