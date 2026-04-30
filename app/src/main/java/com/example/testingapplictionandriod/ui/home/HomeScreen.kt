package com.example.testingapplictionandriod.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onTap: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0F0C29), Color(0xFF302B63), Color(0xFF24243E))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(R.string.home_greeting),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.count_label, uiState.count),
                color = Color.White.copy(alpha = 0.87f),
                fontSize = 18.sp
            )
            GradientButton(
                label = stringResource(R.string.tap_me),
                onClick = onTap
            )
        }
    }
}

@Composable
private fun GradientButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))),
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {
        Text(label, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}
