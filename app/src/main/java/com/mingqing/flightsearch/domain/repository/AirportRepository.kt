package com.mingqing.flightsearch.domain.repository

import androidx.paging.PagingData
import com.mingqing.flightsearch.domain.model.Airport
import kotlinx.coroutines.flow.Flow

interface AirportRepository {
    /**
     * Search airports by IATA code or name
     * Returns Flow for reactive updates
     */
    fun searchAirports(query: String): Flow<List<Airport>>

    fun searchAirportsPaged(query: String): Flow<PagingData<Airport>>

    /**
     * Get all airports except the given one
     * Used to show possible destinations from a departure airport
     */
    fun getAirportsExcept(iataCode: String): Flow<List<Airport>>

    /**
     * Get airport by IATA code
     */
    suspend fun getAirportByCode(iataCode: String): Airport?

    fun getAirportsPaged(): Flow<PagingData<Airport>>
}