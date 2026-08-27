// ui/library/LibraryDetailScreen.kt
package com.khz.madahi.ui.library

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.ContentRepository
import com.khz.madahi.data.remote.repository.LibraryRepository
import com.khz.madahi.models.Content
import com.khz.madahi.ui.common.ErrorContentScreen
import com.khz.madahi.ui.contentdetail.ContentDetailScreenContent
import com.khz.madahi.ui.contentdetail.ContentDetailViewModel
import com.khz.madahi.ui.contentdetail.ContentDetailViewModelFactory
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.textMuted

// ============================================================
// صفحه جزئیات شعر در کتابچه
// ------------------------------------------------------------
// محتوا از سرور لود می‌شود و فقط‌خواند نمایش داده می‌شود
// (بدون دکمه ویرایش/ویس) — با همان ظاهر جزئیات دفترچه
// ============================================================

@Composable
fun LibraryDetailScreen(
    contentId: Int,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val colors = LocalMadahiColors.current

    // ============ لود محتوا از سرور ============
    val libraryRepository = remember { LibraryRepository(RetrofitClient.apiService) }

    var content by remember { mutableStateOf<Content?>(null) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var loaded by remember { mutableStateOf(false) }

    LaunchedEffect(contentId) {
        when (val result = libraryRepository.getContentById(contentId)) {
            is com.khz.madahi.utils.Result.Success -> content = result.data
            is com.khz.madahi.utils.Result.Error   -> loadError = result.message
            is com.khz.madahi.utils.Result.Loading -> { /* ignore */
            }
        }
        loaded = true
    }

    // ============ لودینگ / خطا ============
    if (!loaded) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    if (loadError != null) {
        ErrorContentScreen(
            message = loadError!!,
            onRetry = onNavigateBack
        )
        return
    }

    val poem = content
            ?: run {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "محتوا یافت نشد",
                        color = colors.textMuted,
                        fontSize = 18.sp
                    )
                }
                return
            }

    // ============ ViewModel (علاقه‌مندی) ============
    val viewModel: ContentDetailViewModel = viewModel(
        factory = ContentDetailViewModelFactory(
            preferencesManager = PreferencesManager(context),
            contentRepository = ContentRepository(
                apiService = RetrofitClient.apiService,
                contentDao = AppDatabase.getInstance(context)
                    .contentDAO(),
                favoriteDao = AppDatabase.getInstance(context)
                    .favoriteDAO()
            ),
            appDatabase = AppDatabase.getInstance(context)
        )
    )

    LaunchedEffect(poem.id) {
        viewModel.loadFavoriteStatus(poem)
    }
    val favoriteState by viewModel.favoriteState.collectAsState()

    // ============ اندازه فونت ============
    val preferencesManager = remember { PreferencesManager(context) }
    var fontSize by remember {
        mutableFloatStateOf(preferencesManager.contentFontSize)
    }

    fun saveFontSize(size: Float) {
        preferencesManager.contentFontSize = size
        fontSize = size
    }

    // ============ متن نمایشی (پاک‌سازی تگ‌ها) ============
    val displayContent = poem.content.replace(
        "<p>",
        ""
    )
        .replace(
            "</p>",
            ""
        )
        .replace(
            "<br>",
            ""
        )
        .replace(
            "<br/>",
            ""
        )
        .replace(
            "<br />",
            ""
        )
        .trim()

    // ============ اشتراک‌گذاری ============
    val onShare = {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                """
                📖 ${poem.subject}
                ﷺ ${poem.answer}

                $displayContent

                ──────────────
                📱 دفتر مداحی
                """.trimIndent()
            )
            type = "text/plain"
        }
        context.startActivity(
            Intent.createChooser(
                shareIntent,
                "اشتراک‌ گذاری محتوا"
            )
        )
    }

    // ============ UI (همان جزئیات دفترچه، فقط‌خواند) ============
    ContentDetailScreenContent(
        content = poem,
        displayContent = displayContent,
        fontSize = fontSize,
        favoriteState = favoriteState,
        isReadOnly = true,
        onFontSizeChange = { saveFontSize(it) },
        onToggleFavorite = {
            viewModel.toggleFavorite(poem) { }
        },
        onShare = onShare,
        onNavigateBack = onNavigateBack,
        onDeleteClick = { onNavigateBack() })
}
