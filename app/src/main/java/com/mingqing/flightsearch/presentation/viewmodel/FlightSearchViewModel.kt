package com.mingqing.flightsearch.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mingqing.flightsearch.domain.model.Airport
import com.mingqing.flightsearch.domain.repository.AirportRepository
import com.mingqing.flightsearch.domain.repository.FavoriteRepository
import com.mingqing.flightsearch.domain.repository.UserPreferencesRepository
import com.mingqing.flightsearch.presentation.ui.components.FlightRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// 新增：定义显式的UI模式
sealed interface DisplayMode {
    data object Favorites : DisplayMode
    data object Search : DisplayMode
    data class Routes(val departureAirport: Airport) : DisplayMode
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class FlightSearchViewModel @Inject constructor(
    private val airportRepository: AirportRepository,
    private val favoriteRepository: FavoriteRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    companion object {
        private const val TAG = "FlightSearchViewModel"
        private const val SEARCH_DEBOUNCE_MS = 300L
    }

    // 用户输入的原始查询流
    private val _searchQuery = userPreferencesRepository.getSearchQuery()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), "")

    // 用户选择的出发机场
    private val _selectedAirport = MutableStateFlow<Airport?>(null)

    // 收藏航线流
    private val _favoriteRoutes: StateFlow<List<FlightRoute>> = favoriteRepository.getAllFavorites()
        .transform { favorites ->
            try {
                val flightRoutes = favorites.mapNotNull { fav ->
                    val dep = airportRepository.getAirportByCode(fav.departureCode)
                    val dest = airportRepository.getAirportByCode(fav.destinationCode)
                    if (dep != null && dest != null) FlightRoute(dep, dest, true) else null
                }
                emit(flightRoutes)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load favorite routes", e)
                emit(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    // 搜索结果流
    private val _searchResults: Flow<SearchState> = _searchQuery
        .debounce(SEARCH_DEBOUNCE_MS)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(SearchState(searchResults = emptyList(), isSearching = false))
            } else {
                airportRepository.searchAirports(query)
                    .map { results -> SearchState(searchResults = results, isSearching = false) }
                    .onStart { emit(SearchState(isSearching = true)) }
                    .catch { emit(SearchState(isSearching = false)) }
            }
        }

    // 新增：根据用户输入和选择，动态决定当前的UI模式
    val displayMode: StateFlow<DisplayMode> = combine(
        _searchQuery,
        _selectedAirport
    ) { query, selectedAirport ->
        when {
            selectedAirport != null -> DisplayMode.Routes(selectedAirport)
            query.isNotBlank() -> DisplayMode.Search
            else -> DisplayMode.Favorites
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), DisplayMode.Favorites)

    // 重构：UI State 由 displayMode 驱动，逻辑更清晰
    val uiState: StateFlow<FlightScreenState> = displayMode
        .flatMapLatest { mode ->
            Log.d(TAG, "🎯 Mode changed to: ${mode::class.simpleName}")
            when (mode) {
                is DisplayMode.Favorites -> createFavoritesState()
                is DisplayMode.Search -> createSearchState()
                is DisplayMode.Routes -> createRoutesState(mode.departureAirport)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), FlightScreenState())


    // --- 状态构建辅助函数 ---
    private fun createFavoritesState(): Flow<FlightScreenState> {
        return _favoriteRoutes.map { favorites ->
            FlightScreenState(
                searchQuery = _searchQuery.value,
                favoriteRoutes = favorites
            )
        }
    }

    private fun createSearchState(): Flow<FlightScreenState> {
        return _searchResults.map { searchState ->
            FlightScreenState(
                searchQuery = _searchQuery.value,
                searchResults = searchState.searchResults,
                isSearching = searchState.isSearching
            )
        }
    }

    private fun createRoutesState(departureAirport: Airport): Flow<FlightScreenState> {
        // 结合所有目的地机场和收藏列表来构建航线
        return combine(
            airportRepository.getAirportsExcept(departureAirport.iataCode),
            _favoriteRoutes
        ) { destinations, favoriteRoutes ->
            val routes = destinations.map { dest ->
                FlightRoute(
                    departure = departureAirport,
                    destination = dest,
                    isFavorite = favoriteRoutes.any { favRoute ->
                        favRoute.departure.iataCode == departureAirport.iataCode &&
                                favRoute.destination.iataCode == dest.iataCode
                    }
                )
            }
            FlightScreenState(
                searchQuery = _searchQuery.value,
                departureAirport = departureAirport,
                availableRoutes = routes,
                isLoadingRoutes = false
            )
        }
            .onStart {
                // 在开始加载航线时，发射一个加载中状态
                emit(
                    FlightScreenState(
                        searchQuery = _searchQuery.value,
                        departureAirport = departureAirport,
                        isLoadingRoutes = true
                    )
                )
            }
            .catch { e ->
                Log.e(TAG, "Failed to get available routes for ${departureAirport.iataCode}", e)
                emit(
                    FlightScreenState(
                        searchQuery = _searchQuery.value,
                        departureAirport = departureAirport,
                        isLoadingRoutes = false,
                        errorMessage = "无法加载航线"
                    )
                )
            }
    }

    fun onEvent(event: FlightSearchEvent) {
        Log.d(TAG, "📨 Event received: ${event::class.simpleName}")
        viewModelScope.launch {
            when (event) {
                is FlightSearchEvent.QueryChanged -> {
                    _selectedAirport.value = null // 清除已选机场，会自动切换模式
                    userPreferencesRepository.saveSearchQuery(event.query)
                }

                is FlightSearchEvent.SearchTriggered -> {
                    _selectedAirport.value = null
                    userPreferencesRepository.saveSearchQuery(event.query)
                }

                is FlightSearchEvent.AirportSelected -> {
                    _selectedAirport.value = event.airport
                }

                is FlightSearchEvent.ToggleFavoriteClicked -> {
                    val route = event.route
                    favoriteRepository.toggleFavorite(
                        route.departure.iataCode,
                        route.destination.iataCode
                    )
                }

                is FlightSearchEvent.BackPressed -> {
                    // 返回操作就是清除已选机场，模式会自动切换回搜索或收藏
                    _selectedAirport.value = null
                }
            }
        }
    }
}

/**
 * 搜索状态封装
 */
private data class SearchState(
    val searchResults: List<Airport> = emptyList(),
    val isSearching: Boolean = false
)

/**
 * 单一状态类 (Single State Holder)
 * 这个类代表了屏幕在任何时间点所需要的所有数据。
 */
data class FlightScreenState(
    val searchQuery: String = "",
    val searchResults: List<Airport> = emptyList(),
    val departureAirport: Airport? = null,
    val availableRoutes: List<FlightRoute> = emptyList(),
    val favoriteRoutes: List<FlightRoute> = emptyList(),
    val isSearching: Boolean = false,
    val isLoadingRoutes: Boolean = false,
    val errorMessage: String? = null
)

/**
 * 用户意图事件
 */
sealed interface FlightSearchEvent {
    data class QueryChanged(val query: String) : FlightSearchEvent
    data class SearchTriggered(val query: String) : FlightSearchEvent
    data class AirportSelected(val airport: Airport) : FlightSearchEvent
    data class ToggleFavoriteClicked(val route: FlightRoute) : FlightSearchEvent
    data object BackPressed : FlightSearchEvent
}