package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.PagedModel
import com.enterpriseapplications.model.Review
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface ReviewRetrofitApi {
    @GET("reviews/public/{userID}/written")
    fun getWrittenReviews(@Path("userID") userID: UUID,@Query("page") page: Int = 0,@Query("pageSize") pageSize :Int = 20): Call<PagedModel<Review>>;
    @GET("reviews/public/{userID}/received")
    fun getReceivedReviews(@Path("userID") userID: UUID,@Query("page") page: Int,@Query("pageSize") pageSize: Int = 20): Call<PagedModel<Review>>;
}