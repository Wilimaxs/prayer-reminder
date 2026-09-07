package com.project.prayerreminder.feature.calendar.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme

@Composable
fun CalendarIslamicEvent(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    // visibility if there are no any events in this date
    if (!isVisible) return

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.islamic_events),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        Spacer(
            modifier = Modifier.height(PrayerDimens.StackMedium),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(
                PrayerDimens.StackSmall,
            ),
        ) {
            IslamicEventItem(
                title = "Islamic New Year",
                hijriDate = "1 Muharram 1448 H",
            )

            IslamicEventItem(
                title = "Prophet Muhammad’s Birthday",
                hijriDate = "12 Rabi' al-Awwal 1448 H",
            )
        }
    }
}

@Composable
private fun IslamicEventItem(
    title: String,
    hijriDate: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .padding(PrayerDimens.StackMedium),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )

            Text(
                text = hijriDate,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Preview(name = "Calendar Islamic Event", showBackground = true)
@Composable
private fun CalendarIslamicEventPreview() {
    PrayerReminderTheme {
        CalendarIslamicEvent(
            isVisible = true,
            modifier = Modifier.padding(PrayerDimens.ScreenMargin),
        )
    }
}