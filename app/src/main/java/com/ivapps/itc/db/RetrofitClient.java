package com.ivapps.itc.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.ivapps.itc.R;

import java.net.ContentHandler;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient(Context ctx){
        String pref = ctx.getString(R.string.pref_name);
        SharedPreferences prefs = ctx.getSharedPreferences(pref, Context.MODE_PRIVATE);
        String ip = prefs.getString("ip", null);

        String BASE_URL = "http://"+ ip +"/itc/";

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized  RetrofitClient getInstance(Context context){
        if (mInstance == null){
            mInstance = new RetrofitClient(context);
        }
        return mInstance;
    }

    public Api getApi(){
        return retrofit.create(Api.class);
    }

}
