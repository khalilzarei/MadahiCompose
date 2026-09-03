// ui/intro/IntroScreen.kt
package com.khz.madahi.ui.intro

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.MadahiBackground
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.primaryDark
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary
import kotlinx.coroutines.launch

// ============================================================
// اسلایدهای معرفی — سبک شیشه‌ای و سه‌بعدی
// امضای ورودی دقیقاً مثل نسخه قبلی است (NavGraph دست نمی‌خورد)
// ============================================================

data class IntroSlide(
    val icon: String,        // ایموجی / کاراکتر
    val title: String,
    val description: String
)

val defaultIntroSlides = listOf(
    IntroSlide(
        icon = "📖",
        title = "دفترچه مداحی",
        description = "ذخیره‌سازی نوحه‌ها و اشعار و دسترسی سریع به آن‌ها"
    ),
    IntroSlide(
        icon = "🎙️",
        title = "سفارشی‌سازی",
        description = "اشعار و دیالوگ‌های خود را بسازید و به تعداد دلخواه اضافه کنید"
    ),
    IntroSlide(
        icon = "✨",
        title = "طراحی زیبا",
        description = "رابط کاربری شیشه‌ای و سه‌بعدی با تم سبز و طلایی"
    ),
    IntroSlide(
        icon = "🆓",
        title = "کاملاً رایگان",
        description = "تمام امکانات این اپلیکیشن رایگان می‌باشد"
    )
)

@Composable
fun IntroScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToAppSelection: () -> Unit
) {
    val colors = LocalMadahiColors.current
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { defaultIntroSlides.size })

    val isLastPage = pagerState.currentPage == defaultIntroSlides.size - 1

    fun onFinishIntro() {
        preferencesManager.isFirstTimeLaunch = false
        if (preferencesManager.isLoggedIn) {
            onNavigateToAppSelection()
        } else {
            onNavigateToLogin()
        }
    }

    MadahiBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ============ اسلایدها ============
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->

                val slide = defaultIntroSlides[page]

                GlassCard3D {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        // آیکون در دایره سه‌بعدی (هماهنگ با Splash جدید)
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .shadow(
                                    26.dp,
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
                                text = slide.icon,
                                fontSize = 64.sp
                            )
                        }

                        Spacer(Modifier.height(36.dp))

                        Text(
                            text = slide.title,
                            color = colors.textPrimary,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(14.dp))

                        Text(
                            text = slide.description,
                            color = colors.textMuted,
                            fontSize = 15.sp,
                            lineHeight = 28.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // ============ نقطه‌های راهنما ============
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(defaultIntroSlides.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .size(if (isSelected) 12.dp else 8.dp)
                            .shadow(
                                elevation = if (isSelected) 8.dp else 0.dp,
                                shape = CircleShape
                            )
                            .background(
                                color = if (isSelected) colors.gold else colors.textMuted.copy(alpha = 0.4f),
                                shape = CircleShape
                            )
                    )
                }
            }

            Spacer(Modifier.height(22.dp))

            // ============ دکمه‌ها — نوشته ساده و سبک (مناسب اینترو) ============
            GlassCard3D {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // ===== گذشتن — متن ساده =====
                    Text(
                        text = "گذشتن",
                        color = colors.textMuted,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .clickable { onFinishIntro() }
                            .padding(
                                vertical = 10.dp,
                                horizontal = 12.dp
                            ))

                    // ===== بعدی / شروع — نوشته طلایی با فلش =====
                    Text(
                        text = if (isLastPage) "شروع" else "بعدی",
                        color = colors.gold,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable {
                                if (isLastPage) {
                                    onFinishIntro()
                                } else {
                                    coroutineScope.launch {
                                        pagerState.scrollToPage(pagerState.currentPage + 1)
                                    }
                                }
                            }
                            .padding(
                                vertical = 10.dp,
                                horizontal = 12.dp
                            ))
                }
            }

        }
    }
}

@Preview(showBackground = false)
@Composable
private fun IntroScreenPreview() {
    MadahiThemeGreen(darkTheme = true) {
        IntroScreen(
            onNavigateToLogin = {},
            onNavigateToAppSelection = {})
    }
}
