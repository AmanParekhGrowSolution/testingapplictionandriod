package com.example.testingapplictionandriod.ui.onboarding

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testingapplictionandriod.ui.components.CalenderlyButton

private val PrimaryBlue = Color(0xFF2564CF)
private val PrimaryBlueDark = Color(0xFF1A3A80)
private val PrimaryBlueBg = Color(0xFFD8EAF9)
private val PrimaryBlueBgSoft = Color(0xFFEEF5FF)
private val InkColor = Color(0xFF1A1A2E)
private val MutedColor = Color(0xFF8B8BA7)
private val HairColor = Color(0xFFE4E4ED)
private val CoralColor = Color(0xFFE85C4A)
private val CoralBgColor = Color(0xFFFFF0EE)
private val SurfaceColor = Color(0xFFF7F7FB)

@Composable
fun OnboardingScreen(
    step: Int,
    onNext: () -> Unit,
    onGetStarted: () -> Unit
) {
    val isLastStep = step == 1
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.White, SurfaceColor)))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Hero illustration area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                if (isLastStep) CoralBgColor else PrimaryBlueBg,
                                Color.White
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isLastStep) {
                    OnboardingIllustration2()
                } else {
                    OnboardingIllustration1()
                }
            }

            // Content area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(20.dp))

                // Step dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(2) { i ->
                        Box(
                            modifier = Modifier
                                .size(if (i == step) 20.dp else 8.dp, 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (i == step) PrimaryBlue else HairColor)
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = if (isLastStep) "Go PRO, go further" else "Never miss a day",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkColor,
                    textAlign = TextAlign.Center,
                    letterSpacing = (-0.3).sp
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = if (isLastStep) {
                        "Unlock unlimited events, remove ads, and enjoy beautiful themes."
                    } else {
                        "See all your events, tasks and notes in one beautifully organized calendar."
                    },
                    fontSize = 14.sp,
                    color = MutedColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(32.dp))

                CalenderlyButton(
                    label = if (isLastStep) "Get Started" else "Next",
                    onClick = if (isLastStep) onGetStarted else onNext
                )
            }
        }
    }
}

@Composable
private fun OnboardingIllustration1() {
    Box(
        modifier = Modifier.size(220.dp),
        contentAlignment = Alignment.Center
    ) {
        // Main calendar card
        Box(
            modifier = Modifier
                .size(160.dp, 140.dp)
                .align(Alignment.CenterStart)
                .padding(start = 30.dp, top = 40.dp)
                .background(Brush.verticalGradient(listOf(Color.White, Color(0xFFFAFAFC))), RoundedCornerShape(18.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .background(
                        Brush.linearGradient(listOf(PrimaryBlue, PrimaryBlueDark)),
                        RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(Modifier.size(6.dp).background(Brush.linearGradient(listOf(Color.White, Color.White)), CircleShape))
                    Box(Modifier.size(6.dp).background(Brush.linearGradient(listOf(Color.White.copy(alpha = 0.87f), Color.White.copy(alpha = 0.87f))), CircleShape))
                }
            }
        }
        // Calendar icon overlay
        Icon(
            imageVector = Icons.Filled.CalendarMonth,
            contentDescription = "Calendar illustration",
            tint = PrimaryBlue,
            modifier = Modifier.size(56.dp).align(Alignment.Center)
        )
    }
}

@Composable
private fun OnboardingIllustration2() {
    Box(
        modifier = Modifier.size(220.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    Brush.linearGradient(
                        listOf(CoralColor, Color(0xFFF97316))
                    ),
                    RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "PRO star illustration",
                tint = Color.White,
                modifier = Modifier.size(56.dp)
            )
        }
    }
}
