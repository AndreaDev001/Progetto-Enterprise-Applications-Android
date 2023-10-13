package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Order
import com.enterpriseapplications.model.PagedModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface OrderRetrofitApi {
    @GET("reviews/public/{userID}/orders")
    fun getOrders(@Path("userID") userID: UUID): Call<PagedModel<Order>>;
}