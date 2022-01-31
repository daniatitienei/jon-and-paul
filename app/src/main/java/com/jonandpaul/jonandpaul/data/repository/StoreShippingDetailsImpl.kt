package com.jonandpaul.jonandpaul.data.repository

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jonandpaul.jonandpaul.domain.model.ShippingDetails
import com.jonandpaul.jonandpaul.domain.repository.StoreShippingDetails
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreShippingDetailsImpl @Inject constructor(
    private val context: Context,
    private val moshi: Moshi
) : StoreShippingDetails {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "address")
        private val ADDRESS_KEY = stringPreferencesKey("address_key")
    }

    private val jsonAdapter = moshi.adapter(ShippingDetails::class.java).lenient()

    override fun getShippingDetails(): Flow<ShippingDetails?> = context.dataStore.data
        .map { preferences ->
            if (!preferences[ADDRESS_KEY].isNullOrBlank())
                jsonAdapter.fromJson(preferences[ADDRESS_KEY]!!)
            else ShippingDetails()
        }

    override suspend fun saveShippingDetails(shippingDetails: ShippingDetails) {
        context.dataStore.edit { settings ->
            val shippingDetailsJson = jsonAdapter.toJson(shippingDetails)
            settings[ADDRESS_KEY] = shippingDetailsJson
        }
    }
}