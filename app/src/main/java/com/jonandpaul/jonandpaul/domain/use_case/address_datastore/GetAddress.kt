package com.jonandpaul.jonandpaul.domain.use_case.address_datastore

import com.jonandpaul.jonandpaul.domain.repository.StoreAddress

class GetAddress(
    private val repository: StoreAddress
) {
    operator fun invoke() =
        repository.getAddress()
}