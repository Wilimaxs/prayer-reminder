package com.project.prayerreminder.feature.calendar.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.feature.calendar.CalendarPersonalScheduleUiState

@Composable
fun CalendarMySchedule(
    schedules: List<CalendarPersonalScheduleUiState>,
    onScheduleClick: (CalendarPersonalScheduleUiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (schedules.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.my_schedule),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        Spacer(modifier = Modifier.height(PrayerDimens.StackMedium))

        Column(
            verticalArrangement = Arrangement.spacedBy(
                PrayerDimens.StackSmall,
            ),
        ) {
            schedules.forEach { schedule ->
                MyScheduleItem(
                    title = schedule.title,
                    time = schedule.time,
                    isReminderEnabled = schedule.isReminderEnabled,
                    onClick = {
                        onScheduleClick(schedule)
                    },
                )
            }
        }
    }
}

@Composable
private fun MyScheduleItem(
    title: String,
    time: String,
    isReminderEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 72.dp)
                .padding(PrayerDimens.StackMedium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_clock),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp),
                    )

                    Spacer(modifier = Modifier.width(PrayerDimens.Baseline))

                    Text(
                        text = time,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            if (isReminderEnabled) {
                Icon(
                    painter = painterResource(R.drawable.ic_notifications),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

@Preview(name = "Calendar My Schedule", showBackground = true, widthDp = 412)
@Composable
private fun CalendarMySchedulePreview() {
    PrayerReminderTheme {
        CalendarMySchedule(
            schedules = listOf(
                CalendarPersonalScheduleUiState(
                    id = 1L,
                    title = "Weekly Study",
                    time = "19:30",
                    isReminderEnabled = true,
                ),
            ),
            onScheduleClick = {},
            modifier = Modifier.padding(PrayerDimens.ScreenMargin),
        )
    }
}
