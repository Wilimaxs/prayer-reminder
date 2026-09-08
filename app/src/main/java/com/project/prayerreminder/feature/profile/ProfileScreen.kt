package com.project.prayerreminder.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.feature.profile.composable.ProfileHeader
import com.project.prayerreminder.feature.profile.composable.ProfileSection
import com.project.prayerreminder.feature.profile.composable.ProfileSettingItem
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
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
            verticalArrangement = Arrangement.spacedBy(
                PrayerDimens.StackLarge,
            ),
        ) {
            ProfileHeader(
                name = uiState.user.name,
                onEditClick = {
                    // TODO: Handle edit when phase UI done
                },
                modifier = Modifier.fillMaxWidth(),
            )
            ProfileSection(
                title = stringResource(R.string.prayer_settings),
                modifier = Modifier.fillMaxWidth(),
            ) {
                ProfileSettingItem(
                    icon = R.drawable.ic_location,
                    title = stringResource(R.string.location),
                    subtitle = uiState.prayerSettings.cityName,
                    onClick = {
                        // TODO: Open location settings.
                    },
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                ProfileSettingItem(
                    icon = R.drawable.ic_gps,
                    title = stringResource(R.string.auto_location),
                    subtitle = stringResource(
                        R.string.auto_location_description,
                    ),
                    trailingContent = {
                        Switch(
                            checked = uiState.prayerSettings.isAutoLocationEnabled,
                            onCheckedChange = viewModel::updateAutoLocation,
                            modifier = Modifier.scale(0.8f)
                        )
                    },
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                ProfileSettingItem(
                    icon = R.drawable.ic_calculation,
                    title = stringResource(R.string.calculation_method),
                    subtitle = when (uiState.prayerSettings.calculationMethod) {
                        PrayerCalculationMethod.KemenagIndonesia ->
                            "Kemenag Indonesia"
                    },
                    onClick = {
                        // TODO: Open calculation method selection.
                    },
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                ProfileSettingItem(
                    icon = R.drawable.ic_madhab,
                    title = stringResource(R.string.madhab_asr),
                    subtitle =   when (uiState.prayerSettings.madhab) {
                        AsrMadhab.Shafi -> "Shafi'i (Standard)"
                        AsrMadhab.Hanafi -> "Hanafi"
                    },
                    onClick = {
                        // TODO: Open madhab selection.
                    },
                )
            }
        }
    }
}