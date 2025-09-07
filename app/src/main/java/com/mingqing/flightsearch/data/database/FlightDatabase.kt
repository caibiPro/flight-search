package com.mingqing.flightsearch.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mingqing.flightsearch.data.database.dao.AirportDao
import com.mingqing.flightsearch.data.database.dao.FavoriteDao
import com.mingqing.flightsearch.data.database.entity.AirportEntity
import com.mingqing.flightsearch.data.database.entity.FavoriteEntity

@Database(
    entities = [
        AirportEntity::class,
        FavoriteEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class FlightDatabase : RoomDatabase() {

    abstract fun airportDao(): AirportDao

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        const val DATABASE_NAME = "flight_database"
    }
}