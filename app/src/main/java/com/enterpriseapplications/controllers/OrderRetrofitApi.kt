package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Order
import com.enterpriseapplications.model.PagedModel
import com.enterpriseapplications.model.create.CreateOrder
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface OrderRetrofitApi {
    @GET("orders/public/buyer/{userID}")
    fun getOrders(@Path("userID") userID: UUID,@Query("page") page: Int = 0,@Query("pageSize") pageSize: Int): Call<PagedModel<Order>>;
    @POST("orders/private")
    fun createOrder(@Body createOrder: CreateOrder): Call<Order>;
}