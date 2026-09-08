package com.project.prayerreminder.feature.calendar.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.utils.composables.AppFormField
import com.project.prayerreminder.utils.composables.AppTimePickerField
import java.time.LocalTime

@Composable
fun CalendarAddScheduleContent(
    title: String,
    onTitleChange: (String) -> Unit,
    selectedDateText: String,
    selectedTime: LocalTime,
    onTimeChange: (LocalTime) -> Unit,
    isReminderEnabled: Boolean,
    onReminderEnabledChange: (Boolean) -> Unit,
    reminderBeforeText: String,
    onReminderBeforeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            PrayerDimens.StackMedium,
        ),
    ) {
        AppFormField(
            value = title,
            onValueChange = onTitleChange,
            label = stringResource(R.string.schedule_title),
            placeholder = stringResource(
                R.string.schedule_title_placeholder,
            ),
            isRequired = true,
            maxLength = 50,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                },
            ),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                PrayerDimens.StackSmall,
            ),
            verticalAlignment = Alignment.Top,
        ) {
            AppFormField(
                value = selectedDateText,
                onValueChange = {},
                label = stringResource(R.string.date),
                enabled = false,
                modifier = Modifier.weight(1f),
            )

            AppTimePickerField(
                value = selectedTime,
                onValueChange = onTimeChange,
                label = stringResource(R.string.time),
                isRequired = true,
                modifier = Modifier.weight(1f),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.reminder),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
            )

            Switch(
                checked = isReminderEnabled,
                onCheckedChange = onReminderEnabledChange,
            )
        }

        if (isReminderEnabled) {
            AppFormField(
                value = reminderBeforeText,
                onValueChange = {},
                label = stringResource(R.string.remind_before),
                onClick = onReminderBeforeClick,
            )
        }
    }
}

// Reminder List Before for selected minutes
@Composable
fun ReminderListBefore(
    selectedMinutes: Int,
    onSelectedMinutesChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val reminderOptions = listOf(
        5,
        10,
        15,
        30,
        60,
    )

    Column(
        modifier = modifier,
    ) {
        reminderOptions.forEach { minutes ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .clickable(
                        role = Role.RadioButton,
                        onClick = {
                            onSelectedMinutesChange(minutes)
                        },
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(
                        R.string.minutes_before,
                        minutes,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                )

                RadioButton(
                    selected = selectedMinutes == minutes,
                    onClick = null,
                )
            }
        }
    }
}

@Preview(name = "Calendar Add Schedule", showBackground = true, device = "id:pixel_4")
@Composable
private fun CalendarAddScheduleContentPreview() {
    PrayerReminderTheme {
        var title by remember {
            mutableStateOf("")
        }

        var selectedTime by remember {
            mutableStateOf(
                LocalTime.of(19, 30),
            )
        }

        var isReminderEnabled by remember {
            mutableStateOf(true)
        }

        CalendarAddScheduleContent(
            title = title,
            onTitleChange = {
                title = it
            },
            selectedDateText = "21 July 2026",
            selectedTime = selectedTime,
            onTimeChange = {
                selectedTime = it
            },
            isReminderEnabled = isReminderEnabled,
            onReminderEnabledChange = {
                isReminderEnabled = it
            },
            reminderBeforeText = "30 minutes before",
            onReminderBeforeClick = {},
            modifier = Modifier.padding(
                PrayerDimens.ScreenMargin,
            ),
        )
    }
}