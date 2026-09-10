package com.project.prayerreminder.feature.home.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.project.prayerreminder.core.theme.PrayerShapes
import com.project.prayerreminder.feature.home.HomeNextPrayerUiState
import com.project.prayerreminder.feature.home.HomePrayer
import com.project.prayerreminder.utils.composables.AppBadge

@Composable
fun HomeCard(
    nextPrayer: HomeNextPrayerUiState?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.img_decorative_home),
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopEnd)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PrayerDimens.ScreenMargin)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.next_prayer),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Medium,
                            )
                        )
                        Spacer(modifier = Modifier.height(PrayerDimens.Baseline))
                        Text(
                            text = nextPrayer?.let { prayer ->
                                stringResource(prayer.prayer.nameRes)
                            }.orEmpty(),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    AppBadge(
                        icon = painterResource(R.drawable.ic_mosque),
                        iconTint = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                        containerPadding = PrayerDimens.StackSmall,
                        shapes = PrayerShapes.extraLarge,
                        iconSize = PrayerDimens.StackExtraLarge
                    )
                }
                Spacer(modifier = Modifier.height(PrayerDimens.StackExtraLarge))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = nextPrayer?.time ?: "--:--",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    )
                    Spacer(modifier = Modifier.width(PrayerDimens.StackSmall))
                    nextPrayer?.let { prayer ->
                        AppBadge(
                            icon = painterResource(R.drawable.ic_clock),
                            iconTint = MaterialTheme.colorScheme.primary,
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                            containerPadding = PrayerDimens.StackSmall,
                            shapes = PrayerShapes.small,
                            text = stringResource(
                                R.string.minutes_left,
                                prayer.remainingMinutes,
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Home Card", device = "id:pixel_5", showSystemUi = true)
@Composable
private fun HomeCardPreview() {
    PrayerReminderTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            HomeCard(
                nextPrayer = HomeNextPrayerUiState(
                    prayer = HomePrayer.Maghrib,
                    time = "17:52",
                    remainingMinutes = 12,
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
