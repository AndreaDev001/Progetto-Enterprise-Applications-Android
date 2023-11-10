package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Conversation
import com.enterpriseapplications.model.PagedModel
import com.enterpriseapplications.model.create.CreateConversation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface ConversationRetrofitApi
{
    @GET("conversations/private/{userID}")
    fun getConversations(@Path("userID") userID: UUID): Call<List<Conversation>>;
    @GET("conversations/private/conversation/{conversationID}")
    fun getConversationById(@Path("conversationID") conversationID: UUID): Call<Conversation>;
    @GET("conversations/private/conversation/{userID}/{productID}")
    fun getConversationByStarter(@Path("userID") userID: UUID,@Path("productID") productID: UUID): Call<Conversation>;
    @POST("conversations/private")
    fun createConversation(@Body createConversation: CreateConversation): Call<Conversation>;
}