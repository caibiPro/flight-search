package com.mingqing.flightsearch.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.mingqing.flightsearch.domain.model.Airport
import com.mingqing.flightsearch.domain.model.Favorite
import com.mingqing.flightsearch.domain.repository.AirportRepository
import com.mingqing.flightsearch.domain.repository.FavoriteRepository
import com.mingqing.flightsearch.domain.repository.UserPreferencesRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FlightSearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var airportRepository: AirportRepository
    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var viewModel: FlightSearchViewModel

    // --- 测试数据 ---
    private val testAirports = listOf(
        Airport(1, "LAX", "Los Angeles International", 87534384),
        Airport(2, "JFK", "John F. Kennedy International", 62551253),
    )
    private val testFavorites = listOf(Favorite(1, "LAX", "JFK"))
    private val testDestinations = listOf(Airport(3, "PEK", "Beijing Capital", 95786296))

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        airportRepository = mockk(relaxed = true)
        favoriteRepository = mockk(relaxed = true)
        userPreferencesRepository = mockk(relaxed = true)

        every { userPreferencesRepository.getSearchQuery() } returns flowOf("")
        every { favoriteRepository.getAllFavorites() } returns flowOf(testFavorites)
    }

    private fun createViewModel() {
        viewModel = FlightSearchViewModel(
            airportRepository = airportRepository,
            favoriteRepository = favoriteRepository,
            userPreferencesRepository = userPreferencesRepository
        )
    }

    @Test
    fun `initial state is Loading, then transitions to Favorites on empty query`() = runTest {
        createViewModel()
        viewModel.uiState.test {
            assertEquals("Initial state should be Loading", FlightSearchUiState.Loading, awaitItem())
            val favoritesState = awaitItem()
            assertTrue("State should transition to Favorites", favoritesState is FlightSearchUiState.Favorites)
            assertEquals(testFavorites, (favoritesState as FlightSearchUiState.Favorites).favorites)
        }
    }

    @Test
    fun `init, when loading favorites fails, transitions to Error state`() = runTest {
        val errorMessage = "Failed to read database"
        every { favoriteRepository.getAllFavorites() } returns flow { throw Exception(errorMessage) }
        createViewModel()
        viewModel.uiState.test {
            assertEquals("Initial state should be Loading", FlightSearchUiState.Loading, awaitItem())
            val errorState = awaitItem()
            assertTrue("State should transition to Error", errorState is FlightSearchUiState.Error)
            assertEquals(errorMessage, (errorState as FlightSearchUiState.Error).message)
        }
    }

    @Test
    fun `when query is not empty, state transitions to loading SearchResults, then to populated results`() = runTest {
        val query = "LAX"
        every { userPreferencesRepository.getSearchQuery() } returns flowOf(query)
        every { airportRepository.searchAirports(query) } returns flowOf(testAirports)
        createViewModel()
        viewModel.uiState.test {
            assertEquals("Initial state should be Loading", FlightSearchUiState.Loading, awaitItem())
            val loadingResultsState = awaitItem()
            assertTrue("Should be in SearchResults state", loadingResultsState is FlightSearchUiState.SearchResults)
            with(loadingResultsState as FlightSearchUiState.SearchResults) {
                assertEquals(query, this.query)
                assertTrue("Airports should be empty during search", this.airports.isEmpty())
                assertTrue("Should show loading indicator", this.isLoading)
            }
            val finalResultsState = awaitItem()
            assertTrue("Should finally be in SearchResults state", finalResultsState is FlightSearchUiState.SearchResults)
            with(finalResultsState as FlightSearchUiState.SearchResults) {
                assertEquals(query, this.query)
                assertEquals(testAirports, this.airports)
                assertFalse("Loading should be finished", this.isLoading)
            }
        }
    }

    @Test
    fun `when search fails, transitions to Error state`() = runTest {
        val query = "JFK"
        val errorMessage = "Network unavailable"
        every { userPreferencesRepository.getSearchQuery() } returns flowOf(query)
        every { airportRepository.searchAirports(query) } returns flow { throw Exception(errorMessage) }
        createViewModel()
        viewModel.uiState.test {
            assertEquals(FlightSearchUiState.Loading, awaitItem())
            val loadingSearchState = awaitItem()
            assertTrue(loadingSearchState is FlightSearchUiState.SearchResults)
            assertTrue((loadingSearchState as FlightSearchUiState.SearchResults).isLoading)
            val errorState = awaitItem()
            assertTrue("State should transition to Error", errorState is FlightSearchUiState.Error)
            assertEquals(errorMessage, (errorState as FlightSearchUiState.Error).message)
        }
    }

    @Test
    fun `onEvent AirportSelected, transitions to loading Routes, then to successful Routes`() = runTest {
        val departureAirport = testAirports[0]
        every { airportRepository.getAirportsExcept(departureAirport.iataCode) } returns flowOf(testDestinations)
        createViewModel()
        viewModel.uiState.test {
            awaitItem() // Loading
            awaitItem() // Favorites
            viewModel.onEvent(FlightSearchEvent.AirportSelected(departureAirport))
            val loadingRoutesState = awaitItem()
            assertTrue("State should be Routes", loadingRoutesState is FlightSearchUiState.Routes)
            with(loadingRoutesState as FlightSearchUiState.Routes) {
                assertEquals(departureAirport, this.departureAirport)
                assertTrue("Should be loading destinations", this.isLoading)
                assertTrue("Destinations should be empty while loading", this.destinationAirports.isEmpty())
            }
            val finalRoutesState = awaitItem()
            assertTrue("State should finally be Routes", finalRoutesState is FlightSearchUiState.Routes)
            with(finalRoutesState as FlightSearchUiState.Routes) {
                assertEquals(departureAirport, this.departureAirport)
                assertFalse("Loading should be finished", this.isLoading)
                assertEquals(testDestinations, this.destinationAirports)
            }
        }
    }

    @Test
    fun `onEvent AirportSelected, when repository fails, transitions to Error state`() = runTest {
        val departureAirport = testAirports[0]
        val errorMessage = "Network Error"
        every { airportRepository.getAirportsExcept(any()) } returns flow { throw Exception(errorMessage) }
        createViewModel()
        viewModel.uiState.test {
            awaitItem() // Loading
            awaitItem() // Favorites
            viewModel.onEvent(FlightSearchEvent.AirportSelected(departureAirport))
            awaitItem() // loading Routes state
            val errorState = awaitItem()
            assertTrue("State should be Error", errorState is FlightSearchUiState.Error)
            assertEquals("Network Error", (errorState as FlightSearchUiState.Error).message)
        }
    }

    @Test
    fun `onEvent BackToPreviousScreen, returns to Favorites when query is blank`() = runTest {
        every { airportRepository.getAirportsExcept(any()) } returns flowOf(testDestinations)
        createViewModel()
        viewModel.uiState.test {
            awaitItem() // Loading
            awaitItem() // Favorites
            viewModel.onEvent(FlightSearchEvent.AirportSelected(testAirports[0]))
            awaitItem() // loading Routes
            awaitItem() // final Routes
            viewModel.onEvent(FlightSearchEvent.BackToPreviousScreen)
            val finalState = awaitItem()
            assertTrue("Should return to Favorites state", finalState is FlightSearchUiState.Favorites)
            assertEquals(testFavorites, (finalState as FlightSearchUiState.Favorites).favorites)
        }
    }

    @Test
    fun `onEvent BackToPreviousScreen, returns to SearchResults when query is not blank`() = runTest {
        val query = "LAX"
        every { userPreferencesRepository.getSearchQuery() } returns flowOf(query)
        every { airportRepository.searchAirports(query) } returns flowOf(testAirports)
        every { airportRepository.getAirportsExcept(any()) } returns flowOf(testDestinations)
        createViewModel()
        viewModel.uiState.test {
            awaitItem() // 1. Loading
            awaitItem() // 2. loading SearchResults
            awaitItem() // 3. final SearchResults
            viewModel.onEvent(FlightSearchEvent.AirportSelected(testAirports[0]))
            awaitItem() // 1. loading Routes
            awaitItem() // 2. final Routes
            viewModel.onEvent(FlightSearchEvent.BackToPreviousScreen)
            val finalState = awaitItem()
            assertTrue("Should return to SearchResults state", finalState is FlightSearchUiState.SearchResults)
            assertEquals(query, (finalState as FlightSearchUiState.SearchResults).query)
            assertEquals(testAirports, finalState.airports)
        }
    }

    @Test
    fun `onEvent QueryChanged, calls repository to save query`() = runTest {
        val query = "JFK"
        createViewModel()
        viewModel.onEvent(FlightSearchEvent.QueryChanged(query))
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify(exactly = 1) { userPreferencesRepository.saveSearchQuery(query) }
    }

    @Test
    fun `onEvent ToggleFavoriteClicked, calls repository to toggle favorite`() = runTest {
        createViewModel()
        val departure = "LAX"
        val destination = "JFK"
        viewModel.onEvent(FlightSearchEvent.ToggleFavoriteClicked(departure, destination))
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify(exactly = 1) { favoriteRepository.toggleFavorite(departure, destination) }
    }

    @Test
    fun `when search times out, transitions to Error state with timeout message`() = runTest {
        val query = "LAX"
        every { userPreferencesRepository.getSearchQuery() } returns flowOf(query)
        every { airportRepository.searchAirports(query) } returns flow {
            delay(15000) // Longer than OPERATION_TIMEOUT
            emit(testAirports)
        }
        createViewModel()
        viewModel.uiState.test {
            awaitItem() // Loading
            awaitItem() // SearchResults loading
            val errorState = awaitItem()
            assertTrue("State should transition to Error", errorState is FlightSearchUiState.Error)
            assertEquals("Search timed out. Please try again.", (errorState as FlightSearchUiState.Error).message)
        }
    }

    @Test
    fun `when loading favorites times out, transitions to Error state with timeout message`() = runTest {
        every { favoriteRepository.getAllFavorites() } returns flow {
            delay(15000) // Longer than OPERATION_TIMEOUT
            emit(testFavorites)
        }
        createViewModel()
        viewModel.uiState.test {
            awaitItem() // Loading
            val errorState = awaitItem()
            assertTrue("State should transition to Error", errorState is FlightSearchUiState.Error)
            assertEquals("Request timed out. Please try again.", (errorState as FlightSearchUiState.Error).message)
        }
    }

    @Test
    fun `when loading routes times out, transitions to Error state with timeout message`() = runTest {
        val departureAirport = testAirports[0]
        every { airportRepository.getAirportsExcept(departureAirport.iataCode) } returns flow {
            delay(15000) // Longer than OPERATION_TIMEOUT
            emit(testDestinations)
        }
        createViewModel()
        viewModel.uiState.test {
            awaitItem() // Loading
            awaitItem() // Favorites
            viewModel.onEvent(FlightSearchEvent.AirportSelected(departureAirport))
            awaitItem() // loading Routes state
            val errorState = awaitItem()
            assertTrue("State should be Error", errorState is FlightSearchUiState.Error)
            assertEquals("Request timed out. Please try again.", (errorState as FlightSearchUiState.Error).message)
        }
    }

    @Test
    fun `when getAirportsExcept returns empty list, Routes state shows empty destinations`() = runTest {
        val departureAirport = testAirports[0]
        every { airportRepository.getAirportsExcept(departureAirport.iataCode) } returns flowOf(emptyList())
        createViewModel()
        viewModel.uiState.test {
            awaitItem() // Loading
            awaitItem() // Favorites
            viewModel.onEvent(FlightSearchEvent.AirportSelected(departureAirport))
            awaitItem() // loading Routes state
            val finalRoutesState = awaitItem()
            assertTrue("State should be Routes", finalRoutesState is FlightSearchUiState.Routes)
            with(finalRoutesState as FlightSearchUiState.Routes) {
                assertEquals(departureAirport, this.departureAirport)
                assertFalse("Loading should be finished", this.isLoading)
                assertTrue("Destinations should be empty", this.destinationAirports.isEmpty())
            }
        }
    }

    @Test
    fun `onEvent BackToPreviousScreen with no previousState and blank query, retriggers favorites loading`() = runTest {
        // Arrange: 模拟初始加载收藏夹失败，进入Error状态，此时 previousState 为 null
        val errorMessage = "Database error"
        every { favoriteRepository.getAllFavorites() } returns flow { throw Exception(errorMessage) }
        every { userPreferencesRepository.getSearchQuery() } returns flowOf("") // 确保query为空
        createViewModel()

        viewModel.uiState.test {
            assertEquals(FlightSearchUiState.Loading, awaitItem())
            val errorState = awaitItem()
            assertTrue("Should be in Error state", errorState is FlightSearchUiState.Error)
            assertEquals(errorMessage, (errorState as FlightSearchUiState.Error).message)

            // Arrange: 修复数据源，使其能够成功返回数据
            every { favoriteRepository.getAllFavorites() } returns flowOf(testFavorites)

            // Act: 触发返回事件，这将命中 backToPreviousScreen 的 else 分支
            viewModel.onEvent(FlightSearchEvent.BackToPreviousScreen)

            // Assert: 验证 ViewModel 重新加载并成功进入 Favorites 状态
            val finalState = awaitItem()
            assertTrue("Should recover to Favorites state", finalState is FlightSearchUiState.Favorites)
            assertEquals(testFavorites, (finalState as FlightSearchUiState.Favorites).favorites)
        }
    }
}