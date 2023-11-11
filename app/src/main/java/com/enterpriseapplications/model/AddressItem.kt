package com.enterpriseapplications.model

data class AddressItem(
    val street: String,
    val locality: String,
    val administrative_area: String,
    val postal_code: String,
    val country_iso3: String
)
data class AddressSearchResponse(
    val candidates: List<AddressItem>
)