package com.enterpriseapplications.model.create

import java.util.UUID

data class CreateReview(val reviewedID: UUID,val text: String,val rating: Int)