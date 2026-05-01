package com.example.testingapplictionandriod.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testingapplictionandriod.domain.model.CalendarEvent
import com.example.testingapplictionandriod.ui.components.CalenderlyFab
import com.example.testingapplictionandriod.ui.components.UpgradeBanner
import java.util.Calendar

private val CBlue = Color(0xFF2564CF)
private val CBlueDark = Color(0xFF1A3A80)
private val CDangerRed = Color(0xFFDE3030)
private val CInk = Color(0xFF1A1A2E)
private val CMuted = Color(0xFF8B8BA7)
private val CHair = Color(0xFFE4E4ED)
private val CSurface = Color(0xFFF7F7FB)

private val MONTH_NAMES_DAY = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)
private val WEEK_DAYS = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
private val HOUR_LABELS = listOf("8 AM", "9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM")

@Composable
fun DayViewScreen(
    uiState: CalendarUiState,
    onBack: () -> Unit,
    onShowCreateEvent: () -> Unit
) {
    val today = remember { Calendar.getInstance() }
    val displayDay = uiState.selectedDay ?: today.get(Calendar.DAY_OF_MONTH)
    val todayDay = today.get(Calendar.DAY_OF_MONTH)
    val todayMonth = today.get(Calendar.MONTH) + 1
    val todayYear = today.get(Calendar.YEAR)

    val isToday = displayDay == todayDay &&
            uiState.displayedMonth == todayMonth &&
            uiState.displayedYear == todayYear

    val dayEvents = uiState.events.filter { e ->
        e.year == uiState.displayedYear &&
                e.month == uiState.displayedMonth &&
                e.day == displayDay
    }

    // Build week strip around displayed day
    val cal = remember(uiState.displayedYear, uiState.displayedMonth, displayDay) {
        Calendar.getInstance().apply {
            set(uiState.displayedYear, uiState.displayedMonth - 1, displayDay)
        }
    }
    val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1 // 0=Sun
    val weekDays = remember(displayDay) {
        (-dayOfWeek..6 - dayOfWeek).map { offset ->
            val c = Calendar.getInstance().apply {
                set(uiState.displayedYear, uiState.displayedMonth - 1, displayDay)
                add(Calendar.DAY_OF_MONTH, offset)
            }
            Triple(
                WEEK_DAYS[c.get(Calendar.DAY_OF_WEEK) - 1],
                c.get(Calendar.DAY_OF_MONTH),
                offset == 0
            )
        }
    }

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
                        contentDescription = "Back to month view",
                        tint = CInk,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isToday) "Today" else MONTH_NAMES_DAY[uiState.displayedMonth - 1],
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = CInk
                    )
                    Text(
                        text = "${MONTH_NAMES_DAY[uiState.displayedMonth - 1]} $displayDay, ${uiState.displayedYear}",
                        fontSize = 11.sp,
                        color = CMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search events",
                        tint = CInk,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Brush.verticalGradient(listOf(CHair, CHair)))
            )

            // Week date strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .background(Brush.verticalGradient(listOf(Color.White, Color.White)))
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                weekDays.forEach { (dayLabel, dayNum, isCurrentDay) ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isCurrentDay) Brush.linearGradient(listOf(CBlue, CBlueDark))
                                else Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
                            )
                            .padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dayLabel.uppercase().take(3),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isCurrentDay) Color.White.copy(alpha = 0.87f) else CMuted
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = dayNum.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrentDay) Color.White else CInk
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Brush.verticalGradient(listOf(CHair, CHair)))
            )

            // Timeline
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Brush.verticalGradient(listOf(Color.White, Color.White)))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(modifier = Modifier.fillMaxWidth().height(16.dp))
                    HOUR_LABELS.forEachIndexed { i, label ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CMuted,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(start = 12.dp, top = -5.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .padding(start = 56.dp)
                                    .align(Alignment.TopCenter)
                                    .background(Brush.verticalGradient(listOf(CHair, CHair)))
                            )
                            // Events for this hour
                            dayEvents.filter { e ->
                                e.startHour == i + 8
                            }.forEach { event ->
                                val durationH = ((event.endHour * 60 + event.endMinute) -
                                        (event.startHour * 60 + event.startMinute)).coerceAtLeast(30)
                                val heightDp = (durationH.toFloat() / 60f * 50f).dp
                                Box(
                                    modifier = Modifier
                                        .padding(start = 60.dp, end = 12.dp, top = 2.dp)
                                        .fillMaxWidth()
                                        .height(heightDp.coerceAtLeast(30.dp))
                                        .background(
                                            Brush.linearGradient(
                                                listOf(eventColor(event.type), eventColor(event.type))
                                            ),
                                            RoundedCornerShape(10.dp)
                                        )
                                        .padding(8.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = event.title,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "${formatTime(event.startHour, event.startMinute)}–${formatTime(event.endHour, event.endMinute)}",
                                            fontSize = 10.sp,
                                            color = Color.White.copy(alpha = 0.87f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth().height(120.dp))
                }
            }

            UpgradeBanner()
        }

        CalenderlyFab(
            onClick = onShowCreateEvent,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 144.dp)
        )
    }
}
