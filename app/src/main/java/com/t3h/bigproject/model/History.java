package com.t3h.bigproject.model;

import com.google.gson.annotations.SerializedName;

public class History {
    @SerializedName("user_name")
    private String userName;
    @SerializedName("image")
    private String imageLink;
    @SerializedName("drink_name")
    private String nameDrink;
    @SerializedName("price")
    private String price;
    @SerializedName("number_drink")
    private int numberDrink;
    @SerializedName("Date")
    private String date;

    public String getDate() {
        return date;
    }

    public String getUserName() {
        return userName;
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


    public int getNumberDrink() {
        return numberDrink;
    }
}
