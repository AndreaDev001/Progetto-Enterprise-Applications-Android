package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Ban
import com.enterpriseapplications.model.PagedModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BanRetrofitApi {
    @GET("bans/public/spec")
    fun getBans(
        @Query("bannerEmail") bannerEmail: String,
        @Query("bannedEmail") bannedEmail: String,
        @Query("bannerUsername") bannerUsername: String,
        @Query("bannedUsername") bannedUsername: String,
        @Query("expired") expired: Boolean,
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 20) : Call<PagedModel<Ban>>
}