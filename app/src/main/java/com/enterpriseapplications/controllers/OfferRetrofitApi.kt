package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Offer
import com.enterpriseapplications.model.PagedModel
import com.enterpriseapplications.model.create.CreateOffer
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

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

    @GET("offers/public/{userID}/created")
    fun getCreatedOffers(@Path("userID") userID: UUID,@Query("page") page: Int = 0,@Query("pageSize") pageSize: Int = 20): Call<PagedModel<Offer>>;
    @GET("offers/public/{userID}/received")
    fun getReceivedOffers(@Path("userID") userID: UUID,@Query("page") page: Int = 0,@Query("pageSize") pageSize: Int = 20): Call<PagedModel<Offer>>;
    @GET("offers/public/statuses")
    fun getStatuses(): Call<List<String>>;
    @POST("offers/private")
    fun createOffer(@Body createOffer: CreateOffer): Call<Offer>;
}