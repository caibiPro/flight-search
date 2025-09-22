package com.mingqing.flightsearch.di

import android.content.Context
import androidx.room.Room
import com.mingqing.flightsearch.data.database.FlightDatabase
import com.mingqing.flightsearch.data.database.FlightDatabaseCallback
import com.mingqing.flightsearch.data.database.dao.AirportDao
import com.mingqing.flightsearch.data.database.dao.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFlightDatabase(
        @ApplicationContext context: Context
    ): FlightDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = FlightDatabase::class.java,
            name = FlightDatabase.DATABASE_NAME
        )
        .addCallback(FlightDatabaseCallback()) // 添加预填充回调
        .fallbackToDestructiveMigration() // 开发阶段：数据库结构变化时重建
        .build()
    }

    @Provides
    fun provideAirportDao(database: FlightDatabase): AirportDao {
        return database.airportDao()
    }

    @Provides
    fun provideFavoriteDao(database: FlightDatabase): FavoriteDao {
        return database.favoriteDao()
    }
}