package com.project.prayerreminder.feature.profile.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme

@Composable
fun ProfileSettingItem(
    @DrawableRes icon: Int,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = {
        Icon(
            painter = painterResource(
                R.drawable.ic_arrow_right,
            ),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(20.dp)
        )
    },
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
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
            .padding(
                horizontal = PrayerDimens.StackMedium,
                vertical = PrayerDimens.StackSmall,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp),
        )

        Spacer(modifier = Modifier.width(PrayerDimens.StackMedium))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )
            if (!subtitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(PrayerDimens.StackSmall))
                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        if (trailingContent != null) {
            Spacer(modifier = Modifier.width(PrayerDimens.StackSmall))

            trailingContent()
        }
    }
}

@Preview(name = "Profile Setting Item", showBackground = true, widthDp = 412)
@Composable
private fun ProfileSettingItemPreview() {
    PrayerReminderTheme {
        var isEnabled by remember {
            mutableStateOf(true)
        }

        Column {
            ProfileSettingItem(
                icon = R.drawable.ic_location,
                title = "Location",
                subtitle = "Bandung, Indonesia",
                onClick = {},
            )

            ProfileSettingItem(
                icon = R.drawable.ic_notifications,
                title = "Prayer Reminders",
                subtitle = "Alert for all 5 prayers",
                trailingContent = {
                    Switch(
                        checked = isEnabled,
                        onCheckedChange = {
                            isEnabled = it
                        },
                    )
                },
            )
        }
    }
}