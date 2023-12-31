package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.PagedModel
import com.enterpriseapplications.model.Review
import com.enterpriseapplications.model.create.CreateReview
import com.enterpriseapplications.model.update.UpdateReview
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface ReviewRetrofitApi {

    @GET("reviews/public/review")
    fun getReview(@Query("writerID") writerID: UUID,@Query("receiverID") receiverID: UUID): Call<Review>;
    @GET("reviews/public/{userID}/written")
    fun getWrittenReviews(@Path("userID") userID: UUID,@Query("page") page: Int = 0,@Query("pageSize") pageSize :Int = 20): Call<PagedModel<Review>>;
    @GET("reviews/public/{userID}/received")
    fun getReceivedReviews(@Path("userID") userID: UUID,@Query("page") page: Int,@Query("pageSize") pageSize: Int = 20): Call<PagedModel<Review>>;
    @POST("reviews/private")
    fun createReview(@Body createReview: CreateReview): Call<Review>;
    @PUT("reviews/private")
    fun updateReview(@Body updateReview: UpdateReview): Call<Review>;
}