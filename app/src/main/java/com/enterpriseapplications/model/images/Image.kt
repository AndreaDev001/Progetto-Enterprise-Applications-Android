package com.enterpriseapplications.model.images

import java.time.LocalDate
import java.util.UUID

data class Image(val id: String,
val name: String,
val type: String,
val createdDate: String,
val owner: String)