package com.enterpriseapplications.model.refs

import java.time.LocalDate
import java.util.UUID

data class ConversationRef(val id: UUID,
val starter: UserRef,
val product: ProductRef,
val createdDate: LocalDate)