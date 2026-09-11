package com.project.prayerreminder.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.project.prayerreminder.utils.composables.AppButton
import com.project.prayerreminder.utils.composables.AppSnackbarType
import com.project.prayerreminder.utils.composables.BottomSheetButtonConfig
import com.project.prayerreminder.utils.composables.LocalAppSnackbarHostState
import com.project.prayerreminder.utils.composables.showAppSnackbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateToAbout: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalAppSnackbarHostState.current
    val snackbarTitle = when (uiState.message) {
        ProfileMessage.PrayerSettingSyncSuccess -> {
            stringResource(R.string.prayer_setting_updated_title)
        }

        ProfileMessage.PrayerSettingSyncFailed -> {
            stringResource(R.string.prayer_setting_update_failed_title)
        }

        ProfileMessage.LoadSettingsFailed -> {
            stringResource(R.string.profile_settings_load_failed_title)
        }

        ProfileMessage.SaveSettingFailed -> {
            stringResource(R.string.profile_setting_save_failed_title)
        }

        null -> ""
    }
    val snackbarSubtitle = when (uiState.message) {
        ProfileMessage.PrayerSettingSyncSuccess -> {
            stringResource(R.string.prayer_setting_updated_description)
        }

        ProfileMessage.PrayerSettingSyncFailed -> {
            stringResource(R.string.prayer_setting_update_failed_description)
        }

        ProfileMessage.LoadSettingsFailed,
        ProfileMessage.SaveSettingFailed,
        null,
            -> null
    }

    // Displays the latest Profile operation result at once.
    LaunchedEffect(uiState.message) {
        val message = uiState.message ?: return@LaunchedEffect

        snackbarHostState.showAppSnackbar(
            title = snackbarTitle,
            subtitle = snackbarSubtitle,
            type = if (message == ProfileMessage.PrayerSettingSyncSuccess) {
                AppSnackbarType.SUCCESS
            } else {
                AppSnackbarType.ERROR
            },
        )
        viewModel.clearMessage()
    }

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
                            onNavigateToAbout()
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
                    enabled = !uiState.isPrayerSettingSyncing,
                    isLoading = uiState.isPrayerSettingSyncing,
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
                    enabled = !uiState.isPrayerSettingSyncing,
                    isLoading = uiState.isPrayerSettingSyncing,
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

    uiState.changeLimitRemainingMinutes?.let { remainingMinutes ->
        AlertDialog(
            onDismissRequest = viewModel::dismissChangeLimitDialog,
            title = {
                Text(text = stringResource(R.string.prayer_setting_change_limit_title))
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.prayer_setting_change_limit_description,
                        remainingMinutes,
                    ),
                )
            },
            confirmButton = {
                AppButton(
                    text = stringResource(R.string.confirm),
                    onClick = viewModel::dismissChangeLimitDialog,
                )
            },
        )
    }
}
