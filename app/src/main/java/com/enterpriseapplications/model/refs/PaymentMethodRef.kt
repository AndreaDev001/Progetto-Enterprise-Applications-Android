package com.enterpriseapplications.model.refs

import java.util.UUID

data class PaymentMethodRef(val id: UUID, val brand: String, val holderName: String)