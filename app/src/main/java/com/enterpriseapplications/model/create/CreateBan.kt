package com.enterpriseapplications.model.create

import java.time.LocalDate
import java.util.UUID

data class CreateBan(val bannedID: UUID,val description: String,val reason: String,val expirationDate: String)