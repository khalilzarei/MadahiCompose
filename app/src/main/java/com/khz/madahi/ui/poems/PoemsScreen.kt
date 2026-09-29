// ui/poems/PoemsScreen.kt
package com.khz.madahi.ui.poems

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.models.Content
import com.khz.madahi.ui.common.BottomBarActions
import com.khz.madahi.ui.common.BottomTab
import com.khz.madahi.ui.common.ErrorContentScreen
import com.khz.madahi.ui.components.BaseScreen
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.Mini3DButton
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

// ============================================================
// صفحه شعر و سبک — 🎧 ویژه نسخه پرو
// ------------------------------------------------------------
// - لیست شعرهای ذخیره‌شده همراه با سبک (ویس)
// - پخش صوت (یک‌جا فقط یکی)
// - اگر پرو فعال نباشد → معرفی امکانات و راه تماس با پشتیبانی
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoemsScreen(
    bottomBarActions: BottomBarActions,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val colors = LocalMadahiColors.current

    // ============ ViewModel ============
    val viewModel: PoemsViewModel = viewModel(
        factory = PoemsViewModelFactory(
            preferencesManager = PreferencesManager(context),
            appDatabase = AppDatabase.getInstance(context)
        )
    )

    val uiState by viewModel.uiState.collectAsState()
    val poems by viewModel.poems.collectAsState()
    val playingId by viewModel.playingId.collectAsState()
    val progress by viewModel.progress.collectAsState()

    BaseScreen(
        bottomBarActions = bottomBarActions,
        title = "شعر و سبک",
        subtitle = "🎧",
        selectedBottomTab = BottomTab.POEMS,
        onHeaderBottonClicked = { onNavigateBack() },
    ) {

        when (uiState) {

            // ============ لودینگ ============
            is PoemsUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // ============ خطا ============
            is PoemsUiState.Error   -> {
                ErrorContentScreen(
                    message = (uiState as PoemsUiState.Error).message,
                    onRetry = viewModel::load
                )
            }

            // ============ نسخه پرو فعال نیست ============
            is PoemsUiState.Guest   -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    GlassCard3D(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🎧",
                                fontSize = 52.sp
                            )
                            Spacer(Modifier.height(14.dp))
                            Text(
                                text = "این قسمت ویژه نسخه پرو است",
                                color = colors.textPrimary,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = "شعرهای ذخیره‌شده را همراه با سبک‌ها (ویس) ببینید و از اضافه کردن ویس به شعرها استفاده کنید",
                                color = colors.textMuted,
                                fontSize = 14.sp,
                                lineHeight = 24.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(22.dp))
                            PremiumFeatures()
                            Spacer(Modifier.height(20.dp))
                            Text(
                                text = "برای تهیه و فعال‌سازی اشتراک ویژه با پشتیبانی تماس بگیرید.",
                                color = colors.textMuted,
                                fontSize = 14.sp,
                                lineHeight = 24.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(14.dp))
                            PremiumSupportButton(modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }

            // ============ لیست شعرها ============
            is PoemsUiState.Success -> {
                if (poems.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "📜",
                                fontSize = 48.sp
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "هنوز سبکی اضافه نشده است",
                                color = colors.textMuted,
                                fontSize = 15.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "از صفحه جزئیات هر شعر می‌توانید ویس اضافه کنید",
                                color = colors.textMuted,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = 24.dp,
                            vertical = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(poems) { poem ->
                            PoemRow(
                                poem = poem,
                                isPlaying = playingId == poem.id,
                                progress = if (playingId == poem.id) progress else 0f,
                                onTogglePlay = { viewModel.togglePlay(poem) })
                        }
                    }
                }
            }
        }
    }
}

// ============ آیتم لیست شعر ============

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PoemRow(
    poem: Content,
    isPlaying: Boolean,
    progress: Float,
    onTogglePlay: () -> Unit
) {
    val colors = LocalMadahiColors.current

    GlassCard3D {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 14.dp
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // دکمه پخش/توقف
                Mini3DButton(
                    imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                    tint = colors.gold,
                    onClick = onTogglePlay
                )

                Spacer(Modifier.width(12.dp))

                // عنوان و جواب
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = poem.subject,
                        color = colors.textPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (poem.answer.isNotBlank()) {
                        Text(
                            text = poem.answer,
                            color = colors.textMuted,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // نوار پیشرفت پخش
            if (isPlaying) {
                Spacer(Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp)),
                    color = colors.gold,
                    trackColor = colors.textMuted.copy(alpha = 0.2f)
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun PoemsScreenPreview() {
    MadahiThemeGreen(darkTheme = true) {
        PoemsScreen(
            bottomBarActions = BottomBarActions(),
            onNavigateBack = {})
    }
}
