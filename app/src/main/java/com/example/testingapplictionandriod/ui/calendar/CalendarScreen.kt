package com.example.testingapplictionandriod.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testingapplictionandriod.domain.model.CalendarEvent
import com.example.testingapplictionandriod.domain.model.EventType
import com.example.testingapplictionandriod.ui.components.CalenderlyFab
import com.example.testingapplictionandriod.ui.components.UpgradeBanner
import java.util.Calendar
import java.util.Locale

// Calenderly design tokens
private val CBlue = Color(0xFF2564CF)
private val CBlueDark = Color(0xFF1A3A80)
private val CDangerRed = Color(0xFFDE3030)
private val CInk = Color(0xFF1A1A2E)
private val CInk3 = Color(0xFF3D3D5C)
private val CMuted = Color(0xFF8B8BA7)
private val CMuted2 = Color(0xFFC8C8D8)
private val CHair = Color(0xFFE4E4ED)
private val CSurface = Color(0xFFF7F7FB)
private val CCoralColor = Color(0xFFE85C4A)
private val CSuccessColor = Color(0xFF22C55E)
private val CWarnColor = Color(0xFFF97316)

private val MONTH_NAMES = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

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
    onBackToCalendar: () -> Unit,
    onShowDayView: () -> Unit = {},
    onShowWeekView: () -> Unit = {},
    onToggleNotifications: () -> Unit = {}
) {
    when (uiState.currentScreen) {
        is CalendarNavScreen.Month -> CalendarMonthScreen(
            uiState = uiState,
            onPreviousMonth = onPreviousMonth,
            onNextMonth = onNextMonth,
            onGoToToday = onGoToToday,
            onDaySelected = onDaySelected,
            onShowCreateEvent = onShowCreateEvent,
            onShowEventDetail = onShowEventDetail,
            onShowWeekView = onShowWeekView,
            onShowDayView = onShowDayView,
            onToggleNotifications = onToggleNotifications
        )
        is CalendarNavScreen.CreateEvent -> CreateEventScreen(
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
            val eventId = (uiState.currentScreen as CalendarNavScreen.EventDetail).eventId
            val event = uiState.events.find { it.id == eventId }
            if (event != null) {
                EventDetailScreen(
                    event = event,
                    onBack = onBackToCalendar,
                    onDelete = { onDeleteEvent(eventId) }
                )
            } else {
                onBackToCalendar()
            }
        }
        is CalendarNavScreen.DayView -> DayViewScreen(
            uiState = uiState,
            onBack = onBackToCalendar,
            onShowCreateEvent = onShowCreateEvent
        )
        is CalendarNavScreen.WeekView -> WeekViewScreen(
            uiState = uiState,
            onBack = onBackToCalendar
        )
    }
}

@Composable
private fun CalendarMonthScreen(
    uiState: CalendarUiState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onGoToToday: () -> Unit,
    onDaySelected: (Int) -> Unit,
    onShowCreateEvent: () -> Unit,
    onShowEventDetail: (String) -> Unit,
    onShowWeekView: () -> Unit,
    onShowDayView: () -> Unit,
    onToggleNotifications: () -> Unit
) {
    val today = remember { Calendar.getInstance() }
    val todayYear = today.get(Calendar.YEAR)
    val todayMonth = today.get(Calendar.MONTH) + 1
    val todayDay = today.get(Calendar.DAY_OF_MONTH)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.White, CSurface)))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CalendarTopBar(
                month = "${MONTH_NAMES[uiState.displayedMonth - 1]} ${uiState.displayedYear}",
                onSearch = {},
                onBell = onToggleNotifications,
                onToggleView = onShowWeekView
            )
            WeekdayHeader()
            MonthGrid(
                year = uiState.displayedYear,
                month = uiState.displayedMonth,
                selectedDay = uiState.selectedDay,
                todayYear = todayYear,
                todayMonth = todayMonth,
                todayDay = todayDay,
                events = uiState.events,
                onDayTap = { day ->
                    onDaySelected(day)
                }
            )
            Spacer(Modifier.weight(1f))
            UpgradeBanner()
        }

        CalenderlyFab(
            onClick = onShowCreateEvent,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 144.dp)
        )

        if (uiState.selectedDay != null && uiState.selectedDay > 0) {
            val dayEvents = uiState.events.filter { e ->
                e.year == uiState.displayedYear &&
                        e.month == uiState.displayedMonth &&
                        e.day == uiState.selectedDay
            }
            if (dayEvents.isNotEmpty()) {
                DayEventSheet(
                    events = dayEvents,
                    onEventTap = onShowEventDetail,
                    onDismiss = { onDaySelected(0) }
                )
            }
        }
    }
}

@Composable
private fun CalendarTopBar(
    month: String,
    onSearch: () -> Unit,
    onBell: () -> Unit,
    onToggleView: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Brush.verticalGradient(listOf(Color.White, Color(0xFFFDFDFF))))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onToggleView),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = month,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = CInk,
                letterSpacing = (-0.3).sp
            )
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Toggle calendar view",
                tint = CMuted,
                modifier = Modifier.size(18.dp)
            )
        }
        Row(
            modifier = Modifier
                .background(CSurface, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onToggleView)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.GridView,
                contentDescription = "Switch view",
                tint = CInk3,
                modifier = Modifier.size(16.dp)
            )
            Text(text = "Month", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = CInk)
        }
        Spacer(Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .clickable(onClick = onSearch),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search events",
                tint = CInk,
                modifier = Modifier.size(22.dp)
            )
        }
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .clickable(onClick = onBell),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notifications",
                tint = CInk,
                modifier = Modifier.size(22.dp)
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(CDangerRed, CircleShape)
                    .align(Alignment.TopEnd)
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Brush.verticalGradient(listOf(CHair, CHair)))
    )
}

