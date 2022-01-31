package com.jonandpaul.jonandpaul.domain.use_case.address_datastore

import com.jonandpaul.jonandpaul.domain.repository.StoreShippingDetails

class GetShippingDetails(
    private val repository: StoreShippingDetails
) {
    operator fun invoke() =
        repository.getShippingDetails()
}