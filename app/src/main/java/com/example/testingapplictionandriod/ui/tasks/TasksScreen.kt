package com.example.testingapplictionandriod.ui.tasks

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RadioButtonUnchecked
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testingapplictionandriod.domain.model.Task
import com.example.testingapplictionandriod.domain.model.TaskPriority
import com.example.testingapplictionandriod.ui.components.CalenderlyButton
import com.example.testingapplictionandriod.ui.components.CalenderlyFab
import com.example.testingapplictionandriod.ui.components.UpgradeBanner

private val CBlue = Color(0xFF2564CF)
private val CBlueDark = Color(0xFF1A3A80)
private val CBlueBgSoft = Color(0xFFEEF5FF)
private val CDanger = Color(0xFFDE3030)
private val CDangerBg = Color(0xFFFBDADA)
private val CDangerInk = Color(0xFF801515)
private val CSuccess = Color(0xFF22C55E)
private val CWarn = Color(0xFFF97316)
private val CWarnBg = Color(0xFFFFF7ED)
private val CWarnInk = Color(0xFF8C6E1A)
private val CInk = Color(0xFF1A1A2E)
private val CMuted = Color(0xFF8B8BA7)
private val CMuted2 = Color(0xFFC8C8D8)
private val CHair = Color(0xFFE4E4ED)
private val CSurface = Color(0xFFF7F7FB)

@Composable
fun TasksScreen(
    uiState: TasksUiState,
    onShowCreate: () -> Unit,
    onDismissCreate: () -> Unit,
    onTitleChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onPriorityChange: (TaskPriority) -> Unit,
    onCreateTask: () -> Unit,
    onToggleComplete: (String) -> Unit,
    onShowDetail: (String) -> Unit,
    onDeleteTask: (String) -> Unit,
    onBackToList: () -> Unit
) {
    when (uiState.currentScreen) {
        is TasksNavScreen.List -> TasksListScreen(
            uiState = uiState,
            onShowCreate = onShowCreate,
            onToggleComplete = onToggleComplete,
            onShowDetail = onShowDetail
        )
        is TasksNavScreen.Create -> TasksListScreen(
            uiState = uiState,
            onShowCreate = onShowCreate,
            onToggleComplete = onToggleComplete,
            onShowDetail = onShowDetail,
            showCreateSheet = true,
            onDismissCreate = onDismissCreate,
            onTitleChange = onTitleChange,
            onNotesChange = onNotesChange,
            onPriorityChange = onPriorityChange,
            onCreateTask = onCreateTask
        )
        is TasksNavScreen.Detail -> {
            val taskId = (uiState.currentScreen as TasksNavScreen.Detail).taskId
            val task = uiState.tasks.find { it.id == taskId }
            if (task != null) {
                TaskDetailSheet(
                    task = task,
                    onDismiss = onBackToList,
                    onToggleComplete = { onToggleComplete(taskId) },
                    onDelete = { onDeleteTask(taskId) }
                )
            } else {
                onBackToList()
            }
        }
    }
}

