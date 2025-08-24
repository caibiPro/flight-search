package com.mingqing.flightsearch.presentation.ui.test.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mingqing.flightsearch.presentation.theme.FlightSearchTheme

/**
 * Button testing section component
 * Tests all button variations with theme colors
 */
@Composable
fun ButtonTestSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Button Style Test",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { },
                modifier = Modifier.weight(1f)
            ) {
                Text("Primary")
            }

            OutlinedButton(
                onClick = { },
                modifier = Modifier.weight(1f)
            ) {
                Text("Outlined")
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(
                onClick = { },
                modifier = Modifier.weight(1f)
            ) {
                Text("Text Button")
            }

            ElevatedButton(
                onClick = { },
                modifier = Modifier.weight(1f)
            ) {
                Text("Elevated")
            }
        }
    }
}

// Button variations preview
@Preview(
    name = "Button Variations - Light",
    showBackground = true
)
@Composable
fun ButtonVariationsPreview() {
    FlightSearchTheme {
        ButtonTestSection(
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(
    name = "Button Variations - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ButtonVariationsDarkPreview() {
    FlightSearchTheme {
        ButtonTestSection(
            modifier = Modifier.padding(16.dp)
        )
    }
}