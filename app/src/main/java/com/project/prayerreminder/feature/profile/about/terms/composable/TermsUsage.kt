package com.project.prayerreminder.feature.profile.about.terms.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.FullRoundedShape
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme

private data class TermsUsageItem(
    @param:DrawableRes val icon: Int,
    val title: String,
)

@Composable
fun TermsUsage(
    appName: String,
    modifier: Modifier = Modifier,
) {
    val usageItems = listOf(
        TermsUsageItem(
            icon = R.drawable.ic_clock,
            title = stringResource(R.string.usage_view_prayer_schedules),
        ),
        TermsUsageItem(
            icon = R.drawable.ic_calendar,
            title = stringResource(R.string.usage_manage_islamic_calendar),
        ),
        TermsUsageItem(
            icon = R.drawable.ic_notifications,
            title = stringResource(R.string.usage_receive_prayer_notifications),
        ),
        TermsUsageItem(
            icon = R.drawable.ic_custom_settings,
            title = stringResource(R.string.usage_customize_preferences),
        ),
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            PrayerDimens.StackMedium,
        ),
    ) {
        Text(
            text = stringResource(R.string.application_usage),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
        )

        usageItems.forEach { item ->
            TermsListItem(
                icon = item.icon,
                title = item.title,
                iconContainerColor = MaterialTheme.colorScheme.tertiaryFixed,
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = PrayerDimens.StackSmall,
                ),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = PrayerDimens.Baseline,
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PrayerDimens.StackLarge),
                horizontalArrangement = Arrangement.spacedBy(
                    PrayerDimens.StackMedium,
                ),
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(
                        PrayerDimens.StackSmall,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.prayer_time_accuracy),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge,
                    )

                    Text(
                        text = stringResource(R.string.prayer_time_accuracy_description, appName),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.ic_warning),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(28.dp),
                )
            }
        }
    }
}

@Composable
internal fun TermsListItem(
    @DrawableRes icon: Int,
    title: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    iconContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = PrayerDimens.StackMedium,
                    vertical = PrayerDimens.StackSmall,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                PrayerDimens.StackMedium,
            ),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(FullRoundedShape)
                    .background(iconContainerColor),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                )
            }

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                ),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview(name = "Terms Usage", showBackground = true, widthDp = 412)
@Composable
private fun TermsUsagePreview() {
    PrayerReminderTheme {
        TermsUsage(
            appName = "Prayer Reminder",
            modifier = Modifier.padding(
                PrayerDimens.ScreenMargin,
            ),
        )
    }
}