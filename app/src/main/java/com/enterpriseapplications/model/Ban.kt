package com.enterpriseapplications.model

import com.enterpriseapplications.model.refs.UserRef
import java.time.LocalDate
import java.util.UUID

data class Ban(val id: UUID,
val banner: UserRef,
val banned: UserRef,
val description: String,
val reason: String,
val type: String,
val expired: Boolean,
val createdDate: LocalDate,
val expirationDate: LocalDate)