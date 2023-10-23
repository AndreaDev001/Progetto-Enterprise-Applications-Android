package com.enterpriseapplications.model.create

import java.math.BigDecimal
import java.util.UUID

data class CreateOffer(val price: BigDecimal,
val description: String,val productID: UUID)