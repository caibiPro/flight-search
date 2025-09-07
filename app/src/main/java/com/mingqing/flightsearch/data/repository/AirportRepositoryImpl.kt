package com.mingqing.flightsearch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mingqing.flightsearch.data.database.dao.AirportDao
import com.mingqing.flightsearch.data.database.entity.AirportEntity
import com.mingqing.flightsearch.domain.model.Airport
import com.mingqing.flightsearch.domain.repository.AirportRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AirportRepositoryImpl @Inject constructor(
    private val airportDao: AirportDao
) : AirportRepository {
    override fun searchAirports(query: String): Flow<List<Airport>> {
        return if (query.isBlank()) {
            airportDao.getAllAirports().map { entities ->
                entities.map { it.toDomainModel() }
            }
        } else {
            airportDao.searchAirports(query).map { entities ->
                entities.map { it.toDomainModel() }
            }
        }
    }

    override fun searchAirportsPaged(query: String): Flow<PagingData<Airport>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                airportDao.searchAirportsPaged(query)
            }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                entity.toDomainModel()
            }
        }
    }

    override fun getAirportsExcept(iataCode: String): Flow<List<Airport>> {
        return airportDao.getAllAirports().map { entities ->
            entities.filter {
                it.iataCode != iataCode
            }.map {
                it.toDomainModel()
            }
        }
    }

    override suspend fun getAirportByCode(iataCode: String): Airport? {
        return airportDao.getAirportByCode(iataCode)?.toDomainModel()
    }

    override fun getAirportsPaged(): Flow<PagingData<Airport>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 40
            ),
            pagingSourceFactory = {
                airportDao.getAirportsPaged()
            }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                entity.toDomainModel()
            }
        }
    }

    private fun AirportEntity.toDomainModel(): Airport {
        return Airport(
            id = id,
            iataCode = iataCode,
            name = name,
            passengers = passengers
        )
    }
}