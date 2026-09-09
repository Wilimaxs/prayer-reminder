package com.project.prayerreminder.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.feature.profile.composable.ContentListSection
import com.project.prayerreminder.feature.profile.composable.ProfileHeader
import com.project.prayerreminder.feature.profile.composable.ProfileSettingAction
import com.project.prayerreminder.feature.profile.composable.bottomsheet.CalculationMethodList
import com.project.prayerreminder.feature.profile.composable.bottomsheet.LanguageList
import com.project.prayerreminder.feature.profile.composable.bottomsheet.MadhabList
import com.project.prayerreminder.feature.profile.composable.bottomsheet.ReminderOffsetList
import com.project.prayerreminder.utils.composables.AppBottomSheet
import com.project.prayerreminder.utils.composables.BottomSheetButtonConfig

@OptIn(ExperimentalMaterial3Api::class)
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
                onPrayerRemindersChange = viewModel::updatePrayerReminders,
                onItemClick = { action ->
                    when (action) {

                        ProfileSettingAction.CALCULATION_METHOD -> {
                            viewModel.openBottomSheet(
                                ProfileBottomSheetType.CalculationMethod,
                            )
                        }

                        ProfileSettingAction.MADHAB -> {
                            viewModel.openBottomSheet(
                                ProfileBottomSheetType.Madhab,
                            )
                        }

                        ProfileSettingAction.REMINDER_OFFSET -> {
                            viewModel.openBottomSheet(
                                ProfileBottomSheetType.ReminderOffset,
                            )
                        }

                        ProfileSettingAction.LANGUAGE -> {
                            viewModel.openBottomSheet(
                                ProfileBottomSheetType.Language,
                            )
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
    when (uiState.bottomSheet.activeBottomSheet) {
        ProfileBottomSheetType.CalculationMethod -> {
            AppBottomSheet(
                title = stringResource(R.string.select_calculation_method),
                subtitle = stringResource(R.string.select_calculation_method_description),
                showCloseButton = false,
                onDismissRequest = viewModel::dismissBottomSheet,
                primaryButton = BottomSheetButtonConfig(
                    text = stringResource(R.string.save),
                    onClick = viewModel::confirmBottomSheet,
                ),
            ) {
                CalculationMethodList(
                    selectedMethod = uiState.bottomSheet.selectedCalculationMethod,
                    onSelectedMethodChange = viewModel::selectCalculationMethod,
                )
            }
        }

        ProfileBottomSheetType.Madhab -> {
            AppBottomSheet(
                title = stringResource(R.string.select_madhab),
                subtitle = stringResource(R.string.select_madhab_description),
                showCloseButton = false,
                onDismissRequest = viewModel::dismissBottomSheet,
                primaryButton = BottomSheetButtonConfig(
                    text = stringResource(R.string.save),
                    onClick = viewModel::confirmBottomSheet,
                ),
            ) {
                MadhabList(
                    selectedMadhab = uiState.bottomSheet.selectedMadhab,
                    onSelectedMadhabChange = viewModel::selectMadhab,
                )
            }
        }

        ProfileBottomSheetType.ReminderOffset -> {
            AppBottomSheet(
                title = stringResource(R.string.select_reminder_offset),
                subtitle = stringResource(R.string.select_reminder_offset_description),
                showCloseButton = false,
                onDismissRequest = viewModel::dismissBottomSheet,
                primaryButton = BottomSheetButtonConfig(
                    text = stringResource(R.string.save),
                    onClick = viewModel::confirmBottomSheet,
                ),
            ) {
                ReminderOffsetList(
                    selectedMinutes = uiState.bottomSheet.selectedReminderOffsetMinutes,
                    onSelectedMinutesChange = viewModel::selectReminderOffset,
                )
            }
        }

        ProfileBottomSheetType.Language -> {
            AppBottomSheet(
                title = stringResource(R.string.select_language),
                subtitle = stringResource(R.string.select_language_description),
                showCloseButton = false,
                onDismissRequest = viewModel::dismissBottomSheet,
                primaryButton = BottomSheetButtonConfig(
                    text = stringResource(R.string.save),
                    onClick = viewModel::confirmBottomSheet,
                ),
            ) {
                LanguageList(
                    selectedLanguage = uiState.bottomSheet.selectedLanguage,
                    onSelectedLanguageChange = viewModel::selectLanguage,
                )
            }
        }

        null -> Unit
    }
}