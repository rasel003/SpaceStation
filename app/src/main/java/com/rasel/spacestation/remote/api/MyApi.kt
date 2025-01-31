package com.rasel.spacestation.remote.api

import com.rasel.spacestation.data.models.SpaceStationResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface MyApi {

    @GET("iss-now.json")
    suspend fun getRecommendationList(): Response<SpaceStationResponseModel>

}