package com.mingqing.flightsearch.presentation.ui.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mingqing.flightsearch.presentation.theme.FlightSearchTheme
import com.mingqing.flightsearch.presentation.ui.test.components.ButtonTestSection
import com.mingqing.flightsearch.presentation.ui.test.components.ChipTestSection
import com.mingqing.flightsearch.presentation.ui.test.components.FlightRouteTestSection
import com.mingqing.flightsearch.presentation.ui.test.components.InputFieldTestSection
import com.mingqing.flightsearch.presentation.ui.test.components.ThemeColorDisplay

/**
 * Theme testing screen for Material3 aviation blue theme
 * Tests various components in light/dark mode and dynamic color support
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeTestScreen() {
    var searchText by remember { mutableStateOf("") }
    var selectedChip by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flight Search Theme Test") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Title section
            item {
                ThemeTestHeader()
            }

            // Button testing section
            item {
                ButtonTestSection()
            }

            // Input component testing
            item {
                InputFieldTestSection(
                    searchText = searchText,
                    onSearchTextChange = { searchText = it }
                )
            }

            // Chip testing
            item {
                ChipTestSection(
                    selectedChip = selectedChip,
                    onChipSelected = { selectedChip = it }
                )
            }

            // Card and list testing
            item {
                FlightRouteTestSection()
            }

            // Color display section
            item {
                ThemeColorDisplay()
            }
        }
    }
}

/**
 * Header section for theme test screen
 */
@Composable
private fun ThemeTestHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Material3 Aviation Blue Theme Test",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Test various components in light/dark mode effects",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Theme test screen previews
@Preview(
    name = "Theme Test - Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ThemeTestScreenLightPreview() {
    FlightSearchTheme(darkTheme = false) {
        ThemeTestScreen()
    }
}

@Preview(
    name = "Theme Test - Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ThemeTestScreenDarkPreview() {
    FlightSearchTheme(darkTheme = true) {
        ThemeTestScreen()
    }
}

// Multiple device size previews
@Preview(
    name = "Phone Preview",
    device = "spec:width=360dp,height=640dp,dpi=480",
    showSystemUi = true
)
@Composable
fun PhonePreview() {
    FlightSearchTheme {
        ThemeTestScreen()
    }
}

@Preview(
    name = "Tablet Preview",
    device = "spec:width=1280dp,height=800dp,dpi=480",
    showSystemUi = true
)
@Composable
fun TabletPreview() {
    FlightSearchTheme {
        ThemeTestScreen()
    }
}