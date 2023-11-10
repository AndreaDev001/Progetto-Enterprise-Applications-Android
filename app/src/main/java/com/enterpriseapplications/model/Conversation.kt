package com.enterpriseapplications.model

import com.enterpriseapplications.model.refs.ProductRef
import com.enterpriseapplications.model.refs.UserRef
import java.time.LocalDate
import java.util.UUID

data class Conversation(val id: UUID,
val starter: UserRef,
val product: ProductRef,
val createdDate: LocalDate)