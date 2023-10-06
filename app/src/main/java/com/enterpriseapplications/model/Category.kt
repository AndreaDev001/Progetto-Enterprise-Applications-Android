package com.enterpriseapplications.model

import java.util.UUID

data class Category(val id: UUID,
val primary: String,
val secondary: String,
val tertiary: String)