package com.project.prayerreminder.route

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.project.prayerreminder.feature.profile.about.licenses.OpenSourceLicensesScreen
import com.project.prayerreminder.feature.profile.about.privacy.PrivacyPolicyScreen
import com.project.prayerreminder.feature.profile.terms.TermsScreen
import com.project.prayerreminder.feature.splash.SplashScreen
import com.project.prayerreminder.feature.splash.SplashResult
import com.project.prayerreminder.R
import com.project.prayerreminder.utils.composables.AppSnackbarType
import com.project.prayerreminder.utils.composables.AppSnackbarHost
import com.project.prayerreminder.utils.composables.LocalAppSnackbarHostState
import com.project.prayerreminder.utils.composables.showAppSnackbar
import kotlinx.coroutines.launch

@Composable
fun AppRoute(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val syncSuccessTitle = stringResource(R.string.splash_sync_success_title)
    val syncSuccessSubtitle = stringResource(R.string.splash_sync_success_description)
    val calendarSyncFailedTitle = stringResource(R.string.splash_calendar_sync_failed_title)
    val calendarSyncFailedSubtitle = stringResource(R.string.splash_calendar_sync_failed_description)

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
                    modifier = Modifier
                        .padding(navigationPadding)
                        .consumeWindowInsets(navigationPadding)
                ) {
                    composable(AppScreen.SPLASH.route) {
                        SplashScreen(
                            modifier = Modifier.fillMaxSize(),
                            onFinished = { result ->
                                navController.navigate(AppScreen.HOME.route) {
                                    popUpTo(AppScreen.SPLASH.route) {
                                        inclusive = true
                                    }

                                    launchSingleTop = true
                                }

                                // Displays synchronization feedback after Home is opened.
                                when (result) {
                                    SplashResult.SyncSuccess -> {
                                        coroutineScope.launch {
                                            snackbarHostState.showAppSnackbar(
                                                title = syncSuccessTitle,
                                                subtitle = syncSuccessSubtitle,
                                                type = AppSnackbarType.SUCCESS,
                                            )
                                        }
                                    }

                                    SplashResult.CalendarSyncFailed -> {
                                        coroutineScope.launch {
                                            snackbarHostState.showAppSnackbar(
                                                title = calendarSyncFailedTitle,
                                                subtitle = calendarSyncFailedSubtitle,
                                                type = AppSnackbarType.ERROR,
                                            )
                                        }
                                    }

                                    SplashResult.CacheReady -> Unit
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
                            onNavigateToTerms = {
                                navController.navigate(
                                    AppScreen.TERMS_OF_SERVICE.route,
                                ) {
                                    launchSingleTop = true
                                }
                            },
                            onNavigateToPrivacyPolicy = {
                                navController.navigate(
                                    AppScreen.PRIVACY_POLICY.route,
                                ) {
                                    launchSingleTop = true
                                }
                            },
                            onNavigateToOpenSourceLicenses = {
                                navController.navigate(
                                    AppScreen.OPEN_SOURCE_LICENSES.route,
                                ) {
                                    launchSingleTop = true
                                }
                            },
                        )
                    }
                    composable(AppScreen.TERMS_OF_SERVICE.route) {
                        TermsScreen(
                            modifier = Modifier.fillMaxSize(),
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                        )
                    }
                    composable(AppScreen.OPEN_SOURCE_LICENSES.route) {
                        OpenSourceLicensesScreen(
                            modifier = Modifier.fillMaxSize(),
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                        )
                    }
                    composable(AppScreen.PRIVACY_POLICY.route) {
                        PrivacyPolicyScreen(
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
