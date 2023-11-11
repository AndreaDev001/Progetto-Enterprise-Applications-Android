package com.enterpriseapplications.model.update

import java.util.UUID

data class UpdateReport(val reportID: UUID,
    val description: String,val reason: String)