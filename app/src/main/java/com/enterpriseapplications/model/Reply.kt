package com.enterpriseapplications.model

import com.enterpriseapplications.model.refs.UserRef
import java.time.LocalDate
import java.util.UUID

data class Reply(val id: UUID,
val text: String,
val writer: UserRef,
val reviewID: UUID,
val createdDate: LocalDate)