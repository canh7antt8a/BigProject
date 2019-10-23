package com.t3h.bigproject.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("password")
    private String password;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("dat_of_birth")
    private String dateOfBirth;
    @SerializedName("age")
    private int age;
    @SerializedName("phonenumber")
    private String phonenumber;
    @SerializedName("email")
    private String email;
    @SerializedName("permission")
    private String permission;

    public int getId() {
        return id;
    }

    @SerializedName("sex")

    private String sex;

    public String getAvatar() {
        return avatar;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPermission() {
        return permission;
    }

    public String getSex() {
        return sex;
    }
}
