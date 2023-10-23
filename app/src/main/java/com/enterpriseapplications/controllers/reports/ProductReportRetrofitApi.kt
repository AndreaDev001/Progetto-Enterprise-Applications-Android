package com.enterpriseapplications.controllers.reports

import com.enterpriseapplications.model.create.CreateReport
import com.enterpriseapplications.model.reports.ProductReport
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface ProductReportRetrofitApi {
    @POST("productReports/private/{productID}")
    fun createProductReport(@Body createReport: CreateReport,@Path("productID") productID: UUID): Call<ProductReport>;
}