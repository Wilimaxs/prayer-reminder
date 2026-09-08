package com.project.prayerreminder.feature.profile.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme

@Composable
fun ProfileSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier.padding(
                horizontal = PrayerDimens.StackSmall,
            ),
        )

        Spacer(modifier = Modifier.height(PrayerDimens.StackMedium))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceContainerLow,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp,
            ),
        ) {
            Column(
                content = content,
            )
        }
    }
}

@Preview(
    name = "Profile Section",
    showBackground = true,
    widthDp = 412,
)
@Composable
private fun ProfileSectionPreview() {
    PrayerReminderTheme {
        ProfileSection(
            title = "PRAYER SETTINGS",
            modifier = Modifier.padding(
                PrayerDimens.ScreenMargin,
            ),
        ) {
            ProfileSettingItem(
                icon = R.drawable.ic_location,
                title = "Location",
                subtitle = "Bandung, Indonesia",
                onClick = {},
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
            )

            ProfileSettingItem(
                icon = R.drawable.ic_profile,
                title = "Calculation Method",
                subtitle = "Kemenag Indonesia",
                onClick = {},
            )
        }
    }
}