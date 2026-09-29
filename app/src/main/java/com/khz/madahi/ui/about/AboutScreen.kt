// ui/about/AboutScreen.kt
package com.khz.madahi.ui.about

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.BuildConfig
import com.khz.madahi.helper.SUPPORT_PHONE_NUMBER
import com.khz.madahi.helper.openSupportContact
import com.khz.madahi.ui.common.BottomBarActions
import com.khz.madahi.ui.common.BottomTab
import com.khz.madahi.ui.components.BaseScreen
import com.khz.madahi.ui.components.GlassCard
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.goldLight
import com.khz.madahi.ui.theme.primaryDark
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary
import com.khz.madahi.ui.theme.textSecondary

// ============================================================
// صفحه درباره ما — سبک شیشه‌ای و سه‌بعدی
// امضای ورودی دقیقاً مثل نسخه قبلی است (NavGraph دست نمی‌خورد)
// ============================================================

@Composable
fun AboutScreen(
    bottomBarActions: BottomBarActions
) {
    val colors = LocalMadahiColors.current
    val context = LocalContext.current

    BaseScreen(
        bottomBarActions = bottomBarActions,
        title = "درباره ما",
        subtitle = "",
        selectedBottomTab = BottomTab.ABOUT,
        isCategory = true,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {

            // ============ کارت اصلی ============
            GlassCard3D(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // لوگو
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

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "دفترچه مداحی",
                        color = colors.textPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "نسخه ${BuildConfig.VERSION_NAME}",
                        color = colors.textSecondary,
                        fontSize = 14.sp
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "این اپلیکیشن برای ذخیره‌سازی و دسته‌بندی نوحه‌ها، " + "اشعار و دیالوگ‌های مداحی طراحی شده است.",
                        color = colors.textMuted,
                        fontSize = 15.sp,
                        lineHeight = 28.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            // ============ تماس ============
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        openSupportContact(context)
                    }) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 22.dp,
                            vertical = 18.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "label",
                        tint = colors.textSecondary,
                        modifier = Modifier.size(35.dp)
                    )

                    Spacer(Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {

                        Text(
                            text = "ارتباط با ما",
                            color = colors.textPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = SUPPORT_PHONE_NUMBER,
                            color = colors.textSecondary,
                            fontSize = 14.sp
                        )
                    }

                }
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = "ساخته‌شده با ❤️",
                color = colors.textMuted,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun AboutScreenPreview() {
    MadahiThemeGreen(darkTheme = true) {
        AboutScreen(bottomBarActions = BottomBarActions())
    }
}
