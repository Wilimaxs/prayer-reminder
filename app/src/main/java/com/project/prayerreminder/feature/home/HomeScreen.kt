package com.project.prayerreminder.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.feature.home.composable.HomeCard
import com.project.prayerreminder.feature.home.composable.HomeHeader
import com.project.prayerreminder.feature.home.composable.HomeSchedule
import com.project.prayerreminder.utils.composables.AppBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppBar(
                canNavigateBack = false,
                title = stringResource(R.string.home_label)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .padding(horizontal = PrayerDimens.ScreenMargin)
                .verticalScroll(rememberScrollState())
        ) {
            HomeHeader()
            Spacer(modifier = Modifier.height(PrayerDimens.StackLarge))
            HomeCard(
                Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(PrayerDimens.StackLarge))
            HomeSchedule()
            Spacer(modifier = Modifier.height(PrayerDimens.ScreenMargin))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Home", device = "id:pixel_5")
@Composable
private fun HomePreview() {
    PrayerReminderTheme {
        HomeScreen(modifier = Modifier)
    }
}