package com.example.testingapplictionandriod.ui.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testingapplictionandriod.domain.model.Note
import com.example.testingapplictionandriod.ui.components.CalenderlyFab
import com.example.testingapplictionandriod.ui.components.UpgradeBanner
import java.util.Locale

private val CBlue = Color(0xFF2564CF)
private val CBlueDark = Color(0xFF1A3A80)
private val CBlueBgSoft = Color(0xFFEEF5FF)
private val CDanger = Color(0xFFDE3030)
private val CDangerBg = Color(0xFFFBDADA)
private val CInk = Color(0xFF1A1A2E)
private val CInk3 = Color(0xFF3D3D5C)
private val CMuted = Color(0xFF8B8BA7)
private val CMuted2 = Color(0xFFC8C8D8)
private val CHair = Color(0xFFE4E4ED)
private val CSurface = Color(0xFFF7F7FB)
private val CCoralBg = Color(0xFFFFF0EE)

private val NOTE_COLORS = listOf(
    CBlueBgSoft, CCoralBg, Color(0xFFFFF7ED), CSurface, Color(0xFFE8F5E9), Color(0xFFF3E5F5)
)
private val MONTH_SHORT = listOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")

@Composable
fun NotesScreen(
    uiState: NotesUiState,
    onShowCreate: () -> Unit,
    onDismissCreate: () -> Unit,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onTagChange: (String) -> Unit,
    onCreateNote: () -> Unit,
    onShowDetail: (String) -> Unit,
    onDeleteNote: (String) -> Unit,
    onBackToList: () -> Unit
) {
    when (uiState.currentScreen) {
        is NotesNavScreen.List -> NotesListScreen(
            uiState = uiState,
            onShowCreate = onShowCreate,
            onShowDetail = onShowDetail
        )
        is NotesNavScreen.Create -> NoteCreateScreen(
            uiState = uiState,
            onBack = onDismissCreate,
            onTitleChange = onTitleChange,
            onContentChange = onContentChange,
            onTagChange = onTagChange,
            onSave = onCreateNote
        )
        is NotesNavScreen.Detail -> {
            val noteId = (uiState.currentScreen as NotesNavScreen.Detail).noteId
            val note = uiState.notes.find { it.id == noteId }
            if (note != null) {
                NoteDetailScreen(
                    note = note,
                    onBack = onBackToList,
                    onDelete = { onDeleteNote(noteId) }
                )
            } else {
                onBackToList()
            }
        }
    }
}

@Composable
private fun NotesListScreen(
    uiState: NotesUiState,
    onShowCreate: () -> Unit,
    onShowDetail: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.White, CSurface)))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Brush.verticalGradient(listOf(Color.White, Color.White)))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CInk,
                    modifier = Modifier.weight(1f),
                    letterSpacing = (-0.3).sp
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "Search notes", tint = CInk)
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.GridView, contentDescription = "Toggle grid view", tint = CInk)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Brush.verticalGradient(listOf(CHair, CHair)))
            )
            // Search bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(Brush.verticalGradient(listOf(Color.White, Color.White)))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CSurface, RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = CMuted, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Search notes…", fontSize = 13.sp, color = CMuted)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Brush.verticalGradient(listOf(CHair, CHair)))
            )

            if (uiState.notes.isEmpty()) {
                NotesEmptyState()
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.notes) { note ->
                        NoteCard(note = note, onTap = { onShowDetail(note.id) })
                    }
                    item { Spacer(Modifier.height(120.dp)) }
                    item { Spacer(Modifier.height(120.dp)) }
                }
            }

            UpgradeBanner()
        }

        CalenderlyFab(
            onClick = onShowCreate,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 144.dp)
        )
    }
}

