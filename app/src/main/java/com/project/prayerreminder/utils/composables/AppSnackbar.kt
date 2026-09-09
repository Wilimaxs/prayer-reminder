package com.project.prayerreminder.utils.composables

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.project.prayerreminder.R
import kotlinx.coroutines.launch

enum class AppSnackbarType(
    @param:DrawableRes val iconRes: Int,
) {
    SUCCESS(R.drawable.ic_success),
    ERROR(R.drawable.ic_error),
    INFO(R.drawable.ic_info),
    WARNING(R.drawable.ic_warning),
}

// Configuration for snackbar from visualization snackbar
private data class AppSnackbarVisuals(
    val title: String,
    val subtitle: String?,
    val type: AppSnackbarType,
    val onClick: (() -> Unit)?,
    override val duration: SnackbarDuration,
) : SnackbarVisuals {
    override val message: String get() = subtitle?.takeIf { it.isNotBlank() } ?: title
    override val actionLabel: String? get() = null
    override val withDismissAction: Boolean get() = false
}

// SnackbarHostState for snackbar can access from any screen.
val LocalAppSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("AppSnackbarHostState not provided")
}

// Included a new snackbar to waiting list snackbar if there are double snackbar at one time.
suspend fun SnackbarHostState.showAppSnackbar(
    title: String,
    subtitle: String? = null,
    type: AppSnackbarType = AppSnackbarType.INFO,
    duration: SnackbarDuration = SnackbarDuration.Long,
    onClick: (() -> Unit)? = null,
): SnackbarResult {
    return showSnackbar(
        visuals = AppSnackbarVisuals(
            title = title,
            subtitle = subtitle,
            type = type,
            duration = duration,
            onClick = onClick,
        ),
    )
}

// Monitoring SnackbarHostState and displaying the active snackbar.
@Composable
fun AppSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
    ) { snackbarData ->
        val visuals = snackbarData.visuals as? AppSnackbarVisuals
            ?: return@SnackbarHost

        AppSnackbar(
            snackbarData = snackbarData,
            visuals = visuals,
        )
    }
}

// Snackbar content
@Composable
private fun AppSnackbar(
    snackbarData: SnackbarData,
    visuals: AppSnackbarVisuals,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    var offsetX by remember(snackbarData) {
        mutableFloatStateOf(0f)
    }

    var offsetY by remember(snackbarData) {
        mutableFloatStateOf(0f)
    }

    val (containerColor, contentColor) = when (visuals.type) {
        AppSnackbarType.SUCCESS -> {
            MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        }

        AppSnackbarType.ERROR -> {
            MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        }

        AppSnackbarType.INFO -> {
            MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        }

        AppSnackbarType.WARNING -> {
            MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        }
    }

    val dismissThreshold = with(LocalDensity.current) {
        72.dp.toPx()
    }

    // return snackbar to the initial position when swipe not enough long.
    fun resetPosition() {
        coroutineScope.launch {
            launch {
                animate(
                    initialValue = offsetX,
                    targetValue = 0f,
                ) { value, _ ->
                    offsetX = value
                }
            }

            launch {
                animate(
                    initialValue = offsetY,
                    targetValue = 0f,
                ) { value, _ ->
                    offsetY = value
                }
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationX = offsetX
                translationY = offsetY
                alpha = 1f - (maxOf(offsetX / dismissThreshold, -offsetY / dismissThreshold)
                    .coerceIn(0f, 1f) * 0.4f)
            }
            .pointerInput(snackbarData) {
                detectDragGestures(
                    onDragEnd = {
                        val swipedRight = offsetX >= dismissThreshold
                        val swipedUp = offsetY <= -dismissThreshold

                        if (swipedRight || swipedUp) {
                            snackbarData.dismiss()
                        } else {
                            resetPosition()
                        }
                    },
                    onDragCancel = {
                        resetPosition()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()

                        // Only can swipe to right and top
                        offsetX = (offsetX + dragAmount.x).coerceAtLeast(0f)
                        offsetY = (offsetY + dragAmount.y).coerceAtMost(0f)
                    },
                )
            }
            .then(
                if (visuals.onClick != null) {
                    Modifier.clickable {
                        visuals.onClick.invoke()
                        snackbarData.dismiss()
                    }
                } else {
                    Modifier
                }
            ),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Icon(
                painter = painterResource(visuals.type.iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = contentColor,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = visuals.title,
                    style = MaterialTheme.typography.titleMedium,
                )

                if (!visuals.subtitle.isNullOrBlank()) {
                    Text(
                        text = visuals.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor.copy(alpha = 0.8f),
                    )
                }
            }
        }
    }
}