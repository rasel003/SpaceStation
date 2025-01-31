package com.rasel.spacestation.data.source

import com.rasel.spacestation.data.repository.HomeDataSource
import com.rasel.spacestation.remote.api.MyApi
import com.rasel.spacestation.util.apiRequestFlow
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val api: MyApi
) : HomeDataSource {

    override fun getRecommendationList() =  apiRequestFlow { api.getRecommendationList() }
}
