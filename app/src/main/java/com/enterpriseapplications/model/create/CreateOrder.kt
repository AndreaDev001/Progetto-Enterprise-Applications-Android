package com.enterpriseapplications.model.create

import java.math.BigDecimal
import java.util.UUID

data class CreateOrder(val productID: UUID,val price: BigDecimal,val addressID: UUID,val paymentMethodID: UUID)