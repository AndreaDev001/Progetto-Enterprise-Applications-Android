package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Follow
import com.enterpriseapplications.model.PagedModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface FollowRetrofitApi {
    @GET("follows/public/{userID}/followers")
    fun getFollowers(@Path("userID") userID: UUID): Call<PagedModel<Follow>>;
    @GET("follows/public/{userID}/followed")
    fun getFollowed(@Path("userID") userID: UUID): Call<PagedModel<Follow>>;
}