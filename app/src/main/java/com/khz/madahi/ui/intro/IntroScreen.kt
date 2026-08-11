// ui/intro/IntroScreen.kt
package com.khz.madahi.ui.intro

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.R
import com.khz.madahi.data.local.preferences.PreferencesManager
import kotlinx.coroutines.launch

// ============ Data Class ============
data class IntroSlide(
    val imageRes: Int,
    val title: String,
    val description: String
)

// ============ اسلایدها ============
val slides = listOf(
    IntroSlide(
        imageRes = R.mipmap.ic_launcher,
        title = "دفترچه مداحی",
        description = "ذخیره‌سازی نوحه‌ها و روضه‌ها و دسترسی سریع"
    ),
    IntroSlide(
        imageRes = R.mipmap.ic_launcher,
        title = "سفارشی‌سازی",
        description = "می‌توانید نوحه‌ها و روضه‌های خود را سفارشی کنید و به تعداد دلخواه اضافه کنید"
    ),
    IntroSlide(
        imageRes = R.mipmap.ic_launcher,
        title = "کاملاً رایگان",
        description = "تمام امکانات این اپلیکیشن رایگان می‌باشد"
    ),
    IntroSlide(
        imageRes = R.mipmap.ic_launcher,
        title = "پیشنهادات",
        description = "در صورت داشتن پیشنهاد می‌توانید با برنامه‌نویس اپ تماس بگیرید\n09362371808"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntroScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToCategory: () -> Unit
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { slides.size })

    val isLastPage = pagerState.currentPage == slides.size - 1

    fun onFinishIntro() {
        preferencesManager.isFirstTimeLaunch = false
        if (preferencesManager.isLoggedIn) {
            onNavigateToCategory()
        } else {
            onNavigateToLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ============ ViewPager ============
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            IntroSlideContent(slide = slides[page])
        }

        // ============ خط جداکننده ============
        Divider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // ============ دکمه‌ها + Dots ============
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // دکمه Skip (چپ)
            TextButton(
                onClick = { onFinishIntro() }) {
                Text(
                    "گذشتن",
                    fontSize = 16.sp
                )
            }

            // ============ Dots Indicator (وسط) ============
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(slides.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 14.dp else 8.dp)
                            .background(
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                },
                                shape = CircleShape
                            )
                    )
                }
            }

            // دکمه Next / Start (راست)
            Button(
                onClick = {
                    if (isLastPage) {
                        onFinishIntro()
                    } else {
                        coroutineScope.launch {
                            pagerState.scrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (isLastPage) "شروع" else "بعدی",
                    fontSize = 16.sp
                )
            }
        }
    }
}

// ============ محتوای هر اسلاید ============
@Composable
fun IntroSlideContent(slide: IntroSlide) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.size(260.dp),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant  // ✅ از تم
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = slide.title,
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.Center,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = slide.title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,  // ✅ از تم
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = slide.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,  // ✅ از تم
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun IntroScreenPreview() {
    IntroScreen(
        {},
        {},
    )
}