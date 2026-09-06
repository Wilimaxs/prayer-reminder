package com.project.prayerreminder.core.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val PrayerShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

val FullRoundedShape = RoundedCornerShape(percent = 50)

object PrayerDimens {
    val Baseline = 4.dp
    val ExtraSmall = 6.dp
    val ScreenMargin = 16.dp
    val Gutter = 16.dp
    val StackSmall = 8.dp
    val StackMedium = 16.dp
    val StackLarge = 24.dp
}