package com.rasel.spacestation.data.source

import com.rasel.spacestation.data.repository.HomeDataSource
import com.rasel.spacestation.data.models.SpaceStationResponseModel
import com.rasel.spacestation.remote.utils.ApiResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeCacheDataSource @Inject constructor() : HomeDataSource {

    override fun getRecommendationList(): Flow<ApiResponse<SpaceStationResponseModel>> {
        TODO("Not yet implemented")
    }
}
