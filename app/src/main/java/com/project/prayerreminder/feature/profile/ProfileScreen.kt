package com.project.prayerreminder.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.feature.profile.composable.ContentListSection
import com.project.prayerreminder.feature.profile.composable.ProfileHeader
import com.project.prayerreminder.feature.profile.composable.ProfileSettingAction

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            verticalArrangement = Arrangement.spacedBy(PrayerDimens.StackLarge),
        ) {
            ProfileHeader(
                name = uiState.user.name,
                onEditClick = {
                    // TODO: Open profile picture selector.
                },
                modifier = Modifier.fillMaxWidth(),
            )

            ContentListSection(
                uiState = uiState,
                onAutoLocationChange = viewModel::updateAutoLocation,
                onPrayerRemindersChange = viewModel::updatePrayerReminders,
                onItemClick = { action ->
                    when (action) {
                        ProfileSettingAction.LOCATION -> {
                            // TODO: Open location settings.
                        }

                        ProfileSettingAction.CALCULATION_METHOD -> {
                            // TODO: Open calculation method selection.
                        }

                        ProfileSettingAction.MADHAB -> {
                            // TODO: Open madhab selection.
                        }

                        ProfileSettingAction.REMINDER_OFFSET -> {
                            // TODO: Open reminder offset bottom sheet.
                        }

                        ProfileSettingAction.ADZAN_SOUND -> {
                            // TODO: Open adzan sound selection.
                        }

                        ProfileSettingAction.LANGUAGE -> {
                            // TODO: Open language selection.
                        }

                        ProfileSettingAction.ABOUT_APPLICATION -> {
                            // TODO: Open about application.
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}