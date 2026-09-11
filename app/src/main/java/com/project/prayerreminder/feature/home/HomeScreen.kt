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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.feature.home.composable.HomeCard
import com.project.prayerreminder.feature.home.composable.HomeHeader
import com.project.prayerreminder.feature.home.composable.HomeSchedule
import com.project.prayerreminder.utils.composables.AppBar
import com.project.prayerreminder.utils.composables.AppSnackbarType
import com.project.prayerreminder.utils.composables.LocalAppSnackbarHostState
import com.project.prayerreminder.utils.composables.showAppSnackbar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalAppSnackbarHostState.current
    val announcementTitle = stringResource(R.string.announcement_title)

    // Displays each Remote Config announcement once per announcement ID.
    LaunchedEffect(uiState.announcementId, uiState.announcementMessage) {
        val announcementId = uiState.announcementId ?: return@LaunchedEffect
        val announcementMessage = uiState.announcementMessage ?: return@LaunchedEffect

        viewModel.markAnnouncementAsSeen(announcementId)
        snackbarHostState.showAppSnackbar(
            title = announcementTitle,
            subtitle = announcementMessage,
            type = AppSnackbarType.INFO,
        )
    }

    HomeScreenContent(
        uiState = uiState,
        modifier = modifier,
    )
}

@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
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
        val content = uiState.content

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .padding(horizontal = PrayerDimens.ScreenMargin)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = if (content == null) {
                Alignment.CenterHorizontally
            } else {
                Alignment.Start
            },
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                content != null -> {
                    HomeHeader(
                        locationName = content.locationName,
                        gregorianDate = content.gregorianDate,
                        hijriDate = content.hijriDate,
                    )
                    Spacer(modifier = Modifier.height(PrayerDimens.StackLarge))
                    HomeCard(
                        nextPrayer = content.nextPrayer,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(PrayerDimens.StackLarge))
                    HomeSchedule(
                        prayers = content.prayers,
                    )
                    Spacer(modifier = Modifier.height(PrayerDimens.ScreenMargin))
                }

                uiState.errorMessage != null -> {
                    Text(
                        text = stringResource(R.string.home_data_error),
                    )
                }

                else -> {
                    Text(
                        text = stringResource(R.string.home_data_empty),
                    )
                }
            }
        }
    }
}
