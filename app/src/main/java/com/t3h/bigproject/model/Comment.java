package com.t3h.bigproject.model;

import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("id")
    private int id;
    @SerializedName("id_drink")
    private int id_drink;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("rate")
    private float rate;
    @SerializedName("comment")
    private String comment;

    public int getId() {
        return id;
    }

    public int getId_drink() {
        return id_drink;
    }

    public String getUserName() {
        return userName;
    }

    public float getRate() {
        return rate;
    }

    public String getComment() {
        return comment;
    }
}
