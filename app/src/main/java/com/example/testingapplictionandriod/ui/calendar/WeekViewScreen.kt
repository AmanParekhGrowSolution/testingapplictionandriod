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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GridView
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

private val WEEK_DAY_LETTERS = listOf("M", "T", "W", "T", "F", "S", "S")

@Composable
fun WeekViewScreen(
    uiState: CalendarUiState,
    onBack: () -> Unit
) {
    val today = remember { Calendar.getInstance() }
    val todayYear = today.get(Calendar.YEAR)
    val todayMonth = today.get(Calendar.MONTH) + 1
    val todayDay = today.get(Calendar.DAY_OF_MONTH)

    // Find the start of the week containing the selected day or today
    val anchorDay = uiState.selectedDay ?: todayDay
    val anchorCal = remember(uiState.displayedYear, uiState.displayedMonth, anchorDay) {
        Calendar.getInstance().apply {
            set(uiState.displayedYear, uiState.displayedMonth - 1, anchorDay)
        }
    }
    val dayOfWeek = anchorCal.get(Calendar.DAY_OF_WEEK) // 1=Sun
    val offsetToMonday = if (dayOfWeek == Calendar.SUNDAY) -6 else 2 - dayOfWeek

    val weekDays = remember(uiState.displayedYear, uiState.displayedMonth, anchorDay) {
        (0..6).map { i ->
            Calendar.getInstance().apply {
                set(uiState.displayedYear, uiState.displayedMonth - 1, anchorDay)
                add(Calendar.DAY_OF_MONTH, offsetToMonday + i)
            }
        }
    }

    val firstDay = weekDays.first()
    val lastDay = weekDays.last()
    val headerTitle = "Apr ${firstDay.get(Calendar.DAY_OF_MONTH)} – ${lastDay.get(Calendar.DAY_OF_MONTH)}"
    val weekNum = firstDay.get(Calendar.WEEK_OF_YEAR)

    val hourH = 34.dp
    val timeColW = 40.dp
    val hourLabels = (8..17).map { h -> if (h < 12) "${h}AM" else "${h - 12}PM" }

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
                        text = headerTitle,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = CInk
                    )
                    Text(
                        text = "Week $weekNum · ${uiState.displayedYear}",
                        fontSize = 11.sp,
                        color = CMuted
                    )
                }
                Row(
                    modifier = Modifier
                        .background(CSurface, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onBack)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.GridView,
                        contentDescription = "Switch view",
                        tint = CInk,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(text = "Week", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = CInk)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Brush.verticalGradient(listOf(CHair, CHair)))
            )

            // Day header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Brush.verticalGradient(listOf(Color.White, Color.White)))
            ) {
                Box(modifier = Modifier.width(timeColW))
                weekDays.forEachIndexed { i, dayCal ->
                    val dayNum = dayCal.get(Calendar.DAY_OF_MONTH)
                    val isTodayDay = dayNum == todayDay &&
                            dayCal.get(Calendar.MONTH) + 1 == todayMonth &&
                            dayCal.get(Calendar.YEAR) == todayYear
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .background(
                                if (i > 0) Brush.verticalGradient(listOf(CHair, CHair))
                                else Brush.verticalGradient(listOf(Color.White, Color.White))
                            )
                            .padding(start = if (i > 0) 1.dp else 0.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = WEEK_DAY_LETTERS.getOrElse(i) { "?" },
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CMuted
                            )
                            Spacer(Modifier.height(2.dp))
                            if (isTodayDay) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .background(Brush.linearGradient(listOf(CBlue, CBlueDark)), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = dayNum.toString(),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            } else {
                                Text(
                                    text = dayNum.toString(),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (i >= 5) CMuted else CInk
                                )
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Brush.verticalGradient(listOf(CHair, CHair)))
            )

            // Grid
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(Color.White, Color.White)))
                        .verticalScroll(rememberScrollState())
                ) {
                    hourLabels.forEachIndexed { i, label ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(hourH)
                        ) {
                            Text(
                                text = label,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CMuted,
                                modifier = Modifier
                                    .width(timeColW)
                                    .align(Alignment.TopStart)
                                    .padding(end = 4.dp),
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .padding(start = timeColW)
                                    .background(Brush.verticalGradient(listOf(CHair, CHair)))
                            )
                            // Events for each day in this hour
                            weekDays.forEachIndexed { colIdx, dayCal ->
                                val dayNum = dayCal.get(Calendar.DAY_OF_MONTH)
                                val dayMonth = dayCal.get(Calendar.MONTH) + 1
                                val dayYear = dayCal.get(Calendar.YEAR)
                                val colEvents = uiState.events.filter { e ->
                                    e.year == dayYear && e.month == dayMonth && e.day == dayNum && e.startHour == i + 8
                                }
                                colEvents.take(1).forEach { event ->
                                    val colFraction = 1f / 7f
                                    val startX = timeColW + (colFraction * colIdx * 320).dp
                                    val eventH = ((event.endHour * 60 + event.endMinute -
                                            event.startHour * 60 - event.startMinute).toFloat() / 60f * hourH.value).dp
                                    Box(
                                        modifier = Modifier
                                            .padding(start = timeColW + (colFraction * colIdx * 300).dp, end = 2.dp)
                                            .width((300.dp * colFraction).coerceAtLeast(30.dp))
                                            .height(eventH.coerceAtLeast(20.dp))
                                            .background(
                                                Brush.linearGradient(
                                                    listOf(eventColor(event.type), eventColor(event.type))
                                                ),
                                                RoundedCornerShape(5.dp)
                                            )
                                            .padding(3.dp)
                                    ) {
                                        Text(
                                            text = event.title.take(8),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Box(Modifier.height(100.dp))
                }
            }

            UpgradeBanner()
        }

        CalenderlyFab(
            onClick = {},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 144.dp)
        )
    }
}
