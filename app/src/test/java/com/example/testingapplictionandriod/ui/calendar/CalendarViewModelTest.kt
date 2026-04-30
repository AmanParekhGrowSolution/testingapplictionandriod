package com.example.testingapplictionandriod.ui.calendar

import app.cash.turbine.test
import com.example.testingapplictionandriod.domain.model.EventType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CalendarViewModelTest {

    @Test
    fun initialState_isNotLoading() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun initialState_hasCurrentYearAndMonth() = runTest {
        val viewModel = CalendarViewModel()
        val today = java.util.Calendar.getInstance()
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(today.get(java.util.Calendar.YEAR), state.displayedYear)
            assertEquals(today.get(java.util.Calendar.MONTH) + 1, state.displayedMonth)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun initialState_selectedDayIsNull() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.selectedDay == null)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun initialState_eventsAreEmpty() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.events.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onNextMonth_incrementsMonth() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            val initial = awaitItem()
            if (initial.displayedMonth < 12) {
                viewModel.onNextMonth()
                val next = awaitItem()
                assertEquals(initial.displayedMonth + 1, next.displayedMonth)
                assertEquals(initial.displayedYear, next.displayedYear)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onNextMonth_decemberWrapsToJanuaryNextYear() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            val initial = awaitItem()
            val initialYear = initial.displayedYear
            val stepsToDecember = (12 - initial.displayedMonth + 12) % 12
            repeat(stepsToDecember) {
                viewModel.onNextMonth()
                awaitItem()
            }
            viewModel.onNextMonth()
            val wrapped = awaitItem()
            assertEquals(1, wrapped.displayedMonth)
            assertEquals(initialYear + 1, wrapped.displayedYear)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onPreviousMonth_decrementsMonth() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            val initial = awaitItem()
            if (initial.displayedMonth > 1) {
                viewModel.onPreviousMonth()
                val prev = awaitItem()
                assertEquals(initial.displayedMonth - 1, prev.displayedMonth)
                assertEquals(initial.displayedYear, prev.displayedYear)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onPreviousMonth_januaryWrapsToDecemberPreviousYear() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            val initial = awaitItem()
            val initialYear = initial.displayedYear
            val stepsToJanuary = initial.displayedMonth - 1
            repeat(stepsToJanuary) {
                viewModel.onPreviousMonth()
                awaitItem()
            }
            viewModel.onPreviousMonth()
            val wrapped = awaitItem()
            assertEquals(12, wrapped.displayedMonth)
            assertEquals(initialYear - 1, wrapped.displayedYear)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onNextMonth_clearsSelectedDay() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            awaitItem()
            viewModel.onDaySelected(15)
            awaitItem()
            viewModel.onNextMonth()
            val afterNext = awaitItem()
            assertTrue(afterNext.selectedDay == null)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onPreviousMonth_clearsSelectedDay() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            awaitItem()
            viewModel.onDaySelected(10)
            awaitItem()
            viewModel.onPreviousMonth()
            val afterPrev = awaitItem()
            assertTrue(afterPrev.selectedDay == null)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onDaySelected_updatesSelectedDay() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            awaitItem()
            viewModel.onDaySelected(22)
            val updated = awaitItem()
            assertEquals(22, updated.selectedDay)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onShowAddEvent_setsDialogVisible() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            val initial = awaitItem()
            assertFalse(initial.showAddEventDialog)
            viewModel.onShowAddEvent()
            val afterShow = awaitItem()
            assertTrue(afterShow.showAddEventDialog)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onDismissAddEvent_hidesDialogAndResetsForm() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            awaitItem()
            viewModel.onShowAddEvent()
            awaitItem()
            viewModel.onNewEventTitleChange("Test")
            awaitItem()
            viewModel.onDismissAddEvent()
            val dismissed = awaitItem()
            assertFalse(dismissed.showAddEventDialog)
            assertEquals("", dismissed.newEventTitle)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onAddEvent_withValidTitle_addsEventToList() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            val initial = awaitItem()
            val countBefore = initial.events.size
            viewModel.onDaySelected(15)
            awaitItem()
            viewModel.onShowAddEvent()
            awaitItem()
            viewModel.onNewEventTitleChange("Team Meeting")
            awaitItem()
            viewModel.onNewEventTypeChange(EventType.WORK)
            awaitItem()
            viewModel.onAddEvent()
            val afterAdd = awaitItem()
            assertEquals(countBefore + 1, afterAdd.events.size)
            assertTrue(afterAdd.events.any { it.title == "Team Meeting" })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onAddEvent_withValidTitle_closesDialog() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            awaitItem()
            viewModel.onShowAddEvent()
            awaitItem()
            viewModel.onNewEventTitleChange("Meeting")
            awaitItem()
            viewModel.onAddEvent()
            val afterAdd = awaitItem()
            assertFalse(afterAdd.showAddEventDialog)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onAddEvent_withEmptyTitle_doesNotAddEvent() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            val initial = awaitItem()
            val countBefore = initial.events.size
            viewModel.onShowAddEvent()
            val dialogState = awaitItem()
            assertTrue(dialogState.showAddEventDialog)
            viewModel.onAddEvent()
            expectNoEvents()
            assertEquals(countBefore, viewModel.uiState.value.events.size)
            assertTrue(viewModel.uiState.value.showAddEventDialog)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onAddEvent_withBlankTitle_doesNotAddEvent() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            val initial = awaitItem()
            val countBefore = initial.events.size
            viewModel.onNewEventTitleChange("   ")
            awaitItem()
            viewModel.onAddEvent()
            expectNoEvents()
            assertEquals(countBefore, viewModel.uiState.value.events.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onDeleteEvent_removesCorrectEvent() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            val initial = awaitItem()
            val countBefore = initial.events.size
            viewModel.onDaySelected(15)
            awaitItem()
            viewModel.onShowAddEvent()
            awaitItem()
            viewModel.onNewEventTitleChange("To Delete")
            awaitItem()
            viewModel.onAddEvent()
            val afterAdd = awaitItem()
            val event = afterAdd.events.first { it.title == "To Delete" }
            viewModel.onDeleteEvent(event.id)
            val afterDelete = awaitItem()
            assertEquals(countBefore, afterDelete.events.size)
            assertFalse(afterDelete.events.any { it.id == event.id })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onNewEventTypeChange_updatesType() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            awaitItem()
            viewModel.onNewEventTypeChange(EventType.HEALTH)
            val updated = awaitItem()
            assertEquals(EventType.HEALTH, updated.newEventType)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onNewEventStartHourChange_updatesHour() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            awaitItem()
            viewModel.onNewEventStartHourChange(14)
            val updated = awaitItem()
            assertEquals(14, updated.newEventStartHour)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onGoToToday_navigatesToCurrentMonthAndSelectsToday() = runTest {
        val viewModel = CalendarViewModel()
        viewModel.uiState.test {
            awaitItem()
            repeat(3) {
                viewModel.onNextMonth()
                awaitItem()
            }
            viewModel.onGoToToday()
            val todayState = awaitItem()
            val today = java.util.Calendar.getInstance()
            assertEquals(today.get(java.util.Calendar.YEAR), todayState.displayedYear)
            assertEquals(today.get(java.util.Calendar.MONTH) + 1, todayState.displayedMonth)
            assertEquals(today.get(java.util.Calendar.DAY_OF_MONTH), todayState.selectedDay)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
