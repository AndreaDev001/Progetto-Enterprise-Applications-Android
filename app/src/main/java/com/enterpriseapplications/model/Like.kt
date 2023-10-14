package com.enterpriseapplications.model

import com.enterpriseapplications.model.refs.ProductRef
import com.enterpriseapplications.model.refs.UserRef

data class Like(val id: String,
val user: UserRef,
val product: ProductRef,
val createdDate: String)