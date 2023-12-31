package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.PagedModel
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.model.create.CreateProduct
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.math.BigDecimal
import java.util.UUID

interface ProductRetrofitApi {

    @GET("products/public/spec")
    fun getProducts(
        @Query("primaryCat") primaryCat: String?,
        @Query("secondaryCat") secondaryCat: String?,
        @Query("tertiaryCat") tertiaryCat: String?,
        @Query("name") name: String?,
        @Query("description") description: String?,
        @Query("condition") condition: String?,
        @Query("minPrice") minPrice: BigDecimal?,
        @Query("maxPrice") maxPrice: BigDecimal?,
        @Query("minLikes") minLikes: BigDecimal?,
        @Query("maxLikes") maxLikes: BigDecimal?,
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 20) : Call<PagedModel<Product>>


    @GET("products/public/seller/{userID}")
    fun getSellerProducts(@Path("userID") userID: UUID,@Query("page") page: Int = 0,@Query("pageSize") pageSize: Int = 20): Call<PagedModel<Product>>;

    @GET("products/public/created")
    fun getRecentlyCreated(@Query("page") page: Int,@Query("pageSize") pageSize: Int): Call<PagedModel<Product>>;
    @GET("products/public/{productID}/similar")
    fun getSimilarProducts(@Path("productID") productID: UUID,@Query("page") page: Int,@Query("pageSize") pageSize: Int = 20): Call<PagedModel<Product>>;

    @GET("products/public/liked")
    fun getMostLiked(@Query("page") page: Int,@Query("pageSize") pageSize: Int): Call<PagedModel<Product>>;
    @GET("products/public/expensive")
    fun getMostExpensive(@Query("page") page: Int,@Query("pageSize") pageSize: Int): Call<PagedModel<Product>>;
    @GET("products/public/{productID}/details")
    fun getDetails(@Path("productID") productID: UUID): Call<Product>
    @GET("products/public/conditions")
    fun getConditions(): Call<List<String>>;
    @GET("products/public/visibilities")
    fun getVisibilities(): Call<List<String>>
    @POST("products/private")
    fun createProduct(@Body createProduct: CreateProduct): Call<Product>;
}