package com.mingqing.flightsearch.data.repository

import com.mingqing.flightsearch.data.database.dao.FavoriteDao
import com.mingqing.flightsearch.data.database.entity.FavoriteEntity
import com.mingqing.flightsearch.domain.model.Favorite
import com.mingqing.flightsearch.domain.repository.FavoriteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    private val allFavorites = favoriteDao.getAllFavorites()
        .shareIn(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    override fun getAllFavorites(): Flow<List<Favorite>> {
        return allFavorites.map { entities ->
            entities.map {
                it.toDomainModel()
            }
        }
    }

    override fun observeFavoriteStatus(
        departureCode: String,
        destinationCode: String
    ): Flow<Boolean> {
        return allFavorites.map { favorites ->
            favorites.any {
                it.departureCode == departureCode &&
                        it.destinationCode == destinationCode
            }
        }
    }

    override suspend fun isFavorite(departureCode: String, destinationCode: String): Boolean {
        return favoriteDao.getFavorite(departureCode, destinationCode) != null
    }

    override suspend fun addFavorite(departureCode: String, destinationCode: String) {
        val existing = favoriteDao.getFavorite(departureCode, destinationCode)
        if (existing == null) {
            val favoriteEntity = FavoriteEntity(
                departureCode = departureCode,
                destinationCode = destinationCode
            )
            favoriteDao.insertFavorite(favoriteEntity)
        }
    }

    override suspend fun removeFavorite(departureCode: String, destinationCode: String) {
        val existing = favoriteDao.getFavorite(departureCode, destinationCode)
        existing?.let { favoriteDao.deleteFavorite(it) }
    }

    override suspend fun toggleFavorite(departureCode: String, destinationCode: String) {
        val existing = favoriteDao.getFavorite(departureCode, destinationCode)
        if (existing != null) {
            favoriteDao.deleteFavorite(existing)
        } else {
            favoriteDao.insertFavorite(
                FavoriteEntity(
                    departureCode = departureCode,
                    destinationCode =  destinationCode
                )
            )
        }
    }

    private fun FavoriteEntity.toDomainModel(): Favorite {
        return Favorite(
            id = id,
            departureCode = departureCode,
            destinationCode = destinationCode
        )
    }
}