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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightSearchViewModel @Inject constructor(
    private val airportRepository: AirportRepository,
    private val favoriteRepository: FavoriteRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // UI状态管理
    private val _uiState = MutableStateFlow(FlightSearchUiState())
    val uiState: StateFlow<FlightSearchUiState> = _uiState.asStateFlow()

    // 搜索查询状态 - 直接从DataStore获取
    val searchQuery: StateFlow<String> = userPreferencesRepository.getSearchQuery()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    // 收藏列表状态 - 独立管理
    private val _favorites = favoriteRepository.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        setupSearch()
        setupFavoritesDisplay()
    }

    // 核心优化：统一的状态更新器
    private fun updateUiState(updater: FlightSearchUiState.() -> FlightSearchUiState) {
        _uiState.value = _uiState.value.updater()
    }

    @OptIn(FlowPreview::class)
    private fun setupSearch() {
        viewModelScope.launch {
            searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotBlank()) {
                        performSearch(query)
                    } else {
                        // 清空搜索，准备显示收藏
                        updateUiState { clearSearch() }
                    }
                }
        }
    }

    private fun setupFavoritesDisplay() {
        combine(searchQuery, _favorites) { query, favorites ->
            if (query.isBlank()) {
                updateUiState { showFavorites(favorites) }
            }
        }.launchIn(viewModelScope)
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            updateUiState { showLoading() }

            try {
                airportRepository.searchAirports(query)
                    .catch { e -> updateUiState { showError(e.message) } }
                    .collect { airports -> updateUiState { showAirports(airports) } }
            } catch (e: Exception) {
                updateUiState { showError(e.message) }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveSearchQuery(query)
        }
    }

    fun selectAirport(airport: Airport) {
        viewModelScope.launch {
            updateUiState { showLoading().selectAirport(airport) }

            try {
                airportRepository.getAirportsExcept(airport.iataCode)
                    .catch { e ->
                        updateUiState { showError(e.message) }
                    }
                    .collect { destinationAirports ->
                        updateUiState { showRoutes(airport, destinationAirports) }
                    }
            } catch (e: Exception) {
                updateUiState { showError(e.message) }
            }
        }
    }

    fun backToSearch() {
        val currentQuery = searchQuery.value

        updateUiState {
            if (currentQuery.isBlank()) {
                showFavorites(_favorites.value)
            } else {
                showSearchResults()
            }.clearSelectedAirport()
        }
    }

    fun toggleFavorite(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            try {
                favoriteRepository.toggleFavorite(departureCode, destinationCode)
            } catch (e: Exception) {
                val message = "Failed to toggle favorite: ${e.message}"
                updateUiState { showError(message) }
            }
        }
    }

    fun observeFavoriteStatus(
        departureCode: String,
        destinationCode: String
    ): StateFlow<Boolean> {
        return favoriteRepository.observeFavoriteStatus(departureCode, destinationCode)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    }

    fun clearError() {
        updateUiState { clearError() }
    }

    private fun FlightSearchUiState.showLoading(): FlightSearchUiState = copy(
        isLoading = true,
        errorMessage = null
    )

    private fun FlightSearchUiState.hideLoading(): FlightSearchUiState = copy(
        isLoading = false
    )

    private fun FlightSearchUiState.showError(
        message: String?
    ): FlightSearchUiState = copy(
        isLoading = false,
        errorMessage = message
    )

    private fun FlightSearchUiState.clearError(): FlightSearchUiState = copy(
        errorMessage = null
    )

    private fun FlightSearchUiState.showFavorites(
        favorites: List<Favorite>
    ): FlightSearchUiState = copy(
        displayMode = DisplayMode.Favorites,
        favorites = favorites,
        airports = emptyList(),
        isLoading = false,
        errorMessage = null
    )

    private fun FlightSearchUiState.showAirports(
        airports: List<Airport>
    ): FlightSearchUiState = copy(
        displayMode = DisplayMode.Search,
        favorites = emptyList(),
        airports = airports,
        isLoading = false,
        errorMessage = null
    )

    private fun FlightSearchUiState.showRoutes(
        selectedAirport: Airport,
        destinations: List<Airport>
    ): FlightSearchUiState = copy(
        displayMode = DisplayMode.Routes,
        selectedAirport = selectedAirport,
        destinationAirports = destinations,
        airports = emptyList(),
        favorites = emptyList(),
        isLoading = false,
        errorMessage = null
    )

    private fun FlightSearchUiState.clearSearch(): FlightSearchUiState = copy(
        airports = emptyList(),
        isLoading = false,
        errorMessage = null
    )

    private fun FlightSearchUiState.showSearchResults(): FlightSearchUiState =
        copy(
            displayMode = DisplayMode.Search,
            favorites = emptyList(),
            selectedAirport = null,
            destinationAirports = emptyList()
        )

    private fun FlightSearchUiState.selectAirport(airport: Airport): FlightSearchUiState =
        copy(selectedAirport = airport)

    private fun FlightSearchUiState.clearSelectedAirport(): FlightSearchUiState =
        copy(
            selectedAirport = null,
            destinationAirports = emptyList()
        )
}

enum class DisplayMode {
    Search,
    Favorites,
    Routes
}

data class FlightSearchUiState(
    val displayMode: DisplayMode = DisplayMode.Favorites,
    val isLoading: Boolean = false,
    val airports: List<Airport> = emptyList(),
    val favorites: List<Favorite> = emptyList(),
    val selectedAirport: Airport? = null,
    val destinationAirports: List<Airport> = emptyList(),
    val errorMessage: String? = null
)