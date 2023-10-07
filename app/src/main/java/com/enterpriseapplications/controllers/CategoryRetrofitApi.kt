package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Category
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface CategoryRetrofitApi
{
    @GET("public")
    fun getCategories(): Call<List<Category>>
    @GET("public/{categoryID}")
    fun getCategory(@Path("categoryID") categoryID: UUID): Call<Category>
    @GET("public/primaries")
    fun getPrimaries(): Call<List<String>>;
    @GET("public/secondaries/{primary}")
    fun getSecondaries(@Path("primary") primary: String): Call<List<String>>;
    @GET("public/tertiaries/{primary}/{secondary}")
    fun getTertiaries(@Path("primary") primary: String,@Path("secondary") secondary: String): Call<List<String>>;
}