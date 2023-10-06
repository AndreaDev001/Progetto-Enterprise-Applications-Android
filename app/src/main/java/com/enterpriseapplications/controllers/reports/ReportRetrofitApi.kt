package com.enterpriseapplications.controllers.reports

import com.enterpriseapplications.config.Adapter
import retrofit2.http.GET

interface ReportRetrofitApi {
    
    @GET("/public/reports/reasons")
    fun getReasons(): Adapter<List<String>>
    @GET("/public/reports/types")
    fun getTypes(): Adapter<List<String>>
}