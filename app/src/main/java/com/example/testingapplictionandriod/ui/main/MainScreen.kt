package com.example.testingapplictionandriod.ui.main

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testingapplictionandriod.domain.model.TaskPriority
import com.example.testingapplictionandriod.ui.app.AppViewModel
import com.example.testingapplictionandriod.ui.app.MainTab
import com.example.testingapplictionandriod.ui.calendar.CalendarScreen
import com.example.testingapplictionandriod.ui.calendar.CalendarViewModel
import com.example.testingapplictionandriod.ui.mine.MineScreen
import com.example.testingapplictionandriod.ui.notes.NotesScreen
import com.example.testingapplictionandriod.ui.notes.NotesViewModel
import com.example.testingapplictionandriod.ui.tasks.TasksScreen
import com.example.testingapplictionandriod.ui.tasks.TasksViewModel

private val CBlue = Color(0xFF2564CF)
private val CMuted = Color(0xFF8B8BA7)
private val CHair = Color(0xFFE4E4ED)
private val CInk = Color(0xFF1A1A2E)

@Composable
fun MainScreen(appViewModel: AppViewModel) {
    val appState by appViewModel.uiState.collectAsStateWithLifecycle()
    val calendarViewModel: CalendarViewModel = viewModel()
    val tasksViewModel: TasksViewModel = viewModel()
    val notesViewModel: NotesViewModel = viewModel()

    val calendarUiState by calendarViewModel.uiState.collectAsStateWithLifecycle()
    val tasksUiState by tasksViewModel.uiState.collectAsStateWithLifecycle()
    val notesUiState by notesViewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        // Content area (fills above tab bar)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 83.dp)
        ) {
            when (appState.selectedTab) {
                MainTab.CALENDAR -> CalendarScreen(
                    uiState = calendarUiState,
                    onPreviousMonth = calendarViewModel::onPreviousMonth,
                    onNextMonth = calendarViewModel::onNextMonth,
                    onGoToToday = calendarViewModel::onGoToToday,
                    onDaySelected = calendarViewModel::onDaySelected,
                    onShowCreateEvent = calendarViewModel::onShowCreateEvent,
                    onDismissCreateEvent = calendarViewModel::onDismissCreateEvent,
                    onNewEventTitleChange = calendarViewModel::onNewEventTitleChange,
                    onNewEventDescriptionChange = calendarViewModel::onNewEventDescriptionChange,
                    onNewEventTypeChange = calendarViewModel::onNewEventTypeChange,
                    onNewEventStartHourChange = calendarViewModel::onNewEventStartHourChange,
                    onNewEventStartMinuteChange = calendarViewModel::onNewEventStartMinuteChange,
                    onNewEventEndHourChange = calendarViewModel::onNewEventEndHourChange,
                    onNewEventEndMinuteChange = calendarViewModel::onNewEventEndMinuteChange,
                    onAddEvent = calendarViewModel::onAddEvent,
                    onDeleteEvent = calendarViewModel::onDeleteEvent,
                    onShowEventDetail = calendarViewModel::onShowEventDetail,
                    onBackToCalendar = calendarViewModel::onBackToCalendar,
                    onShowDayView = calendarViewModel::onShowDayView,
                    onShowWeekView = calendarViewModel::onShowWeekView,
                    onToggleNotifications = calendarViewModel::onToggleNotifications
                )
                MainTab.TASKS -> TasksScreen(
                    uiState = tasksUiState,
                    onShowCreate = tasksViewModel::onShowCreate,
                    onDismissCreate = tasksViewModel::onDismissCreate,
                    onTitleChange = tasksViewModel::onTitleChange,
                    onNotesChange = tasksViewModel::onNotesChange,
                    onPriorityChange = tasksViewModel::onPriorityChange,
                    onCreateTask = tasksViewModel::onCreateTask,
                    onToggleComplete = tasksViewModel::onToggleComplete,
                    onShowDetail = tasksViewModel::onShowDetail,
                    onDeleteTask = tasksViewModel::onDeleteTask,
                    onBackToList = tasksViewModel::onBackToList
                )
                MainTab.NOTES -> NotesScreen(
                    uiState = notesUiState,
                    onShowCreate = notesViewModel::onShowCreate,
                    onDismissCreate = notesViewModel::onDismissCreate,
                    onTitleChange = notesViewModel::onTitleChange,
                    onContentChange = notesViewModel::onContentChange,
                    onTagChange = notesViewModel::onTagChange,
                    onCreateNote = notesViewModel::onCreateNote,
                    onShowDetail = notesViewModel::onShowDetail,
                    onDeleteNote = notesViewModel::onDeleteNote,
                    onBackToList = notesViewModel::onBackToList
                )
                MainTab.MINE -> MineScreen()
            }
        }

        // Bottom tab bar
        CalenderlyTabBar(
            selectedTab = appState.selectedTab,
            onTabSelected = appViewModel::onTabSelected,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun CalenderlyTabBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        TabItem(MainTab.CALENDAR, "Calendar", Icons.Filled.CalendarMonth),
        TabItem(MainTab.TASKS, "Tasks", Icons.Filled.CheckCircle),
        TabItem(MainTab.NOTES, "Notes", Icons.Filled.NoteAlt),
        TabItem(MainTab.MINE, "Mine", Icons.Filled.Person)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Brush.verticalGradient(listOf(CHair, CHair)))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(83.dp)
                .background(Brush.verticalGradient(listOf(Color.White, Color(0xFFFDFDFF))))
                .navigationBarsPadding()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabs.forEach { tab ->
                val isSelected = selectedTab == tab.id
                val iconColor = if (isSelected) CBlue else CMuted
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onTabSelected(tab.id) }
                        .padding(top = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = tab.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = iconColor
                    )
                }
            }
        }
    }
}

private data class TabItem(val id: MainTab, val label: String, val icon: ImageVector)
