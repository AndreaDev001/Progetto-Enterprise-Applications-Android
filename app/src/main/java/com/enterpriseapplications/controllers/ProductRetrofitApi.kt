package com.enterpriseapplications.controllers

import com.enterpriseapplications.config.Adapter
import com.enterpriseapplications.model.PagedModel
import com.enterpriseapplications.model.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductRetrofitApi {

    @GET("products/public/spec")
    fun getProducts(
        @Query("primaryCat") primaryCat: String?,
        @Query("secondaryCat") secondaryCat: String?,
        @Query("tertiaryCat") tertiaryCat: String?,
        @Query("name") name: String?,
        @Query("description") description: String?,
        @Query("condition") condition: String?,
        @Query("minPrice") minPrice: Number?,
        @Query("maxPrice") maxPrice: Number?,
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 20) : Call<PagedModel<Product>>

    @GET("products/public/{productID}")
    fun getDetails(@Path("productID") id: Long): Call<Product>
    @GET("products/public/products/conditions")
    fun getConditions(): Call<List<String>>;
    @GET("/products/public/visibilities")
    fun getVisibilities(): Call<List<String>>
}