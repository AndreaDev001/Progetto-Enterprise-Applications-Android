package com.enterpriseapplications.controllers.reports

import com.enterpriseapplications.model.PagedModel
import com.enterpriseapplications.model.create.CreateReport
import com.enterpriseapplications.model.reports.Report
import com.enterpriseapplications.model.update.UpdateReply
import com.enterpriseapplications.model.update.UpdateReport
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface ReportRetrofitApi {

    @GET("reports/public/spec")
    fun getReports(
        @Query("reporterEmail") reporterEmail: String?,
        @Query("reportedEmail") reportedEmail: String?,
        @Query("reporterUsername") reporterUsername: String?,
        @Query("reportedUsername") reportedUsername: String?,
        @Query("description") description: String?,
        @Query("reason") reason: String?,
        @Query("type") type: String?,
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 20,
    ) : Call<PagedModel<Report>>
    @GET("reports/public/reasons")
    fun getReasons(): Call<List<String>>
    @GET("reports/public/types")
    fun getTypes(): Call<List<String>>
    @POST("reports/private/{userID}")
    fun createReport(@Body createReport: CreateReport,@Path("userID") userID: UUID): Call<Report>;
    @PUT("reports/private")
    fun updateReport(@Body updateReport: UpdateReport)
}