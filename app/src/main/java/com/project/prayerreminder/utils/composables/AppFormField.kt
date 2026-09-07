package com.project.prayerreminder.utils.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme

@Composable
fun AppFormField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    helperText: String? = null,
    errorMessage: String? = null,
    isRequired: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    maxLength: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.medium,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    val hasError = !errorMessage.isNullOrBlank()

    val resolvedMinLines = if (singleLine) {
        1
    } else {
        minLines.coerceAtLeast(1)
    }

    val resolvedMaxLines = if (singleLine) {
        1
    } else {
        maxLines.coerceAtLeast(resolvedMinLines)
    }

    Column(
        modifier = modifier,
    ) {
        if (!label.isNullOrBlank()) {
            Text(
                text = buildAnnotatedString {
                    append(label)

                    if (isRequired) {
                        withStyle(
                            style = SpanStyle(color = MaterialTheme.colorScheme.error),
                        ) {
                            append(" *")
                        }
                    }
                },
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                style = MaterialTheme.typography.labelLarge,
            )

            Spacer(modifier = Modifier.height(PrayerDimens.StackSmall))
        }

        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                val limitedValue = if (maxLength != null) {
                    newValue.take(maxLength)
                } else {
                    newValue
                }

                onValueChange(limitedValue)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            minLines = resolvedMinLines,
            maxLines = resolvedMaxLines,
            isError = hasError,
            textStyle = textStyle,
            placeholder = if (!placeholder.isNullOrBlank()) {
                {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            } else {
                null
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            shape = shape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorTextColor = MaterialTheme.colorScheme.onErrorContainer,

                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                errorContainerColor = MaterialTheme.colorScheme.errorContainer,

                cursorColor = MaterialTheme.colorScheme.primary,
                errorCursorColor = MaterialTheme.colorScheme.error,

                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
                errorBorderColor = MaterialTheme.colorScheme.error,

                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPlaceholderColor = MaterialTheme.colorScheme.outline,
            ),
        )

        when {
            hasError -> {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(
                        start = PrayerDimens.StackMedium,
                        top = PrayerDimens.StackSmall,
                        end = PrayerDimens.StackMedium,
                    ),
                )
            }

            !helperText.isNullOrBlank() -> {
                Text(
                    text = helperText,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(
                        start = PrayerDimens.StackMedium,
                        top = PrayerDimens.StackSmall,
                        end = PrayerDimens.StackMedium,
                    ),
                )
            }
        }
    }
}

@Preview(
    name = "App Form Field",
    showBackground = true,
    widthDp = 412,
)
@Composable
private fun AppFormFieldPreview() {
    PrayerReminderTheme(
        darkTheme = false,
    ) {
        var scheduleTitle by remember {
            mutableStateOf("")
        }

        Column(
            modifier = Modifier.padding(PrayerDimens.ScreenMargin),
        ) {
            // Example field can edit
            AppFormField(
                value = scheduleTitle,
                onValueChange = {
                    scheduleTitle = it
                },
                label = "Schedule Title",
                placeholder = "Example: Mosque Study",
                isRequired = true,
                maxLength = 50,
            )

            Spacer(
                modifier = Modifier.height(PrayerDimens.StackMedium),
            )

            // Example field can't edit
            AppFormField(
                value = "21 July 2026",
                onValueChange = {},
                label = "Date",
                enabled = false,
            )

            Spacer(
                modifier = Modifier.height(PrayerDimens.StackMedium),
            )

            // Example display error from UI state.
            AppFormField(
                value = "",
                onValueChange = {},
                label = "Schedule Name",
                errorMessage = "Schedule name is required.",
                isRequired = true,
            )
        }
    }
}