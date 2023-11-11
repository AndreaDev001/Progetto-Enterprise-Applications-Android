package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.PaymentMethod
import com.enterpriseapplications.model.create.CreatePaymentMethod
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface PaymentMethodRetrofitApi {
    @GET("paymentMethods/private/owner/{userID}")
    fun getPaymentMethods(@Path("userID") userID: UUID): Call<List<PaymentMethod>>;
    @GET("paymentMethods/public/brands")
    fun getPaymentMethodBrands(): Call<List<String>>;
    @POST("paymentMethods/private")
    fun createPaymentMethod(@Body paymentMethod: CreatePaymentMethod): Call<PaymentMethod>;
}