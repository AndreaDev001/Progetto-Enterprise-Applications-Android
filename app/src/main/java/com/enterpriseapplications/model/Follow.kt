package com.enterpriseapplications.model

import com.enterpriseapplications.model.refs.UserRef
import java.time.LocalDate
import java.util.UUID

data class Follow(val id: String,
val follower: UserRef,
val followed: UserRef,
val createdDate: String)