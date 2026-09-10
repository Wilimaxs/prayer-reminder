package com.project.prayerreminder.feature.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.feature.calendar.composable.CalendarAddScheduleContent
import com.project.prayerreminder.feature.calendar.composable.CalendarContent
import com.project.prayerreminder.feature.calendar.composable.CalendarIslamicEvent
import com.project.prayerreminder.feature.calendar.composable.CalendarMySchedule
import com.project.prayerreminder.feature.calendar.composable.ReminderListBefore
import com.project.prayerreminder.utils.composables.AppBar
import com.project.prayerreminder.utils.composables.AppBottomSheet
import com.project.prayerreminder.utils.composables.AppButton
import com.project.prayerreminder.utils.composables.BottomSheetButtonConfig
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CalendarScreenContent(
        uiState = uiState,
        onDateSelected = viewModel::selectDate,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarScreenContent(
    uiState: CalendarUiState,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showAddScheduleBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

    var showReminderBeforeBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

    var scheduleTitle by rememberSaveable {
        mutableStateOf("")
    }

    var selectedHour by rememberSaveable {
        mutableIntStateOf(19)
    }

    var selectedMinute by rememberSaveable {
        mutableIntStateOf(30)
    }

    var isReminderEnabled by rememberSaveable {
        mutableStateOf(true)
    }

    var reminderBeforeMinutes by rememberSaveable {
        mutableIntStateOf(30)
    }

    var pendingReminderBeforeMinutes by rememberSaveable {
        mutableIntStateOf(30)
    }

    val selectedTime = LocalTime.of(
        selectedHour,
        selectedMinute,
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            AppBar(
                canNavigateBack = false,
                title = stringResource(R.string.calendar_label)
            )
        },
        floatingActionButton = {
            AppButton(
                text = stringResource(R.string.add_schedule),
                leadingIcon = R.drawable.ic_add,
                onClick = {
                    showAddScheduleBottomSheet = true
                },
                modifier = Modifier.shadow(
                    elevation = 4.dp,
                    shape = MaterialTheme.shapes.extraLarge,
                ),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .padding(horizontal = PrayerDimens.ScreenMargin)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 72.dp),
            verticalArrangement = Arrangement.spacedBy(
                PrayerDimens.StackLarge,
            ),
        ) {
            CalendarContent(
                selectedDate = uiState.selectedDate,
                onDateSelected = onDateSelected,
                modifier = Modifier.fillMaxWidth(),
            )

            CalendarIslamicEvent(
                events = uiState.islamicEvents,
                modifier = Modifier.fillMaxWidth(),
            )
            CalendarMySchedule(
                schedules = uiState.personalSchedules,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
    if (showAddScheduleBottomSheet) {
        AppBottomSheet(
            title = stringResource(R.string.add_schedule),
            subtitle = stringResource(R.string.add_schedule_subtitle),
            showCloseButton = false,
            onDismissRequest = {
                showAddScheduleBottomSheet = false
            },
            secondaryButton = BottomSheetButtonConfig(
                text = stringResource(R.string.cancel),
                onClick = {
                    showAddScheduleBottomSheet = false
                },
            ),
            primaryButton = BottomSheetButtonConfig(
                text = stringResource(R.string.save_schedule),
                onClick = {
                    showAddScheduleBottomSheet = false
                },
            ),
        ) {
            CalendarAddScheduleContent(
                title = scheduleTitle,
                onTitleChange = {
                    scheduleTitle = it
                },
                // Masih static selama fase UI.
                selectedDateText = "21 July 2026",
                selectedTime = selectedTime,
                onTimeChange = {
                    selectedHour = it.hour
                    selectedMinute = it.minute
                },
                isReminderEnabled = isReminderEnabled,
                onReminderEnabledChange = {
                    isReminderEnabled = it
                },
                reminderBeforeText = stringResource(
                    R.string.minutes_before,
                    reminderBeforeMinutes,
                ),
                onReminderBeforeClick = {
                    // Salin pilihan lama agar tombol Cancel tidak mengubah data.
                    pendingReminderBeforeMinutes = reminderBeforeMinutes
                    showReminderBeforeBottomSheet = true
                },
            )
        }
    }
    if (showReminderBeforeBottomSheet) {
        AppBottomSheet(
            title = stringResource(R.string.select_reminder_time),
            subtitle = stringResource(
                R.string.select_reminder_time_subtitle,
            ),
            showCloseButton = false,
            onDismissRequest = {
                showReminderBeforeBottomSheet = false
            },
            secondaryButton = BottomSheetButtonConfig(
                text = stringResource(R.string.cancel),
                onClick = {
                    showReminderBeforeBottomSheet = false
                },
            ),
            primaryButton = BottomSheetButtonConfig(
                text = stringResource(R.string.confirm),
                onClick = {
                    reminderBeforeMinutes =
                        pendingReminderBeforeMinutes

                    showReminderBeforeBottomSheet = false
                },
            ),
        ) {
            ReminderListBefore(
                selectedMinutes = pendingReminderBeforeMinutes,
                onSelectedMinutesChange = {
                    pendingReminderBeforeMinutes = it
                },
            )
        }
    }
}