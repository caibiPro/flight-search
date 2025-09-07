package com.mingqing.flightsearch.domain.model

/**
 * Domain model for Favorite route
 *
 * Represents a user's favorite flight route from departure to destination.
 */
data class Favorite(
    val id: Int,
    val departureCode: String,
    val destinationCode: String
)