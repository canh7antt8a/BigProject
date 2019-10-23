package com.t3h.bigproject.model;


import com.google.gson.annotations.SerializedName;

public class UploadAvatar {
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("name_file")
    private String imageName;

    public String getImageName() {
        return imageName;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
