package com.example.testingapplictionandriod.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
    onShowAddEvent: () -> Unit,
    onDismissAddEvent: () -> Unit,
    onNewEventTitleChange: (String) -> Unit,
    onNewEventDescriptionChange: (String) -> Unit,
    onNewEventTypeChange: (EventType) -> Unit,
    onNewEventStartHourChange: (Int) -> Unit,
    onNewEventStartMinuteChange: (Int) -> Unit,
    onNewEventEndHourChange: (Int) -> Unit,
    onNewEventEndMinuteChange: (Int) -> Unit,
    onAddEvent: () -> Unit,
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

            CalendarGrid(
                year = uiState.displayedYear,
                month = uiState.displayedMonth,
                selectedDay = uiState.selectedDay,
                events = uiState.events,
                onDaySelected = onDaySelected
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(horizontal = 16.dp)
                    .background(Color.White.copy(alpha = 0.1f))
            )

            EventsSection(
                modifier = Modifier.weight(1f),
                year = uiState.displayedYear,
                month = uiState.displayedMonth,
                selectedDay = uiState.selectedDay,
                events = uiState.events,
                onDeleteEvent = onDeleteEvent
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .size(56.dp)
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                    ),
                    CircleShape
                )
                .clip(CircleShape)
                .clickable(onClick = onShowAddEvent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.cd_add_event),
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        if (uiState.showAddEventDialog) {
            AddEventDialog(
                uiState = uiState,
                onDismiss = onDismissAddEvent,
                onTitleChange = onNewEventTitleChange,
                onDescriptionChange = onNewEventDescriptionChange,
                onTypeChange = onNewEventTypeChange,
                onStartHourChange = onNewEventStartHourChange,
                onStartMinuteChange = onNewEventStartMinuteChange,
                onEndHourChange = onNewEventEndHourChange,
                onEndMinuteChange = onNewEventEndMinuteChange,
                onConfirm = onAddEvent
            )
        }
    }
}

