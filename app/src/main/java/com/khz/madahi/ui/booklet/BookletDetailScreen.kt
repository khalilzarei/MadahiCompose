// ui/booklet/BookletDetailScreen.kt
package com.khz.madahi.ui.booklet

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.GlassTextField
import com.khz.madahi.ui.components.MadahiBackground
import com.khz.madahi.ui.components.TopTitleBar
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.primaryLight
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

// ============================================================
// صفحه جزئیات بخش کتابچه — طراحی هماهنگ با دفترچه + بارگذاری تدریجی
// ------------------------------------------------------------
// - هدر TopTitleBar با عنوان دسته
// - جستجو در شعرها
// - لیست شعرها: اول ۳۰ تا، با رسیدن به انتها ۳۰ تای بعدی
// - کلیک روی هر شعر → ContentDetailScreen
// ============================================================

@Composable
fun BookletDetailScreen(
    sectionId: Int,
    sectionTitle: String,
    onNavigateBack: () -> Unit,
    onNavigateToContent: (Int) -> Unit
) {
    val viewModel: BookletDetailViewModel = viewModel(
        factory = BookletDetailViewModelFactory(
            sectionId,
            sectionTitle
        )
    )

    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val listState = rememberLazyListState()

    val colors = LocalMadahiColors.current

    // ============ تشخیص رسیدن به انتهای لیست ============
    val shouldLoadMore by remember {
        derivedStateOf {
            val info = listState.layoutInfo
            val lastVisible = info.visibleItemsInfo.lastOrNull()?.index
                    ?: -1
            info.totalItemsCount > 0 && lastVisible >= info.totalItemsCount - 5
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadMore()
        }
    }

    MadahiBackground {
        Column(modifier = Modifier.fillMaxSize()) {

            // ============ هدر ============
            TopTitleBar(
                title = sectionTitle,
                subTitle = "",
                onBack = onNavigateBack
            )

            // ============ جستجو ============
            GlassTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                minHeight = 54.dp,
                hint = "جستجو در اشعار...",
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(12.dp))

            // ============ محتوا (فضای باقی‌مانده) ============
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (val state = uiState) {
                    is BookletDetailUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = colors.gold)
                        }
                    }

                    is BookletDetailUiState.Success -> {
                        if (state.items.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "🔍",
                                        fontSize = 44.sp
                                    )
                                    Spacer(Modifier.height(10.dp))
                                    Text(
                                        text = "شعری یافت نشد",
                                        color = colors.textMuted,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(
                                    horizontal = 24.dp,
                                    vertical = 8.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(
                                    items = state.items,
                                    key = { it.id }) { item ->
                                    BookletPoemCard(
                                        item = item,
                                        onClick = { onNavigateToContent(item.id) })
                                }

                                // ============ لودینگ صفحه بعد ============
                                if (state.isLoadingMore) {
                                    item(key = "loading_more") {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                color = colors.gold,
                                                modifier = Modifier.size(28.dp),
                                                strokeWidth = 3.dp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    is BookletDetailUiState.Error   -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "⚠️ ${state.message}",
                                    color = colors.textMuted,
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(16.dp))
                                GlassCard3D(modifier = Modifier
                                    .clickable { viewModel.loadItems() }
                                    .padding(horizontal = 32.dp)) {
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
// کارت هر شعر کتابچه — هماهنگ با ContentItem دفترچه
// ============================================================

@Composable
private fun BookletPoemCard(
    item: BookletItem,
    onClick: () -> Unit
) {
    val colors = LocalMadahiColors.current

    // سبک • ناشر
    val metaLine = listOf(
        item.style,
        item.publisherName
    ).filter { it.isNotBlank() }
        .joinToString(" • ")

    GlassCard3D {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .height(80.dp)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically) {
            // عنوان + سبک/ناشر
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (metaLine.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = metaLine,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.primaryLight,
                        maxLines = 1,
                        fontSize = 12.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "مشاهده شعر",
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(34.dp)
            )
        }
    }
}
