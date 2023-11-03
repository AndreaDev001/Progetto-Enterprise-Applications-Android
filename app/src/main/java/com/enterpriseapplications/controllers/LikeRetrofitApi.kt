package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Like
import com.enterpriseapplications.model.PagedModel
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface LikeRetrofitApi
{
    @GET("likes/private/user/{userID}")
    fun getLikedProducts(@Path("userID") userID: UUID,@Query("page") page: Int = 0,@Query("pageSize") pageSize: Int = 20): Call<PagedModel<Like>>
    @GET("likes/private/{userID}/{productID}")
    fun getLike(@Path("userID") userID: UUID,@Path("productID") productID: UUID): Call<Like>;
    @POST("likes/private/{productID}")
    fun createLike(@Path("productID") productID: UUID): Call<Like>;
    @DELETE("likes/private/likeID")
    fun deleteLike(@Path("likeID") likeID: UUID): Call<Void>;
    @DELETE("likes/private/product/{productID}")
    fun deleteLikeByProduct(@Path("productID") productID: UUID): Call<Any>;
}