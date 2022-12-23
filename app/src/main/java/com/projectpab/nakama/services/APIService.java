package com.projectpab.nakama.services;

import com.projectpab.nakama.models.Crew;
import com.projectpab.nakama.models.Haki;
import com.projectpab.nakama.models.Movie;
import com.projectpab.nakama.models.Pirates;
import com.projectpab.nakama.models.ValueData;
import com.projectpab.nakama.models.ValueNoData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {
    //==================================PIRATES=============================================
    @POST("getAllPirates")
    @FormUrlEncoded
    Call<ValueData<Pirates>> getAllPirates(@Field("key") String key);

    @POST("insertPirates")
    @FormUrlEncoded
    Call<ValueNoData> insertPirates(@Field("key") String key,
                                    @Field("pirates_name") String pirates_name,
                                    @Field("pirates_photo") String pirates_photo);
    @POST("updatePirates")
    @FormUrlEncoded
    Call<ValueNoData> updatePirates(@Field("key") String key,
                                    @Field("pirates_id") int pirates_id,
                                    @Field("pirates_name") String pirates_name,
                                    @Field("pirates_photo") String pirates_photo);

    @POST("deletePirates")
    @FormUrlEncoded
    Call<ValueNoData> deletePirates(@Field("key") String key,
                                    @Field("pirates_id") int pirates_id);

}
