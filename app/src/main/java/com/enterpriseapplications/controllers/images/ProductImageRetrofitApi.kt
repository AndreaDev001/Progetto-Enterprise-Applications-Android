package com.enterpriseapplications.controllers.images

import com.enterpriseapplications.model.images.ProductImage
import com.enterpriseapplications.model.images.UserImage
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.util.UUID

interface ProductImageRetrofitApi {
    @GET("productImages/public/{productID}/amount")
    fun getAmount(@Path("productID") productID: UUID): Call<Int>;

    @POST("productImages/private/{productID}")
    @Multipart
    fun updateProductImages(@Path("productID") productID: UUID,@Part files: List<MultipartBody.Part>): Call<List<ProductImage>>;
}