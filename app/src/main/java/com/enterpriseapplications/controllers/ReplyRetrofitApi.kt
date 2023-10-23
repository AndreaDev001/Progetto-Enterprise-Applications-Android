package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Reply
import com.enterpriseapplications.model.create.CreateReply
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ReplyRetrofitApi {
    @POST("replies/private")
    fun createReply(@Body createReply: CreateReply) : Call<Reply>;
}