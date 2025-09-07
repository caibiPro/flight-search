package com.mingqing.flightsearch.domain.repository

import com.mingqing.flightsearch.domain.model.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    fun getAllFavorites(): Flow<List<Favorite>>

    fun observeFavoriteStatus(departureCode: String, destinationCode: String): Flow<Boolean>

    suspend fun isFavorite(departureCode: String, destinationCode: String): Boolean

    suspend fun addFavorite(departureCode: String, destinationCode: String)

    suspend fun removeFavorite(departureCode: String, destinationCode: String)

    suspend fun toggleFavorite(departureCode: String, destinationCode: String)
}