package com.enterpriseapplications.model.refs

import java.math.BigDecimal
import java.util.UUID

data class ProductRef(val id: UUID,
val name: String,val brand: String,val price: BigDecimal)