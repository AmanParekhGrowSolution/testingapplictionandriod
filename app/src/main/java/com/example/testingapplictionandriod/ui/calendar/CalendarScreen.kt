package com.example.testingapplictionandriod.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testingapplictionandriod.R
import com.example.testingapplictionandriod.domain.model.CalendarEvent
import com.example.testingapplictionandriod.domain.model.EventType
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CalendarScreen(
    uiState: CalendarUiState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onGoToToday: () -> Unit,
    onDaySelected: (Int) -> Unit,
    onShowCreateEvent: () -> Unit,
    onDismissCreateEvent: () -> Unit,
    onNewEventTitleChange: (String) -> Unit,
    onNewEventDescriptionChange: (String) -> Unit,
    onNewEventTypeChange: (EventType) -> Unit,
    onNewEventStartHourChange: (Int) -> Unit,
    onNewEventStartMinuteChange: (Int) -> Unit,
    onNewEventEndHourChange: (Int) -> Unit,
    onNewEventEndMinuteChange: (Int) -> Unit,
    onAddEvent: () -> Unit,
    onDeleteEvent: (String) -> Unit,
    onShowEventDetail: (String) -> Unit,
    onBackToCalendar: () -> Unit
) {
    when (val screen = uiState.currentScreen) {
        CalendarNavScreen.Month -> CalendarMonthView(
            uiState = uiState,
            onPreviousMonth = onPreviousMonth,
            onNextMonth = onNextMonth,
            onGoToToday = onGoToToday,
            onDaySelected = onDaySelected,
            onShowCreateEvent = onShowCreateEvent,
            onShowEventDetail = onShowEventDetail,
            onDeleteEvent = onDeleteEvent
        )
        CalendarNavScreen.CreateEvent -> CreateEventScreen(
            uiState = uiState,
            onDismiss = onDismissCreateEvent,
            onTitleChange = onNewEventTitleChange,
            onDescriptionChange = onNewEventDescriptionChange,
            onTypeChange = onNewEventTypeChange,
            onStartHourChange = onNewEventStartHourChange,
            onStartMinuteChange = onNewEventStartMinuteChange,
            onEndHourChange = onNewEventEndHourChange,
            onEndMinuteChange = onNewEventEndMinuteChange,
            onSave = onAddEvent
        )
        is CalendarNavScreen.EventDetail -> {
            val event = uiState.events.find { it.id == screen.eventId }
            if (event != null) {
                EventDetailScreen(
                    event = event,
                    onBack = onBackToCalendar,
                    onDelete = { onDeleteEvent(event.id) }
                )
            } else {
                LaunchedEffect(screen.eventId) { onBackToCalendar() }
            }
        }
    }
}

// ── Month view ────────────────────────────────────────────────

@Composable
private fun CalendarMonthView(
    uiState: CalendarUiState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onGoToToday: () -> Unit,
    onDaySelected: (Int) -> Unit,
    onShowCreateEvent: () -> Unit,
    onShowEventDetail: (String) -> Unit,
    onDeleteEvent: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0F0C29), Color(0xFF302B63), Color(0xFF24243E))
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CalendarTopBar(
                year = uiState.displayedYear,
                month = uiState.displayedMonth,
                onPreviousMonth = onPreviousMonth,
                onNextMonth = onNextMonth,
                onGoToToday = onGoToToday
            )

            WeekdayHeaders()

            CalendarGrid(
                year = uiState.displayedYear,
                month = uiState.displayedMonth,
                selectedDay = uiState.selectedDay,
                events = uiState.events,
                onDaySelected = onDaySelected
            )

            EventsPanel(
                modifier = Modifier.weight(1f),
                year = uiState.displayedYear,
                month = uiState.displayedMonth,
                selectedDay = uiState.selectedDay,
                events = uiState.events,
                onShowEventDetail = onShowEventDetail,
                onDeleteEvent = onDeleteEvent
            )
        }

        // FAB — rounded square, gradient
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp)
                .size(56.dp)
                .background(
                    Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))),
                    RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onShowCreateEvent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.cd_add_event),
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

// ── Top bar ───────────────────────────────────────────────────

