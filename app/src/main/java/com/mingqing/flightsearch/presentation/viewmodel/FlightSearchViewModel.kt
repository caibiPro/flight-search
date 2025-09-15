package com.mingqing.flightsearch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mingqing.flightsearch.domain.model.Airport
import com.mingqing.flightsearch.domain.model.Favorite
import com.mingqing.flightsearch.domain.repository.AirportRepository
import com.mingqing.flightsearch.domain.repository.FavoriteRepository
import com.mingqing.flightsearch.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class FlightSearchViewModel @Inject constructor(
    private val airportRepository: AirportRepository,
    private val favoriteRepository: FavoriteRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private var searchQueryJob: Job? = null
    private var previousState: FlightSearchUiState? = null

    companion object {
        private val OPERATION_TIMEOUT = 10.seconds
        private const val SEARCH_DEBOUNCE_MS = 300L
    }

    private val _uiState = MutableStateFlow<FlightSearchUiState>(FlightSearchUiState.Loading)
    val uiState: StateFlow<FlightSearchUiState> = _uiState.asStateFlow()

    // 依然保留对搜索词的监听，这是驱动搜索逻辑的最佳方式
    private val searchQuery: StateFlow<String> = userPreferencesRepository.getSearchQuery()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ""
        )

    init {
        // 启动时，监听搜索词的变化
        observeSearchQuery()
    }

    // 步骤 3: 采用一个中心化的事件处理器
    fun onEvent(event: FlightSearchEvent) {
        when (event) {
            is FlightSearchEvent.AirportSelected -> selectAirport(event.airport)
            is FlightSearchEvent.QueryChanged -> updateSearchQuery(event.query)
            is FlightSearchEvent.ToggleFavoriteClicked -> toggleFavorite(event.departureCode, event.destinationCode)
            is FlightSearchEvent.BackToPreviousScreen -> backToPreviousScreen()
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        searchQueryJob?.cancel()
        searchQueryJob = viewModelScope.launch {
            searchQuery
                .debounce(SEARCH_DEBOUNCE_MS)
                .distinctUntilChanged()
                .collect { query ->
                    showFavoriteOrSearch(query)
                }
        }
    }

    private suspend fun showFavoriteOrSearch(query: String) {
        if (query.isBlank()) {
            loadFavorites()
        } else {
            performSearch(query)
        }
    }

    private suspend fun loadFavorites() {
        val result = withTimeoutOrNull(OPERATION_TIMEOUT) {
            favoriteRepository.getAllFavorites()
                .catch { e ->
                    _uiState.value =
                        FlightSearchUiState.Error(e.message ?: "Failed to load favorites")
                }
                .collect { favorites ->
                    _uiState.value = FlightSearchUiState.Favorites(favorites)
                }
        }

        if (result == null) {
            _uiState.value = FlightSearchUiState.Error("Request timed out. Please try again.")
        }
    }

    private suspend fun performSearch(query: String) {
        // Store current state before transitioning to search
        if (_uiState.value !is FlightSearchUiState.SearchResults) {
            previousState = _uiState.value
        }

        _uiState.value = FlightSearchUiState.SearchResults(
            query = query,
            airports = emptyList(),
            isLoading = true
        )

        val result = withTimeoutOrNull(OPERATION_TIMEOUT) {
            airportRepository.searchAirports(query)
                .catch { e ->
                    _uiState.value = FlightSearchUiState.Error(e.message ?: "Search failed")
                }
                .collect { airports ->
                    _uiState.value =
                        FlightSearchUiState.SearchResults(query = query, airports = airports)
                }
        }

        if (result == null) {
            _uiState.value = FlightSearchUiState.Error("Search timed out. Please try again.")
        }
    }

    private fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveSearchQuery(query)
        }
    }

    @OptIn(FlowPreview::class)
    private fun selectAirport(airport: Airport) {
        viewModelScope.launch {
            // Store current state before transitioning to routes
            previousState = _uiState.value

            _uiState.value = FlightSearchUiState.Routes(
                departureAirport = airport,
                destinationAirports = emptyList(),
                isLoading = true
            )

            try {
                val destinations = withTimeoutOrNull(OPERATION_TIMEOUT) {
                    airportRepository.getAirportsExcept(airport.iataCode)
                        .timeout(OPERATION_TIMEOUT)
                        .firstOrNull()
                }

                when {
                    destinations == null -> {
                        _uiState.value = FlightSearchUiState.Error("Request timed out. Please try again.")
                    }
                    destinations.isEmpty() -> {
                        _uiState.value = FlightSearchUiState.Routes(
                            departureAirport = airport,
                            destinationAirports = emptyList(),
                            isLoading = false
                        )
                    }
                    else -> {
                        _uiState.value = FlightSearchUiState.Routes(
                            departureAirport = airport,
                            destinationAirports = destinations,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = FlightSearchUiState.Error(
                    e.message ?: "Failed to load routes. Please check your connection and try again."
                )
            }
        }
    }

    private fun backToPreviousScreen() {
        val storedPreviousState = previousState

        // If we have a stored previous state, restore it
        if (storedPreviousState != null) {
            _uiState.value = storedPreviousState
            previousState = null
        } else {
            viewModelScope.launch {
                loadFavorites()
            }
        }
    }

    private fun toggleFavorite(
        departureCode: String,
        destinationCode: String
    ) = viewModelScope.launch {
        // 收藏操作不直接改变主UI状态，而是通过更新数据库，
        // 依赖于收藏夹页面的实时数据流来刷新UI
        favoriteRepository.toggleFavorite(departureCode, destinationCode)
    }

    // 这个方法可以保持不变，因为它与主UI状态机解耦，用于单个列表项的UI更新
    fun observeFavoriteStatus(
        departureCode: String,
        destinationCode: String
    ): StateFlow<Boolean> {
        return favoriteRepository.observeFavoriteStatus(departureCode, destinationCode)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)
    }
}

// 步骤 1: 定义清晰、互斥的UI状态
sealed interface FlightSearchUiState {
    // 初始加载状态
    data object Loading : FlightSearchUiState

    // 收藏夹/主页状态
    data class Favorites(
        val favorites: List<Favorite>
    ) : FlightSearchUiState

    // 搜索结果状态
    data class SearchResults(
        val query: String,
        val airports: List<Airport>,
        val isLoading: Boolean = false
    ) : FlightSearchUiState

    // 航线详情状态
    data class Routes(
        val departureAirport: Airport,
        val destinationAirports: List<Airport>,
        val isLoading: Boolean = false
    ) : FlightSearchUiState

    // 错误状态
    data class Error(
        val message: String
    ) : FlightSearchUiState
}

// 步骤 2: 定义所有用户意图/事件
sealed interface FlightSearchEvent {
    data class QueryChanged(
        val query: String
    ) : FlightSearchEvent

    data class AirportSelected(
        val airport: Airport
    ) : FlightSearchEvent

    data class ToggleFavoriteClicked(
        val departureCode: String,
        val destinationCode: String
    ) : FlightSearchEvent

    data object BackToPreviousScreen : FlightSearchEvent
}