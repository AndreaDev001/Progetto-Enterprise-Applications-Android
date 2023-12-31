package com.enterpriseapplications.model.reports

import com.enterpriseapplications.model.refs.ProductRef
import com.enterpriseapplications.model.refs.UserRef
import java.time.LocalDate
import java.util.UUID

data class ProductReport(val id: String,
val reporter: UserRef,
val reported: UserRef,
val reason: String,
val type: String,
val createdDate: String,
val product: ProductRef)