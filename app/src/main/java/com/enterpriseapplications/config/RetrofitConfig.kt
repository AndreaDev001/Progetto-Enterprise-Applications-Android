package com.enterpriseapplications.config

import android.app.Application
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.config.authentication.AuthorizationInterceptor
import com.enterpriseapplications.controllers.BanRetrofitApi
import com.enterpriseapplications.controllers.ProductRetrofitApi
import com.enterpriseapplications.controllers.reports.ReportRetrofitApi
import com.enterpriseapplications.controllers.UserRetrofitApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConfig(val application: Application,authenticationManager: AuthenticationManager)
{
    var baseURL: String = "http://192.168.1.74:8080/api/v1/"
    private val httpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(AuthorizationInterceptor(authenticationManager)).build()
    private val retrofit: Retrofit = Retrofit.
            Builder()
            .baseUrl(baseURL)
            .addCallAdapterFactory(AdapterFactory())
            .addConverterFactory(GsonConverterFactory.create()).
            client(httpClient)
            .build()

    val productController: ProductRetrofitApi = retrofit.create(ProductRetrofitApi::class.java)
    val userController: UserRetrofitApi = retrofit.create(UserRetrofitApi::class.java)
    val reportController: ReportRetrofitApi = retrofit.create(ReportRetrofitApi::class.java)
    val banController: BanRetrofitApi = retrofit.create(BanRetrofitApi::class.java)
}