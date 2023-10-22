package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Category
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface CategoryRetrofitApi
{
    @GET("categories/public")
    fun getCategories(): Call<List<Category>>
    @GET("categories/public/{categoryID}")
    fun getCategory(@Path("categoryID") categoryID: UUID): Call<Category>
    @GET("categories/public/primaries")
    fun getPrimaries(): Call<List<String>>;
    @GET("categories/public/secondaries/{primary}")
    fun getSecondaries(@Path("primary") primary: String): Call<List<String>>;
    @GET("categories/public/tertiaries/{primary}/{secondary}")
    fun getTertiaries(@Path("primary") primary: String,@Path("secondary") secondary: String): Call<List<String>>;
}