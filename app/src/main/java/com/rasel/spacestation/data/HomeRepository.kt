package com.rasel.spacestation.data

import com.rasel.spacestation.data.source.HomeDataSourceFactory
import com.rasel.spacestation.data.models.SpaceStationResponseModel
import com.rasel.spacestation.remote.utils.ApiResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class HomeRepository @Inject constructor(
    private val dataSourceFactory: HomeDataSourceFactory
) {
     fun getRecommendationList(): Flow<ApiResponse<SpaceStationResponseModel>> =
        dataSourceFactory.getRemoteDataSource().getRecommendationList()
}