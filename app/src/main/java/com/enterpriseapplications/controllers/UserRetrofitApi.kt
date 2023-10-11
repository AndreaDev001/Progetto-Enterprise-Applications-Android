package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.PagedModel
import com.enterpriseapplications.model.UserDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserRetrofitApi
{

    @GET("users/public/genders")
    fun getGenders(): Call<List<String>>;

    @GET("users/public/visibilities")
    fun getVisibilities(): Call<List<String>>;

    @GET("users/public/spec")
    fun getUsers(
        @Query("email") email: String?,
        @Query("username") username: String?,
        @Query("name") name: String?,
        @Query("surname") surname: String?,
        @Query("gender") gender: String?,
        @Query("description") description: String?,
        @Query("minRating") minRating: Number?,
        @Query("maxRating") maxRating: Number?,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<PagedModel<UserDetails>>
}