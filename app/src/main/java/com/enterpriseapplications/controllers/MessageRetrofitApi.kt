package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Conversation
import com.enterpriseapplications.model.Message
import com.enterpriseapplications.model.PagedModel
import com.enterpriseapplications.model.create.CreateMessage
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface MessageRetrofitApi {
    @GET("messages/private/conversation/{conversationID}")
    fun getConversation(@Path("conversationID") conversationID: UUID,@Query("page") page: Int = 0,@Query("pageSize") pageSize: Int = 20): Call<PagedModel<Message>>;
    @POST("messages/private")
    fun createMessage(@Body createMessage: CreateMessage): Call<Message>;
}