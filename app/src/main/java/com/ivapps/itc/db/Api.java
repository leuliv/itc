package com.ivapps.itc.db;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("register.php")
    Call<JsonObject> register(
            @Field("name") String name,
            @Field("department") String department,
            @Field("email") String email,
            @Field("phone") int phone
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<JsonObject> login(
            @Field("phone") int phone
    );

    @POST("checkConn.php")
    Call<JsonObject> checkConn();

}
