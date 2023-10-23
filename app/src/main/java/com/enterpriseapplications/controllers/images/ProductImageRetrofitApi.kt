package com.enterpriseapplications.controllers.images

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface ProductImageRetrofitApi {
    @GET("productImages/public/{productID}/amount")
    fun getAmount(@Path("productID") productID: UUID): Call<Int>;
}