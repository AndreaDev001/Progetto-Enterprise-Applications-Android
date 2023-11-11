package com.enterpriseapplications.model

import com.enterpriseapplications.model.refs.ProductRef
import com.enterpriseapplications.model.refs.UserRef
import java.time.LocalDate
import java.util.UUID

data class Like(val id: UUID,
val user: UserRef,
val product: ProductRef,
val createdDate: LocalDate)