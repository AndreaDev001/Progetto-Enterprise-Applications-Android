package com.enterpriseapplications.model

import com.enterpriseapplications.model.refs.ConversationRef
import com.enterpriseapplications.model.refs.UserRef
import java.time.LocalDateTime
import java.util.UUID

data class Message(val id: String,
val sender: UserRef,
val receiver: UserRef,
val conversation: ConversationRef,
val text: String,
val createdDate: String)