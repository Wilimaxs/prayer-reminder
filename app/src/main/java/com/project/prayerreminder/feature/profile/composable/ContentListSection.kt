package com.project.prayerreminder.feature.profile.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.feature.profile.AsrMadhab
import com.project.prayerreminder.feature.profile.PrayerCalculationMethod
import com.project.prayerreminder.feature.profile.ProfileUiState

// Identifies which profile setting item was selected.
enum class ProfileSettingAction {
    CALCULATION_METHOD,
    MADHAB,
    REMINDER_OFFSET,
    ADZAN_SOUND,
    LANGUAGE,
    ABOUT_APPLICATION,
}

// Represents one section displayed on the Profile screen.
private data class ProfileSectionUiModel(
    val title: String,
    val items: List<ProfileSettingUiModel>,
)

// Represents one setting item inside a Profile section.
private data class ProfileSettingUiModel(
    @param:DrawableRes val icon: Int,
    val title: String,
    val subtitle: String?,
    val trailing: ProfileTrailingUiModel = ProfileTrailingUiModel.Arrow,
    val action: ProfileSettingAction? = null,
)

// Represents the content displayed on the right side of an item.
private sealed interface ProfileTrailingUiModel {

    data object Arrow : ProfileTrailingUiModel

    data class Toggle(
        val checked: Boolean,
        val onCheckedChange: (Boolean) -> Unit,
    ) : ProfileTrailingUiModel

    data class TextWithArrow(
        val text: String,
    ) : ProfileTrailingUiModel
}

@Composable
fun ContentListSection(
    uiState: ProfileUiState,
    onPrayerRemindersChange: (Boolean) -> Unit,
    onItemClick: (ProfileSettingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sections = listOf(
        ProfileSectionUiModel(
            title = stringResource(R.string.prayer_settings),
            items = listOf(
                ProfileSettingUiModel(
                    icon = R.drawable.ic_calculation,
                    title = stringResource(R.string.calculation_method),
                    subtitle = when (
                        uiState.prayerSettings.calculationMethod
                    ) {
                        PrayerCalculationMethod.KemenagIndonesia -> {
                            stringResource(R.string.kemenag_indonesia)
                        }

                        PrayerCalculationMethod.MuslimWorldLeague -> {
                            stringResource(R.string.muslim_world_league)
                        }

                        PrayerCalculationMethod.JakimMalaysia -> {
                            stringResource(R.string.jakim_malaysia)
                        }
                    },
                    action = ProfileSettingAction.CALCULATION_METHOD,
                ),
                ProfileSettingUiModel(
                    icon = R.drawable.ic_madhab,
                    title = stringResource(R.string.madhab_asr),
                    subtitle = when (
                        uiState.prayerSettings.madhab
                    ) {
                        AsrMadhab.Shafi -> stringResource(R.string.shafi_standard)
                        AsrMadhab.Hanafi -> stringResource(R.string.hanafi)
                    },
                    action = ProfileSettingAction.MADHAB,
                ),
            ),
        ),
        ProfileSectionUiModel(
            title = stringResource(R.string.notifications),
            items = listOf(
                ProfileSettingUiModel(
                    icon = R.drawable.ic_reminder,
                    title = stringResource(R.string.prayer_reminders),
                    subtitle = stringResource(R.string.prayer_reminders_description),
                    trailing = ProfileTrailingUiModel.Toggle(
                        checked = uiState.notificationSettings.isPrayerRemindersEnabled,
                        onCheckedChange = onPrayerRemindersChange,
                    ),
                ),
                ProfileSettingUiModel(
                    icon = R.drawable.ic_clock,
                    title = stringResource(R.string.reminder_offset),
                    subtitle = stringResource(
                        R.string.minutes_before,
                        uiState.notificationSettings.reminderOffsetMinutes,
                    ),
                    action = if (
                        uiState.notificationSettings.isPrayerRemindersEnabled
                    ) {
                        ProfileSettingAction.REMINDER_OFFSET
                    } else {
                        null
                    }
                ),
                ProfileSettingUiModel(
                    icon = R.drawable.ic_adzan,
                    title = stringResource(R.string.adzan_sound),
                    subtitle = stringResource(R.string.default_value),
                    action = ProfileSettingAction.ADZAN_SOUND,
                ),
            ),
        ),
        ProfileSectionUiModel(
            title = stringResource(R.string.appearance),
            items = listOf(
                ProfileSettingUiModel(
                    icon = R.drawable.ic_language,
                    title = stringResource(R.string.language),
                    subtitle = stringResource(R.string.language_description),
                    trailing = ProfileTrailingUiModel.TextWithArrow(text = stringResource(R.string.english)),
                    action = ProfileSettingAction.LANGUAGE,
                ),
            ),
        ),
        ProfileSectionUiModel(
            title = stringResource(R.string.application),
            items = listOf(
                ProfileSettingUiModel(
                    icon = R.drawable.ic_about,
                    title = stringResource(R.string.about_application),
                    subtitle = null,
                    action = ProfileSettingAction.ABOUT_APPLICATION,
                ),
            ),
        ),
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            PrayerDimens.StackLarge,
        ),
    ) {
        sections.forEach { section ->
            ProfileSection(
                title = section.title,
                modifier = Modifier.fillMaxWidth(),
            ) {
                section.items.forEachIndexed { index, item ->
                    ProfileSettingItem(
                        icon = item.icon,
                        title = item.title,
                        subtitle = item.subtitle,
                        onClick = item.action?.let { action ->
                            {
                                onItemClick(action)
                            }
                        },
                        trailingContent = {
                            when (val trailing = item.trailing) {
                                ProfileTrailingUiModel.Arrow -> {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_arrow_right),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp),
                                    )
                                }

                                is ProfileTrailingUiModel.Toggle -> {
                                    Switch(
                                        checked = trailing.checked,
                                        onCheckedChange = trailing.onCheckedChange,
                                        modifier = Modifier.scale(0.8f),
                                    )
                                }

                                is ProfileTrailingUiModel.TextWithArrow -> {
                                    Text(
                                        text = trailing.text,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.bodySmall,
                                    )

                                    Spacer(modifier = Modifier.width(PrayerDimens.StackSmall))

                                    Icon(
                                        painter = painterResource(R.drawable.ic_arrow_right),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp),
                                    )
                                }
                            }
                        },
                    )
                    if (index < section.items.lastIndex) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                        )
                    }
                }
            }
        }
    }
}