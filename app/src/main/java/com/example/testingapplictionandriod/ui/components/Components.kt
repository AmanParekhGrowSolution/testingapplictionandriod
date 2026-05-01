package com.example.testingapplictionandriod.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PrimaryBlue = Color(0xFF2564CF)
private val PrimaryBlueDark = Color(0xFF1A3A80)
private val PrimaryBlueBgSoft = Color(0xFFEEF5FF)
private val HairColor = Color(0xFFE4E4ED)
private val InkColor = Color(0xFF1A1A2E)
private val MutedColor = Color(0xFF8B8BA7)
private val SurfaceColor = Color(0xFFF7F7FB)

@Composable
fun CalenderlyButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(
                brush = if (enabled) {
                    Brush.linearGradient(listOf(PrimaryBlue, PrimaryBlueDark))
                } else {
                    Brush.linearGradient(listOf(Color(0xFFBACAED), Color(0xFFBACAED)))
                },
                shape = RoundedCornerShape(14.dp)
            )
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun CalenderlyOutlineButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = PrimaryBlue
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .background(
                Brush.linearGradient(listOf(Color.White, Color.White)),
                RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun CalenderlyFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .shadow(8.dp, RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(listOf(PrimaryBlue, PrimaryBlueDark)),
                RoundedCornerShape(18.dp)
            )
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Create new item",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun UpgradeBanner(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                Brush.linearGradient(listOf(Color(0xFFEEF4FF), Color(0xFFFFF7ED)))
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    Brush.linearGradient(listOf(PrimaryBlue, PrimaryBlueDark)),
                    RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "★", color = Color.White, fontSize = 14.sp)
        }
        Box(modifier = Modifier.weight(1f)) {
            androidx.compose.foundation.layout.Column {
                Text(
                    text = "Upgrade to PRO",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkColor
                )
                Text(
                    text = "Remove ads · Unlock all features",
                    fontSize = 11.sp,
                    color = MutedColor
                )
            }
        }
    }
}
