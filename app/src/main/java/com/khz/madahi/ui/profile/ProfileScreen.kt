// ui/profile/ProfileScreen.kt
package com.khz.madahi.ui.profile

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.ui.common.BottomBarActions
import com.khz.madahi.ui.common.BottomTab
import com.khz.madahi.ui.components.BaseScreen
import com.khz.madahi.ui.components.Delete3DButton
import com.khz.madahi.ui.components.GlassCard
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.goldLight
import com.khz.madahi.ui.theme.primaryDark
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary
import com.khz.madahi.ui.theme.textSecondary

// ============================================================
// صفحه پروفایل — سبک شیشه‌ای و سه‌بعدی
// امضای ورودی دقیقاً مثل نسخه قبلی است (NavGraph دست نمی‌خورد)
// ============================================================

@Composable
fun ProfileScreen(
    bottomBarActions: BottomBarActions
) {
    val colors = LocalMadahiColors.current
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    val user = preferencesManager.user
    val fullName = user?.fullName ?: "کاربر مهمان"
    val mobile = user?.mobile ?: "—"

    // TODO: اتصال به دیتابیس برای آمار واقعی (تعداد دسته‌ها/اشعار/دیالوگ‌ها)
    val categoryCount = 0
    val poemCount = 0
    val dialogCount = 0

    BaseScreen(
        bottomBarActions = bottomBarActions,
        title = "پروفایل",
        subtitle = "",
        selectedBottomTab = BottomTab.PROFILE,
        onHeaderBottonClicked = {},
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {

            // ============ کارت اصلی پروفایل ============
            GlassCard3D(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // آواتار — حرف اول نام
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .shadow(18.dp, CircleShape)
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
                            text = fullName.trim().firstOrNull()?.toString() ?: "؟",
                            color = colors.goldLight,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = fullName,
                        color = colors.textPrimary,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = mobile,
                        color = colors.textSecondary,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            // ============ آمار ============
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                GlassCard(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$categoryCount",
                            color = colors.gold,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "دسته‌بندی",
                            color = colors.textMuted,
                            fontSize = 13.sp
                        )
                    }
                }

                GlassCard(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$poemCount",
                            color = colors.gold,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "شعر",
                            color = colors.textMuted,
                            fontSize = 13.sp
                        )
                    }
                }

                GlassCard(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$dialogCount",
                            color = colors.gold,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "دیالوگ",
                            color = colors.textMuted,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(22.dp))

            // ============ خروج از حساب (TODO: مثل نسخه قبلی) ============
            Delete3DButton(
                text = "خروج از حساب",
                modifier = Modifier.fillMaxWidth(),
                onClick = { /* TODO: خروج از حساب */ }
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun ProfileScreenPreview() {
    MadahiThemeGreen(darkTheme = true) {
        ProfileScreen(bottomBarActions = BottomBarActions())
    }
}
