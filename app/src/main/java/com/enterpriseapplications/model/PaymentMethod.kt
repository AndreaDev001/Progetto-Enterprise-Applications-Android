package com.enterpriseapplications.model

import com.enterpriseapplications.model.refs.UserRef
import java.time.LocalDate
import java.util.UUID

data class PaymentMethod(val id: UUID,
val user: UserRef,
val holderName: String,
val number: String,
val brand: String,
val createdDate: LocalDate,
val expirationDate: LocalDate)