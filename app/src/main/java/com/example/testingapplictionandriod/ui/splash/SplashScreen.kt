package com.example.testingapplictionandriod.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val SplashBgStart = Color(0xFFE8F0FA)
private val SplashBgEnd = Color(0xFFFFFFFF)
private val PrimaryBlue = Color(0xFF2564CF)
private val PrimaryBlueDark = Color(0xFF1A3A80)
private val InkColor = Color(0xFF1A1A2E)
private val MutedColor = Color(0xFF8B8BA7)

@Composable
fun SplashScreen(onSplashDone: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        delay(1500)
        onSplashDone()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SplashBgStart, SplashBgEnd))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(
                        Brush.linearGradient(listOf(PrimaryBlue, PrimaryBlueDark)),
                        RoundedCornerShape(28.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = "Calenderly app icon",
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
            }
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Calenderly",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = InkColor,
                letterSpacing = (-0.5).sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Your time, beautifully organized.",
                fontSize = 13.sp,
                color = MutedColor
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Spacer(Modifier.height(40.dp))
            Text(
                text = "v1.0.0",
                fontSize = 11.sp,
                color = Color(0xFFC8C8D8),
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
