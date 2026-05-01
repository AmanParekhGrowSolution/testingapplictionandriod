package com.example.testingapplictionandriod.ui.tasks

import androidx.lifecycle.ViewModel
import com.example.testingapplictionandriod.domain.model.Task
import com.example.testingapplictionandriod.domain.model.TaskPriority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TasksViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    fun onShowCreate() {
        _uiState.update {
            it.copy(
                currentScreen = TasksNavScreen.Create,
                newTaskTitle = "",
                newTaskNotes = "",
                newTaskPriority = TaskPriority.NONE
            )
        }
    }

    fun onDismissCreate() {
        _uiState.update { it.copy(currentScreen = TasksNavScreen.List) }
    }

    fun onShowDetail(taskId: String) {
        _uiState.update { it.copy(currentScreen = TasksNavScreen.Detail(taskId), selectedTaskId = taskId) }
    }

    fun onBackToList() {
        _uiState.update { it.copy(currentScreen = TasksNavScreen.List, selectedTaskId = null) }
    }

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(newTaskTitle = title) }
    }

    fun onNotesChange(notes: String) {
        _uiState.update { it.copy(newTaskNotes = notes) }
    }

    fun onPriorityChange(priority: TaskPriority) {
        _uiState.update { it.copy(newTaskPriority = priority) }
    }

    fun onCreateTask() {
        val state = _uiState.value
        if (state.newTaskTitle.isBlank()) return
        val task = Task(
            title = state.newTaskTitle.trim(),
            notes = state.newTaskNotes.trim(),
            priority = state.newTaskPriority
        )
        _uiState.update {
            it.copy(
                tasks = it.tasks + task,
                currentScreen = TasksNavScreen.List,
                newTaskTitle = "",
                newTaskNotes = "",
                newTaskPriority = TaskPriority.NONE
            )
        }
    }

    fun onToggleComplete(taskId: String) {
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks.map { t ->
                    if (t.id == taskId) t.copy(isCompleted = !t.isCompleted) else t
                }
            )
        }
    }

    fun onDeleteTask(taskId: String) {
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks.filter { it.id != taskId },
                currentScreen = TasksNavScreen.List,
                selectedTaskId = null
            )
        }
    }
}
