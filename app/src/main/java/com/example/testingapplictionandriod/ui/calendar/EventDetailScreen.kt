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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testingapplictionandriod.domain.model.CalendarEvent
import com.example.testingapplictionandriod.domain.model.EventType

private val CBlue = Color(0xFF2564CF)
private val CBlueDark = Color(0xFF1A3A80)
private val CBlueBgSoft = Color(0xFFEEF5FF)
private val CDanger = Color(0xFFDE3030)
private val CDangerBg = Color(0xFFFBDADA)
private val CInk = Color(0xFF1A1A2E)
private val CInk3 = Color(0xFF3D3D5C)
private val CMuted = Color(0xFF8B8BA7)
private val CHair = Color(0xFFE4E4ED)
private val CSurface = Color(0xFFF7F7FB)

private val MONTH_NAMES_FULL = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

@Composable
fun EventDetailScreen(
    event: CalendarEvent,
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
    val typeColor = eventColor(event.type)

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
                    .height(56.dp)
                    .background(Brush.verticalGradient(listOf(Color.White, Color.White)))
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
                Text(
                    text = "Event Details",
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
                // Event title card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(listOf(Color.White, CSurface)),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(24.dp)
                                    .background(typeColor, RoundedCornerShape(2.dp))
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = event.title,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                color = CInk,
                                letterSpacing = (-0.3).sp
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .background(CBlueBgSoft, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = event.type.name.lowercase()
                                    .replaceFirstChar { it.uppercase() },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CBlue
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Date and time info
                DetailRow(
                    icon = Icons.Filled.CalendarMonth,
                    title = "${MONTH_NAMES_FULL[event.month - 1]} ${event.day}, ${event.year}",
                    subtitle = "${formatTime(event.startHour, event.startMinute)} – ${formatTime(event.endHour, event.endMinute)}"
                )
                Spacer(Modifier.height(8.dp))
                DetailRow(
                    icon = Icons.Filled.Schedule,
                    title = "Duration",
                    subtitle = formatDuration(event.startHour, event.startMinute, event.endHour, event.endMinute)
                )

                if (event.description.isNotBlank()) {
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
                        text = event.description,
                        fontSize = 14.sp,
                        color = CInk3,
                        lineHeight = 20.sp
                    )
                }

                Spacer(Modifier.height(32.dp))

                // Action buttons
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
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Edit", color = CBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
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

@Composable
private fun DetailRow(icon: ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(listOf(Color.White, CSurface)),
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(CSurface, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CMuted,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = CInk)
            Text(text = subtitle, fontSize = 12.sp, color = CMuted)
        }
    }
}

private fun formatDuration(startH: Int, startM: Int, endH: Int, endM: Int): String {
    val startMins = startH * 60 + startM
    val endMins = endH * 60 + endM
    val diff = endMins - startMins
    if (diff <= 0) return "—"
    val h = diff / 60
    val m = diff % 60
    return when {
        h == 0 -> "$m min"
        m == 0 -> "$h hr"
        else -> "$h hr $m min"
    }
}
