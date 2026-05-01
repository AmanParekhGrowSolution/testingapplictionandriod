package com.example.testingapplictionandriod.ui.mine

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
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
import com.example.testingapplictionandriod.ui.components.CalenderlyButton

private val CBlue = Color(0xFF2564CF)
private val CBlueDark = Color(0xFF1A3A80)
private val CBlueBg = Color(0xFFD8EAF9)
private val CDanger = Color(0xFFDE3030)
private val CDangerBg = Color(0xFFFBDADA)
private val CInk = Color(0xFF1A1A2E)
private val CInk2 = Color(0xFF2E2E48)
private val CMuted = Color(0xFF8B8BA7)
private val CHair = Color(0xFFE4E4ED)
private val CSurface = Color(0xFFF7F7FB)
private val CWarn = Color(0xFFF97316)
private val CWarnBg = Color(0xFFFFF7ED)
private val CWarnInk = Color(0xFF8C6E1A)

@Composable
fun MineScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(CSurface, Color.White)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Brush.verticalGradient(listOf(Color.White, Color.White)))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mine",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CInk,
                    letterSpacing = (-0.3).sp
                )
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings", tint = CInk)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Brush.verticalGradient(listOf(CHair, CHair)))
            )

            // Profile card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            Brush.linearGradient(listOf(CBlue, CBlueDark)),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "HN",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Hunaid Nakhuda",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CInk
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "webdevai@growsolutions.in",
                    fontSize = 13.sp,
                    color = CMuted
                )
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .background(Brush.linearGradient(listOf(CBlueBg, CBlueBg)), RoundedCornerShape(8.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(text = "Free Plan", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CBlue)
                }
            }

            // PRO upgrade card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(
                        Brush.linearGradient(listOf(CBlue, CBlueDark)),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "PRO icon",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Upgrade to PRO",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Remove ads · Unlimited events · Dark mode",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.87f)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.linearGradient(listOf(Color.White.copy(alpha = 0.87f), Color.White.copy(alpha = 0.87f))),
                                RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(text = "View", color = CBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Account section
            SettingsSection(title = "ACCOUNT") {
                SettingsRow(
                    icon = Icons.Filled.Edit,
                    iconBg = CBlueBg,
                    iconTint = CBlue,
                    label = "Edit Profile"
                )
                SettingsDivider()
                SettingsRow(
                    icon = Icons.Filled.Notifications,
                    iconBg = Color(0xFFFFF7ED),
                    iconTint = CWarn,
                    label = "Notifications"
                )
                SettingsDivider()
                SettingsRow(
                    icon = Icons.Filled.Refresh,
                    iconBg = Color(0xFFE8F5E9),
                    iconTint = Color(0xFF22C55E),
                    label = "Sync Calendar"
                )
            }

            Spacer(Modifier.height(16.dp))

            // Preferences section
            SettingsSection(title = "PREFERENCES") {
                SettingsRow(
                    icon = Icons.Filled.DarkMode,
                    iconBg = Color(0xFFEDE7F6),
                    iconTint = Color(0xFF7C3AED),
                    label = "Appearance"
                )
                SettingsDivider()
                SettingsRow(
                    icon = Icons.Filled.GridView,
                    iconBg = CBlueBg,
                    iconTint = CBlue,
                    label = "Default View"
                )
                SettingsDivider()
                SettingsRow(
                    icon = Icons.Filled.Lock,
                    iconBg = Color(0xFFF3F4F6),
                    iconTint = CMuted,
                    label = "Privacy"
                )
            }

            Spacer(Modifier.height(16.dp))

            // Support section
            SettingsSection(title = "SUPPORT") {
                SettingsRow(
                    icon = Icons.Filled.Help,
                    iconBg = Color(0xFFFFF7ED),
                    iconTint = CWarn,
                    label = "Help & FAQ"
                )
                SettingsDivider()
                SettingsRow(
                    icon = Icons.Filled.Info,
                    iconBg = CBlueBg,
                    iconTint = CBlue,
                    label = "About Calenderly"
                )
            }

            Spacer(Modifier.height(16.dp))

            // Danger section
            SettingsSection(title = "") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(CDangerBg, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete account",
                            tint = CDanger,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Delete account", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = CDanger)
                        Text(text = "This action can't be undone", fontSize = 12.sp, color = CMuted)
                    }
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        tint = CMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(Modifier.height(100.dp))
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        if (title.isNotBlank()) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CMuted,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color.White, CSurface)), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(start = 68.dp)
            .background(Brush.verticalGradient(listOf(CHair, CHair)))
    )
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    label: String,
    trailing: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = CMuted,
            modifier = Modifier.size(16.dp)
        )
    }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconBg, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = iconTint, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = CInk,
            modifier = Modifier.weight(1f)
        )
        trailing()
    }
}
