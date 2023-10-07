package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Offer
import com.enterpriseapplications.model.PagedModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OfferRetrofitApi
{
    @GET("offers/public/spec")
    fun getOffers(
        @Query("description") description: String?,
        @Query("price") price: Number?,
        @Query("buyerEmail") buyerEmail: String?,
        @Query("buyerUsername") buyerUsername: String?,
        @Query("productName") productName: String?,
        @Query("productDescription") productDescription: String?,
        @Query("status") offerStatus: String?,
        @Query("expired") expired: Boolean?,
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 20
    ): Call<PagedModel<Offer>>

    @GET("offers/public/statuses")
    fun getStatuses(): Call<List<String>>;
}