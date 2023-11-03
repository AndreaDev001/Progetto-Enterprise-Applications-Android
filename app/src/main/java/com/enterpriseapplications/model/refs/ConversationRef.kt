package com.enterpriseapplications.model.refs

data class ConversationRef(val id: String,
val starter: UserRef,
val product: ProductRef,
val createdDate: String)