@Composable
private fun CalendarTopBar(
    year: Int,
    month: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onGoToToday: () -> Unit
) {
    val cal = java.util.Calendar.getInstance().apply { set(year, month - 1, 1) }
    val monthName = SimpleDateFormat(stringResource(R.string.date_format_month_only), Locale.getDefault()).format(cal.time)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 8.dp, top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = monthName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                lineHeight = 30.sp
            )
            Text(
                text = year.toString(),
                color = Color.White.copy(alpha = 0.87f),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }

        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF6366F1).copy(alpha = 0.35f), Color(0xFF8B5CF6).copy(alpha = 0.35f))
                    ),
                    RoundedCornerShape(20.dp)
                )
                .clickable(onClick = onGoToToday)
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.btn_today),
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        IconButton(onClick = onPreviousMonth) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.cd_previous_month),
                tint = Color.White
            )
        }

        IconButton(onClick = onNextMonth) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.cd_next_month),
                tint = Color.White
            )
        }
    }
}

// ── Weekday headers ───────────────────────────────────────────

@Composable
private fun WeekdayHeaders() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        stringArrayResource(R.array.calendar_day_headers).forEach { label ->
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.87f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

// ── Calendar grid ─────────────────────────────────────────────

@Composable
private fun CalendarGrid(
    year: Int,
    month: Int,
    selectedDay: Int?,
    events: List<CalendarEvent>,
    onDaySelected: (Int) -> Unit
) {
    val today = java.util.Calendar.getInstance()
    val todayYear = today.get(java.util.Calendar.YEAR)
    val todayMonth = today.get(java.util.Calendar.MONTH) + 1
    val todayDay = today.get(java.util.Calendar.DAY_OF_MONTH)

    val cal = java.util.Calendar.getInstance().apply { set(year, month - 1, 1) }
    val daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
    // Monday-first offset: Mon=0, Tue=1, …, Sun=6
    val firstDayOfWeek = (cal.get(java.util.Calendar.DAY_OF_WEEK) - java.util.Calendar.MONDAY + 7) % 7

    val cells = List(firstDayOfWeek) { 0 } + (1..daysInMonth).toList()
    val remainder = cells.size % 7
    val paddedCells = if (remainder == 0) cells else cells + List(7 - remainder) { 0 }

    Column(modifier = Modifier.padding(horizontal = 6.dp)) {
        paddedCells.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { day ->
                    CalendarDayCell(
                        day = day,
                        isToday = day != 0 && year == todayYear && month == todayMonth && day == todayDay,
                        isSelected = day != 0 && day == selectedDay,
                        dayEvents = if (day != 0) events.filter {
                            it.year == year && it.month == month && it.day == day
                        } else emptyList(),
                        onClick = { if (day != 0) onDaySelected(day) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    day: Int,
    isToday: Boolean,
    isSelected: Boolean,
    dayEvents: List<CalendarEvent>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(72.dp)
            .padding(2.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        if (day != 0) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        when {
                            isSelected -> Modifier.background(
                                Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))),
                                RoundedCornerShape(14.dp)
                            )
                            isToday -> Modifier.background(
                                Brush.linearGradient(listOf(Color(0xFF06B6D4), Color(0xFF3B82F6))),
                                RoundedCornerShape(14.dp)
                            )
                            else -> Modifier.background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF6366F1).copy(alpha = 0.04f), Color(0xFF8B5CF6).copy(alpha = 0.04f))
                                ),
                                RoundedCornerShape(14.dp)
                            )
                        }
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .clickable(onClick = onClick)
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp, bottom = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = day.toString(),
                    color = if (isSelected || isToday) Color.White else Color.White.copy(alpha = 0.87f),
                    fontSize = 14.sp,
                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                if (dayEvents.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 3.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        dayEvents.take(3).forEach { event ->
                            val (sc, ec) = event.type.gradientColors()
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .background(
                                        if (isSelected || isToday)
                                            Brush.linearGradient(
                                                listOf(Color.White.copy(alpha = 0.75f), Color.White)
                                            )
                                        else
                                            Brush.linearGradient(listOf(sc, ec)),
                                        RoundedCornerShape(2.dp)
                                    )
                            )
                        }
                        if (dayEvents.size > 3) {
                            Text(
                                text = stringResource(R.string.events_overflow_count, dayEvents.size - 3),
                                color = if (isSelected || isToday) Color.White
                                        else Color.White.copy(alpha = 0.87f),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Events panel (bottom sheet style) ────────────────────────

@Composable
private fun EventsPanel(
    modifier: Modifier = Modifier,
    year: Int,
    month: Int,
    selectedDay: Int?,
    events: List<CalendarEvent>,
    onShowEventDetail: (String) -> Unit,
    onDeleteEvent: (String) -> Unit
) {
    val selectedEvents = if (selectedDay != null) {
        events.filter { it.year == year && it.month == month && it.day == selectedDay }
            .sortedWith(compareBy({ it.startHour }, { it.startMinute }))
    } else emptyList()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(listOf(Color(0xFF1A1741), Color(0xFF2D2A5E))),
                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
    ) {
        // Drag handle
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(4.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(Color.White.copy(alpha = 0.15f), Color.White.copy(alpha = 0.28f))
                        ),
                        RoundedCornerShape(2.dp)
                    )
            )
        }

        // Day header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (selectedDay != null) {
                    val cal = java.util.Calendar.getInstance().apply { set(year, month - 1, selectedDay) }
                    SimpleDateFormat(stringResource(R.string.date_format_day_month), Locale.getDefault()).format(cal.time)
                } else {
                    stringResource(R.string.events_title_no_selection)
                },
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier.weight(1f)
            )
            if (selectedDay != null && selectedEvents.isNotEmpty()) {
                Text(
                    text = pluralStringResource(R.plurals.events_count, selectedEvents.size, selectedEvents.size),
                    color = Color.White.copy(alpha = 0.87f),
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        when {
            selectedDay == null -> PanelEmptyState(stringResource(R.string.events_tap_to_select))
            selectedEvents.isEmpty() -> PanelEmptyState(stringResource(R.string.events_no_events))
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp, bottom = 88.dp
                    )
                ) {
                    items(selectedEvents, key = { it.id }) { event ->
                        EventPanelCard(
                            event = event,
                            onTap = { onShowEventDetail(event.id) },
                            onDelete = { onDeleteEvent(event.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PanelEmptyState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF6366F1).copy(alpha = 0.08f), Color(0xFF8B5CF6).copy(alpha = 0.08f))
                ),
                RoundedCornerShape(12.dp)
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color.White.copy(alpha = 0.87f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EventPanelCard(
    event: CalendarEvent,
    onTap: () -> Unit,
    onDelete: () -> Unit
) {
    val (startColor, endColor) = event.type.gradientColors()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    listOf(startColor.copy(alpha = 0.12f), endColor.copy(alpha = 0.12f))
                ),
                RoundedCornerShape(14.dp)
            )
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onTap)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(5.dp)
                .height(46.dp)
                .background(
                    Brush.verticalGradient(listOf(startColor, endColor)),
                    RoundedCornerShape(3.dp)
                )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = event.title,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = stringResource(
                    R.string.event_time_range,
                    event.startHour.padded(),
                    event.startMinute.padded(),
                    event.endHour.padded(),
                    event.endMinute.padded()
                ),
                color = Color.White.copy(alpha = 0.87f),
                fontSize = 12.sp
            )
            if (event.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = event.description,
                    color = Color.White.copy(alpha = 0.87f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(listOf(startColor, endColor)),
                    RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = stringResource(event.type.labelRes),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(R.string.cd_delete_event),
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// ── Shared helpers ─────────────────────────────────────────────

internal fun Int.padded(): String = toString().padStart(2, '0')

internal fun EventType.gradientColors(): Pair<Color, Color> = when (this) {
    EventType.WORK -> Color(0xFF6366F1) to Color(0xFF8B5CF6)
    EventType.PERSONAL -> Color(0xFF06B6D4) to Color(0xFF3B82F6)
    EventType.HEALTH -> Color(0xFF10B981) to Color(0xFF059669)
    EventType.SOCIAL -> Color(0xFFF59E0B) to Color(0xFFD97706)
    EventType.OTHER -> Color(0xFF94A3B8) to Color(0xFF64748B)
}
