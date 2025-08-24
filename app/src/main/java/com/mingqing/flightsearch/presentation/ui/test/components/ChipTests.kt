package com.mingqing.flightsearch.presentation.ui.test.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mingqing.flightsearch.presentation.theme.FlightSearchTheme

/**
 * Chip testing section component
 * Tests filter chips and assist chips with theme colors
 */
@Composable
fun ChipTestSection(
    selectedChip: Int,
    onChipSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Chip Component Test",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedChip == 0,
                onClick = { onChipSelected(0) },
                label = { Text("All") }
            )
            FilterChip(
                selected = selectedChip == 1,
                onClick = { onChipSelected(1) },
                label = { Text("Favorites") },
                leadingIcon = if (selectedChip == 1) {
                    {
                        Icon(
                            Icons.Default.Favorite, null,
                            Modifier.size(18.dp)
                        )
                    }
                } else null
            )
            AssistChip(
                onClick = { },
                label = { Text("Suggest") }
            )
        }
    }
}

// Chip component preview
@Preview(
    name = "Chip Test Section - Light",
    showBackground = true
)
@Composable
fun ChipTestSectionPreview() {
    FlightSearchTheme {
        var selectedChip by remember { mutableIntStateOf(0) }
        
        ChipTestSection(
            selectedChip = selectedChip,
            onChipSelected = { selectedChip = it },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(
    name = "Chip Test Section - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ChipTestSectionDarkPreview() {
    FlightSearchTheme {
        var selectedChip by remember { mutableIntStateOf(1) }
        
        ChipTestSection(
            selectedChip = selectedChip,
            onChipSelected = { selectedChip = it },
            modifier = Modifier.padding(16.dp)
        )
    }
}