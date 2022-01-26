package com.jonandpaul.jonandpaul.domain.use_case.address_datastore

import com.jonandpaul.jonandpaul.domain.repository.StoreAddress

class SaveAddress(
    private val repository: StoreAddress
) {
    suspend operator fun invoke(newAddress: String) {
        repository.saveAddress(newAddress = newAddress)
    }
}