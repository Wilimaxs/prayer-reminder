package com.project.prayerreminder.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.prayerreminder.feature.splash.SplashScreen

@Composable
fun AppRoute(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.SPLASH.route,
        modifier = modifier
    ) {
        // Splash Screen
        composable(route = AppScreen.SPLASH.route) {
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

        //TODO: Dummy home for testing
        composable(route = AppScreen.HOME.route) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Home")
            }
        }
    }
}