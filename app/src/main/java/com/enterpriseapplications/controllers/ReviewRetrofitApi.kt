package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.PagedModel
import com.enterpriseapplications.model.Review
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface ReviewRetrofitApi {
    @GET("reviews/public/{userID}/written")
    fun getWrittenReviews(@Path("userID") userID: UUID): Call<PagedModel<Review>>;
    @GET("reviews/public/{userID}/received")
    fun getReceivedReviews(@Path("userID") userID: UUID): Call<PagedModel<Review>>;
}