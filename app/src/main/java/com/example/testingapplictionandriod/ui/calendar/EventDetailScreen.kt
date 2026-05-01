package com.example.testingapplictionandriod.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testingapplictionandriod.R
import com.example.testingapplictionandriod.domain.model.CalendarEvent
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EventDetailScreen(
    event: CalendarEvent,
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
    val (startColor, endColor) = event.type.gradientColors()

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

            // ── Hero header ──────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(listOf(startColor, endColor))
                    )
            ) {
                Column {
                    // Action row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.cd_back),
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = onDelete) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = stringResource(R.string.cd_delete_event),
                                tint = Color.White
                            )
                        }
                    }

                    // Event metadata
                    Column(
                        modifier = Modifier.padding(
                            start = 20.dp, end = 20.dp, top = 4.dp, bottom = 28.dp
                        )
                    ) {
                        // Category pill
                        Box(
                            modifier = Modifier
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF6366F1).copy(alpha = 0.20f), Color(0xFF8B5CF6).copy(alpha = 0.20f))
                                    ),
                                    RoundedCornerShape(999.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = stringResource(event.type.labelRes),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = event.title,
                            color = Color.White,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 34.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        val cal = java.util.Calendar.getInstance().apply {
                            set(event.year, event.month - 1, event.day)
                        }
                        val dateStr = SimpleDateFormat(
                            "EEEE, MMM d", Locale.getDefault()
                        ).format(cal.time)
                        Text(
                            text = "$dateStr · ${event.startHour.padded()}:${event.startMinute.padded()} – ${event.endHour.padded()}:${event.endMinute.padded()}",
                            color = Color.White.copy(alpha = 0.87f),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // ── Body content ─────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Stats card
                val durationMins = (event.endHour * 60 + event.endMinute) -
                        (event.startHour * 60 + event.startMinute)
                val durationText = when {
                    durationMins <= 0 -> stringResource(R.string.duration_na)
                    durationMins < 60 -> stringResource(R.string.duration_minutes, durationMins)
                    durationMins % 60 == 0 -> stringResource(R.string.duration_hours, durationMins / 60)
                    else -> stringResource(
                        R.string.duration_hours_minutes, durationMins / 60, durationMins % 60
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF6366F1).copy(alpha = 0.08f), Color(0xFF8B5CF6).copy(alpha = 0.08f))
                            ),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatItem(
                        label = stringResource(R.string.label_duration),
                        value = durationText,
                        color = startColor
                    )
                    StatDivider()
                    StatItem(
                        label = stringResource(R.string.label_event_type),
                        value = stringResource(event.type.labelRes),
                        color = endColor
                    )
                    StatDivider()
                    StatItem(
                        label = stringResource(R.string.label_date),
                        value = "${event.day}/${event.month}",
                        color = startColor
                    )
                }

                // Time detail card
                DetailSection(title = stringResource(R.string.label_time)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.dialog_start_time).uppercase(),
                                color = Color.White.copy(alpha = 0.45f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${event.startHour.padded()}:${event.startMinute.padded()}",
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "→",
                            color = Color.White.copy(alpha = 0.45f),
                            fontSize = 20.sp
                        )
                        Column {
                            Text(
                                text = stringResource(R.string.dialog_end_time).uppercase(),
                                color = Color.White.copy(alpha = 0.45f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${event.endHour.padded()}:${event.endMinute.padded()}",
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Description / notes card (only if non-empty)
                if (event.description.isNotEmpty()) {
                    DetailSection(title = stringResource(R.string.label_notes)) {
                        Text(
                            text = event.description,
                            color = Color.White.copy(alpha = 0.87f),
                            fontSize = 15.sp,
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.50f),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(36.dp)
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF6366F1).copy(alpha = 0.12f), Color(0xFF8B5CF6).copy(alpha = 0.12f))
                )
            )
    )
}

@Composable
private fun DetailSection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF6366F1).copy(alpha = 0.06f), Color(0xFF8B5CF6).copy(alpha = 0.06f))
                ),
                RoundedCornerShape(14.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = title.uppercase(),
            color = Color.White.copy(alpha = 0.38f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        content()
    }
}
