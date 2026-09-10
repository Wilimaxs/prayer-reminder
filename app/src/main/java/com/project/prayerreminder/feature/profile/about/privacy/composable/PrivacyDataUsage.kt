package com.project.prayerreminder.feature.profile.about.privacy.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme

@Composable
fun PrivacyDataUsage(
    modifier: Modifier = Modifier,
) {
    val dataUsageItems = listOf(
        stringResource(R.string.use_location_for_prayer_schedule),
        stringResource(R.string.use_local_application_data),
        stringResource(R.string.use_data_for_reminders),
        stringResource(R.string.use_diagnostic_data),
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            PrayerDimens.StackLarge,
        ),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PrayerDimens.StackLarge),
                verticalArrangement = Arrangement.spacedBy(
                    PrayerDimens.StackMedium,
                ),
            ) {
                Text(
                    text = stringResource(R.string.how_we_use_information),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )

                dataUsageItems.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(
                            PrayerDimens.StackMedium,
                        ),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_success),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                        )

                        Text(
                            text = item,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = PrayerDimens.Baseline,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PrayerDimens.StackLarge),
                verticalArrangement = Arrangement.spacedBy(
                    PrayerDimens.StackSmall,
                ),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        PrayerDimens.StackSmall,
                    ),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_privacy_policy),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                    )

                    Text(
                        text = stringResource(R.string.data_storage_and_security),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }

                Text(
                    text = stringResource(R.string.data_storage_and_security_description),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Preview(name = "Privacy Data Usage", showBackground = true, widthDp = 412)
@Composable
private fun PrivacyDataUsagePreview() {
    PrayerReminderTheme {
        PrivacyDataUsage(
            modifier = Modifier.padding(
                PrayerDimens.ScreenMargin,
            ),
        )
    }
}