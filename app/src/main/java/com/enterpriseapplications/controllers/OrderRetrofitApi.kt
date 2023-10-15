package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Order
import com.enterpriseapplications.model.PagedModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface OrderRetrofitApi {
    @GET("orders/public/{userID}/orders")
    fun getOrders(@Path("userID") userID: UUID,@Query("page") page: Int = 0,@Query("pageSize") pageSize: Int): Call<PagedModel<Order>>;
}