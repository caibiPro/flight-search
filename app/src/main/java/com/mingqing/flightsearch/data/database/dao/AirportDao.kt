package com.mingqing.flightsearch.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.mingqing.flightsearch.data.database.entity.AirportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {

    // 按IATA代码搜索（精确匹配）
    @Query("SELECT * FROM airport WHERE iata_code = :iataCode")
    suspend fun getAirportByCode(iataCode: String): AirportEntity?

    // 按 IATA代码或名称模糊搜索
    @Query("""
        SELECT * FROM airport
        WHERE iata_code LIKE '%' || :query || '%'
        OR name LIKE '%' || :query || '%'
        ORDER BY passengers DESC
        LIMIT 20
    """
    )
    fun searchAirports(query: String): Flow<List<AirportEntity>>

    @Query("""
        SELECT * FROM airport
        WHERE iata_code LIKE '%' || :query || '%'
        OR name LIKE '%' || :query || '%'
        ORDER BY passengers DESC
    """)
    fun searchAirportsPaged(query: String): PagingSource<Int, AirportEntity>

    // 获取所有机场（按流量排序）
    @Query("SELECT * FROM airport ORDER BY passengers DESC")
    fun getAllAirports(): Flow<List<AirportEntity>>

    @Query("SELECT * FROM airport ORDER BY passengers DESC")
    fun getAirportsPaged(): PagingSource<Int, AirportEntity>
}