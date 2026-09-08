package com.project.prayerreminder.feature.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.feature.profile.composable.ProfileHeader

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = PrayerDimens.ScreenMargin,
                    vertical = PrayerDimens.StackLarge,
                ),
        ) {
            ProfileHeader(
                name = "Hamba Tuhan",
                onEditClick = {
                    // TODO: Handle edit when phase UI done
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(
    name = "Profile Screen",
    showBackground = true,
    showSystemUi = true,
    device = "id:pixel_5",
)
@Composable
private fun ProfileScreenPreview() {
    PrayerReminderTheme {
        ProfileScreen(
            modifier = Modifier.fillMaxSize(),
        )
    }
}