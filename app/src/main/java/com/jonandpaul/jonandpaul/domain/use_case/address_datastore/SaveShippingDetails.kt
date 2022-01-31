package com.jonandpaul.jonandpaul.domain.use_case.address_datastore

import com.jonandpaul.jonandpaul.domain.model.ShippingDetails
import com.jonandpaul.jonandpaul.domain.repository.StoreShippingDetails

class SaveShippingDetails(
    private val repository: StoreShippingDetails
) {
    suspend operator fun invoke(shippingDetails: ShippingDetails) {
        repository.saveShippingDetails(shippingDetails = shippingDetails)
    }
}