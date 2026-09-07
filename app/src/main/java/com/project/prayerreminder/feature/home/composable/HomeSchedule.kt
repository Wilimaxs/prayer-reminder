package com.project.prayerreminder.feature.home.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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

@Composable
fun HomeSchedule(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.prayer_schedule_today),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
        )
        Spacer(modifier = Modifier.height(PrayerDimens.StackMedium))
        // TODO: dummy list schedule prayer
        Column(
            verticalArrangement = Arrangement.spacedBy(
                PrayerDimens.StackSmall,
            ),
        ) {
            PrayerScheduleItem(
                prayerName = R.string.fajr,
                prayerTime = "04:35",
                prayerIcon = R.drawable.ic_fajr,
                isActive = false,
            )

            PrayerScheduleItem(
                prayerName = R.string.dhuhr,
                prayerTime = "11:58",
                prayerIcon = R.drawable.ic_dhuhr,
                isActive = false,
            )

            PrayerScheduleItem(
                prayerName = R.string.asr,
                prayerTime = "15:21",
                prayerIcon = R.drawable.ic_asr,
                isActive = true,
            )

            PrayerScheduleItem(
                prayerName = R.string.maghrib,
                prayerTime = "17:52",
                prayerIcon = R.drawable.ic_maghrib,
                isActive = false,
            )

            PrayerScheduleItem(
                prayerName = R.string.isha,
                prayerTime = "19:03",
                prayerIcon = R.drawable.ic_isha,
                isActive = false,
            )
        }
    }
}

@Composable
private fun PrayerScheduleItem(
    @StringRes prayerName: Int,
    prayerTime: String,
    @DrawableRes prayerIcon: Int,
    isActive: Boolean,
    modifier: Modifier = Modifier,
) {
    // Calculate the container color
    val containerColor = if (isActive) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerLow
    }
    // Calculate the main content color
    val mainContentColor = if (isActive) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    // Calculate the icon color
    val iconColor = if (isActive) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .padding(
                    horizontal = PrayerDimens.StackMedium,
                    vertical = 14.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(prayerIcon),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp),
            )

            Spacer(
                modifier = Modifier.width(PrayerDimens.StackMedium),
            )

            Text(
                text = stringResource(prayerName),
                color = mainContentColor,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (isActive) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Medium
                    },
                ),
                modifier = Modifier.weight(1f),
            )

            Text(
                text = prayerTime,
                color = mainContentColor,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )

            Spacer(
                modifier = Modifier.width(PrayerDimens.StackLarge),
            )

            Icon(
                painter = painterResource(
                    if (isActive) {
                        R.drawable.ic_notifications_active
                    } else {
                        R.drawable.ic_notifications
                    }
                ),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Preview(showBackground = true, name = "Home Schedule",)
@Composable
private fun HomeSchedulePreview() {
    PrayerReminderTheme {
        HomeSchedule(
            modifier = Modifier.padding(16.dp),
        )
    }
}