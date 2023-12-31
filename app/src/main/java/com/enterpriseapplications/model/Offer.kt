package com.enterpriseapplications.model

import com.enterpriseapplications.model.refs.ProductRef
import com.enterpriseapplications.model.refs.UserRef
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class Offer(val id: UUID,
val buyer: UserRef,
val product: ProductRef,
val description: String,
val price: BigDecimal,
val status: String,
val createdDate: LocalDate,
val expirationDate: LocalDate,
val expired: Boolean)