package com.project.prayerreminder.feature.profile.about.privacy.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme

@Composable
fun PrivacyHeader(
    appName: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = appName,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(PrayerDimens.StackSmall))

        Text(
            text = stringResource(R.string.official_document),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge.copy(
                letterSpacing = 2.sp,
            ),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(PrayerDimens.StackExtraLarge))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp,
            ),
        ) {
            Text(
                text = stringResource(R.string.privacy_introduction, appName),
                modifier = Modifier.padding(
                    PrayerDimens.StackLarge,
                ),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview(name = "Privacy Header", showBackground = true, widthDp = 412)
@Composable
private fun PrivacyHeaderPreview() {
    PrayerReminderTheme {
        PrivacyHeader(
            appName = "Prayer Reminder",
            modifier = Modifier.padding(
                PrayerDimens.ScreenMargin,
            ),
        )
    }
}