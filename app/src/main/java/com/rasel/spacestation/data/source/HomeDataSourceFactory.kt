package com.rasel.spacestation.data.source

import com.rasel.spacestation.data.repository.HomeDataSource
import javax.inject.Inject

open class HomeDataSourceFactory @Inject constructor(
    private val cacheDataSource: HomeCacheDataSource,
    private val remoteDataSource: HomeRemoteDataSource
) {

   /* open suspend fun getDataStore(isCached: Boolean): HomeDataSource {
        return if (isCached && !characterCache.isExpired()) {
            return getCacheDataSource()
        } else {
            getRemoteDataSource()
        }
    }*/

    fun getRemoteDataSource(): HomeDataSource {
        return remoteDataSource
    }

    fun getCacheDataSource(): HomeDataSource {
        return cacheDataSource
    }
}
