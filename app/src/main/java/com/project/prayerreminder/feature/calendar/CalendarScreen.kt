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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.feature.calendar.composable.CalendarContent
import com.project.prayerreminder.feature.calendar.composable.CalendarIslamicEvent
import com.project.prayerreminder.feature.calendar.composable.CalendarMySchedule
import com.project.prayerreminder.utils.composables.AppBar
import com.project.prayerreminder.utils.composables.AppBottomSheet
import com.project.prayerreminder.utils.composables.AppButton
import com.project.prayerreminder.utils.composables.BottomSheetButtonConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
) {
    var showAddScheduleBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
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
                modifier = Modifier.fillMaxWidth(),
            )

            CalendarIslamicEvent(
                isVisible = true,
                modifier = Modifier.fillMaxWidth(),
            )
            CalendarMySchedule(
                isVisible = true,
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
            Text(
                text = "Add Schedule form will be placed here.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Calendar", device = "id:pixel_5")
@Composable
private fun CalendarScreenPreview() {
    PrayerReminderTheme {
        CalendarScreen(modifier = Modifier)
    }
}