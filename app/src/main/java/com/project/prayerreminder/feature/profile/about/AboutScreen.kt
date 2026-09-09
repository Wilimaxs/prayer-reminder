package com.project.prayerreminder.feature.profile.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.project.prayerreminder.BuildConfig
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.feature.profile.about.composable.AboutDeveloper
import com.project.prayerreminder.feature.profile.about.composable.AboutFeature
import com.project.prayerreminder.feature.profile.about.composable.AboutHeader
import com.project.prayerreminder.feature.profile.about.composable.AboutLegalInformation
import com.project.prayerreminder.utils.composables.AppBar

@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val appName = stringResource(R.string.app_name)

    Scaffold(
        modifier = modifier,
        topBar = {
            AppBar(
                title = stringResource(R.string.about_application),
                canNavigateBack = true,
                navigateBack = onNavigateBack,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = PrayerDimens.ScreenMargin,
                    vertical = PrayerDimens.StackLarge,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                PrayerDimens.StackExtraLarge,
            ),
        ) {
            AboutHeader(
                appName = appName,
                versionName = stringResource(R.string.version_name, BuildConfig.VERSION_NAME),
                description = stringResource(R.string.about_application_description),
                modifier = Modifier.fillMaxWidth(),
            )

            AboutFeature(
                modifier = Modifier.fillMaxWidth(),
            )

            AboutDeveloper(
                developerImage = painterResource(R.drawable.img_developer),
                modifier = Modifier.fillMaxWidth(),
            )

            AboutLegalInformation(
                onPrivacyPolicyClick = {
                    // TODO: Open privacy policy.
                },
                onTermsOfServiceClick = {
                    // TODO: Open terms of service.
                },
                onOpenSourceLicensesClick = {
                    // TODO: Open open-source licenses.
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    PrayerDimens.Baseline,
                ),
            ) {
                Text(
                    text = stringResource(R.string.made_with_for_muslims),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = stringResource(R.string.application_copyright, appName),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(
    name = "About Screen",
    showBackground = true,
    widthDp = 412,
    heightDp = 915,
)
@Composable
private fun AboutScreenPreview() {
    PrayerReminderTheme {
        AboutScreen(
            onNavigateBack = {},
        )
    }
}