package com.example.testingapplictionandriod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testingapplictionandriod.ui.calendar.CalendarScreen
import com.example.testingapplictionandriod.ui.calendar.CalendarViewModel
import com.example.testingapplictionandriod.ui.theme.TestingapplictionandriodTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestingapplictionandriodTheme {
                val viewModel: CalendarViewModel = viewModel()
                val uiState = viewModel.uiState.collectAsStateWithLifecycle()
                CalendarScreen(
                    uiState = uiState.value,
                    onPreviousMonth = viewModel::onPreviousMonth,
                    onNextMonth = viewModel::onNextMonth,
                    onGoToToday = viewModel::onGoToToday,
                    onDaySelected = viewModel::onDaySelected,
                    onShowAddEvent = viewModel::onShowAddEvent,
                    onDismissAddEvent = viewModel::onDismissAddEvent,
                    onNewEventTitleChange = viewModel::onNewEventTitleChange,
                    onNewEventDescriptionChange = viewModel::onNewEventDescriptionChange,
                    onNewEventTypeChange = viewModel::onNewEventTypeChange,
                    onNewEventStartHourChange = viewModel::onNewEventStartHourChange,
                    onNewEventStartMinuteChange = viewModel::onNewEventStartMinuteChange,
                    onNewEventEndHourChange = viewModel::onNewEventEndHourChange,
                    onNewEventEndMinuteChange = viewModel::onNewEventEndMinuteChange,
                    onAddEvent = viewModel::onAddEvent,
                    onDeleteEvent = viewModel::onDeleteEvent
                )
            }
        }
    }
}
