package com.example.testingapplictionandriod.ui.notes

import androidx.lifecycle.ViewModel
import com.example.testingapplictionandriod.domain.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar

class NotesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    fun onShowCreate() {
        _uiState.update {
            it.copy(
                currentScreen = NotesNavScreen.Create,
                newNoteTitle = "",
                newNoteContent = "",
                newNoteTag = ""
            )
        }
    }

    fun onDismissCreate() {
        _uiState.update { it.copy(currentScreen = NotesNavScreen.List) }
    }

    fun onShowDetail(noteId: String) {
        _uiState.update { it.copy(currentScreen = NotesNavScreen.Detail(noteId), selectedNoteId = noteId) }
    }

    fun onBackToList() {
        _uiState.update { it.copy(currentScreen = NotesNavScreen.List, selectedNoteId = null) }
    }

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(newNoteTitle = title) }
    }

    fun onContentChange(content: String) {
        _uiState.update { it.copy(newNoteContent = content) }
    }

    fun onTagChange(tag: String) {
        _uiState.update { it.copy(newNoteTag = tag) }
    }

    fun onCreateNote() {
        val state = _uiState.value
        if (state.newNoteTitle.isBlank()) return
        val today = Calendar.getInstance()
        val note = Note(
            title = state.newNoteTitle.trim(),
            content = state.newNoteContent.trim(),
            tag = state.newNoteTag.trim(),
            colorIndex = state.notes.size % 6,
            createdYear = today.get(Calendar.YEAR),
            createdMonth = today.get(Calendar.MONTH) + 1,
            createdDay = today.get(Calendar.DAY_OF_MONTH)
        )
        _uiState.update {
            it.copy(
                notes = it.notes + note,
                currentScreen = NotesNavScreen.List,
                newNoteTitle = "",
                newNoteContent = "",
                newNoteTag = ""
            )
        }
    }

    fun onDeleteNote(noteId: String) {
        _uiState.update { state ->
            state.copy(
                notes = state.notes.filter { it.id != noteId },
                currentScreen = NotesNavScreen.List,
                selectedNoteId = null
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onToggleSearch() {
        _uiState.update { it.copy(isSearching = !it.isSearching, searchQuery = "") }
    }
}
