package com.enterpriseapplications.model.images

import com.enterpriseapplications.model.refs.ProductRef
import java.time.LocalDate
import java.util.UUID

data class ProductImage(val id: String,
val name: String,
val type: String,
val createdDate: String,
val owner: String,
val productRef: ProductRef)