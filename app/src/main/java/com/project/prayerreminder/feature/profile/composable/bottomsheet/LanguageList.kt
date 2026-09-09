package com.project.prayerreminder.feature.profile.composable.bottomsheet

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.utils.composables.AppSelectionList
import com.project.prayerreminder.utils.composables.AppSelectionOption
import com.project.prayerreminder.utils.enumeration.AppLanguage

@Composable
fun LanguageList(
    selectedLanguage: AppLanguage,
    onSelectedLanguageChange: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = AppLanguage.entries.map { language ->
        AppSelectionOption(
            value = language,
            title = when (language) {
                AppLanguage.English -> {
                    stringResource(R.string.english)
                }

                AppLanguage.Indonesian -> {
                    stringResource(R.string.indonesian)
                }
            },
        )
    }

    AppSelectionList(
        options = options,
        selectedValue = selectedLanguage,
        onSelectedValueChange = onSelectedLanguageChange,
        modifier = modifier,
    )
}

@Preview(name = "Language List", showBackground = true, widthDp = 412)
@Composable
private fun LanguageListPreview() {
    PrayerReminderTheme {
        var selectedLanguage by rememberSaveable {
            mutableStateOf(AppLanguage.English)
        }

        LanguageList(
            selectedLanguage = selectedLanguage,
            onSelectedLanguageChange = {
                selectedLanguage = it
            },
            modifier = Modifier.padding(
                PrayerDimens.ScreenMargin,
            ),
        )
    }
}