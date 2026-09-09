package com.project.prayerreminder.feature.profile.about.terms.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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

private data class ThirdPartyItem(
    @param:DrawableRes val icon: Int,
    val title: String,
    val description: String,
)

@Composable
fun TermsThirdParty(
    appName: String,
    modifier: Modifier = Modifier,
) {
    val services = listOf(
        ThirdPartyItem(
            icon = R.drawable.ic_calculation,
            title = stringResource(R.string.aladhan_api),
            description = stringResource(
                R.string.aladhan_api_description,
            ),
        ),
        ThirdPartyItem(
            icon = R.drawable.ic_info,
            title = stringResource(R.string.firebase),
            description = stringResource(
                R.string.firebase_description,
            ),
        ),
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            PrayerDimens.StackMedium,
        ),
    ) {
        Text(
            text = stringResource(R.string.third_party_services),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
        )

        services.forEach { service ->
            ThirdPartyServiceCard(
                icon = service.icon,
                title = service.title,
                description = service.description,
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = PrayerDimens.StackSmall,
                ),
            shape = MaterialTheme.shapes.large,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.errorContainer,
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp,
            ),
        ) {
            Column(
                modifier = Modifier.padding(
                    PrayerDimens.StackLarge,
                ),
                verticalArrangement = Arrangement.spacedBy(
                    PrayerDimens.StackSmall,
                ),
            ) {
                Text(
                    text = stringResource(R.string.limitation_of_liability),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = stringResource(R.string.limitation_of_liability_description, appName),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = PrayerDimens.StackSmall,
            ),
            color = MaterialTheme.colorScheme.outlineVariant,
        )

        Text(
            text = stringResource(R.string.changes_to_application),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            text = stringResource(R.string.changes_to_application_description),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun ThirdPartyServiceCard(
    @DrawableRes icon: Int,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colorScheme.primary,
                    ),
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(PrayerDimens.StackMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    PrayerDimens.StackMedium,
                ),
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp),
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(
                        PrayerDimens.Baseline,
                    ),
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )

                    Text(
                        text = description,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Preview(name = "Terms Third Party", showBackground = true, widthDp = 412)
@Composable
private fun TermsThirdPartyPreview() {
    PrayerReminderTheme {
        TermsThirdParty(
            appName = "Prayer Reminder",
            modifier = Modifier.padding(
                PrayerDimens.ScreenMargin,
            ),
        )
    }
}