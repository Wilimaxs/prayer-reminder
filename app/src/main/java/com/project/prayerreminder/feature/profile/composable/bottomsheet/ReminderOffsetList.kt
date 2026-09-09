package com.project.prayerreminder.feature.profile.composable.bottomsheet

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.utils.composables.AppSelectionList
import com.project.prayerreminder.utils.composables.AppSelectionOption

@Composable
fun ReminderOffsetList(
    selectedMinutes: Int,
    onSelectedMinutesChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val reminderOffsets = listOf(0, 5, 10, 15, 30, 60)

    AppSelectionList(
        options = reminderOffsets.map { minutes ->
            AppSelectionOption(
                value = minutes,
                title = if (minutes == 0) {
                    stringResource(R.string.at_prayer_time)
                } else {
                    stringResource(R.string.minutes_before, minutes)
                },
            )
        },
        selectedValue = selectedMinutes,
        onSelectedValueChange = onSelectedMinutesChange,
        modifier = modifier,
    )
}

@Preview(name = "Reminder Offset List", showBackground = true, widthDp = 412)
@Composable
private fun ReminderOffsetListPreview() {
    PrayerReminderTheme {
        var selectedMinutes by rememberSaveable {
            mutableIntStateOf(10)
        }

        ReminderOffsetList(
            selectedMinutes = selectedMinutes,
            onSelectedMinutesChange = {
                selectedMinutes = it
            },
            modifier = Modifier.padding(
                PrayerDimens.ScreenMargin,
            ),
        )
    }
}