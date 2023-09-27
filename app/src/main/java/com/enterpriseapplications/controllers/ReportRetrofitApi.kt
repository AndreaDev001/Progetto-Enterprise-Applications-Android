package com.enterpriseapplications.controllers

import com.enterpriseapplications.config.Adapter
import retrofit2.http.GET

interface ReportRetrofitApi {
    
    @GET("reports/reasons")
    suspend fun getReasons(): Adapter<List<String>>
    @GET("reports/types")
    suspend fun getTypes(): Adapter<List<String>>
}