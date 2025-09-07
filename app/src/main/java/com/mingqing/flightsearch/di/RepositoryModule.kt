package com.mingqing.flightsearch.di

import com.mingqing.flightsearch.data.repository.AirportRepositoryImpl
import com.mingqing.flightsearch.data.repository.FavoriteRepositoryImpl
import com.mingqing.flightsearch.domain.repository.AirportRepository
import com.mingqing.flightsearch.domain.repository.FavoriteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAirportRepository(
        airportRepositoryImpl: AirportRepositoryImpl
    ): AirportRepository

    @Binds
    abstract fun bindFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ): FavoriteRepository
}