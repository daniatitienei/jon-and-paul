package com.jonandpaul.jonandpaul.data.repository

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jonandpaul.jonandpaul.JonAndPaulApplication
import com.jonandpaul.jonandpaul.domain.repository.StoreAddress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreAddressImpl @Inject constructor(
    private val context: Application
) : StoreAddress {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "address")
        private val ADDRESS_KEY = stringPreferencesKey("address_key")
    }

    override fun getAddress(): Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[ADDRESS_KEY] ?: ""
        }

    override suspend fun saveAddress(newAddress: String) {
        context.dataStore.edit { settings ->
            settings[ADDRESS_KEY] = newAddress
        }
    }
}