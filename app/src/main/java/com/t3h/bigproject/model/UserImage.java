package com.t3h.bigproject.model;

import com.google.gson.annotations.SerializedName;

public class UserImage {
    @SerializedName("avatar")
    private String avatar;

    public String getAvatar() {
        return avatar;
    }
}
