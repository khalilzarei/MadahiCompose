// ui/booklet/BookletDetailScreen.kt
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.MadahiBackground
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.goldLight
import com.khz.madahi.ui.theme.primaryDark
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary
import androidx.compose.ui.platform.LocalLayoutDirection

// ============================================================
// صفحه جزئیات بخش کتابچه — طراحی کاملاً مستقل
// ============================================================

@Composable
fun BookletDetailScreen(
    sectionId: Int,
    sectionTitle: String,
    onNavigateBack: () -> Unit
) {
    val viewModel: BookletDetailViewModel = viewModel(
        factory = BookletDetailViewModelFactory(sectionId, sectionTitle)
    )

    val uiState by viewModel.uiState.collectAsState()
    var selectedItem by remember { mutableStateOf<BookletItem?>(null) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MadahiBackground {
            val colors = LocalMadahiColors.current

            Column(modifier = Modifier.fillMaxSize()) {
                // هدر
                BookletDetailHeader(
                    title = sectionTitle,
                    onBackClick = onNavigateBack
                )

                // محتوا
                when (val state = uiState) {
                    is BookletDetailUiState.Loading -> {
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

                    is BookletDetailUiState.Success -> {
                        if (state.items.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "📄", fontSize = 48.sp)
                                    Spacer(Modifier.height(12.dp))
                                    Text(
                                        text = "هنوز متنی اضافه نشده",
                                        color = colors.textMuted,
                                        fontSize = 15.sp
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
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(state.items) { item ->
                                    BookletItemCard(
                                        item = item,
                                        onClick = { selectedItem = item }
                                    )
                                }
                            }
                        }
                    }

                    is BookletDetailUiState.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "⚠️ ${state.message}",
                                color = colors.textMuted,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

    // ============ دیالوگ نمایش متن ============
    selectedItem?.let { item ->
        BookletContentDialog(
            item = item,
            onDismiss = { selectedItem = null }
        )
    }
}

// ============================================================
// هدر جزئیات
// ============================================================

@Composable
private fun BookletDetailHeader(
    title: String,
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

            Text(
                text = title,
                color = colors.textPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ============================================================
// کارت هر متن
// ============================================================

@Composable
private fun BookletItemCard(
    item: BookletItem,
    onClick: () -> Unit
) {
    val colors = LocalMadahiColors.current

    GlassCard3D(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // آیکون
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            brush = Brush.radialGradient(
                                listOf(
                                    colors.goldLight.copy(alpha = 0.4f),
                                    colors.gold.copy(alpha = 0.15f)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "📄", fontSize = 20.sp)
                }

                Spacer(Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        color = colors.textPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (item.source.isNotBlank()) {
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = item.source,
                            color = colors.gold,
                            fontSize = 12.sp
                        )
                    }
                }

                Text(
                    text = "‹",
                    color = colors.gold,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // پیش‌نمایش متن
            Spacer(Modifier.height(10.dp))
            Text(
                text = item.content.take(120) + if (item.content.length > 120) "..." else "",
                color = colors.textMuted,
                fontSize = 13.sp,
                lineHeight = 22.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ============================================================
// دیالوگ نمایش کامل متن
// ============================================================

@Composable
private fun BookletContentDialog(
    item: BookletItem,
    onDismiss: () -> Unit
) {
    val colors = LocalMadahiColors.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.primaryDark.copy(alpha = 0.92f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            GlassCard3D(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .fillMaxSize(0.85f)
                    .clickable(enabled = false) {} // جلوگیری از بسته شدن با کلیک داخل کارت
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    // عنوان
                    Text(
                        text = item.title,
                        color = colors.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (item.source.isNotBlank()) {
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = item.source,
                            color = colors.gold,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    // خط جداکننده
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        Color.Transparent,
                                        colors.gold.copy(alpha = 0.4f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Spacer(Modifier.height(18.dp))

                    // متن اصلی
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Text(
                                text = item.content,
                                color = colors.textPrimary,
                                fontSize = 17.sp,
                                lineHeight = 32.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // دکمه بستن
                    GlassCard3D(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onDismiss)
                    ) {
                        Text(
                            text = "بستن",
                            color = colors.gold,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp)
                        )
                    }
                }
            }
        }
    }
}

// ============================================================
// Preview
// ============================================================

@Preview(showBackground = false)
@Composable
private fun BookletDetailScreenPreview() {
    MadahiThemeGreen(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Placeholder for preview
        }
    }
}
