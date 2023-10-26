package com.enterpriseapplications.controllers.images

import com.enterpriseapplications.model.images.UserImage
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface UserImageRetrofitApi {
    @POST("userImages/private")
    @Multipart
    fun updateUserImage(@Part file: MultipartBody.Part): Call<UserImage>;
}