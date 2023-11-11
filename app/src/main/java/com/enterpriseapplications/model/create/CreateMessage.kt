package com.enterpriseapplications.model.create

import java.util.UUID

data class CreateMessage(val conversationID: UUID,val receiverID: UUID, val text: String)