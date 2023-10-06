package com.enterpriseapplications.model

import java.time.LocalDate
import java.util.UUID

data class UserDetails(val id: UUID,
val email: String,
val username: String,
val name: String,
val surname: String,
val gender: String,
val rating: Number,
val description: String,
val visibility: String,
val amountOfFollowers: Int,
val amountOfFollowed: Int,
val amountOfProducts: Int,
val amountOfWrittenReviews: Int,
val amountOfReceivedReviews: Int,
val amountOfReplies: Int,
val createdDate: LocalDate)