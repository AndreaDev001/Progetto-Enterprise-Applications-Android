package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Address
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface AddressRetrofitApi {
    @GET("addresses/private/user/{userID}")
    fun getAddresses(@Path("userID") userID: UUID): Call<List<Address>>;
}