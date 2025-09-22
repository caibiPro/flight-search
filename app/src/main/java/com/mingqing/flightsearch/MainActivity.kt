package com.mingqing.flightsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mingqing.flightsearch.presentation.theme.FlightSearchTheme
import com.mingqing.flightsearch.presentation.ui.FlightSearchScreen
import com.mingqing.flightsearch.presentation.ui.components.AppScaffold
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 启用 Edge-to-Edge 显示
        enableEdgeToEdge()

        setContent {
            FlightSearchTheme {
                AppScaffold {
                    FlightSearchScreen()
                }
            }
        }
    }
}