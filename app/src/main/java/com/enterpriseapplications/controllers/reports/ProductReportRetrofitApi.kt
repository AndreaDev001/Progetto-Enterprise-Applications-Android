package com.enterpriseapplications.controllers.reports

import com.enterpriseapplications.model.create.CreateReport
import com.enterpriseapplications.model.reports.ProductReport
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface ProductReportRetrofitApi {

    @GET("productReports/private/{reportID}")
    fun getProductReport(@Path("reportID") productReportID: UUID): Call<ProductReport>;
    @POST("productReports/private/{productID}")
    fun createProductReport(@Body createReport: CreateReport,@Path("productID") productID: UUID): Call<ProductReport>;
    @GET("productReports/private/productReport/{userID}/{productID}")
    fun getProductReport(@Path("userID") userID: UUID,@Path("productID") productID: UUID): Call<ProductReport>;
}