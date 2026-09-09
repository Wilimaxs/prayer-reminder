package com.project.prayerreminder.feature.profile.composable.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.feature.profile.PrayerCalculationMethod

@Composable
fun CalculationMethodList(
    selectedMethod: PrayerCalculationMethod,
    onSelectedMethodChange: (PrayerCalculationMethod) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            PrayerDimens.StackSmall
        ),
    ) {
        PrayerCalculationMethod.entries.forEach { method ->
            val isSelected = method == selectedMethod

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = isSelected,
                        role = Role.RadioButton,
                        onClick = {
                            onSelectedMethodChange(method)
                        },
                    ),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        Color.Transparent
                    },
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp,
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 64.dp)
                        .padding(
                            horizontal = PrayerDimens.StackMedium,
                            vertical = PrayerDimens.StackSmall,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = MaterialTheme.colorScheme.outline,
                        ),
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = PrayerDimens.StackSmall),
                        verticalArrangement = Arrangement.spacedBy(
                            PrayerDimens.Baseline,
                        ),
                    ) {
                        Text(
                            text = when (method) {
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
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge,
                        )

                        Text(
                            text = when (method) {
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
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
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