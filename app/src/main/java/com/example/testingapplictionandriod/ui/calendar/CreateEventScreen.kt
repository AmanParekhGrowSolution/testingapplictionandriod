package com.example.testingapplictionandriod.ui.calendar

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testingapplictionandriod.domain.model.EventType

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
private val CSuccessColor = Color(0xFF22C55E)
private val CWarnColor = Color(0xFFF97316)
private val CCoralColor = Color(0xFFE85C4A)
private val CMintInk = Color(0xFF00897B)

@Composable
fun CreateEventScreen(
    uiState: CalendarUiState,
    onDismiss: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onTypeChange: (EventType) -> Unit,
    onStartHourChange: (Int) -> Unit,
    onStartMinuteChange: (Int) -> Unit,
    onEndHourChange: (Int) -> Unit,
    onEndMinuteChange: (Int) -> Unit,
    onSave: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.White, CSurface)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
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
                        .clickable(onClick = onDismiss),
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
                    text = "New Event",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = CInk,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Brush.linearGradient(listOf(CBlue, CBlueDark)))
                        .clickable(
                            enabled = uiState.newEventTitle.isNotBlank(),
                            onClick = onSave
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Save",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
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
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title field
                InputField(
                    label = "TITLE",
                    value = uiState.newEventTitle,
                    onValueChange = onTitleChange,
                    placeholder = "Event title",
                    isBig = true
                )

                // Date/time row
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    val selectedDay = uiState.selectedDay
                    val month = MONTH_NAMES_SHORT[uiState.displayedMonth - 1]
                    Box(modifier = Modifier.weight(1f)) {
                        InputFieldStatic(
                            label = "DATE",
                            value = if (selectedDay != null) "$month $selectedDay" else "$month —"
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        InputFieldStatic(
                            label = "START",
                            value = formatTime(uiState.newEventStartHour, uiState.newEventStartMinute)
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        InputFieldStatic(
                            label = "END",
                            value = formatTime(uiState.newEventEndHour, uiState.newEventEndMinute)
                        )
                    }
                }

                // Time spinners row
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "START TIME",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CMuted,
                            letterSpacing = 0.3.sp
                        )
                        Spacer(Modifier.height(6.dp))
                        TimeSpinner(
                            hour = uiState.newEventStartHour,
                            minute = uiState.newEventStartMinute,
                            onHourChange = onStartHourChange,
                            onMinuteChange = onStartMinuteChange
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "END TIME",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CMuted,
                            letterSpacing = 0.3.sp
                        )
                        Spacer(Modifier.height(6.dp))
                        TimeSpinner(
                            hour = uiState.newEventEndHour,
                            minute = uiState.newEventEndMinute,
                            onHourChange = onEndHourChange,
                            onMinuteChange = onEndMinuteChange
                        )
                    }
                }

                // Event type
                Column {
                    Text(
                        text = "TYPE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CMuted,
                        letterSpacing = 0.3.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        EventType.entries.forEach { type ->
                            val isSelected = uiState.newEventType == type
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .background(
                                        if (isSelected) Brush.linearGradient(
                                            listOf(
                                                eventColor(type),
                                                eventColor(type)
                                            )
                                        )
                                        else Brush.linearGradient(listOf(Color.White, Color.White)),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onTypeChange(type) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = type.name.take(4),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else CMuted
                                )
                            }
                        }
                    }
                }

                // Description field
                InputField(
                    label = "NOTES",
                    value = uiState.newEventDescription,
                    onValueChange = onDescriptionChange,
                    placeholder = "Add notes…",
                    minLines = 3
                )
            }
        }
    }
}

@Composable
private fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isBig: Boolean = false,
    minLines: Int = 1
) {
    Column {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = CMuted,
            letterSpacing = 0.3.sp
        )
        Spacer(Modifier.height(6.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = if (isBig) 16.sp else 14.sp,
                fontWeight = if (isBig) FontWeight.SemiBold else FontWeight.Normal,
                color = CInk
            ),
            cursorBrush = SolidColor(CBlue),
            minLines = minLines,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(Color.White, Color.White)),
                    RoundedCornerShape(12.dp)
                )
                .padding(14.dp),
            decorationBox = { inner ->
                if (value.isEmpty()) {
                    Text(text = placeholder, color = CMuted2, fontSize = if (isBig) 16.sp else 14.sp)
                }
                inner()
            }
        )
    }
}

@Composable
private fun InputFieldStatic(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = CMuted,
            letterSpacing = 0.3.sp
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(
                    Brush.verticalGradient(listOf(Color.White, Color.White)),
                    RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = CInk)
        }
    }
}

@Composable
private fun TimeSpinner(
    hour: Int,
    minute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(listOf(Color.White, Color.White)),
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        SpinnerPicker(
            value = hour,
            range = 0..23,
            onInc = { onHourChange((hour + 1) % 24) },
            onDec = { onHourChange((hour - 1 + 24) % 24) },
            format = { "%02d".format(it) }
        )
        Text(
            text = ":",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = CInk,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        SpinnerPicker(
            value = minute,
            range = 0..59,
            onInc = { onMinuteChange((minute + 15) % 60) },
            onDec = { onMinuteChange((minute - 15 + 60) % 60) },
            format = { "%02d".format(it) }
        )
    }
}

@Composable
private fun SpinnerPicker(
    value: Int,
    range: IntRange,
    onInc: () -> Unit,
    onDec: () -> Unit,
    format: (Int) -> String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(CSurface, RoundedCornerShape(8.dp))
                .clickable(onClick = onInc),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "▲", fontSize = 10.sp, color = CBlue)
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = format(value),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = CInk
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(CSurface, RoundedCornerShape(8.dp))
                .clickable(onClick = onDec),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "▼", fontSize = 10.sp, color = CBlue)
        }
    }
}

private val MONTH_NAMES_SHORT = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
)
