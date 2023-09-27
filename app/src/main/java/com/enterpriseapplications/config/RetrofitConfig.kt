package com.enterpriseapplications.config

import com.enterpriseapplications.controllers.BanRetrofitApi
import com.enterpriseapplications.controllers.ProductRetrofitApi
import com.enterpriseapplications.controllers.ReportRetrofitApi
import com.enterpriseapplications.controllers.UserRetrofitApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConfig
{
    var baseURL: String = "http://192.168.1.74:8080/api/v1/"
    val client = OkHttpClient.Builder().build()
    val retrofit: Retrofit = Retrofit.
            Builder()
            .baseUrl(baseURL)
            .addCallAdapterFactory(AdapterFactory())
            .addConverterFactory(GsonConverterFactory.create()).
            client(client)
            .build()

    val productController: ProductRetrofitApi = retrofit.create(ProductRetrofitApi::class.java)
    val userController: UserRetrofitApi = retrofit.create(UserRetrofitApi::class.java)
    val reportController: ReportRetrofitApi = retrofit.create(ReportRetrofitApi::class.java)
    val banController: BanRetrofitApi = retrofit.create(BanRetrofitApi::class.java)
}