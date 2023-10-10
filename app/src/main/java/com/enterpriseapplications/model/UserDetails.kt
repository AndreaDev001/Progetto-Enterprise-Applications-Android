package com.enterpriseapplications.model

import java.time.LocalDate
import java.util.UUID

data class UserDetails(val id: String,
val email: String,
val username: String,
val name: String,
val surname: String,
val description: String,
val rating: Number,
val gender: String,
val visibility: String,
val amountOfFollowers: Int,
val amountOfFollowed: Int,
val amountOfProducts: Int,
val amountOfWrittenReviews: Int,
val amountOfReceivedReviews: Int,
val amountOfReplies: Int,
val createdDate: String)