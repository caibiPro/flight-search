package com.mingqing.flightsearch.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mingqing.flightsearch.data.database.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    // 获取所有收藏（按ID降序，最新收藏在前）
    @Query("SELECT * FROM favorite ORDER BY id DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    // 检查特定路线是否已收藏
    @Query("""
        SELECT * FROM favorite
        WHERE departure_code = :departureCode
        AND destination_code = :destinationCode
    """)
    suspend fun getFavorite(
        departureCode: String,
        destinationCode: String
    ): FavoriteEntity?

    // 添加收藏
    @Insert
    suspend fun insertFavorite(favorite: FavoriteEntity)

    // 删除收藏
    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)
}