package com.project.prayerreminder.feature.profile.about.licenses

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.feature.profile.about.licenses.composable.OpenSourceLicensesContent
import com.project.prayerreminder.utils.composables.AppBar

@Composable
fun OpenSourceLicensesScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val appName = stringResource(R.string.app_name)

    Scaffold(
        modifier = modifier,
        topBar = {
            AppBar(
                title = stringResource(R.string.open_source_licenses),
                canNavigateBack = true,
                navigateBack = onNavigateBack,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            OpenSourceLicensesContent(
                appName = appName,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = PrayerDimens.ScreenMargin,
                        vertical = PrayerDimens.StackLarge,
                    ),
            )

            Text(
                text = stringResource(R.string.additional_licenses_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = PrayerDimens.StackExtraLarge,
                        vertical = PrayerDimens.StackLarge,
                    ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(
    name = "Open Source Licenses Screen",
    showBackground = true,
    widthDp = 412,
    heightDp = 915,
)
@Composable
private fun OpenSourceLicensesScreenPreview() {
    PrayerReminderTheme {
        OpenSourceLicensesScreen(
            onNavigateBack = {},
        )
    }
}