@Composable
private fun WeekdayHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(Brush.verticalGradient(listOf(Color.White, Color.White)))
    ) {
        listOf("S", "M", "T", "W", "T", "F", "S").forEachIndexed { i, day ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (i == 0 || i == 6) CMuted else CInk3,
                    letterSpacing = 0.3.sp
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Brush.verticalGradient(listOf(CHair, CHair)))
    )
}

@Composable
private fun MonthGrid(
    year: Int,
    month: Int,
    selectedDay: Int?,
    todayYear: Int,
    todayMonth: Int,
    todayDay: Int,
    events: List<CalendarEvent>,
    onDayTap: (Int) -> Unit
) {
    val cells = remember(year, month) { buildMonthCells(year, month) }
    val eventsThisMonth = remember(events, year, month) {
        events.filter { it.year == year && it.month == month }
    }

    Column {
        cells.chunked(7).forEach { week ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Brush.verticalGradient(listOf(CHair, CHair)))
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp)
            ) {
                week.forEachIndexed { ci, cell ->
                    val isToday = !cell.overflow &&
                            year == todayYear && month == todayMonth && cell.day == todayDay
                    val dayEvents = if (!cell.overflow) eventsThisMonth.filter { it.day == cell.day } else emptyList()

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .background(Brush.verticalGradient(listOf(Color.White, Color.White)))
                            .clickable(enabled = !cell.overflow) { onDayTap(cell.day) }
                            .padding(start = 6.dp, end = 4.dp, top = 6.dp, bottom = 4.dp)
                    ) {
                        if (isToday) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .background(Brush.linearGradient(listOf(CBlue, CBlueDark)), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cell.day.toString(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        } else {
                            Text(
                                text = cell.day.toString(),
                                fontSize = 12.sp,
                                fontWeight = if (cell.overflow) FontWeight.Normal else FontWeight.Medium,
                                color = when {
                                    cell.overflow -> CMuted2
                                    selectedDay == cell.day -> CBlue
                                    else -> CInk
                                }
                            )
                        }
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                                .padding(top = 28.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            dayEvents.take(2).forEach { event ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(14.dp)
                                        .background(eventColor(event.type), RoundedCornerShape(3.dp))
                                        .padding(horizontal = 4.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(
                                        text = event.title,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            if (dayEvents.size > 2) {
                                Text(
                                    text = "+${dayEvents.size - 2} more",
                                    fontSize = 9.sp,
                                    color = CMuted
                                )
                            }
                        }
                    }
                    if (ci < 6) {
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxSize()
                                .background(Brush.verticalGradient(listOf(CHair, CHair)))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DayEventSheet(
    events: List<CalendarEvent>,
    onEventTap: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0x731A1A2E), Color(0x731A1A2E))))
            .clickable(onClick = onDismiss)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(Color.White, CSurface)),
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(horizontal = 20.dp)
                .clickable(enabled = false) {}
        ) {
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(4.dp)
                    .background(Brush.linearGradient(listOf(CHair, CHair)), RoundedCornerShape(2.dp))
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Events",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = CInk
            )
            Spacer(Modifier.height(12.dp))
            events.forEach { event ->
                EventSheetRow(event = event, onTap = { onEventTap(event.id) })
                Spacer(Modifier.height(8.dp))
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EventSheetRow(event: CalendarEvent, onTap: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(CSurface, CSurface)), RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onTap)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(40.dp)
                .background(eventColor(event.type), RoundedCornerShape(2.dp))
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = event.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = CInk)
            Text(
                text = "${formatTime(event.startHour, event.startMinute)} – ${formatTime(event.endHour, event.endMinute)}",
                fontSize = 12.sp,
                color = CMuted
            )
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = "View event details",
            tint = CMuted2,
            modifier = Modifier.size(16.dp)
        )
    }
}

private data class DayCell(val day: Int, val overflow: Boolean)

private fun buildMonthCells(year: Int, month: Int): List<DayCell> {
    val cal = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

    val prevMonthCal = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1)
        add(Calendar.MONTH, -1)
    }
    val daysInPrevMonth = prevMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH)

    val cells = mutableListOf<DayCell>()
    for (i in firstDayOfWeek downTo 1) {
        cells.add(DayCell(daysInPrevMonth - i + 1, overflow = true))
    }
    for (d in 1..daysInMonth) {
        cells.add(DayCell(d, overflow = false))
    }
    var next = 1
    while (cells.size < 35) {
        cells.add(DayCell(next++, overflow = true))
    }
    return cells
}

internal fun eventColor(type: EventType): Color = when (type) {
    EventType.WORK -> CBlue
    EventType.PERSONAL -> CCoralColor
    EventType.HEALTH -> CSuccessColor
    EventType.SOCIAL -> CWarnColor
    EventType.OTHER -> CMuted
}

internal fun formatTime(hour: Int, minute: Int): String {
    val amPm = if (hour < 12) "AM" else "PM"
    val h = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
    return String.format(Locale.US, "%d:%02d %s", h, minute, amPm)
}
