package com.enterpriseapplications.model

import com.enterpriseapplications.model.refs.ProductRef
import com.enterpriseapplications.model.refs.UserRef
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class Order(val id: String,
val buyer: UserRef,
val seller: UserRef,
val price: BigDecimal,
val product: ProductRef,
val createdDate: String)