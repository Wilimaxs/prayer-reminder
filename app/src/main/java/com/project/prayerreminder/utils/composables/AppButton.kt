package com.project.prayerreminder.utils.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme

enum class AppButtonVariant {
    Filled,
    Outlined,
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    variant: AppButtonVariant = AppButtonVariant.Filled,
    @DrawableRes leadingIcon: Int? = null,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
) {
    // Loading for the Disable button
    val isButtonEnabled = enabled && !isLoading

    when (variant) {
        AppButtonVariant.Filled -> {
            Button(
                onClick = onClick,
                modifier = modifier.heightIn(min = 48.dp),
                enabled = isButtonEnabled,
                shape = shape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    focusedElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    disabledElevation = 0.dp,
                ),
                contentPadding = contentPadding,
            ) {
                AppButtonContent(
                    text = text,
                    leadingIcon = leadingIcon,
                    isLoading = isLoading,
                    textStyle = textStyle,
                )
            }
        }

        AppButtonVariant.Outlined -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier.heightIn(min = 48.dp),
                enabled = isButtonEnabled,
                shape = shape,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isButtonEnabled) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    },
                ),
                contentPadding = contentPadding,
            ) {
                AppButtonContent(
                    text = text,
                    leadingIcon = leadingIcon,
                    isLoading = isLoading,
                    textStyle = textStyle,
                )
            }
        }
    }
}

@Composable
private fun AppButtonContent(
    text: String,
    @DrawableRes leadingIcon: Int?,
    isLoading: Boolean,
    textStyle: TextStyle,
) {
    when {
        isLoading -> {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = LocalContentColor.current,
                strokeWidth = 2.dp,
            )

            Spacer(modifier = Modifier.width(PrayerDimens.StackSmall))
        }

        leadingIcon != null -> {
            Icon(
                painter = painterResource(leadingIcon),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )

            Spacer(modifier = Modifier.width(PrayerDimens.StackSmall))
        }
    }

    Text(
        text = text,
        style = textStyle,
    )
}

@Preview(name = "App Button", showBackground = true, widthDp = 412)
@Composable
private fun AppButtonPreview() {
    PrayerReminderTheme {
        Column(
            modifier = Modifier.padding(PrayerDimens.ScreenMargin),
            verticalArrangement = Arrangement.spacedBy(
                PrayerDimens.StackMedium,
            ),
        ) {
            // Example filled button with full width.
            AppButton(
                text = "Save",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )

            // Example outlined button with full width.
            AppButton(
                text = "Cancel",
                onClick = {},
                variant = AppButtonVariant.Outlined,
                modifier = Modifier.fillMaxWidth(),
            )

            // Example leading button
            AppButton(
                text = "Saving",
                onClick = {},
                isLoading = true,
                modifier = Modifier.fillMaxWidth(),
            )

            // Example wrap-content with the leading icon.
            AppButton(
                text = "Add Schedule",
                onClick = {},
                leadingIcon = R.drawable.ic_calendar,
            )
        }
    }
}