@Composable
private fun NoteCard(note: Note, onTap: () -> Unit) {
    val bgColor = NOTE_COLORS.getOrElse(note.colorIndex) { CSurface }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onTap)
            .padding(14.dp)
    ) {
        if (note.tag.isNotBlank()) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.linearGradient(listOf(Color.White.copy(alpha = 0.87f), Color.White.copy(alpha = 0.87f))),
                        RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = note.tag.uppercase(Locale.getDefault()),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CMuted,
                    letterSpacing = 0.3.sp
                )
            }
            Spacer(Modifier.height(8.dp))
        }
        Text(
            text = note.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = CInk,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        if (note.content.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = note.content,
                fontSize = 12.sp,
                color = CInk3,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )
        }
        Spacer(Modifier.height(8.dp))
        val dateStr = "${MONTH_SHORT.getOrElse(note.createdMonth - 1) { "" }} ${note.createdDay}"
        Text(text = dateStr, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = CMuted, letterSpacing = 0.2.sp)
    }
}

@Composable
private fun NotesEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(
                    Brush.linearGradient(listOf(CBlueBgSoft, CCoralBg)),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.NoteAlt,
                contentDescription = "No notes",
                tint = CBlue,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Your notes live here",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = CInk
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Capture ideas, meeting minutes and quick thoughts. Tap + to start.",
            fontSize = 14.sp,
            color = CMuted,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun NoteCreateScreen(
    uiState: NotesUiState,
    onBack: () -> Unit,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onTagChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.White, CSurface)))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(Brush.verticalGradient(listOf(Color.White, Color.White)))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Go back",
                        tint = CInk,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = "New Note",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CInk,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Brush.linearGradient(listOf(CBlue, CBlueDark)))
                        .clickable(
                            enabled = uiState.newNoteTitle.isNotBlank(),
                            onClick = onSave
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = "Save", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Brush.verticalGradient(listOf(CHair, CHair)))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Title
                BasicTextField(
                    value = uiState.newNoteTitle,
                    onValueChange = onTitleChange,
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = CInk
                    ),
                    cursorBrush = SolidColor(CBlue),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { inner ->
                        if (uiState.newNoteTitle.isEmpty()) {
                            Text(text = "Note title…", color = CMuted2, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                        inner()
                    }
                )
                // Tag
                BasicTextField(
                    value = uiState.newNoteTag,
                    onValueChange = onTagChange,
                    textStyle = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CBlue
                    ),
                    cursorBrush = SolidColor(CBlue),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { inner ->
                        if (uiState.newNoteTag.isEmpty()) {
                            Text(text = "Add tag…", color = CMuted2, fontSize = 13.sp)
                        }
                        inner()
                    }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Brush.verticalGradient(listOf(CHair, CHair)))
                )
                // Content
                BasicTextField(
                    value = uiState.newNoteContent,
                    onValueChange = onContentChange,
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        color = CInk,
                        lineHeight = 22.sp
                    ),
                    cursorBrush = SolidColor(CBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    decorationBox = { inner ->
                        if (uiState.newNoteContent.isEmpty()) {
                            Text(text = "Start writing…", color = CMuted2, fontSize = 15.sp)
                        }
                        inner()
                    }
                )
            }
        }
    }
}

@Composable
private fun NoteDetailScreen(
    note: Note,
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
    val bgColor = NOTE_COLORS.getOrElse(note.colorIndex) { CSurface }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(bgColor, Color.White)))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Brush.verticalGradient(listOf(bgColor, bgColor)))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Go back",
                        tint = CInk,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onDelete),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Save,
                        contentDescription = "Delete note",
                        tint = CDanger,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                if (note.tag.isNotBlank()) {
                    Text(
                        text = note.tag.uppercase(Locale.getDefault()),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CBlue,
                        letterSpacing = 0.3.sp
                    )
                    Spacer(Modifier.height(8.dp))
                }
                Text(
                    text = note.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = CInk
                )
                Spacer(Modifier.height(8.dp))
                val dateStr = "${MONTH_SHORT.getOrElse(note.createdMonth - 1) { "" }} ${note.createdDay}, ${note.createdYear}"
                Text(text = dateStr, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = CMuted, letterSpacing = 0.3.sp)
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Brush.verticalGradient(listOf(CHair, CHair)))
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = note.content.ifBlank { "No content" },
                    fontSize = 15.sp,
                    color = CInk,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
