package com.mingqing.flightsearch.data.repository

import com.mingqing.flightsearch.data.datastore.UserPreferencesDataStore
import com.mingqing.flightsearch.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore
): UserPreferencesRepository {
    override fun getSearchQuery(): Flow<String> {
        return userPreferencesDataStore.searchQueryFlow
    }

    override suspend fun saveSearchQuery(query: String) {
        userPreferencesDataStore.saveSearchQuery(query)
    }
}