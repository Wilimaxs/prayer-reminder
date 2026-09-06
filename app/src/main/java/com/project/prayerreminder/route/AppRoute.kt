package com.project.prayerreminder.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.prayerreminder.feature.main.MainBottomNavigationScreen
import com.project.prayerreminder.feature.splash.SplashScreen

@Composable
fun AppRoute(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route

    val showBottomNavigation = AppScreen.entries.firstOrNull { screen ->
        screen.route == currentScreen
    }?.showBottomNavigation == true

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomNavigation) {
                MainBottomNavigationScreen(
                    currentScreen = currentScreen,
                    onItemClick = { screen ->
                        navController.navigate(screen.route) {
                            popUpTo(AppScreen.HOME.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        val navigationPadding = if (showBottomNavigation) {
            innerPadding
        } else {
            PaddingValues()
        }
        NavHost(
            navController = navController,
            startDestination = AppScreen.SPLASH.route,
            modifier = Modifier
                .padding(navigationPadding)
                .consumeWindowInsets(navigationPadding)
        ) {
            composable(AppScreen.SPLASH.route) {
                SplashScreen(
                    modifier = Modifier.fillMaxSize(),
                    onFinished = {
                        navController.navigate(AppScreen.HOME.route) {
                            popUpTo(AppScreen.SPLASH.route) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(AppScreen.HOME.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Home Screen")
                }
            }

            composable(AppScreen.CALENDAR.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Calendar Screen")
                }
            }

            composable(AppScreen.PROFILE.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Profile Screen")
                }
            }
        }
    }
}