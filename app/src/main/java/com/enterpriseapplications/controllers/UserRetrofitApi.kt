package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.PagedModel
import com.enterpriseapplications.model.UserDetails
import com.enterpriseapplications.model.create.UpdateUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface UserRetrofitApi
{

    @PUT("users/private")
    fun updateUser(@Body updateUser: UpdateUser): Call<UserDetails>;

    @GET("users/public/{userID}/details")
    fun getUserDetails(@Path("userID") userID: UUID): Call<UserDetails>;

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
        @Query("minRating") minRating: Int?,
        @Query("maxRating") maxRating: Int?,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<PagedModel<UserDetails>>
}