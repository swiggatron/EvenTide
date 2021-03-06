package com.jjswigut.eventide.data.repository

import android.content.Context
import com.jjswigut.eventide.data.local.AppDatabase
import com.jjswigut.eventide.data.local.Dao
import com.jjswigut.eventide.data.remote.RemoteDataSource
import com.jjswigut.eventide.data.remote.Service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(service: Service) = RemoteDataSource(service)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideDao(db: AppDatabase) = db.dao()

    @Singleton
    @Provides
    fun provideTideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: Dao
    ) =
        TideRepository(remoteDataSource, localDataSource)

    @Singleton
    @Provides
    fun provideStationRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: Dao
    ) =
        StationRepository(remoteDataSource, localDataSource)
}