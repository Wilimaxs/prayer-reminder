package com.project.prayerreminder.feature.profile.about.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.FullRoundedShape
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme

private data class AboutFeatureItem(
    @param:DrawableRes val icon: Int,
    val title: String,
    val description: String,
)

@Composable
fun AboutFeature(
    modifier: Modifier = Modifier,
) {
    val features = listOf(
        AboutFeatureItem(
            icon = R.drawable.ic_prayer_schedule,
            title = stringResource(R.string.about_feature_prayer_schedule),
            description = stringResource(R.string.about_feature_prayer_schedule_description),
        ),
        AboutFeatureItem(
            icon = R.drawable.ic_calendar,
            title = stringResource(R.string.about_feature_islamic_calendar),
            description = stringResource(R.string.about_feature_islamic_calendar_description),
        ),
        AboutFeatureItem(
            icon = R.drawable.ic_notifications_active,
            title = stringResource(R.string.about_feature_prayer_reminder),
            description = stringResource(R.string.about_feature_prayer_reminder_description),
        ),
        AboutFeatureItem(
            icon = R.drawable.ic_custom_settings,
            title = stringResource(R.string.about_feature_custom_settings),
            description = stringResource(R.string.about_feature_custom_settings_description),
        ),
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            PrayerDimens.StackMedium,
        ),
    ) {
        Text(
            text = stringResource(R.string.features),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
        )
        features.chunked(2).forEach { rowFeatures ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(
                    PrayerDimens.StackSmall,
                ),
            ) {
                rowFeatures.forEach { feature ->
                    AboutFeatureCard(
                        icon = painterResource(feature.icon),
                        title = feature.title,
                        description = feature.description,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )
                }
            }
        }
    }
}

@Composable
private fun AboutFeatureCard(
    icon: Painter,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
        ),
    ) {
        Column(
            modifier = Modifier.padding(
                PrayerDimens.StackMedium,
            ),
        ) {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )

            Spacer(modifier = Modifier.height(PrayerDimens.StackSmall))

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )

            Spacer(modifier = Modifier.height(PrayerDimens.Baseline))

            Text(
                text = description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun AboutDeveloper(
    developerImage: Painter,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            PrayerDimens.StackMedium,
        ),
    ) {
        Text(
            text = stringResource(R.string.developer),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp,
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PrayerDimens.StackMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    PrayerDimens.StackMedium,
                ),
            ) {
                Image(
                    painter = developerImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(FullRoundedShape)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = FullRoundedShape,
                        ),
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        PrayerDimens.Baseline,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.developed_by),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )

                    Text(
                        text = stringResource(R.string.android_developer),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@Preview(name = "About Feature", showBackground = true, widthDp = 412)
@Composable
private fun AboutFeaturePreview() {
    PrayerReminderTheme {
        Column(
            modifier = Modifier.padding(
                PrayerDimens.ScreenMargin,
            ),
            verticalArrangement = Arrangement.spacedBy(
                PrayerDimens.StackLarge,
            ),
        ) {
            AboutFeature()

            AboutDeveloper(
                developerImage = painterResource(
                    R.drawable.img_developer,
                ),
            )
        }
    }
}