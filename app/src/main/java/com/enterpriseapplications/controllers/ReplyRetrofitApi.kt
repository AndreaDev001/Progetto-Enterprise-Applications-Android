package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Reply
import com.enterpriseapplications.model.create.CreateReply
import com.enterpriseapplications.model.update.UpdateReply
import com.enterpriseapplications.model.update.UpdateReview
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ReplyRetrofitApi {
    @POST("replies/private")
    fun createReply(@Body createReply: CreateReply) : Call<Reply>;
    @PUT("replies/private")
    fun updateReply(@Body updateReply: UpdateReply): Call<Reply>;
}