@Composable
private fun TasksListScreen(
    uiState: TasksUiState,
    onShowCreate: () -> Unit,
    onToggleComplete: (String) -> Unit,
    onShowDetail: (String) -> Unit,
    showCreateSheet: Boolean = false,
    onDismissCreate: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onNotesChange: (String) -> Unit = {},
    onPriorityChange: (TaskPriority) -> Unit = {},
    onCreateTask: () -> Unit = {}
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
                    text = "Tasks",
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
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "Search tasks", tint = CInk)
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More options", tint = CInk)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Brush.verticalGradient(listOf(CHair, CHair)))
            )

            if (uiState.tasks.isEmpty()) {
                TasksEmptyState()
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    val today = uiState.tasks.filter { !it.isCompleted }
                    val completed = uiState.tasks.filter { it.isCompleted }

                    if (today.isNotEmpty()) {
                        item {
                            TaskSectionHeader(label = "TODAY", count = today.size)
                        }
                        items(today) { task ->
                            TaskRow(
                                task = task,
                                onToggle = { onToggleComplete(task.id) },
                                onTap = { onShowDetail(task.id) }
                            )
                        }
                    }
                    if (completed.isNotEmpty()) {
                        item {
                            TaskSectionHeader(label = "COMPLETED", count = completed.size)
                        }
                        items(completed) { task ->
                            TaskRow(
                                task = task,
                                onToggle = { onToggleComplete(task.id) },
                                onTap = { onShowDetail(task.id) }
                            )
                        }
                    }
                    item { Spacer(Modifier.height(200.dp)) }
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

        if (showCreateSheet) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(listOf(Color(0x731A1A2E), Color(0x731A1A2E))))
                    .clickable(onClick = onDismissCreate)
            )
            CreateTaskSheet(
                uiState = uiState,
                onDismiss = onDismissCreate,
                onTitleChange = onTitleChange,
                onNotesChange = onNotesChange,
                onPriorityChange = onPriorityChange,
                onCreate = onCreateTask,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun TaskSectionHeader(label: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Brush.verticalGradient(listOf(CSurface, CSurface)))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = CMuted,
            letterSpacing = 0.5.sp
        )
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(Color.White, Color.White)), RoundedCornerShape(8.dp))
                .padding(horizontal = 6.dp, vertical = 1.dp)
        ) {
            Text(text = count.toString(), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = CMuted2)
        }
    }
}

@Composable
private fun TaskRow(
    task: Task,
    onToggle: () -> Unit,
    onTap: () -> Unit
) {
    val isOverdue = !task.isCompleted && task.priority == TaskPriority.HIGH
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Color.White, Color.White)))
            .clickable(onClick = onTap)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .clickable(onClick = onToggle),
            contentAlignment = Alignment.Center
        ) {
            if (task.isCompleted) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Mark as incomplete",
                    tint = CSuccess,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.RadioButtonUnchecked,
                    contentDescription = "Mark as complete",
                    tint = if (isOverdue) CDanger else CMuted2,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (task.isCompleted) CMuted2 else CInk,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
            )
            if (!task.isCompleted) {
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (task.priority != TaskPriority.NONE) {
                        val (bg, ink) = when (task.priority) {
                            TaskPriority.HIGH -> CDangerBg to CDangerInk
                            TaskPriority.MEDIUM -> CWarnBg to CWarnInk
                            else -> CSurface to CMuted
                        }
                        Box(
                            modifier = Modifier
                                .background(bg, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = task.priority.name,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ink
                            )
                        }
                    }
                }
            }
        }
        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More options", tint = CMuted2)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(start = 52.dp)
            .background(Brush.verticalGradient(listOf(CHair, CHair)))
    )
}

