package com.jonandpaul.jonandpaul.domain.use_case.counties_api

import com.jonandpaul.jonandpaul.data.repository.CountiesRepository
import com.jonandpaul.jonandpaul.domain.model.County
import com.jonandpaul.jonandpaul.ui.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetCounties @Inject constructor(
    private val repository: CountiesRepository
) {
    operator fun invoke(): Flow<Resource<List<County>>> = flow {
        try {
            emit(Resource.Loading<List<County>>())

            val counties = repository.getCounties()

            emit(Resource.Success<List<County>>(data = counties))

        } catch (e: HttpException) {
            emit(
                Resource.Error<List<County>>(
                    message = e.localizedMessage ?: "An unexpected error has occured."
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error<List<County>>(
                    e.localizedMessage ?: "No internet. Please check your connection"
                )
            )
        }
    }
}