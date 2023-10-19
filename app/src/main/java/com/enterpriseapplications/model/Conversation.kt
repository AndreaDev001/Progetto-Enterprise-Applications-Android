package com.enterpriseapplications.model

import com.enterpriseapplications.model.refs.UserRef
import java.util.UUID

data class Conversation(val id: String,
val first: UserRef,
val second: UserRef,
val createdDate: String)