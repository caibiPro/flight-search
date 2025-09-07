package com.mingqing.flightsearch.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    fun getSearchQuery(): Flow<String>

    suspend fun saveSearchQuery(query: String)
}