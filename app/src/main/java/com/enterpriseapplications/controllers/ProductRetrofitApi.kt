package com.enterpriseapplications.controllers

import com.enterpriseapplications.config.Adapter
import com.enterpriseapplications.model.Product
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductRetrofitApi {

    @GET("{id}")
    fun getDetails(@Path("id") id: Long): Adapter<Product>
    @GET("products/conditions")
    fun getConditions(): Adapter<List<String>>
    @GET("products/visibilities")
    fun getVisibilities(): Adapter<List<String>>
}