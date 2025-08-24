package com.mingqing.flightsearch.presentation.ui.test.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mingqing.flightsearch.presentation.theme.FlightSearchTheme

/**
 * Input field testing section component
 * Tests text fields with theme colors and focus states
 */
@Composable
fun InputFieldTestSection(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Input Component Test",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            label = { Text("Search Airport") },
            placeholder = { Text("Enter IATA code or airport name...") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            label = { Text("Filled Style") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Search input field preview
@Preview(
    name = "Search Input Field - Light",
    showBackground = true
)
@Composable
fun SearchInputPreview() {
    FlightSearchTheme {
        var searchText by remember { mutableStateOf("LAX") }
        
        InputFieldTestSection(
            searchText = searchText,
            onSearchTextChange = { searchText = it },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(
    name = "Search Input Field - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SearchInputDarkPreview() {
    FlightSearchTheme {
        var searchText by remember { mutableStateOf("Los Angeles International") }
        
        InputFieldTestSection(
            searchText = searchText,
            onSearchTextChange = { searchText = it },
            modifier = Modifier.padding(16.dp)
        )
    }
}