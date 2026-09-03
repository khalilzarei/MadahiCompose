// ui/booklet/BookletScreen.kt
package com.khz.madahi.ui.booklet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.MadahiBackground
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.goldLight
import com.khz.madahi.ui.theme.primaryDark
import com.khz.madahi.ui.theme.primaryLight
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

// ============================================================
// صفحه اصلی کتابچه — طراحی کاملاً مستقل از دفترچه
// ------------------------------------------------------------
// - هدر اختصاصی با دکمه بازگشت
// - لیست بخش‌های کتابچه (ادعیه، زیارات، متون و...)
// - طراحی متفاوت با تم اختصاصی کتابچه
// ============================================================

@Composable
fun BookletScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSection: (Int, String) -> Unit
) {
    val context = LocalContext.current

    val viewModel: BookletViewModel = viewModel(
        factory = BookletViewModelFactory(
            preferencesManager = PreferencesManager(context)
        )
    )

    val uiState by viewModel.uiState.collectAsState()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MadahiBackground {
            val colors = LocalMadahiColors.current

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // ============ هدر اختصاصی کتابچه ============
                BookletHeader(
                    onBackClick = onNavigateBack
                )

                // ============ محتوا ============
                when (val state = uiState) {
                    is BookletUiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = colors.gold,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    is BookletUiState.Success -> {
                        if (state.sections.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "📚", fontSize = 52.sp)
                                    Spacer(Modifier.height(14.dp))
                                    Text(
                                        text = "هنوز بخشی اضافه نشده",
                                        color = colors.textMuted,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f),
                                contentPadding = PaddingValues(
                                    horizontal = 24.dp,
                                    vertical = 16.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                items(state.sections) { section ->
                                    BookletSectionCard(
                                        section = section,
                                        onClick = {
                                            onNavigateToSection(section.id, section.title)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    is BookletUiState.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "⚠️",
                                    fontSize = 42.sp
                                )
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    text = state.message,
                                    color = colors.textMuted,
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(16.dp))
                                GlassCard3D(
                                    modifier = Modifier
                                        .clickable { viewModel.loadSections() }
                                        .padding(horizontal = 32.dp)
                                ) {
                                    Text(
                                        text = "تلاش مجدد",
                                        color = colors.gold,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(
                                            horizontal = 28.dp,
                                            vertical = 14.dp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================================
// هدر اختصاصی کتابچه
// ============================================================

@Composable
private fun BookletHeader(
    onBackClick: () -> Unit
) {
    val colors = LocalMadahiColors.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        colors.primaryDark.copy(alpha = 0.95f),
                        colors.primaryDark.copy(alpha = 0.7f),
                        Color.Transparent
                    )
                )
            )
            .padding(top = 44.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // دکمه بازگشت
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        brush = Brush.radialGradient(
                            listOf(
                                colors.goldLight.copy(alpha = 0.5f),
                                colors.gold.copy(alpha = 0.2f)
                            )
                        ),
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.25f),
                        shape = CircleShape
                    )
                    .clickable(onClick = onBackClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "بازگشت",
                    tint = colors.gold,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            // عنوان
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "📚 کتابچه",
                    color = colors.textPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "مجموعه ادعیه و متون مقدس",
                    color = colors.textMuted,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// ============================================================
// کارت هر بخش کتابچه
// ============================================================

@Composable
private fun BookletSectionCard(
    section: BookletSection,
    onClick: () -> Unit
) {
    val colors = LocalMadahiColors.current

    GlassCard3D(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // آیکون بخش
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        brush = Brush.radialGradient(
                            listOf(
                                colors.goldLight.copy(alpha = 0.35f),
                                colors.gold.copy(alpha = 0.1f)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = section.icon, fontSize = 28.sp)
            }

            Spacer(Modifier.width(16.dp))

            // عنوان و توضیح
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = section.title,
                    color = colors.textPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = section.description,
                    color = colors.textMuted,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (section.itemCount > 0) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "${section.itemCount} متن",
                        color = colors.gold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // فلش
            Text(
                text = "‹",
                color = colors.gold,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ============================================================
// Preview
// ============================================================

@Preview(showBackground = false)
@Composable
private fun BookletScreenPreview() {
    MadahiThemeGreen(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Placeholder for preview
        }
    }
}
