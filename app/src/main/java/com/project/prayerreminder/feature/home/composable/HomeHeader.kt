package com.project.prayerreminder.feature.home.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme

@Composable
fun HomeHeader() {
    Column {
        Text(
            text = stringResource(R.string.Greeting),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
            )
        )
        Spacer(modifier = Modifier.height(PrayerDimens.Baseline))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_location),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(PrayerDimens.Baseline))
            Text(
                text = "Jakarta, Indonesia",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall
            )
        }
        Spacer(modifier = Modifier.height(PrayerDimens.Baseline))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "21 July 2026",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.width(PrayerDimens.StackSmall))
            VerticalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.height(16.dp)
            )
            Spacer(modifier = Modifier.width(PrayerDimens.StackSmall))
            Text(
                text = "5 Muharram 1448 H",
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true, name = "Home Header")
@Composable
private fun HomeHeaderPreview() {
    PrayerReminderTheme {
        HomeHeader()
    }
}