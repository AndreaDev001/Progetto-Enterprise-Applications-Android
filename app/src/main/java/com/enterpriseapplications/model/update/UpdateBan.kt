package com.enterpriseapplications.model.update

import java.util.UUID

data class UpdateBan(val bannedID: UUID,
val description: String,val reason: String)