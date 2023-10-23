package com.enterpriseapplications.controllers.reports

import com.enterpriseapplications.model.create.CreateReport
import com.enterpriseapplications.model.reports.MessageReport
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface MessageReportRetrofitApi {
    @POST("messageReports/private/{messageID}")
    fun createMessageReport(@Body createReport: CreateReport,@Path("messageID") messageID: UUID): Call<MessageReport>;
}