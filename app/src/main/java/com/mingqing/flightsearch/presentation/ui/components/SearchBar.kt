package com.mingqing.flightsearch.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mingqing.flightsearch.presentation.theme.FlightSearchTheme

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "搜索机场（IATA代码或名称）",
    enabled: Boolean = true
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }


    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "搜索",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "清除",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        singleLine = true,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
            }
        ),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Preview(name = "Light Theme - Empty")
@Preview(name = "Dark Theme - Empty", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchBarEmptyPreview() {
    FlightSearchTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            SearchBar(
                query = "",
                onQueryChange = { }
            )
        }
    }
}

@Preview(name = "Light Theme - With Text")
@Preview(name = "Dark Theme - With Text", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchBarWithTextPreview() {
    FlightSearchTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            SearchBar(
                query = "LAX",
                onQueryChange = { }
            )
        }
    }
}

@Preview(name = "Light Theme - Disabled")
@Preview(name = "Dark Theme - Disabled", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchBarDisabledPreview() {
    FlightSearchTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            SearchBar(
                query = "JFK",
                onQueryChange = { },
                enabled = false
            )
        }
    }
}
