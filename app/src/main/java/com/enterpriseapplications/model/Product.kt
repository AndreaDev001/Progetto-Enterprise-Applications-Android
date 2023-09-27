package com.enterpriseapplications.model

data class Product(val name: String,
val description: String,
val brand: String,
val condition: String,
val price: Number,
val minPrice: Number,
val likes: Number,
val primaryCategory: String,
val secondaryCategory: String,
val tertiaryCategory: String,
val seller: User)
