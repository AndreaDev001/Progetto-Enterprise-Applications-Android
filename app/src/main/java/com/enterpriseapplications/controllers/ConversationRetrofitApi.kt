package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Conversation
import com.enterpriseapplications.model.PagedModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface ConversationRetrofitApi
{
    @GET("conversations/public/{userID}")
    fun getConversations(@Path("userID") userID: UUID): Call<List<Conversation>>;
}