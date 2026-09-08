package com.project.prayerreminder.utils.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePickerField(
    value: LocalTime?,
    onValueChange: (LocalTime) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    isRequired: Boolean = false,
    errorMessage: String? = null,
    is24Hour: Boolean = true,
) {
    var showTimePicker by rememberSaveable {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current
    val appLocale = LocalConfiguration.current.locales[0]

    val timeFormatter = remember(
        is24Hour,
        appLocale,
    ) {
        DateTimeFormatter.ofPattern(
            if (is24Hour) "HH:mm" else "hh:mm a",
            appLocale,
        )
    }

    AppFormField(
        value = value?.format(timeFormatter).orEmpty(),
        onValueChange = {},
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        enabled = enabled,
        isRequired = isRequired,
        errorMessage = errorMessage,
        onClick = {
            focusManager.clearFocus()
            showTimePicker = true
        },
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_clock),
                contentDescription = null,
                tint = if (enabled) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.outline
                },
                modifier = Modifier.size(24.dp),
            )
        },
    )

    if (showTimePicker) {
        val initialTime = value ?: LocalTime.now()

        val timePickerState = rememberTimePickerState(
            initialHour = initialTime.hour,
            initialMinute = initialTime.minute,
            is24Hour = is24Hour,
        )

        AlertDialog(
            onDismissRequest = {
                showTimePicker = false
            },
            title = {
                Text(
                    text = stringResource(R.string.select_time),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )
            },
            text = {
                TimePicker(
                    state = timePickerState,
                )
            },
            dismissButton = {
                AppButton(
                    text = stringResource(R.string.cancel),
                    onClick = {
                        showTimePicker = false
                    },
                    variant = AppButtonVariant.Outlined,
                )
            },
            confirmButton = {
                AppButton(
                    text = stringResource(R.string.confirm),
                    onClick = {
                        onValueChange(
                            LocalTime.of(
                                timePickerState.hour,
                                timePickerState.minute,
                            ),
                        )

                        showTimePicker = false
                    },
                )
            },
            shape = MaterialTheme.shapes.extraLarge,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        )
    }
}

@Preview(name = "App Time Picker Field", showBackground = true, widthDp = 412)
@Composable
private fun AppTimePickerFieldPreview() {
    PrayerReminderTheme {
        var selectedTime by remember {
            mutableStateOf<LocalTime?>(
                LocalTime.of(19, 30),
            )
        }

        AppTimePickerField(
            value = selectedTime,
            onValueChange = {
                selectedTime = it
            },
            label = "Time",
            placeholder = "Select time",
            isRequired = true,
            modifier = Modifier.padding(PrayerDimens.ScreenMargin),
        )
    }
}