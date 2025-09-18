package com.mingqing.flightsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mingqing.flightsearch.presentation.theme.FlightSearchTheme
import com.mingqing.flightsearch.presentation.ui.FlightSearchScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlightSearchTheme {
                FlightSearchScreen()
            }
        }
    }
}