package com.t3h.bigproject.model;

import com.google.gson.annotations.SerializedName;

public class UploadDrink {
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("image")
    private String image;

    public String getImage() {
        return image;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
