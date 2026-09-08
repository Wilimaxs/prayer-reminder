package com.project.prayerreminder.feature.profile.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.utils.composables.AppBadge

@Composable
fun ProfileHeader(
    name: String,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    avatarPainter: Painter? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.size(92.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (avatarPainter != null) {
                    Image(
                        painter = avatarPainter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_profile),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            AppBadge(
                icon = painterResource(R.drawable.ic_edit),
                iconTint = MaterialTheme.colorScheme.onPrimary,
                iconSize = 16.dp,
                containerColor = MaterialTheme.colorScheme.primary,
                containerPadding = PrayerDimens.StackSmall,
                shapes = CircleShape,
                contentDescription = stringResource(
                    R.string.edit_profile_picture,
                ),
                onClick = onEditClick,
                modifier = Modifier.align(Alignment.BottomEnd),
            )
        }

        Text(
            text = name,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                top = PrayerDimens.StackSmall,
            ),
        )
    }
}

@Preview(name = "Profile Header", showBackground = true, widthDp = 412)
@Composable
private fun ProfileHeaderPreview() {
    PrayerReminderTheme {
        ProfileHeader(
            name = "Hamba Tuhan",
            onEditClick = {},
            modifier = Modifier.padding(
                PrayerDimens.ScreenMargin,
            ),
        )
    }
}