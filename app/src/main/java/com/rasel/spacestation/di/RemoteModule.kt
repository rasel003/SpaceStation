package com.rasel.spacestation.di

import android.content.Context
import com.rasel.spacestation.remote.api.MyApi
import com.rasel.spacestation.remote.api.ServiceFactory
import com.rasel.spacestation.remote.utils.NetworkConnectionInterceptor
import com.rasel.spacestation.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Singleton
    @Provides
    fun provideMyApi(
        networkConnectionInterceptor: NetworkConnectionInterceptor,
    ): MyApi {
        return ServiceFactory.createMyApiService(
            BuildConfig.DEBUG,
            BuildConfig.BASE_URL,
            networkConnectionInterceptor
        )

    }

    @Singleton
    @Provides
    fun provideNetworkConnectionInterceptor(
        @ApplicationContext context: Context,
    ): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(context)
    }
}
