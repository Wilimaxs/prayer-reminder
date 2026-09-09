package com.project.prayerreminder.feature.profile.composable.bottomsheet

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.feature.profile.PrayerCalculationMethod
import com.project.prayerreminder.utils.composables.AppSelectionList
import com.project.prayerreminder.utils.composables.AppSelectionOption

@Composable
fun CalculationMethodList(
    selectedMethod: PrayerCalculationMethod,
    onSelectedMethodChange: (PrayerCalculationMethod) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppSelectionList(
        options = PrayerCalculationMethod.entries.map { method ->
            AppSelectionOption(
                value = method,
                title = when (method) {
                    PrayerCalculationMethod.KemenagIndonesia -> {
                        stringResource(R.string.kemenag_indonesia)
                    }

                    PrayerCalculationMethod.MuslimWorldLeague -> {
                        stringResource(R.string.muslim_world_league)
                    }

                    PrayerCalculationMethod.JakimMalaysia -> {
                        stringResource(R.string.jakim_malaysia)
                    }
                },
                subtitle = when (method) {
                    PrayerCalculationMethod.KemenagIndonesia -> {
                        stringResource(R.string.kemenag_indonesia_description)
                    }

                    PrayerCalculationMethod.MuslimWorldLeague -> {
                        stringResource(R.string.muslim_world_league_description)
                    }

                    PrayerCalculationMethod.JakimMalaysia -> {
                        stringResource(R.string.jakim_malaysia_description)
                    }
                },
            )
        },
        selectedValue = selectedMethod,
        onSelectedValueChange = onSelectedMethodChange,
        modifier = modifier,
    )
}

@Preview(name = "Calculation Method List", showBackground = true, widthDp = 412)
@Composable
private fun CalculationMethodListPreview() {
    PrayerReminderTheme {
        var selectedCode by rememberSaveable {
            mutableIntStateOf(PrayerCalculationMethod.KemenagIndonesia.apiCode)
        }

        CalculationMethodList(
            selectedMethod = PrayerCalculationMethod.fromApiCode(selectedCode),
            onSelectedMethodChange = { method -> selectedCode = method.apiCode },
            modifier = Modifier.padding(PrayerDimens.ScreenMargin),
        )
    }
}