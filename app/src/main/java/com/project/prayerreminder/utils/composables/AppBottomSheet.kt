package com.project.prayerreminder.utils.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme

// Bottom sheet button footer configuration
data class BottomSheetButtonConfig(
    val text: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
    val isLoading: Boolean = false,
    @param:DrawableRes val leadingIcon: Int? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomSheet(
    title: String,
    onDismissRequest: () -> Unit,
    primaryButton: BottomSheetButtonConfig,
    modifier: Modifier = Modifier,
    secondaryButton: BottomSheetButtonConfig? = null,
    subtitle: String? = null,
    showCloseButton: Boolean = true,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    ),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = PrayerDimens.StackMedium,
        vertical = PrayerDimens.StackLarge,
    ),
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = MaterialTheme.shapes.extraLarge,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.outlineVariant,
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding(),
        ) {
            // header bottom sheet
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = PrayerDimens.StackMedium,
                        end = PrayerDimens.StackSmall,
                        bottom = PrayerDimens.StackMedium,
                    ),
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(
                        PrayerDimens.Baseline,
                    ),
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                    if (!subtitle.isNullOrBlank()) {
                        Text(
                            text = subtitle,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }

                if (showCloseButton) {
                    IconButton(
                        onClick = onDismissRequest,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = stringResource(R.string.close),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
            )

            // content bottom sheet
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(
                        weight = 1f,
                        fill = false,
                    )
                    .verticalScroll(rememberScrollState())
                    .padding(contentPadding),
                content = content,
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
            )

            // footer bottom sheet
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PrayerDimens.StackMedium),
                horizontalArrangement = Arrangement.spacedBy(
                    PrayerDimens.StackSmall,
                ),
            ) {
                if (secondaryButton != null) {
                    AppButton(
                        text = secondaryButton.text,
                        onClick = secondaryButton.onClick,
                        enabled = secondaryButton.enabled,
                        isLoading = secondaryButton.isLoading,
                        leadingIcon = secondaryButton.leadingIcon,
                        variant = AppButtonVariant.Outlined,
                        modifier = Modifier.weight(1f),
                    )
                }

                AppButton(
                    text = primaryButton.text,
                    onClick = primaryButton.onClick,
                    enabled = primaryButton.enabled,
                    isLoading = primaryButton.isLoading,
                    leadingIcon = primaryButton.leadingIcon,
                    variant = AppButtonVariant.Filled,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "App Bottom Sheet", showBackground = true, widthDp = 412, heightDp = 720)
@Composable
private fun AppBottomSheetPreview() {
    PrayerReminderTheme {
        AppBottomSheet(
            title = "Add Schedule",
            subtitle = "Create a reminder for the selected date.",
            onDismissRequest = {},
            secondaryButton = BottomSheetButtonConfig(
                text = "Cancel",
                onClick = {},
            ),
            primaryButton = BottomSheetButtonConfig(
                text = "Save Schedule",
                onClick = {},
            ),
        ) {
            Text(
                text = "Schedule form content will be placed here.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}