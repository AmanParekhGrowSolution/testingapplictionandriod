package com.example.testingapplictionandriod.ui.calendar

import com.example.testingapplictionandriod.domain.model.CalendarEvent
import com.example.testingapplictionandriod.domain.model.EventType

data class CalendarUiState(
    val displayedYear: Int = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR),
    val displayedMonth: Int = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1,
    val selectedDay: Int? = null,
    val events: List<CalendarEvent> = emptyList(),
    val isLoading: Boolean = false,
    val showAddEventDialog: Boolean = false,
    val newEventTitle: String = "",
    val newEventDescription: String = "",
    val newEventType: EventType = EventType.PERSONAL,
    val newEventStartHour: Int = 9,
    val newEventStartMinute: Int = 0,
    val newEventEndHour: Int = 10,
    val newEventEndMinute: Int = 0
)
