package com.enterpriseapplications.model

import com.enterpriseapplications.model.refs.UserRef
import java.time.LocalDate
import java.util.UUID

data class Review(val id: String,
val writer: UserRef,
val receiver: UserRef,
val rating: Int,
val text: String,
val reply: Reply?,
val createdDate: String,
)