@Composable
private fun CalendarTopBar(
    year: Int,
    month: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onGoToToday: () -> Unit
) {
    val cal = java.util.Calendar.getInstance().apply { set(year, month - 1, 1) }
    val monthName = SimpleDateFormat("MMMM", Locale.getDefault()).format(cal.time)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.app_calendar_title),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        )

        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF6366F1).copy(alpha = 0.3f), Color(0xFF8B5CF6).copy(alpha = 0.3f))
                    ),
                    RoundedCornerShape(20.dp)
                )
                .clickable(onClick = onGoToToday)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = stringResource(R.string.btn_today),
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onPreviousMonth) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.cd_previous_month),
                tint = Color.White
            )
        }

        Text(
            text = "$monthName $year",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(140.dp)
        )

        IconButton(onClick = onNextMonth) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.cd_next_month),
                tint = Color.White
            )
        }
    }
}

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
    val firstDayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1

    val cells = List(firstDayOfWeek) { 0 } + (1..daysInMonth).toList()
    val remainder = cells.size % 7
    val paddedCells = if (remainder == 0) cells else cells + List(7 - remainder) { 0 }

    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            stringArrayResource(R.array.calendar_day_headers).forEach { dayLabel ->
                Text(
                    text = dayLabel,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = Color.White.copy(alpha = 0.87f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        paddedCells.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { day ->
                    CalendarDayCell(
                        day = day,
                        isToday = day != 0 && year == todayYear && month == todayMonth && day == todayDay,
                        isSelected = day != 0 && day == selectedDay,
                        hasEvents = day != 0 && events.any {
                            it.year == year && it.month == month && it.day == day
                        },
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
    hasEvents: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        if (day != 0) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        when {
                            isSelected -> Modifier.background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                                ),
                                CircleShape
                            )
                            isToday -> Modifier.background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF06B6D4), Color(0xFF3B82F6))
                                ),
                                CircleShape
                            )
                            else -> Modifier
                        }
                    )
                    .clip(CircleShape)
                    .clickable(onClick = onClick),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = day.toString(),
                        color = when {
                            isSelected || isToday -> Color.White
                            else -> Color.White.copy(alpha = 0.87f)
                        },
                        fontSize = 13.sp,
                        fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                    if (hasEvents) {
                        Spacer(modifier = Modifier.height(1.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(
                                    brush = if (isSelected || isToday)
                                        Brush.linearGradient(listOf(Color.White.copy(alpha = 0.6f), Color.White))
                                    else
                                        Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))),
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EventsSection(
    modifier: Modifier = Modifier,
    year: Int,
    month: Int,
    selectedDay: Int?,
    events: List<CalendarEvent>,
    onDeleteEvent: (String) -> Unit
) {
    val selectedEvents = events.filter { e ->
        e.year == year && e.month == month && e.day == selectedDay
    }.sortedWith(compareBy({ it.startHour }, { it.startMinute }))

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (selectedDay != null) {
                    val cal = java.util.Calendar.getInstance().apply { set(year, month - 1, selectedDay) }
                    SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(cal.time)
                } else {
                    stringResource(R.string.events_title_no_selection)
                },
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )

            if (selectedDay != null) {
                Text(
                    text = "${selectedEvents.size} ${stringResource(R.string.events_count_suffix)}",
                    color = Color.White.copy(alpha = 0.87f),
                    fontSize = 13.sp
                )
            }
        }

        if (selectedDay == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF6366F1).copy(alpha = 0.08f),
                                Color(0xFF8B5CF6).copy(alpha = 0.08f)
                            )
                        ),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.events_tap_to_select),
                    color = Color.White.copy(alpha = 0.87f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else if (selectedEvents.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF6366F1).copy(alpha = 0.08f),
                                Color(0xFF8B5CF6).copy(alpha = 0.08f)
                            )
                        ),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.events_no_events),
                    color = Color.White.copy(alpha = 0.87f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 16.dp, end = 16.dp, bottom = 80.dp
                )
            ) {
                items(selectedEvents, key = { it.id }) { event ->
                    EventCard(event = event, onDelete = { onDeleteEvent(event.id) })
                }
            }
        }
    }
}

