package com.enterpriseapplications.model

import java.time.LocalDate
import java.util.UUID

data class UserDetails(val id: String,
val email: String? = null,
val username: String,
val name: String? = null,
val surname: String? = null,
val description: String? = null,
val rating: Number? = null,
val gender: String? = null,
val visibility: String? = null,
val amountOfFollowers: Int? = null,
val amountOfFollowed: Int? = null,
val amountOfProducts: Int? = null,
val amountOfWrittenReviews: Int? = null,
val amountOfReceivedReviews: Int? = null,
val amountOfReplies: Int? = null,
val createdDate: String? = null)