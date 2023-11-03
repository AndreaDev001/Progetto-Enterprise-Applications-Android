package com.enterpriseapplications.model.update

import java.util.UUID

data class UpdateOfferSeller(val offerID: UUID,val productID: UUID,val offerStatus: String)