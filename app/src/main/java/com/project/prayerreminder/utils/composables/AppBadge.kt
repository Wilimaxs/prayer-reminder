package com.project.prayerreminder.utils.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.core.theme.PrayerShapes

@Composable
fun AppBadge(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    containerPadding: Dp = PrayerDimens.StackMedium,
    shapes: Shape = PrayerShapes.medium,
    text: String? = null,
    textStyles: TextStyle = MaterialTheme.typography.labelSmall,
    icon: Painter? = null,
    iconTint: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    iconSize: Dp = PrayerDimens.StackMedium,
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .clip(shapes)
            .background(color = containerColor, shape = shapes)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        role = Role.Button,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                },
            )
            .padding(containerPadding),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = contentDescription,
                    tint = iconTint,
                    modifier = Modifier.size(iconSize)
                )
            }
            if (icon != null && text != null) {
                Spacer(modifier = Modifier.width(PrayerDimens.StackSmall))
            }
            if (text != null) {
                Text(
                    text = text,
                    style = textStyles
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Badge")
@Composable
private fun AppBadgeIconTextPreview() {
    PrayerReminderTheme {
        AppBadge(
            icon = painterResource(R.drawable.ic_location),
        )
    }
}