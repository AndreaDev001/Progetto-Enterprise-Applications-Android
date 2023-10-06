package com.enterpriseapplications.model.images

import java.time.LocalDate
import java.util.UUID

data class Image(val id: UUID,
val name: String,
val type: String,
val createdDate: LocalDate,
val owner: String)