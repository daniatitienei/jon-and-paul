package com.jonandpaul.jonandpaul.domain.repository

import com.jonandpaul.jonandpaul.domain.model.County
import retrofit2.http.GET

interface CountiesApi {

    @GET("judete")
    suspend fun getCounties(): List<County>
}