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

    //==================================CREW=============================================
    @POST("getAllCrew")
    @FormUrlEncoded
    Call<ValueData<Crew>> getAllCrew(@Field("key") String key);

    @POST("getCrewByPirates")
    @FormUrlEncoded
    Call<ValueData<Crew>> getCrewByPirates(@Field("key") String key,
                                           @Field("pirates_id") int pirates_id);

    @POST("insertCrew")
    @FormUrlEncoded
    Call<ValueNoData> insertCrew(@Field("key") String key,
                                 @Field("pirates_id") int pirates_id,
                                 @Field("crew_name") String crew_name,
                                 @Field("crew_photo") String crew_photo,
                                 @Field("crew_bounty") String crew_bounty);
    @POST("updateCrew")
    @FormUrlEncoded
    Call<ValueNoData> updateCrew(@Field("key") String key,
                                 @Field("crew_id") int crew_id,
                                 @Field("pirates_id") int pirates_id,
                                 @Field("crew_name") String crew_name,
                                 @Field("crew_photo") String crew_photo,
                                 @Field("crew_bounty") String crew_bounty);

    @POST("deleteCrew")
    @FormUrlEncoded
    Call<ValueNoData> deleteCrew(@Field("key") String key,
                                 @Field("crew_id") int crew_id);

    //==================================MOVIE=============================================
    @POST("getAllMovie")
    @FormUrlEncoded
    Call<ValueData<Movie>> getAllMovie(@Field("key") String key);

    @POST("insertMovie")
    @FormUrlEncoded
    Call<ValueNoData> insertMovie(@Field("key") String key,
                                  @Field("movie_name") String movie_name,
                                  @Field("movie_year") String movie_year,
                                  @Field("movie_photo") String movie_photo);
    @POST("updateMovie")
    @FormUrlEncoded
    Call<ValueNoData> updateMovie(@Field("key") String key,
                                  @Field("movie_id") int movie_id,
                                  @Field("movie_name") String movie_name,
                                  @Field("movie_year") String movie_year,
                                  @Field("movie_photo") String movie_photo);

    @POST("deleteMovie")
    @FormUrlEncoded
    Call<ValueNoData> deleteMovie(@Field("key") String key,
                                  @Field("movie_id") int movie_id);

}
