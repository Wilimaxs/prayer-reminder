package com.project.prayerreminder.feature.main

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.route.AppScreen

@Composable
fun MainBottomNavigationScreen(
    currentScreen: String?,
    onItemClick: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 0.dp
    ) {
        AppScreen.bottomNavigationScreens.forEach { screen ->
            val icon = screen.icon
            val label = screen.label
            val isSelected = currentScreen == screen.route

            if (icon != null && label != null) {
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            onItemClick(screen)
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = stringResource(id = label)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = label)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor =
                            MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedTextColor =
                            MaterialTheme.colorScheme.onSurface,
                        indicatorColor =
                            MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor =
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor =
                            MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                )
            }
        }
    }
}