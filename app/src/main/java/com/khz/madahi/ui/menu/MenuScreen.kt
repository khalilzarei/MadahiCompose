// ui/menu/MenuScreen.kt
package com.khz.madahi.ui.menu

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.MadahiBackground
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.goldLight
import com.khz.madahi.ui.theme.primaryDark
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

// ============================================================
// صفحه منوی اصلی (بعد از لاگین)
// ------------------------------------------------------------
// دو مسیر:
//   📖 دفترچه مداحی  → کتگوری‌ها و اشعار خود کاربر
//   📚 کتابچه      → کتابخانه‌ی عمومی اشعار
// ============================================================

@Composable
fun MenuScreen(
    onNavigateToDaftarkeh: () -> Unit,
    onNavigateToKtabeh: () -> Unit
) {
    val colors = LocalMadahiColors.current

    MadahiBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 32.dp,
                    vertical = 40.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ============ لوگو ============
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .shadow(
                        18.dp,
                        CircleShape
                    )
                    .background(
                        Brush.radialGradient(
                            listOf(
                                colors.primary,
                                colors.primaryDark
                            )
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "د",
                    color = colors.goldLight,
                    fontSize = 46.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "دفتر مداحی",
                color = colors.textPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "کجا می‌خواهی بروی؟",
                color = colors.textMuted,
                fontSize = 15.sp
            )

            Spacer(Modifier.height(48.dp))

            // ============ دفترچه مداحی ============
            GlassCard3D(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToDaftarkeh() }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📖",
                        fontSize = 38.sp
                    )
                    Spacer(Modifier.width(18.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "دفترچه مداحی",
                            color = colors.textPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "نوحه‌ها و روضه‌های خودت",
                            color = colors.textMuted,
                            fontSize = 14.sp
                        )
                    }
                    Text(
                        text = "‹",
                        color = colors.gold,
                        fontSize = 26.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ============ کتابچه ============
            GlassCard3D(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToKtabeh() }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📚",
                        fontSize = 38.sp
                    )
                    Spacer(Modifier.width(18.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "کتابچه",
                            color = colors.textPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "کتابخانه‌ی عمومی اشعار",
                            color = colors.textMuted,
                            fontSize = 14.sp
                        )
                    }
                    Text(
                        text = "‹",
                        color = colors.gold,
                        fontSize = 26.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuScreenPreview() {
    MadahiThemeGreen(darkTheme = true) {
        MenuScreen(
            onNavigateToDaftarkeh = {},
            onNavigateToKtabeh = {})
    }
}
