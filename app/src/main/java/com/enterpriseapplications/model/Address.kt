package com.enterpriseapplications.model

import java.time.LocalDate
import java.util.UUID

data class Address(val id: UUID,
val countryCode: String,
val street: String,
val locality: String,
val postalCode: String,
val createdDate: LocalDate)