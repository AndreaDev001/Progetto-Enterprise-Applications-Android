package com.enterpriseapplications.model.images

import com.enterpriseapplications.model.refs.UserRef
import java.time.LocalDate
import java.util.UUID

data class UserImage(val id: UUID,
val name: String,
val type: String,
val createdDate: LocalDate,
val owner: String,
val userRef: UserRef)