@Composable
private fun EventCard(
    event: CalendarEvent,
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
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(44.dp)
                .background(
                    Brush.verticalGradient(listOf(startColor, endColor)),
                    RoundedCornerShape(2.dp)
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
            Spacer(modifier = Modifier.height(2.dp))
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
                tint = Color.White.copy(alpha = 0.87f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun AddEventDialog(
    uiState: CalendarUiState,
    onDismiss: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onTypeChange: (EventType) -> Unit,
    onStartHourChange: (Int) -> Unit,
    onStartMinuteChange: (Int) -> Unit,
    onEndHourChange: (Int) -> Unit,
    onEndMinuteChange: (Int) -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1A1741), Color(0xFF2D2A5E))
                    ),
                    RoundedCornerShape(20.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.dialog_add_event_title),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.newEventTitle,
                    onValueChange = onTitleChange,
                    label = {
                        Text(
                            stringResource(R.string.dialog_event_title_hint),
                            color = Color.White.copy(alpha = 0.87f)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White.copy(alpha = 0.87f),
                        focusedBorderColor = Color(0xFF6366F1),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                        cursorColor = Color(0xFF6366F1),
                        focusedLabelColor = Color(0xFF6366F1),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.87f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.newEventDescription,
                    onValueChange = onDescriptionChange,
                    label = {
                        Text(
                            stringResource(R.string.dialog_event_desc_hint),
                            color = Color.White.copy(alpha = 0.87f)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White.copy(alpha = 0.87f),
                        focusedBorderColor = Color(0xFF6366F1),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                        cursorColor = Color(0xFF6366F1),
                        focusedLabelColor = Color(0xFF6366F1),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.87f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.dialog_event_type_label),
                    color = Color.White.copy(alpha = 0.87f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    EventType.entries.forEach { type ->
                        val isSelected = uiState.newEventType == type
                        val (sc, ec) = type.gradientColors()
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (isSelected) {
                                        Brush.linearGradient(listOf(sc, ec))
                                    } else {
                                        Brush.linearGradient(
                                            listOf(sc.copy(alpha = 0.15f), ec.copy(alpha = 0.15f))
                                        )
                                    },
                                    RoundedCornerShape(8.dp)
                                )
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onTypeChange(type) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(type.labelRes),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.dialog_start_time),
                            color = Color.White.copy(alpha = 0.87f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        TimeSelector(
                            hour = uiState.newEventStartHour,
                            minute = uiState.newEventStartMinute,
                            onHourChange = onStartHourChange,
                            onMinuteChange = onStartMinuteChange,
                            hourContentDescription = stringResource(R.string.cd_start_hour),
                            minuteContentDescription = stringResource(R.string.cd_start_minute)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.dialog_end_time),
                            color = Color.White.copy(alpha = 0.87f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        TimeSelector(
                            hour = uiState.newEventEndHour,
                            minute = uiState.newEventEndMinute,
                            onHourChange = onEndHourChange,
                            onMinuteChange = onEndMinuteChange,
                            hourContentDescription = stringResource(R.string.cd_end_hour),
                            minuteContentDescription = stringResource(R.string.cd_end_minute)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.05f),
                                        Color.White.copy(alpha = 0.12f)
                                    )
                                ),
                                RoundedCornerShape(12.dp)
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .clickable(onClick = onDismiss)
                            .padding(vertical = 13.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.btn_cancel),
                            color = Color.White.copy(alpha = 0.87f),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (uiState.newEventTitle.isNotBlank()) {
                                    Brush.linearGradient(
                                        listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                                    )
                                } else {
                                    Brush.linearGradient(
                                        listOf(
                                            Color(0xFF6366F1).copy(alpha = 0.4f),
                                            Color(0xFF8B5CF6).copy(alpha = 0.4f)
                                        )
                                    )
                                },
                                RoundedCornerShape(12.dp)
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .clickable(
                                enabled = uiState.newEventTitle.isNotBlank(),
                                onClick = onConfirm
                            )
                            .padding(vertical = 13.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.btn_add_event),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeSelector(
    hour: Int,
    minute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    hourContentDescription: String,
    minuteContentDescription: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    listOf(Color.White.copy(alpha = 0.05f), Color.White.copy(alpha = 0.10f))
                ),
                RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TimeSpinner(
            value = hour,
            onIncrement = { onHourChange((hour + 1) % 24) },
            onDecrement = { onHourChange((hour - 1 + 24) % 24) },
            contentDescription = hourContentDescription
        )

        Text(
            text = stringResource(R.string.time_separator),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        TimeSpinner(
            value = minute,
            onIncrement = { onMinuteChange((minute + 15) % 60) },
            onDecrement = { onMinuteChange((minute - 15 + 60) % 60) },
            contentDescription = minuteContentDescription
        )
    }
}

@Composable
private fun TimeSpinner(
    value: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    contentDescription: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onIncrement,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = stringResource(R.string.cd_increase_value, contentDescription),
                tint = Color.White.copy(alpha = 0.87f),
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = value.padded(),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        IconButton(
            onClick = onDecrement,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = stringResource(R.string.cd_decrease_value, contentDescription),
                tint = Color.White.copy(alpha = 0.87f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

private fun Int.padded(): String = toString().padStart(2, '0')

private fun EventType.gradientColors(): Pair<Color, Color> = when (this) {
    EventType.WORK -> Color(0xFF6366F1) to Color(0xFF8B5CF6)
    EventType.PERSONAL -> Color(0xFF06B6D4) to Color(0xFF3B82F6)
    EventType.HEALTH -> Color(0xFF10B981) to Color(0xFF059669)
    EventType.SOCIAL -> Color(0xFFF59E0B) to Color(0xFFD97706)
    EventType.OTHER -> Color(0xFF94A3B8) to Color(0xFF64748B)
}
