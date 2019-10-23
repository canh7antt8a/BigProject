package com.t3h.bigproject.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBuilder {
    private static Api instance;
    public static final String BASE_URL = "http://192.168.1.158/cafestore/";
    public static Api getInstance(){
        if (instance==null){

            instance = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build().create(Api.class);
        }
        return instance;
    }
}
