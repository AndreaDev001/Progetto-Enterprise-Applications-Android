package com.enterpriseapplications.model.create

data class CreatePaymentMethod(val holderName: String,
val number: String,
val brand: String,
val expirationDate: String)