package com.project.prayerreminder.feature.calendar.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.core.theme.PrayerReminderTheme
import com.project.prayerreminder.utils.extensions.toMonthYear
import com.project.prayerreminder.utils.extensions.toNarrowName
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

@Composable
fun CalendarContent(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Keeps navigation limited to the selected Gregorian year.
    val calendarState = rememberCalendarState(
        startMonth = YearMonth.of(selectedDate.year, Month.JANUARY),
        endMonth = YearMonth.of(selectedDate.year, Month.DECEMBER),
        firstVisibleMonth = YearMonth.from(selectedDate),
        firstDayOfWeek = DayOfWeek.SUNDAY,
        outDateStyle = OutDateStyle.EndOfRow,
    )

    val appLocale = LocalConfiguration.current.locales[0]

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
        ),
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = PrayerDimens.StackMedium,
                vertical = PrayerDimens.StackLarge,
            ),
        ) {
            Text(
                text = calendarState.firstVisibleMonth.yearMonth.toMonthYear(appLocale),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Medium,
                ),
            )
            Spacer(modifier = Modifier.height(PrayerDimens.StackLarge))


            // Header calendar
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                CalendarWeekDays.forEach { dayOfWeek ->
                    val isWeekend = dayOfWeek == DayOfWeek.SATURDAY ||
                            dayOfWeek == DayOfWeek.SUNDAY
                    Text(
                        text = dayOfWeek.toNarrowName(appLocale),
                        color = if (isWeekend) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (isWeekend) {
                                FontWeight.SemiBold
                            } else {
                                FontWeight.Medium
                            },
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            Spacer(modifier = Modifier.height(PrayerDimens.StackMedium))

            // date calendar
            HorizontalCalendar(
                state = calendarState,
                modifier = Modifier.fillMaxWidth(),
                dayContent = { day ->
                    val isSelected = selectedDate == day.date
                    val isWeekend = day.date.dayOfWeek == DayOfWeek.SATURDAY ||
                            day.date.dayOfWeek == DayOfWeek.SUNDAY
                    val dateTextColor = when {
                        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
                        isWeekend -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                    Box(
                        modifier = Modifier.aspectRatio(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (day.position == DayPosition.MonthDate) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .background(
                                        color = if (day.date == selectedDate) {
                                            MaterialTheme.colorScheme.primaryContainer
                                        } else {
                                            Color.Transparent
                                        }
                                    )
                                    .clickable(
                                        interactionSource = null,
                                        indication = null,
                                        onClick = { onDateSelected(day.date) }
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = day.date.dayOfMonth.toString(),
                                    color = dateTextColor,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = when {
                                            selectedDate == day.date -> FontWeight.Bold
                                            isWeekend -> FontWeight.Medium
                                            else -> FontWeight.Normal
                                        },
                                    ),
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}

// list of calendar from Sunday to Saturday
private val CalendarWeekDays = listOf(
    DayOfWeek.SUNDAY,
    DayOfWeek.MONDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY,
    DayOfWeek.SATURDAY,
)

@Preview(name = "Calendar Content", showBackground = true, device = "id:pixel_5")
@Composable
private fun CalendarContentLightPreview() {
    PrayerReminderTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            CalendarContent(
                selectedDate = LocalDate.of(2026, Month.JULY, 21),
                onDateSelected = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
