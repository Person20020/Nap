package com.github.person20020.nap

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.person20020.nap.ui.components.BottomNavBar
import com.github.person20020.nap.ui.screens.HomeScreen
import com.github.person20020.nap.ui.screens.OptionsScreen
import com.github.person20020.nap.ui.screens.SettingsScreen
import com.github.person20020.nap.ui.theme.DeveloperSettingsScreen
import com.github.person20020.nap.viewmodels.MainViewModel


val routes = listOf("home", "options", "settings", "settings/developer")

@Composable
fun App(
    navController: NavHostController,
    mainViewModel: MainViewModel,
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController = navController
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            enterTransition = {
                val currentRouteIndex = routes.indexOf(initialState.destination.route)
                val targetRouteIndex = routes.indexOf(targetState.destination.route)

                if (currentRouteIndex < targetRouteIndex) {
                    slideInHorizontally { it / 6 } + fadeIn(tween(150))
                } else {
                    slideInHorizontally { -it / 6 } + fadeIn(tween(150))
                }
            },
            exitTransition = {
                val currentRouteIndex = routes.indexOf(initialState.destination.route)
                val targetRouteIndex = routes.indexOf(targetState.destination.route)

                if (currentRouteIndex < targetRouteIndex) {
                    slideOutHorizontally { -it / 8 } + fadeOut(tween(100))
                } else {
                    slideOutHorizontally { it / 8 } + fadeOut(tween(100))
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(mainViewModel)
            }
            composable("options") {
                OptionsScreen(mainViewModel)
            }
            composable("settings") {
                SettingsScreen(
                    mainViewModel,
                    onNavigate = { route ->
                        navController.navigate(route)
                    }
                )
            }

            composable("settings/developer") {
                DeveloperSettingsScreen()
            }
        }
    }
}