package com.example.testingapplictionandriod.ui.calendar

import androidx.lifecycle.ViewModel
import com.example.testingapplictionandriod.domain.model.CalendarEvent
import com.example.testingapplictionandriod.domain.model.EventType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(buildInitialState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    fun onPreviousMonth() {
        _uiState.update { state ->
            val (newYear, newMonth) = if (state.displayedMonth == 1) {
                state.displayedYear - 1 to 12
            } else {
                state.displayedYear to state.displayedMonth - 1
            }
            state.copy(displayedYear = newYear, displayedMonth = newMonth, selectedDay = null)
        }
    }

    fun onNextMonth() {
        _uiState.update { state ->
            val (newYear, newMonth) = if (state.displayedMonth == 12) {
                state.displayedYear + 1 to 1
            } else {
                state.displayedYear to state.displayedMonth + 1
            }
            state.copy(displayedYear = newYear, displayedMonth = newMonth, selectedDay = null)
        }
    }

    fun onGoToToday() {
        val today = java.util.Calendar.getInstance()
        _uiState.update {
            it.copy(
                displayedYear = today.get(java.util.Calendar.YEAR),
                displayedMonth = today.get(java.util.Calendar.MONTH) + 1,
                selectedDay = today.get(java.util.Calendar.DAY_OF_MONTH)
            )
        }
    }

    fun onDaySelected(day: Int) {
        _uiState.update { it.copy(selectedDay = day) }
    }

    fun onShowCreateEvent() {
        _uiState.update { it.copy(currentScreen = CalendarNavScreen.CreateEvent) }
    }

    fun onDismissCreateEvent() {
        _uiState.update {
            it.copy(
                currentScreen = CalendarNavScreen.Month,
                newEventTitle = "",
                newEventDescription = "",
                newEventType = EventType.PERSONAL,
                newEventStartHour = 9,
                newEventStartMinute = 0,
                newEventEndHour = 10,
                newEventEndMinute = 0
            )
        }
    }

    fun onShowEventDetail(eventId: String) {
        _uiState.update { it.copy(currentScreen = CalendarNavScreen.EventDetail(eventId)) }
    }

    fun onBackToCalendar() {
        _uiState.update { it.copy(currentScreen = CalendarNavScreen.Month) }
    }

    fun onNewEventTitleChange(title: String) {
        _uiState.update { it.copy(newEventTitle = title) }
    }

    fun onNewEventDescriptionChange(description: String) {
        _uiState.update { it.copy(newEventDescription = description) }
    }

    fun onNewEventTypeChange(type: EventType) {
        _uiState.update { it.copy(newEventType = type) }
    }

    fun onNewEventStartHourChange(hour: Int) {
        _uiState.update { it.copy(newEventStartHour = hour) }
    }

    fun onNewEventStartMinuteChange(minute: Int) {
        _uiState.update { it.copy(newEventStartMinute = minute) }
    }

    fun onNewEventEndHourChange(hour: Int) {
        _uiState.update { it.copy(newEventEndHour = hour) }
    }

    fun onNewEventEndMinuteChange(minute: Int) {
        _uiState.update { it.copy(newEventEndMinute = minute) }
    }

    fun onAddEvent() {
        val state = _uiState.value
        if (state.newEventTitle.isBlank()) return

        val today = java.util.Calendar.getInstance()
        val targetDay = state.selectedDay ?: today.get(java.util.Calendar.DAY_OF_MONTH)

        val event = CalendarEvent(
            id = UUID.randomUUID().toString(),
            title = state.newEventTitle.trim(),
            description = state.newEventDescription.trim(),
            type = state.newEventType,
            year = state.displayedYear,
            month = state.displayedMonth,
            day = targetDay,
            startHour = state.newEventStartHour,
            startMinute = state.newEventStartMinute,
            endHour = state.newEventEndHour,
            endMinute = state.newEventEndMinute
        )

        _uiState.update {
            it.copy(
                events = it.events + event,
                currentScreen = CalendarNavScreen.Month,
                newEventTitle = "",
                newEventDescription = "",
                newEventType = EventType.PERSONAL,
                newEventStartHour = 9,
                newEventStartMinute = 0,
                newEventEndHour = 10,
                newEventEndMinute = 0
            )
        }
    }

    fun onDeleteEvent(eventId: String) {
        _uiState.update {
            it.copy(
                events = it.events.filter { e -> e.id != eventId },
                currentScreen = CalendarNavScreen.Month
            )
        }
    }

    private companion object {
        fun buildInitialState(): CalendarUiState {
            val today = java.util.Calendar.getInstance()
            return CalendarUiState(
                displayedYear = today.get(java.util.Calendar.YEAR),
                displayedMonth = today.get(java.util.Calendar.MONTH) + 1,
                events = emptyList()
            )
        }
    }
}
