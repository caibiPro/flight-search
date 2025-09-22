package com.mingqing.flightsearch.presentation.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mingqing.flightsearch.presentation.theme.FlightSearchTheme

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: FavoriteButtonSize = FavoriteButtonSize.Normal
) {
    val hapticFeedback = LocalHapticFeedback.current

    IconButton(
        onClick = {
            // 使用更明显的触感反馈
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        },
        modifier = modifier.size(size.buttonSize)
    ) {
        Icon(
            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Outlined.FavoriteBorder
            },
            contentDescription = if (isFavorite) {
                "Remove from favorites"
            } else {
                "Add to favorites"
            },
            tint = if (isFavorite) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier.size(size.iconSize)
        )
    }
}

enum class FavoriteButtonSize(
    val buttonSize: Dp,
    val iconSize: Dp
) {
    Small(32.dp, 16.dp),    // 紧凑场景
    Normal(40.dp, 20.dp),   // 平衡大小
    Large(48.dp, 24.dp)     // 标准触摸目标
}

@Preview(name = "Not Favorite")
@Composable
private fun FavoriteButtonNotFavoritePreview() {
    FlightSearchTheme {
        Surface {
            FavoriteButton(
                isFavorite = false,
                onClick = { }
            )
        }
    }
}

@Preview(name = "Favorite")
@Composable
private fun FavoriteButtonFavoritePreview() {
    FlightSearchTheme {
        Surface {
            FavoriteButton(
                isFavorite = true,
                onClick = { }
            )
        }
    }
}