package com.enterpriseapplications.model.reports

import com.enterpriseapplications.model.refs.UserRef
import java.time.LocalDate
import java.util.UUID

data class Report(val id: String,
val reporter: UserRef,
val reported: UserRef,
val description: String,
val reason: String,
val type: String,
val createdDate: String)