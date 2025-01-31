package com.rasel.spacestation.data.repository

import com.rasel.spacestation.data.models.SpaceStationResponseModel
import com.rasel.spacestation.remote.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface HomeDataSource {
     fun getRecommendationList(): Flow<ApiResponse<SpaceStationResponseModel>>
}
