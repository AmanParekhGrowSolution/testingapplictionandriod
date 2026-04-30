package com.example.testingapplictionandriod.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(uiState: HomeUiState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFEF4444), Color(0xFFDC2626))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = uiState.greeting,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp
        )
    }
}
