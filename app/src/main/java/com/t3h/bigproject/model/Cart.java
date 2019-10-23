package com.t3h.bigproject.model;

import com.google.gson.annotations.SerializedName;

public class Cart {
    @SerializedName("id")
    private int id;
    @SerializedName("id_user")
    private int idUser;
    @SerializedName("id_drink")
    private int idDrink;

    public int getId() {
        return id;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getIdDrink() {
        return idDrink;
    }
}
