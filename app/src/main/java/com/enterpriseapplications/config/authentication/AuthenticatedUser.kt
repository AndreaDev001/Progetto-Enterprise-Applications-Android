package com.enterpriseapplications.config.authentication

import java.util.UUID

data class AuthenticatedUser(val userID: UUID,
val email: String,val username: String,
val name: String,val surname: String
,val tokenExpiration: Long)
