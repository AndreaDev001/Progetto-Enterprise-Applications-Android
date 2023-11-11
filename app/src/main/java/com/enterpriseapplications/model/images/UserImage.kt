package com.enterpriseapplications.model.images

import com.enterpriseapplications.model.refs.UserRef
import java.time.LocalDate
import java.util.UUID

data class UserImage(val id: String,
val name: String,
val type: String,
val createdDate: String,
val owner: String,
val userRef: UserRef)