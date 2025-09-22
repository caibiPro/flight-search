package com.mingqing.flightsearch.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mingqing.flightsearch.domain.model.Airport
import com.mingqing.flightsearch.presentation.theme.FlightSearchTheme

@Composable
fun AirportList(
    airports: List<Airport>,
    onAirportClick: (Airport) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = airports,
            key = { airport -> airport.id }
        ) { airport ->
            AirportItem(
                airport = airport,
                onClick = { onAirportClick(airport) }
            )
        }
    }
}

@Composable
fun AirportItem(
    airport: Airport,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.run {
            cardElevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // IATA Code Badge
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Text(
                    text = airport.iataCode,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            // Airport Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = airport.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${airport.passengers} passengers",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}


@Preview(name = "Light Theme")
@Preview(name = "Dark Theme", uiMode =
android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AirportListPreview() {
    FlightSearchTheme {
        Surface {
            AirportList(
                airports = listOf(
                    Airport(
                        id = 1,
                        iataCode = "LAX",
                        name = "Los Angeles International Airport",
                        passengers = 84557968
                    ),
                    Airport(
                        id = 2,
                        iataCode = "JFK",
                        name = "John F. Kennedy International Airport",
                        passengers = 62551253
                    ),
                    Airport(
                        id = 3,
                        iataCode = "CDG",
                        name = "Charles de Gaulle Airport",
                        passengers = 76150009
                    )
                ),
                onAirportClick = { }
            )
        }
    }
}

@Preview(name = "Empty List")
@Composable
private fun AirportListEmptyPreview() {
    FlightSearchTheme {
        Surface {
            AirportList(
                airports = emptyList(),
                onAirportClick = { }
            )
        }
    }
}
