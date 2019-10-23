package com.t3h.bigproject.model;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("id")
    private int id;
    @SerializedName("image")
    private String imageLink;
    @SerializedName("name_drink")
    private String nameDrink;
    @SerializedName("price")
    private String price;
    @SerializedName("number_comment")
    private String numberComment;
    @SerializedName("introduce_drink")
    private String introduceDrink;
    @SerializedName("rate")
    private float rate;
    @SerializedName("status")
    private int status;
    @SerializedName("number_drink")
    private int numberOrder;

    public int getNumberOrder() {
        return numberOrder;
    }

    public int getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getNameDrink() {
        return nameDrink;
    }

    public String getPrice() {
        return price;
    }

    public String getNumberComment() {
        return numberComment;
    }

    public String getIntroduceDrink() {
        return introduceDrink;
    }

    public float getRate() {
        return rate;
    }
}
