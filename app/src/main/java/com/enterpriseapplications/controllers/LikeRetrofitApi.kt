package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Like
import com.enterpriseapplications.model.PagedModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface LikeRetrofitApi
{
    @GET("likes/public/user/{userID}")
    fun getLikedProducts(@Path("userID") userID: UUID,@Query("page") page: Int = 0,@Query("pageSize") pageSize: Int = 20): Call<PagedModel<Like>>;
}