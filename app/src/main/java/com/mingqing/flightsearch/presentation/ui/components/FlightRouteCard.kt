package com.mingqing.flightsearch.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mingqing.flightsearch.domain.model.Airport
import com.mingqing.flightsearch.presentation.theme.FlightSearchTheme

data class FlightRoute(
    val departure: Airport,
    val destination: Airport,
    val isFavorite: Boolean = false
)

@Composable
fun FlightRouteCard(
    route: FlightRoute,
    onFavoriteClick: (FlightRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 收藏按钮
            FavoriteButton(
                isFavorite = route.isFavorite,
                onClick = { onFavoriteClick(route) }
            )

            // 对称的间距
            val spacing = 16.dp
            Spacer(modifier = Modifier.width(spacing))

            // 航线信息
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AirportInfo(
                    airport = route.departure,
                    label = "DEPART",
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.Flight,
                    contentDescription = "Flight",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                AirportInfo(
                    airport = route.destination,
                    label = "ARRIVE",
                    modifier = Modifier.weight(1f)
                )
            }

            // 右侧对称间距
            Spacer(modifier = Modifier.width(spacing))
        }
    }
}

@Composable
private fun AirportInfo(
    airport: Airport,
    label: String,
    modifier: Modifier = Modifier
) {
    val isArrival = label == "ARRIVE"

    Column(
        modifier = modifier,
        horizontalAlignment = if (isArrival) Alignment.End else
            Alignment.Start
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = if (isArrival) TextAlign.End else TextAlign.Start
        )

        Text(
            text = airport.iataCode,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = if (isArrival) TextAlign.End else TextAlign.Start
        )

        Text(
            text = airport.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            textAlign = if (isArrival) TextAlign.End else TextAlign.Start,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(name = "Light Theme - Flight Route")
@Preview(name = "Dark Theme - Flight Route", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FlightRouteCardPreview() {
    FlightSearchTheme {
        Surface {
            FlightRouteCard(
                route = FlightRoute(
                    departure = Airport(
                        id = 1,
                        iataCode = "LAX",
                        name = "Los Angeles International",
                        passengers = 84557968
                    ),
                    destination = Airport(
                        id = 2,
                        iataCode = "JFK",
                        name = "John F. Kennedy International",
                        passengers = 62551253
                    ),
                    isFavorite = false
                ),
                onFavoriteClick = { }
            )
        }
    }
}

@Preview(name = "Light Theme - Favorite Route")
@Preview(name = "Dark Theme - Favorite Route", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FlightRouteCardFavoritePreview() {
    FlightSearchTheme {
        Surface {
            FlightRouteCard(
                route = FlightRoute(
                    departure = Airport(
                        id = 1,
                        iataCode = "LAX",
                        name = "Los Angeles International",
                        passengers = 84557968
                    ),
                    destination = Airport(
                        id = 2,
                        iataCode = "JFK",
                        name = "John F. Kennedy International",
                        passengers = 62551253
                    ),
                    isFavorite = true
                ),
                onFavoriteClick = { }
            )
        }
    }
}