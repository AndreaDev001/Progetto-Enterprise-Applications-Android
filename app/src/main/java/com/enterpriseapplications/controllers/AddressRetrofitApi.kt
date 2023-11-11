package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Address
import com.enterpriseapplications.model.AddressSearchResponse
import com.enterpriseapplications.model.create.CreateAddress
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface AddressRetrofitApi {
    @GET("addresses/private/user/{userID}")
    fun getAddresses(@Path("userID") userID: UUID): Call<List<Address>>;
    @GET("addresses/public/countries")
    fun getCountries(): Call<List<String>>;
    @GET("addresses/public/query")
    fun searchForAddresses(@Query("countryCode") code: String,@Query("query") query: String): Call<AddressSearchResponse>;
    @POST("addresses/private")
    fun createAddress(@Body createAddress: CreateAddress): Call<Address>;
}