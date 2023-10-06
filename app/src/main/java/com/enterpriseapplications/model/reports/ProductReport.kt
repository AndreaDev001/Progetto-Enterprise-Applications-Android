package com.enterpriseapplications.model.reports

import com.enterpriseapplications.model.refs.UserRef
import java.time.LocalDate
import java.util.UUID

data class ProductReport(val id: UUID,
val reporter: UserRef,
val reported: UserRef,
val reason: String,
val type: String,
val createdDate: LocalDate,
val productID: UUID,
val details: String,
val name: String,
val description: String)