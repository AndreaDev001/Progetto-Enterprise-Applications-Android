package com.enterpriseapplications.config.authentication

import java.util.UUID

data class AuthenticatedUser(val userID: UUID,val email: String,val username: String,val tokenExpiration: Long,val roles: List<String>)
