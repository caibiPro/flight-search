package com.mingqing.flightsearch.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "airport",
    indices = [
        Index(value = ["iata_code"]),
        Index(value = ["passengers"])
    ]
)
data class AirportEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "iata_code")
    val iataCode: String,
    val name: String,
    val passengers: Int
)