package com.project.prayerreminder.feature.profile.composable.bottomsheet

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.feature.profile.AsrMadhab
import com.project.prayerreminder.utils.composables.AppSelectionList
import com.project.prayerreminder.utils.composables.AppSelectionOption

@Composable
fun MadhabList(
    selectedMadhab: AsrMadhab,
    onSelectedMadhabChange: (AsrMadhab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = listOf(
        AppSelectionOption(
            value = AsrMadhab.Shafi,
            title = stringResource(R.string.shafi_standard),
            subtitle = stringResource(R.string.shafi_description),
        ),
        AppSelectionOption(
            value = AsrMadhab.Hanafi,
            title = stringResource(R.string.hanafi),
            subtitle = stringResource(R.string.hanafi_description),
        ),
    )

    AppSelectionList(
        options = options,
        selectedValue = selectedMadhab,
        onSelectedValueChange = onSelectedMadhabChange,
        modifier = modifier,
    )
}

@Preview(name = "Madhab List", showBackground = true, widthDp = 412)
@Composable
private fun MadhabListPreview() {
    PrayerReminderTheme {
        MadhabList(
            selectedMadhab = AsrMadhab.Shafi,
            onSelectedMadhabChange = {},
            modifier = Modifier.padding(
                PrayerDimens.ScreenMargin,
            ),
        )
    }
}