package com.mingqing.flightsearch.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 应用级别的脚手架组件，统一处理系统栏内边距
 * 支持真正的沉浸式设计，内容可以延伸到系统栏下方
 */
@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    includeStatusBarPadding: Boolean = true,
    includeNavigationBarPadding: Boolean = false,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (includeStatusBarPadding) {
                    Modifier.padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                } else {
                    Modifier
                }
            )
            .then(
                if (includeNavigationBarPadding) {
                    Modifier.padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                } else {
                    Modifier
                }
            )
    ) {
        content()
    }
}