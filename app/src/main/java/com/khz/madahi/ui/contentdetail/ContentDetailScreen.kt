package com.khz.madahi.ui.contentdetail

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.ContentRepository
import com.khz.madahi.helper.extention.logD
import com.khz.madahi.models.Content
import com.khz.madahi.ui.theme.Gold
import com.khz.madahi.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentDetailScreen(
    content: Content?,
    onNavigateBack: () -> Unit,
    onFavoriteChanged: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val preferencesManager = remember { PreferencesManager(context) }

    // ============ ذخیره و بازیابی اندازه فونت ============
    var fontSize by remember {
        mutableFloatStateOf(preferencesManager.contentFontSize)
    }

    fun saveFontSize(size: Float) {
        preferencesManager.contentFontSize = size
        fontSize = size
    }

    if (content == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "محتوا یافت نشد",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
        return
    }

    // ============ ViewModel ============
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

    // ✅ رفع باگ: قبلاً viewModel.loadFavoriteStatus(content) مستقیم در بدنه‌ی
    // Composable صدا زده می‌شد که یعنی با هر recomposition (مثلاً تغییر fontSize
    // یا هر state دیگری) دوباره اجرا می‌شد و یک کوروتین جدید و غیرضروری لانچ
    // می‌کرد. الان با LaunchedEffect(content.id) فقط زمانی اجرا می‌شود که
    // محتوا واقعاً عوض شده باشد.
    LaunchedEffect(content.id) {
        viewModel.loadFavoriteStatus(content)
    }

    val favoriteState by viewModel.favoriteState.collectAsState()
    val favorite by viewModel.favorite.collectAsState()

    // تشخیص نوع محتوا
    val isNoheh = content.contentType == "0"
    val primaryColor = if (isNoheh) Gold else PrimaryGreen
    val typeText = if (isNoheh) "نوحه" else "روضه"

    // متن نمایشی
    val displayContent = content.content.replace(
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

    LaunchedEffect(favoriteState) {
        logD("favoriteState: $favoriteState")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = content.answer,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = { },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // دکمه اشتراک‌گذاری
                        IconButton(
                            onClick = {
                                val shareIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        """
                                        📖 ${content.subject}
                                        ﷺ ${content.answer}
                                        
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
                            },
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = CircleShape
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "اشتراک‌ گذاری",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(
                            onClick = {
                                if (fontSize > 12f) {
                                    saveFontSize(fontSize - 2f)
                                }
                            },
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = CircleShape
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ZoomOut,
                                contentDescription = "کاهش فونت",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            onClick = {
                                if (fontSize < 30f) {
                                    saveFontSize(fontSize + 2f)
                                }
                            },
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = CircleShape
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ZoomIn,
                                contentDescription = "افزایش فونت",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = CircleShape
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "بازگشت",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.toggleFavorite(content) {
                        onFavoriteChanged()
                    }
                },
                containerColor = if (favoriteState is FavoriteState.Favorite) {
                    Color.Red
                } else {
                    MaterialTheme.colorScheme.primary
                },
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = if (favoriteState is FavoriteState.Favorite) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = if (favoriteState is FavoriteState.Favorite) {
                        "حذف از علاقه‌مندی‌ها"
                    } else {
                        "افزودن به علاقه‌مندی‌ها"
                    },
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                text = displayContent,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = fontSize.sp,
                    lineHeight = (fontSize * 1.8f).sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        }
    }

    // ============ نمایش خطاها ============
    if (favoriteState is FavoriteState.Error) {
        // TODO: نمایش Snackbar خطا
    }
}

// ============ Preview ============
//@Preview(showBackground = true)
//@Composable
//fun ContentDetailScreenPreview() {
//    MaterialTheme {
//        ContentDetailScreen(
//            content = Content(
//                idContent = 0,
//                id = "1",
//                categoryId = "1",
//                userId = "1",
//                answer = "مجموعه نوحه‌های مناسبتی",
//                content = "<p>متن نمونه برای نمایش در صفحه جزئیات محتوا. این متن برای تست و نمایش ظاهر صفحه استفاده می‌شود.</p>",
//                subject = "نوحه‌های محرم",
//                contentType = "0"
//            ),
//            onNavigateBack = {})
//    }
//}