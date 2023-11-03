package com.enterpriseapplications.model

import com.enterpriseapplications.model.refs.UserRef
import java.time.LocalDate
import java.util.UUID

data class Reply(val id: String,
val text: String,
val writer: UserRef,
val reviewID: String,
val createdDate: String)