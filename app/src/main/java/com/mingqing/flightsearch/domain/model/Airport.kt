package com.mingqing.flightsearch.domain.model

/**
 * Domain model for Airport
 *
 * This represents the business concept of an airport in our domain.
 * It's independent of any database or UI framework.
 */
data class Airport(
    val id: Int,
    val iataCode: String,
    val name: String,
    val passengers: Int
)