@Composable
private fun TasksEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(
                    Brush.linearGradient(listOf(CBlueBgSoft, CSurface)),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "No tasks",
                tint = CBlue,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = "All clear!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = CInk
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "You don't have any tasks yet. Tap the + button to add your first one.",
            fontSize = 14.sp,
            color = CMuted,
            lineHeight = 20.sp
        )
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun CreateTaskSheet(
    uiState: TasksUiState,
    onDismiss: () -> Unit,
    onTitleChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onPriorityChange: (TaskPriority) -> Unit,
    onCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(listOf(Color.White, CSurface)),
                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(4.dp)
                .background(Brush.linearGradient(listOf(CHair, CHair)), RoundedCornerShape(2.dp))
                .align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "New Task",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = CInk,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Brush.verticalGradient(listOf(CHair, CHair)))
        )
        Spacer(Modifier.height(16.dp))

        // Title input
        Text(
            text = "TASK",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = CMuted,
            letterSpacing = 0.3.sp
        )
        Spacer(Modifier.height(6.dp))
        BasicTextField(
            value = uiState.newTaskTitle,
            onValueChange = onTitleChange,
            textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = CInk),
            cursorBrush = SolidColor(CBlue),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(Color.White, Color.White)),
                    RoundedCornerShape(12.dp)
                )
                .padding(14.dp),
            decorationBox = { inner ->
                if (uiState.newTaskTitle.isEmpty()) {
                    Text(text = "What needs to be done?", color = CMuted2, fontSize = 16.sp)
                }
                inner()
            }
        )
        Spacer(Modifier.height(14.dp))

        // Priority selector
        Text(
            text = "PRIORITY",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = CMuted,
            letterSpacing = 0.3.sp
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                Triple(TaskPriority.LOW, CSurface, CMuted),
                Triple(TaskPriority.MEDIUM, CWarnBg, CWarnInk),
                Triple(TaskPriority.HIGH, CDangerBg, CDangerInk)
            ).forEach { (priority, bg, textColor) ->
                val isSelected = uiState.newTaskPriority == priority
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .background(
                            if (isSelected) Brush.linearGradient(listOf(bg, bg))
                            else Brush.linearGradient(listOf(Color.White, Color.White)),
                            RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onPriorityChange(priority) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = priority.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) textColor else CMuted
                    )
                }
            }
        }
        Spacer(Modifier.height(14.dp))

        // Notes
        Text(
            text = "NOTES",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = CMuted,
            letterSpacing = 0.3.sp
        )
        Spacer(Modifier.height(6.dp))
        BasicTextField(
            value = uiState.newTaskNotes,
            onValueChange = onNotesChange,
            textStyle = TextStyle(fontSize = 13.sp, color = CInk),
            cursorBrush = SolidColor(CBlue),
            minLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(Color.White, Color.White)),
                    RoundedCornerShape(12.dp)
                )
                .padding(14.dp),
            decorationBox = { inner ->
                if (uiState.newTaskNotes.isEmpty()) {
                    Text(text = "Add details…", color = CMuted2, fontSize = 13.sp)
                }
                inner()
            }
        )
        Spacer(Modifier.height(20.dp))
        CalenderlyButton(
            label = "Create Task",
            onClick = onCreate,
            enabled = uiState.newTaskTitle.isNotBlank()
        )
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun TaskDetailSheet(
    task: Task,
    onDismiss: () -> Unit,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.White, CSurface)))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Back header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Brush.verticalGradient(listOf(Color.White, Color.White)))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.RadioButtonUnchecked,
                        contentDescription = "Back to tasks",
                        tint = CInk,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = "Task Detail",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CInk,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
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
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .background(
                                Brush.linearGradient(
                                    if (task.isCompleted) listOf(CSuccess, CSuccess)
                                    else listOf(Color.White, Color.White)
                                ),
                                CircleShape
                            )
                            .clip(CircleShape)
                            .clickable(onClick = onToggleComplete),
                        contentAlignment = Alignment.Center
                    ) {
                        if (task.isCompleted) {
                            Text(text = "✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = task.title,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = CInk,
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                        )
                        if (task.priority != TaskPriority.NONE) {
                            Spacer(Modifier.height(8.dp))
                            val (bg, ink) = when (task.priority) {
                                TaskPriority.HIGH -> CDangerBg to CDangerInk
                                TaskPriority.MEDIUM -> CWarnBg to CWarnInk
                                else -> CSurface to CMuted
                            }
                            Box(
                                modifier = Modifier
                                    .background(bg, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(task.priority.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ink)
                            }
                        }
                    }
                }

                if (task.notes.isNotBlank()) {
                    Spacer(Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Brush.verticalGradient(listOf(CHair, CHair)))
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "NOTES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CMuted,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = task.notes,
                        fontSize = 14.sp,
                        color = Color(0xFF3D3D5C),
                        lineHeight = 20.sp
                    )
                }

                Spacer(Modifier.height(32.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .background(
                                Brush.linearGradient(listOf(CBlueBgSoft, CBlueBgSoft)),
                                RoundedCornerShape(12.dp)
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .clickable(onClick = onToggleComplete),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (task.isCompleted) "Undo" else "Mark Done",
                            color = CBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .background(
                                Brush.linearGradient(listOf(CDangerBg, CDangerBg)),
                                RoundedCornerShape(12.dp)
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .clickable(onClick = onDelete),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Delete", color = CDanger, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
