package com.enterpriseapplications.model.create

import java.time.LocalDate
import java.util.UUID

data class CreateBan(val bannedID: UUID,val reason: String,val expirationDate: String)