package com.project.prayerreminder.feature.profile.about.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.feature.profile.composable.ProfileSettingItem

private data class LegalInformationItem(
    @param:DrawableRes val icon: Int,
    val title: String,
    val onClick: () -> Unit,
)

@Composable
fun AboutLegalInformation(
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit,
    onOpenSourceLicensesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val legalItems = listOf(
        LegalInformationItem(
            icon = R.drawable.ic_privacy_policy,
            title = stringResource(R.string.privacy_policy),
            onClick = onPrivacyPolicyClick,
        ),
        LegalInformationItem(
            icon = R.drawable.ic_terms_of_service,
            title = stringResource(R.string.terms_of_service),
            onClick = onTermsOfServiceClick,
        ),
        LegalInformationItem(
            icon = R.drawable.ic_open_source_licenses,
            title = stringResource(R.string.open_source_licenses),
            onClick = onOpenSourceLicensesClick,
        ),
    )

    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.legal_and_information),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = PrayerDimens.StackMedium,
                ),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp,
            ),
        ) {
            Column {
                legalItems.forEachIndexed { index, item ->
                    ProfileSettingItem(
                        icon = item.icon,
                        title = item.title,
                        subtitle = null,
                        onClick = item.onClick,
                    )

                    if (index < legalItems.lastIndex) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    name = "About Legal Information",
    showBackground = true,
    widthDp = 412,
)
@Composable
private fun AboutLegalInformationPreview() {
    PrayerReminderTheme {
        AboutLegalInformation(
            onPrivacyPolicyClick = {},
            onTermsOfServiceClick = {},
            onOpenSourceLicensesClick = {},
            modifier = Modifier.padding(
                PrayerDimens.ScreenMargin,
            ),
        )
    }
}