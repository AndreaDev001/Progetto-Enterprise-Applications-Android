package com.enterpriseapplications.model.create

import java.util.UUID

data class CreateMessageDto(val receiverID: UUID,val text: String)