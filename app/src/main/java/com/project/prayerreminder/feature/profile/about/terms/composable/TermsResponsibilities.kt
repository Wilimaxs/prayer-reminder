package com.project.prayerreminder.feature.profile.about.terms.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme

private data class TermsResponsibilityItem(
    @param:DrawableRes val icon: Int,
    val title: String,
)

@Composable
fun TermsResponsibilities(
    modifier: Modifier = Modifier,
) {
    val responsibilities = listOf(
        TermsResponsibilityItem(
            icon = R.drawable.ic_mosque,
            title = stringResource(R.string.responsibility_verify_prayer_times),
        ),
        TermsResponsibilityItem(
            icon = R.drawable.ic_location,
            title = stringResource(R.string.responsibility_manage_location_permission),
        ),
        TermsResponsibilityItem(
            icon = R.drawable.ic_info,
            title = stringResource(R.string.responsibility_use_application_appropriately),
        ),
        TermsResponsibilityItem(
            icon = R.drawable.ic_notifications,
            title = stringResource(R.string.responsibility_manage_notifications),
        ),
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            PrayerDimens.StackMedium,
        ),
    ) {
        Text(
            text = stringResource(R.string.user_responsibilities),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Medium,
            ),
        )

        responsibilities.forEach { responsibility ->
            TermsListItem(
                icon = responsibility.icon,
                title = responsibility.title,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                iconContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            )
        }
    }
}

@Preview(name = "Terms Responsibilities", showBackground = true, widthDp = 412)
@Composable
private fun TermsResponsibilitiesPreview() {
    PrayerReminderTheme {
        TermsResponsibilities(
            modifier = Modifier.padding(
                PrayerDimens.ScreenMargin,
            ),
        )
    }
}