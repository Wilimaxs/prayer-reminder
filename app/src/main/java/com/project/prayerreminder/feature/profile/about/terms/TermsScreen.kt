package com.project.prayerreminder.feature.profile.terms

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.feature.profile.about.terms.composable.TermsHeader
import com.project.prayerreminder.feature.profile.about.terms.composable.TermsResponsibilities
import com.project.prayerreminder.feature.profile.about.terms.composable.TermsThirdParty
import com.project.prayerreminder.feature.profile.about.terms.composable.TermsUsage
import com.project.prayerreminder.utils.composables.AppBar

@Composable
fun TermsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val appName = stringResource(R.string.app_name)

    Scaffold(
        modifier = modifier,
        topBar = {
            AppBar(
                title = stringResource(R.string.terms_of_service),
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
            verticalArrangement = Arrangement.spacedBy(
                PrayerDimens.StackExtraLarge,
            ),
        ) {
            TermsHeader(
                appName = appName,
                modifier = Modifier.fillMaxWidth(),
            )

            TermsUsage(
                appName = appName,
                modifier = Modifier.fillMaxWidth(),
            )

            TermsResponsibilities(
                modifier = Modifier.fillMaxWidth(),
            )

            TermsThirdParty(
                appName = appName,
                modifier = Modifier.fillMaxWidth(),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = PrayerDimens.StackLarge,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    PrayerDimens.StackSmall,
                ),
            ) {
                Text(
                    text = appName,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = stringResource(R.string.developer_email),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = stringResource(R.string.developer_phone),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = stringResource(
                        R.string.terms_footer_copyright,
                    ),
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(name = "Terms Screen", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun TermsScreenPreview() {
    PrayerReminderTheme {
        TermsScreen(
            onNavigateBack = {},
        )
    }
}