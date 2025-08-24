package com.mingqing.flightsearch.presentation.ui.test.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mingqing.flightsearch.presentation.theme.FlightSearchTheme

/**
 * Flight route card component
 * Displays flight route information with favorite button
 */
@Composable
fun FlightRouteCard(
    route: String,
    description: String,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = { }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = route,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onFavoriteClick
            ) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * Flight route card testing section
 * Tests multiple route cards with different data
 */
@Composable
fun FlightRouteTestSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Flight Route Card Test",
            style = MaterialTheme.typography.titleMedium
        )

        val routes = listOf(
            "LAX → JFK" to "Los Angeles → New York",
            "PEK → SHA" to "Beijing → Shanghai",
            "LHR → CDG" to "London → Paris"
        )

        routes.forEach { (route, description) ->
            FlightRouteCard(
                route = route,
                description = description,
                onFavoriteClick = { }
            )
        }
    }
}

// Individual flight route card preview
@Preview(
    name = "Flight Route Card - Light",
    showBackground = true
)
@Composable
fun FlightRouteCardPreview() {
    FlightSearchTheme {
        FlightRouteCard(
            route = "LAX → JFK",
            description = "Los Angeles → New York",
            onFavoriteClick = { },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(
    name = "Flight Route Card - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun FlightRouteCardDarkPreview() {
    FlightSearchTheme {
        FlightRouteCard(
            route = "PEK → SHA",
            description = "Beijing → Shanghai",
            onFavoriteClick = { },
            modifier = Modifier.padding(16.dp)
        )
    }
}

// Flight route section preview
@Preview(
    name = "Flight Route Section",
    showBackground = true
)
@Composable
fun FlightRouteSectionPreview() {
    FlightSearchTheme {
        FlightRouteTestSection(
            modifier = Modifier.padding(16.dp)
        )
    }
}