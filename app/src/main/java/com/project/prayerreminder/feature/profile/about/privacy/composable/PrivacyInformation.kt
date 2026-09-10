package com.project.prayerreminder.feature.profile.about.privacy.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.FullRoundedShape
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme

private data class PrivacyInformationItem(
    @param:DrawableRes val icon: Int,
    val title: String,
    val description: String,
)

@Composable
fun PrivacyInformation(
    modifier: Modifier = Modifier,
) {
    val informationItems = listOf(
        PrivacyInformationItem(
            icon = R.drawable.ic_location,
            title = stringResource(R.string.location_data),
            description = stringResource(R.string.location_data_privacy_description),
        ),
        PrivacyInformationItem(
            icon = R.drawable.ic_custom_settings,
            title = stringResource(R.string.application_preferences),
            description = stringResource(R.string.application_preferences_privacy_description),
        ),
        PrivacyInformationItem(
            icon = R.drawable.ic_calendar,
            title = stringResource(R.string.personal_schedule_data),
            description = stringResource(R.string.personal_schedule_data_description),
        ),
        PrivacyInformationItem(
            icon = R.drawable.ic_info,
            title = stringResource(R.string.diagnostic_performance_data),
            description = stringResource(R.string.diagnostic_performance_data_description),
        ),
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            PrayerDimens.StackMedium,
        ),
    ) {
        Text(
            text = stringResource(R.string.information_we_process),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        informationItems.forEach { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
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
                        PrayerDimens.StackSmall,
                    ),
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(FullRoundedShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    Text(
                        text = item.title,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )

                    Text(
                        text = item.description,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Preview(name = "Privacy Information", showBackground = true, widthDp = 412)
@Composable
private fun PrivacyInformationPreview() {
    PrayerReminderTheme {
        PrivacyInformation(
            modifier = Modifier.padding(
                PrayerDimens.ScreenMargin,
            ),
        )
    }
}