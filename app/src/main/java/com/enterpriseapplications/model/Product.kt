package com.enterpriseapplications.model

import com.enterpriseapplications.model.refs.UserRef
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class Product(val id: String,
val name: String,
val description: String,
val brand: String? = null,
val seller: UserRef,
val condition: String? = null,
val price: BigDecimal,
val amountOfLikes: Number,
val minPrice: BigDecimal? = null,
val category: Category? = null,
val createdDate: String? = null)