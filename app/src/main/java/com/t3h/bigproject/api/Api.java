package com.t3h.bigproject.api;

import com.t3h.bigproject.model.Cart;
import com.t3h.bigproject.model.Comment;
import com.t3h.bigproject.model.History;
import com.t3h.bigproject.model.Order;
import com.t3h.bigproject.model.UploadAvatar;
import com.t3h.bigproject.model.UploadDrink;
import com.t3h.bigproject.model.User;
import com.t3h.bigproject.model.UserImage;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

//MainActivity function
    @POST("mainactivity/register.php")
    @FormUrlEncoded
    Call<ResponseBody> register(@Field("user_name") String userName,
                                @Field("avatar") String avatar,
                                @Field("password")  String password,
                                @Field("full_name") String full_name,
                                @Field("date_of_birth") String dateOfBirth,
                                @Field("age") int age,
                                @Field("phonenumber") String phonenumber,
                                @Field("email") String email,
                                @Field("permission") String permission,
                                @Field("sex") String sex);

    @POST("mainactivity/login.php")
    @FormUrlEncoded
    Call<User> login(@Field("user_name") String userName,
                     @Field("password") String password);

    @POST("mainactivity/getUserName.php")
    @FormUrlEncoded
    Call<ArrayList> checkUser(@Field("user_name") String userName);

    @POST("mainactivity/getEmail.php")
    @FormUrlEncoded
    Call<ArrayList> checkEmail(@Field("email") String email);

//userActivity Function

    //avatar

        @Multipart
        @POST("useractivity/uploadavatar.php")
        Call<UploadAvatar> uploadImage(@Part MultipartBody.Part file,
                                       @Part("image") RequestBody name);

        @POST("useractivity/updateavatar.php")
        @FormUrlEncoded
        Call<ResponseBody> uploadAvatarDatabase(@Field("user_name") String userName,
                                                @Field("avatar") String avatar);

        @POST("useractivity/getuseravatar.php")
        @FormUrlEncoded
        Call<UserImage> getImageUser(@Field("user_name") String userName);

        //comment
        @POST("useractivity/addcomment.php")
        @FormUrlEncoded
        Call<ResponseBody> writeComment(@Field("id_drink") int id_drink,
                                        @Field("user_name") String user_name,
                                        @Field("comment") String comment,
                                        @Field("rate") Float rate);

        @POST("useractivity/getcomment.php")
        @FormUrlEncoded
        Call<ArrayList<Comment>> getComment(@Field("id_drink") int idDrink);

    //cart
        @POST("useractivity/updatenumbercart.php")
        @FormUrlEncoded
        Call<ResponseBody> updateNumberDrink(@Field("id_user") int idUser,
                                             @Field("id_drink") int idDrink,
                                             @Field("number_drink") int numberDrink);

        @POST("useractivity/getcart.php")
        @FormUrlEncoded
        Call<ArrayList<Order>> getAllCartItem(@Field("id_user") int idUser);

        @POST("useractivity/addtocart.php")
        @FormUrlEncoded
        Call<ResponseBody> addCart(@Field("id_user") int idUser,
                                   @Field("id_drink") int idDrink,
                                   @Field("number_drink") int numberDrink);

        @POST("useractivity/checkcart.php")
        @FormUrlEncoded
        Call<Cart> checkCart(@Field("id_user") int idUser,
                             @Field("id_drink") int idDrink);

        @POST("useractivity/deletecart.php")
        @FormUrlEncoded
        Call<ResponseBody> deleteCart(@Field("id_user") int id_user,
                                      @Field("id_drink") int id_drink);
    //drink
        @POST("useractivity/getdrink.php")
        Call<ArrayList<Order>> getDrink();

        @POST("useractivity/addratedrink.php")
        @FormUrlEncoded
        Call<ResponseBody> addRateToDrink( @Field("id") int id,
                                           @Field("rate") float rate);
        @POST("useractivity/deletedrink.php")
        @FormUrlEncoded
        Call<ResponseBody> deleteDrink(@Field("id") int id);

        @POST("useractivity/addnumbercommentdrink.php")
        @FormUrlEncoded
        Call<ResponseBody> addNumberCommentDrink(@Field("id") int id,
                                                 @Field("number_comment") int numberComment);
    //order
        @POST("useractivity/addhistory.php")
        @FormUrlEncoded
        Call<ResponseBody> addtoorder(@Field("user_name") String userName,
                                      @Field("image") String image,
                                      @Field("drink_name") String nameDrink,
                                      @Field("price") String price,
                                      @Field("status") int status,
                                      @Field("number_drink") int numberOrder,
                                      @Field("Date") String date);

        @POST("useractivity/getHistory.php")
        @FormUrlEncoded
        Call<ArrayList<History>> getHistory(@Field("user_name") String user_name);
    //favorite

        @POST("useractivity/addtofavorite.php")
        @FormUrlEncoded
        Call<ResponseBody> addFavorite(@Field("id_user") int idUser,
                                        @Field("id_drink") int idDrink);
        @POST("useractivity/getfavorite.php")
        @FormUrlEncoded
        Call<ArrayList<Order>> getFavorite(@Field("id_user") int idUser);

        @POST("useractivity/deletefavorite.php")
        @FormUrlEncoded
        Call<ResponseBody> deleteFavorite(@Field("id_user") int idUser,
                                          @Field("id_drink") int idDrink);

        @POST("useractivity/checkfavorite.php")
        @FormUrlEncoded
        Call<Cart> checkFavorite(@Field("id_user") int idUser,
                                            @Field("id_drink") int idDrink);
//AddDrink
    @Multipart
    @POST("adddrinkactivity/addimagedrink.php")
    Call<UploadDrink> addImageDrink(@Part MultipartBody.Part file,
                                    @Part("image") RequestBody name);

    @POST("adddrinkactivity/inserimage.php")
    @FormUrlEncoded
    Call<ResponseBody> insertImage(@Field("image") String image);

    @POST("adddrinkactivity/updateinfordrink.php")
    @FormUrlEncoded
    Call<ResponseBody> addInforDrink(@Field("image") String image,
                                     @Field("name_drink") String drinkName,
                                     @Field("price") String price,
                                     @Field("introduce_drink") String introduceDrink,
                                     @Field("status") int status);

}

