package com.enterpriseapplications.model.update

import java.util.UUID

data class UpdateReview(val reviewID: UUID,
                        val text: String, val rating: Int)