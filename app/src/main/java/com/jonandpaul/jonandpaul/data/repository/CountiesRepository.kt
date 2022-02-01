package com.jonandpaul.jonandpaul.data.repository

import com.jonandpaul.jonandpaul.domain.model.County
import com.jonandpaul.jonandpaul.domain.repository.CountiesApi
import javax.inject.Inject

class CountiesRepository @Inject constructor(
    private val api: CountiesApi
) {
    suspend fun getCounties(): List<County> = api.getCounties()
}