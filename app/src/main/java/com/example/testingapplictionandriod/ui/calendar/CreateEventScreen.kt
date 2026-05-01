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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testingapplictionandriod.R
import com.example.testingapplictionandriod.domain.model.EventType
import java.text.SimpleDateFormat
import java.util.Locale

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
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0F0C29), Color(0xFF302B63), Color(0xFF24243E))
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar: close | title | save
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.cd_close),
                        tint = Color.White
                    )
                }
                Text(
                    text = stringResource(R.string.screen_create_event),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .background(
                            if (uiState.newEventTitle.isNotBlank())
                                Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)))
                            else
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF6366F1).copy(alpha = 0.40f),
                                        Color(0xFF8B5CF6).copy(alpha = 0.40f)
                                    )
                                ),
                            RoundedCornerShape(999.dp)
                        )
                        .clip(RoundedCornerShape(999.dp))
                        .clickable(enabled = uiState.newEventTitle.isNotBlank(), onClick = onSave)
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_save),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            // Scrollable form body
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Large title input
                BasicTextField(
                    value = uiState.newEventTitle,
                    onValueChange = onTitleChange,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    cursorBrush = SolidColor(Color(0xFF6366F1)),
                    decorationBox = { innerField ->
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (uiState.newEventTitle.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.hint_event_title),
                                    color = Color.White.copy(alpha = 0.30f),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            innerField()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                // Subtle divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.10f))
                )

                // Date row
                val dateLabel = uiState.selectedDay?.let { day ->
                    val cal = java.util.Calendar.getInstance().apply {
                        set(uiState.displayedYear, uiState.displayedMonth - 1, day)
                    }
                    SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(cal.time)
                } ?: run {
                    val cal = java.util.Calendar.getInstance()
                    SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(cal.time)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.06f), RoundedCornerShape(14.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = stringResource(R.string.label_date),
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = dateLabel,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Start / End time row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        SectionLabel(text = stringResource(R.string.dialog_start_time))
                        Spacer(modifier = Modifier.height(6.dp))
                        TimeSpinnerBlock(
                            hour = uiState.newEventStartHour,
                            minute = uiState.newEventStartMinute,
                            onHourChange = onStartHourChange,
                            onMinuteChange = onStartMinuteChange,
                            hourDescription = stringResource(R.string.cd_start_hour),
                            minuteDescription = stringResource(R.string.cd_start_minute)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        SectionLabel(text = stringResource(R.string.dialog_end_time))
                        Spacer(modifier = Modifier.height(6.dp))
                        TimeSpinnerBlock(
                            hour = uiState.newEventEndHour,
                            minute = uiState.newEventEndMinute,
                            onHourChange = onEndHourChange,
                            onMinuteChange = onEndMinuteChange,
                            hourDescription = stringResource(R.string.cd_end_hour),
                            minuteDescription = stringResource(R.string.cd_end_minute)
                        )
                    }
                }

                // Event type chips
                SectionLabel(text = stringResource(R.string.dialog_event_type_label))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    EventType.entries.forEach { type ->
                        val isSelected = uiState.newEventType == type
                        val (sc, ec) = type.gradientColors()
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (isSelected) Brush.linearGradient(listOf(sc, ec))
                                    else Brush.linearGradient(
                                        listOf(sc.copy(alpha = 0.15f), ec.copy(alpha = 0.15f))
                                    ),
                                    RoundedCornerShape(10.dp)
                                )
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onTypeChange(type) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(type.labelRes),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Notes / description
                SectionLabel(text = stringResource(R.string.label_notes))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.06f), RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    BasicTextField(
                        value = uiState.newEventDescription,
                        onValueChange = onDescriptionChange,
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 15.sp
                        ),
                        cursorBrush = SolidColor(Color(0xFF6366F1)),
                        minLines = 3,
                        decorationBox = { innerField ->
                            if (uiState.newEventDescription.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.hint_add_notes),
                                    color = Color.White.copy(alpha = 0.28f),
                                    fontSize = 15.sp
                                )
                            }
                            innerField()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        color = Color.White.copy(alpha = 0.50f),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp
    )
}

@Composable
private fun TimeSpinnerBlock(
    hour: Int,
    minute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    hourDescription: String,
    minuteDescription: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        SpinnerWheel(
            value = hour,
            onIncrement = { onHourChange((hour + 1) % 24) },
            onDecrement = { onHourChange((hour - 1 + 24) % 24) },
            description = hourDescription
        )
        Text(
            text = stringResource(R.string.time_separator),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        SpinnerWheel(
            value = minute,
            onIncrement = { onMinuteChange((minute + 15) % 60) },
            onDecrement = { onMinuteChange((minute - 15 + 60) % 60) },
            description = minuteDescription
        )
    }
}

@Composable
private fun SpinnerWheel(
    value: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    description: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onIncrement, modifier = Modifier.size(40.dp)) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = stringResource(R.string.cd_increase_value, description),
                tint = Color.White.copy(alpha = 0.87f),
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = value.padded(),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        IconButton(onClick = onDecrement, modifier = Modifier.size(40.dp)) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = stringResource(R.string.cd_decrease_value, description),
                tint = Color.White.copy(alpha = 0.87f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
