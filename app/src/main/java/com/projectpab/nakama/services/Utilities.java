package com.projectpab.nakama.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utilities {
    private static final String BASE_URL = "https://hostingimaa.000webhostapp.com/onepiece/index.php/MobileControl/";
    public static final String API_KEY = "UASPAB";
    private static Retrofit retrofit;
    public static final String EXTRA_ZOOM_FOTO = "ZOOM_FOTO";

    public static Retrofit getRetrofit(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
