package com.enterpriseapplications.model.update

import java.math.BigDecimal
import java.util.UUID

data class UpdateOfferBuyer(val offerID: UUID,val description: String,val price: BigDecimal)