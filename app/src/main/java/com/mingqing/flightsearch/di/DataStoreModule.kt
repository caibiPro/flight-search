package com.mingqing.flightsearch.di

import android.content.Context
import com.mingqing.flightsearch.data.datastore.UserPreferencesDataStore
import com.mingqing.flightsearch.data.repository.UserPreferencesRepositoryImpl
import com.mingqing.flightsearch.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {

    @Binds
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository

    companion object {
        @Provides
        @Singleton
        fun provideUserPreferencesDataStore(
            @ApplicationContext context: Context
        ): UserPreferencesDataStore {
            return UserPreferencesDataStore(context)
        }
    }
}