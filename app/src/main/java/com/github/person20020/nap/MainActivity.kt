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
import androidx.compose.ui.graphics.fromColorLong
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.github.person20020.nap.ui.theme.NapTheme
import com.github.person20020.nap.viewmodels.MainViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val mainViewModel: MainViewModel by viewModels {
        MainViewModel.factory(
            (application as Nap).repository,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        val prefs = (application as Nap).repository

        var initialSeedColor: Long? = null

        splashScreen.setKeepOnScreenCondition { initialSeedColor == null }

        lifecycleScope.launch {
            val initialDarkTheme = prefs.darkTheme.first()
            val initialDynamicColor = prefs.dynamicColors.first()
            initialSeedColor = prefs.seedColor.first()

            setContent {
                val navController = rememberNavController()

                val darkTheme by prefs.darkTheme.collectAsStateWithLifecycle(initialDarkTheme)
                val dynamicColors by prefs.dynamicColors.collectAsStateWithLifecycle(initialDynamicColor)
                val seedColor by prefs.seedColor.collectAsStateWithLifecycle(initialSeedColor)

                val isDark =
                    when (darkTheme) {
                        -1 -> false
                        1 -> true
                        else -> isSystemInDarkTheme()
                    }

                LaunchedEffect(isDark) {
                    enableEdgeToEdge(
                        statusBarStyle =
                            if (isDark) {
                                SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                            } else {
                                SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
                            },
                        navigationBarStyle =
                            if (isDark) {
                                SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                            } else {
                                SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
                            },
                    )
                }

                NapTheme(
                    seedColor = Color.fromColorLong(seedColor),
                    isDark = isDark,
                    dynamicColor = dynamicColors,
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
