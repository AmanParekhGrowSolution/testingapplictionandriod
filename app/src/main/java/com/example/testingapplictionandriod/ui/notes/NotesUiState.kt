package com.example.testingapplictionandriod.ui.notes

import com.example.testingapplictionandriod.domain.model.Note

sealed interface NotesNavScreen {
    data object List : NotesNavScreen
    data object Create : NotesNavScreen
    data class Detail(val noteId: String) : NotesNavScreen
}

data class NotesUiState(
    val notes: List<Note> = emptyList(),
    val currentScreen: NotesNavScreen = NotesNavScreen.List,
    val newNoteTitle: String = "",
    val newNoteContent: String = "",
    val newNoteTag: String = "",
    val selectedNoteId: String? = null,
    val searchQuery: String = "",
    val isSearching: Boolean = false
)
