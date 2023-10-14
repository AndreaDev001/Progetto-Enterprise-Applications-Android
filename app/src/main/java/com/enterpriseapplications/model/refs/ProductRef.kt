package com.enterpriseapplications.model.refs

import java.math.BigDecimal
import java.util.UUID

data class ProductRef(val id: String,
val name: String,val description: String,val likes: Int,val seller: UserRef,val price: BigDecimal)