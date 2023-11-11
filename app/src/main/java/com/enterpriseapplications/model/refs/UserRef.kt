package com.enterpriseapplications.model.refs

import java.util.UUID

data class UserRef(val id: UUID,
   val username: String,
val name: String? = null,
val surname: String? = null,
val gender: String? = null,
val rating: Number? = null)