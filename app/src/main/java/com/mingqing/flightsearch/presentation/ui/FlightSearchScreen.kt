package com.mingqing.flightsearch.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mingqing.flightsearch.domain.model.Airport
import com.mingqing.flightsearch.domain.model.Favorite
import com.mingqing.flightsearch.presentation.theme.FlightSearchTheme
import com.mingqing.flightsearch.presentation.ui.components.SearchBar
import com.mingqing.flightsearch.presentation.viewmodel.FlightSearchEvent
import com.mingqing.flightsearch.presentation.viewmodel.FlightSearchUiState
import com.mingqing.flightsearch.presentation.viewmodel.FlightSearchViewModel

@Composable
fun FlightSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: FlightSearchViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    FlightSearchContent(
        modifier = modifier,
        uiState = uiState,
        searchQuery = searchQuery,
        onEvent = viewModel::onEvent
    )
}

@Composable
internal fun FlightSearchContent(
    modifier: Modifier = Modifier,
    uiState: FlightSearchUiState,
    searchQuery: String,
    onEvent: (FlightSearchEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { query ->
                onEvent(FlightSearchEvent.QueryChanged(query))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is FlightSearchUiState.Loading -> {
                LoadingContent()
            }

            is FlightSearchUiState.Favorites -> {
                FavoritesContent(
                    favorites = uiState.favorites
                )
            }

            is FlightSearchUiState.SearchResults -> {
                SearchResultsContent(
                    query = uiState.query,
                    airports = uiState.airports,
                    isLoading = uiState.isLoading
                )
            }

            is FlightSearchUiState.Routes -> {
                RoutesContent(
                    departureAirport = uiState.departureAirport,
                    destinationAirports = uiState.destinationAirports,
                    isLoading = uiState.isLoading
                )
            }

            is FlightSearchUiState.Error -> {
                ErrorContent(message = uiState.message)
            }
        }
    }
}

@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CircularProgressIndicator()
            Text(
                text = "加载中...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun FavoritesContent(favorites: List<Favorite>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "收藏列表: ${favorites.size} 项",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SearchResultsContent(
    query: String,
    airports: List<Airport>,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 标题行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "搜索结果: $query",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(1f)
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 进度条
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = if (isLoading) "正在搜索机场..." else "${airports.size} 个机场",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun RoutesContent(
    departureAirport: Airport,
    destinationAirports: List<Airport>,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 标题行与加载指示器
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "从 ${departureAirport.name}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.weight(1f)
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        strokeWidth = 2.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = when {
                    isLoading -> "正在加载航线..."
                    destinationAirports.isEmpty() -> "暂无可用航线"
                    else -> "可前往 ${destinationAirports.size} 个目的地"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun ErrorContent(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Text(
            text = "错误: $message",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Preview(name = "Light Theme - Loading")
@Preview(name = "Dark Theme - Loading", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FlightSearchScreenLoadingPreview() {
    FlightSearchTheme {
        Surface {
            FlightSearchContent(
                uiState = FlightSearchUiState.Loading,
                searchQuery = "",
                onEvent = { }
            )
        }
    }
}

@Preview(name = "Light Theme - Favorites")
@Preview(name = "Dark Theme - Favorites", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FlightSearchScreenFavoritesPreview() {
    val mockFavorites = listOf(
        Favorite(id = 1, departureCode = "LAX", destinationCode = "JFK"),
        Favorite(id = 2, departureCode = "SFO", destinationCode = "ORD")
    )
    FlightSearchTheme {
        Surface {
            FlightSearchContent(
                uiState = FlightSearchUiState.Favorites(mockFavorites),
                searchQuery = "",
                onEvent = { }
            )
        }
    }
}


@Preview(name = "Light Theme - Search Results")
@Preview(
    name = "Dark Theme - Search Results", uiMode =
    Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun FlightSearchScreenSearchResultsPreview() {
    val mockAirports = listOf(
        Airport(
            id = 1,
            iataCode = "LAX",
            name = "Los Angeles International",
            passengers = 87500000
        ),
        Airport(
            id = 2,
            iataCode = "SFO",
            name = "San Francisco International",
            passengers = 57800000
        )
    )

    FlightSearchTheme {
        Surface {
            FlightSearchContent(
                uiState = FlightSearchUiState.SearchResults(
                    query = "LA",
                    airports = mockAirports,
                    isLoading = false
                ),
                searchQuery = "LA",
                onEvent = { }
            )
        }
    }
}

@Preview(name = "Light Theme - Routes")
@Preview(name = "Dark Theme - Routes", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FlightSearchScreenRoutesPreview() {
    val departureAirport = Airport(
        id = 1,
        iataCode = "LAX",
        name = "Los Angeles International",
        passengers = 87500000
    )
    val destinationAirports = listOf(
        Airport(
            id = 2, iataCode = "JFK", name = "John F. Kennedy International", passengers =
            62500000
        ),
        Airport(id = 3, iataCode = "ORD", name = "O'Hare International", passengers = 84300000)
    )

    FlightSearchTheme {
        Surface {
            FlightSearchContent(
                uiState = FlightSearchUiState.Routes(
                    departureAirport = departureAirport,
                    destinationAirports = destinationAirports,
                    isLoading = false
                ),
                searchQuery = "",
                onEvent = { }
            )
        }
    }
}

@Preview(name = "Light Theme - Error")
@Preview(name = "Dark Theme - Error", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FlightSearchScreenErrorPreview() {
    FlightSearchTheme {
        Surface {
            FlightSearchContent(
                uiState = FlightSearchUiState.Error("网络连接失败，请检查网络设置后重试"),
                searchQuery = "",
                onEvent = { }
            )
        }
    }
}

@Preview(name = "Light Theme - Search with Loading")
@Preview(name = "Dark Theme - Search with Loading", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FlightSearchScreenSearchLoadingPreview() {
    FlightSearchTheme {
        Surface {
            FlightSearchContent(
                uiState = FlightSearchUiState.SearchResults(
                    query = "JFK",
                    airports = emptyList(),
                    isLoading = true  // 显示加载中的搜索状态
                ),
                searchQuery = "JFK",
                onEvent = { }
            )
        }
    }
}