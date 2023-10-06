package com.enterpriseapplications.controllers

import com.enterpriseapplications.config.Adapter
import com.enterpriseapplications.model.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductRetrofitApi {

    @GET("/public/{productID}")
    fun getDetails(@Path("productID") id: Long): Adapter<Product>
    @GET("/public/products/conditions")
    fun getConditions(): Call<List<String>>;
    @GET("/public/products/visibilities")
    fun getVisibilities(): Adapter<List<String>>
}