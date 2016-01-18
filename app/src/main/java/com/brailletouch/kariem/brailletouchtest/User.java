package com.brailletouch.kariem.brailletouchtest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Kariem on 1/17/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    public String getUser_name() {
        return user_name;
    }

    public String getUser_profile_img() {
        return user_profile_img;
    }

    public void setUser_profile_img(String user_profile_img) {
        this.user_profile_img = user_profile_img;
    }

    public String getUser_screen_name() {
        return user_screen_name;
    }

    public void setUser_screen_name(String user_screen_name) {
        this.user_screen_name = user_screen_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {

        this.user_name = user_name;
    }

    String user_name;
    String user_profile_img;
    String user_screen_name;
    String user_id;

}
