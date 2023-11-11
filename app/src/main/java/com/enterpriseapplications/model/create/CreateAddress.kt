package com.enterpriseapplications.model.create

data class CreateAddress(val countryCode: String,
    val street: String,
    val locality: String,
    val postalCode: String,
    val ownerName: String)