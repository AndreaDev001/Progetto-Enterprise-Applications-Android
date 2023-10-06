package com.enterpriseapplications.model

import com.enterpriseapplications.model.refs.UserRef
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class Product(val id: UUID,
val name: String,
val description: String,
val brand: String,
val seller: UserRef,
val condition: String,
val price: BigDecimal,
val minPrice: BigDecimal,
val category: Category,
val createdDate: LocalDate)