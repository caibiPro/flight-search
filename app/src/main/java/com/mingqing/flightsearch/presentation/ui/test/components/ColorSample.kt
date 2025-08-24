package com.mingqing.flightsearch.presentation.ui.test.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mingqing.flightsearch.presentation.theme.FlightSearchTheme

/**
 * Color sample component to display theme colors
 * Used for testing and showcasing the theme color palette
 */
@Composable
fun ColorSample(
    color: Color,
    name: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/**
 * Theme color palette display section
 */
@Composable
fun ThemeColorDisplay(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Theme Color Display",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ColorSample(
                color = MaterialTheme.colorScheme.primary,
                name = "Primary",
                modifier = Modifier.weight(1f)
            )
            ColorSample(
                color = MaterialTheme.colorScheme.secondary,
                name = "Secondary",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ColorSample(
                color = MaterialTheme.colorScheme.tertiary,
                name = "Tertiary",
                modifier = Modifier.weight(1f)
            )
            ColorSample(
                color = MaterialTheme.colorScheme.surface,
                name = "Surface",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// Color sample preview
@Preview(
    name = "Color Sample",
    showBackground = true
)
@Composable
fun ColorSamplePreview() {
    FlightSearchTheme {
        ColorSample(
            color = MaterialTheme.colorScheme.primary,
            name = "Primary",
            modifier = Modifier.padding(16.dp)
        )
    }
}

// Color palette preview
@Preview(
    name = "Color Palette",
    showBackground = true
)
@Composable
fun ColorPalettePreview() {
    FlightSearchTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Aviation Blue Theme Palette",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ColorSample(
                    color = MaterialTheme.colorScheme.primary,
                    name = "Primary",
                    modifier = Modifier.weight(1f)
                )
                ColorSample(
                    color = MaterialTheme.colorScheme.secondary,
                    name = "Secondary",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ColorSample(
                    color = MaterialTheme.colorScheme.tertiary,
                    name = "Tertiary",
                    modifier = Modifier.weight(1f)
                )
                ColorSample(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    name = "Surface Variant",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}