package com.example.testingapplictionandriod.ui.calendar

import com.example.testingapplictionandriod.domain.model.EventType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CalendarViewModelTest {

    @Test
    fun initialState_hasCurrentYearAndMonth() {
        val viewModel = CalendarViewModel()
        val today = java.util.Calendar.getInstance()
        assertEquals(today.get(java.util.Calendar.YEAR), viewModel.uiState.value.displayedYear)
        assertEquals(today.get(java.util.Calendar.MONTH) + 1, viewModel.uiState.value.displayedMonth)
    }

    @Test
    fun initialState_selectedDayIsNull() {
        val viewModel = CalendarViewModel()
        assertNull(viewModel.uiState.value.selectedDay)
    }

    @Test
    fun initialState_hasSampleEvents() {
        val viewModel = CalendarViewModel()
        assertTrue(viewModel.uiState.value.events.isNotEmpty())
    }

    @Test
    fun onNextMonth_incrementsMonth() {
        val viewModel = CalendarViewModel()
        val initialMonth = viewModel.uiState.value.displayedMonth
        val initialYear = viewModel.uiState.value.displayedYear
        if (initialMonth < 12) {
            viewModel.onNextMonth()
            assertEquals(initialMonth + 1, viewModel.uiState.value.displayedMonth)
            assertEquals(initialYear, viewModel.uiState.value.displayedYear)
        }
    }

    @Test
    fun onNextMonth_decemberWrapsToJanuaryNextYear() {
        val viewModel = CalendarViewModel()
        val initialMonth = viewModel.uiState.value.displayedMonth
        val initialYear = viewModel.uiState.value.displayedYear
        val stepsToDecember = (12 - initialMonth + 12) % 12
        repeat(stepsToDecember) { viewModel.onNextMonth() }
        assertEquals(12, viewModel.uiState.value.displayedMonth)
        viewModel.onNextMonth()
        assertEquals(1, viewModel.uiState.value.displayedMonth)
        assertEquals(initialYear + 1, viewModel.uiState.value.displayedYear)
    }

    @Test
    fun onPreviousMonth_decrementsMonth() {
        val viewModel = CalendarViewModel()
        val initialMonth = viewModel.uiState.value.displayedMonth
        val initialYear = viewModel.uiState.value.displayedYear
        if (initialMonth > 1) {
            viewModel.onPreviousMonth()
            assertEquals(initialMonth - 1, viewModel.uiState.value.displayedMonth)
            assertEquals(initialYear, viewModel.uiState.value.displayedYear)
        }
    }

    @Test
    fun onPreviousMonth_januaryWrapsToDecemberPreviousYear() {
        val viewModel = CalendarViewModel()
        val initialMonth = viewModel.uiState.value.displayedMonth
        val initialYear = viewModel.uiState.value.displayedYear
        repeat(initialMonth - 1) { viewModel.onPreviousMonth() }
        assertEquals(1, viewModel.uiState.value.displayedMonth)
        viewModel.onPreviousMonth()
        assertEquals(12, viewModel.uiState.value.displayedMonth)
        assertEquals(initialYear - 1, viewModel.uiState.value.displayedYear)
    }

    @Test
    fun onNextMonth_clearsSelectedDay() {
        val viewModel = CalendarViewModel()
        viewModel.onDaySelected(15)
        viewModel.onNextMonth()
        assertNull(viewModel.uiState.value.selectedDay)
    }

    @Test
    fun onPreviousMonth_clearsSelectedDay() {
        val viewModel = CalendarViewModel()
        viewModel.onDaySelected(10)
        viewModel.onPreviousMonth()
        assertNull(viewModel.uiState.value.selectedDay)
    }

    @Test
    fun onDaySelected_updatesSelectedDay() {
        val viewModel = CalendarViewModel()
        viewModel.onDaySelected(22)
        assertEquals(22, viewModel.uiState.value.selectedDay)
    }

    @Test
    fun onShowAddEvent_setsDialogVisible() {
        val viewModel = CalendarViewModel()
        assertFalse(viewModel.uiState.value.showAddEventDialog)
        viewModel.onShowAddEvent()
        assertTrue(viewModel.uiState.value.showAddEventDialog)
    }

    @Test
    fun onDismissAddEvent_hidesDialogAndResetsForm() {
        val viewModel = CalendarViewModel()
        viewModel.onShowAddEvent()
        viewModel.onNewEventTitleChange("Test")
        viewModel.onDismissAddEvent()
        assertFalse(viewModel.uiState.value.showAddEventDialog)
        assertEquals("", viewModel.uiState.value.newEventTitle)
    }

    @Test
    fun onAddEvent_withValidTitle_addsEventToList() {
        val viewModel = CalendarViewModel()
        val countBefore = viewModel.uiState.value.events.size
        viewModel.onDaySelected(15)
        viewModel.onShowAddEvent()
        viewModel.onNewEventTitleChange("Team Meeting")
        viewModel.onNewEventTypeChange(EventType.WORK)
        viewModel.onAddEvent()
        assertEquals(countBefore + 1, viewModel.uiState.value.events.size)
        assertTrue(viewModel.uiState.value.events.any { it.title == "Team Meeting" })
    }

    @Test
    fun onAddEvent_withValidTitle_closesDialog() {
        val viewModel = CalendarViewModel()
        viewModel.onShowAddEvent()
        viewModel.onNewEventTitleChange("Meeting")
        viewModel.onAddEvent()
        assertFalse(viewModel.uiState.value.showAddEventDialog)
    }

    @Test
    fun onAddEvent_withEmptyTitle_doesNotAddEvent() {
        val viewModel = CalendarViewModel()
        val countBefore = viewModel.uiState.value.events.size
        viewModel.onShowAddEvent()
        viewModel.onNewEventTitleChange("")
        viewModel.onAddEvent()
        assertEquals(countBefore, viewModel.uiState.value.events.size)
        assertTrue(viewModel.uiState.value.showAddEventDialog)
    }

    @Test
    fun onAddEvent_withBlankTitle_doesNotAddEvent() {
        val viewModel = CalendarViewModel()
        val countBefore = viewModel.uiState.value.events.size
        viewModel.onShowAddEvent()
        viewModel.onNewEventTitleChange("   ")
        viewModel.onAddEvent()
        assertEquals(countBefore, viewModel.uiState.value.events.size)
    }

    @Test
    fun onDeleteEvent_removesCorrectEvent() {
        val viewModel = CalendarViewModel()
        viewModel.onDaySelected(15)
        viewModel.onShowAddEvent()
        viewModel.onNewEventTitleChange("To Delete")
        viewModel.onAddEvent()
        val event = viewModel.uiState.value.events.first { it.title == "To Delete" }
        val countBefore = viewModel.uiState.value.events.size
        viewModel.onDeleteEvent(event.id)
        assertEquals(countBefore - 1, viewModel.uiState.value.events.size)
        assertFalse(viewModel.uiState.value.events.any { it.id == event.id })
    }

    @Test
    fun onNewEventTypeChange_updatesType() {
        val viewModel = CalendarViewModel()
        viewModel.onNewEventTypeChange(EventType.HEALTH)
        assertEquals(EventType.HEALTH, viewModel.uiState.value.newEventType)
    }

    @Test
    fun onNewEventStartHourChange_updatesHour() {
        val viewModel = CalendarViewModel()
        viewModel.onNewEventStartHourChange(14)
        assertEquals(14, viewModel.uiState.value.newEventStartHour)
    }

    @Test
    fun onGoToToday_navigatesToCurrentMonthAndSelectsToday() {
        val viewModel = CalendarViewModel()
        repeat(3) { viewModel.onNextMonth() }
        viewModel.onGoToToday()
        val today = java.util.Calendar.getInstance()
        assertEquals(today.get(java.util.Calendar.YEAR), viewModel.uiState.value.displayedYear)
        assertEquals(today.get(java.util.Calendar.MONTH) + 1, viewModel.uiState.value.displayedMonth)
        assertEquals(today.get(java.util.Calendar.DAY_OF_MONTH), viewModel.uiState.value.selectedDay)
    }
}
