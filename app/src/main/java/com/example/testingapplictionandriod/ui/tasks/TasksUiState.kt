package com.example.testingapplictionandriod.ui.tasks

import com.example.testingapplictionandriod.domain.model.Task
import com.example.testingapplictionandriod.domain.model.TaskPriority

sealed interface TasksNavScreen {
    data object List : TasksNavScreen
    data object Create : TasksNavScreen
    data class Detail(val taskId: String) : TasksNavScreen
}

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val currentScreen: TasksNavScreen = TasksNavScreen.List,
    val newTaskTitle: String = "",
    val newTaskNotes: String = "",
    val newTaskPriority: TaskPriority = TaskPriority.NONE,
    val selectedTaskId: String? = null
)
