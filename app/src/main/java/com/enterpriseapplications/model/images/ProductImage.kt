package com.enterpriseapplications.model.images

import com.enterpriseapplications.model.refs.ProductRef
import java.time.LocalDate
import java.util.UUID

data class ProductImage(val id: UUID,
val name: String,
val type: String,
val createdDate: LocalDate,
val owner: String,
val productRef: ProductRef)