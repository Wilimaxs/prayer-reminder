package com.project.prayerreminder.route

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.project.prayerreminder.R

enum class AppScreen(
    val route: String,
    val showBottomNavigation: Boolean,
    @param:StringRes val label: Int? = null,
    @param:DrawableRes val icon: Int? = null
) {
    SPLASH(
        route = "splash",
        showBottomNavigation = false
    ),
    HOME(
        route = "home",
        showBottomNavigation = true,
        label = R.string.home_label,
        icon = R.drawable.ic_home
    ),
    CALENDAR(
        route = "calendar",
        showBottomNavigation = true,
        label = R.string.calendar_label,
        icon = R.drawable.ic_calendar,
    ),
    PROFILE(
        route = "profile",
        showBottomNavigation = true,
        label = R.string.profile_label,
        icon = R.drawable.ic_profile,
    );

    companion object {
        // Contains all screens at bottom navigation
        val bottomNavigationScreens: List<AppScreen> =
            entries.filter { screen ->
                screen.showBottomNavigation
            }
    }
}