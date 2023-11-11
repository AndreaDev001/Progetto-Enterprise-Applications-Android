package com.enterpriseapplications.model.create

import java.math.BigDecimal

data class CreateProduct(val name: String,val description: String,
val price: BigDecimal,val minPrice: BigDecimal,val brand: String,
val condition: String,val visibility: String,val primaryCat: String,
val secondaryCat: String,val tertiaryCat: String)