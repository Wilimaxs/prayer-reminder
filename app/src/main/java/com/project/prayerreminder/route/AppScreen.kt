package com.project.prayerreminder.route

enum class AppScreen(
    val route: String,
    val showBottomNavigation: Boolean
) {
    SPLASH(
        route = "splash",
        showBottomNavigation = false
    ),
    HOME(
        route = "home",
        showBottomNavigation = true
    ),
    CALENDAR(
        route = "calendar",
        showBottomNavigation = true
    ),
    PROFILE(
        route = "profile",
        showBottomNavigation = true
    );

    companion object {
        // Contains all screens at bottom navigation
        val bottomNavigationScreens: List<AppScreen> =
            entries.filter { screen ->
                screen.showBottomNavigation
            }
    }
}