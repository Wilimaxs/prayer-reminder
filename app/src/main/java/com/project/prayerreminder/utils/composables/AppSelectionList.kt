package com.project.prayerreminder.utils.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme

data class AppSelectionOption<T>(
    val value: T,
    val title: String,
    val subtitle: String? = null,
)

@Composable
fun <T> AppSelectionList(
    options: List<AppSelectionOption<T>>,
    selectedValue: T,
    onSelectedValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(
            PrayerDimens.StackSmall,
        ),
    ) {
        options.forEach { option ->
            val isSelected = option.value == selectedValue

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = isSelected,
                        role = Role.RadioButton,
                        onClick = {
                            onSelectedValueChange(option.value)
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
                            .padding(
                                start = PrayerDimens.StackSmall,
                            ),
                        verticalArrangement = Arrangement.spacedBy(
                            PrayerDimens.Baseline,
                        ),
                    ) {
                        Text(
                            text = option.title,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge,
                        )

                        if (!option.subtitle.isNullOrBlank()) {
                            Text(
                                text = option.subtitle,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "App Selection List", showBackground = true, widthDp = 412)
@Composable
private fun AppSelectionListPreview() {
    PrayerReminderTheme {
        var selectedValue by rememberSaveable {
            mutableStateOf("shafi")
        }

        AppSelectionList(
            options = listOf(
                AppSelectionOption(
                    value = "shafi",
                    title = "Shafi'i (Standard)",
                    subtitle = "Asr begins when an object's shadow equals its length.",
                ),
                AppSelectionOption(
                    value = "hanafi",
                    title = "Hanafi",
                    subtitle = "Asr begins when an object's shadow is twice its length.",
                ),
            ),
            selectedValue = selectedValue,
            onSelectedValueChange = {
                selectedValue = it
            },
            modifier = Modifier.padding(
                PrayerDimens.ScreenMargin,
            ),
        )
    }
}