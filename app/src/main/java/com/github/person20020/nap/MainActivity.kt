package com.github.person20020.nap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.github.person20020.nap.ui.theme.DefaultSeedColor
import com.github.person20020.nap.ui.theme.NapTheme
import com.github.person20020.nap.utils.toHsv
import com.github.person20020.nap.viewmodels.MainViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.getValue

class MainActivity : ComponentActivity() {

    val mainViewModel: MainViewModel by viewModels {
        MainViewModel.factory(
            (application as Nap).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        val prefs = (application as Nap).repository

        var initialDarkTheme: Int? = null
        var initialDynamicColor: Boolean? = null
        var initialSeedColorHue: Float? = null

        splashScreen.setKeepOnScreenCondition { initialSeedColorHue == null }

        lifecycleScope.launch {
            initialDarkTheme = prefs.darkTheme.first()
            initialDynamicColor = prefs.dynamicColors.first()
            initialSeedColorHue = prefs.seedColorHue.first()

            setContent {
                val navController = rememberNavController()

                val darkTheme by prefs.darkTheme.collectAsStateWithLifecycle(initialDarkTheme)
                val dynamicColors by prefs.dynamicColors.collectAsStateWithLifecycle(initialDynamicColor)
                val seedColorHue by prefs.seedColorHue.collectAsStateWithLifecycle(initialSeedColorHue)

                val isDark = when(darkTheme) {
                    -1 -> false
                    1 -> true
                    else -> isSystemInDarkTheme()
                }

                LaunchedEffect(isDark) {
                    enableEdgeToEdge(
                        statusBarStyle = if (isDark) SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                        else SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
                        navigationBarStyle = if (isDark) SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                        else SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
                    )
                }


                NapTheme(
                    seedColor = Color.hsv(seedColorHue, 1f, 1f),
                    isDark = isDark,
                    dynamicColor = dynamicColors
                ) {
                    App(
                        navController,
                        mainViewModel,
                    )
                }
            }
        }
    }
}
