package com.project.prayerreminder.route

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.prayerreminder.feature.calendar.CalendarScreen
import com.project.prayerreminder.feature.home.HomeScreen
import com.project.prayerreminder.feature.main.MainBottomNavigationScreen
import com.project.prayerreminder.feature.profile.ProfileScreen
import com.project.prayerreminder.feature.profile.about.AboutScreen
import com.project.prayerreminder.feature.splash.SplashScreen
import com.project.prayerreminder.utils.composables.AppSnackbarHost
import com.project.prayerreminder.utils.composables.LocalAppSnackbarHostState

@Composable
fun AppRoute(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route
    val snackbarHostState = remember { SnackbarHostState() }

    val showBottomNavigation = AppScreen.entries.firstOrNull { screen ->
        screen.route == currentScreen
    }?.showBottomNavigation == true

    CompositionLocalProvider(
        LocalAppSnackbarHostState provides snackbarHostState,
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
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
                    enterTransition = {
                        EnterTransition.None
                    },
                    exitTransition = {
                        ExitTransition.None
                    },
                    popEnterTransition = {
                        EnterTransition.None
                    },
                    popExitTransition = {
                        ExitTransition.None
                    },
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
                        HomeScreen(
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    composable(AppScreen.CALENDAR.route) {
                        CalendarScreen(
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    composable(AppScreen.PROFILE.route) {
                        ProfileScreen(
                            modifier = Modifier.fillMaxSize(),
                            onNavigateToAbout = {
                                navController.navigate(AppScreen.ABOUT.route) {
                                    launchSingleTop = true
                                }
                            },
                        )
                    }
                    composable(AppScreen.ABOUT.route) {
                        AboutScreen(
                            modifier = Modifier.fillMaxSize(),
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                        )
                    }
                }
            }
            AppSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .zIndex(1f),
            )
        }
    }
}
