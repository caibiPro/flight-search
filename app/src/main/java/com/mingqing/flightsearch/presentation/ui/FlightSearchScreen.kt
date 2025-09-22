package com.mingqing.flightsearch.presentation.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mingqing.flightsearch.domain.model.Airport
import com.mingqing.flightsearch.presentation.theme.FlightSearchTheme
import com.mingqing.flightsearch.presentation.ui.components.AirportList
import com.mingqing.flightsearch.presentation.ui.components.FlightRoute
import com.mingqing.flightsearch.presentation.ui.components.FlightRouteCard
import com.mingqing.flightsearch.presentation.ui.components.SearchBar
import com.mingqing.flightsearch.presentation.viewmodel.DisplayMode
import com.mingqing.flightsearch.presentation.viewmodel.FlightScreenState
import com.mingqing.flightsearch.presentation.viewmodel.FlightSearchEvent
import com.mingqing.flightsearch.presentation.viewmodel.FlightSearchViewModel

@Composable
fun FlightSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: FlightSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val displayMode by viewModel.displayMode.collectAsStateWithLifecycle()

    FlightSearchContent(
        modifier = modifier,
        uiState = uiState,
        displayMode = displayMode,
        onEvent = viewModel::onEvent
    )
}

@Composable
internal fun FlightSearchContent(
    modifier: Modifier = Modifier,
    uiState: FlightScreenState,
    displayMode: DisplayMode,
    onEvent: (FlightSearchEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(uiState.departureAirport) {
        if (uiState.departureAirport != null) {
            keyboardController?.hide()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = { query ->
                onEvent(FlightSearchEvent.QueryChanged(query))
            },
            onSearch = {
                onEvent(FlightSearchEvent.SearchTriggered(uiState.searchQuery))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. 使用 AnimatedContent 包裹内容切换逻辑
        AnimatedContent(
            targetState = displayMode,
            label = "DisplayModeAnimation",
            transitionSpec = {
                // 定义入场和出场动画
                // 新内容从下方轻微滑入并淡入
                val enter = slideInVertically(
                    animationSpec = tween(300),
                    initialOffsetY = { height -> height / 10 }
                ) + fadeIn(animationSpec = tween(300))

                // 旧内容向下方轻微滑出并淡出
                val exit = slideOutVertically(
                    animationSpec = tween(300),
                    targetOffsetY = { height -> height / 10 }
                ) + fadeOut(animationSpec = tween(300))

                // 将入场和出场动画组合起来，创造平滑的过渡效果
                enter.togetherWith(exit)
            }
        ) { mode ->
            // 3. 在 AnimatedContent 的内容 lambda 中，根据模式渲染对应的 Composable
            when (mode) {
                is DisplayMode.Routes -> RoutesContent(
                    departureAirport = uiState.departureAirport,
                    routes = uiState.availableRoutes,
                    isLoading = uiState.isLoadingRoutes,
                    onFavoriteClick = { route ->
                        onEvent(FlightSearchEvent.ToggleFavoriteClicked(route))
                    }
                )
                is DisplayMode.Search -> SearchResultsContent(
                    query = uiState.searchQuery,
                    airports = uiState.searchResults,
                    isLoading = uiState.isSearching,
                    onAirportClick = { airport ->
                        keyboardController?.hide()
                        onEvent(FlightSearchEvent.AirportSelected(airport))
                    }
                )
                is DisplayMode.Favorites -> FavoritesContent(
                    favoriteRoutes = uiState.favoriteRoutes,
                    onFavoriteClick = { route ->
                        onEvent(FlightSearchEvent.ToggleFavoriteClicked(route))
                    }
                )
            }
        }
    }
}

@Composable
fun FavoritesContent(
    favoriteRoutes: List<FlightRoute>,
    onFavoriteClick: (FlightRoute) -> Unit
) {
    if (favoriteRoutes.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "暂无收藏的航线\n搜索机场并选择航线来添加收藏",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        Column {
            Text(
                text = "收藏的航线 (${favoriteRoutes.size})",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(
                    items = favoriteRoutes,
                    key = { route -> "${route.departure.iataCode}-${route.destination.iataCode}" }) { route ->
                    FlightRouteCard(route = route, onFavoriteClick = onFavoriteClick)
                }
            }
        }
    }
}

@Composable
fun SearchResultsContent(
    query: String,
    airports: List<Airport>,
    isLoading: Boolean,
    onAirportClick: (Airport) -> Unit
) {
    Column {
        // 搜索状态头部
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "搜索结果: \"$query\"",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (isLoading) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when {
                        isLoading -> "正在搜索机场..."
                        !isLoading && airports.isEmpty() -> "未找到匹配的机场"
                        else -> "找到 ${airports.size} 个机场"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        if (airports.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            AirportList(
                airports = airports,
                onAirportClick = onAirportClick
            )
        }
    }
}

@Composable
fun RoutesContent(
    departureAirport: Airport?,
    routes: List<FlightRoute>,
    isLoading: Boolean,
    onFavoriteClick: (FlightRoute) -> Unit
) {
    Column {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "从 ${departureAirport?.name ?: ""} (${departureAirport?.iataCode ?: ""}) 出发",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(8.dp))
                if (isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                Text(
                    text = if (isLoading) "正在加载航线..." else "可前往 ${routes.size} 个目的地",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        if (routes.isNotEmpty()) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items = routes, key = { it.destination.id }) { route ->
                    FlightRouteCard(route = route, onFavoriteClick = onFavoriteClick)
                }
            }
        }
    }
}


@Preview(name = "Light Theme - Favorites")
@Preview(name = "Night Theme - Favorites", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FlightSearchScreenFavoritesPreview() {
    val mockRoutes = listOf(
        FlightRoute(
            Airport(1, "SFO", "San Francisco", 0),
            Airport(2, "JFK", "New York", 0),
            true
        )
    )
    FlightSearchTheme {
        Surface {
            FlightSearchContent(
                uiState = FlightScreenState(favoriteRoutes = mockRoutes),
                displayMode = DisplayMode.Favorites,
                onEvent = { }
            )
        }
    }
}

@Preview(name = "Light Theme - Search Results")
@Preview(name = "Night Theme - Search Results", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FlightSearchScreenSearchResultsPreview() {
    val mockAirports = listOf(Airport(1, "SFO", "San Francisco", 0))
    FlightSearchTheme {
        Surface {
            FlightSearchContent(
                uiState = FlightScreenState(searchQuery = "San", searchResults = mockAirports),
                displayMode = DisplayMode.Search,
                onEvent = { }
            )
        }
    }
}