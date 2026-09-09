package com.project.prayerreminder.feature.profile.about.licenses.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens

private data class OpenSourceLicenseItem(
    @param:StringRes val name: Int,
    @param:StringRes val license: Int,
)

@Composable
fun OpenSourceLicensesContent(
    appName: String,
    modifier: Modifier = Modifier,
) {
    val licenses = listOf(
        OpenSourceLicenseItem(
            name = R.string.license_jetpack_compose,
            license = R.string.apache_license_2_0,
        ),
        OpenSourceLicenseItem(
            name = R.string.license_room_datastore,
            license = R.string.apache_license_2_0,
        ),
        OpenSourceLicenseItem(
            name = R.string.license_hilt,
            license = R.string.apache_license_2_0,
        ),
        OpenSourceLicenseItem(
            name = R.string.license_retrofit_okhttp,
            license = R.string.apache_license_2_0,
        ),
        OpenSourceLicenseItem(
            name = R.string.license_gson,
            license = R.string.apache_license_2_0,
        ),
        OpenSourceLicenseItem(
            name = R.string.license_firebase,
            license = R.string.apache_license_2_0,
        ),
        OpenSourceLicenseItem(
            name = R.string.license_timber,
            license = R.string.apache_license_2_0,
        ),
        OpenSourceLicenseItem(
            name = R.string.license_kizitonwose_calendar,
            license = R.string.mit_license,
        ),
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            PrayerDimens.StackSmall,
        ),
    ) {
        Text(
            text = stringResource(R.string.open_source_licenses_description, appName),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )

        licenses.forEach { item ->
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PrayerDimens.StackMedium),
                    verticalArrangement = Arrangement.spacedBy(
                        PrayerDimens.Baseline,
                    ),
                ) {
                    Text(
                        text = stringResource(item.name),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )

                    Text(
                        text = stringResource(item.license),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}